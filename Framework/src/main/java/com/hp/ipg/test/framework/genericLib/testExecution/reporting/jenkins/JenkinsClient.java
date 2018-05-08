package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.ITestResult;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JenkinsClient {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JenkinsClient.class);
    private static ObjectMapper om = new ObjectMapper();

    @Value("${jenkins.user:Get_the_value}")
    private String userName;
    @Value("${jenkins.apiKey:Get_The_Value}")
    private String apiKey;
    private Credentials apiCredentials;
    private static final int SENDREQUEST_POLL_INTERVAL_MS = 1000;

    @PostConstruct
    void init() {
        apiCredentials = (Credentials) new UsernamePasswordCredentials(userName, apiKey);
    }

    public View getView(URL viewUrl) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return sendGet(viewUrl, View.class);
    }

    public Job getJob(URL jobUrl) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return sendGet(jobUrl, Job.class);
    }

    public Build getBuild(URL buildURL) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return sendGet(buildURL, Build.class);
    }

    public Build getBuild(URL jobURL, int buildNumber) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return sendGet(new URL(jobURL.toString() + buildNumber + "/"), Build.class);
    }

    public List<JenkinsResult> getTestResults(URL jobUrl, ITestResult result, int startBuildNum) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return getTestResults(jobUrl, result, startBuildNum, 10);
    }

    public List<JenkinsResult> getTestResults(URL jobUrl, ITestResult result, int startBuildNum, int totalBuilds) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        List<JenkinsResult> results = new ArrayList<JenkinsResult>();
        int curBuild = startBuildNum;
        int totalResults = 0;
        while (totalResults < totalBuilds) {
            String fullClassName = result.getTestClass().getName();

            //test result URL example:
            //	https://moonstone-ci.ops.rd.adapps.hp.com/job/portal/8339/testngreports/com.hp.ipg.tests.titan.companyAdmin/AppManagment/PullPrintAppDefault/
            URL url = new URL(String.format("%s%s/testngreports/%s/%s/%s/",
                    jobUrl.toString(),
                    curBuild,
                    fullClassName.substring(0, fullClassName.lastIndexOf('.')),
                    fullClassName.substring(fullClassName.lastIndexOf('.') + 1),
                    result.getMethod().getMethodName()));

            try {
                JenkinsResult r = sendGet(url, JenkinsResult.class);
                r.setUrl(url);
                results.add(r);
            } catch (RuntimeException e) {
            }

            totalResults++;
            curBuild--;
            if (curBuild == 0) {
                break;
            }
        }

        return results;
    }

    public List<Build> getBuilds(URL jobUrl) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        List<Build> builds = new ArrayList<Build>();
        Job job = getJob(jobUrl);

        for (BuildHeader bh : job.getBuilds()) {
            builds.add(getBuild(bh.getUrl()));
        }

        return Lists.reverse(builds);
    }

    public int buildJob(URL jobUrl, String... params) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        Job job = getJob(jobUrl);
        int buildNumber = job.getNextBuildNumber();
        String jobToExecute = jobUrl + "build";
        if (params.length > 0) {
            jobToExecute = jobUrl + "buildWithParameters?" + Joiner.on('&').join(params);
        }
        LOGGER.info("Executing jenkins job: " + jobToExecute);
        sendPost(URI.create(jobToExecute));
        return buildNumber;
    }

    private void sendPost(URI uri) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        HttpPost postMethod = new HttpPost(uri.toString());
        sendRequest(postMethod, 201);
    }

    private <T> T sendGet(URL baseUrl, Class<T> clazz) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        URL restUrl = new URL(baseUrl, "api/json");
        HttpGet reportGetter = new HttpGet(restUrl.toString());
        String response = sendRequest(reportGetter, 200);
        return (T) om.readValue(response, clazz);
    }

    private String sendRequest(HttpUriRequest method, int expectedResponseCode) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        URI uri = method.getURI();
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), apiCredentials);
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);

        //Build client that trusts all certs
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf)
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        method.addHeader("accept", MediaType.APPLICATION_JSON);

        int responseCode = 0;
        int retryCount = 0;
        StringBuilder sb = new StringBuilder();
        while (expectedResponseCode != responseCode && retryCount <= 3) {
            LOGGER.debug("Calling [" + method.getMethod() + "] on '" + uri + "' (attempt: " + retryCount + ")...");
            try (CloseableHttpResponse response = httpclient.execute(host, method, localContext)) {
                responseCode = response.getStatusLine().getStatusCode();
                if (responseCode != expectedResponseCode) {
                    retryCount++;
                    try {
                        Thread.sleep(SENDREQUEST_POLL_INTERVAL_MS);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                HttpEntity entity = response.getEntity();
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                EntityUtils.consume(entity);
            }
        }
        if (retryCount >= 3) {
            throw new RuntimeException("Expected a " + expectedResponseCode + " response from Jenkins API but found [" + responseCode + "] after '" + retryCount + "' retry attempts. (URL:" + uri + ")");
        }
        return sb.toString();
    }
}


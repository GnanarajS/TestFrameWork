package com.hp.ipg.test.framework.genericLib.testExecution.reporting;


import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.hp.ipg.test.framework.genericLib.testExecution.TestSuiteBase;
import com.hp.ipg.test.framework.genericLib.testExecution.utils.BuildUtils;
import org.slf4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JiraReportingClient implements IReporter,ISuiteListener {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JiraReportingClient.class);
    private ArrayList<String> filteredTestsToReport;
    private List<ISuite> testSuites;

    private JiraReportingConfiguration jiraReportingConfiguration;
    private JiraRestClient jiraRestClient;
    private long issueTypeId =  10205;
    private File restAssuredLogFile;



    @Override
    public void onStart(ISuite iSuite) {
        jiraReportingConfiguration = new JiraReportingConfiguration();

        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

        try {
            URI jiraServerUri = new URI(jiraReportingConfiguration.getServerAddress());
            jiraRestClient = factory.createWithBasicHttpAuthentication(jiraServerUri,jiraReportingConfiguration.getAccountEmail(),jiraReportingConfiguration.getAccountPassword());
        } catch (URISyntaxException e) {
            LOGGER.error("Encountered error while forming server URI:", e);
        }


        if(jiraReportingConfiguration.getFilterTestList() != null) {
            filteredTestsToReport = new ArrayList(Arrays.asList(jiraReportingConfiguration.getFilterTestList().split(",")));
            LOGGER.info("Tests to report to PON Jira" + "=" + jiraReportingConfiguration.getFilterTestList());
        }
    }

    @Override
    public void generateReport(List<XmlSuite> list, List<ISuite> suites, String s) {
        this.testSuites = suites;
        if(filteredTestsToReport!= null && (jiraReportingConfiguration.getCreatePONJiraDefect())) {
            fetchFailedTestCases();
        }
    }

    private void fetchFailedTestCases() {
        for(ISuite suite: testSuites) {
            Map<String, ISuiteResult> suiteResult = suite.getResults();
            for(ISuiteResult result: suiteResult.values()) {
                filterTestCases(result.getTestContext().getFailedTests());
            }
        }
    }

    private void filterTestCases(IResultMap resultMap) {

        for(ITestResult result : resultMap.getAllResults()) {

            ITestNGMethod testMethod = result.getMethod();

            if(shouldCreateDefectForTestFailure(testMethod)) {
                createDefect(result,testMethod);
            }
        }
    }

    private void createDefect(ITestResult result, ITestNGMethod testMethod) {

        Throwable cause = result.getThrowable();

        if(cause!= null) {

            LOGGER.info("Cause for "+testMethod.getMethodName()+ " is: "+cause.getMessage());

            //Read restassured log file
            this.readLogsFile();


            //Creating a new defect
            IssueInputBuilder issueInputBuilder = new IssueInputBuilder(jiraReportingConfiguration.getProjectKey(),issueTypeId);
            issueInputBuilder.setSummary("[AutoCreated] " + testMethod.getMethodName() + " failed");

            issueInputBuilder.setDescription("Failure Reason:" + "\n" +
                        cause.getMessage());

            IssueInput issueInput = issueInputBuilder.build();
            Promise<BasicIssue> basicIssuePromise =  jiraRestClient.getIssueClient().createIssue(issueInput);
            String issueKey = basicIssuePromise.claim().getKey();

            this.addAttachmentToIssue(issueKey);

        }
    }


    private boolean shouldCreateDefectForTestFailure(ITestNGMethod testMethod) {

        boolean createDefectForTestFailure = false;

        ArrayList<String> testGroups = new ArrayList(Arrays.asList(testMethod.getGroups()));

        for (String filteredTest : filteredTestsToReport) {
            if (testGroups.contains(filteredTest)) {
                if(!issueAlreadyCreated(testMethod)) {
                    createDefectForTestFailure = true;
                    break;
                }
            }
        }
        return createDefectForTestFailure;
    }

    private boolean issueAlreadyCreated(ITestNGMethod testMethod) {

        boolean isIssueAlreadyCreated = false;
        String searchJqlQuery = "project=WEPATA AND status=IceBox AND issuetype=Bug";

        Promise<SearchResult> searchJql = jiraRestClient.getSearchClient().searchJql(searchJqlQuery);

        for (Issue issue : searchJql.claim().getIssues()) {

            if(issue.getSummary().contains(testMethod.getMethodName())){

                isIssueAlreadyCreated = true;

                break;
            }
        }
        
        LOGGER.info("Issue already created----->"+isIssueAlreadyCreated);

        return isIssueAlreadyCreated;

    }

    private void readLogsFile() {
        restAssuredLogFile = new File(BuildUtils.getTestOutputDirectory(), TestSuiteBase.restAssuredLogFile);
    }

    private void addAttachmentToIssue(String issueKey){
        Promise<Issue> createdIssue = jiraRestClient.getIssueClient().getIssue(issueKey);
        jiraRestClient.getIssueClient().addAttachments(createdIssue.claim().getAttachmentsUri(),restAssuredLogFile);
    }


    @Override
    public void onFinish(ISuite iSuite) {

    }
}

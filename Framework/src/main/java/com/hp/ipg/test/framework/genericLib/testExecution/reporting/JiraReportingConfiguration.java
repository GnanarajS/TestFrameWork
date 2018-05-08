package com.hp.ipg.test.framework.genericLib.testExecution.reporting;

public class JiraReportingConfiguration {

    private String filterTestList;
    private Boolean createPONJiraDefect;
    private String accountEmail = "WEPA_API";
    private String accountPassword = "Q54cb&pvhDN^#&h7";
    private String serverAddress = "https://jira.cso-hp.com";
    private String projectKey;



    public JiraReportingConfiguration() {

        String property = System.getProperty("filterTestList");
        if(property!= null && !property.isEmpty()) {
            this.filterTestList = property;
        }

        property = System.getProperty("createDefectInPONJira");
        if(property!= null && !property.isEmpty()) {
            this.createPONJiraDefect = Boolean.valueOf(property);
        }

        property = System.getProperty("projectKey");
        if(property!= null && !property.isEmpty()){
            this.projectKey = property;
        }
    }

    public String getFilterTestList() {
        return filterTestList;
    }

    public Boolean getCreatePONJiraDefect() {
        return createPONJiraDefect;
    }

    public String getAccountEmail() { return accountEmail; }

    public String getAccountPassword() { return accountPassword; }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getProjectKey() {
        return projectKey;
    }


}

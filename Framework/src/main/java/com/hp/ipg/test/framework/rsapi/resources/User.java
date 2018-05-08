package com.hp.ipg.test.framework.rsapi.resources;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class User extends HateoasResource {

    private enum Property {
        name,
        familyName,
        givenName,
        userName,
        password,
        emails
    }

    private enum EmailsProperty {
        value,
        primary
    }

    public static User getInstance() throws IOException {
        User user = (User) ResourceBase.loadTemplate(ResourceName.USER, ApiVersion.DEFAULT, User.class);
        user.setUserName(generateEmail());
        user.setPassword(DEFAULT_PASSWORD);
        user.setGivenName(generateEmail());
        user.setFamilyName(generateEmail());
        Emails emails = new Emails();
        emails.setPrimary("true");
        emails.setValue(generateEmail());
        List<Emails> emailsList = new ArrayList<>();
        emailsList.add(emails);
        user.setEmails(emailsList);
        return user;
    }

    public static String generateEmail() {
        String email = "Roam-User" + UUID.randomUUID() + "@mail.hpfoghorn.com";
        return email;
    }

    public String getSchemaPath() {
        return ResourceBase.getSchemaPath(ResourceName.USER, ApiVersion.DEFAULT);
    }

    private User() {
    }

    public void setFamilyName(String familyName) {
        HashMap<String, Object> name = (HashMap<String, Object>) this.get(Property.name.toString());
        name.put(Property.familyName.toString(), familyName);
    }

    public void setGivenName(String givenName) {
        HashMap<String, Object> name = (HashMap<String, Object>) this.get(Property.name.toString());
        name.put(Property.givenName.toString(), givenName);
    }

    public String getFamilyName() {
        HashMap<String, Object> name = (HashMap<String, Object>) this.get(Property.name.toString());
        return (String) name.get(Property.familyName.toString());
    }

    public String getGivenName() {
        HashMap<String, Object> name = (HashMap<String, Object>) this.get(Property.name.toString());
        return (String) name.get(Property.givenName.toString());
    }

    public void setUserName(String userName) {
        this.put(Property.userName.toString(), userName);
    }

    public String getUserName() {
        return (String) this.get(Property.userName.toString());
    }

    public void setPassword(String password) {
        this.put(Property.password.toString(), password);
    }

    public String getPassword() {
        return (String) this.get(Property.password.toString());
    }

    public Emails[] getEmails() {
        ArrayList<Emails> emails = (ArrayList<Emails>) get(Property.emails.toString());
        return (Emails[]) emails.toArray(new Emails[emails.size()]);
    }

    public void setEmails(List<Emails> emails) {
        put(Property.emails.toString(), emails);
    }

    public static class Emails extends HateoasResource {

        public String getValue() {
            return (String) this.get(EmailsProperty.value.toString());
        }

        public void setValue(String value) {
            this.put(EmailsProperty.value.toString(), value);
        }
        public void setPrimary(String value) {
            this.put(EmailsProperty.primary.toString(), value);
        }

    }

}

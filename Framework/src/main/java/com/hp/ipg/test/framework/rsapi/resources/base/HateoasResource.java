package com.hp.ipg.test.framework.rsapi.resources.base;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import java.nio.file.Paths;
import java.util.*;

/**
 * Hateoas resource model. Supports self describing links container
 */
public class HateoasResource extends ResourceBase {

    public static final String LINKS = "links";
    public static final String LINK_SELF = "self";
    public static final String LINK_REL = "rel";
    public static final String LINK_HREF = "href";

    /**
     * Method gets the id of current resource from the hateoas self ref link
     *
     * @return
     */
    public UUID getId() {
        return this.getLinkUuid(LINK_SELF);
    }

    /**
     * Method gets the full href url from the hateoas self ref link
     *
     * @return
     */
    public String getIdHref() {
        return this.getLinkHref(LINK_SELF);
    }

    /**
     * Method parses out the id from any hateoas ref link
     *
     * @param linkRel - link to parse
     * @return - UUID of parsed hateoas ref link
     */
    public UUID getLinkUuid(String linkRel) {
        String href = this.getLinkHref(linkRel);
        if (href != null) {
            String uuid = href.substring(href.lastIndexOf("/") + 1);
            return UUID.fromString(uuid);
        } else {
            return null;
        }
    }

    public UUID getUserId() {
        if (this.containsKey("id")) {
            String userId = this.get("id").toString();
            return UUID.fromString(userId);
        } else {
            return null;
        }
    }

    public String getUserNameFromResponse() {
        if (this.containsKey("userName")) {
            String userName = this.get("userName").toString();
            return userName;
        } else {
            return null;
        }

    }

    private String fetchUserNameFromResponse() {
        if (this.containsKey("responseMessage")) {
            String responseMessage = this.get("responseMessage").toString();
            Map<String, String> responseMessageMap = splitToMap(responseMessage);
            if (!responseMessageMap.isEmpty() && responseMessageMap.containsKey("\"userName\"")) {
                return responseMessageMap.get("\"userName\"");
            }
        }
        return null;
    }

    public String getUserIdFromResponse(ValidatableResponse response) {
        String attribute = response.extract().body().jsonPath().get("responseMessage");
        String value = attribute.substring(attribute.indexOf("Resources"));
        Map<String, String> responseMessageMap = splitToMap(value);
        String userId = responseMessageMap.get("\"id\"");
        userId = userId.replace("\"", "");
        return userId;
    }

    /**
     * Method gets the full href url for the specified hateos ref link
     *
     * @param linkRel - link to retrieve
     * @return - href associated with specified link ref
     */
    public String getLinkHref(String linkRel) {
        if (this.containsKey(LINKS)) {
            List links = (List) this.get(LINKS);
            Iterator var4 = links.iterator();

            while (var4.hasNext()) {
                Map map = (Map) var4.next();
                if (linkRel.equals(map.get(LINK_REL))) {
                    return map.get(LINK_HREF).toString();
                }
            }
        }

        return null;
    }

    public String getLocationLink() {
        if (this.containsKey("headerParamMap")) {
            Map headerParamMap = (Map) this.get("headerParamMap");

            while (!headerParamMap.isEmpty()) {
                return headerParamMap.get("Location").toString();
            }
        }
        return null;
    }

    /**
     * Method add a hateoas self describing link
     *
     * @param linkRelName - relationship descriptor
     * @param href        - full href url
     */
    public void addLink(String linkRelName, String href) {
        if (!this.containsKey(LINKS)) {
            this.put(LINKS, new ArrayList());
        }

        ArrayList links = (ArrayList) this.get(LINKS);
        if (!links.contains(linkRelName)) {
            HashMap link = new HashMap();
            link.put(LINK_REL, linkRelName);
            link.put(LINK_HREF, href);
            links.add(link);
        }
    }

    /**
     * Method add a hateoas self describing link
     *
     * @param linkRelName - relationship descriptor
     * @param path        - endpoint subpath
     * @param id          - id of resource
     */
    public void addLink(String linkRelName, String path, UUID id) {
        addLink(linkRelName, RestAssured.baseURI + Paths.get(path, id.toString()).toString());
    }
}
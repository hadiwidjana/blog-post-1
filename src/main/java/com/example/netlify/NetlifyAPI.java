package com.example.netlify;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.allure.AllureUtils.*;
import static io.restassured.RestAssured.given;

public class NetlifyAPI {

    private static final String NETLIFY_BEARER = "5M0Aa4WiZRZhVv93mlXCuzs6J3qY9rf-OCOGLX5lEJc";
    public static String ALLURE_URL;
    private static List<String> siteIds;

    public static void DeployAllure(boolean deleteReport, boolean deleteResult) throws Exception {
        generateReport(); //generate allure-report
        Thread.sleep(5000);
        zipReport(); // zip it to allure-report.zip

        RestAssured.baseURI = "https://api.netlify.com";
        File file = new File("allure-report.zip");
        Response res = given().headers(
                        "Authorization",
                        "Bearer " + NETLIFY_BEARER,
                        "Content-Type",
                        "application/zip").
                body(file).
                when().
                post("/api/v1/sites").
                then().
                assertThat().statusCode(201).extract().response();

        //get url from response body
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(res.getBody().prettyPrint());
        ALLURE_URL= (String) jsonResponse.get("url"); //here is the URL

        //clean up
        delZipReport(); //always delete the zip
        if (deleteReport) delAllureReport(); //conditional delete report
        if (deleteResult) delAllureResult(); //conditional delete result
    }

    private static void getSites() throws ParseException {
        RestAssured.baseURI = "https://api.netlify.com";
        Response res = given().headers(
                        "Authorization",
                        "Bearer " + NETLIFY_BEARER).
                when().
                get("/api/v1/sites").
                then().
                assertThat().statusCode(200).extract().response();
        JSONParser parser = new JSONParser();
        JSONArray jsonResponse = (JSONArray) parser.parse(res.getBody().asString());
        siteIds = new ArrayList<String>();
        for (int i = 0; i < jsonResponse.size(); i++) {
            JSONObject siteData = (JSONObject) jsonResponse.get(i);
            siteIds.add((String) siteData.get("id"));
        }
    }

    public static void deleteAllSites() throws ParseException {
        getSites();
        for (int i = 0; i < siteIds.size(); i++) {
            RestAssured.baseURI = "https://api.netlify.com";
            Response res = given().headers(
                            "Authorization",
                            "Bearer " + NETLIFY_BEARER).
                    when().
                    delete("/api/v1/sites/" + siteIds.get(i)).
                    then().
                    assertThat().statusCode(204).extract().response();
        }
        System.out.println("All sites has been deleted");
    }

    public static void main(String[] args) throws Exception {
        deleteAllSites();
    }
}

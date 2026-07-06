package testbase;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Data;
import models.Booking;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;
import utilities.Configuration;

@Data
public class TestBase {

    public static ThreadLocal<RequestSpecBuilder> requestSpecBuilderThreadLocal = new ThreadLocal<RequestSpecBuilder>();
    public Configuration configuration;
    public ThreadLocal<Configuration> configurationThreadLocal = new ThreadLocal<Configuration>();
    public RequestSpecBuilder requestSpecBuilder;
    public SoftAssert softAssert = new SoftAssert();

    public TestBase() {
        if (configurationThreadLocal.get() == null) {
            configuration = new Configuration(this);
            configurationThreadLocal.set(configuration);
        } else {
            configuration = configurationThreadLocal.get();
        }

        if (requestSpecBuilderThreadLocal.get() == null) {
            requestSpecBuilder = new RequestSpecBuilder().addFilter(new AllureRestAssured());
            requestSpecBuilderThreadLocal.set(requestSpecBuilder);
        } else {
            requestSpecBuilder = requestSpecBuilderThreadLocal.get();
        }
    }

    @Step("Set Base URI")
    public void setBaseURI(String baseURI) {
        requestSpecBuilder = requestSpecBuilder.setBaseUri(baseURI);
    }

    @Step("Set Base Path")
    public void setBasePath(String basePath) {
        requestSpecBuilder = requestSpecBuilder.setBasePath(basePath);
    }

    @Step("Set Content Type")
    public void setContentType(ContentType contentType) {
        requestSpecBuilder = requestSpecBuilder.setContentType(contentType);
    }

    @Step("Set Accept")
    public void setAccept(ContentType contentType) {
        requestSpecBuilder = requestSpecBuilder.setAccept(ContentType.JSON);
    }

    @Step("Set Accept")
    public void setAccept(String contentType) {
        requestSpecBuilder = requestSpecBuilder.setAccept(contentType);
    }

    @Step("Set Header")
    public void setHeader(String headerName, String headerValue) {
        requestSpecBuilder = requestSpecBuilder.addHeader(headerName, headerValue);
    }

    @Step("Set Cookie")
    public void setCookie(String cookieKey, String cookieValue, Object... cookieNameValuePairs) {
        requestSpecBuilder = requestSpecBuilder.addCookie(cookieKey, cookieValue, cookieNameValuePairs);
    }

    @Step("Set Request Body")
    public void setRequestBody(String requestBody) {
        requestSpecBuilder = requestSpecBuilder.setBody(requestBody);
    }

    @Step("Send GET Request")
    public Response get() {
        Response response = RestAssured.given()
                .spec(requestSpecBuilder.build())
                .get();
        requestSpecBuilder = null;
        requestSpecBuilderThreadLocal.remove();
        return response;
    }

    @Step("Send POST Request")
    public Response post() {
        Response response = RestAssured.given()
                .spec(requestSpecBuilder.build())
                .post();
        requestSpecBuilder = null;
        requestSpecBuilderThreadLocal.remove();
        return response;
    }

    @Step("Send PUT Request")
    public Response put() {
        Response response = RestAssured.given()
                .spec(requestSpecBuilder.build())
                .put();
        requestSpecBuilder = null;
        requestSpecBuilderThreadLocal.remove();
        return response;
    }

    @Step("Send PATCH Request")
    public Response patch() {
        Response response = RestAssured.given()
                .spec(requestSpecBuilder.build())
                .patch();
        requestSpecBuilder = null;
        requestSpecBuilderThreadLocal.remove();
        return response;
    }

    @Step("Assert Strings are Equal")
    public void assertStringsEquals(AssertionType assertionType, String actualString, String expectedString) {
        if (assertionType.equals(AssertionType.SOFT)) {
            softAssert.assertEquals(actualString, expectedString);
        } else if (assertionType.equals(AssertionType.HARD)) {
            Assert.assertEquals(actualString, expectedString);
        } else {
            String failureMessage = "Invalid Assertion Type provided. Please provide either SOFT or HARD as the assertion type.";
            utilities.Step.step(failureMessage);
            Assert.fail(failureMessage);
        }
    }

    @Step("Assert Booleans are Equal")
    public void assertBooleansEquals(AssertionType assertionType, Boolean actualBoolean, Boolean expectedBoolean) {
        if (assertionType.equals(AssertionType.SOFT)) {
            softAssert.assertEquals(actualBoolean, expectedBoolean);
        } else if (assertionType.equals(AssertionType.HARD)) {
            Assert.assertEquals(actualBoolean, expectedBoolean);
        } else {
            String failureMessage = "Invalid Assertion Type provided. Please provide either SOFT or HARD as the assertion type.";
            utilities.Step.step(failureMessage);
            Assert.fail(failureMessage);
        }
    }

    @Step("Assert Strings are Equal")
    public void assertBookingEquals(AssertionType assertionType, Booking actualBooking, Booking expectedBooking) {
        if (assertionType.equals(AssertionType.SOFT)) {
            softAssert.assertEquals(actualBooking, expectedBooking);
        } else if (assertionType.equals(AssertionType.HARD)) {
            Assert.assertEquals(actualBooking, expectedBooking);
        } else {
            String failureMessage = "Invalid Assertion Type provided. Please provide either SOFT or HARD as the assertion type.";
            utilities.Step.step(failureMessage);
            Assert.fail(failureMessage);
        }
    }

    @Step("Asserting all Soft Assertions")
    public void assertSoftAssertions() {
        softAssert.assertAll();
    }

    public enum AssertionType {SOFT, HARD}

}

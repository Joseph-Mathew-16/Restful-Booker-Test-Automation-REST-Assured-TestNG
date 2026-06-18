package endpoints;

import io.restassured.http.ContentType;
import testbase.RestfulBookerBase;

public class BookingEndpoint extends RestfulBookerBase {

    public static String endpoint = "/booking";

    public BookingEndpoint() {
        setBaseURI(configuration.url);
        setBasePath(endpoint);
    }

    public BookingEndpoint setBookingId(String bookingId) {
        setBasePath(endpoint + "/" + bookingId);
        return this;
    }

    public BookingEndpoint setContentTypeForBookingEndpoint(ContentType contentType) {
        setContentType(contentType);
        return this;
    }

    public BookingEndpoint setAcceptForBookingEndpoint(ContentType contentType) {
        setAccept(contentType);
        return this;
    }

    public BookingEndpoint setAcceptForBookingEndpoint(String contentType) {
        setAccept(contentType);
        return this;
    }

    public BookingEndpoint setIfMatchHeader(String etag) {
        setHeader("If-Match", etag);
        return this;
    }

    public BookingEndpoint setAuthorizationHeader(String authorizationToken) {
        setHeader("Cookie", "token=" + authorizationToken);
        return this;
    }

    public BookingEndpoint setAuthorizationCookie(String authorizationToken) {
        setCookie("token", authorizationToken);
        return this;
    }

    public BookingEndpoint setBookingDetailsInBody(String firstName, String lastName, String totalPrice, String depositPaid, String checkIn, String checkOut, String additionalNeeds) {
        int totalPriceInt = Integer.parseInt(totalPrice);
        Boolean depositPaidBoolean = Boolean.parseBoolean(depositPaid);
        String requestBody = "{\"firstname\":\"" + firstName + "\",\"lastname\":\"" + lastName + "\",\"totalprice\":" + totalPriceInt + ",\"depositpaid\":" + depositPaidBoolean + ",\"bookingdates\":{\"checkin\":\"" + checkIn + "\",\"checkout\":\"" + checkOut + "\"},\"additionalneeds\":\"" + additionalNeeds + "\"}";
        setRequestBody(requestBody);
        return this;
    }

    public BookingEndpoint setBookingDetailsWithFirstNameOnlyInBody(String firstName) {
        String requestBody = "{\"firstname\":\"" + firstName + "\"}";
        setRequestBody(requestBody);
        return this;
    }

}

package endpoints;

import io.restassured.http.ContentType;
import models.Booking;
import models.BookingDates;
import testbase.RestfulBookerBase;

public class BookingEndpoint extends RestfulBookerBase {

    public static final String endpoint = "/booking";

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

    public BookingEndpoint setAcceptForBookingEndpoint(String contentType) {
        setAccept(contentType);
        return this;
    }

    public BookingEndpoint setAuthorizationCookie(String authorizationToken) {
        setCookie("token", authorizationToken);
        return this;
    }

    public BookingEndpoint setBookingDetailsInBody(String firstName, String lastName, String totalPrice, String depositPaid, String checkIn, String checkOut, String additionalNeeds) {
        int totalPriceInt = Integer.parseInt(totalPrice);
        Boolean depositPaidBoolean = Boolean.parseBoolean(depositPaid);

        BookingDates bookingDates = BookingDates.builder().checkin(checkIn).checkout(checkOut).build();
        Booking booking = Booking.builder().firstname(firstName).lastname(lastName).totalprice(totalPriceInt).depositpaid(depositPaidBoolean).bookingdates(bookingDates).additionalneeds(additionalNeeds).build();

        String requestBody = booking.writeJson();
        setRequestBody(requestBody);
        return this;
    }

}

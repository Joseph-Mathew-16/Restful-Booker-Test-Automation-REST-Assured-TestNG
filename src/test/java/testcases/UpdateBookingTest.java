package testcases;

import endpoints.AuthEndpoint;
import endpoints.BookingEndpoint;
import io.qameta.allure.Allure;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Name;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testbase.RestfulBookerBase;

import static io.qameta.allure.SeverityLevel.NORMAL;

public class UpdateBookingTest extends RestfulBookerBase {

    @DataProvider(name = "CSV Data Reader")
    public Object[][] dataProvider() {
        String folderPath = "Update_Booking_Cases";
        String fileName = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1) + ".csv";
        return csvReader(folderPath, fileName);
    }

    @Test(dataProvider = "CSV Data Reader", description = "This test creates & updates a booking request.")
    @Name("Update Booking Test")
    @Severity(NORMAL)
    @Owner("Joseph Mathew")
    public void updateBookingTest(String firstName, String lastName, String totalPrice, String depositPaid, String checkIn, String checkOut, String additionalNeeds, String username, String password, String updatedFirstName, String updatedLastName, String updatedTotalPrice, String updatedDepositPaid, String updatedCheckIn, String updatedCheckOut, String updatedAdditionalNeeds) {

        BookingEndpoint postBookingEndpoint = new BookingEndpoint();

        Allure.step("Building POST request", () -> {
            postBookingEndpoint.setContentTypeForBookingEndpoint(ContentType.JSON).setBookingDetailsInBody(firstName, lastName, totalPrice, depositPaid, checkIn, checkOut, additionalNeeds);
        });
        Response bookingResponse = postBookingEndpoint.post();

        String bookingId = bookingResponse.body().jsonPath().getString("bookingid");
        Allure.step("Verifying values in response of POST request", () -> {
                    assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.firstname"), firstName);
                    assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.lastname"), lastName);
                    assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.totalprice"), String.valueOf(totalPrice));
                    assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.depositpaid"), String.valueOf(depositPaid));
                    assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.bookingdates.checkin"), checkIn);
                    assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.bookingdates.checkout"), checkOut);
                    assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.additionalneeds"), additionalNeeds);
                }
        );

        /*assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.firstname"), firstName);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.lastname"), lastName);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.totalprice"), String.valueOf(totalPrice));
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.depositpaid"), String.valueOf(depositPaid));
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.bookingdates.checkin"), checkIn);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.bookingdates.checkout"), checkOut);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.additionalneeds"), additionalNeeds);*/

        BookingEndpoint getBookingEndpoint = new BookingEndpoint();

        Allure.step("Building GET request", () -> {
            getBookingEndpoint.setBookingId(bookingId);
                }
        );
        Response getBookingResponseBody = getBookingEndpoint.get();

        Allure.step("Verifying values in response of GET request", () -> {
            assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("firstname"), firstName);
            assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("lastname"), lastName);
            assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("totalprice"), String.valueOf(totalPrice));
            assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("depositpaid"), String.valueOf(depositPaid));
            assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("bookingdates.checkin"), checkIn);
            assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("bookingdates.checkout"), checkOut);
            assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("additionalneeds"), additionalNeeds);
        });

        AuthEndpoint authEndpoint = new AuthEndpoint();
        Allure.step("Building POST request", () -> {
            authEndpoint.setContentTypeForAuthEndpoint(ContentType.JSON).setUsernameAndPassword(username, password);
        });
        Response authResponse = authEndpoint.post();

        String authToken = authResponse.body().jsonPath().getString("token");

        BookingEndpoint updatedBookingEndpoint = new BookingEndpoint();
        Allure.step("Building PUT request", () -> {
            updatedBookingEndpoint.setBookingId(bookingId)
                    .setContentTypeForBookingEndpoint(ContentType.JSON)
                    .setAcceptForBookingEndpoint("application/json")
                    .setAuthorizationHeader(authToken)
                    .setBookingDetailsInBody(updatedFirstName, updatedLastName, updatedTotalPrice, updatedDepositPaid, updatedCheckIn, updatedCheckOut, updatedAdditionalNeeds);
        });
        Response updatedBookingResponse = updatedBookingEndpoint.put();

        Allure.step("Verifying values in response of PUT request", () -> {
            assertStringsEquals(AssertionType.SOFT, updatedBookingResponse.body().jsonPath().getString("firstname"), updatedFirstName);
            assertStringsEquals(AssertionType.SOFT, updatedBookingResponse.body().jsonPath().getString("lastname"), updatedLastName);
            assertStringsEquals(AssertionType.SOFT, updatedBookingResponse.body().jsonPath().getString("totalprice"), String.valueOf(updatedTotalPrice));
            assertStringsEquals(AssertionType.SOFT, updatedBookingResponse.body().jsonPath().getString("depositpaid"), String.valueOf(updatedDepositPaid));
            assertStringsEquals(AssertionType.SOFT, updatedBookingResponse.body().jsonPath().getString("bookingdates.checkin"), updatedCheckIn);
            assertStringsEquals(AssertionType.SOFT, updatedBookingResponse.body().jsonPath().getString("bookingdates.checkout"), updatedCheckOut);
            assertStringsEquals(AssertionType.SOFT, updatedBookingResponse.body().jsonPath().getString("additionalneeds"), updatedAdditionalNeeds);
        });

        assertSoftAssertions();
    }
}
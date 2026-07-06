package testcases;

import endpoints.AuthEndpoint;
import endpoints.BookingEndpoint;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Name;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testbase.RestfulBookerBase;
import utilities.Step;

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

        String bookingId = Step.step("Booking POST request", () -> {

            Response postBookingResponse = new BookingEndpoint().setContentTypeForBookingEndpoint(ContentType.JSON).setBookingDetailsInBody(firstName, lastName, totalPrice, depositPaid, checkIn, checkOut, additionalNeeds).post();

            Step.step("Verifying values in response of POST request", () -> {
                assertStringsEquals(AssertionType.SOFT, postBookingResponse.body().jsonPath().getString("booking.firstname"), firstName);
                assertStringsEquals(AssertionType.SOFT, postBookingResponse.body().jsonPath().getString("booking.lastname"), lastName);
                assertStringsEquals(AssertionType.SOFT, postBookingResponse.body().jsonPath().getString("booking.totalprice"), String.valueOf(totalPrice));
                assertStringsEquals(AssertionType.SOFT, postBookingResponse.body().jsonPath().getString("booking.depositpaid"), String.valueOf(depositPaid));
                assertStringsEquals(AssertionType.SOFT, postBookingResponse.body().jsonPath().getString("booking.bookingdates.checkin"), checkIn);
                assertStringsEquals(AssertionType.SOFT, postBookingResponse.body().jsonPath().getString("booking.bookingdates.checkout"), checkOut);
                assertStringsEquals(AssertionType.SOFT, postBookingResponse.body().jsonPath().getString("booking.additionalneeds"), additionalNeeds);
            });

            return postBookingResponse.body().jsonPath().getString("bookingid");

        });

        Step.step("Booking GET request", () -> {
            Response getBookingResponse = new BookingEndpoint().setBookingId(bookingId).get();

            Step.step("Verifying values in response of GET request", () -> {
                assertStringsEquals(AssertionType.SOFT, getBookingResponse.body().jsonPath().getString("firstname"), firstName);
                assertStringsEquals(AssertionType.SOFT, getBookingResponse.body().jsonPath().getString("lastname"), lastName);
                assertStringsEquals(AssertionType.SOFT, getBookingResponse.body().jsonPath().getString("totalprice"), String.valueOf(totalPrice));
                assertStringsEquals(AssertionType.SOFT, getBookingResponse.body().jsonPath().getString("depositpaid"), String.valueOf(depositPaid));
                assertStringsEquals(AssertionType.SOFT, getBookingResponse.body().jsonPath().getString("bookingdates.checkin"), checkIn);
                assertStringsEquals(AssertionType.SOFT, getBookingResponse.body().jsonPath().getString("bookingdates.checkout"), checkOut);
                assertStringsEquals(AssertionType.SOFT, getBookingResponse.body().jsonPath().getString("additionalneeds"), additionalNeeds);
            });

        });

        String authToken = Step.step("Authorization POST Request", () -> {
            Response postAuthResponse = new AuthEndpoint().setContentTypeForAuthEndpoint(ContentType.JSON).setUsernameAndPassword(username, password).post();

            return postAuthResponse.body().jsonPath().getString("token");
        });

        Step.step("Authorization PUT Request", () -> {
            Response putBookingResponse = new BookingEndpoint().setBookingId(bookingId)
                    .setContentTypeForBookingEndpoint(ContentType.JSON)
                    .setAcceptForBookingEndpoint("application/json")
                    .setAuthorizationCookie(authToken)
                    .setBookingDetailsInBody(updatedFirstName, updatedLastName, updatedTotalPrice, updatedDepositPaid, updatedCheckIn, updatedCheckOut, updatedAdditionalNeeds).put();

            Step.step("Verifying values in response of PUT request", () -> {
                assertStringsEquals(AssertionType.SOFT, putBookingResponse.body().jsonPath().getString("firstname"), updatedFirstName);
                assertStringsEquals(AssertionType.SOFT, putBookingResponse.body().jsonPath().getString("lastname"), updatedLastName);
                assertStringsEquals(AssertionType.SOFT, putBookingResponse.body().jsonPath().getString("totalprice"), String.valueOf(updatedTotalPrice));
                assertStringsEquals(AssertionType.SOFT, putBookingResponse.body().jsonPath().getString("depositpaid"), String.valueOf(updatedDepositPaid));
                assertStringsEquals(AssertionType.SOFT, putBookingResponse.body().jsonPath().getString("bookingdates.checkin"), updatedCheckIn);
                assertStringsEquals(AssertionType.SOFT, putBookingResponse.body().jsonPath().getString("bookingdates.checkout"), updatedCheckOut);
                assertStringsEquals(AssertionType.SOFT, putBookingResponse.body().jsonPath().getString("additionalneeds"), updatedAdditionalNeeds);
            });

        });

        assertSoftAssertions();
    }
}
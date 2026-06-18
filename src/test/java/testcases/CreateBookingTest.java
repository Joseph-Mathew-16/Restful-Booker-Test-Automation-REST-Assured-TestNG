package testcases;

import endpoints.BookingEndpoint;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Name;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testbase.RestfulBookerBase;

import static io.qameta.allure.SeverityLevel.NORMAL;

public class CreateBookingTest extends RestfulBookerBase {

    @DataProvider(name = "CSV Data Reader")
    public Object[][] dataProvider() {
        String folderPath = "Create_Booking_Cases";
        String fileName = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1) + ".csv";
        return csvReader(folderPath, fileName);
    }

    @Test(dataProvider = "CSV Data Reader", description = "This test creates a booking request.")
    @Name("Create Booking Test")
    @Severity(NORMAL)
    @Owner("Joseph Mathew")
    public void createBookingTest(String expectedFirstName, String expectedLastName, String expectedTotalPrice, String expectedDepositPaid, String expectedCheckIn, String expectedCheckOut, String expectedAdditionalNeeds) {

        Response bookingResponse = new BookingEndpoint().setContentTypeForBookingEndpoint(ContentType.JSON).setBookingDetailsInBody(expectedFirstName, expectedLastName, expectedTotalPrice, expectedDepositPaid, expectedCheckIn, expectedCheckOut, expectedAdditionalNeeds).post();

        String bookingId = bookingResponse.body().jsonPath().getString("bookingid");
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.firstname"), expectedFirstName);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.lastname"), expectedLastName);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.totalprice"), String.valueOf(expectedTotalPrice));
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.depositpaid"), String.valueOf(expectedDepositPaid));
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.bookingdates.checkin"), expectedCheckIn);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.bookingdates.checkout"), expectedCheckOut);
        assertStringsEquals(AssertionType.SOFT, bookingResponse.body().jsonPath().getString("booking.additionalneeds"), expectedAdditionalNeeds);

        Response getBookingResponseBody = new BookingEndpoint().setBookingId(bookingId).get();

        assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("firstname"), expectedFirstName);
        assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("lastname"), expectedLastName);
        assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("totalprice"), String.valueOf(expectedTotalPrice));
        assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("depositpaid"), String.valueOf(expectedDepositPaid));
        assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("bookingdates.checkin"), expectedCheckIn);
        assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("bookingdates.checkout"), expectedCheckOut);
        assertStringsEquals(AssertionType.SOFT, getBookingResponseBody.body().jsonPath().getString("additionalneeds"), expectedAdditionalNeeds);

        assertSoftAssertions();
    }
}
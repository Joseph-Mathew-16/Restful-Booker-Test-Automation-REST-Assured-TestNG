package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.testng.Reporter;
import utilities.Step;

@Builder
@Data
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {
    String firstname;
    String lastname;
    int totalprice;
    boolean depositpaid;
    BookingDates bookingdates;
    String additionalneeds;

    public String writeJson() {
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(this);
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            json = jsonNode.toPrettyString();
        } catch (Exception exception) {
            String errorMessage = exception.getMessage();
            Step.step(errorMessage);
        }
        return json;
    }

    public Booking readJson(String json){
        try{
           return new ObjectMapper().readValue(json,Booking.class);
        }catch(JsonParseException jsonParseException){
            String errorMessage = "Underlying input contains invalid content of type JsonParser supports (JSON for default case)";
            Step.step(errorMessage);
        }catch(JsonMappingException jsonMappingException){
            String errorMessage = "Error occurred while mapping JSON to Booking object";
            Step.step(errorMessage);
        }catch (JsonProcessingException jsonProcessingException){
            String errorMessage = "Error occurred while processing JSON";
            Step.step(errorMessage);
        }
        return null;
    }
}

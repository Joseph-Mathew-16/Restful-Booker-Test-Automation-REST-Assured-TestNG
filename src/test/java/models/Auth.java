package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.testng.Reporter;

@Builder
@Data
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Auth {
    public String username;
    public String password;

    public String printJson() {
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(this);
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            json = jsonNode.toPrettyString();
        } catch (Exception exception) {
            String errorMessage = exception.getMessage();
            // TODO: Fix reporter method.
            Reporter.log(errorMessage, true);
        }
        return json;
    }
}

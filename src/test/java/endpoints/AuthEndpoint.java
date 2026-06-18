package endpoints;

import io.restassured.http.ContentType;
import testbase.RestfulBookerBase;

public class AuthEndpoint extends RestfulBookerBase {

    public static String endpoint = "/auth";

    public AuthEndpoint() {
        setBaseURI(configuration.url);
        setBasePath(endpoint);
    }

    public AuthEndpoint setContentTypeForAuthEndpoint(ContentType contentType) {
        setContentType(contentType);
        return this;
    }

    public AuthEndpoint setUsernameAndPassword(String username, String password) {
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        setRequestBody(requestBody);
        return this;
    }
}

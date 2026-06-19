package endpoints;

import io.restassured.http.ContentType;
import models.Auth;
import testbase.RestfulBookerBase;

public class AuthEndpoint extends RestfulBookerBase {

    public static final String endpoint = "/auth";

    public AuthEndpoint() {
        setBaseURI(configuration.url);
        setBasePath(endpoint);
    }

    public AuthEndpoint setContentTypeForAuthEndpoint(ContentType contentType) {
        setContentType(contentType);
        return this;
    }

    public AuthEndpoint setUsernameAndPassword(String username, String password) {
        Auth auth = Auth.builder().username(username).password(password).build();
        String requestBody = auth.printJson();
        setRequestBody(requestBody);
        return this;
    }
}

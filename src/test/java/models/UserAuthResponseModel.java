package models;

import java.util.List;

public class UserAuthResponseModel {

    String id;
    String username;
    String email;
    List<String> roles;

    public String getJwtToken() {
        return jwtToken;
    }

    String jwtToken;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}

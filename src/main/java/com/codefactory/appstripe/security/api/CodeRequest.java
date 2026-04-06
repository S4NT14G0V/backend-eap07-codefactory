package com.codefactory.appstripe.security.api;

import java.util.Objects;

import lombok.Data;
import com.codefactory.appstripe.security.domain.User;

@Data
public class CodeRequest {
    private String username;
    private int code;

    public CodeRequest() {
    }

    public CodeRequest(String username, int code) {
        this.username = username;
        this.code = code;
    }

    public String getUsername() {
        return this.username;
    }

    public User getUser() {
        return new User(this.username, "");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CodeRequest username(String username) {
        setUsername(username);
        return this;
    }

    public CodeRequest code(int code) {
        setCode(code);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CodeRequest)) {
            return false;
        }
        CodeRequest codeRequest = (CodeRequest) o;
        return Objects.equals(username, codeRequest.username) && code == codeRequest.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, code);
    }

    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }


}

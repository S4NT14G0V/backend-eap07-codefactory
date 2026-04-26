package com.codefactory.appstripe.security.api;

import java.util.Objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CodeRequest {

    @NotBlank
    private String twoFactorSecret;

    @Min(0)
    private int code;

    public CodeRequest() {
    }

    public CodeRequest(String twoFactorSecret, int code) {
        this.twoFactorSecret = twoFactorSecret;
        this.code = code;
    }

    public String getTwoFactorSecret() {
        return this.twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public int getCode() {
        return this.code;
    }

    public CodeRequest code(int code) {
        setCode(code);
        return this;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CodeRequest)) {
            return false;
        }
        CodeRequest codeRequest = (CodeRequest) o;
        return Objects.equals(twoFactorSecret, codeRequest.twoFactorSecret) && code == codeRequest.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(twoFactorSecret, code);
    }

    @Override
    public String toString() {
        return "{" +
            " twoFactorSecret='" + getTwoFactorSecret() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }


}

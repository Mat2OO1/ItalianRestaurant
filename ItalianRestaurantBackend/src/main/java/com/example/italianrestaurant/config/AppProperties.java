package com.example.italianrestaurant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public class AppProperties {
    private final Jwt jwt = new Jwt();
    private final OAuth2 oauth2 = new OAuth2();

    public static class Jwt {
        private String tokenSecret;
        private long tokenExpirationHours;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationSeconds() {
            return tokenExpirationHours;
        }

        public void setTokenExpirationSeconds(long tokenExpirationHours) {
            this.tokenExpirationHours = tokenExpirationHours;
        }
    }

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public Jwt getJwt() {
        return jwt;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }
}

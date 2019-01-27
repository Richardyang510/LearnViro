package com.words.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class WordsConfiguration extends Configuration {

    @NotEmpty
    private String URL;

    @NotEmpty
    private String USER;

    @NotEmpty
    private String PASS;

    @JsonProperty
    public String getURL() {
        return URL;
    }

    @JsonProperty
    public void setURL(String URL) {
        this.URL = URL;
    }

    @JsonProperty
    public String getUSER() {
        return USER;
    }

    @JsonProperty
    public void setUSER(String USER) {
        this.USER = USER;
    }

    @JsonProperty
    public String getPASS() {
        return PASS;
    }

    @JsonProperty
    public void setPASS(String PASS) {
        this.PASS = PASS;
    }
}

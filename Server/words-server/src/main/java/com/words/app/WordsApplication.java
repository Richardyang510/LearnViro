package com.words.app;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WordsApplication extends Application<WordsConfiguration> {

    public static void main(String[] args) throws Exception {
        new WordsApplication().run(args);
    }

    @Override
    public String getName() {
        return "words-server";
    }

    @Override
    public void initialize(Bootstrap<WordsConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false))
        );
    }

    public void run(WordsConfiguration config, Environment environment) throws Exception {
        WordsEndpoint endpoint = new WordsEndpoint();
        environment.jersey().register(endpoint);
    }
}

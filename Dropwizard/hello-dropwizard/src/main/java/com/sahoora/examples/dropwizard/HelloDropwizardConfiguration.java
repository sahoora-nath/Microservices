package com.sahoora.examples.dropwizard;

import com.sahoora.examples.dropwizard.resources.HelloSayingFactory;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class HelloDropwizardConfiguration extends Configuration {
    private HelloSayingFactory sayingFactory;

    @JsonProperty("helloapp")
    public HelloSayingFactory getSayingFactory() {
        return sayingFactory;
    }

    @JsonProperty("helloapp")
    public void setSayingFactory(HelloSayingFactory sayingFactory) {
        this.sayingFactory = sayingFactory;
    }
}

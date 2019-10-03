package io.fonimus.configs;

import lombok.Data;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties("test")
@Validated
public class Properties {

    @NotNull
    @NestedConfigurationProperty
    private PhoneNumber phoneNumber;

    private String key;

    private List<String> listString;

    private Map<String, Object> mapStringObject;

    @NotNull
    private Pojo pojo;

    @NotEmpty
    private List<Pojo> listPojo;

    @NotEmpty
    private Map<String, Pojo> mapStringPojo;

    @Data
    @Validated
    public static class Pojo {

        @NotNull
        private String sub;

        private String other;
    }
}

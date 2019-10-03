package io.fonimus.configs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PhoneNumber {

    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
    private String value;

    @NotNull
    @Pattern(regexp = "(?i)cell|house|work")
    private String type;

    @NotNull
    private String test;
}

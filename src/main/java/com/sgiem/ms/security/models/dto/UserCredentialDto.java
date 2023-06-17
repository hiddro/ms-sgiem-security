package com.sgiem.ms.security.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentialDto implements Serializable {

    @JsonProperty(value = "names")
    private String names;

    @JsonProperty(value = "surenames")
    private String surenames;

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "roles")
    private String roles;

}

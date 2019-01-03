package com.dccarlos.learning.asim.resources.security;

import java.io.Serializable;
import java.security.Principal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
@JsonDeserialize(builder = KisUser.KisUserBuilder.class)
public class KisUser implements Serializable, Principal {
    private static final long serialVersionUID = 1491137652954623448L;

    @Getter private final String userName;

    @Override
    @JsonIgnore
    public String getName() {
        return userName;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class KisUserBuilder {}
}
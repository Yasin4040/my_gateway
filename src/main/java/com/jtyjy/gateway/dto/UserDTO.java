package com.jtyjy.gateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private List<String> scopes;

    private List<String> authorities;

}

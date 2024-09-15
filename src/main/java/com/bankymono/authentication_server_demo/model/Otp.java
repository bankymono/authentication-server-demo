package com.bankymono.authentication_server_demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class Otp {
    @Id
    private String username;
    private String code;
}

package com.WilsonPicpay.Picpay.dtos;

import com.WilsonPicpay.Picpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO (String firstName, String lastName, String email, String document, BigDecimal balance, String password, UserType userType){
}

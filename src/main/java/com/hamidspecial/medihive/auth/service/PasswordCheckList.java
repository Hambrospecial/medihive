package com.hamidspecial.medihive.auth.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PasswordCheckList {
    private Integer minLength = Integer.MAX_VALUE;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecial;
    private Integer minUniqueChars = 0;
}

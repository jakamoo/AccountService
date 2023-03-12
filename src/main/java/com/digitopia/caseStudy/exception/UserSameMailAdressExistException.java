package com.digitopia.caseStudy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserSameMailAdressExistException extends RuntimeException{
    public UserSameMailAdressExistException(String message) {
        super(message);
    }
}




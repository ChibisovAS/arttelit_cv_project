package com.arttelit.chibisov.simplestock.advice;

import com.arttelit.chibisov.simplestock.exceptions.ForbiddenException;
import com.arttelit.chibisov.simplestock.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        if (e instanceof NotFoundException)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase() +" "+ HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND);

        else if (e instanceof ForbiddenException)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN.getReasonPhrase() +" "+ HttpStatus.FORBIDDEN.value(),
                    HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(">>> Another Exception <<<",
                    HttpStatus.OK);
    }


}

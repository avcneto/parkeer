package com.parkeer.parkeer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceAlreadyExistsException extends ResponseStatusException {
  public ResourceAlreadyExistsException(String message) {
    super(HttpStatus.CONFLICT, message, null);
  }
}

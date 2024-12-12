package com.home.petstore.exception;

public class TimeIntervalExceededException extends RuntimeException {
  public TimeIntervalExceededException(String message) {
    super(message);
  }
}

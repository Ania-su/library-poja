package hei.school.demo.endpoint.rest.controller.exceptionHandler;

import java.time.Instant;

public record ApiError(Instant timestamp, int status, String error, String message) {
  public ApiError(int status, String error, String message) {
    this(Instant.now(), status, error, message);
  }
}

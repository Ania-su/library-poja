package hei.school.demo.endpoint.rest.controller.exceptionHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import hei.school.demo.exception.BadRequestException;
import hei.school.demo.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Mock private MethodArgumentTypeMismatchException typeMismatchException;

  @Mock private NoResourceFoundException noResourceFoundException;

  @Test
  void handleNotFound_shouldReturn404WithMessage() {
    var exception = new NotFoundException("Book with id 1 not found");

    var response = handler.handleNotFound(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    Assertions.assertNotNull(response.getBody());
    assertThat(response.getBody().status()).isEqualTo(404);
    assertThat(response.getBody().error()).isEqualTo("Not Found");
    assertThat(response.getBody().message()).isEqualTo("Book with id 1 not found");
  }

  @Test
  void handleBadRequestException_shouldReturn400WithMessage() {
    var exception = new BadRequestException("Invalid title");

    var response = handler.handleBadRequestException(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(response.getBody());
    assertThat(response.getBody().status()).isEqualTo(400);
    assertThat(response.getBody().error()).isEqualTo("Bad Request");
    assertThat(response.getBody().message()).isEqualTo("Invalid title");
  }

  @Test
  void handleMethodArgumentTypeMismatchException_shouldReturn400() {
    when(typeMismatchException.getMessage()).thenReturn("Invalid UUID format");

    var response = handler.handleMethodArgumentTypeMismatchException(typeMismatchException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(response.getBody());
    assertThat(response.getBody().status()).isEqualTo(400);
    assertThat(response.getBody().message()).isEqualTo("Invalid UUID format");
  }

  @Test
  void handleNoResourceFound_shouldReturn404WithResourcePath() {
    when(noResourceFoundException.getResourcePath()).thenReturn("unknown/path");

    var response = handler.handleNoResourceFound(noResourceFoundException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    Assertions.assertNotNull(response.getBody());
    assertThat(response.getBody().status()).isEqualTo(404);
    assertThat(response.getBody().message()).isEqualTo("Resource not found: unknown/path");
  }

  @Test
  void handleGeneric_shouldReturn500WithMessage() {
    var exception = new RuntimeException("database unavailable");

    var response = handler.handleGeneric(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    Assertions.assertNotNull(response.getBody());
    assertThat(response.getBody().status()).isEqualTo(500);
    assertThat(response.getBody().error()).isEqualTo("Internal Server Error");
    assertThat(response.getBody().message()).isEqualTo("database unavailable");
  }
}

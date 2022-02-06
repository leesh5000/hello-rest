package leesh.devcom.backend.exception;

import leesh.devcom.backend.controller.IndexController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static leesh.devcom.backend.exception.ErrorCode.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    /**
     * Exception to be thrown when validation on an argument annotated with @Valid fails.
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("handleMethodArgumentNotValidException", e);

        ErrorResponse payload = ErrorResponse.of(INVALID_INPUT_VALUE, e.getBindingResult());

        EntityModel<ErrorResponse> of = EntityModel.of(payload,
                linkTo(IndexController.class).withRel("index"),
                Link.of("index.html#_" + payload.getCode()).withRel("profile"));

        return ResponseEntity.status(payload.getStatus()).body(of);
    }
}

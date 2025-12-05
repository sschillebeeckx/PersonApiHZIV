package be.abis.exercise.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MyExceptionHandler implements ErrorWebExceptionHandler {

	@ExceptionHandler(value = LoginException.class)
	protected Mono<ProblemDetail> handlePersonLoginWrong(LoginException le, ServerWebExchange exchange) {
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		problem.setTitle("Login Wrong");
		problem.setDetail(le.getMessage());
		return Mono.just(problem);
	}

	@ExceptionHandler(value = PersonNotFoundException.class)
	protected Mono<ProblemDetail> handlePersonNotFound (PersonNotFoundException pnfe, ServerWebExchange exchange) {
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		problem.setTitle("Not Found");
		problem.setDetail(pnfe.getMessage());
		return Mono.just(problem);
	}

	/*@ExceptionHandler(value = NoPersonsFoundException.class)
	protected Mono<ProblemDetail> handleNoPersonsFoundJSON(NoPersonsFoundException npfe, ServerWebExchange exchange) {
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		problem.setTitle("Nobody Found");
		problem.setDetail(npfe.getMessage());
		return Mono.just(problem);
	}*/

	@ExceptionHandler(value = NoPersonsFoundException.class)
	protected ResponseEntity<XMLProblemDetail> handleNoPersonsFoundXML(NoPersonsFoundException npfe, ServerWebExchange exchange) {
		XMLProblemDetail problem = new XMLProblemDetail(HttpStatus.NOT_FOUND.value(),"Nobody Found",npfe.getMessage());
		ResponseEntity entityProblem = ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_XML)
				.body(problem);
		return entityProblem;
	}

	/*@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ProblemDetail> handleValidationErrors(WebExchangeBindException ex) {
		Map<String, String> errors = ex.getFieldErrors().stream()
				.collect(Collectors.toMap(
						f -> f.getField(),
						f -> f.getDefaultMessage()
				));

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setTitle("Validation Failed");
		problem.setProperty("errors", errors);

		return Mono.just(problem);
	}
*/

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		System.out.println("handling things");
		if (ex instanceof WebExchangeBindException webEx) {
			Map<String, String> errors = webEx.getFieldErrors().stream()
					.collect(Collectors.toMap(
							f -> f.getField(),
							f -> f.getDefaultMessage()
					));

			ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
			problem.setTitle("Validation Failed");
			problem.setProperty("errors", errors);

			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

			byte[] bytes;
			ObjectMapper mapper = new ObjectMapper();
			try {
				bytes = mapper.writeValueAsBytes(problem);
			} catch (JsonProcessingException e) {
				bytes = new byte[0];
			}

			return exchange.getResponse().writeWith(
					Mono.just(exchange.getResponse()
							.bufferFactory()
							.wrap(bytes))
			);
		}

		return Mono.error(ex);
	}


}

package be.abis.exercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

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

}

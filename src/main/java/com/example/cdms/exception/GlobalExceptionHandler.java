package com.example.cdms.exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Pattern DETAIL = Pattern.compile("Detail:\\s*(.+)");

	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				ex.getMessage() != null ? ex.getMessage() : "Invalid");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ProblemDetail handleIDuplicateKey(DuplicateKeyException ex) {
		String detail = "Conflict Data.";
		String msg = ex.getMostSpecificCause().getMessage();
		if (msg != null) {
			Matcher m = DETAIL.matcher(msg);
			if (m.find()) {
				detail = m.group(1).trim();
			}
		}
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

}

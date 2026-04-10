package com.petcation.common.exception;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.log4j.Log4j;

@ControllerAdvice
@Log4j
public class CommonExceptionAdvice {
	@ExceptionHandler(BadRequestException.class)
	@RequestMapping(produces = "application/json")
	public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
		log.warn("400 BAD_REQUEST: " + ex.getMessage());
	
    	Map<String, Object> body = new LinkedHashMap<>();
    	body.put("status", HttpStatus.BAD_REQUEST.value());
    	body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
    	body.put("detail", ex.getMessage());
    	body.put("path", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	        .contentType(MediaType.APPLICATION_JSON)
	        .body(body);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	@RequestMapping(produces = "application/json")
	public ResponseEntity<Map<String, Object>> handleUnauthrized(UnauthorizedException ex, HttpServletRequest req) throws IOException {
		log.warn("401 UNAUTHORIZED: " + ex.getMessage());

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		body.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
		body.put("detail", ex.getMessage());
		body.put("path", req.getRequestURI());
		body.put("loginRequired", true);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body);
	}
	
	@ExceptionHandler(Exception.class)
	public String exceptionMethod(Exception ex, Model model) {
		log.error("Exception...." + ex.getMessage());
		model.addAttribute("exception", ex);
		log.error(model);
		return "/common/error/error";
	}
   
   @ExceptionHandler(NoHandlerFoundException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public String handle404(NoHandlerFoundException ex) {
      
      return "/common/error/error";
   }
}

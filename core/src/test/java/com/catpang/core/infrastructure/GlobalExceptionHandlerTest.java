package com.catpang.core.infrastructure;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.catpang.core.codes.ErrorCode;
import com.catpang.core.exception.GlobalExceptionHandler;

/**
 * 다양한 상태 코드에 대한 ResponseStatusException을 발생시키는 테스트용 컨트롤러.
 */
@RestController
@RequestMapping("/test")
class ExceptionTestController {

	@GetMapping("/bad-request")
	public void badRequest() {
		throw new ResponseStatusException(BAD_REQUEST, "Bad Request Test");
	}

	@GetMapping("/unauthorized")
	public void unauthorized() {
		throw new ResponseStatusException(UNAUTHORIZED, "Unauthorized Test");
	}

	@GetMapping("/forbidden")
	public void forbidden() {
		throw new ResponseStatusException(FORBIDDEN, "Forbidden Test");
	}

	@GetMapping("/not-found")
	public void notFound() {
		throw new ResponseStatusException(NOT_FOUND, "Not Found Test");
	}

	@GetMapping("/internal-server-error")
	public void internalServerError() {
		throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Internal Server Error Test");
	}

	@GetMapping("/service-unavailable")
	public void serviceUnavailable() {
		throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Service Unavailable Test");
	}
}

@WebMvcTest(controllers = {ExceptionTestController.class, GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username = "admin", roles = {"MASTER_ADMIN"})
	void handleBadRequestException() throws Exception {
		mockMvc.perform(get("/test/bad-request"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code", is(ErrorCode.BAD_REQUEST.name())))
			.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
			.andExpect(jsonPath("$.divisionCode", is("G001")))
			.andExpect(jsonPath("$.resultMsg", is("Bad Request")))
			.andExpect(jsonPath("$.reason", is("Bad Request Test")));
	}

	@Test
	@WithMockUser(username = "admin", roles = {"MASTER_ADMIN"})
	void handleUnauthorizedException() throws Exception {
		mockMvc.perform(get("/test/unauthorized"))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.code", is(ErrorCode.UNAUTHORIZED.name())))
			.andExpect(jsonPath("$.status", is("UNAUTHORIZED")))
			.andExpect(jsonPath("$.divisionCode", is("G013")))
			.andExpect(jsonPath("$.resultMsg", is("Unauthorized")))
			.andExpect(jsonPath("$.reason", is("Unauthorized Test")));
	}

	@Test
	@WithMockUser(username = "admin", roles = {"MASTER_ADMIN"})
	void handleForbiddenException() throws Exception {
		mockMvc.perform(get("/test/forbidden"))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.code", is(ErrorCode.FORBIDDEN.name())))
			.andExpect(jsonPath("$.status", is("FORBIDDEN")))
			.andExpect(jsonPath("$.divisionCode", is("G008")))
			.andExpect(jsonPath("$.resultMsg", is("Forbidden Exception")))
			.andExpect(jsonPath("$.reason", is("Forbidden Test")));
	}

	@Test
	@WithMockUser(username = "admin", roles = {"MASTER_ADMIN"})
	void handleNotFoundException() throws Exception {
		mockMvc.perform(get("/test/not-found"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code", is(ErrorCode.NOT_FOUND.name())))
			.andExpect(jsonPath("$.status", is("NOT_FOUND")))
			.andExpect(jsonPath("$.divisionCode", is("G009")))
			.andExpect(jsonPath("$.resultMsg", is("Not Found Exception")))
			.andExpect(jsonPath("$.reason", is("Not Found Test")));
	}

	@Test
	@WithMockUser(username = "admin", roles = {"MASTER_ADMIN"})
	void handleInternalServerErrorException() throws Exception {
		mockMvc.perform(get("/test/internal-server-error"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code", is(ErrorCode.INTERNAL_SERVER_ERROR.name())))
			.andExpect(jsonPath("$.status", is("INTERNAL_SERVER_ERROR")))
			.andExpect(jsonPath("$.divisionCode", is("G999")))
			.andExpect(jsonPath("$.resultMsg", is("Internal Server Error Exception")))
			.andExpect(jsonPath("$.reason", is("Internal Server Error Test")));
	}
}
package com.github.nkolytschew.mvc.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * simple controller.
 * <p>
 * returns hello universe on request path "/hello".
 * used to check if our app works and responds.
 */
@RestController
class HelloUniverseController {
	@GetMapping("/")
	public String getHelloUniverse() {
		return "Hello Universe";
	}
}
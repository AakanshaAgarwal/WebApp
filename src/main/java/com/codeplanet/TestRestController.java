package com.codeplanet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {
	
	@Autowired
	TestRestService testRestService;
	@GetMapping("/firstApi")
	public Map getFirstApi(HttpServletRequest req) {
		Map map = testRestService.getFirstApi();
		return map;
	}

}

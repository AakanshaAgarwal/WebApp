package com.codeplanet;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestRestService {
	
	
    @Autowired
    TestRestRepository testRestRepository;
	public Map getFirstApi() {
	  return testRestRepository.getFirstApi();
	}

}

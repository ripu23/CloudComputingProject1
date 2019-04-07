package com.cloud.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.app.services.ResponseProcessor;
import com.cloud.app.services.SqsService;
import com.cloud.app.utility.Constants;
import com.cloud.app.utility.DataStorage;

@RestController
public class AppController {

    @Autowired
    SqsService sqs;
    
    @Autowired
    DataStorage ds;

    @Autowired
    ResponseProcessor processor;
    
    @Autowired
    private DataStorage dataStorage;
    
    @RequestMapping("/")
    public ResponseEntity<?> handleRequest() {
    	//Generating processId to map and send response
    	String processId = Constants.PROCESS_ID.concat(String.valueOf(dataStorage.getCounter()));
        sqs.addRequest(processId);
        String response = processor.serveRequest(processId);
        if(!response.isEmpty()) {
        	return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
        	return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
}

package com.cloud.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.cloud.app.services.RequestProcessorService;

@SpringBootApplication
@EnableAsync
public class WorkerApplication {

    private static RequestProcessorService processorService;

    @Autowired
    public WorkerApplication(RequestProcessorService processorService){
        this.processorService = processorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
        System.out.println("Worker Started");
        processorService.processRequests();
    }

}

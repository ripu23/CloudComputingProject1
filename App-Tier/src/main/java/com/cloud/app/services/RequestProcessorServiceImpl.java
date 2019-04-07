package com.cloud.app.services;

import com.amazonaws.services.sqs.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RequestProcessorServiceImpl implements RequestProcessorService {

    @Autowired
    private SqsService sqsService;

    @Autowired
    private VideoService videoService;

    private void terminateInstance() {
        // Instances are configured to terminate on shutdown
        Runtime run = Runtime.getRuntime();
        try {
            run.exec("sudo shutdown 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void processRequests() {
        while (true) {
            Message msg = sqsService.receiveMessage();
            if(msg == null) {
                break;
            }
            String processId = msg.getBody();
            if (videoService.processVideo(processId)) {
                // Delete this message if processing was successful
                sqsService.deleteMessage(msg);
            }
        }
        // It will come out of while loop only if there were no messages in the queue
        // and in that case we should terminate this instance
        System.out.println("Shutting down worker");
        terminateInstance();
    }

}

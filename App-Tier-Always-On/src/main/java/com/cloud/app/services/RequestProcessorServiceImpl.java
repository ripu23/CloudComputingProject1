package com.cloud.app.services;

import com.amazonaws.services.sqs.model.Message;
import com.cloud.app.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RequestProcessorServiceImpl implements RequestProcessorService {

    Logger logger = LoggerFactory.getLogger(RequestProcessorServiceImpl.class);

    private static int processedCount = 0;

    @Autowired
    private SqsService sqsService;

    @Autowired
    private VideoService videoService;

    @Async
    public void processRequests() {
        while (true) {
            logger.info("Waiting for a request for next " + Constants.MESSAGE_WAIT + "s.");
            Message msg = sqsService.receiveMessage();
            if(msg == null) {
                continue;
            }
            String processId = msg.getBody();
            if (videoService.processVideo(processId)) {
                // Delete this message if processing was successful
                logger.info("Videos processed successfully: " + (++processedCount));
                sqsService.deleteMessage(msg);
            }
        }
    }

}

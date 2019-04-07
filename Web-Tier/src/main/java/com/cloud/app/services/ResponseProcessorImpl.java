package com.cloud.app.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.Message;
import com.cloud.app.aws.SqsOperations;
import com.cloud.app.utility.Constants;
import com.cloud.app.utility.DataStorage;

@Component
public class ResponseProcessorImpl implements ResponseProcessor {

	Logger logger = LoggerFactory.getLogger(ResponseProcessorImpl.class);


	@Autowired
	SqsOperations sqsOperations;

	@Autowired
	DataStorage dataStorage;

	@Override
	@Async
	public String serveRequest(String requestId) {

		Map<String, String> requestReponseMap = dataStorage.getMap();

		while (true) {
			if(requestReponseMap.containsKey(requestId)){
				String response = requestReponseMap.get(requestId);
				requestReponseMap.remove(requestId);
				return response;
			}
			try {
				logger.info(String.format("%s: %s", "Request still to be processed", requestId));
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(fixedDelay = Constants.POLL_INTERVAL)
	public void pollMessageAndPutToMap() {
		Message message = sqsOperations.readMessageFromOutputQueue();
		if (message != null) {
			String queueMsg = message.getBody();
			List<String> splitMsg = Arrays.asList(queueMsg.split("[:_]"));
			logger.info("New response in output queue: " + splitMsg.toString());
			if(splitMsg.size() > 1) {
				Map<String, String> requestReponseMap = dataStorage.getMap();
				requestReponseMap.put(splitMsg.get(0), queueMsg);
				sqsOperations.deleteMessage(message);
			}
		}
	}

}

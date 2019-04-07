package com.cloud.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.Message;
import com.cloud.app.aws.SqsOperations;

@Component
public class SqsServiceImpl implements SqsService {

    @Autowired
    private SqsOperations sqsOperations;

    @Override
    public void addRequest(String processId) {
        sqsOperations.addRequestToQueue(processId);
    }

    @Override
    public int getNumMsgs() {
        return sqsOperations.getNumMsgs();
    }

	@Override
	public Message readMessage() {
		
		return sqsOperations.readMessageFromOutputQueue();
	}

}

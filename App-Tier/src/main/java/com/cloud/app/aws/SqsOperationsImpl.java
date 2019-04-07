package com.cloud.app.aws;

import java.util.List;

import com.amazonaws.services.sqs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloud.app.config.AwsConfig;
import com.cloud.app.utility.Constants;

@Component
public class SqsOperationsImpl implements SqsOperations {

    @Autowired
    private AwsConfig awsConfig;

    @Override
    public Message receiveMessage() {
        ReceiveMessageRequest req = new ReceiveMessageRequest(Constants.INPUT_QUEUE_URL)
                .withMaxNumberOfMessages(1)
                .withWaitTimeSeconds(Constants.MESSAGE_WAIT)
                .withVisibilityTimeout(Constants.MESSAGE_VISIBILITY);
        ReceiveMessageResult res = awsConfig.amazonSQS().receiveMessage(req);
        List<Message> messageList = res.getMessages();
        if (messageList.isEmpty()) {
            return null;
        }
        return messageList.get(0);
    }

    @Override
    public void deleteMessage(Message message) {
        String handle = message.getReceiptHandle();
        DeleteMessageRequest req = new DeleteMessageRequest(Constants.INPUT_QUEUE_URL, handle);
        awsConfig.amazonSQS().deleteMessage(req);
    }

	@Override
	public void putMessage(String response) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(Constants.OUPUT_QUEUE_URL)
                .withMessageBody(response).withDelaySeconds(0);
        awsConfig.amazonSQS().sendMessage(sendMessageRequest);
		
	}
}

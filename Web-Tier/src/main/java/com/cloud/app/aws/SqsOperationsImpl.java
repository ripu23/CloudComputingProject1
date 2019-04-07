package com.cloud.app.aws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.cloud.app.config.AwsConfig;
import com.cloud.app.utility.Constants;

@Component
public class SqsOperationsImpl implements SqsOperations {

    @Autowired
    private AwsConfig awsConfig;
    

    public void addRequestToQueue(String processId) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(Constants.INPUT_QUEUE_URL)
                .withMessageBody(processId).withDelaySeconds(0);
        awsConfig.amazonSQS().sendMessage(sendMessageRequest);
    }

    public int getNumMsgs() {
        GetQueueAttributesRequest gqar = new GetQueueAttributesRequest(Constants.INPUT_QUEUE_URL,
                new ArrayList<>(Arrays.asList("ApproximateNumberOfMessages")));
        Map<String, String> attrs = awsConfig.amazonSQS().getQueueAttributes(gqar).getAttributes();
        return Integer.valueOf(attrs.get("ApproximateNumberOfMessages"));
    }

	@Override
	public Message readMessageFromOutputQueue() {
		ReceiveMessageRequest req = new ReceiveMessageRequest(Constants.OUPUT_QUEUE_URL)
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
		System.out.println(String.format("%s %s %s", "Deleting message", message.getBody(), "from output queue"));
		String handle = message.getReceiptHandle();
        DeleteMessageRequest req = new DeleteMessageRequest(Constants.OUPUT_QUEUE_URL, handle);
        awsConfig.amazonSQS().deleteMessage(req);
		
	}
}

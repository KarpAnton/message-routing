package com.andersenlab.messageclient.listener;

import com.andersenlab.client.context.ClientContext;
import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.pubsub.Commit;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Messages;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messageclient.configuration.property.ConsumerProperties;
import com.andersenlab.messageclient.processor.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageListener implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    private MessageProcessor messageProcessor;
    private BrokerControllerApi brokerControllerApi;
    private ClientContext<Subscriber> subscriberClientContext;
    private Integer batchSize;
    private boolean enableAutoCommit;
    private boolean isRunnable = true;

    public MessageListener(MessageProcessor messageProcessor,
                           BrokerControllerApi brokerControllerApi,
                           ClientContext<Subscriber> subscriberClientContext,
                           ConsumerProperties properties) {

        this.messageProcessor = messageProcessor;
        this.brokerControllerApi = brokerControllerApi;
        this.subscriberClientContext = subscriberClientContext;
        this.batchSize = properties.getBatchSize();
        this.enableAutoCommit = properties.isEnabledAutoCommit();
    }

    @Override
    public void run() {
        while (isRunnable) {
            doPoll();
        }
    }

    private void doPoll() {
        Messages messages = brokerControllerApi.requestAvailableMessages(subscriberClientContext.getName(), batchSize);
        List<Message> processedMessages = new ArrayList<>();
        if (enableAutoCommit && messages.isNotEmpty()) { // at-most-once
            commitMessages(messages);
        }
        for (Message message : messages.getMessages()) {
            try {
                messageProcessor.process(message);
                processedMessages.add(message);
            } catch (Exception e) {
                LOG.error("Exception thrown during processing message with correlation id {}. Exception message: {}", message.getId(), e.getMessage());
                break;
            }
        }
        if (!enableAutoCommit && messages.isNotEmpty()) { // committing processed messages (at-least-once)
            commitMessages(processedMessages);
        }
    }

    private void commitMessages(List<Message> processedMessages) {
        Commit commit = new Commit(
                subscriberClientContext.getDestination().getName(),
                subscriberClientContext.getName(),
                processedMessages
        );
        brokerControllerApi.commitMessages(commit);
    }

    private void commitMessages(Messages messages) {
        commitMessages(messages.getMessages());
    }

    public void setRunnable(boolean runnable) {
        isRunnable = runnable;
    }

    public void setEnableAutoCommit(boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }
}

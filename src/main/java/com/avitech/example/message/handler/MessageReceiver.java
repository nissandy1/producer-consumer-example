package com.avitech.example.message.handler;

import com.avitech.example.message.dto.Command;
import com.avitech.example.model.User;
import com.avitech.example.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Log4j2
public class MessageReceiver extends AbstractMessageHandler implements MessageListener {

    private final UserRepository userRepository;

    public MessageReceiver(ConnectionFactory factory, UserRepository userRepository) throws JMSException {
        super(factory);
        this.userRepository = userRepository;
    }

    /**
     * Starts listening on message queue.
     * @throws JMSException
     */
    public void start() throws JMSException {
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(this);
    }

    /**
     * Handle incoming message from queue and execute command on DB
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        // handle incoming message from queue
        if (message instanceof TextMessage tm) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // deserialize command from json
                Command command = objectMapper.readValue(tm.getText(), Command.class);

                log.debug("Message received: {}, Thread: {}",
                        command.toString(),
                        Thread.currentThread().getName());
                switch (command.getDbCommand()) {
                    case ADD -> save(command.getUserToAdd());
                    case PRINT_ALL -> printAll();
                    case DELETE_ALL -> deleteAll();
                }
            } catch (JMSException | JsonProcessingException e) {
                log.error("Error during receiving message from queue: {}", e.getMessage());
            }
        }
    }

    private User save(User user) {
        log.info("Saving User to DB: {}", user);
        return userRepository.save(user);
    }

    private void printAll() {
        log.info("Printing all Users from DB");
        userRepository.findAll().forEach(u -> log.info(u.toString()));
    }

    private void deleteAll() {
        log.info("Deleting all Users from DB");
        userRepository.deleteAll();
    }
}
package com.avitech.example.message.handler;

import com.avitech.example.enums.DbCommand;
import com.avitech.example.message.dto.Command;
import com.avitech.example.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

@Log4j2
public class MessageSender extends AbstractMessageHandler {

    private final MessageProducer producer;

    public MessageSender(ConnectionFactory factory) throws JMSException {
        super(factory);
        this.producer = session.createProducer(queue);
    }

    /**
     * Creates commands and sends them to message queue as json objects
     *
     * @throws JsonProcessingException
     * @throws JMSException
     */
    public void start() throws JMSException, JsonProcessingException {
        // create entities
        User user = new User();
        user.setId(1L);
        user.setGuid("a1");
        user.setName("Robert");

        User user1 = new User();
        user1.setId(2L);
        user1.setGuid("a2");
        user1.setName("Martin");

        // create DTOs for commands
        Command add1 = Command.builder().dbCommand(DbCommand.ADD).userToAdd(user).build();
        Command add2 = Command.builder().dbCommand(DbCommand.ADD).userToAdd(user1).build();
        Command printAll = Command.builder().dbCommand(DbCommand.PRINT_ALL).build();
        Command deleteAll = Command.builder().dbCommand(DbCommand.DELETE_ALL).build();

        ObjectMapper objectMapper = new ObjectMapper();

        // send commands as jsons  to queue
        sendMessage(objectMapper.writeValueAsString(add1));
        sendMessage(objectMapper.writeValueAsString(add2));
        sendMessage(objectMapper.writeValueAsString(printAll));
        sendMessage(objectMapper.writeValueAsString(deleteAll));
        sendMessage(objectMapper.writeValueAsString(printAll));
    }

    private void sendMessage(String message) throws JMSException {
        log.debug("Sending message: {}, Thread:{}",
                message,
                Thread.currentThread().getName());
        TextMessage textMessage = session.createTextMessage(message);
        producer.send(textMessage);
    }

}

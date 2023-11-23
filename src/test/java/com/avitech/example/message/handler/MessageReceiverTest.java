package com.avitech.example.message.handler;

import com.avitech.example.enums.DbCommand;
import com.avitech.example.message.dto.Command;
import com.avitech.example.model.User;
import com.avitech.example.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageReceiverTest {

    private MessageReceiver messageReceiver;
    @Mock
    private ConnectionFactory connectionFactory;
    @Mock
    private Session session;
    @Mock
    private Connection con;
    @Mock
    private Queue queue;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TextMessage message;
    @Mock
    private MessageConsumer messageConsumer;

    @BeforeEach
    public void setup() throws JMSException {
        when(connectionFactory.createConnection()).thenReturn(con);
        when(con.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
        when(session.createQueue(any())).thenReturn(queue);
        messageReceiver = new MessageReceiver(connectionFactory, userRepository);
    }

    @Test
    void testSaveUser() throws JsonProcessingException, JMSException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setId(1L);
        Command add = Command.builder().dbCommand(DbCommand.ADD).userToAdd(user).build();
        String dto = objectMapper.writeValueAsString(add);

        when(message.getText()).thenReturn(dto);

        // when
        messageReceiver.onMessage(message);

        // then
        verify(userRepository, times(1)).save(refEq(user));
    }

    @Test
    void testPrintAll() throws JsonProcessingException, JMSException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        Command add = Command.builder().dbCommand(DbCommand.PRINT_ALL).build();
        String dto = objectMapper.writeValueAsString(add);

        when(message.getText()).thenReturn(dto);

        // when
        messageReceiver.onMessage(message);

        // then
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteAll() throws JsonProcessingException, JMSException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        Command add = Command.builder().dbCommand(DbCommand.DELETE_ALL).build();
        String dto = objectMapper.writeValueAsString(add);

        when(message.getText()).thenReturn(dto);

        // when
        messageReceiver.onMessage(message);

        // then
        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    void testStartListener() throws JMSException {
        // given
        when(session.createConsumer(queue)).thenReturn(messageConsumer);

        // when
        messageReceiver.start();

        // then
        verify(session, times(1)).createConsumer(queue);
        verify(messageConsumer, times(1)).setMessageListener(refEq(messageReceiver));
    }

    @Test
    void testDestroy() throws JMSException {
        // when
        messageReceiver.destroy();

        // then
        verify(con, times(1)).close();
    }

    @Test
    void testError() throws JMSException {
        // given
        String dto = "INVALID";

        when(message.getText()).thenReturn(dto);

        // when
        messageReceiver.onMessage(message);

        // then
        verify(userRepository, times(0)).findAll();
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(0)).deleteAll();
    }
}

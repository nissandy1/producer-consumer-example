package com.avitech.example;

import com.avitech.example.jms.JmsProvider;
import com.avitech.example.message.handler.MessageReceiver;
import com.avitech.example.message.handler.MessageSender;
import com.avitech.example.repository.impl.UserRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.log4j.Log4j2;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Log4j2
public class ExampleMain {

    public static void main(String[] args) {
        log.info("Program started");

        // create instances
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.avitech.user_catalog");
        EntityManager em = emf.createEntityManager();
        ConnectionFactory connectionFactory = JmsProvider.getConnectionFactory();

        try {
            final MessageSender sender = new MessageSender(connectionFactory);
            final MessageReceiver receiver = new MessageReceiver(connectionFactory, new UserRepositoryImpl(em));

            // start processing
            sender.start();
            receiver.start();

            // terminate instances
            sender.destroy();
            // wait for receiver
            Thread.sleep(500);
            receiver.destroy();
        } catch (JMSException | JsonProcessingException | InterruptedException e) {
            log.error("Error during running the program: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        log.info("Program finished successfully");
    }

}

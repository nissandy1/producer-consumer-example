package com.avitech.example.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;
import java.util.List;

public class JmsProvider {

    private JmsProvider() {
        // Singleton class
    }

    public static ConnectionFactory getConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
        factory.setTrustedPackages(List.of("com.avitech.exercise"));
        return factory;
    }
}

package com.avitech.example.message.handler;

import com.avitech.example.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

@Log4j2
public abstract class AbstractMessageHandler {

    protected final Session session;
    protected final Connection con;
    protected final Queue queue;

    protected AbstractMessageHandler(ConnectionFactory factory) throws JMSException {
        this.con = factory.createConnection();
        this.con.start();
        this.session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.queue = session.createQueue(Constants.USER_QUEUE_NAME);
    }

    /**
     * Closes connection/session with message queue.
     * @throws JMSException
     */
    public void destroy() throws JMSException {
        log.info("Closing queue connection on {}", this);
        con.close();
    }

    public abstract void start() throws JMSException, JsonProcessingException;

}

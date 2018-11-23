package myjmspubsub;

import javax.jms.JMSException;
import javax.jms.Session;
import com.sun.messaging.ConnectionConfiguration;//Necesario para conectarse con broker remoto
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Queue;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class SubscribeToQueue implements MessageListener {
    private final String QUEUE_NAME = "MyQueue";   
    private final MessageConsumer consumer;
    private final Session session;

    public SubscribeToQueue() throws JMSException {
        ConnectionFactory connFactory = new ConnectionFactory();
        //para un broker remoto:
        //connFactory.setProperty(ConnectionConfiguration.imqAddressList, "192.168.56.101");
        //o:
        //connFactory.setProperty(ConnectionConfiguration.imqAddressList, "192.168.56.101:7676");
        Connection connection = connFactory.createConnection();
        Queue queue = new Queue(QUEUE_NAME);
        session = connection.createSession(); 
        consumer = session.createConsumer(queue);
        consumer.setMessageListener(this);
        connection.start();
    }

    @Override
    public void onMessage(Message message) {
        TextMessage txtMsg = (TextMessage) message;
        try {
            String text = txtMsg.getText();
            System.out.println("Recibido: " + text);
            if(text.equalsIgnoreCase("exit")) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            session.close();
                            System.exit(0);
                        } catch (JMSException ex) {
                            ex.printStackTrace();
                        }
                    }
                }.start();

            }
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws JMSException {
        SubscribeToQueue sub = new SubscribeToQueue();
        //Reciba mensajes hasta que llegue 'exit'
        System.out.println("Recibiendo mensajes !!!");
    } 


}

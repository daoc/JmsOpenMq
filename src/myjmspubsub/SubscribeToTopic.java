package myjmspubsub;

import javax.jms.JMSException;
import javax.jms.Session;
import com.sun.messaging.ConnectionConfiguration;//Necesario para conectarse con broker remoto
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Topic;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class SubscribeToTopic implements MessageListener {
    private final String TOPIC_NAME = "MyTopic";   
    private final MessageConsumer consumer;
    private final Session session;

    public SubscribeToTopic() throws JMSException {
        ConnectionFactory connFactory = new ConnectionFactory();
        //para un broker remoto:
        //connFactory.setProperty(ConnectionConfiguration.imqAddressList, "192.168.56.101");
        //o:
        //connFactory.setProperty(ConnectionConfiguration.imqAddressList, "192.168.56.101:7676");
        Connection connection = connFactory.createConnection();
        Topic topic = new Topic(TOPIC_NAME);
        session = connection.createSession(); 
        consumer = session.createConsumer(topic);
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
        SubscribeToTopic sub = new SubscribeToTopic();
        //Reciba mensajes hasta que llegue 'exit'
        System.out.println("Recibiendo mensajes !!!");
    } 


}

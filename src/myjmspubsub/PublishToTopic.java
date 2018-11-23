package myjmspubsub;

import javax.jms.JMSException;
import javax.jms.Session;
import com.sun.messaging.ConnectionConfiguration;//Necesario para conectarse con broker remoto
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Topic;
import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class PublishToTopic {
    private final String TOPIC_NAME = "MyTopic";   
    private final MessageProducer producer;
    private final Session session;

    public PublishToTopic() throws JMSException {
        ConnectionFactory connFactory = new ConnectionFactory();
        //para un broker remoto:
        //connFactory.setProperty(ConnectionConfiguration.imqAddressList, "192.168.56.101");
        //o:
        //connFactory.setProperty(ConnectionConfiguration.imqAddressList, "192.168.56.101:7676");
        Connection connection = connFactory.createConnection();
        Topic topic = new Topic(TOPIC_NAME);
        session = connection.createSession(); 
        producer = session.createProducer(topic);
    }

    public void publishMsg(String msg) throws JMSException {
        TextMessage message = session.createTextMessage(msg);
        producer.send(message);
        System.out.println("Enviado: " + msg);
    }

    public static void main(String[] args) throws JMSException {
        PublishToTopic pub = new PublishToTopic();
        Scanner kb = new Scanner(System.in);

        //Envía un mensaje por cada línea hasta que se escriba 'exit'
        System.out.println("Escriba sus mensajes !!!");
        while (true) {
            String msg = kb.nextLine();
            if (msg.equalsIgnoreCase("exit")) {
                pub.publishMsg(msg);
                pub.session.close();
                System.exit(0);
            } else {
                pub.publishMsg(msg);
            }
        }
    } 

}

package zad1;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Chat{
    private Context context;
    private ConnectionFactory factory;
    private Connection connection;
    private String factoryName;
    private String destName;
    private Destination dest;
    private Session session;
    private MessageProducer sender;
    private Topic topic;
    private TopicSubscriber subscriber;
    private String subscriptionName;
    private ChatApp chatApp;
    public Chat(String topic_name, String factoryName, String subscriptionName, ChatApp chatApp){
        this.chatApp = chatApp;
        this.factoryName = factoryName;
        this.destName = topic_name;
        this.subscriptionName = subscriptionName;
        try { // init config
            context = new InitialContext();
            factory = (ConnectionFactory) context.lookup(factoryName);
            topic = (Topic) context.lookup(destName);
            connection = factory.createConnection();
            session = connection.createSession(
                    false, Session.AUTO_ACKNOWLEDGE);
            subscriber = session.createDurableSubscriber(
                    topic, subscriptionName);
            dest = (Destination) context.lookup(destName);
            sender = session.createProducer(dest);
            connection.start();
            start();
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Thread receive = new Thread(this::receiving);
        receive.start();
    }
    private void receiving(){
        while(true){
            chatApp.addNewMessage(receiveMessages());
        }
    }
    public void sendMessage(String msg){
        try {
            TextMessage message = session.createTextMessage();
            message.setText(msg);
            sender.send(message);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
    private String receiveMessages(){
        try{
            Message message = subscriber.receive();
            TextMessage text = (TextMessage) message;
            return text.getText();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

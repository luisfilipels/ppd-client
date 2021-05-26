package networking;

import main.MainViewController;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import utils.ClientDataSingleton;

import javax.jms.*;
import java.util.List;
import java.util.concurrent.Semaphore;

class Subscriber implements MessageListener{

    // TODO: Change this
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private MainViewController controller;

    public void initialize(MainViewController controller) {
        this.controller = controller;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            List<String> subscribedTopics = ClientDataSingleton.getInstance().subscribedTopics;

            for (String topic : subscribedTopics) {
                Destination dest = session.createTopic(topic);
                MessageConsumer subscriber = session.createConsumer(dest);
                subscriber.setMessageListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            try{
                if (controller != null) {
                    controller.addLog(((TextMessage) message).getText());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

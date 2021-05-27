package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import utils.ClientDataSingleton;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadDataController {
    // TODO: Change this
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;


    @FXML
    private ListView<CheckBox> topicListView;
    ObservableList<CheckBox> topicList;

    @FXML
    private TextField brokerAddressField;

    ConnectionFactory connectionFactory;

    private static final String PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    // Got this function from StackOverFlow
    public static boolean isValid(final String ip){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    private String getIp() {
        String ip = brokerAddressField.getText().trim();
        if (ip.trim().equals("") || !isValid(ip)) {
            return "failover://tcp://localhost:61616";
        } else {
            return "failover://tcp://" + ip + ":61616";
        }
    }

    private void createConnectionAndRefreshTopicsWithIP(String ip) {
        connectionFactory = new ActiveMQConnectionFactory(ip);
        try {
            Connection connection = connectionFactory.createConnection();
            DestinationSource destinationSource = new DestinationSource(connection);
            connection.start();
            destinationSource.start();
            getTopicsUsingSource(destinationSource);
            connection.close();
            destinationSource.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRefreshButton() {
        String ip = getIp();
        createConnectionAndRefreshTopicsWithIP(ip);
    }

    private void getTopicsUsingSource(DestinationSource source) {
        topicList.clear();
        try {
            var list = source.getTopics();
            for (var v : list) {
                CheckBox b = new CheckBox();
                b.setText(v.getTopicName());
                topicList.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        topicList = FXCollections.observableArrayList();
        topicListView.setItems(topicList);
        //createConnectionAndRefreshTopicsWithIP(url);
    }

    @FXML
    public void onConfirmButton(ActionEvent event) {
        // TODO: Select server IP

        ClientDataSingleton clientData = ClientDataSingleton.getInstance();
        for (CheckBox box : topicList) {
            if (box.isSelected()) {
                clientData.subscribedTopics.add(box.getText());
            }
        }
        clientData.setBrokerIP(getIp());

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            primaryStage.setTitle("Cliente");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

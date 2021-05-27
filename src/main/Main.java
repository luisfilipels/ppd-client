package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(1);
        Stage popup = new Stage();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("popupName.fxml")
        );
        System.out.println(2);
        popup.initStyle(StageStyle.UTILITY);
        Parent root2 = loader.load();
        System.out.println(3);
        popup.setTitle("Leitura de dados");
        popup.setResizable(false);
        popup.setScene(new Scene(root2, 400, 450));
        popup.show();
        System.out.println(4);
        popup.setAlwaysOnTop(true);
        popup.toFront();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

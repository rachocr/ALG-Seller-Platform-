package application.listguard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load login view
        FXMLLoader loginLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene loginScene = new Scene(loginLoader.load(), 1600, 900);

        String css = Main.class.getResource("/application/listguard/application.css").toExternalForm();
        loginScene.getStylesheets().add(css);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Amazon List Guard");
        primaryStage.setScene(loginScene);

        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/assets/Logo.png")));

        primaryStage.show();


        // Load customer view
        FXMLLoader customerLoader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene customerScene = new Scene(customerLoader.load(), 741, 900);
        customerScene.getStylesheets().add(css);

        Stage customerStage = new Stage();
        customerStage.setResizable(false);
        customerStage.setTitle("Customer View - Amazon List Guard");
        customerStage.setScene(customerScene);

        customerStage.getIcons().add(new Image(Main.class.getResourceAsStream("/assets/Logo.png")));

        customerStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
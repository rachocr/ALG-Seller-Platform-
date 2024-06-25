package application.listguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField emailTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        handleLogin();
                        event.consume();
                    }
                });
            }
        });
    }

    @FXML
    private void handleLogin() {
        String email = emailTF.getText();
        String password = passwordTF.getText();

        if (email.isEmpty()) {
            showAlert("You need to enter your email");
            return;
        }
        if (password.isEmpty()) {
            showAlert("You need to enter your password");
            return;
        }

        if (validateLogin(email, password)) {
            // Fetch seller data
            Seller seller = fetchSellerData(email);

            if (seller != null) {
                // Load the dashboard view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
                try {
                    Parent root = loader.load();

                    // Get the controller
                    DashboardController dashboardController = loader.getController();

                    // Pass seller data to DashboardController
                    dashboardController.initialize(seller);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Amazon List Guard");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully logged in!");
                    alert.showAndWait();

                    // Show the dashboard
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(root, 1600, 900));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Failed to fetch seller data. Please try again.");
            }
        } else {
            showAlert("Invalid email or password");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Amazon List Guard");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateLogin(String email, String password) {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM seller WHERE email_seller = ? AND pass_seller = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Seller fetchSellerData(String email) {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT name_seller, email_seller, shop_name, account_status FROM seller WHERE email_seller = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name_seller");
                String sellerEmail = resultSet.getString("email_seller");
                String shopName = resultSet.getString("shop_name");
                String accountStatus = resultSet.getString("account_status");
                return new Seller(name, sellerEmail, shopName, accountStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void showUnderDevelopmentDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Amazon List Guard");
        alert.setHeaderText(null);
        alert.setContentText("Please contact the administrator before proceeding.");
        alert.showAndWait();
    }

}

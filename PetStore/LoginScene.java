import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class LoginScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login - Pet Adoption");

        // Create the username and password fields
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Create the login button
        Button loginButton = new Button("Login");
        loginButton.setDisable(true);  // Disable login until fields are filled
        
        VBox usernameVBox = new VBox(5);
        Label usernameLabel = new Label("Username:");
        usernameVBox.getChildren().addAll(usernameLabel, usernameField);
        
        VBox passwordVBox = new VBox(5);
        Label passwordLabel = new Label("Password:");
        passwordVBox.getChildren().addAll(passwordLabel, passwordField);
        
        // Create the create account button
        Button createAccountButton = new Button("Create Account");

        // Add a listener to enable/disable login button based on the fields' content
        loginButton.disableProperty().bind(
                Bindings.isEmpty(usernameField.textProperty()).or(Bindings.isEmpty(passwordField.textProperty()))
        );

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            User u = DAOUser.authenticateUser(username, password);
            if (u != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Welcome to Pet Adoption");
                alert.setContentText("You have successfully logged in.");
                alert.showAndWait();
                openTabsApp(primaryStage, u.getUserId());
            } else {
                // Failure - Show error dialog
                showErrorDialog("Invalid credentials", "Please check your username and password.");
            }
        });

        // Create account button action
        createAccountButton.setOnAction(e -> {
            openCreateAccountWindow(primaryStage);
        });

        // Layout
        VBox layout = new VBox(10, usernameVBox, passwordVBox, loginButton, createAccountButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to open Pet Adoption App (After successful login)
    private void openTabsApp(Stage primaryStage, int userId) {
        MainTabsApp tabsApp = new MainTabsApp(userId);
        Stage stage = new Stage();
        tabsApp.start(stage);
        
        // Close the login stage when pet adoption app opens
        primaryStage.close();
    }

    // Method to show an error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to open Create Account Window
    private void openCreateAccountWindow(Stage parentStage) {
        // Create the new window (Scene) to register a new user
        Stage createAccountStage = new Stage();
        createAccountStage.setTitle("Create Account");

        // Fields for account creation
        TextField newUsernameField = new TextField();
        PasswordField newPasswordField = new PasswordField();
        TextField fullNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        
        VBox newUsernameVBox = new VBox(5);
        Label newUsernameLabel = new Label("Username:");
        newUsernameVBox.getChildren().addAll(newUsernameLabel, newUsernameField);
        
        VBox newPasswordVBox = new VBox(5);
        Label newPasswordLabel = new Label("Password:");
        newPasswordVBox.getChildren().addAll(newPasswordLabel, newPasswordField);
        
        VBox fullNameVBox = new VBox(5);
        Label fullNameLabel = new Label("Full Name:");
        fullNameVBox.getChildren().addAll(fullNameLabel, fullNameField);
        
        VBox emailVBox = new VBox(5);
        Label emailLabel = new Label("Email:");
        emailVBox.getChildren().addAll(emailLabel, emailField);
        
        VBox phoneVBox = new VBox(5);
        Label phoneLabel = new Label("Phone number:");
        phoneVBox.getChildren().addAll(phoneLabel, phoneField);
        
        VBox addressVBox = new VBox(5);
        Label addressLabel = new Label("Address:");
        addressVBox.getChildren().addAll(addressLabel, addressField);

        // Create the buttons
        Button createButton = new Button("Create Account");
        Button cancelButton = new Button("Cancel");

        // Action to create account
        createButton.setOnAction(e -> {
            String username = newUsernameField.getText();
            String password = newPasswordField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            // Check if all fields are filled (basic validation)
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                showErrorDialog("Input Error", "Please fill in all fields.");
            } else {
                try {
                // Create user (assuming DAOUser.addUser() inserts the new user into the database)
                User newUser = new User(username, password, fullName, email, phone, address);
                DAOUser daoUser = new DAOUser();
                daoUser.addUser(newUser);  // Add user to the database

                // Show success dialog
                showSuccessDialog("Account Created", "Your account has been created successfully.");
                createAccountStage.close();  // Close the window
                    } catch (Exception ex) {
                // Log the exception if something goes wrong
                System.out.println("Error creating account: " + ex.getMessage());
                showErrorDialog("Account Creation Error", "An error occurred while creating the account.");
                    }
            }
        });

        // Cancel button action
        cancelButton.setOnAction(e -> createAccountStage.close());

        // Layout for account creation
        VBox layout = new VBox(10, newUsernameVBox, newPasswordVBox, fullNameVBox, emailVBox, phoneVBox, addressVBox, createButton, cancelButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene createAccountScene = new Scene(layout, 300, 420);
        createAccountStage.setScene(createAccountScene);
        createAccountStage.show();
    }

    // Method to show success dialog
    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

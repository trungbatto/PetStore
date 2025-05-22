import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.List;
import java.text.NumberFormat; // Added for simpler number formatting
import java.util.Locale; // For locale-specific formatting
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserInfoApp extends VBox {

    private int userId;
    private VBox inventoryVBox;
    private DAOUserInventory daoUserInventory;
    private ScrollPane inventoryScrollPane;
    private Button purchaseButton;
    private NumberFormat numberFormat; // For formatting prices

    public UserInfoApp(int userId) {
        this.userId = userId;
        this.daoUserInventory = new DAOUserInventory();
        
        purchaseButton = new Button("Purchase");
        purchaseButton.setOnAction(e -> showPurchaseVerification());

        // Initialize NumberFormat for formatting prices (with 2 decimal places and thousands separators)
        this.numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(2); // Ensure 2 decimal places
        numberFormat.setMaximumFractionDigits(2);

        DAOUser dao = new DAOUser();
        User user = dao.getUserById(userId);

        // Display user details
        if (user != null) {
            this.getChildren().addAll(
                new Text("Full Name: " + user.getFullName()),
                new Text("Username: " + user.getUsername()),
                new Text("Email: " + user.getEmail()),
                new Text("Phone: " + user.getPhone()),
                new Text("Address: " + user.getAddress()),
                new Text("Created Time: " + user.getCreatedTime())
            );
        } else {
            this.getChildren().add(new Text("User not found."));
        }

        // Add space between user info and inventory
        this.setSpacing(20);

        // Initialize the inventory section
        this.inventoryVBox = new VBox(10);
        this.inventoryScrollPane = new ScrollPane(inventoryVBox);
        inventoryScrollPane.setFitToWidth(true);
        inventoryScrollPane.setPrefHeight(400);
        inventoryScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        inventoryScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Load initial inventory
        refreshInventory();

        // Add inventory scroll pane to the layout
        this.getChildren().addAll(inventoryScrollPane, purchaseButton);

        // Set overall layout style
        this.setStyle("-fx-padding: 20;");
    }

    // Method to refresh the inventory display
    public void refreshInventory() {
        // Clear current inventory display
        inventoryVBox.getChildren().clear();

        // Fetch updated inventory
        List<UserInventory> userInventoryItems = daoUserInventory.getInventoryByUserId(userId);

        // Iterate through inventory items and rebuild the UI
        for (UserInventory inventory : userInventoryItems) {
            HBox productHBox = new HBox(10); // Horizontal layout for each product
            productHBox.setPrefHeight(100); // Set a fixed height for each row
            productHBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 10;"); // Add a bottom border for separation

            // Product image section
            Image image = new Image(inventory.getImage(), 80, 80, true, true);
            ImageView imageView = new ImageView(image);
            HBox imageSection = new HBox(imageView);
            imageSection.setMinWidth(100); // Fixed width for image section
            imageSection.setAlignment(Pos.CENTER_LEFT);

            // Product details section (Name only)
            VBox productInfoVBox = new VBox(5);
            Label productName = new Label(inventory.getProductName());
            productName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            productInfoVBox.getChildren().add(productName); // Only name here
            HBox infoSection = new HBox(productInfoVBox);
            infoSection.setMinWidth(400); // Wider section for product info
            infoSection.setAlignment(Pos.CENTER_LEFT);

            // Spacer to push price, quantity, and delete button to the right
            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS); // Spacer takes up remaining space

            // Price section (separated like quantity and delete button)
            Label productPrice = new Label("$" + numberFormat.format(inventory.getPrice())); // Use NumberFormat instead of String.format
            productPrice.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 14;");
            HBox priceSection = new HBox(productPrice);
            priceSection.setMinWidth(150); // Fixed width for price section (same as quantity)
            priceSection.setAlignment(Pos.CENTER);

            // Quantity section
            Label quantityLabel = new Label("Quantity: " + inventory.getQuantity());
            quantityLabel.setStyle("-fx-font-size: 14;");
            HBox quantitySection = new HBox(quantityLabel);
            quantitySection.setMinWidth(150); // Fixed width for quantity section
            quantitySection.setAlignment(Pos.CENTER);

            // Delete button section
            Button deleteButton = new Button("Remove");
            deleteButton.setStyle("-fx-text-fill: red; -fx-font-size: 14;");
            deleteButton.setOnAction(e -> {
                inventoryVBox.getChildren().remove(productHBox); // Remove from UI
                
                Product product = DAOProduct.getProductById(inventory.getProductId());
                int newStock = product.getStockQuantity() + inventory.getQuantity();
                DAOProduct.updateProductQuantity(newStock, inventory.getProductId());
                
                daoUserInventory.removeInventoryItem(userId, inventory.getProductId()); // Remove from backend
            });
            HBox deleteSection = new HBox(deleteButton);
            deleteSection.setMinWidth(100); // Fixed width for delete button section
            deleteSection.setAlignment(Pos.CENTER_RIGHT);

            // Add all sections to the product HBox
            productHBox.getChildren().addAll(imageSection, infoSection, spacer, priceSection, quantitySection, deleteSection);
            productHBox.setAlignment(Pos.CENTER_LEFT);

            // Add the productHBox to the inventoryVBox
            inventoryVBox.getChildren().add(productHBox);
        }
    }
    
    public void confirmPurchase()
    {
        List<UserInventory> userInventoryItems = daoUserInventory.getInventoryByUserId(userId);

        if (userInventoryItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty. Please add items to purchase.");
            alert.showAndWait();
            return;
        }
    
        boolean success = true;
    
        for (UserInventory item : userInventoryItems) {
            Transaction transaction = new Transaction(
                userId,
                "product", // type
                item.getPrice(),
                item.getQuantity()
            );
    
            boolean inserted = DAOTransaction.insertTransaction(transaction);
            if (!inserted) {
                success = false;
                break;
            }
    
            // Remove the inventory entry
            daoUserInventory.removeInventoryItem(userId, item.getProductId());
        }
    
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Purchase Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your purchase has been completed successfully.");
            alert.showAndWait();
    
            refreshInventory(); // Refresh UI
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Purchase Failed");
            alert.setHeaderText(null);
            alert.setContentText("There was an error processing your transaction. Please try again.");
            alert.showAndWait();
        }
    }
    
    private void showPurchaseVerification() {

        Stage confirmationStage = new Stage();
        confirmationStage.setTitle("Confirm Purchase");
    
        VBox confirmationLayout = new VBox(10);
        confirmationLayout.setPadding(new Insets(20));
        confirmationLayout.setAlignment(Pos.CENTER);
    
        // Username and password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
    
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        
        VBox usernameVBox = new VBox();
        Label usernameLabel = new Label("Username");
        usernameVBox.getChildren().addAll(usernameLabel, usernameField);
        
        VBox passwordVBox = new VBox();
        Label passwordLabel = new Label("Password");
        passwordVBox.getChildren().addAll(passwordLabel, passwordField);
    
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-font-size: 14px;");
    
        // Button action to verify the credentials
        confirmButton.setOnAction(event -> {
            User u = DAOUser.authenticateSpecificUser(userId, usernameField.getText(), passwordField.getText());
            if (u != null) {
                // If credentials are correct, adopt the pet and show success message
                confirmPurchase();
                confirmationStage.close();
            } else {
                // Show error message if credentials are incorrect
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid credentials");
                alert.setHeaderText(null);
                alert.setContentText("Please check your username and password.");
                alert.showAndWait();
            }
        });
    
        // Add elements to the layout
        confirmationLayout.getChildren().addAll(usernameVBox, passwordVBox, confirmButton);
    
        Scene confirmationScene = new Scene(confirmationLayout, 300, 200);
        confirmationStage.setScene(confirmationScene);
        confirmationStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the parent window
        confirmationStage.show();
    }
}
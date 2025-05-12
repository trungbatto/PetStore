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
import java.util.List;
import java.text.NumberFormat; // Added for simpler number formatting
import java.util.Locale; // For locale-specific formatting

public class UserInfoApp extends VBox {

    private int userId;
    private VBox inventoryVBox;
    private DAOUserInventory daoUserInventory;
    private ScrollPane inventoryScrollPane;
    private NumberFormat numberFormat; // For formatting prices

    public UserInfoApp(int userId) {
        this.userId = userId;
        this.daoUserInventory = new DAOUserInventory();

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
        this.getChildren().add(inventoryScrollPane);

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
}
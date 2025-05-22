import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ProductStoreApp extends VBox {

    private int userId;
    private UserInfoApp userInfoApp;
    
    public ProductStoreApp(int userId, UserInfoApp userInfoApp) {
        this.userId = userId;
        this.userInfoApp = userInfoApp;
        
        this.setSpacing(20);
        this.setPadding(new Insets(10));

        HBox filterPanel = new HBox(15);
        filterPanel.setAlignment(Pos.CENTER);
        filterPanel.setPadding(new Insets(10));

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by Product Name");
        searchBar.setPrefWidth(200);

        ChoiceBox<String> categoryBox = new ChoiceBox<>();
        categoryBox.getItems().addAll("All", "Toy", "Accessory");
        categoryBox.setValue("All");

        ChoiceBox<String> availabilityBox = new ChoiceBox<>();
        availabilityBox.getItems().addAll("All", "Available", "Out of Stock");
        availabilityBox.setValue("All");

        VBox searchVBox = new VBox(5, new Label("Search"), searchBar);
        VBox categoryVBox = new VBox(5, new Label("Category"), categoryBox);
        VBox availabilityVBox = new VBox(5, new Label("Availability"), availabilityBox);

        filterPanel.getChildren().addAll(searchVBox, categoryVBox, availabilityVBox);

        GridPane gridPane = createProductGrid();

        searchBar.textProperty().addListener((obs, oldVal, newVal) ->
            updateProductGrid(gridPane, newVal, categoryBox.getValue(), availabilityBox.getValue())
        );
        categoryBox.valueProperty().addListener((obs, oldVal, newVal) ->
            updateProductGrid(gridPane, searchBar.getText(), newVal, availabilityBox.getValue())
        );
        availabilityBox.setOnAction(e ->
            updateProductGrid(gridPane, searchBar.getText(), categoryBox.getValue(), availabilityBox.getValue())
        );

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        this.getChildren().addAll(filterPanel, scrollPane);
    }

    private GridPane createProductGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(90);
        grid.setVgap(30);

        DAOProduct dao = new DAOProduct();
        List<Product> products = dao.getAllProducts();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int row = i / 4, col = i % 4;

            VBox box = createProductBox(product);
            grid.add(box, col, row);
        }

        return grid;
    }

    private void updateProductGrid(GridPane grid, String search, String category, String availability) {
        grid.getChildren().clear();

        DAOProduct dao = new DAOProduct();
        List<Product> products = dao.getAllProducts();

        products = products.stream()
            .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
            .filter(p -> category.equals("All") || p.getCategory().equalsIgnoreCase(category))
            .filter(p -> {
                if (availability.equals("Available")) return p.isAvailable();
                if (availability.equals("Out of Stock")) return !p.isAvailable();
                return true;
            })
            .collect(Collectors.toList());

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int row = i / 4, col = i % 4;
            VBox box = createProductBox(product);
            grid.add(box, col, row);
        }
    }

    private VBox createProductBox(Product product) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(220);
        box.setPadding(new Insets(10));

        Image image = new Image(product.getImage(), 200, 200, false, false);
        ImageView imageView = new ImageView(image);

        Label label = new Label(
            product.getName() + "\n" +
            "Category: " + product.getCategory() + "\n" +
            "Price: $" + product.getPrice() + "\n" +
            "Available: " + (product.isAvailable() ? "Yes" : "No")
        );
        label.setWrapText(true);
        label.setMaxWidth(200);
        label.setStyle("-fx-font-size: 12px;");

        imageView.setOnMouseClicked(e -> showProductDetails(product));
        box.getChildren().addAll(imageView, label);
        return box;
    }

    private void showProductDetails(Product product) {
        Stage stage = new Stage();
        stage.setTitle("Product Details");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Image image = new Image(product.getImage(), 300, 300, false, false);
        ImageView imageView = new ImageView(image);

        Label details = new Label(
            "Name: " + product.getName() + "\n" +
            "Category: " + product.getCategory() + "\n" +
            "Price: $" + product.getPrice() + "\n" +
            "Description: " + product.getDescription() + "\n" +
            "Available: " + (product.isAvailable() ? "Yes" : "No")
        );
        details.setStyle("-fx-font-size: 16px;");
        
        TextField quantityField = new TextField("1");
        quantityField.setPromptText("Enter quantity");
        quantityField.setMaxWidth(100);

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-font-size: 14px;");

    // Assume you already have currentUserId
    addToCartButton.setOnAction(e -> {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) throw new NumberFormatException();

            if (quantity > product.getStockQuantity()) {
                showAlert(Alert.AlertType.ERROR, "Not enough stock!");
                return;
            }

            addToInventory(product, userId, quantity);
            showAlert(Alert.AlertType.INFORMATION, "Item added to your inventory!");
            
            userInfoApp.refreshInventory(); //Refresh the userInfo

            stage.close();
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid quantity.");
        }
    });

        layout.getChildren().addAll(imageView, details, quantityField, addToCartButton);
        stage.setScene(new Scene(layout, 470, 600));
        stage.show();
    }
    
    private void addToInventory(Product product, int id, int quantity) 
    {
        DAOProduct daoProduct = new DAOProduct();
        
        product.reduceStockQuantity(quantity);
        daoProduct.updateProduct(product);
        
        UserInventory inventory = new UserInventory(id, product.getProductId(), product.getName(), quantity, product.getPrice(), product.getImage());
        DAOUserInventory.addInventory(inventory, product);
    }
    
    private void showAlert(Alert.AlertType type, String message) {
    Alert alert = new Alert(type);
    alert.setTitle("Notification");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
    }


}

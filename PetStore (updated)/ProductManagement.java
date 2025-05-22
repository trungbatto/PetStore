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

public class ProductManagement extends VBox {

    private GridPane productGrid;
    private TextField searchBar;
    private ChoiceBox<String> categoryChoiceBox;

        public ProductManagement() {
            this.setSpacing(20);
            this.setPadding(new Insets(10));
    
            HBox filterPanel = new HBox(15);
            filterPanel.setAlignment(Pos.CENTER);
            filterPanel.setPadding(new Insets(10));
    
            searchBar = new TextField();
            searchBar.setPromptText("Search by Product Name");
            searchBar.setPrefWidth(200);
    
            categoryChoiceBox = new ChoiceBox<>();
            categoryChoiceBox.getItems().addAll("All", "Toy", "Accessory");
            categoryChoiceBox.setValue("All");
            
            Button addButton = new Button("Add Product");
            addButton.setOnAction(e -> openAddProductForm());
    
            VBox searchVBox = new VBox(5, new Label("Search"), searchBar);
            VBox categoryVBox = new VBox(5, new Label("Category"), categoryChoiceBox);
            VBox addVBox = new VBox(5, new Label("Add Product"), addButton);
            
    
            filterPanel.getChildren().addAll(searchVBox, categoryVBox, addVBox);
    
            productGrid = createProductGrid();
    
            searchBar.textProperty().addListener((obs, oldVal, newVal) ->
                updateProductGrid(productGrid, newVal, categoryChoiceBox.getValue())
            );
            categoryChoiceBox.valueProperty().addListener((obs, oldVal, newVal) ->
                updateProductGrid(productGrid, searchBar.getText(), newVal)
            );
    
            ScrollPane scrollPane = new ScrollPane(productGrid);
            scrollPane.setFitToWidth(true);
            scrollPane.setPadding(new Insets(10));
    
            this.getChildren().addAll(filterPanel, scrollPane);
        }

        private GridPane createProductGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(90);
        gridPane.setVgap(30);
    
        DAOProduct daoProduct = new DAOProduct();
        List<Product> productList = daoProduct.getAllProducts();
    
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            int row = i / 4;
            int col = i % 4;
    
            VBox productBox = new VBox(5);
            productBox.setAlignment(Pos.CENTER);
            productBox.setPrefWidth(220);
            productBox.setPadding(new Insets(10));
    
            Image image;
            try {
                image = new Image(product.getImage(), 200, 200, false, false);
                if (image.isError()) throw new Exception();
            } catch (Exception e) {
                image = new Image("file:default.jpeg", 200, 200, false, false);
            }
    
            ImageView imageView = new ImageView(image);
    
            Label productLabel = new Label(
                product.getName() + "\n" +
                "Category: " + product.getCategory() + "\n" +
                "Price: $" + product.getPrice() + "\n" +
                "In Stock: " + product.getStockQuantity()
            );
            productLabel.setWrapText(true);
            productLabel.setMaxWidth(200);
            productLabel.setStyle("-fx-font-size: 12px;");
    
            productBox.getChildren().addAll(imageView, productLabel);
            gridPane.add(productBox, col, row);
    
            imageView.setOnMouseClicked(event -> showProductDetails(product));
        }
    
        return gridPane;
    }

    private void updateProductGrid(GridPane grid, String search, String category) {
        productGrid.getChildren().clear();

        DAOProduct daoProduct = new DAOProduct();
        List<Product> productList = daoProduct.getAllProducts();
    
        productList = productList.stream()
            .filter(p -> p.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
            .filter(p -> category.equals("All") || p.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            int row = i / 4;
            int col = i % 4;
    
            VBox productBox = new VBox(5);
            productBox.setAlignment(Pos.CENTER);
            productBox.setPrefWidth(220);
            productBox.setPadding(new Insets(10));
    
            Image image = new Image(product.getImage(), 200, 200, false, false);
            ImageView imageView = new ImageView(image);
    
            Label productLabel = new Label(
                product.getName() + "\n" +
                "Category: " + product.getCategory() + "\n" +
                "Price: $" + product.getPrice() + "\n" +
                "In Stock: " + product.getStockQuantity()
            );
            productLabel.setWrapText(true);
            productLabel.setMaxWidth(200);
            productLabel.setStyle("-fx-font-size: 12px;");
    
            productBox.getChildren().addAll(imageView, productLabel);
            productGrid.add(productBox, col, row);
    
            imageView.setOnMouseClicked(event -> showProductDetails(product));
        }
    }

    private void showProductDetails(Product product) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Product Detail");
        detailStage.initModality(Modality.APPLICATION_MODAL);
    
        VBox detailLayout = new VBox(10);
        detailLayout.setPadding(new Insets(20));
        detailLayout.setAlignment(Pos.CENTER);
    
        Image image = new Image(product.getImage(), 300, 300, false, false);
        ImageView detailImage = new ImageView(image);
    
        Label detailLabel = new Label(
            "Name: " + product.getName() + "\n" +
            "Category: " + product.getCategory() + "\n" +
            "Price: $" + product.getPrice() + "\n" +
            "Quantity: " + product.getStockQuantity() + "\n" +
            "Description: " + product.getDescription()
        );
        detailLabel.setStyle("-fx-font-size: 16px;");
    
        Button modifyButton = new Button("Modify");
        modifyButton.setStyle("-fx-font-size: 14px;");
        modifyButton.setOnAction(event -> openModifyProductForm(product, detailStage));
    
        detailLayout.getChildren().addAll(detailImage, detailLabel, modifyButton);
        detailStage.setScene(new Scene(detailLayout, 470, 600));
        detailStage.show();
    }


    private void openModifyProductForm(Product product, Stage parentStage) {
        Stage modifyStage = new Stage();
        modifyStage.setTitle("Modify Product");
        modifyStage.initModality(Modality.APPLICATION_MODAL);
    
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
    
        TextField nameField = new TextField(product.getName());
        ChoiceBox<String> categoryChoiceBox = new ChoiceBox<>();
        categoryChoiceBox.getItems().addAll("Toy", "Accessory");
        categoryChoiceBox.setValue(product.getCategory());
        
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField quantityField = new TextField(String.valueOf(product.getStockQuantity()));
        TextArea descField = new TextArea(product.getDescription());
        TextField imageField = new TextField(product.getImage());
    
        Button saveButton = new Button("Save");
    
        saveButton.setOnAction(e -> {
            product.setName(nameField.getText());
            product.setCategory(categoryChoiceBox.getValue());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setStockQuantity(Integer.parseInt(quantityField.getText()));
            product.setDescription(descField.getText());
            product.setImage(imageField.getText());
    
            DAOProduct dao = new DAOProduct();
            if (dao.updateProduct(product)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product updated successfully!");
                alert.showAndWait();
                modifyStage.close();
                parentStage.close();
                refreshProductGrid();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update product.");
                alert.showAndWait();
            }
        });
    
        form.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Category:"), categoryChoiceBox,
            new Label("Price:"), priceField,
            new Label("Quantity:"), quantityField,
            new Label("Description:"), descField,
            new Label("Image Path:"), imageField,
            saveButton
        );
    
        modifyStage.setScene(new Scene(form));
        modifyStage.show();
    }


    private void openAddProductForm() {
        Stage addStage = new Stage();
        addStage.setTitle("Add New Product");
    
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);
    
        TextField nameField = new TextField();
        ChoiceBox<String> categoryChoiceBox = new ChoiceBox<>();
        categoryChoiceBox.getItems().addAll("Toy", "Accessory");
        TextField priceField = new TextField();
        TextField quantityField = new TextField();
        TextArea descriptionField = new TextArea();
        TextField imageField = new TextField();
    
        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Category:"), categoryChoiceBox);
        form.addRow(2, new Label("Price:"), priceField);
        form.addRow(3, new Label("Quantity:"), quantityField);
        form.addRow(4, new Label("Description:"), descriptionField);
        form.addRow(5, new Label("Image Path:"), imageField);
    
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                
                if (nameField.getText().isEmpty() || categoryChoiceBox.getValue().isEmpty() || priceField.getText().isEmpty() ||  quantityField.getText().isEmpty() || imageField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                    return;
                }
                
                Product newProduct = new Product(
                    nameField.getText(),
                    categoryChoiceBox.getValue(),
                    Double.parseDouble(priceField.getText()),
                    descriptionField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    imageField.getText()
                );
    
                DAOProduct dao = new DAOProduct();
                if (dao.insertProduct(newProduct)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product added successfully!");
                    alert.showAndWait();
                    addStage.close();
                    refreshProductGrid();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to add product").showAndWait();
                }
    
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid input").showAndWait();
            }
        });
    
        form.addRow(6, saveButton);
    
        addStage.setScene(new Scene(form));
        addStage.show();
    }


    private void refreshProductGrid() {
    updateProductGrid(
        productGrid,
        searchBar.getText(),
        categoryChoiceBox.getValue()
    );
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
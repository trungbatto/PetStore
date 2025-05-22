import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.util.List;
import java.util.stream.Collectors;

public class PetManagement extends VBox{

    private GridPane gridPane;
    private TextField searchBar;
    private ChoiceBox<String> speciesChoiceBox;
    private ChoiceBox<String> genderChoiceBox;
    private ChoiceBox<String> vaccinatedChoiceBox;
    private ChoiceBox<String> availableChoiceBox;
    
    public PetManagement() {
        this.setSpacing(20);
        this.setPadding(new Insets(10));

        // Search panel (HBox)
        HBox searchPanel = new HBox(15);
        searchPanel.setAlignment(Pos.CENTER);
        searchPanel.setPadding(new Insets(10));
        
        Button addPetButton = new Button("Add Pet");
        addPetButton.setOnAction(e -> openAddPetForm());

        // Search bar
        searchBar = new TextField();
        searchBar.setPromptText("Search by Pet Name");
        searchBar.setPrefWidth(200);

        // Species ChoiceBox
        speciesChoiceBox = new ChoiceBox<>();
        speciesChoiceBox.getItems().addAll("All", "Cat", "Dog");
        speciesChoiceBox.setValue("All");

        // Gender ChoiceBox
        genderChoiceBox = new ChoiceBox<>();
        genderChoiceBox.getItems().addAll("All", "Male", "Female");
        genderChoiceBox.setValue("All");
        
        vaccinatedChoiceBox = new ChoiceBox<>();
        vaccinatedChoiceBox.getItems().addAll("All", "Vaccinated", "Not Vaccinated");
        vaccinatedChoiceBox.setValue("All");
        
        availableChoiceBox = new ChoiceBox<>();
        availableChoiceBox.getItems().addAll("All", "Available", "Adopted");
        availableChoiceBox.setValue("All");
        
        VBox addPetVBox = new VBox(5);
        Label addPetLabel = new Label("Add a new pet");
        addPetVBox.getChildren().addAll(addPetLabel, addPetButton);
        
        VBox searchVBox = new VBox(5);
        Label searchLabel = new Label("Search");
        searchVBox.getChildren().addAll(searchLabel, searchBar);
        
        // Species ChoiceBox with label
        VBox speciesVBox = new VBox(5);
        Label speciesLabel = new Label("Species");
        speciesVBox.getChildren().addAll(speciesLabel, speciesChoiceBox);
        
        // Gender ChoiceBox with label
        VBox genderVBox = new VBox(5);
        Label genderLabel = new Label("Gender");
        genderVBox.getChildren().addAll(genderLabel, genderChoiceBox);
        
        // Vaccinated ChoiceBox with label
        VBox vaccinatedVBox = new VBox(5);
        Label vaccinatedLabel = new Label("Vaccinated");
        vaccinatedVBox.getChildren().addAll(vaccinatedLabel, vaccinatedChoiceBox);
        
        // Available ChoiceBox with label
        VBox availableVBox = new VBox(5);
        Label availableLabel = new Label("Available");
        availableVBox.getChildren().addAll(availableLabel, availableChoiceBox);
        
        // Add UI components to search panel
        searchPanel.getChildren().addAll(addPetVBox, searchVBox, speciesVBox, genderVBox, vaccinatedVBox, availableVBox);

        // Main Grid of Pets
        gridPane = createPetGrid();

        // Update the pet grid based on search criteria
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> 
    updatePetGrid(gridPane, newValue, speciesChoiceBox.getValue(), genderChoiceBox.getValue(), vaccinatedChoiceBox.getValue(),availableChoiceBox.getValue())
);

speciesChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> 
    updatePetGrid(gridPane, searchBar.getText(), newValue, genderChoiceBox.getValue(), vaccinatedChoiceBox.getValue(),availableChoiceBox.getValue())
);

genderChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> 
    updatePetGrid(gridPane, searchBar.getText(), speciesChoiceBox.getValue(), newValue, vaccinatedChoiceBox.getValue(),availableChoiceBox.getValue())
);

vaccinatedChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> 
    updatePetGrid(gridPane, searchBar.getText(), speciesChoiceBox.getValue(), genderChoiceBox.getValue(), newValue, availableChoiceBox.getValue())
);

availableChoiceBox.setOnAction(event -> updatePetGrid(gridPane, searchBar.getText(), speciesChoiceBox.getValue(), genderChoiceBox.getValue(), vaccinatedChoiceBox.getValue(), availableChoiceBox.getValue()));

    
        
        // Wrap the gridPane in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        // Add search panel and pet grid to the main layout
        this.getChildren().addAll(searchPanel, scrollPane);
    }

    private GridPane createPetGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(90);
        gridPane.setVgap(30);

        DAOPet daoPet = new DAOPet();
        List<Pet> petList = daoPet.getAllPets();
        
        for (int i = 0; i < petList.size(); i++) {
            Pet pet = petList.get(i);
            int row = i / 4;
            int col = i % 4;

            VBox petBox = new VBox(5);
            petBox.setAlignment(Pos.CENTER);
            petBox.setPrefWidth(220);
            petBox.setPadding(new Insets(10));

            Image image = new Image(pet.getImage(), 200, 200, false, false);
            try {
                image = new Image(pet.getImage(), 200, 200, false, false);
                if (image.isError()) throw new Exception(); // catch silent failures
            } 
            catch (Exception e) {
                image = new Image("file:default.jepg", 200, 200, false, false); // fallback
            }
            ImageView imageView = new ImageView(image);

            Label petLabel = new Label(
                pet.getName() + "\n" +
                "Gender: " + pet.getGender() + "\n" +
                "Age: " + pet.getAge() + "\n" +
                "Vaccinated: " + (pet.isVaccinated() ? "Vaccinated" : "Not vaccinated")
            );
            petLabel.setWrapText(true);
            petLabel.setMaxWidth(200);
            petLabel.setStyle("-fx-font-size: 12px;");

            petBox.getChildren().addAll(imageView, petLabel);
            gridPane.add(petBox, col, row);

            imageView.setOnMouseClicked(event -> showPetDetails(pet));
        }

        return gridPane;
    }

    private void updatePetGrid(GridPane gridPane, String searchText, String species, String gender, String vaccinatedFilter, String availableFilter)
     {
        gridPane.getChildren().clear();  // Clear the previous grid

        DAOPet daoPet = new DAOPet();
        List<Pet> petList = daoPet.getAllPets();

        // Filter pets based on the search criteria
        petList = petList.stream()
                .filter(pet -> pet.getName().toLowerCase().contains(searchText.toLowerCase()))
                .filter(pet -> (species.equals("All") || pet.getSpecies().equalsIgnoreCase(species)))
                .filter(pet -> (gender.equals("All") || pet.getGender().equalsIgnoreCase(gender)))
                .filter(pet -> {
                        if (vaccinatedFilter == null || vaccinatedFilter.equals("All")) {
                            return true;
                        } else if (vaccinatedFilter.equals("Vaccinated")) {
                            return pet.isVaccinated();
                        } else if (vaccinatedFilter.equals("Not Vaccinated")) {
                            return !pet.isVaccinated();
                        }
                            return true;
                })
                .filter(pet -> {
                        if (availableFilter == null || availableFilter.equals("All")) {
                            return true;
                        } else if (availableFilter.equals("Available")) {
                            return pet.isAvailable();
                        } else if (availableFilter.equals("Adopted")) {
                            return !pet.isAvailable();
                        }
                            return true;
                })
                .collect(Collectors.toList());

        // Add filtered pets to the grid
        for (int i = 0; i < petList.size(); i++) {
            Pet pet = petList.get(i);
            int row = i / 4;
            int col = i % 4;

            VBox petBox = new VBox(5);
            petBox.setAlignment(Pos.CENTER);
            petBox.setPrefWidth(220);
            petBox.setPadding(new Insets(10));

            Image image = new Image(pet.getImage(), 200, 200, false, false);
            ImageView imageView = new ImageView(image);

            Label petLabel = new Label(
                pet.getName() + "\n" +
                "Gender: " + pet.getGender() + "\n" +
                "Age: " + pet.getAge() + "\n" +
                "Vaccinated: " + (pet.isVaccinated() ? "Vaccinated" : "Not vaccinated")
            );
            petLabel.setWrapText(true);
            petLabel.setMaxWidth(200);
            petLabel.setStyle("-fx-font-size: 12px;");

            petBox.getChildren().addAll(imageView, petLabel);
            gridPane.add(petBox, col, row);

            imageView.setOnMouseClicked(event -> showPetDetails(pet));
        }
    }

    private void showPetDetails(Pet pet) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Pet Detail"); 

        // Setting the modality to block interaction with the primary window
        detailStage.initModality(Modality.APPLICATION_MODAL);
        
        Stage primaryStage = (Stage) detailStage.getOwner();
        if (primaryStage != null) {
            detailStage.initOwner(primaryStage);  // This ensures the owner is set for modality
        }
        
        VBox detailLayout = new VBox(10);
        detailLayout.setPadding(new Insets(20));
        detailLayout.setAlignment(Pos.CENTER);

        Image image = new Image(pet.getImage(), 300, 300, false, false);
        ImageView detailImage = new ImageView(image);

        Label detailLabel = new Label(
            "Name: " + pet.getName() + "\n" +
            "Gender: " + pet.getGender() + "\n" +
            "Age: " + pet.getAge() + "\n" + 
            "Species: " + pet.getSpecies() + "\n" +
            "Breed: " + pet.getBreed() + "\n" +
            "Description: " + pet.getDescription() + "\n" +
            "Vaccinated: " + (pet.isVaccinated() ? "Vaccinated" : "Not vaccinated") + "\n" +
            "Price: " + pet.getPrice() + "\n" +
            "Available: " + (pet.isAvailable() ? "Yes" : "No")
        );
        detailLabel.setStyle("-fx-font-size: 16px;");

        Button modifyButton = new Button("Modify");
        modifyButton.setStyle("-fx-font-size: 14px;");
        
        if (!pet.isAvailable()) {
            modifyButton.setDisable(true);
            modifyButton.setText("Pet Adopted");
        } else {modifyButton.setOnAction(event -> {
            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modify Pet");
            modifyStage.initModality(Modality.APPLICATION_MODAL);
        
            VBox form = new VBox(10);
            form.setPadding(new Insets(20));
            form.setAlignment(Pos.CENTER);
        
            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField(pet.getName());
            
            Label speciesLabel = new Label("Species:");
            ChoiceBox<String> speciesChoiceBox = new ChoiceBox<>();
            speciesChoiceBox.getItems().addAll("Dog", "Cat");
            speciesChoiceBox.setValue(pet.getSpecies());
            
            Label breedLabel = new Label("Breed:");
            TextField breedField = new TextField(pet.getBreed());
            
            Label ageLabel = new Label("Age:");
            TextField ageField = new TextField(String.valueOf(pet.getAge()));
            
            Label genderLabel = new Label("Gender:");
            ChoiceBox<String> genderChoiceBox = new ChoiceBox<>();
            genderChoiceBox.getItems().addAll("Male", "Female");
            genderChoiceBox.setValue(pet.getGender());
            
            Label descLabel = new Label("Description:");
            TextArea descField = new TextArea(pet.getDescription());
            descField.setPrefRowCount(3);
            
            Label priceLabel = new Label("Price:");
            TextField priceField = new TextField(String.valueOf(pet.getPrice()));
            
            Label imageLabel = new Label("Image URL:");
            TextField imageField = new TextField(pet.getImage());
            
            CheckBox vaccinatedBox = new CheckBox("Vaccinated");
            vaccinatedBox.setSelected(pet.isVaccinated());
            
            Button saveButton = new Button("Save");
            
            saveButton.setOnAction(e -> {
                pet.setName(nameField.getText());
                pet.setSpecies(speciesChoiceBox.getValue());
                pet.setBreed(breedField.getText());
                pet.setAge(Integer.parseInt(ageField.getText()));
                pet.setGender(genderChoiceBox.getValue());
                pet.setDescription(descField.getText());
                pet.setPrice(Double.parseDouble(priceField.getText()));
                pet.setImage(imageField.getText());
                pet.setVaccinated(vaccinatedBox.isSelected());
            
                DAOPet daoPet = new DAOPet();
                boolean success = daoPet.updatePetForManager(pet);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Modified successfully.");
                    alert.showAndWait();
                    modifyStage.close();
                    detailStage.close();
                    refreshPetGrid();                
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update pet.");
                    alert.showAndWait();
                }
            });
    
            
                form.getChildren().addAll(nameLabel, nameField, speciesLabel, speciesChoiceBox,
                    breedLabel, breedField,ageLabel, ageField, genderLabel, genderChoiceBox,
                    descLabel, descField, priceLabel, priceField, imageLabel, imageField,
                    vaccinatedBox,saveButton);
                modifyStage.setScene(new Scene(form));
                modifyStage.show();
                
            });
            }
            
            
            detailLayout.getChildren().addAll(detailImage, detailLabel, modifyButton);
    
            Scene detailScene = new Scene(detailLayout, 470, 630);
            detailStage.setScene(detailScene);
            detailStage.show();
    }

    private void refreshPetGrid() {
        updatePetGrid(
            gridPane,
            searchBar.getText(),
            speciesChoiceBox.getValue(),
            genderChoiceBox.getValue(),
            vaccinatedChoiceBox.getValue(),
            availableChoiceBox.getValue()
        );
        }   
        
        private void openAddPetForm() {
        Stage addStage = new Stage();
        addStage.setTitle("Add New Pet");
    
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);
    
        TextField nameField = new TextField();
        ChoiceBox<String> speciesChoiceBox = new ChoiceBox<>();
        speciesChoiceBox.getItems().addAll("Cat", "Dog");
        TextField breedField = new TextField();
        TextField ageField = new TextField();
        ChoiceBox<String> genderChoiceBox = new ChoiceBox<>();
        genderChoiceBox.getItems().addAll("Male", "Female");
        TextArea descriptionField = new TextArea();
        TextField priceField = new TextField();
        TextField imageField = new TextField();
        CheckBox vaccinatedCheckBox = new CheckBox("Vaccinated");
    
        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Species:"), speciesChoiceBox);
        form.addRow(2, new Label("Breed:"), breedField);
        form.addRow(3, new Label("Age:"), ageField);
        form.addRow(4, new Label("Gender:"), genderChoiceBox);
        form.addRow(5, new Label("Description:"), descriptionField);
        form.addRow(6, new Label("Price:"), priceField);
        form.addRow(7, new Label("Image Path:"), imageField);
        form.addRow(8, vaccinatedCheckBox);
    
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String species = speciesChoiceBox.getValue();
                String breed = breedField.getText();
                int age = Integer.parseInt(ageField.getText());
                String gender = genderChoiceBox.getValue();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                String image = imageField.getText();
                boolean vaccinated = vaccinatedCheckBox.isSelected();
    
                // Basic field validation
                if (name.isEmpty() || breed.isEmpty() || species.isEmpty() ||  gender == null || image.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                    return;
                }
    
                Pet newPet = new Pet(name, species, breed, age, gender, description, price, image, vaccinated);
                DAOPet daoPet = new DAOPet();
                boolean success = daoPet.insertPet(newPet);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Pet added successfully.");
                    addStage.close();
                    refreshPetGrid();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add pet.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Age and price must be valid numbers.");
            }
        });
    
        VBox root = new VBox(10, form, saveButton);
        root.setPadding(new Insets(10));
    
        Scene scene = new Scene(root);
        addStage.setScene(scene);
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.showAndWait();
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setContentText(content);
    alert.showAndWait();
    }
}

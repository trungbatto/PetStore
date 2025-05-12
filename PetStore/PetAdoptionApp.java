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

public class PetAdoptionApp extends VBox{

    public PetAdoptionApp() {
        // Main layout (VBox to hold search and pet grid)
        this.setSpacing(20);
        this.setPadding(new Insets(10));

        // Search panel (HBox)
        HBox searchPanel = new HBox(15);
        searchPanel.setAlignment(Pos.CENTER);
        searchPanel.setPadding(new Insets(10));

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by Pet Name");
        searchBar.setPrefWidth(200);

        // Species ChoiceBox
        ChoiceBox<String> speciesChoiceBox = new ChoiceBox<>();
        speciesChoiceBox.getItems().addAll("All", "Cat", "Dog");
        speciesChoiceBox.setValue("All");

        // Gender ChoiceBox
        ChoiceBox<String> genderChoiceBox = new ChoiceBox<>();
        genderChoiceBox.getItems().addAll("All", "Male", "Female");
        genderChoiceBox.setValue("All");
        
        ChoiceBox<String> vaccinatedChoiceBox = new ChoiceBox<>();
        vaccinatedChoiceBox.getItems().addAll("All", "Vaccinated", "Not Vaccinated");
        vaccinatedChoiceBox.setValue("All");
        
        ChoiceBox<String> availableChoiceBox = new ChoiceBox<>();
        availableChoiceBox.getItems().addAll("All", "Available", "Adopted");
        availableChoiceBox.setValue("All");
        
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
        searchPanel.getChildren().addAll(searchVBox, speciesVBox, genderVBox, vaccinatedVBox, availableVBox);

        // Main Grid of Pets
        GridPane gridPane = createPetGrid();

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

        Button adoptButton = new Button("Adopt");
        adoptButton.setStyle("-fx-font-size: 14px;");
        
        if (!pet.isAvailable()) {
            adoptButton.setDisable(true);
            adoptButton.setText("Unavailable");
        } else {adoptButton.setOnAction(event -> showAdoptionConfirmation(pet, detailStage));}    
        
        
        detailLayout.getChildren().addAll(detailImage, detailLabel, adoptButton);

        Scene detailScene = new Scene(detailLayout, 470, 630);
        detailStage.setScene(detailScene);
        detailStage.show();
    }
    
    private void showAdoptionConfirmation(Pet pet, Stage parentStage) {

    Stage confirmationStage = new Stage();
    confirmationStage.setTitle("Confirm Adoption");

    VBox confirmationLayout = new VBox(10);
    confirmationLayout.setPadding(new Insets(20));
    confirmationLayout.setAlignment(Pos.CENTER);

    // Username and password fields
    TextField usernameField = new TextField();
    usernameField.setPromptText("Enter your username");

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Enter your password");

    Button confirmButton = new Button("Confirm");
    confirmButton.setStyle("-fx-font-size: 14px;");

    
    // Button action to verify the credentials
    confirmButton.setOnAction(event -> {
        User u = DAOUser.authenticateUser(usernameField.getText(), passwordField.getText());
        if (u != null) {
            // If credentials are correct, adopt the pet and show success message
            adoptPet(pet, u.getUserId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Adopted successfully.");
            alert.showAndWait();
            confirmationStage.close();
            parentStage.close(); // Close the pet detail window after adoption
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
    confirmationLayout.getChildren().addAll(usernameField, passwordField, confirmButton);

    Scene confirmationScene = new Scene(confirmationLayout, 300, 200);
    confirmationStage.setScene(confirmationScene);
    confirmationStage.initOwner(parentStage); // Ensure the owner for modality
    confirmationStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the parent window
    confirmationStage.show();
}

private void adoptPet(Pet pet, int id) {
    // Example: Update pet status in the database and other relevant fields
    DAOPet daoPet = new DAOPet();
    pet.setAvailable(false);  // Mark pet as adopted (not available)
    pet.setOwnerId(id);
    daoPet.updatePet(pet);    // Assume updatePet updates the pet's data in the database
    Transaction transaction = new Transaction (id, "Adoption", pet.getPrice(), 1);
    DAOTransaction.insertTransaction(transaction);
}


}

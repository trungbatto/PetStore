import javafx.beans.property.*;

public class Pet {
    private final IntegerProperty petId = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty species = new SimpleStringProperty();
    private final StringProperty breed = new SimpleStringProperty();
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final StringProperty gender = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final BooleanProperty available = new SimpleBooleanProperty();
    private final IntegerProperty ownerId = new SimpleIntegerProperty();
    private final StringProperty image = new SimpleStringProperty();
    private final BooleanProperty vaccinated = new SimpleBooleanProperty();

    public Pet(String name, String species, String breed, int age, String gender, String description, double price, String image, boolean vaccinated) {
        this.name.set(name);
        this.species.set(species);
        this.breed.set(breed);
        this.age.set(age);
        this.gender.set(gender);
        this.description.set(description);
        this.price.set(price);
        this.image.set(image);
        this.vaccinated.set(vaccinated);
    }

    public Pet(int id,String name, String species, String breed, int age, String gender, String description, double price, boolean available, int ownerId, String image, boolean vaccinated) 
    {
        this.petId.set(id);
        this.name.set(name);
        this.species.set(species);
        this.breed.set(breed);
        this.age.set(age);
        this.gender.set(gender);
        this.description.set(description);
        this.price.set(price);
        this.available.set(available);
        this.ownerId.set(ownerId);
        this.image.set(image);
        this.vaccinated.set(vaccinated);
    }

    public int getPetId() {return petId.get();}
    public void setPetId(int id) {this.petId.set(id);}
    public IntegerProperty petIdProperty() {return petId;}

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    public String getSpecies() { return species.get(); }
    public void setSpecies(String value) { species.set(value); }
    public StringProperty speciesProperty() { return species; }

    public String getBreed() { return breed.get(); }
    public void setBreed(String value) { breed.set(value); }
    public StringProperty breedProperty() { return breed; }

    public int getAge() { return age.get(); }
    public void setAge(int value) { age.set(value); }
    public IntegerProperty ageProperty() { return age; }

    public String getGender() { return gender.get();}
    public void setGender(String gender) {this.gender.set(gender);}
    public StringProperty genderProperty() {return gender;}

    public String getDescription() { return description.get();}
    public void setDescription(String description) {this.description.set(description);}
    public StringProperty descriptionProperty() {return description;}

    public double getPrice() { return price.get(); }
    public void setPrice(double value) { price.set(value); }
    public DoubleProperty priceProperty() { return price; }    

    public boolean isAvailable() { return available.get();}
    public void setAvailable(boolean available) {this.available.set(available);}
    public BooleanProperty availableProperty() {return available;}

    public int getOwnerId() { return ownerId.get(); }
    public void setOwnerId(int ownerId) { this.ownerId.set(ownerId); }
    public IntegerProperty ownerIdProperty() { return ownerId; }

    public boolean isVaccinated() { return vaccinated.get(); }
    public void setVaccinated(boolean value) { vaccinated.set(value); }
    public BooleanProperty vaccinatedProperty() { return vaccinated; }

    public String getImage() { return image.get(); }
    public void setImage(String value) { image.set(value); }
    public StringProperty imageProperty() { return image; }
}


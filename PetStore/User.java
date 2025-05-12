import javafx.beans.property.*;
import java.time.LocalDateTime;

public class User {
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> createdTime = new SimpleObjectProperty<>();
    
    
    public User(int id, String u, String p)
    {
        id = userId.get();
        u = username.get();
        p = password.get();
    }
    public User(String username, String password, String fullName, String email, String phone, String address) {
        this.username.set(username);
        this.password.set(password);
        this.fullName.set(fullName);
        this.email.set(email);
        this.phone.set(phone);
        this.address.set(address);
    }
    
    public User(int userId, String username, String password, String fullName, String email, String phone, String address, LocalDateTime createdTime) {
        this.userId.set(userId); // Set the ID if available (for loaded data)
        this.username.set(username);
        this.password.set(password);
        this.fullName.set(fullName);
        this.email.set(email);
        this.phone.set(phone);
        this.address.set(address);
        this.createdTime.set(createdTime);
    }

    // Getters and setters
    public int getUserId() { return userId.get(); }
    public void setUserId(int id) { userId.set(id); }
    public IntegerProperty userIdProperty() { return userId; }

    public String getUsername() { return username.get(); }
    public void setUsername(String value) { username.set(value); }
    public StringProperty usernameProperty() { return username; }

    public String getPassword() { return password.get(); }
    public void setPassword(String value) { password.set(value); }
    public StringProperty passwordProperty() { return password; }
    
    public String getFullName() {return fullName.get();}
    public void setFullName(String fullName) {this.fullName.set(fullName);}
    public StringProperty fullNameProperty() {return fullName;}
    
    public String getAddress() {return address.get();}
    public void setAddress(String address) {this.address.set(address);}
    public StringProperty addressProperty() {return address;}

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getPhone() { return phone.get(); }
    public void setPhone(String value) { phone.set(value); }
    public StringProperty phoneProperty() { return phone; }
    
    public LocalDateTime getCreatedTime() { return createdTime.get(); }
    public void setCreatedTime(LocalDateTime value) { createdTime.set(value); }
    public ObjectProperty<LocalDateTime> createdTimeProperty() { return createdTime; }
}


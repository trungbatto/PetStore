import java.time.LocalDateTime;
import javafx.beans.property.*;

public class Transaction {
    private final IntegerProperty transactionId = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty transactionType = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty amount = new SimpleIntegerProperty();

    public Transaction(int transactionId, int userId, String transactionType, LocalDateTime date, double price, int amount) {
        this.transactionId.set(transactionId);
        this.userId.set(userId);
        this.transactionType.set(transactionType);
        this.date.set(date);
        this.price.set(price);
        this.amount.set(amount);
    }
    
    public Transaction(int userId, String transactionType, double price, int amount) {
        this.userId.set(userId);
        this.transactionType.set(transactionType);
        this.price.set(price);
        this.amount.set(amount);
    }
    

    public int getTransactionId() {return transactionId.get();}
    public void setTransactionId(int transactionId) {this.transactionId.set(transactionId);}
    public IntegerProperty transactionIdProperty() {return transactionId;}

    public int getUserId() {return userId.get();}
    public void setUserId(int userId) {this.userId.set(userId);}
    public IntegerProperty userIdProperty() {return userId;}

    public String getTransactionType() {return transactionType.get();}
    public void setTransactionType(String transactionType) {this.transactionType.set(transactionType);}
    public StringProperty transactionTypeProperty() {return transactionType;}

    public LocalDateTime getDate() {return date.get();}
    public void setDate(LocalDateTime date) {this.date.set(date);}
    public ObjectProperty<LocalDateTime> dateProperty() {return date;}
    
    public double getPrice() {return price.get();}
    public void setPrice(double price) {this.price.set(price);}
    public DoubleProperty priceProperty() { return price;}
    
    public int getAmount() {return amount.get();}
    public void setAmount(int value) {amount.set(value);}
    public IntegerProperty amountProperty() {return amount;}

}


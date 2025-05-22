import javafx.beans.property.*;
import java.time.LocalDateTime;

public class UserInventory {
    private final IntegerProperty inventoryId = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final StringProperty productName = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty image = new SimpleStringProperty();

    public UserInventory(int userId, int productId, String productName, int quantity, double price, String image) {
        this.userId.set(userId);
        this.productId.set(productId);
        this.productName.set(productName);
        this.quantity.set(quantity);
        this.price.set(price);
        this.image.set(image);
    }
    
    public UserInventory(int inventoryId, int userId, int productId, String productName, int quantity, double price, String image) 
    {
        this.inventoryId.set(inventoryId);
        this.userId.set(userId);
        this.productId.set(productId);
        this.productName.set(productName);
        this.quantity.set(quantity);
        this.price.set(price);
        this.image.set(image);
    }
    
    public int getInventoryId() { return inventoryId.get(); }
    public void setInventoryId(int id) { inventoryId.set(id); }
    public IntegerProperty InventoryIdProperty() { return inventoryId; }

    public int getUserId() { return userId.get(); }
    public void setUserId(int id) { userId.set(id); }
    public IntegerProperty userIdProperty() { return userId; }

    public int getProductId() { return productId.get(); }
    public void setProductId(int id) { productId.set(id); }
    public IntegerProperty productIdProperty() { return productId; }
    
    public String getProductName() { return productName.get(); }
    public void setProductName(String id) { productName.set(id); }
    public StringProperty productNameProperty() { return productName; }

    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int value) { quantity.set(value); }
    public IntegerProperty quantityProperty() { return quantity; }
    
    public double getPrice() { return price.get(); }
    public void setPrice(double value) { price.set(value); }
    public DoubleProperty priceQuantity() { return price; }
    
    public String getImage() { return image.get(); }
    public void setImage(String id) { image.set(id); }
    public StringProperty imageProperty() { return image; }
}


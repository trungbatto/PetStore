import javafx.beans.property.*;

public class Product {
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private IntegerProperty stockQuantity = new SimpleIntegerProperty();
    private final StringProperty image = new SimpleStringProperty();

    public Product(int productId, String name, String category, double price, String description,  int stockQuantity, String image) {
        this.productId.set(productId);
        this.name.set(name);
        this.category.set(category);
        this.description.set(description);
        this.price.set(price);
        this.stockQuantity.set(stockQuantity);
        this.image.set(image);
    }
    
    public Product(String name, String category, double price, String description,  int stockQuantity, String image) {
        this.name.set(name);
        this.category.set(category);
        this.description.set(description);
        this.price.set(price);
        this.stockQuantity.set(stockQuantity);
        this.image.set(image);
    }

    public int getProductId() { return productId.get(); }
    public void setProductId(int id) { productId.set(id); }
    public IntegerProperty productIdProperty() { return productId; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }
    
    public String getCategory() { return category.get(); }
    public void setCategory(String value) { category.set(value); }
    public StringProperty categoryProperty() { return category; }

    public String getDescription() { return description.get(); }
    public void setDescription(String value) { description.set(value); }
    public StringProperty descriptionProperty() { return description; }

    public double getPrice() { return price.get(); }
    public void setPrice(double value) { price.set(value); }
    public DoubleProperty priceProperty() { return price; }

    public int getStockQuantity() { return stockQuantity.get(); }
    public void setStockQuantity(int value) { stockQuantity.set(value); }
    public IntegerProperty stockQuantityProperty() { return stockQuantity; }
    
    public void reduceStockQuantity(int value) 
        {   int currentStock = getStockQuantity();
            stockQuantity.set(currentStock -= value);}

    public String getImage() { return image.get(); }
    public void setImage(String value) { image.set(value); }
    public StringProperty imageProperty() { return image; }
    
    public boolean isAvailable() 
    {if (stockQuantity.get() > 0)
        {
            return true;
        }
        else {return false;}  
    }
}


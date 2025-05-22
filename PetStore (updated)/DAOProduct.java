import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DAOProduct {
   

    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO petstore.products (product_id, name, category, price, description, stock_quantity, image) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            
            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getDescription());
            stmt.setInt(6, product.getStockQuantity());
            stmt.setString(7, product.getImage());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProducts(int productId) {
        String sql = "DELETE FROM petstore.products WHERE product_id = ?";

        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE petstore.products SET name = ?, category = ?, price = ?, description = ?, stock_quantity = ?, image = ? WHERE product_id = ?";
    
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
    
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getDescription());
            stmt.setInt(5, product.getStockQuantity());
            stmt.setString(6, product.getImage());
            stmt.setInt(7, product.getProductId());
            return stmt.executeUpdate() > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateProductQuantity(int quantity, int productId) {
        String sql = "UPDATE petstore.products SET stock_quantity = ? WHERE product_id = ?";
    
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
    
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Product getProductById(int productId) {
    String query = "SELECT * FROM products WHERE product_id = ?";
    
    try (Connection conn = ConnectDatabase.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Product product = new Product(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getDouble("price"),
            rs.getString("description"),
            rs.getInt("stock_quantity"),
            rs.getString("image")
            );
            return product;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return null; // Product not found
}

      

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM petstore.products";
        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getInt("stock_quantity"),
                    rs.getString("image")
                );
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            products.add(new Product(1, "Sample Product", "Sample Category", 50, "Sample Description", 10, "cat.jpeg"));
        }
        return products;
    }
}

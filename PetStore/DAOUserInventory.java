import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DAOUserInventory {

    // Create a new inventory record
    public static void addInventory(UserInventory inventory, Product product) {
    // Step 1: Check if the product already exists in the user's inventory
    String checkQuery = "SELECT quantity FROM user_inventory WHERE user_id = ? AND product_id = ?";
    
    try (Connection c = ConnectDatabase.getConnection();
         PreparedStatement checkStmt = c.prepareStatement(checkQuery)) {
        
        checkStmt.setInt(1, inventory.getUserId());
        checkStmt.setInt(2, inventory.getProductId());
        ResultSet rs = checkStmt.executeQuery();
        
        if (rs.next()) {
            // Product already exists, update the quantity
            int currentQuantity = rs.getInt("quantity");
            int newQuantity = currentQuantity + inventory.getQuantity();  // Add new quantity
            
            String updateQuery = "UPDATE user_inventory SET quantity = ?, price = ? WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement updateStmt = c.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, newQuantity);
                updateStmt.setDouble(2, product.getPrice() * newQuantity);
                updateStmt.setInt(3, inventory.getUserId());
                updateStmt.setInt(4, inventory.getProductId()); 
                updateStmt.executeUpdate();
            }
        } else {
            // Product does not exist, insert new record
            String insertQuery = "INSERT INTO user_inventory (user_id, product_id, product_name, quantity, price, image) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = c.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, inventory.getUserId());
                insertStmt.setInt(2, inventory.getProductId());
                insertStmt.setString(3, inventory.getProductName());
                insertStmt.setInt(4, inventory.getQuantity());      
                insertStmt.setDouble(5, inventory.getPrice());
                insertStmt.setString(6, inventory.getImage());
                insertStmt.executeUpdate();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Read all inventory for a user
    public static List<UserInventory> getInventoryByUserId(int userId) {
        List<UserInventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM user_inventory WHERE user_id = ?";

        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserInventory inventory = new UserInventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getString("image")
                );
                inventoryList.add(inventory);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryList;
    }

    // Delete an inventory record
    public static boolean removeInventoryItem(int userId, int productId) {
        String sql = "DELETE FROM user_inventory WHERE user_id = ? and product_id = ?";

        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

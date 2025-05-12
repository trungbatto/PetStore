import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOUser 
{

    // Method to get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users"; // Replace with your actual table name
        
        try (Connection c = ConnectDatabase.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),  // assuming 'user_id' is the primary key
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Method to get a single user by userId
    public static User getUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM petstore.users WHERE user_id = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Method to add a new user
    public void addUser(User user) {
        String query = "INSERT INTO users (username, password, full_name, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing user
    public void updateUser(User user) {
        String query = "UPDATE users SET username = ?, password = ?, full_name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getAddress());
            stmt.setInt(7, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a user by userId
    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static User authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) 
        {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                int id = rs.getInt("user_id");
                String uname = rs.getString("username");
                String pwd = rs.getString("password");
                User user = getUserById(id);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}



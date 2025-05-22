import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOManager 
{

    public List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();
        String query = "SELECT * FROM managers"; 
        
        try (Connection c = ConnectDatabase.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Manager manager = new Manager(
                    rs.getInt("manager_id"), 
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                managers.add(manager);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return managers;
    }

    public static Manager getManagerById(int managerId) {
        Manager manager = null;
        String query = "SELECT * FROM petstore.managers WHERE manager_id = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    manager = new Manager(
                        rs.getInt("manager_id"),
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
        return manager;
    }

    
    public void addManager(Manager manager) {
        String query = "INSERT INTO managers (username, password, full_name, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setString(1, manager.getUsername());
            stmt.setString(2, manager.getPassword());
            stmt.setString(3, manager.getFullName());
            stmt.setString(4, manager.getEmail());
            stmt.setString(5, manager.getPhone());
            stmt.setString(6, manager.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateManager(Manager manager) {
        String query = "UPDATE managers SET username = ?, password = ?, full_name = ?, email = ?, phone = ?, address = ? WHERE manager_id = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setString(1, manager.getUsername());
            stmt.setString(2, manager.getPassword());
            stmt.setString(3, manager.getFullName());
            stmt.setString(4, manager.getEmail());
            stmt.setString(5, manager.getPhone());
            stmt.setString(6, manager.getAddress());
            stmt.setInt(7, manager.getManagerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteManager(int managerId) {
        String query = "DELETE FROM managers WHERE manager_id = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setInt(1, managerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Manager authenticateManager(String username, String password) {
        String query = "SELECT * FROM managers WHERE username = ? AND password = ?";
        
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(query)) 
        {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                int id = rs.getInt("manager_id");
                String uname = rs.getString("username");
                String pwd = rs.getString("password");
                Manager manager = getManagerById(id);
                return manager;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}



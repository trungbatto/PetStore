import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DAOPet {
   

    public boolean insertPet(Pet pet) {
        String sql = "INSERT INTO petstore.pets (name, species, breed, age, gender, description, price, image, vaccinated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            
            stmt.setString(1, pet.getName());
            stmt.setString(2, pet.getSpecies());
            stmt.setString(3, pet.getBreed());
            stmt.setInt(4, pet.getAge());
            stmt.setString(5, pet.getGender());
            stmt.setString(6, pet.getDescription());
            stmt.setDouble(7, pet.getPrice());
            stmt.setString(8, pet.getImage());
            stmt.setBoolean(9, pet.isVaccinated());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePet(int petId) {
        String sql = "DELETE FROM petstore.pets WHERE pet_id = ?";

        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            
            stmt.setInt(1, petId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePet(Pet pet) {
        String sql = "UPDATE petstore.pets SET name = ?, species = ?, breed = ?, age = ?, gender = ?, description = ?, price = ?, available = ?, owner_id = ?, image = ?, vaccinated = ? WHERE pet_id = ?";
    
        try (Connection c = ConnectDatabase.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
    
            stmt.setString(1, pet.getName());
            stmt.setString(2, pet.getSpecies());
            stmt.setString(3, pet.getBreed());
            stmt.setInt(4, pet.getAge());
            stmt.setString(5, pet.getGender());
            stmt.setString(6, pet.getDescription());
            stmt.setDouble(7, pet.getPrice());
            stmt.setBoolean(8, pet.isAvailable());
            stmt.setInt(9, pet.getOwnerId());
            stmt.setString(10, pet.getImage());
            stmt.setBoolean(11, pet.isVaccinated());
            stmt.setInt(12, pet.getPetId());
    
            return stmt.executeUpdate() > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public Pet getPetById(int id) {
        String sql = "SELECT * FROM petstore.pets WHERE pet_id = ?";
        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pet(
                    rs.getInt("pet_id"),
                    rs.getString("name"),
                    rs.getString("species"),
                    rs.getString("breed"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBoolean("available"),
                    rs.getInt("owner_id"),
                    rs.getString("image"),
                    rs.getBoolean("vaccinated")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets";
        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            ResultSet rs = stmt.executeQuery("SELECT * FROM pets");
            while (rs.next()) {
                Pet pet = new Pet(
                    rs.getInt("pet_id"),
                    rs.getString("name"),
                    rs.getString("species"),
                    rs.getString("breed"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBoolean("available"),
                    rs.getInt("owner_id"),
                    rs.getString("image"),
                    rs.getBoolean("vaccinated")
                );
                pets.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            pets.add(new Pet(1, "Sample Pet", "Dog", "Mixed", 5, "Male", "Friendly pet", 100.0, true, 1, "cat.jpg", true));
        }
        return pets;
    }
}

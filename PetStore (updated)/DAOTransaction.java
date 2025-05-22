
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOTransaction
{
    public static boolean insertTransaction(Transaction transaction) {
        String sql = "INSERT INTO petstore.transactions (user_id, transaction_type, price, amount) VALUES (?, ?, ?, ?)";

        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            
            stmt.setInt(1, transaction.getUserId());
            stmt.setString(2, transaction.getTransactionType());
            stmt.setDouble(3, transaction.getPrice());
            stmt.setInt(4,transaction.getAmount());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTransaction(int transactionId) {
        String sql = "DELETE FROM petstore.transactions WHERE transaction_id = ?";

        try (Connection c = ConnectDatabase.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);)
        {
            
            stmt.setInt(1, transactionId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   

    public List<Transaction> getTransactionsByUserId(int userId) {
    List<Transaction> transactions = new ArrayList<>();
    String sql = "SELECT * FROM petstore.transactions WHERE user_id = ?";

    try (Connection c = ConnectDatabase.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {

        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Transaction tx = new Transaction(rs.getInt("transaction_id"),
            rs.getInt("user_id"),
            rs.getString("transaction_type"),
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getDouble("price"),
            rs.getInt("amount"));
            transactions.add(tx);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return transactions;
    }
    
    public List<Transaction> getAllTransactions() {
    List<Transaction> transactions = new ArrayList<>();
    String sql = "SELECT * FROM petstore.transactions";

    try (Connection c = ConnectDatabase.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Transaction tx = new Transaction(rs.getInt("transaction_id"),
            rs.getInt("user_id"),
            rs.getString("transaction_type"),
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getDouble("price"),
            rs.getInt("amount"));
            transactions.add(tx);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return transactions;
    }

}

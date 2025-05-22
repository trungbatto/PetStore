import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
    public static Connection getConnection()
    {
        Connection c = null;
        
        try {
            
            String url = "jdbc:mysql://localhost:3306/petstore";
            String username = "root";
            String password = "Trung151103.";
            Connection conn = DriverManager.getConnection(url, username, password);

            c = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return c;
    }

    public static void closeConnection(Connection c)
    {
        try {
            if(c!= null)
            {
                c.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}   


import java.sql.*;

public class ConnectionClass {
    private Connection conn;
    private Statement st;
    private ResultSet rSet;
    private String host;
    private String dbname;
    private String port;
    private String password;
    private String username;

    public ConnectionClass(String host, String dbname, String port,String username, String password)
            throws ClassNotFoundException {
        this.host = host;
        this.dbname = dbname;
        this.port = port;
        this.password = password;
        this.username = username;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb:// " + this.host + ":" + this.port + "/" + this.dbname;
            conn = DriverManager.getConnection(url, this.username, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void executeUpdate(String query) throws SQLException {

        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
    public ResultSet executeQuery(String query) throws SQLException {
        try{
            st = conn.createStatement();
            rSet = st.executeQuery(query);
            return rSet;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void close() throws SQLException {
        conn.close();
    }
}

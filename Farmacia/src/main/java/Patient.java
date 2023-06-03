import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Patient extends Person {
    public Patient() {
        super();
    }

    public Patient(String nombre, String mail) {
        super(nombre, mail);
    }

    @Override
    public void load(String id) {
        ConnectionClass conn  = null;
        try {
            conn = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            ResultSet rSet  = conn.executeQuery("SELECT * FROM PATIENT  WHERE mail = '" + id +"';");
            if (rSet.next()){
                System.out.println("He entrado en el load Patient id : " + rSet.getString("mail"));
                this.setName(rSet.getString("name"));
                this.setMail(rSet.getString("mail"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("He salido del load Patient");
    }
    public static ArrayList<String> generatePatientList(){
        ArrayList<String> list = new ArrayList<>();
        try {
            ConnectionClass conn  = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            ResultSet rSet  = conn.executeQuery("SELECT mail FROM PATIENT" );
            while (rSet.next()){
                list.add(rSet.getString("mail"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Medicine {
    private int id ;
    private String name;
    private float Tmax;
    private float Tmin;
    public Medicine() {

    }

    public Medicine(int id, String name, float tmax, float tmin) {
        this.id = id;
        this.name = name;
        Tmax = tmax;
        Tmin = tmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTmax() {
        return Tmax;
    }

    public void setTmax(float tmax) {
        Tmax = tmax;
    }

    public float getTmin() {
        return Tmin;
    }

    public void setTmin(float tmin) {
        Tmin = tmin;
    }

    public void load (int id){
        try {
            ConnectionClass conn  = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            ResultSet rSet  = conn.executeQuery("SELECT * FROM MEDICINE  WHERE id = " + id +";");
            if (rSet.next()) {
                System.out.println("He entrado en load Medicine id : "+ rSet.getInt("id"));
                this.id = rSet.getInt("id");
                this.name = rSet.getString("name");
                this.Tmax = rSet.getFloat("t_max");
                this.Tmin = rSet.getFloat("t_min");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("He salido de load Medicine");
    }
    public static ArrayList<Medicine> generateMedicineList(){
        ArrayList<Medicine> list = new ArrayList<>();
        try {
            ConnectionClass conn  = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            ResultSet rSet  = conn.executeQuery("SELECT * FROM MEDICINE" );
            while (rSet.next()){
                Medicine medicine = new Medicine();
                medicine.load(rSet.getInt("id"));
                list.add(medicine);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}

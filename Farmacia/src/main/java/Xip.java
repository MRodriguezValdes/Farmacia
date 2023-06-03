import java.sql.*;
import java.util.Date;

public class Xip {
    private int id;
    private Medicine medicine;
    private Patient patient;

    private Date finalDate;

    public Xip() {

    }

    public Xip(int id, Medicine medicine, Patient patient, Date finalDate) {
        this.setId(id);
        this.setMedicine(medicine);
        this.setPatient(patient);
        this.setFinalDate(finalDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }
    public void load (int id){
        try {
            ConnectionClass conn  = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            ResultSet rSet = conn.executeQuery("SELECT * FROM CHIP WHERE id = " + id +";");
            this.medicine = new Medicine();
            this.patient = new Patient();
            if(rSet.next()){
                System.out.println("He entrado al load xip");
                this.id =  rSet.getInt("id");
                this.finalDate = rSet.getDate("c_date");
                this.patient.load(rSet.getString("id_patient"));
                this.medicine.load(rSet.getInt("id_medicine"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("He salido load xip");
    }


}

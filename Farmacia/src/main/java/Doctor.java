import java.sql.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Doctor extends Person {
    private ConnectionClass conn;
    private String password;
    private LocalDate lastLog;

    private int  session;

    private ArrayList<Xip> lastRelease;

    public Doctor() {
        super();
        this.lastRelease = new ArrayList<>();
        try {
            conn = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Doctor(String nombre, String mail, String password, LocalDate lastLog, int session) {
        super(nombre, mail);
        this.setPassword(password);
        this.setLastLog(lastLog);
        this.setSession(session);
        this.lastRelease = new ArrayList<>();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getLastLog() {
        return lastLog;
    }

    public void setLastLog(LocalDate lastLog) {
        this.lastLog = lastLog;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int  session) {
        this.session = session;
    }

    public ArrayList<Xip> getLastRelease() {
        return lastRelease;
    }

    public void setLastRelease(Xip xipRelease) {
        this.lastRelease.add(xipRelease);
    }

    public void login(String username,String password) {

        try {


            ResultSet result = conn.executeQuery("SELECT COUNT(*) FROM DOCTOR WHERE mail='" + username + "' and pass='" + password + "'");

            if (result.next()) {
                System.out.println("count"+result.getInt("COUNT(*)"));
                if (result.getInt(1) == 1) {

                    //Codigo de session
                    this.setSession(generateCodeSession());

                    // Actalizo lastLog y session
                    conn.executeUpdate("UPDATE DOCTOR SET last_log= SYSDATE(),session = " + this.getSession() + " WHERE mail='" + username + "'");

                    //Cargamos la info del doctor
                    load(username);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int generateCodeSession() {
        Random random = new Random();
        StringBuffer result = new StringBuffer("");
        int index = 0;
        while (index < 9) {
            result.append(random.nextInt(9));
            index++;
        }

        return Integer.parseInt(result.toString());
    }

    public void loadReleaseList(){
        try {
            ResultSet result = conn.executeQuery("SELECT id FROM CHIP WHERE doctor_mail= '" + this.getMail()+ "'");

            while (result.next()) {
                System.out.println("He entrado al load  realease ");
                Xip xip = new Xip();
                xip.load(result.getInt("id"));
                this.setLastRelease(xip);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("He salido del load realease");
    }

    @Override
    public void load(String id) {
        try {
            ResultSet result = conn.executeQuery("SELECT * FROM DOCTOR WHERE mail='" + id + "'");
            if (result.next()) {
                this.password = result.getString("pass");
                this.lastLog = result.getDate("last_log").toLocalDate();
                this.setMail(result.getString("mail"));
                this.setName(result.getString("name"));
                this.session = result.getInt("session");
            }
            loadReleaseList();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isLogged(String  mail , String session){
        this.load(mail);
        return (this.getSession() == Integer.parseInt(session) && LocalDate.now().isEqual(this.getLastLog()));
    }

    public String getTable(){
        System.out.println("He entrado en el get table");
        StringBuffer table = new StringBuffer ("");
        table.append("<thead>"+
          "<th>DoctorName</th>"+
          "<th>DoctorMail</th>"+
          "<th>ChipID</th>"+
          "<th>MedicineName</th>"+
          "<th>PatientName</th>"+
          "<th>PatientMail</th>"+
          "<th>ChipFinalDate</th>"+
          "</thead>");
        for (int i = 0; i <this.getLastRelease().size() ; i++) {
            table.append("<tr>" +
                    "<td>"+this.getName()+"</td>"+
                    "<td>"+this.getMail()+"</td>"+
                    "<td>"+this.getLastRelease().get(i).getId()+"</td>"+
                    "<td>"+this.getLastRelease().get(i).getMedicine().getName()+"</td>"+
                    "<td>"+this.getLastRelease().get(i).getPatient().getName()+"</td>"+
                    "<td>"+this.getLastRelease().get(i).getPatient().getMail()+"</td>"+
                    "<td>"+this.getLastRelease().get(i).getFinalDate()+"</td>" +
                    "</tr>");
        }

        System.out.println("Devuelvo algo................................................................");

        return " "+ table;
    }
}

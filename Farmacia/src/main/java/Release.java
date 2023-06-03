import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "ServeRelease", urlPatterns = {"/release"})
public class Release extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("hola soy el get de release");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lee el JSON del cuerpo de la solicitud
        BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

        // Accede a los valores del JSON
        String mail = jsonObject.get("mail").getAsString();
        String session = jsonObject.get("session").getAsString();
        int idXip = Integer.parseInt(jsonObject.get("idXip").getAsString());
        int idMedicine = Integer.parseInt(jsonObject.get("idMedicine").getAsString());
        String patientMail = jsonObject.get("patientMail").getAsString();
        String finalDate = jsonObject.get("finalDate").getAsString();

        try {
            ConnectionClass conn = conn = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            ResultSet rSet = conn.executeQuery("SELECT COUNT(*) FROM CHIP WHERE id = " + idXip);
            if (rSet.next()) {

                if (rSet.getInt(1) == 0) {
                    conn.executeUpdate("INSERT INTO CHIP (id,doctor_mail,id_medicine,id_patient,c_date) VALUES(" + idXip + ",'" + mail + "'," + idMedicine + ",'" + patientMail + "',str_to_date('" + finalDate + "','%Y-%m-%d'))");
                    resp.getWriter().append("ok");

                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        // Permitir solicitudes desde cualquier origen
        resp.setHeader("Access-Control-Allow-Origin", "*");

        // Otros encabezados CORS opcionales
        resp.setHeader("Access-Control-Allow-Methods", "GET");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}

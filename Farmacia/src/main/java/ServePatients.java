import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "ServePatient", urlPatterns = {"/patient"})
public class ServePatients extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mail = req.getParameter("email");
        String session = req.getParameter("session");
        Doctor doctor = new Doctor();
        ArrayList<String> patients;
        Gson gson = new GsonBuilder().create();
        String json = "";
        if (mail != null && session != null) {
            if (doctor.isLogged(mail, session)) {
                patients = Patient.generatePatientList();
                json = gson.toJson(patients);
            }
        }


        // Permitir solicitudes desde cualquier origen
        resp.setHeader("Access-Control-Allow-Origin", "*");

        // Otros encabezados CORS opcionales
        resp.setHeader("Access-Control-Allow-Methods", "GET");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        resp.getWriter().append(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        System.out.println(name + " " + email);

        ConnectionClass conn = null;
        try {

            conn = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            ResultSet rSet = conn.executeQuery("SELECT COUNT(*) FROM PATIENT WHERE mail = '" + email + "' AND name='"+name+"'");
            if (rSet.next()) {
                if (rSet.getInt(1)==0){
                    conn.executeUpdate("INSERT INTO PATIENT (mail,name)VALUES ('" + email + "','" + name + "')");
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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.KeyStore;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("email");
        String password = req.getParameter("password");
        Doctor doctor = new Doctor();

        if(username != null && password != null){

            doctor.login(username, password);

            System.out.println("session: "+doctor.getSession());

        }

        // Permitir solicitudes desde cualquier origen
        resp.setHeader("Access-Control-Allow-Origin", "*");

        // Otros encabezados CORS opcionales
        resp.setHeader("Access-Control-Allow-Methods", "GET");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        //Respuesta del servidor
        resp.getWriter().append(""+doctor.getSession());
        System.out.println("fin login");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        System.out.println(username + " " + password);
        try {
            ConnectionClass conn = new ConnectionClass("localhost", "FARMACIA", "3306", "root", "1234");
            conn.executeUpdate("INSERT INTO DOCTOR (mail,pass,name)VALUES ('" + username + "','" + password + "','"+name+"')");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }


        resp.setHeader("Access-Control-Allow-Origin", "*");

        // Otros encabezados CORS opcionales
        resp.setHeader("Access-Control-Allow-Methods", "GET");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}

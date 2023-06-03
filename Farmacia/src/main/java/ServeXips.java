import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet (name = "ServeXipsServlet", urlPatterns = {"/xips"})
public class ServeXips extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mail = req.getParameter("email");
        String session = req.getParameter("session");
        Doctor doctor = new Doctor();
        String table="" ;
        if (mail != null && session != null) {
             if(doctor.isLogged(mail,session)){
                table = doctor.getTable();
                System.out.println(table);
            }
        }

        // Permitir solicitudes desde cualquier origen
        resp.setHeader("Access-Control-Allow-Origin", "*");

        // Otros encabezados CORS opcionales
        resp.setHeader("Access-Control-Allow-Methods", "GET");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        //Enviar respuesta
        resp.getWriter().append(table);
    }
}

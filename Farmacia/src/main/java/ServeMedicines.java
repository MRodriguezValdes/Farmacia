import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name="ServeMedicine",urlPatterns = {"/medicine"})
public class ServeMedicines extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mail = req.getParameter("email");
        String session = req.getParameter("session");
        Doctor doctor = new Doctor();
        ArrayList<Medicine> medicines;
        Gson gson = new GsonBuilder().create();
        String json = "";

        if (mail != null && session != null) {
            if(doctor.isLogged(mail,session)){
                medicines = Medicine.generateMedicineList();
                json = gson.toJson(medicines);
            }
        }

        // Permitir solicitudes desde cualquier origen
        resp.setHeader("Access-Control-Allow-Origin", "*");

        // Otros encabezados CORS opcionales
        resp.setHeader("Access-Control-Allow-Methods", "GET");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");


        resp.getWriter().append(json);
    }
}

 # Proyecto Final
 # Titulo : Farmacia
 # Descripcion:

 Con el Proyecto el objetivo perseguido es simular el comportamiento de una web que permita :

 1. Loguearse a un doctor

 2. Registrarse a un doctor

 3. El doctor podrá dar de alta un xip o un paciente

 4. Debemos tener una tabla en tiempo real donde se muestren los pacientes de el doctor que se encuentra logueado , así como el medicamento recetado,etc

 5. En el alta de xip debemos recibir como json del backend un json con todos los pacientes y medicamentos  disponibles en la base de datos .
 De forma resumida los aspectos a cumplir son los planteados anteriormente . Es importante recordar que el backend también lo crearemos nosotros, es un proyecto Full Stack.

 # Solución de los requerimientos :

 ## El proyecto está dividido en dos partes Frontend y Backend :

 ## Backend:
 ### Requerimiento:
 ```text
 Class Person abstract
    -Atributs:
- String name: Nom complet de la persona
- String mail.
    -Constructors:
   	 -Buit
   	 -Tots els paràmetres
    -Mètodes:
   	 -load(String id). Abstract. No té retorn

 ```
 ### Solucion :
 ```java
 public abstract class Person {
	private String name;
	private String mail;

	public Person() {
	}

	public Person(String nombre, String mail) {
    	this.setName(nombre);
    	this.setMail(mail);
	}

	public String getName() {
    	return name;
	}

	public void setName(String name) {
    	this.name = name;
	}

	public String getMail() {
    	return mail;
	}

	public void setMail(String mail) {
    	this.mail = mail;
	}

	public abstract void load (String id );
}

 ```

 ### Requerimiento:
 ```text
 Class Doctor hereda de Person
    -Atributs:
-String pass: Codi de hash de la contrasenya del doctor.
-LocalDate lastLog: Data del darrer login de l'usuari.
 -String session: Codi de 10 dígits aleatoris, que identifiquen la sessió del doctor, ha de ser únic i en cas de no realitzar un login amb èxit ha de ser null.
-ArrayList releaseList: Array de Xips vinculats al doctor.
    -Constructors:
   	 -Buit
   	 -Tots els paràmetres (excepte releaseList).
    -Mètodes:
    -login(String mail, String pass): No té retorn. Si el mail i el pass son correctes, carrega a l’objecte els atributs de la BBDD a través del load(), s’ha de fixar els atributs lastLog i el session a la BBDD.
-isLogged(String mail, String session): Retorna un boolean true si troba el mail amb la session en data; carrega les dades amb login(). En cas contrari retorna false.
-load(String id):  No té retorn. Carrega a l’objecte les dades del Doctor que corresponen a id=(BBDD: farmacia.doctor.mail).
-loadReleaseList(): Carrega a l’array del Doctor tots els xips (que estan en data) de la BBDD vinculats a ell.
-getTable(): Retorna un string que correspon a una taula HTML de tots els xips d’alta, vigents, del doctor.

 ```

 ### Solucion :

 ```java

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

                	// Actualizar lastLog y session
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
            	System.out.println("He entrado al load  release ");
            	Xip xip = new Xip();
            	xip.load(result.getInt("id"));
            	this.setLastRelease(xip);
        	}
    	} catch (SQLException e) {
        	throw new RuntimeException(e);
    	}
    	System.out.println("He salido del load release");
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

 ```
 ### Básicamente el backend gira entorno a la clase Doctor, por tanto solo mostraré a continuación los métodos load tanto de Xip, Patient , Medicine

 ### Load Xip:
 ```java
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

 ```
 ### Load Patient:
 ```java
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

 ```
 ### Load Medicine:
 ```java
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
 ```

 ### Recibir pacientes y medicamentos en formato json en el frontend:

 ### Recibir pacientes
 ```java
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
 ```
 ### Recibir medicamentos

 ```java

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

 ```

 ### Servlets :

 ### ServePatients:

 ```java
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
 ```
 ### ServeMedicines:
 ```java

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
 ```
 ```text
 Básicamente esa es la estructura interna que permite el funcionamiento del backend. Debemos recordar que lo que permite que se  acceda a dichas funciones es la llamada que se realiza a estas desde los servlets correspondientes, que por motivos de limpieza y simplicidad no  muestro en el Readme.
 ```
 ## Frontend
 ### Recibir tabla en tiempo real :
 ```java
 function requestForTable() {
  let rhttp = new XMLHttpRequest();
  let mail = sessionStorage.getItem("mail");
  let session = sessionStorage.getItem("session");
  console.log(mail);
  console.log(session);
  rhttp.onreadystatechange = function () {
	if (this.readyState == 4 && this.status == 200) {
  	let data = rhttp.responseText;
  	console.log(data);
  	document.getElementById("tabla").innerHTML = data;
	}
  };

  let url =
	"email=" +
	encodeURIComponent(mail) +
	"&session=" +
	encodeURIComponent(session);
  console.log(url);

  rhttp.open(
	"GET",
	"http://localhost:3001/Farmacia_war_exploded/xips?" + url,
	true
  );
  rhttp.send();
}
 ```
 ### Alta del xip:

 ```java
 function altaXip() {
  let ehttp = new XMLHttpRequest();
  let mail = sessionStorage.getItem("mail");
  let session = sessionStorage.getItem("session");
  let idXip = document.getElementById("idXip").value;
  let idMedicine = document.getElementById("idMedicine").value;
  let patientMail = document.getElementById("patientMail").value;
  let finalDate = document.getElementById("finalDate").value;

  let data = {
	mail: mail,
	session: session,
	idXip: idXip,
	idMedicine: idMedicine,
	patientMail: patientMail,
	finalDate: finalDate,
  };

  data = JSON.stringify(data);

  console.log(data);

  ehttp.onreadystatechange = function () {
	if (this.readyState == 4 && this.status == 200) {
  	let data = ehttp.responseText;
  	if (data == "ok") {
    	document.getElementById("idXip").value = "";
    	document.getElementById("idMedicine").value = "";function requestForListPatients() {
  let rhttp = new XMLHttpRequest();
  let mail = sessionStorage.getItem("mail");
  let session = sessionStorage.getItem("session");
  console.log(mail);
  console.log(session);
  rhttp.onreadystatechange = function () {
	if (this.readyState == 4 && this.status == 200) {
  	let data = JSON.parse(rhttp.responseText);
  	let selectElement = document.getElementById("patientMail");
  	selectElement.innerHTML = ""; // empty select
  	console.log(data[3]);
  	for (let i = 0; i < data.length; i++) {
    	let option = document.createElement("option");
    	option.value = data[i];
    	option.textContent = data[i];
    	selectElement.appendChild(option);
  	}
  	console.log(data);
	}
  };

  let url =
	"email=" +
	encodeURIComponent(mail) +
	"&session=" +
	encodeURIComponent(session);
  console.log(url);

  rhttp.open(
	"GET",
	"http://localhost:3001/Farmacia_war_exploded/patient?" + url,
	true
  );
  rhttp.send();
}
    	document.getElementById("patientMail").value = "";
    	finalDate = document.getElementById("finalDate").value = "";
    	alert("Ha sido dado de alta");
  	} else {
    	alert("Se ha producido un error");
  	}
	}
  };
  ehttp.open("POST", "http://localhost:3001/Farmacia_war_exploded/release");
  ehttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  ehttp.send(data);
}

 ```
 En esta función empleo un json para enviar la información al backend

 ### Recibir JSON de medicinas y pacientes :

 ### JSON medicinas :

 ```java
 function requestForListMedicine() {
  let rhttp = new XMLHttpRequest();
  let mail = sessionStorage.getItem("mail");
  let session = sessionStorage.getItem("session");
  console.log(mail);
  console.log(session);
  rhttp.onreadystatechange = function () {
	if (this.readyState == 4 && this.status == 200) {
  	let data = JSON.parse(rhttp.responseText);
  	let selectElement = document.getElementById("idMedicine");
  	selectElement.innerHTML = ""; // empty select
  	console.log(data[3].name);
  	for (let i = 0; i < data.length; i++) {
    	let option = document.createElement("option");
    	option.value = data[i].id;
    	option.textContent = data[i].name;
    	selectElement.appendChild(option);
  	}

  	console.log(data);
	}
  };

  let url =
	"email=" +
	encodeURIComponent(mail) +
	"&session=" +
	encodeURIComponent(session);
  console.log(url);

  rhttp.open(
	"GET",
	"http://localhost:3001/Farmacia_war_exploded/medicine?" + url,
	true
  );
  rhttp.send();
}
 ```
 ### JSON Pacientes :
 ```java
 function requestForListPatients() {
  let rhttp = new XMLHttpRequest();
  let mail = sessionStorage.getItem("mail");
  let session = sessionStorage.getItem("session");
  console.log(mail);
  console.log(session);
  rhttp.onreadystatechange = function () {
	if (this.readyState == 4 && this.status == 200) {
  	let data = JSON.parse(rhttp.responseText);
  	let selectElement = document.getElementById("patientMail");
  	selectElement.innerHTML = ""; // empty select
  	console.log(data[3]);
  	for (let i = 0; i < data.length; i++) {
    	let option = document.createElement("option");
    	option.value = data[i];
    	option.textContent = data[i];
    	selectElement.appendChild(option);
  	}
  	console.log(data);
	}
  };

  let url =
	"email=" +
	encodeURIComponent(mail) +
	"&session=" +
	encodeURIComponent(session);
  console.log(url);

  rhttp.open(
	"GET",
	"http://localhost:3001/Farmacia_war_exploded/patient?" + url,
	true
  );
  rhttp.send();
}
 ```
 # Pruebas Funcionales :

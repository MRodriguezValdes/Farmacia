function verify_password() {
  let password1 = document.getElementById("password1").value;
  let password2 = document.getElementById("password2").value;

  if (password1 != password2) {
    alert("Las contraseñas no coinciden");
  } else if (password1.length < 6) {
    alert("La contraseña debe tener al menos 6 caracteres");
  } else {
    doPost();
  }
}

function getAtribute() {
  document.getElementById("email").value = sessionStorage.getItem("email");
  sessionStorage.clear();
}

function logout() {
  sessionStorage.clear();
  location.href = "login.html";
}

function doGet() {
  var rhttp = new XMLHttpRequest();

  rhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      let data = this.responseText;
      if (data != "0") {
        console.log(data);
        sessionStorage.setItem("mail", document.getElementById("email").value);
        sessionStorage.setItem("session", data);
        location.href = "gestion.html";
      } else {
        alert("The user or password is incorrect");
        document.getElementById("password").value = "";
      }
    }
  };

  let url =
    "email=" +
    encodeURIComponent(document.getElementById("email").value) +
    "&password=" +
    encodeURIComponent(document.getElementById("password").value);
  rhttp.open(
    "GET",
    "http://localhost:3001/Farmacia_war_exploded/login?" + url,
    true
  );
  rhttp.send();
}

function doPost() {
  let ehttp = new XMLHttpRequest();

  ehttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      location.href = "login.html";
      sessionStorage.setItem("email", document.getElementById("email").value);
    }
  };

  let url =
    "email=" +
    document.getElementById("email").value +
    "&password=" +
    document.getElementById("password1").value +
    "&name=" +
    document.getElementById("name").value;

  ehttp.open("POST", "http://localhost:3001/Farmacia_war_exploded/login");
  ehttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  ehttp.send(url);
}

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

function altaPatient() {
  let ehttp = new XMLHttpRequest();
  let name = document.getElementById("name").value;
  let email = document.getElementById("email").value;
  console.log(name + " " + email);

  ehttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      let data = ehttp.responseText;
      if (data == "ok") {
        document.getElementById("name").value = "";
        document.getElementById("email").value = "";
        alert("Ha sido dado de alta");
      } else {
        alert("Upss ya existe");
      }
    }
  };

  let url =
    "email=" + encodeURIComponent(email) + "&name=" + encodeURIComponent(name);

  ehttp.open("POST", "http://localhost:3001/Farmacia_war_exploded/patient");
  ehttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  ehttp.send(url);
}
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
        document.getElementById("idMedicine").value = "";
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

function requestForData() {
  setInterval(requestForListMedicine,5000);
  requestForListMedicine();
  requestForListPatients();
}

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

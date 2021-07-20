package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    EditText txt_nombreUsu, txt_direccionUsu, txt_emailUsu, txt_passwordUsu,txt_confirmacionUsu; // defiiniendo objetos para capturar datos de la vista
    BaseDatos miBdd;
    RadioButton rbCliente, rbAdmin;
    String tipoUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txt_nombreUsu=(EditText)findViewById(R.id.txt_nombreUsu);
        txt_direccionUsu=(EditText)findViewById(R.id.txt_direccionUsu);
        txt_emailUsu=(EditText)findViewById(R.id.txt_emailUsu);
        txt_passwordUsu=(EditText)findViewById(R.id.txt_passwordUsu);
        txt_confirmacionUsu=(EditText)findViewById(R.id.txt_confirmacionUsu);

        miBdd=new BaseDatos(getApplicationContext());
        tipoUsu="Cliente";
    }
    public void cerrarPantallaRegistro(View vista){

        finish();
    }


    public void registrarUsuario(View vista) {
        // captura los calores ingresados por el usuario en variable Java de tipo String
        String nombre = txt_nombreUsu.getText().toString();
        String direccion = txt_direccionUsu.getText().toString();
        String email = txt_emailUsu.getText().toString();
        String password = txt_passwordUsu.getText().toString();
        String passwordConfirmada = txt_confirmacionUsu.getText().toString();
        String tipo = tipoUsu.toString();

        if (nombre.isEmpty() || direccion.isEmpty() || email.isEmpty() || password.isEmpty() || tipo.isEmpty() ) { //si algun campo esta vacio
            Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                    Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
        } else {

                if (contieneSoloLetras(nombre) == false) {
                    Toast.makeText(getApplicationContext(), "El nombre no debe contener numeros",
                            Toast.LENGTH_LONG).show(); //mostrando error de nombre
                } else {
                            Pattern pattern = Patterns.EMAIL_ADDRESS;
                            if (pattern.matcher(email).matches() == false) { //no cumple el correo
                                Toast.makeText(getApplicationContext(), "Ingrese un Email Valido",
                                        Toast.LENGTH_LONG).show(); //mostrando correo invalido
                            } else {
                                if (password.length() < 8) {
                                    Toast.makeText(getApplicationContext(), "Ingrese una contraseña mínimo de 8 dígitos",
                                            Toast.LENGTH_LONG).show(); //mostrando mensaje de contraseña invalida
                                } else {
                                    if (validarpassword(password) == false) {
                                        Toast.makeText(getApplicationContext(), "la contraseña debe tener numeros y letras",
                                                Toast.LENGTH_LONG).show(); //mostrando mensaje de contraseña invalida
                                    } else {
                                        if (password.equals(passwordConfirmada)) {
                                            password = getMD5(password);
                                            // Cuando la condiccion es verdadera se realiza el proceso e insersion
                                            miBdd.agregarUsuario(nombre, direccion, email, password, tipo);//invocando al metodo agregarusuario del objeto miBdd para insertar datos en SQLite
                                            Toast.makeText(getApplicationContext(), "Usuario almacenado exitosamente", Toast.LENGTH_LONG).show();
                                        } else {
                                            // Cuando la condiccion es falsa se presenta un mensaje de error
                                            Toast.makeText(getApplicationContext(), "La contraseña ingresada no coincide", Toast.LENGTH_LONG).show();

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

    public boolean contieneSoloLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                return false;
            }
        }
        return true;
    }


    public boolean validarpassword(String password) {
        boolean numeros = false;
        boolean letras = false;
        for (int x = 0; x < password.length(); x++) {
            char c = password.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')  || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                letras = true;
            }
            if ((c >= '0' && c <= '9') ) {
                numeros = true;
            }

        }
        if (numeros == true && letras ==true){
            return true;
        }
        return false;
    }

    //encriptar contraseña
    public static String getMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
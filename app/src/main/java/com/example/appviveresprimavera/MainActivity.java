package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 18/07/2021
@fModificación 18/07/2021
@descripción: inicio de sesion.
*/
public class MainActivity extends AppCompatActivity {
    //para inicio de sesion*************
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objeto de tipo editor de sharedpreferences
    String llave = "sesion";
    String llavetipo = "tipoUsu";
    //**********************************
    EditText txtEmailLogin, txtPasswordLogin;
    BaseDatos bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtEmailLogin=(EditText) findViewById(R.id.txtEmailLogin);
        txtPasswordLogin=(EditText) findViewById(R.id.txtPasswordLogin);
        bdd=new BaseDatos(getApplicationContext());
        //inicializar elementos sahred para verificar o guardar sesion ******************
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //validar si la sesion esta guardada o no
        revisarSesion();

    }
    public void abrirRegistro(View vista) {
        Intent ventanaRegistro = new Intent(getApplicationContext(), RegistroActivity.class); // creando un intent para convocar a registroActivity
        startActivity(ventanaRegistro); // iniciando la pantalla de registro
    }


    //funciones para guardar o verificar sesion******************************
    //metodos para verificar sesion
    private void revisarSesion(){
        boolean sesion = this.preferences.getBoolean(llave,false);
        String tipoUsu = this.preferences.getString(llavetipo,"Cliente");
        if (sesion == true && tipoUsu.equals("Administrador")){
            Intent ventanaMenu = new Intent(getApplicationContext(),MenuActivity.class);
            startActivity(ventanaMenu);
        }else if(sesion == true && tipoUsu.equals("Cliente")){
            Intent ventanaProductos = new Intent(getApplicationContext(),ConsultarProductoActivity.class);
            startActivity(ventanaProductos);
        }

    }

    //metodo para guardar sesion
    private void guardarSesion(boolean checked, String tipoUsu){
        editor.putBoolean(llave,checked); //editor que guardara la lave y el valor que tendra
        editor.apply(); //que guarde o aplique el cambio
        editor.putString(llavetipo, tipoUsu); //editor que guardara la lave y el valor que tendra
        editor.apply(); //que guarde o aplique el cambio
    }
    //************************************************************************

    public void iniciarSesion(View vista){
        boolean sesion = false;
        // logica de negocio para capturar los valores ingresados de email y Password y consultarlos
        String email =txtEmailLogin.getText().toString();
        String password =txtPasswordLogin.getText().toString();
        password = getMD5(password);
        //consultando el usuario en la bas de datos
        Cursor usuarioEncontrado = bdd.obtenerUsuarioPorEmailPassword(email,password);
        if(usuarioEncontrado!=null){
            String nombreBdd= usuarioEncontrado.getString(1).toString();
            String tipoBdd= usuarioEncontrado.getString(5).toString();
            Toast.makeText(getApplicationContext(),"Bienvenido "+nombreBdd,Toast.LENGTH_LONG).show();
            finish();
            sesion = true;
            guardarSesion(sesion,tipoBdd);
            if (tipoBdd.equals("Cliente")){
                //entrar a Pantallas Clientes
                Intent ventanaProductos= new Intent(getApplicationContext(), ConsultarProductoActivity.class); // creando un intent para convocar a registroActivity
                startActivity(ventanaProductos);
            }else if (tipoBdd.equals("Administrador")){
                //entrar a Pantallas Adminisrtador
                Intent ventanaMenu= new Intent(getApplicationContext(), MenuActivity.class); // creando un intent para convocar a registroActivity
                startActivity(ventanaMenu);
            }

        }else{
            //para el caso de que el usuarioEncontrado sea nulo se muestra un mensaje informativo
            Toast.makeText(getApplicationContext(),"Email o contraseña incorrecta",Toast.LENGTH_LONG).show();
            txtPasswordLogin.setText("");//limpuenaod el valor del campo de la contarseña
        }

    }
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

package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

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
    //**********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializar elementos sahred para verificar o guardar sesion ******************
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //validar si la sesion esta guardada o no
        if (revisarSesion()== true){
            Intent ventanaMenu = new Intent(getApplicationContext(),MenuActivity.class);
            startActivity(ventanaMenu);
        }
        
        //guardando provicinalmente la sesion hasta que la katy cree el login
        boolean sesion = false;
        sesion = true;
        guardarSesion(sesion);
        //*********************************************************************************

    }
    public void abrirRegistro(View vista) {
        Intent ventanaRegistro = new Intent(getApplicationContext(), GestionProductosActivity.class); // creando un intent para convocar a registroActivity
        startActivity(ventanaRegistro); // iniciando la pantalla de registro
    }

    public void abrirventanaMenu(View vista) {
        Intent ventanaProveedores = new Intent(getApplicationContext(), MenuActivity.class); // creando un intent para convocar a registroActivity
        startActivity(ventanaProveedores); // iniciando la pantalla de proveeedores
    }

    //funciones para guardar o verificar sesion******************************
    //metodos para verificar sesion
    private boolean revisarSesion(){
        boolean sesion = this.preferences.getBoolean(llave,false);
        return sesion;
    }

    //metodo para guardar sesion
    private void guardarSesion(boolean checked){
        editor.putBoolean(llave,checked); //editor que guardara la lave y el valor que tendra
        editor.apply(); //que guarde o aplique el cambio
    }
    //************************************************************************

}

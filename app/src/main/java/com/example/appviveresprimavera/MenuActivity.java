package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 19/06/2021
@fModificación 22/06/2021
@descripción: Base de Datos
*/
public class MenuActivity extends AppCompatActivity {
    //para inicio de sesion
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objetito de tipo editor de sharedpreferences
    String llave = "sesion";
    String llaveIdUsuario = "tipoIdUsu";
    //*****************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //inicializar elementos shared
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //*************************
    }

    //MENU DE OPCIONES
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.gestionUsuarios){
            //proceso de cada boton
            Toast.makeText(this, "Gestión de Usuarios" ,Toast.LENGTH_LONG).show();
            //abriendo la ventana de gestion de curso
            // Intent ventanaCursos=new Intent(getApplicationContext(),RegistroCursosActivity.class);
            //startActivity(ventanaCursos); //solicitamos que habra el menu
            //finish(); //cerrando la activity

        }else if(id == R.id.gestionProductos) {

            Toast.makeText(this, "Gestión de Poductos", Toast.LENGTH_LONG).show();
            Intent ventanagestionProductos=new Intent(getApplicationContext(),GestionProductosActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
            startActivity(ventanagestionProductos); //solicitamos que habra el formulario de login
            finish(); //cerrando la activity

        }else if(id == R.id.gestionVentas) {
            Toast.makeText(this, "Gestión de Ventas ", Toast.LENGTH_LONG).show();
        }else if (id == R.id.datosProveedores){
            Toast.makeText(this, "Datos Proveedores ", Toast.LENGTH_LONG).show();
            Intent ventanaProveedores=new Intent(getApplicationContext(),ProveedoresActivitty.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
            startActivity(ventanaProveedores); //solicitamos que habra el formulario de login
            finish(); //cerrando la activity
        }else if (id == R.id.cerrarSesion){
            //cerrar sesion
            editor.putBoolean(llave,false);
            editor.apply();
            editor.putString(llaveIdUsuario,"");
            editor.apply();
            Toast.makeText(getApplicationContext(), "Cerrar Sesión", Toast.LENGTH_LONG).show();
            //redirijo a la actividad de inicio de sesion y cierro el menu
            Intent ventanaLogin=new Intent(getApplicationContext(),MainActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
            startActivity(ventanaLogin); //solicitamos que habra el formulario de login
            finish(); //cerrando la activity
        }
        return super.onOptionsItemSelected(item);

    }

}
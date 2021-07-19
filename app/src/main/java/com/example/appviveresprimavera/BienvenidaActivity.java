package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/*
@autores:@autores:Sandoval,sanchez,rovayo
@creación/ 18/07/2021
@fModificación 18/07/2021
@descripción: ventana de bienvenida que es visible por 2 seg.
*/
public class BienvenidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        //definir un hilo -> simula un cronometro que ejecuta una accion con base a un tiempo determinado
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //acciones que necesite agregar despues del timpo
                Intent ventanaLogin=new Intent(getApplicationContext(),MainActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
                startActivity(ventanaLogin); //solicitamos que habra el formulario de login
                finish(); //cerrando la activity de bienvenida
            }
        }, 2000); // tiempo activo
    }
}
package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void abrirRegistro(View vista) {
        Intent ventanaRegistro = new Intent(getApplicationContext(), RegistroActivity.class); // creando un intent para convocar a registroActivity
        startActivity(ventanaRegistro); // iniciando la pantalla de registro
    }

}

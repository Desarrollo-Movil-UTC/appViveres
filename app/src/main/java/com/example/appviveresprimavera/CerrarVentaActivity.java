package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 18/07/2021
@fModificación 18/07/2021
@descripción: Cerrar la venta.
*/
public class CerrarVentaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_venta);

    }

    public void Salir(View vista) {
        Intent ventanaLista = new Intent(getApplicationContext(), ConsultarProductoActivity.class);
        startActivity(ventanaLista);
        finish();
    }
}
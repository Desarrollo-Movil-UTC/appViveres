package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class VerDespacharVentasActivity extends AppCompatActivity {
    //definicion de elementos xml
    ListView listVentas;
    ArrayList<String> listaVentas = new ArrayList<>(); //cargar los datos de la BDD
    //cursor para obtener los prodcuto
    Cursor ventasObtenidas;
    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_despachar_ventas);
        //mapeo de elementos
        listVentas = (ListView) findViewById(R.id.listventas);
        //instanciar /construir la base de datos en el objeto mi bdd
        miBdd= new BaseDatos(getApplicationContext());
        consultarVentas(); //invoca al metodo de listar


    }

    public void consultarVentas(){
        listaVentas.clear(); //vaciando el listado
        //Quiero obtener las ventas con estado Solicitado al llamar las ventas mando el estado
        String estado = "Solicitado";
        ventasObtenidas = miBdd.obtenerVenta(estado); //consultando cursos y guardandolos en un cursor
        if(ventasObtenidas != null){ //verificando que realmente haya datos dentro de SQLite

            do{
                String id = ventasObtenidas.getString(0);
                String fecha = ventasObtenidas.getString(1);
                String total = ventasObtenidas.getString(2);
                String estado_venta = ventasObtenidas.getString(3);
                String idUsuario = ventasObtenidas.getString(4);
                //construyendo las filas para presentar datos en el ListView
                listaVentas.add(id+": "+" Fecha: "+fecha+" Total: "+total+" Estado. "+estado_venta);
                //crear un adaptador para presentar los datos del listado (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorProductos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaVentas);
                listVentas.setAdapter(adaptadorProductos); //presentando el adaptador dentro del listview

            }while(ventasObtenidas.moveToNext()); //validando si aun existen prodcutos
        }else{
            Toast.makeText(getApplicationContext(), "No existen ventas Solicitadas", Toast.LENGTH_LONG).show();
        }
    }

    //Boton Salir
    public void salirVentas(View vista) { //metodo para cerrar
        Intent ventanaInicial = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(ventanaInicial);
        finish(); //cerrando la activity
    }

}
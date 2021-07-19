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

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 15/07/2021
@Modificación: 15/07/2021
@descripción: Gestion de productos - Lista de productos
*/

public class GestionProductosActivity extends AppCompatActivity {

    //definicion de elementos xml
    ListView listProductos;
    ArrayList<String> listaProductos = new ArrayList<>(); //cargar los datos de la BDD

    //cursor para obtener los prodcuto
    Cursor productosObtenidos;

    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_productos);

        //mapeo de elementos
        listProductos = (ListView) findViewById(R.id.listProductos);

        //instanciar /construir la base de datos en el objeto mi bdd
        miBdd= new BaseDatos(getApplicationContext());
        consultarProductos(); //invoca al metodo de listar

        //generar acciones cuando se da click sobre un prodcuto de la lista
        listProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productosObtenidos.moveToPosition(position);

                //mediante el cursor se obtiene los datos del curso seleccionado
                String idProd = productosObtenidos.getString(0).toString();
                String nombreProd = productosObtenidos.getString(1).toString();
                String fecha = productosObtenidos.getString(2).toString();
                String cantidad = productosObtenidos.getString(3).toString();
                String precio = productosObtenidos.getString(4).toString();
                String foto = productosObtenidos.getString(5).toString();
                String descripcion = productosObtenidos.getString(6).toString();

                //manejando el objeto para abrir la ventana de Lista de Estudiantes de un curso
                Intent ventanaActualizarProducto = new Intent(getApplicationContext(), ActualizarEliminarProductoActivity.class);
                ventanaActualizarProducto.putExtra("idProd", idProd); //pasando el id del curso como parametro
                ventanaActualizarProducto.putExtra("nombreProd", nombreProd);
                ventanaActualizarProducto.putExtra("fechaProd", fecha);
                ventanaActualizarProducto.putExtra("cantidadProd", cantidad);
                ventanaActualizarProducto.putExtra("precioProd", precio);
                ventanaActualizarProducto.putExtra("fotoProd", foto);
                ventanaActualizarProducto.putExtra("descripcionProd", descripcion);

                startActivity(ventanaActualizarProducto);
                finish();
            }
        });
    }

    //Boton Añadir
    public void agregarProducto(View vista) {
        Intent ventanaRegistroProducto = new Intent(getApplicationContext(), RegistroProductoActivity.class);
        startActivity(ventanaRegistroProducto);
        finish();
    }

    public void consultarProductos(){
        listaProductos.clear(); //vaciando el listado
        productosObtenidos = miBdd.obtenerProductos(); //consultando cursos y guardandolos en un cursor
        if(productosObtenidos != null){ //verificando que realmente haya datos dentro de SQLite

            do{
                String id = productosObtenidos.getString(0).toString();
                String nombreProd = productosObtenidos.getString(1).toString();
                String fecha = productosObtenidos.getString(2).toString();
                String cantidad = productosObtenidos.getString(3).toString();
                String precio = productosObtenidos.getString(4).toString();
                String foto = productosObtenidos.getString(5).toString();
                String descripcion = productosObtenidos.getString(6).toString();


                //construyendo las filas para presentar datos en el ListView
                listaProductos.add(id+": "+nombreProd+" Fecha Caducidad: ("+fecha+") Cantidad: "+cantidad+" P. Unit. "+precio);

                //crear un adaptador para presentar los datos del listado (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorProductos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaProductos);
                listProductos.setAdapter(adaptadorProductos); //presentando el adaptador dentro del listview

            }while(productosObtenidos.moveToNext()); //validando si aun existen prodcutos
        }else{
            Toast.makeText(getApplicationContext(), "No existen productos", Toast.LENGTH_LONG).show();
        }
    }

    //Boton Salir
    public void salirProductos(View vista) { //metodo para cerrar
        Intent ventanaMenu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(ventanaMenu);
        finish(); //cerrando la activity
    }
}
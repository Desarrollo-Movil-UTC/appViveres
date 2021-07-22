package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class ConsultarProductoActivity extends AppCompatActivity implements ItemListenner{
    ArrayList<Producto>listDatos;
    RecyclerView rv_prod;
    Cursor productosObtenidos;
    BaseDatos miBdd;
    Producto producto;
    //*****************************
    //para inicio de sesion
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objetito de tipo editor de sharedpreferences
    String llave = "sesion";
    String llaveIdUsuario = "tipoIdUsu";
    //*****************************
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_producto);
        //inicializar elementos shared
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //*************************
        miBdd= new BaseDatos(getApplicationContext());
        listDatos= new ArrayList<>();
        rv_prod=(RecyclerView)findViewById(R.id.rv_prod);
        rv_prod.setLayoutManager(new LinearLayoutManager(this));
        consultarProductos();
        AdapterDatos adapter=new AdapterDatos(listDatos,this);
        rv_prod.setAdapter(adapter);

    }

    public void consultarProductos(){
        productosObtenidos = miBdd.obtenerProductos(); //consultando cursos y guardandolos en un cursor
        producto=null;
        if(productosObtenidos != null){ //verificando que realmente haya datos dentro de SQLite

            do{
                producto=new Producto();
                producto.setId(productosObtenidos.getInt(0));
                producto.setNombre(productosObtenidos.getString(1));
                producto.setPrecio(productosObtenidos.getString(3));


                //construyendo las filas para presentar datos en el ListView
                listDatos.add(producto);

            }while(productosObtenidos.moveToNext()); //validando si aun existen prodcutos
        }else{
            Toast.makeText(getApplicationContext(), "No existen productos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(int posicion) {
        productosObtenidos.moveToPosition(posicion);

        //mediante el cursor se obtiene los datos del curso seleccionado
        String idProd = productosObtenidos.getString(0).toString();
        String nombreProd = productosObtenidos.getString(1).toString();
        String fecha = productosObtenidos.getString(2).toString();
        String cantidad = productosObtenidos.getString(3).toString();
        String precio = productosObtenidos.getString(4).toString();
        String foto = productosObtenidos.getString(5).toString();
        String descripcion = productosObtenidos.getString(6).toString();

        //manejando el objeto para abrir la ventana de Lista de Estudiantes de un curso
        Intent ventanaActualizarProducto = new Intent(getApplicationContext(), DetalleProductoActivity.class);
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

    public void cerrarSecion(View vista){
        //cerrar sesion
        editor.putBoolean(llave,false);
        editor.apply();
        editor.putString(llaveIdUsuario,"");
        editor.apply();
        Toast.makeText(getApplicationContext(), "Sesión Cerrada", Toast.LENGTH_LONG).show();
        //redirijo a la actividad de inicio de sesion y cierro el menu
        Intent ventanaLogin=new Intent(getApplicationContext(),MainActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
        startActivity(ventanaLogin); //solicitamos que habra el formulario de login
        finish(); //cerrando la activity
    }

    public void abrirCarrito(View vista){
        //redirijo al carrito de compras
        Intent ventanaCarrito=new Intent(getApplicationContext(),CerrarVentaActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
        startActivity(ventanaCarrito); //solicitamos que habra el formulario de login
        finish(); //cerrando la activity
    }


}
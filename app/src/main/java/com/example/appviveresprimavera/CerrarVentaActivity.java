package com.example.appviveresprimavera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 18/07/2021
@fModificación 18/07/2021
@descripción: Cerrar la venta.
*/
public class CerrarVentaActivity extends AppCompatActivity {
    //para consultar el id del usuario que tiene abierta la secion**********************************
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objetito de tipo editor de sharedpreferences
    String llaveIdUsuario = "tipoIdUsu";
    String idUsuario;
    //**********************************************************************************************
    TextView txtTotal;
    BaseDatos miBdd;
    //salida*********************
    ListView lstProdutosCarrito;
    ArrayList<String> listaproductosCarrito = new ArrayList<>(); //cargar los datos de la BDD
    Cursor productosObtenidos;
    Double calcularTotal= 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_venta);
        //inicializar elementos shared para obtener el id del usuario que tiene abierta la secion***
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        idUsuario= this.preferences.getString(llaveIdUsuario,"");
        //******************************************************************************************
        txtTotal=(TextView) findViewById(R.id.txtTotal);
        miBdd=new BaseDatos(getApplicationContext());
        lstProdutosCarrito=(ListView) findViewById(R.id.lstProdutosCarrito);
        consultarProductosCarrito();

        if (productosObtenidos == null){
            txtTotal.setText("0.0");
        }else{
            total();
            txtTotal.setText(calcularTotal.toString());
        }

        //evento cuando de clic a un producto
        //generar acciones cuando se da click sore un elemento de la lista de cursos
        lstProdutosCarrito.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //programacion de acciones cuando se da click en un elemento/item de la lista de clientes
                productosObtenidos.moveToPosition(position); // moviendo el cursor hacia la posicion donde se dio click
                //mediante el cursor se obtiene los datos del cliente seleccionado en la lista mediante las posiciones en el cursor
                String idProducto = productosObtenidos.getString(0);
                eliminarProducto(idProducto);

            }
        });

    }

    public void Salir(View vista) {
        Intent ventanaLista = new Intent(getApplicationContext(), ConsultarProductoActivity.class);
        startActivity(ventanaLista);
        finish();
    }

    public void consultarProductosCarrito(){
        listaproductosCarrito.clear(); //vaciando el listado
        int idUsu = Integer.parseInt(idUsuario);
        productosObtenidos = miBdd.obtenerProductosCarrito(idUsu); //consultando cursos y guardandolos en un cursor
        if(productosObtenidos != null){ //verificando que realmente haya datos dentro de SQLite
            do{
                String id = productosObtenidos.getString(0).toString();
                String producto = productosObtenidos.getString(1).toString();
                String precio = productosObtenidos.getString(2).toString();
                String cantidad= productosObtenidos.getString(3).toString();
                String subtotal= productosObtenidos.getString(4).toString();
                //construyendo las filas para presentar datos en el ListView
                listaproductosCarrito.add(id+": "+producto+" Precio:  "+precio+" Cantidad:  "+cantidad+" subtotal  "+subtotal);
                //crear un adaptador para presentar los datos del listado (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorCursos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaproductosCarrito);
                lstProdutosCarrito.setAdapter(adaptadorCursos); //presentando el adaptador dentro del listview
            }while(productosObtenidos.moveToNext()); //validando si aun existen cursos dentro del cursor
        }else{
            Toast.makeText(getApplicationContext(), "No existen productos en el carrito", Toast.LENGTH_LONG).show();
        }
    }

    public void total(){
        calcularTotal= 0.0;
        for (int i=0; i< productosObtenidos.getCount();i++){
            productosObtenidos.moveToPosition(i);
            Double subtotal= productosObtenidos.getDouble(4);
            calcularTotal=calcularTotal+subtotal;
        }
    }

    //metodo para procesar la eliminacion para presentar una ventana de confirmacion
    public void eliminarProducto(String id){
        //que se habla una ventanita pequeña con las opciones de eliminacion
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("CONFIRMACION")
                .setMessage("¿esta seguro de eliminar el producto seleccionado?")
                .setPositiveButton("si, Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //procesar la eliminarcion
                        miBdd.eliminarProductoCarrito(id); //llamo al metodo eliminar y le paso el id del estudiante
                        Toast.makeText(getApplicationContext(),"Producto Eliminado" ,Toast.LENGTH_SHORT).show();
                        consultarProductosCarrito();
                        total();
                        txtTotal.setText(calcularTotal.toString());
                    }
                }).setNegativeButton("No, Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //si le da a este boton no se hara ninguna accin y solo aparece el toast.
                        Toast.makeText(getApplicationContext(),"Eliminacion Cancelada" ,Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(true); //que se pueda cnacelar la operacion que se esta realziando
        AlertDialog cuadroDialogo = estructuraConfirmacion.create(); //instanciando el cuadro de dialogo con la estructura indicada
        cuadroDialogo.show(); //mostrando en pantalla el cuadro de dialogo
    }


}
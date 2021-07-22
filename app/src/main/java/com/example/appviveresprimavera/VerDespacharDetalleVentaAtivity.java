package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VerDespacharDetalleVentaAtivity extends AppCompatActivity {
    //definicion de variables
    String fecha_venta,total_venta,estado_venta; // variables para capturar valores que vienen cmo parametro
    String id_venta,idUsuario_venta;
    TextView txtNombreCliente,txtDireccionCliente,txtFechaVenta,txtEstadoVenta,txtTotalVenta;
    BaseDatos miBdd;
    //salida*********************
    ListView lstProdutosVenta;
    ArrayList<String> listaproductosVenta = new ArrayList<>(); //cargar los datos de la BDD
    Cursor productosObtenidos;
    Cursor clienteObtenido; //declaracion global para usarla desde culquier metodo
    Cursor productoNombre; //para buscar el producto por nombre y actualizar el stock
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_despachar_detalle_venta_ativity);
        //Mapeo de elementos
        txtNombreCliente=(TextView) findViewById(R.id.txtNombreCliente);
        txtDireccionCliente=(TextView) findViewById(R.id.txtDireccionCliente);
        txtFechaVenta=(TextView) findViewById(R.id.txtFechaVenta);
        txtEstadoVenta=(TextView) findViewById(R.id.txtEstadoVenta);
        txtTotalVenta=(TextView) findViewById(R.id.txtTotalVenta);
        lstProdutosVenta=(ListView) findViewById(R.id.lstProdutosVenta);
        miBdd=new BaseDatos(getApplicationContext());
        //objeto tipo Bundle que captura los parametros que se han pasado a esta actividad
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                id_venta = parametrosExtra.getString("id_venta");
                fecha_venta = parametrosExtra.getString("fecha_venta");
                total_venta = parametrosExtra.getString("total_venta");
                estado_venta = parametrosExtra.getString("estado_venta");
                idUsuario_venta = parametrosExtra.getString("idUsuario_venta");

            }catch (Exception ex){ //ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

        //presentar los datos recibidos de curso en pantalla
        txtFechaVenta.setText(fecha_venta);
        txtEstadoVenta.setText(estado_venta);
        txtTotalVenta.setText(total_venta);
        consultarCliente();
        consultarProductosVenta();

    }

    public void consultarProductosVenta(){
        listaproductosVenta.clear(); //vaciando el listado
        int idVen = Integer.parseInt(id_venta);
        productosObtenidos = miBdd.obtenerProductosVenta(idVen); //consultando cursos y guardandolos en un cursor
        if(productosObtenidos != null){ //verificando que realmente haya datos dentro de SQLite
            do{
                String producto = productosObtenidos.getString(1).toString();
                String precio = productosObtenidos.getString(2).toString();
                String cantidad= productosObtenidos.getString(3).toString();
                String subtotal= productosObtenidos.getString(4).toString();
                //construyendo las filas para presentar datos en el ListView
                listaproductosVenta.add(producto+" Precio:  "+precio+" Cantidad:  "+cantidad+" subtotal  "+subtotal);
                //crear un adaptador para presentar los datos del listado (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorCursos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaproductosVenta);
                lstProdutosVenta.setAdapter(adaptadorCursos); //presentando el adaptador dentro del listview
            }while(productosObtenidos.moveToNext()); //validando si aun existen cursos dentro del cursor
        }else{
            lstProdutosVenta.invalidateViews();
            Toast.makeText(getApplicationContext(), "No existen productos en la venta", Toast.LENGTH_LONG).show();
        }
    }

    public void consultarCliente(){
        int idUsu = Integer.parseInt(idUsuario_venta);
        clienteObtenido = miBdd.obtenerUsuarioId(idUsu); //consultando cursos y guardandolos en un cursor
        if(clienteObtenido != null){ //verificando que realmente haya datos dentro de SQLite
            do{
                String nombreCliente = clienteObtenido.getString(1).toString();
                String direccionCliente = clienteObtenido.getString(2).toString();
                txtNombreCliente.setText(nombreCliente);
                txtDireccionCliente.setText(direccionCliente);

            }while(clienteObtenido.moveToNext()); //validando si aun existen cursos dentro del cursor
        }else{
            Toast.makeText(getApplicationContext(), "No se encontro el cliente", Toast.LENGTH_LONG).show();
        }
    }

    //Boton Salir
    public void salirDespacharVentas(View vista) { //metodo para cerrar
        Intent ventanaInicial = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(ventanaInicial);
        finish(); //cerrando la activity
    }

    //Boton Actualizar
    public void actualizarEstadoVenta(View vista) {
        //proceso de actualizacion al registro seleccionado
        int idven = Integer.parseInt(id_venta);
        String estado_modificado = "Entregado";
        int idUsu = Integer.parseInt(idUsuario_venta);
        double total = Double.parseDouble(total_venta);
        miBdd.actualizarVentaId(fecha_venta,total,estado_modificado,idUsu,idven);
        Toast.makeText(getApplicationContext(), "Venta Despachada", Toast.LENGTH_LONG).show();
        actualizarStockProducto();
    }

    public void actualizarStockProducto() {
        if (productosObtenidos == null){
            Toast.makeText(getApplicationContext(),"No hay Productos Añadidos" ,Toast.LENGTH_SHORT).show();
        }else {
            for (int i = 0; i < productosObtenidos.getCount(); i++) {
                Integer nuevoStock=0;
                productosObtenidos.moveToPosition(i);
                String nombreProductoDetalle = productosObtenidos.getString(1);
                String cantidadProductoDetalle = productosObtenidos.getString(3);
                Integer restarStock =Integer.parseInt(cantidadProductoDetalle);
                productoNombre = miBdd.obtenerProductoPorNombre(nombreProductoDetalle);
                if(productoNombre != null){ //verificando que realmente haya datos dentro de SQLite
                    do{
                        Integer idProductoBdd = productoNombre.getInt(0);
                        Integer stockActualBdd = productoNombre.getInt(3);
                        nuevoStock= stockActualBdd-restarStock;
                        //Toast.makeText(getApplicationContext(), "mm"+idProductoBdd+"dd"+stockActualBdd+"aa"+nuevoStock, Toast.LENGTH_LONG).show();
                        miBdd.actualizarStockProductoId(nuevoStock,idProductoBdd);
                    }while(productoNombre.moveToNext()); //validando si aun existen cursos dentro del cursor
                }else{
                    Toast.makeText(getApplicationContext(), "No se encontro el producto", Toast.LENGTH_LONG).show();
                }

            }

            Intent ventanaVentas = new Intent(getApplicationContext(), VerDespacharVentasActivity.class);
            startActivity(ventanaVentas);
            finish(); //cerrando la activity

        }

    }


}
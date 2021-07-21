package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;


public class DetalleProductoActivity extends AppCompatActivity {
    //para consultar el id del usuario que tiene abierta la secion**********************************
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objetito de tipo editor de sharedpreferences
    String llaveIdUsuario = "tipoIdUsu";
    String idUsuario;
    //**********************************************************************************************
    EditText txt_cantidadCompra;
    TextView txt_nombrePro,txt_detallePro;
    BaseDatos miBdd;
    String nombrePro,precioPro,cantidadPro, detallePro, imagenPro;
    ImageView img_producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        txt_nombrePro=(TextView) findViewById(R.id.txt_nombrePro);
        txt_detallePro=(TextView) findViewById(R.id.txt_detallePro);
        txt_cantidadCompra=(EditText)findViewById(R.id.txt_cantidadCompra);
        img_producto=(ImageView)findViewById(R.id.img_producto);
        //inicializar elementos shared para obtener el id del usuario que tiene abierta la secion***
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        idUsuario= this.preferences.getString(llaveIdUsuario,"");
        //******************************************************************************************
        miBdd=new BaseDatos(getApplicationContext());

        Bundle parametrosExtra = getIntent().getExtras();

        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                nombrePro = parametrosExtra.getString("nombreProd");
                precioPro = parametrosExtra.getString("precioProd");
                cantidadPro = parametrosExtra.getString("cantidadProd");
                detallePro = parametrosExtra.getString("descripcionProd");
                imagenPro = parametrosExtra.getString("fotoProd");


            }catch (Exception ex){ //ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

        //presentar los datos recibidos de curso en pantalla
        txt_nombrePro.setText(nombrePro);
        txt_detallePro.setText(detallePro);
        //presenta la imagen creada como mapa de bits en el imagen view
        Bitmap myBitmap = BitmapFactory.decodeFile(imagenPro);
        img_producto.setImageBitmap(myBitmap);

    }
    public void Cancelar(View vista) {
        Intent ventanaLista = new Intent(getApplicationContext(), ConsultarProductoActivity.class);
        startActivity(ventanaLista);
        finish();
    }

    public void addCarrito(View vista) {
        //Capturando los valores ingresados por el usuario en variables Java de tipo String.
        String cantidadSolicitadaString = txt_cantidadCompra.getText().toString();

        if (cantidadSolicitadaString.isEmpty()){
            Toast.makeText(getApplicationContext(), "Para continuar Ingresa una cantidad",Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio
        }else{
            int cantidadSolicitada = Integer.parseInt(cantidadSolicitadaString);
            int cantidadStock = Integer.parseInt(cantidadPro);
            int idUsu = Integer.parseInt(idUsuario);
            double precio = Double.parseDouble(precioPro);
            double subtotal = cantidadSolicitada*precio;
            if(cantidadSolicitada <= 0){
                Toast.makeText(getApplicationContext(), "La cantidad no puede ser menor o igual a 0",Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio
            }else{
                if(cantidadSolicitada > cantidadStock){
                    Toast.makeText(getApplicationContext(), "No existe esa cantidad en Stock",Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio
                }else{
                    miBdd.agregarProductoAlCarrito(nombrePro,precio, cantidadSolicitada, subtotal, idUsu);
                    Toast.makeText(getApplicationContext(), "Producto Añadido al Carrito", Toast.LENGTH_LONG).show(); //mostrando mensaje de usuario registrado
                    finish();
                    Intent ventanaConsultarProductos=new Intent(getApplicationContext(),ConsultarProductoActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
                    startActivity(ventanaConsultarProductos); //solicitamos que habra el formulario de login
                }
            }
        }
    }



}
package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    EditText txt_cantidadCompra;
    TextView txt_nombrePro,txt_detallePro;
    BaseDatos miBdd;
    String nombrePro, detallePro, imagenPro;
    ImageView img_producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        txt_nombrePro=(TextView) findViewById(R.id.txt_nombrePro);
        txt_detallePro=(TextView) findViewById(R.id.txt_detallePro);
        txt_cantidadCompra=(EditText)findViewById(R.id.txt_cantidadCompra);
        img_producto=(ImageView)findViewById(R.id.img_producto);
        miBdd=new BaseDatos(getApplicationContext());


        Bundle parametrosExtra = getIntent().getExtras();

        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                nombrePro = parametrosExtra.getString("nombreProd");
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

}
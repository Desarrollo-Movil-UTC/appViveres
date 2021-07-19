package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 17/07/2021
@Modificación: 17/07/2021
@descripción: Detalle Proveedor
*/
public class DetalleProveedoresActivity extends AppCompatActivity {
    //definicion de variables: para capturar los valores que recibe la actividad
    String idP, nombreP, descripcionP, telefonoP, celularP, emailP,diasVisitaP,imagenP;
    TextView txtIdP,txtNombreP,txtDescripcionP,txtTelefonoP,txtCelularP,txtEmailP,txtDiasVisitaP;
    ImageView imgProveedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_proveedores);
        //Mapeo de elementos
        txtIdP=(TextView) findViewById(R.id.txtID);
        txtNombreP=(TextView) findViewById(R.id.txtNombre);
        txtDescripcionP=(TextView) findViewById(R.id.txtDescripcion);
        txtTelefonoP=(TextView) findViewById(R.id.txtTelefono);
        txtCelularP=(TextView) findViewById(R.id.txtCelular);
        txtEmailP=(TextView) findViewById(R.id.txtEmail);
        txtDiasVisitaP=(TextView) findViewById(R.id.txtDiasVisita);
        imgProveedor=findViewById(R.id.imgProveedor);

        //objeto tipo Bundle que captura los parametros que se han pasado a esta actividad
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                idP = parametrosExtra.getString("idProv");
                nombreP = parametrosExtra.getString("nombreProv");
                descripcionP = parametrosExtra.getString("descripcionProv");
                telefonoP = parametrosExtra.getString("telefonProv");
                celularP = parametrosExtra.getString("celularProv");
                emailP = parametrosExtra.getString("emailProv");
                diasVisitaP = parametrosExtra.getString("diasVisitaProv");
                imagenP = parametrosExtra.getString("imagen_prov");
                //Toast.makeText(getApplicationContext(), "datos"+imagenP,Toast.LENGTH_LONG ).show();
            }catch (Exception ex){ //ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

        //presentar los datos recibidos de curso en pantalla
        txtIdP.setText(idP);
        txtNombreP.setText(nombreP);
        txtDescripcionP.setText(descripcionP);
        txtTelefonoP.setText(telefonoP);
        txtCelularP.setText(celularP);
        txtEmailP.setText(emailP);
        txtDiasVisitaP.setText(diasVisitaP);

        //Definiendo la URL de la imagen del cliente seleccionado
        String urlImagen="http://192.168.0.191:8000"+imagenP;
        //Dibujando la imagen dentro del ImageVIew correspondiente
        Glide.with(getApplicationContext()).load(urlImagen).into(imgProveedor);
    }

    //Boton Salir
    public void salirDetalleProveedores(View vista) { //metodo para cerrar
        Intent ventanaInicial = new Intent(getApplicationContext(), ProveedoresActivitty.class);
        startActivity(ventanaInicial);
        finish(); //cerrando la activity
    }


}
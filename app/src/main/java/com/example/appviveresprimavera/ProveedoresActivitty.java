package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appviveresprimavera.entidades.Proveedor;
import com.example.appviveresprimavera.webservice.ClienteApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProveedoresActivitty extends AppCompatActivity {
    //Instanciando Objetos para las Peticiones REST al ApiServer
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.191:8000/") //definiendo la URL base de la web
            .addConverterFactory(GsonConverterFactory.create()).build(); //se convierte los datos a json
    ClienteApi clienteApi=retrofit.create(ClienteApi.class);
    ArrayList<Proveedor> listadoProveedores= new ArrayList<>();
    LinearLayout layoutScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores_activitty);
        //Consultando clientes del ApiServer y presentandolos en el ListView correspondiente
        obtenerClientes(null);
        //Mapeo de elementos de xml
        layoutScroll=(LinearLayout) findViewById(R.id.layoutScroll);

    }

    //Metodo para consultar todos los clientes mediante el ApiServer
    public void obtenerClientes(View vista){
        listadoProveedores.clear(); //vaciando el listado de clientes del ListView
        Call<List<Proveedor>> clientesHTTP=clienteApi.obtenerTodos();//realizar la llamada HTTP a partir del API
        //Obteniendo los datos del ApiServer y generando validacion para presentarlos en pantalla
        //o mostrar mensajes de error:
        clientesHTTP.enqueue(new Callback<List<Proveedor>>() {
            //Metodo que se ejecuta cuando hay una RESPUESTA CORRECTA:
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                try{
                    if(response.isSuccessful()){
                        List<Proveedor> proveedores=response.body(); //capturando el listado JSON de clientes
                        for (int i=0;i<proveedores.size();i++){
                            listadoProveedores.add(proveedores.get(i));//Agregando los clientes en el ListView
                            //crear un boton por cada elemento que capture
                            String imagenP = proveedores.get(i).getImagen();
                            crearBoton(imagenP,i);
                        }
                    }
                }catch (Exception ex){
                    //Mensaje de advertencia cuando el ApiServer tiene algun error
                    Toast.makeText(getApplicationContext(),"ERROR -> "+ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {
                //Mensaje de advertencia cuando no hay Conexion al ApiServer
                Toast.makeText(getApplicationContext(),"ERROR DE CONEXIÓN ",Toast.LENGTH_LONG).show();
            }
        });


    }

    private View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selected_item = (Integer) v.getId(); //capturo el id del boton para consultar el datos del proeedor
            //Toast.makeText(getApplicationContext(), "numero"+selected_item,Toast.LENGTH_LONG).show();
            //Capturando al cliente seleccionado en el ListVIew mediante su posición
            Proveedor proveedorSeleccionado=listadoProveedores.get(selected_item);
            String idProv =proveedorSeleccionado.getId();
            String nombreProv =proveedorSeleccionado.getNombre();
            String descripcionProv =proveedorSeleccionado.getDescripcion();
            String telefonProv =proveedorSeleccionado.getTelefono();
            String celularProv =proveedorSeleccionado.getCelular();
            String emailProv =proveedorSeleccionado.getEmail();
            String diasVisitaProv =proveedorSeleccionado.getDias_visita();
            String imagen_prov =proveedorSeleccionado.getImagen();
            //manejando el objeto para abrir la ventana de editarCurso
            Intent ventanaDetalleProveedor = new Intent(getApplicationContext(), DetalleProveedoresActivity.class);
            ventanaDetalleProveedor.putExtra("idProv", idProv); //pasando el id del detalle como parametro
            ventanaDetalleProveedor.putExtra("nombreProv", nombreProv);
            ventanaDetalleProveedor.putExtra("descripcionProv", descripcionProv);
            ventanaDetalleProveedor.putExtra("telefonProv", telefonProv);
            ventanaDetalleProveedor.putExtra("celularProv", celularProv);
            ventanaDetalleProveedor.putExtra("emailProv", emailProv);
            ventanaDetalleProveedor.putExtra("diasVisitaProv", diasVisitaProv);
            ventanaDetalleProveedor.putExtra("imagen_prov", imagen_prov);
            startActivity(ventanaDetalleProveedor); //solicitando que se habra la ventana para editar curso
            finish(); //cerrando la ventana de editar curso una vez terminado el prceso de edicion

        }
    };

    //Boton Salir
    public void salirListaProveedores(View vista) { //metodo para cerrar
        Intent ventanaInicial = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ventanaInicial);
        finish(); //cerrando la activity
    }

    public void crearBoton(String imagenP, Integer indice){
        ImageButton boton = new ImageButton(this);
        //si quiero que ocupe el tamaño del contenedor
        //boton.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //definir tamaño del boton
        int ancho = 600;
        int alto = 600;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
        boton.setLayoutParams(params);
        //definir fondo;
        boton.setBackgroundColor(Color.TRANSPARENT);
        //definir el id
        boton.setId(indice);
        //definir metodo onclic
        boton.setOnClickListener(ClickListener);
        //boton.setImageResource(R.drawable.logo);//si quiero definir imagen estatica
        //Definiendo la URL de la imagen del Proveedor seleccionado que viene de la API
        String urlImagen="http://192.168.0.191:8000"+imagenP;
        //Dibujando la imagen dentro del imageButton correspondiente
        Glide.with(getApplicationContext()).load(urlImagen).into(boton);
        layoutScroll.addView(boton); //lo inserto en la vista

    }

}
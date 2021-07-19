package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    //Declarando Objetos para la Interfaz Gráfica
    ListView lstProveedores;
    //Instanciando Objetos para las Peticiones REST al ApiServer
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.105:8000/") //definiendo la URL base de la web
            .addConverterFactory(GsonConverterFactory.create()).build(); //se convierte los datos a json
    ClienteApi clienteApi=retrofit.create(ClienteApi.class);
    ArrayList<Proveedor> listadoProveedores= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores_activitty);
        //Mapeo de Elementos XML
        lstProveedores=findViewById(R.id.lstProveedores);
        //Consultando clientes del ApiServer y presentandolos en el ListView correspondiente
        obtenerClientes(null);
        //Accion al dar click sobre un elemento de la lista
        lstProveedores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Capturando al cliente seleccionado en el ListVIew mediante su posición
                Proveedor proveedorSeleccionado=listadoProveedores.get(position);
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
        });

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
                        for(Proveedor proveedor: proveedores){ //Recorriendo cada uno de los clientes obtenidos del ApiServer
                            listadoProveedores.add(proveedor);//Agregando los clientes en el ListView
                            //Generando el Adaptador de  Clientes para poder presentarlos en el ListView correspondiente
                            ArrayAdapter<Proveedor> adaptadorClientes=new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1,listadoProveedores);
                            lstProveedores.setAdapter(adaptadorClientes); //Asignando el Adaptador en el ListView
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

    //Boton Salir
    public void salirListaProveedores(View vista) { //metodo para cerrar
        Intent ventanaInicial = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ventanaInicial);
        finish(); //cerrando la activity
    }

}
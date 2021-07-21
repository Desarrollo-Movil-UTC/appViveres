package com.example.appviveresprimavera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaUsuarioActivity extends AppCompatActivity {

    //definicion de elementos xml
    ListView listUsuarios;
    ArrayList<String> listaUsuarios = new ArrayList<>(); //cargar los datos de la BDD

    //cursor para obtener los prodcuto
    Cursor usuariosObtenidos;

    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);

        //mapeo de elementos
        listUsuarios = (ListView) findViewById(R.id.listUsuarios);

        //instanciar /construir la base de datos en el objeto mi bdd
        miBdd = new BaseDatos(getApplicationContext());
        consultarUsuarios(); //invoca al metodo de listar
        listUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuariosObtenidos.moveToPosition(position);

                //mediante el cursor se obtiene los datos del curso seleccionado
                String idUsu = usuariosObtenidos.getString(0).toString();
                String nombreUsu = usuariosObtenidos.getString(1).toString();
               eliminarUsuario(nombreUsu,idUsu);
            }
        });
    }
    public void eliminarUsuario(String nombreU, String idU) {
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("CONFIRMACIÓN")
                .setMessage("¿Está seguro de eliminar el usuario "+nombreU+"?")
                .setPositiveButton("Si, Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //id viene de las variables declaradas que contiene el putExtra
                        miBdd.eliminarUsuario(idU); //procesa la eliminacion con base al id del cliente
                        Toast.makeText(getApplicationContext(), "Usuario "+nombreU+" eliminado exitosamente",Toast.LENGTH_SHORT).show();
                        consultarUsuarios(); //invoca al metodo de listar
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Eliminación cancelada",Toast.LENGTH_SHORT).show();

                    }
                }).setCancelable(true); //es posible que se pueda cancelar el proceso
        //construir el cuadro de dialogo
        AlertDialog cuadroDialogo = estructuraConfirmacion.create();
        cuadroDialogo.show(); //se presenta en pantalla
    }

    public void consultarUsuarios(){
        listaUsuarios.clear(); //vaciando el listado
        usuariosObtenidos = miBdd.obtenerUsuarios(); //consultando cursos y guardandolos en un cursor
        if(usuariosObtenidos != null){ //verificando que realmente haya datos dentro de SQLite

            do{
                String id = usuariosObtenidos.getString(0).toString();
                String nombre = usuariosObtenidos.getString(1).toString();
                String direccion = usuariosObtenidos.getString(2).toString();
                String email = usuariosObtenidos.getString(3).toString();
                String password = usuariosObtenidos.getString(4).toString();
                String tipo = usuariosObtenidos.getString(5).toString();


                //construyendo las filas para presentar datos en el ListView
                listaUsuarios.add(id+": "+nombre+" | Tipo Usuario: "+tipo);

                //crear un adaptador para presentar los datos del listado (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorUsuarios = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaUsuarios);
                listUsuarios.setAdapter(adaptadorUsuarios); //presentando el adaptador dentro del listview

            }while(usuariosObtenidos.moveToNext()); //validando si aun existen prodcutos
        }else{
            Toast.makeText(getApplicationContext(), "No existen usuarios", Toast.LENGTH_LONG).show();
        }
    }

    //Boton Salir
    public void salirLista(View vista) { //metodo para cerrar
        Intent ventanaMenu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(ventanaMenu);
        finish(); //cerrando la activity
    }
    public void registrarAdmin(View vista) {
        Intent ventanaMenu = new Intent(getApplicationContext(), RegistrarAdminActivity.class);
        startActivity(ventanaMenu);
        finish(); //cerrando la activity
    }
}
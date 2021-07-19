package com.example.appviveresprimavera;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 17/07/2021
@fModificación 17/07/2021
@descripción: Base de Datos
*/

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class BaseDatos extends SQLiteOpenHelper {
    private static final String nombreBdd = "bdd_viveres"; //definiendo el nombre dela Bdd
    private static final int versionBdd = 4; //definiendo la version de la BDD

    //estructura de la tabla productos

    private static final String tablaProducto= "create table producto(id_prod integer primary key autoincrement," +
            "nombre_prod text, fecha_prod text, stock_prod integer, precio_prod double, URLimagen_prod text, descripcion_prod text, proveedor_prod text)"; // definiendo estructura de la tabla usuarios
    private static final String tablaUsuario="create table usuario(id_usu integer primary key autoincrement, nombre_usu text, direccion_usu text," +
            "email_usu text,password_usu text,tipo_usu text);";//definir la estructura de la tabla de usuarios

    /*
    private static final String tablaProducto= "create table producto(id_prod integer primary key autoincrement," +
            "nombre_prod text, fecha_prod text, stock_prod integer, precio_prod double, imagen_prod BLOB, URLimagen_prod text, descripcion_prod text)"; // definiendo estructura de la tabla usuarios
     */

    //Constructor
    public BaseDatos(Context contexto) {
        super(contexto, nombreBdd, null, versionBdd);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ejecutando el query DDl(sentencia de definicion de datos) para crear la tabla
        db.execSQL(tablaProducto);
        db.execSQL(tablaUsuario);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS producto");//elimincacion de la version anterior de la tabla usuarios se puee usar otro comando Dll como alter table
        db.execSQL(tablaProducto); //Ejecucion del codigo para crear la tabla usuaios con su nueva estructura
        db.execSQL("DROP TABLE IF EXISTS usuario");// elimina de la version anterior de la tabla usuario
        db.execSQL(tablaUsuario);
    }

    //agregar producto (String nombre, String fecha, int stock, double precio, byte[] imagen,  String URLimagen, String descripcion)
    public boolean agregarProducto(String nombre, String fecha, int stock, double precio, String URLimagen, String descripcion, String proveedor){
        SQLiteDatabase miBdd=getWritableDatabase(); //instanciamos la BDD y llamamos a la BDD en el objeto miBdd
        if(miBdd != null){ //validar la BDD
            //realiza el proceso de insercion
            miBdd.execSQL("insert into producto(nombre_prod, fecha_prod, stock_prod, precio_prod, URLimagen_prod, descripcion_prod) " +
                    "values  ('"+nombre+"','"+fecha+"','"+stock+"','"+precio+"','"+URLimagen+"','"+descripcion+"')");

            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retorno cuando la insercion es exitosa
        }
        return false; //retorno cuando no existe la bdd
    }

    //PROCESO 6: metodo para consultar clientes existentes en la BDD
    public Cursor obtenerProductos(){

        SQLiteDatabase miBdd = getWritableDatabase(); //llamado a la bdd
        //consultando los productos en la BDD y guardandolos en un cursor
        Cursor productos=miBdd.rawQuery("select * from producto", null);
        if(productos.moveToFirst()){ //verifica que el objeto tenga resultados
            miBdd.close(); //cerrando la conexion a la bdd
            return productos; //retorna el cursos que contiene el listado de clientes
        }else{
            return null; //retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    public boolean actualizarProducto(String nombre, String fecha, int stock, double precio, String imagen,
                                      String descripcion, String id){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd
        if (miBdd != null){ //validando que la bdd realmente exista
            //proceso de actualizacion
            miBdd.execSQL("update producto set nombre_prod='"+nombre+"', " +
                    "fecha_prod='"+fecha+"', stock_prod='"+stock+"', precio_prod='"+precio+"', " +
                    "URLimagen_prod='"+imagen+"',descripcion_prod='"+descripcion+"' where id_prod="+id);
            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retornando verdadero ya que el procesos de actualizacion fue exitoso
        }
        return false; //se retorna falso cuando no existe la bdd
    }

    public boolean eliminarProducto(String id){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd

        if (miBdd != null){ //validando que la bdd realmente exista

            //proceso de eliminacion
            miBdd.execSQL("delete from producto where id_prod="+id);
            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retornando verdadero ya que el proceso de eliminacion fue exitoso
        }
        return false; //se retorna falso cuando no existe la bdd
    }
    public boolean agregarUsuario(String nombre, String direccion, String email, String password, String tipo){
        SQLiteDatabase miBdd = getWritableDatabase(); // llamando a la base de datos en el objeto miBdd
        if(miBdd!=null){// validando que existan datos en la base de datos
            miBdd.execSQL("insert into usuario(nombre_usu,direccion_usu, email_usu, password_usu, tipo_usu) " +
                    "values('"+nombre+"','"+direccion+"','"+email+"','"+password+"','"+tipo+"')");//ejecutando la sentencia de insercion sql
            miBdd.close(); //cerrando la conexion a la base de datos
            return true; //retorno cuando la insercion es exitosa
        }
        return false;// retorno cuando no exista la base de datos
    }
    public Cursor obtenerUsuarioPorEmailPassword(String email, String password){
        SQLiteDatabase miBdd = getWritableDatabase(); // llamando a la base de datos en el objeto miBdd
        //Ejecutando la consulta y almacenando las resultados en el objeto usuario
        Cursor usuario= miBdd.rawQuery("select * from usuario where email_usu='"+email+"'\n" +
                "and password_usu='"+password+"';", null);
        if(usuario.moveToFirst()){ //verificando que el objeto usuario tenga resultados
            return usuario; //retornamos los datos encontrados

        }else{
            //No se encuentra el usuario -> porque no existe o porque el email y contrase;a ingresados son incorrectos
            return null;
        }

    }
}

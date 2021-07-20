package com.example.appviveresprimavera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 15/07/2021
@Modificación: 15/07/2021
@descripción: Gestion de productos - Actualizar/eliminar productos
*/

public class ActualizarEliminarProductoActivity extends AppCompatActivity implements View.OnClickListener {

    //definicion de variables
    String id,nombreProd,fechaCad,cantidad,precio,URLfoto,descripcion; // variables para capturar valores que vienen cmo parametro

    //definicion de elementos xml
    TextView txtIdProducto, txtFotoProductoEditar;
    EditText txtNombreProductoEditar, txtFechaCaducidadProductoEditar, txtCantidadProductoEditar, txtPrecioProductoEditar, txtDescripcionProductoEditar;
    ImageView FotoProductoEditar;
    Button btnFechaCaducidadProductoEditar, btnFotoProductoEditar;

    // Capturar fecha actual y fecha ingresada
    private int Anio_act, Mes_act, Dia_act;
    private int Anio_prod, Mes_prod, Dia_prod;

    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    //ruta
    private final String ruta_fotos = Environment.getExternalStorageDirectory().
            getAbsolutePath() +"/viveres/";
    private File file = new File(ruta_fotos);

    Uri uri;
    File mi_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_eliminar_producto);

        //mapeo de elementos
        txtIdProducto = (TextView) findViewById(R.id.txtIdProducto);
        txtNombreProductoEditar = (EditText) findViewById(R.id.txtNombreProductoEditar);
        txtFechaCaducidadProductoEditar = (EditText) findViewById(R.id.txtFechaCaducidadProductoEditar);
        txtCantidadProductoEditar = (EditText) findViewById(R.id.txtCantidadProductoEditar);
        txtPrecioProductoEditar = (EditText) findViewById(R.id.txtPrecioProductoEditar);
        txtDescripcionProductoEditar = (EditText) findViewById(R.id.txtDescripcionProductoEditar);

        txtFotoProductoEditar = (TextView) findViewById(R.id.txtFotoProductoEditar);
        FotoProductoEditar = (ImageView) findViewById(R.id.FotoProductoEditar);

        btnFechaCaducidadProductoEditar = (Button) findViewById(R.id.btnFechaCaducidadProductoEditar);
        btnFechaCaducidadProductoEditar.setOnClickListener(this); //al dar click
        btnFotoProductoEditar = (Button) findViewById(R.id.btnFotoProductoEditar);
        btnFotoProductoEditar.setOnClickListener(this); //al dar click

        //objeto tipo Bundle que captura los parametros que se han pasado a esta actividad
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                id = parametrosExtra.getString("idProd");
                nombreProd = parametrosExtra.getString("nombreProd");
                fechaCad = parametrosExtra.getString("fechaProd");
                cantidad = parametrosExtra.getString("cantidadProd");
                precio = parametrosExtra.getString("precioProd");
                URLfoto = parametrosExtra.getString("fotoProd");
                descripcion = parametrosExtra.getString("descripcionProd");

            }catch (Exception ex){ //ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

        //presentar los datos recibidos de curso en pantalla
        txtIdProducto.setText(id);
        txtNombreProductoEditar.setText(nombreProd);
        txtFechaCaducidadProductoEditar.setText(fechaCad);
        txtCantidadProductoEditar.setText(cantidad);
        txtPrecioProductoEditar.setText(precio);
        txtFotoProductoEditar.setText(URLfoto);
        txtDescripcionProductoEditar.setText(descripcion);
        //presenta la imagen creada como mapa de bits en el imagen view
        Bitmap myBitmap = BitmapFactory.decodeFile(URLfoto);
        FotoProductoEditar.setImageBitmap(myBitmap);

        //instanciar /construir la base de datos en el objeto mi bdd
        miBdd= new BaseDatos(getApplicationContext());

        //Si no existe crea la carpeta donde se guardaran las fotos
        file.mkdirs();
    }

    //Boton Volver
    public void volverProductos(View vista) {
        Intent ventanaGestionProductos = new Intent(getApplicationContext(), GestionProductosActivity.class);
        startActivity(ventanaGestionProductos);
        finish();
    }

    //Boton Actualizar
    public void actualizarProducto(View vista) {
        //capturando los valores ingresados por el usuario en variables Java de tipo String
        String nombre = txtNombreProductoEditar.getText().toString();
        String fecha = txtFechaCaducidadProductoEditar.getText().toString();
        String cantidad = txtCantidadProductoEditar.getText().toString();
        String precio = txtPrecioProductoEditar.getText().toString();
        String foto= txtFotoProductoEditar.getText().toString();;
        String descripcion = txtDescripcionProductoEditar.getText().toString();

        //validaciones
        //campos vacios
        if (nombre.isEmpty() || fecha.isEmpty() || cantidad.isEmpty() || precio.isEmpty() || foto.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                    Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
        } else {
            if (contieneSoloLetras(nombre) == false) {
                Toast.makeText(getApplicationContext(), "El nombre no debe contener números",
                        Toast.LENGTH_LONG).show(); //mostrando error de nombre
            } else { //CANTIDAD diferente de 0
                int cantidad2 = Integer.parseInt(cantidad);
                if (cantidad2 <= 0) {
                    Toast.makeText(getApplicationContext(), "La cantidad debe ser mayor a 0",
                            Toast.LENGTH_LONG).show();
                } else { //PRECIO diferente de 0
                    double precio2 = Double.parseDouble(precio);
                    if (precio2 > 0) { //precio2 <= 0
                        //proceso de actualizacion al registro seleccionado
                        miBdd.actualizarProducto(nombre, fecha, cantidad2, precio2, foto, descripcion, id);
                        Toast.makeText(getApplicationContext(), "Actualizacion exitosa", Toast.LENGTH_LONG).show();
                        volverProductos(null); //invocando al metodo volverCliente

                    } else {
                        Toast.makeText(getApplicationContext(), "El Precio debe ser un valor aceptable",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        }
    }

    //Boton Eliminar
    public void eliminarProducto(View vista) {
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("CONFIRMACIÓN")
                .setMessage("¿Está seguro de eliminar el producto "+nombreProd+"?")
                .setPositiveButton("Si, Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //id viene de las variables declaradas que contiene el putExtra
                        miBdd.eliminarProducto(id); //procesa la eliminacion con base al id del cliente
                        Toast.makeText(getApplicationContext(), "Producto "+nombreProd+" eliminado exitosamente",Toast.LENGTH_SHORT).show();
                        volverProductos(null);
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

    public boolean contieneSoloLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c == 'ñ' || c == 'Ñ'
                    || c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú'
                    || c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú')) {
                return false;
            }
        }
        return true;
    }

    //METODOS PARA OBTENER Y VALIDAR LA FECHA DE CADUCIDAD
    @Override
    public void onClick(View v) {
        // obtenemos la fecha actual
        final Calendar calendario = Calendar.getInstance();
        Anio_act = calendario.get(Calendar.YEAR);
        Mes_act = calendario.get(Calendar.MONTH);
        Dia_act = calendario.get(Calendar.DAY_OF_MONTH);

        if (v == btnFechaCaducidadProductoEditar) {
            //creamos la instancia de tipo date
            //this indicamos que se despliegue en el mismo formulario
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                //captura la fecha seleccionada por el usuario
                @Override
                public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                    //las variables declaradas para fecha inicio toman la fecha seleccionada
                    Anio_prod = anio;
                    Mes_prod = mes;
                    Dia_prod = dia;
                    String fechaDeCaducidad = (Dia_prod+"/"+(Mes_prod + 1)+"/"+Anio_prod);
                    validarFechaCaducidad(fechaDeCaducidad);
                }
            }, Anio_act, Mes_act, Dia_act); //presenta picker con la fecha actual
            datePickerDialog.show(); //mostrando el date picker
        }

        if (v == btnFotoProductoEditar) {
            cargarImagen();
        }
    }

    public boolean validarFechaCaducidad(String fechaCaducidadString){
        //formato de  fecha
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            //obtener fecha de hoy y se lo guarda en stringFechaHoy con tipo fecha
            String stringFechaHoy = df.format(Calendar.getInstance().getTime());

            //casting de fechas a tipo Date
            Date fechaHoy =df.parse(stringFechaHoy);
            Date fechaCaducidad = df.parse(fechaCaducidadString);

            //si fecha inicio esta despues de la fecha actual devuelve true
            if(fechaCaducidad.after(fechaHoy)){
                txtFechaCaducidadProductoEditar.setText(Dia_prod+"/"+(Mes_prod + 1)+"/"+Anio_prod);
                Toast.makeText(getApplicationContext(), "Fecha editada correctamente", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                Toast.makeText(getApplicationContext(), "Fecha seleccionada es incorrecta", Toast.LENGTH_LONG).show();
                txtFechaCaducidadProductoEditar.setText(fechaCad);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error de formato en la fecha", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //METODOS PARA CARGAR FOTO
    private void cargarImagen(){
        String file = ruta_fotos + getCode() + ".png";
        mi_foto = new File( file );
        try {
            mi_foto.createNewFile();
        } catch (IOException ex) {
            Log.e("ERROR ", "Error:" + ex);
        }
        //
        uri = Uri.fromFile( mi_foto );
        //Abre la galeria
        Intent imagen = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagen.setType("image/");
        //startActivityForResult(imagen.createChooser(imagen, "Seleccione"),10);

        //Guarda imagen
        imagen.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //Retorna a la actividad
        startActivityForResult(imagen, 0);
        /*
        Intent imagen = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagen.setType("image/");
        startActivityForResult(imagen.createChooser(imagen, "Seleccione"),10);
         */
    }

    @SuppressLint("SimpleDateFormat")
    private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        String photoCode = "pic_" + date;
        return photoCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){ // si selecciona la imagen
            Uri path = data.getData();
            String dir = getPath(path);
            //String dir = getPath(uri);

            FotoProductoEditar.setImageURI(path); //presenta imagen seleccionada
            //txtFotoProducto.setText(dir); //presenta la direccion

            txtFotoProductoEditar.setText(dir); //presenta la direccion
        } else{
            Toast.makeText(getApplicationContext(), "No ha seleccionado una imagen", Toast.LENGTH_LONG).show();
        }
    }

    public String getPath (Uri uri){
        if(uri==null) return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }


}
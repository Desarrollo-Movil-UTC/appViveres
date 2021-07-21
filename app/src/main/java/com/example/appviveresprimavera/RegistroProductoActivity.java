package com.example.appviveresprimavera;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appviveresprimavera.entidades.Proveedor;
import com.example.appviveresprimavera.webservice.ClienteApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 17/07/2021
@Modificación: 17/07/2021
@descripción: Gestion de productos - Registro de productos
*/

public class RegistroProductoActivity extends AppCompatActivity implements View.OnClickListener {

    //definicion de elementos xml
    EditText txtNombreProducto, txtFechaCaducidadProducto, txtCantidadProducto, txtPrecioProducto, txtDescripcionProducto;
    ImageView FotoProducto;
    TextView txtFotoProducto;
    Button btnFechaCaducidadProducto, btnFotoProducto;

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
        setContentView(R.layout.activity_registro_producto);

        //mapeo de elementos
        txtNombreProducto = (EditText) findViewById(R.id.txtNombreProducto);
        txtFechaCaducidadProducto = (EditText) findViewById(R.id.txtFechaCaducidadProducto);
        txtCantidadProducto = (EditText) findViewById(R.id.txtCantidadProducto);
        txtPrecioProducto = (EditText) findViewById(R.id.txtPrecioProducto);
        txtDescripcionProducto = (EditText) findViewById(R.id.txtDescripcionProducto);

        FotoProducto = (ImageView) findViewById(R.id.FotoProducto);
        FotoProducto.setImageResource(R.drawable.fondo_img_producto);
        txtFotoProducto = (TextView) findViewById(R.id.txtFotoProducto);

        btnFechaCaducidadProducto = (Button) findViewById(R.id.btnFechaCaducidadProducto);
        btnFechaCaducidadProducto.setOnClickListener(this); //al dar click
        btnFotoProducto = (Button) findViewById(R.id.btnFotoProducto);
        btnFotoProducto.setOnClickListener(this); //al dar click

        //instanciar /construir la base de datos en el objeto mi bdd
        miBdd= new BaseDatos(getApplicationContext());

        //Si no existe crea la carpeta donde se guardaran las fotos
        file.mkdirs();

    }

    //Boton Cancelar
    public void LimpiarRegistroProducto(View vista) {
        txtNombreProducto.setText("");
        txtFechaCaducidadProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");
        txtFotoProducto.setText("");
        txtDescripcionProducto.setText("");

        FotoProducto.setImageResource(R.drawable.fondo_img_producto);
        txtNombreProducto.requestFocus();
    }

    //Boton Volver
    public void volverProductos(View vista) {
        Intent ventanaGestionProductos = new Intent(getApplicationContext(), GestionProductosActivity.class);
        startActivity(ventanaGestionProductos);
        finish();
    }

    //Boton Guardar
    public void guardarRegistroProducto(View vista) {

        //capturando los valores ingresados por el usuario en variables Java de tipo String
        String nombre = txtNombreProducto.getText().toString();
        String fecha = txtFechaCaducidadProducto.getText().toString();
        String cantidad = txtCantidadProducto.getText().toString();
        String precio = txtPrecioProducto.getText().toString();
        String URLfoto= txtFotoProducto.getText().toString();;
        String descripcion = txtDescripcionProducto.getText().toString();

        //validaciones
        //campos vacios
        if (nombre.isEmpty() || fecha.isEmpty() || cantidad.isEmpty() || precio.isEmpty() || URLfoto.isEmpty() || descripcion.isEmpty()) {
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

                        miBdd.agregarProducto(nombre, fecha, cantidad2, precio2, URLfoto, descripcion);
                        LimpiarRegistroProducto(null); //limpia los campos y como es llamado desde la vista envia null
                        Toast.makeText(getApplicationContext(), "Producto registrado exitosamente",
                                Toast.LENGTH_LONG).show();

                        Intent ventanaGestionProductos = new Intent(getApplicationContext(), GestionProductosActivity.class);
                        startActivity(ventanaGestionProductos);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "El Precio debe ser un valor aceptable",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        }

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

        if (v == btnFechaCaducidadProducto) {
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

        if (v == btnFotoProducto) {
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
                txtFechaCaducidadProducto.setText(Dia_prod+"/"+(Mes_prod + 1)+"/"+Anio_prod);
                Toast.makeText(getApplicationContext(), "Fecha correcta", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                Toast.makeText(getApplicationContext(), "Fecha seleccionada es incorrecta", Toast.LENGTH_LONG).show();
                txtFechaCaducidadProducto.setText("");
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

            FotoProducto.setImageURI(path); //presenta imagen seleccionada
            //txtFotoProducto.setText(dir); //presenta la direccion

            txtFotoProducto.setText(dir); //presenta la direccion
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





























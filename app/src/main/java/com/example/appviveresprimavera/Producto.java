package com.example.appviveresprimavera;

import android.content.Intent;

import java.io.Serializable;

public class Producto implements Serializable {
    private Integer id;
    private String nombre;
    private String precio;
    private String descripcion;
    private String imagen;

    public Producto(Integer id, String nombre, String precio, String descripcion, String imagen){
        this.id=id;
        this.nombre=nombre;
        this.precio=precio;
        this.descripcion=descripcion;
        this.imagen=imagen;
    }
    public Producto(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

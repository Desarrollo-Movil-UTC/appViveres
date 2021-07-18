package com.example.appviveresprimavera.entidades;

public class Proveedor {
    private String id;
    private String nombre;
    private String descripcion;
    private  String telefono;
    private  String celular;
    private  String email;
    private  String dias_visita;
    private  String imagen;

    public String toString(){
        return this.nombre+" "+this.telefono+" "+this.dias_visita;
    }

    public String getId() {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular)
    {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getDias_visita() {
        return dias_visita;
    }

    public void setDias_visita(String dias_visita)
    {
        this.dias_visita = dias_visita;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen)
    {
        this.imagen = imagen;
    }


}

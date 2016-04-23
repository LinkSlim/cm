package com.jj.futbol;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jesus on 21/04/2016.
 */
public class Lugar {
    private String id;
    private String direccion;
    private LatLng coordenadas;
    private String nombre;
    private Partido partido;

    public Lugar(String id, String direccion, LatLng coordenadas, String nombre, Partido partido) {
        this.id = id;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        this.nombre = nombre;
        this.partido = partido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LatLng getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(LatLng coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    @Override
    public String toString() {
        return "Lugar{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", coordenadas=" + coordenadas +
                ", partido=" + partido +
                '}';
    }
}
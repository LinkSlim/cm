package com.jj.futbol;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;

import java.io.Serializable;

/**
 * Created by Jesus on 21/04/2016.
 */
public class Partido implements Serializable{
    private Bitmap EscudoLocal;
    private String Local;
    private String Visitante;
    private Bitmap EscudoVisitante;

    public Partido(Bitmap EscudoLocal, String Local, String Visitante, Bitmap EscudoVisitante) {
        this.EscudoLocal = EscudoLocal;
        this.Local = Local;
        this.Visitante = Visitante;
        this.EscudoVisitante = EscudoVisitante;
    }

    public Partido(String local, String visitante) {
        this.EscudoLocal = null;
        this.Local = local;
        this.Visitante = visitante;
        this.EscudoVisitante = null;
    }

    public Partido() {
        this.EscudoLocal = null;
        this.Local = "";
        this.Visitante = "";
        this.EscudoVisitante = null;
    }


    public Bitmap getEscudoLocal() {
        return EscudoLocal;
    }

    public String getLocal() {
        return Local;
    }

    public String getVisitante() {
        return Visitante;
    }

    public Bitmap getEscudoVisitante() {
        return EscudoVisitante;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "Local='" + Local + '\'' +
                ", Visitante='" + Visitante + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partido partido = (Partido) o;

        if (!Local.equals(partido.Local)) return false;
        if (!Visitante.equals(partido.Visitante)) return false;
        return true;


    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Local.hashCode();
        result = 31 * result + Visitante.hashCode();

        return result;
    }
}
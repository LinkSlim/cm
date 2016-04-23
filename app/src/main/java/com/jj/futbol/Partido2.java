package com.jj.futbol;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Jesus on 21/04/2016.
 */
public class Partido2 implements Serializable{
    private String EscudoLocal;
    private String Local;
    private String Visitante;
    private String EscudoVisitante;

    public Partido2(String EscudoLocal, String Local, String Visitante, String EscudoVisitante) {
        this.EscudoLocal = EscudoLocal;
        this.Local = Local;
        this.Visitante = Visitante;
        this.EscudoVisitante = EscudoVisitante;
    }

    public Partido2(String local, String visitante) {
        this.EscudoLocal = null;
        this.Local = local;
        this.Visitante = visitante;
        this.EscudoVisitante = null;
    }

    public Partido2() {
        this.EscudoLocal = null;
        this.Local = "";
        this.Visitante = "";
        this.EscudoVisitante = null;
    }


    public String getEscudoLocal() {
        return EscudoLocal;
    }

    public String getLocal() {
        return Local;
    }

    public String getVisitante() {
        return Visitante;
    }

    public String getEscudoVisitante() {
        return EscudoVisitante;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "Local='" + Local + '\'' +
                ", Visitante='" + Visitante + '\'' +
                '}';
    }


}
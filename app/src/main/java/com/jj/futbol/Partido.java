package com.jj.futbol;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

/**
 * Created by Jesus on 21/04/2016.
 */
public class Partido {
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
}
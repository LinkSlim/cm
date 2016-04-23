package com.jj.futbol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListaPartidos extends AppCompatActivity {

    public Bitmap imagenHilo;
    private String imageHttpAddress = "http://thumb.resfu.com/img_data/escudos/medium/263.jpg?size=60x&amp;ext=png&amp;lossy=1&amp;1";
    public ArrayList<Partido> listaPartidos = new ArrayList<Partido>();
    public Partido2 partidoSeleccionado;
    public Intent output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partidos);



        //imagen = (ImageView)findViewById(R.id.imageEquipoLocal);
        //CargaImagenes nuevaTarea = new CargaImagenes();
        //nuevaTarea.execute(imageHttpAddress);

        listaPartidos.add(new Partido(BitmapFactory.decodeResource(getResources(), R.drawable.im_colibri), "Arsenal1", "Arsenal2", BitmapFactory.decodeResource(getResources(), R.drawable.im_buho)));
        listaPartidos.add(new Partido(BitmapFactory.decodeResource(getResources(), R.drawable.im_buho), "FC Barcelona", "FC Barcelona B", BitmapFactory.decodeResource(getResources(), R.drawable.im_colibri)));
        listaPartidos.add(new Partido(BitmapFactory.decodeResource(getResources(), R.drawable.im_colibri), "Antena3", "La Sexta", BitmapFactory.decodeResource(getResources(), R.drawable.im_cuervo)));


        ListView lista = (ListView) findViewById(R.id.listaPartidos);
        lista.setAdapter(new Adaptador_lista(this, R.layout.entrada, listaPartidos){
            @Override
            public void onEntrada(Object entrada, View view) {
                ImageView escudoLocal = (ImageView) view.findViewById(R.id.imageEquipoLocal);
                escudoLocal.setImageBitmap(((Partido) entrada).getEscudoLocal());

                TextView local = (TextView) view.findViewById(R.id.textViewEquipoLocal);
                local.setText(((Partido) entrada).getLocal());

                TextView visitante = (TextView) view.findViewById(R.id.textViewEquipoVisitante);
                visitante.setText(((Partido) entrada).getVisitante());

                ImageView escudoVisitante = (ImageView) view.findViewById(R.id.imageEquipoVisitante);
                escudoVisitante.setImageBitmap(((Partido) entrada).getEscudoVisitante());
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Partido elegido = (Partido) pariente.getItemAtPosition(posicion);
                partidoSeleccionado = new Partido2(elegido.getLocal(), elegido.getVisitante());
                //Toast.makeText(ListaPartidos.this, texto, Toast.LENGTH_LONG).show();
                //Log.i("PARTIDO", partidoSeleccionado.toString());
                output = new Intent(getApplicationContext(), Partido2.class);
                output.putExtra("p", partidoSeleccionado);
                setResult(RESULT_OK, output);
                finish();

            }
        });
    }


//    private class CargaImagenes extends AsyncTask<String, Void, Bitmap> {
//
//        ProgressDialog pDialog;
//
//        @Override
//        protected void onPreExecute() {
//            try{
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//
//            pDialog = new ProgressDialog(getApplicationContext());
//            pDialog.setMessage("Cargando Imagen");
//            pDialog.setCancelable(true);
//            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            pDialog.show();
//            }catch (Exception e){
//                Log.e("ERRRORRR", "ooooooooHHHHHH");
//            }
//
//
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            Log.i("doInBackground" , "Entra en doInBackground");
//            String url = params[0];
//            Bitmap imagen = descargarImagen(url);
//            return imagen;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//
//            imagenHilo = result;
//
//
//            pDialog.dismiss();
//        }
//
//        private Bitmap descargarImagen (String imageHttpAddress){
//            URL imageUrl = null;
//            Bitmap imagen = null;
//            try{
//                imageUrl = new URL(imageHttpAddress);
//                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
//                conn.connect();
//                imagen = BitmapFactory.decodeStream(conn.getInputStream());
//            }catch(IOException ex){
//                ex.printStackTrace();
//            }
//
//            return imagen;
//        }
//
//    }


}

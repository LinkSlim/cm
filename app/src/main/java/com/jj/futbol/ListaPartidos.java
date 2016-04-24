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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class ListaPartidos extends AppCompatActivity{

    public Bitmap imagenHilo;
    private String imageHttpAddress = "http://thumb.resfu.com/img_data/escudos/medium/263.jpg?size=60x&amp;ext=png&amp;lossy=1&amp;1";
    public ArrayList<Partido> listaPartidos = new ArrayList<Partido>();
    public Partido2 partidoSeleccionado;
    public Intent output;
    public String js;
    public String web = "http://www.resultados-futbol.com/scripts/api/api.php?key=aac9f27d384e2a552775d8ce3a4698d8&format=json&tz=Europe/Madrid&lang=es&rm=1&req=tv_channel_matches&date=2016-04-16&init=0&filter=Liga%20BBVA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partidos);
        setTitle(getResources().getText(R.string.title_lista_partidos));  //Cambio el titulo de la pantalla


        //new ReadWeatherJSONFeedTask().execute(web);


        CargaImagenes nuevaTarea = new CargaImagenes();
        nuevaTarea.execute(imageHttpAddress);

        /*listaPartidos.add(new Partido(BitmapFactory.decodeResource(getResources(), R.drawable.im_colibri), "Arsenal1", "Arsenal2", BitmapFactory.decodeResource(getResources(), R.drawable.im_buho)));
        listaPartidos.add(new Partido(BitmapFactory.decodeResource(getResources(), R.drawable.im_buho), "FC Barcelona", "FC Barcelona B", BitmapFactory.decodeResource(getResources(), R.drawable.im_colibri)));
        listaPartidos.add(new Partido(BitmapFactory.decodeResource(getResources(), R.drawable.im_colibri), "Antena3", "La Sexta", BitmapFactory.decodeResource(getResources(), R.drawable.im_cuervo)));*/



    }





    public String clienteHttp(String dir_web) throws IOException{
        //String dir_web = " ";
        String body = " ";
        try {
            URL url = new URL(dir_web);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String codigoRespuesta = Integer.toString(urlConnection.getResponseCode());
            if(codigoRespuesta.equals("200")){//Vemos si es 200 OK y leemos el cuerpo del mensaje.
                        InputStream in = urlConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                if(r != null)
                    r.close();
                in.close();
                body = total.toString();
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            body = e.toString(); //Error URL incorrecta
        } catch (SocketTimeoutException e){
            body = e.toString(); //Error: Finalizado el timeout esperando la respuesta del servidor.
        } catch (Exception e) {
            body = e.toString();//Error diferente a los anteriores.
        }
        return body;
    }




    /*private class ReadWeatherJSONFeedTask extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        protected void onPreExecute() {
            try{
                // TODO Auto-generated method stub
                super.onPreExecute();

                pDialog = new ProgressDialog(getApplicationContext());
                pDialog.setMessage("Cargando Imagen");
                pDialog.setCancelable(true);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }catch (Exception e){
                Log.e("ERRRORRR", "ooooooooHHHHHH");
            }


        }

        protected String doInBackground(String... urls) {
            try {
                return clienteHttp(urls[0]);
            }catch (Exception e){
                Log.i("JESUS",urls[0]);
                return "ERROR";
            }

        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject tv_matches = new JSONObject(jsonObject.getString("tv_matches"));
                //Toast.makeText(getBaseContext(), tv_matches.getString("clouds") + " - " + tv_matches.getString("stationName"), Toast.LENGTH_SHORT).show();
                Log.i("JESUS", tv_matches.toString());
            } catch (Exception e) {
                Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
            }

        }
    }*/


    private class CargaImagenes extends AsyncTask<String, Bitmap, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                pDialog = new ProgressDialog(getApplicationContext());
                pDialog.setMessage("Cargando Imagen");
                pDialog.setCancelable(true);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }catch (Exception e){
                Log.e("ERRRORRR", "ooooooooHHHHHH");
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground" , "Entra en doInBackground");
            String url = params[0];
            Bitmap imagen = descargarImagen(url);
            return imagen;
        }


        private Bitmap descargarImagen (String imageHttpAddress){
            URL imageUrl = null;
            Bitmap imagen = null;
            try{
                imageUrl = new URL(imageHttpAddress);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                imagen = BitmapFactory.decodeStream(conn.getInputStream());
            }catch(IOException ex){
                ex.printStackTrace();
            }

            return imagen;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            listaPartidos.add(new Partido(result, "Sevilla", "Betis", result));
            listaPartidos.add(new Partido(result, "Madrid", "Barsa", result));
            listaPartidos.add(new Partido(result, "Barsa", "Getafe", result));
            ListView lista = (ListView) findViewById(R.id.listaPartidos);
            lista.setAdapter(new Adaptador_lista(getApplicationContext(), R.layout.entrada, listaPartidos){
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
                    Log.i("PARTIDO", partidoSeleccionado.toString());
                    output = new Intent(getApplicationContext(), Partido2.class);
                    output.putExtra("p", partidoSeleccionado);
                    setResult(RESULT_OK, output);
                    finish();

                }
            });



            pDialog.dismiss();



        }

    }


}

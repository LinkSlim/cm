package com.jj.futbol;

import android.app.Activity;
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

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ListaPartidos extends Activity{

    public Set<Partido> listaPartidos = new HashSet<Partido>();
    public Partido partidoSeleccionado;
    public Intent output;
    public String web = "http://www.resultados-futbol.com/scripts/api/api.php?key=aac9f27d384e2a552775d8ce3a4698d8&format=json&tz=Europe/Madrid&lang=es&rm=1&req=tv_channel_matches&init=0&filter=Liga%20BBVA";
    public Bitmap escudoLocal, escudoVisitante;
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partidos);
        setTitle(getResources().getText(R.string.title_lista_partidos));  //Cambio el titulo de la pantalla

        //Cojo los partido de la fecha de hoy

        DescargaJson descargaJson = new DescargaJson();
        descargaJson.execute(web);
    }


    private String getFecha(String formato){ //formato de la API "yyyy-MM-dd"
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat(formato);
        return format1.format(cal.getTime());
        // Output "YYYY-MM-DD"
    }

    private class DescargaJson extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = ProgressDialog.show(getApplicationContext(), "Procesando", "Espere unos segundos...", true, false);
            /*try{
                pDialog = new ProgressDialog(getApplicationContext());
                pDialog.setMessage("Cargando Imagen");
                pDialog.setCancelable(true);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }catch (Exception e){
                Log.e("ERRRORRR", "ooooooooHHHHHH");
            }*/
        }

        protected String doInBackground(String... urls) {
            try {
                return clienteHttp(urls[0]);
            }catch (Exception e){
                Log.i("JESUS",urls[0]);
                return "ERROR";
            }

        }


        private String clienteHttp(String dir_web) throws IOException{
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

        private Bitmap cogeEscudo(String nombreEquipo){
            Bitmap b = null;

            switch (nombreEquipo){
                case "Athletic":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.athletic);
                    break;
                case "Atlético":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.atletico);
                    break;
                case "Barcelona":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.barcelona);
                    break;
                case "Real Betis":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.betis);
                    break;
                case "Celta":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.celta);
                    break;
                case "Deportivo":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.deportivo);
                    break;
                case "Eibar":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.eibar);
                    break;
                case "Espanyol":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.espanyol);
                    break;
                case "Getafe":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.getafe);
                    break;
                case "Granada":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.granada);
                    break;
                case "Levante":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.levante);
                    break;
                case "Málaga":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.malaga);
                    break;
                case "Las Palmas":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.palmas);
                    break;
                case "Rayo Vallecano":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.rayo);
                    break;
                case "Real Madrid":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.realmadrid);
                    break;
                case "R. Sociedad":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.realsociedad);
                    break;
                case "Sevilla":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.sevilla);
                    break;
                case "Sporting":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.sporting);
                    break;
                case "Valencia":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.valencia);
                    break;
                case "Villarreal":
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.villareal);
                    break;
                default:
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.es);
                    break;
            }
            return b;
        }

        protected void onPostExecute(String result) {
            String canal = "", local = "", visitor = "", competition_name = "", local_shield = "", visitor_shield = "", date = "", hour = "", minute = "";

            //CargaImagenes descargaImagen = new CargaImagenes();
            JSONObject tv_match, match;
            JSONArray matches;
            Partido partido;
            int i = 0, j = 0;

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray tv_matches = jsonObject.getJSONArray("tv_matches");
                for(i = 0; i < tv_matches.length(); i++){
                    tv_match = tv_matches.getJSONObject(i);
                    canal = tv_match.getString("title");
                    matches = tv_match.getJSONArray("matches");
                    for(j = 0; j < matches.length(); j++){
                        match = matches.getJSONObject(j);
                        local = match.getString("local");
                        visitor = match.getString("visitor");
                        competition_name = match.getString("competition_name");
                        local_shield = match.getString("local_shield");
                        visitor_shield = match.getString("visitor_shield");
                        date = fechaAFormatoEspanol(match.getString("date"));
                        hour = match.getString("hour");
                        minute = match.getString("minute");

                        //Descargo los escudos
                        //CargaImagenes descargaImagen = new CargaImagenes();
                        //descargaImagen.execute(local_shield, visitor_shield);
                        partido = new Partido(cogeEscudo(local), local, visitor, cogeEscudo(visitor), date, hour+":"+minute);
                        if(!listaPartidos.contains(partido)){
                            listaPartidos.add(partido);
                        }

                    }

                }

                Log.i("JESUS", "ESTOY EN ONPOSTEXECUTE Y SE HA PARSEADO EL JSON");

                //pDialog.dismiss();

                ListView lista = (ListView) findViewById(R.id.listaPartidos);
                lista.setAdapter(new Adaptador_lista(getApplicationContext(), R.layout.entrada, new ArrayList<Partido>(listaPartidos)){
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

                        TextView fecha = (TextView) view.findViewById(R.id.textViewFecha);
                        fecha.setText(((Partido) entrada).getDia());

                        TextView hora = (TextView) view.findViewById(R.id.textViewHora);
                        hora.setText(((Partido) entrada).getHora());
                    }
                });

                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                        Partido elegido = (Partido) pariente.getItemAtPosition(posicion);
                        partidoSeleccionado = new Partido(elegido.getLocal(), elegido.getVisitante(), elegido.getDia(), elegido.getHora());
                        //Toast.makeText(ListaPartidos.this, texto, Toast.LENGTH_LONG).show();
                        Log.i("PARTIDO", partidoSeleccionado.toString());
                        output = new Intent(getApplicationContext(), Partido.class);
                        output.putExtra("p", partidoSeleccionado);
                        setResult(RESULT_OK, output);
                        finish();
                    }
                });

                //pDialog.dismiss();

            } catch (Exception e) {
                Log.d("JESUS", "HUBO UNA EXCEPCION");
            }

        }

        private String fechaAFormatoEspanol(String fecha){
            Date d = new Date(fecha);
            return new SimpleDateFormat("dd/MM/yyyy").format(d).toString();
        }
    }


    private class CargaImagenes extends AsyncTask<String, Void, ArrayList<Bitmap>> {

        @Override
        protected void onPreExecute() {
            /*super.onPreExecute();
            try{
                pDialog = new ProgressDialog(getApplicationContext());
                pDialog.setMessage("Cargando Imagen");
                pDialog.setCancelable(true);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }catch (Exception e){
                Log.e("ERRRORRR", "ooooooooHHHHHH");
            }*/
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(String... params) {
            Log.i("JESUS" , "Entra en doInBackground de CargaImagenes");
            Bitmap imagen1 = descargarImagen(params[0]);
            Bitmap imagen2 = descargarImagen(params[1]);
            ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
            bitmapArray.add(imagen1); // Add a bitmap
            bitmapArray.add(imagen2); // Add a bitmap
            return bitmapArray;
        }


        private Bitmap descargarImagen (String imageHttpAddress){
            URL imageUrl = null;
            Bitmap imagen = null;
            try{
                imageUrl = new URL(imageHttpAddress);
                //HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                //conn.connect();
                //imagen = BitmapFactory.decodeStream(conn.getInputStream());
                imagen = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }catch(IOException ex){
                ex.printStackTrace();
            }

            return imagen;
        }


        @Override
        protected void onPostExecute(ArrayList<Bitmap> result) {
            super.onPostExecute(result);

            escudoLocal = result.get(0);
            escudoVisitante = result.get(1);
            //pDialog.dismiss();
        }

    }


}

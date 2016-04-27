package com.jj.futbol;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.DoubleNode;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, View.OnClickListener, ChildEventListener, GoogleMap.InfoWindowAdapter {

    public static final int PLACE_PICKER_REQUEST = 1;
    public static final int LISTA_PARTIDOS_REQUEST = 2;
    private ArrayList<Partido> listaPartidosViejos = new ArrayList<Partido>();
    private ArrayList<Partido> listaPartidosNuevos = new ArrayList<Partido>();
    private ArrayList<LatLng> listaPosiciones = new ArrayList<LatLng>();
    private GoogleMap mapa;
    private Firebase myFirebaseRef;
    private ArrayList<Marker> listaMarcas = new ArrayList<Marker>();
    public Lugar lugar = new Lugar();
    /*Para personalizar las marcas*/
    private View popup = null;
    private LayoutInflater inflater = null;
    String str;

 /*   public MapsActivity(LayoutInflater inflater) {
        this.inflater=inflater;

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * Ejemplo de integracion con Firebase
         * En este caso se esta realizando una lectura de un dato alojado en Firebase
         **/
        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase("https://appjj.firebaseio.com/posiciones");
        myFirebaseRef.addChildEventListener(this);
        
        /*Agrego al boton un Listener para que el boton me lleve a la Activity de PlacePicker al ser pulsado (onClick)*/
        final Button botonPlacePicker = (Button) findViewById(R.id.AbrirPlacePicker);
        botonPlacePicker.setOnClickListener(this);

    }

    public Marker addMarca(GoogleMap map, LatLng coordenadas, String equipoLocal, String equipoVisitante, String sitio, String fecha, String hora, String icono){
        Log.i("AÑADIR", "Marca añadida");
        Bitmap b = cogeEscudo(icono);
        Marker marker = map.addMarker(new MarkerOptions().position(coordenadas).title(equipoLocal+"¡"+equipoVisitante).snippet(sitio+"¡"+fecha+"¡"+hora).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(b, 30, 30, false))));
        listaMarcas.add(marker);
        return marker;
    }

    public void sustituyeMarca(GoogleMap map, LatLng coordenadas, String equipoLocal, String equipoVisitante, String sitio, String fecha, String hora, String icono){
        Bitmap b = cogeEscudo(icono);
        Boolean existe = false;
        Marker marcaAEliminar = null;
        //return map.addMarker(new MarkerOptions().position(coordenadas).title(titulo).snippet(snippet).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(b, 30, 30, false))));
        for(Marker marca: listaMarcas){
            if(marca.getPosition().equals(coordenadas)){
                existe = true;
                marcaAEliminar = marca;
                borraMarca(marca);
                break;
            }
        }
        if(existe)
            listaMarcas.remove(marcaAEliminar);

        addMarca(map, coordenadas, equipoLocal, equipoVisitante, sitio, fecha, hora, icono);
        Log.i("JESUS", "Marca sustituida");
    }

    public void mueveCamara(GoogleMap map, LatLng coordenadas){
        map.moveCamera(CameraUpdateFactory.newLatLng(coordenadas));
        Log.i("JESUS", "Camara movida");
    }

    public void borraMarca(Marker marker){
        marker.remove();
        Log.i("BORRAR", "Marca borrada");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(this, R.string.common_google_play_services_network_error_text, Toast.LENGTH_LONG).show();
        }

        /*map.setMyLocationEnabled(true); //Activo el boton de Mi Ubicacion
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setOnMyLocationButtonClickListener(this);
        }*/

        // Muevo la camara a Sevilla
        LatLng sevilla = new LatLng(37.388986, -5.984540);
        mueveCamara(map, sevilla);
        map.setInfoWindowAdapter(this);
    }



    @Override
    public boolean onMyLocationButtonClick() {

        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //Si el GPS no está habilitado
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast toast = Toast.makeText(getBaseContext(), "Activa el GPS para ir a tu ubicación", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.AbrirPlacePicker:

                // BEGIN_INCLUDE(intent)
            /* Use the PlacePicker Builder to construct an Intent.
            Note: This sample demonstrates a basic use case.
            The PlacePicker Builder supports additional properties such as search bounds.
             */
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(this);
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                    // Hide the pick option in the UI to prevent users from starting the picker
                    // multiple times.
                    //showPickAction(false);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
// END_INCLUDE(intent)



            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                //String toastMsg = String.format("Place: %s, ID: %s, Direccion: %s", place.getName(), place.getId(), place.getAddress());
                lugar = new Lugar(place.getId(), place.getAddress().toString(), place.getLatLng(), place.getName().toString(), new Partido());
                //Toast.makeText(this, lugar.toString(), Toast.LENGTH_LONG).show();
                Log.i("LUGAR", lugar.toString());
                Intent i = new Intent(this, ListaPartidos.class );
                startActivityForResult(i, LISTA_PARTIDOS_REQUEST);
            }
        }

        if (requestCode == LISTA_PARTIDOS_REQUEST) {
            if (resultCode == RESULT_OK) {

                Partido partido = (Partido) data.getSerializableExtra("p");
                //Toast.makeText(this, partido.toString(), Toast.LENGTH_LONG).show();
                lugar.setPartido(partido);
                Log.i("LUGAR", lugar.toString());
                myFirebaseRef.child(lugar.getId()).setValue(lugar.getNombre() +"¡"+ lugar.getDireccion() +"¡"+ lugar.getCoordenadas().latitude +"¡"+ lugar.getCoordenadas().longitude +"¡"+ lugar.getPartido().getLocal() +"¡"+ lugar.getPartido().getVisitante() +"¡"+ lugar.getPartido().getDia() +"¡"+ lugar.getPartido().getHora());
                mueveCamara(mapa, lugar.getCoordenadas());
                Toast.makeText(this, "Partido añadido correctamente", Toast.LENGTH_LONG).show();
            }
        }
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

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        //String nombre, direccion, lalitud, longitud, local, visitante, dia, hora;
        String[] atributos;
        Double lati = 0.0, longi = 0.0;
        LatLng ll;

        if (s == null) //'s' es el nombre de la key del elemento que se añadio en la llamada anterior
            s = "S";

        String chorizo = (String) dataSnapshot.getValue();   //Saco el valor del nodo actual
        atributos = chorizo.split("¡"); //Separo la cadena en dos (son las coordenadas de una posicion)
        lati = Double.parseDouble(atributos[2].trim());
        longi = Double.parseDouble(atributos[3].trim());
        ll = new LatLng(lati,longi);
        addMarca(mapa, ll, atributos[4], atributos[5], atributos[0], atributos[6], atributos[7]+"h", atributos[4]);   //Añado una marca al mapa con la posicion creada
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //String nombre, direccion, lalitud, longitud, local, visitante;
        String[] atributos;
        Double lati = 0.0, longi = 0.0;
        LatLng ll;

        if (s == null) //'s' es el nombre de la key del elemento que se añadio en la llamada anterior
            s = "S";

        String chorizo = (String) dataSnapshot.getValue();   //Saco el valor del nodo actual
        atributos = chorizo.split("¡"); //Separo la cadena en dos (son las coordenadas de una posicion)
        lati = Double.parseDouble(atributos[2].trim());
        longi = Double.parseDouble(atributos[3].trim());
        ll = new LatLng(lati,longi);
        sustituyeMarca(mapa, ll, atributos[4], atributos[5], atributos[0], atributos[6], atributos[7]+"h", atributos[4]);   //Añado una marca al mapa con la posicion creada
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    /*InfoWindow(LayoutInflater inflater, String s) {

        this.inflater = inflater;

        str = s;
    }*/

    @Override
    public View getInfoContents(Marker marker) {


        if (popup == null) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popup = inflater.inflate(R.layout.entrada, null);
        }

        ImageView escudoLocal = (ImageView) popup.findViewById(R.id.imageEquipoLocal);
        escudoLocal.setImageBitmap(cogeEscudo(marker.getTitle().split("¡")[0].trim()));

        TextView local = (TextView) popup.findViewById(R.id.textViewEquipoLocal);
        local.setText(marker.getTitle().split("¡")[0].trim());

        TextView visitante = (TextView) popup.findViewById(R.id.textViewEquipoVisitante);
        visitante.setText(marker.getTitle().split("¡")[1].trim());

        ImageView escudoVisitante = (ImageView) popup.findViewById(R.id.imageEquipoVisitante);
        escudoVisitante.setImageBitmap(cogeEscudo(marker.getTitle().split("¡")[1].trim()));

        TextView fecha = (TextView) popup.findViewById(R.id.textViewFecha);
        fecha.setText(marker.getSnippet().split("¡")[1].trim());

        TextView hora = (TextView) popup.findViewById(R.id.textViewHora);
        hora.setText(marker.getSnippet().split("¡")[2].trim());

        return popup;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}





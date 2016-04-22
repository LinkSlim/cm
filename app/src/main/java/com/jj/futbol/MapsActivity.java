package com.jj.futbol;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, View.OnClickListener {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int LISTA_PARTIDOS_REQUEST = 2;
    private ArrayList<Partido> listaPartidosViejos = new ArrayList<Partido>();
    private ArrayList<Partido> listaPartidosNuevos = new ArrayList<Partido>();
    private ArrayList<LatLng> listaPosiciones = new ArrayList<LatLng>();
    private GoogleMap mapa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * Ejemplo de integracion con Firebase
         * En este caso se esta realizando una lectura de un dato alojado en Firebase
         **/
        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://appjj.firebaseio.com/posiciones");


        myFirebaseRef.addChildEventListener(new ChildEventListener() {

            //onChildAdded() se llama para una vez para cada hijo existente de forma secuencial en el DataSnapshot al abrir la aplicacion por primera vez
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String x, y;
                String[] latylang;
                Double latitud = 0.0, longitud = 0.0;
                LatLng ll;

                if (s == null)
                    s = "S";

                String cadena = (String) dataSnapshot.getValue();
                latylang = cadena.split(",");
                x = latylang[0].trim();
                y = latylang[1].trim();
                latitud = Double.parseDouble(latylang[0]);
                longitud = Double.parseDouble(latylang[1]);
                ll = new LatLng(latitud,longitud);
                listaPosiciones.add(ll);
                addMarca(mapa, ll, latylang[0]+", "+latylang[1]);

                Log.i("POS X", s);
                Log.i("POS Y", s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });


        //myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");
//        myFirebaseRef.child("message").child("otro2").addValueEventListener(new ValueEventListener() { //Agrego un listener al dato llamado "message"
//            @Override
//            public void onDataChange(DataSnapshot snapshot) { //Cuando el valor del dato "message" cambia, se ejecuta este codigo
//                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
//                Toast toast = Toast.makeText(getBaseContext(), snapshot.getValue().toString(), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                toast.show();
//            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {
//            }
//        });

        /*Agrego al boton un Listener para que el boton me lleve a la Activity de PlacePicker al ser pulsado (onClick)*/
        final Button botonPlacePicker = (Button) findViewById(R.id.AbrirPlacePicker);
        final Button botonListaPartidos = (Button) findViewById(R.id.AbrirListaPartidos);
        botonPlacePicker.setOnClickListener(this);
        botonListaPartidos.setOnClickListener(this);


    }
    public Marker addMarca(GoogleMap map, LatLng coordenadas, String titulo){
        Log.i("AÑADIR", "Marca añadida");
        return map.addMarker(new MarkerOptions().position(coordenadas).title(titulo));
    }

    public void mueveCamara(GoogleMap map, LatLng coordenadas){
        map.moveCamera(CameraUpdateFactory.newLatLng(coordenadas));
        Log.i("MOVER", "Camara movida");
    }

    public void borraMarca(Marker marker){
        marker.remove();
        Log.i("BORRAR", "Marca borrada");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;

        map.setMyLocationEnabled(true); //Activo el boton de Mi Ubicacion
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setOnMyLocationButtonClickListener(this);
        }


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        Marker marca = addMarca(map, sydney, "Marca en Sidney");
        mueveCamara(map, sydney);
        //borraMarca(marca);
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

// END_INCLUDE(intent)

                break;

            case R.id.AbrirListaPartidos:
                Intent i = new Intent(this, ListaPartidos.class );
                startActivityForResult(i, LISTA_PARTIDOS_REQUEST);

            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s, ID: %s, Direccion: %s", place.getName(), place.getId(), place.getAddress());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == LISTA_PARTIDOS_REQUEST) {
            if (resultCode == RESULT_OK) {
                //Hacer cosas
            }
        }
    }

}





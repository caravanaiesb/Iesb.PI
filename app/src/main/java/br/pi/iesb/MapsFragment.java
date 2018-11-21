package br.pi.iesb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener {

    private static final String TAG = "MapsFragment";
    private GoogleMap mMap;
    private LocationManager locationManager;
    private FirebaseDatabase firebaseDatabase,firebaseDatabaseCarona;
    private DatabaseReference databaseReference,databaseReferenceCarona;
    private List<Evento> eventosLista = new ArrayList<>();
    private BitmapDescriptor icon;
    private List<Usuario> usuartioLista = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mMap = googleMap;
            mMap.setOnMapClickListener(this);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }else{
                mMap.setMyLocationEnabled(true);
            }
            mMap.setMyLocationEnabled(true);
            try {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions
                                .loadRawResourceStyle(getContext(), R.raw.mapstyle));
                if (!success) {
                    Log.e("MapsFragment", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapsFragment", "Can't find style. Error: ", e);
            }
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation==null){
                Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                LatLng userLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                LatLng latLng = new LatLng(userLocation.latitude, userLocation.longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
                }else{
            LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            MarkerOptions marker = new MarkerOptions();
            MarkerOptions markEventos = new MarkerOptions();
            LatLng latLng = new LatLng(userLocation.latitude, userLocation.longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cameraUpdate);

            // Recuperar EVENTOS e plotar localizacoes no map
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Eventos");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        eventosLista.clear();
                        for(DataSnapshot snap : dataSnapshot.getChildren()){
                            Evento a =snap.getValue(Evento.class);
                            Log.d("Eventos",a.toString());
                            eventosLista.add(a);
                        }
                        if(eventosLista==null){

                        }
                        else
                        for(int j=0;j<eventosLista.size();j++){
                            Evento model = eventosLista.get(j);
                            LatLng eventolocation = new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()));
                            MarkerOptions marker = new MarkerOptions();
                            marker.position(eventolocation);
                            marker.title(model.getTxtNomeEvento());
                            mMap.addMarker(marker);

                            firebaseDatabaseCarona = FirebaseDatabase.getInstance();
                            databaseReferenceCarona = firebaseDatabaseCarona.getReference("Eventos/"+model.getTxtNomeEvento()+"/Motoristas Disponiveis/");
                            databaseReferenceCarona.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    usuartioLista.clear();
                                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                                        Usuario c =snap.getValue(Usuario.class);
                                        Log.d("Carona",c.toString());
                                        usuartioLista.add(c);
                                    }
                                    for(int j=0;j<usuartioLista.size();j++){
                                        Usuario model = usuartioLista.get(j);
                                        LatLng pontodepartida = new LatLng(Double.parseDouble(model.getPartidaLatitude()), Double.parseDouble(model.getPartidaLongitude()));
                                        MarkerOptions marker = new MarkerOptions();
                                        marker.position(pontodepartida);
                                        marker.title(model.getEvento());
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                                        marker.snippet(model.getNomeUsuario()).icon(icon);
                                        mMap.addMarker(marker);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //RECUPERA PONTOS DE PARTIDA DAS CARONAS

        }}
        catch (SecurityException ex)
        {
            Log.e(TAG, "Error", ex);
        }

    }
    @Override
    public void onMapClick(LatLng latLng) {
        String chave = getActivity().getIntent().getStringExtra("Chave");
        if(chave.equals("C")) {
            Intent i = new Intent(getContext(), Event_Detail_Motorista.class);
            String lat = String.valueOf(latLng.latitude);
            String longi = String.valueOf(latLng.longitude);
            String posicao = getActivity().getIntent().getStringExtra("key");
            i.putExtra("latitude", lat);
            i.putExtra("long", longi);
            i.putExtra("key",posicao);
            startActivity(i);
        }
    if(chave.equals("E")) {
        Intent i = new Intent(getContext(), CadastroeventoActivity.class);
        String lat = String.valueOf(latLng.latitude);
        String longi = String.valueOf(latLng.longitude);
        i.putExtra("latitude", lat);
        i.putExtra("long", longi);
        String posicao = getActivity().getIntent().getStringExtra("key");
        i.putExtra("key", posicao);
        i.putExtra("Chave","M");
        startActivity(i);


    }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions marker = new MarkerOptions();
        marker.position(myLocation);
        marker.title("Aqui O");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}

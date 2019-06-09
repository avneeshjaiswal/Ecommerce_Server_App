package com.example.avneeshjaiswal.ecommerceserver.Activity;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.avneeshjaiswal.ecommerceserver.Common.Common;
import com.example.avneeshjaiswal.ecommerceserver.Common.DirectionJsonParser;
import com.example.avneeshjaiswal.ecommerceserver.R;
import com.example.avneeshjaiswal.ecommerceserver.Remote.IGeoCoordinates;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingOrder extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener
    {

    private GoogleMap mMap;
    private final static int PLAY_SERVICE_RESOLUTION_REQUEST = 1000;
    private final static int LOCATION_PERMISSION_REQUEST = 1001;

    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private static int UPDATE_INTERVAL=1000;
    private static int FATEST_INTERVAL=5000;
    private static int DISPLCAMENT=10;

    private IGeoCoordinates services;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);

        services = Common.getGeoCodeService();

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestRuntimePermission();
        }else{
            if(checkPlayService()){
                buildGoogleApiClient();
                createLocationRequest();
            }
        }

        displayLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

        private void displayLocation() {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestRuntimePermission();
            }else{
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if(lastLocation != null){
                    double latitude = lastLocation.getLatitude();
                    double longitude = lastLocation.getLongitude();

                    //now adding marker in location and move the camera
                    LatLng yourLocation = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                    //after adding marker to location, add marker for this order and draw route
                    drawRoute(yourLocation,Common.currentRequest.getAddress());


                }else{
                    //Toast.makeText(this,"Couldn't get the location",Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG","Couldn't get the location");
                }
            }
        }

        private void drawRoute(final LatLng yourLocation, String address) {
            services.getGeoCode(address).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    try{
                        JSONObject jsonObject =  new JSONObject(response.body().toString());
                        String lat = ((JSONArray)jsonObject.get("results"))
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location")
                                .get("lat").toString();

                        String lng = ((JSONArray)jsonObject.get("results"))
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location")
                                .get("lng").toString();

                        LatLng orderLocation = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.box);
                        bitmap = Common.scaleBitmap(bitmap,70,70);

                        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .title("Order of "+Common.currentRequest.getPhone())
                                .position(orderLocation);
                        mMap.addMarker(markerOptions);

                        //draw route
                        services.getDirections(yourLocation.latitude+","+yourLocation.longitude,
                                orderLocation.latitude+","+orderLocation.longitude)
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                        new ParserTask().execute(response.body());
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                                    }
                                });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                }
            });
        }

        private void createLocationRequest() {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(UPDATE_INTERVAL);
            locationRequest.setFastestInterval(FATEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setSmallestDisplacement(DISPLCAMENT);
        }

        protected synchronized void buildGoogleApiClient() {
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

        }

        private boolean checkPlayService() {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if(resultCode != ConnectionResult.SUCCESS){
                if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                    GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RESOLUTION_REQUEST).show();
                }else{
                    Toast.makeText(TrackingOrder.this,"this device is not supported!",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return false;
            }
            return true;
        }

        private void requestRuntimePermission() {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,},LOCATION_PERMISSION_REQUEST
                    );


        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch(requestCode){
                case LOCATION_PERMISSION_REQUEST:
                    if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        if(checkPlayService()){
                            buildGoogleApiClient();
                            createLocationRequest();

                            displayLocation();
                        }
                    }
                    break;
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            displayLocation();
            startLocationUpdates();
        }

        private void startLocationUpdates() {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }

        @Override
        public void onConnectionSuspended(int i) {
            googleApiClient.connect();

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;
            displayLocation();
        }

        @Override
        protected void onResume() {
            super.onResume();
            checkPlayService();
        }

        @Override
        protected void onStart() {
            super.onStart();
            if(googleApiClient != null){
                googleApiClient.connect();
            }
        }


        private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>> {
            ProgressDialog dialog = new ProgressDialog(TrackingOrder.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Please Wait....");
                dialog.show();
            }

            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
                JSONObject jsonObject;
                List<List<HashMap<String,String>>> routes = null;
                try{
                    jsonObject = new JSONObject(strings[0]);
                  DirectionJsonParser parser =new DirectionJsonParser();

                  routes = parser.parse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> lists) {


                dialog.dismiss();
                ArrayList<LatLng> points = new ArrayList<>();
                PolylineOptions polylineOptions = null;

                for(int i=0;i<lists.size();i++){
                    polylineOptions = new PolylineOptions();

                    List<HashMap<String,String>> path = lists.get(i);

                    for(int j=0;j<path.size();j++){
                        HashMap<String,String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat,lng);

                        points.add(position);
                    }

                    polylineOptions.addAll(points);
                    polylineOptions.width(12);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);

                }

                //to avoid crash
                if(points.size()!=0)
                {mMap.addPolyline(polylineOptions);}

            }
        }
    }

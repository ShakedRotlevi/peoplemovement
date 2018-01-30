package com.example.shakedrotlevi.peoplemovementapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.graphics.Color;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.firebase.database.GenericTypeIndicator;
import com.google.maps.android.PolyUtil;




import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;




//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
public class MapsActivity extends Activity implements OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest mLocationRequest;
    private GoogleMap map;
    Marker marker = null;
    ArrayList markerPoints= new ArrayList();
    LatLng currentLatLng;
    LatLng dest;
    int lineOptionsSize = 0;
    double pointLat, pointLon;
    ArrayList<LatLng> avoid = new ArrayList<LatLng>();
    Polyline polyline = null;// = new Polyline();

   // PolylineOptions lineOptions = new PolylineOptions();

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //  startLocationUpdates();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_map:
                    Log.d("myTag", "Clicked Map");
                    return true;
                case R.id.navigation_create:
                    startActivity(new Intent(MapsActivity.this, CreateGroupActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onMapReady(GoogleMap mapReady) {
        startLocationUpdates( mapReady);

    }

    public void setUpMap(){

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates(final GoogleMap mapReady) {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.d(" no permission", " no permissions");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }


        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation(),mapReady);
                    }
                },
                Looper.myLooper());
    }
    public void onLocationChanged(Location location, GoogleMap mapReady) {
        // New location has now been determined
        Log.d("myTag", "LOCATION CHANGED");
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d("long and lat", msg);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        Map<String, LocationObject> locations = new HashMap<>();
        LocationObject location1;// = new LocationObject(38.8977,-37.0365);

        double lat=38.900701;   //min y (max y is 38.900765)
        double lon = -77.049541;    //max x (min x is -77.049379)
        /* crowd in whole foods area
        difference: .000365 in x lon
        difference: .000023 in y lat
        */
        for(int i =0;i<25;i++){
            lat = lat + 0.00000023;
            lon = lon + 0.00000356;
            location1= new LocationObject(lat,lon);
            locations.put("location"+ i, location1);
        }
        lon = -77.048865;
        lat = 38.900342;
        for(int i =25;i<50;i++){
            lat = lat + 0.00000023;
            lon = lon + 0.00000356;
            location1= new LocationObject(lat,lon);
            locations.put("location"+ i, location1);
        }
        lat = 38.902655;
        lon = -77.048940;
        for(int i =50;i<75;i++){
            lat = lat + 0.00000023;
            lon = lon + 0.00000356;
            location1= new LocationObject(lat,lon);
            locations.put("location"+ i, location1);
        }

        //  DatabaseReference myRef = database.getReference("actual location");
        DatabaseReference refMap = database.getReference("locations map");
        DatabaseReference clusterArray = database.getReference("clusters").child("array");

        // myRef.setValue("Lat:" + location.getLatitude() + ", Lon: "+ location.getLongitude());
        refMap.setValue(locations);



        refMap.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
               /* String loc = dataSnapshot.getKey();
                String welcome = dataSnapshot.getValue(LocationObject.class).getWelcome();
                //String welcome = dataSnapshot.
                Log.d("THIS IS WHAT WELCOME IS", "Welcome is " + welcome);
                Toast toast= Toast.makeText(MapsActivity.this, welcome, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                View view = toast.getView();
                view.setBackgroundColor(Color.BLUE);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView welcomeText = (TextView) toastLayout.getChildAt(0);
                welcomeText.setTextSize(30);
                toast.show();*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        clusterArray.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //Double lat = dataSnapshot.getValue(Double.class);
              /*  Iterable <Double> cluster = new Iterable<Double>() {
                    @NonNull
                    @Override
                    public Iterator<Double> iterator() {
                        return null;
                    }
                }*/
               // ArrayList<Double> arr = (ArrayList<Double>) dataSnapshot.getValue();
                //   Iterable cluster = dataSnapshot.getChildren();
                //  Iterator iter = cluster.iterator();
                //  Double lat = (Double) iter.next();
                // Double lon = (Double) iter.next();

               /* for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Double lat = postSnapshot.getValue(Double.class);
                    Log.e("Get Data", post.<YourMethod>());
                }*/


                GenericTypeIndicator<ArrayList<Double>> child = new GenericTypeIndicator<ArrayList<Double>>() {};
                ArrayList<Double> center = dataSnapshot.getValue(child);
                Double lat = (Double) center.get(0);
                Double lon = (Double)center.get(1);

              /*  ArrayList arr = (ArrayList) dataSnapshot.getValue(ArrayList.class);

                Double lat = (Double) arr.get(0);
                Double lon = (Double)arr.get(1);*/
                LatLng cluster = new LatLng(lat, lon);
                avoid.add(cluster);
                Log.d("THIS IS CLUSTER:", "LAT is " + lat + "LON IS "+ lon);
                Circle circle = map.addCircle(new CircleOptions()
                        .center(new LatLng(lat, lon))
                        .radius(20)
                        .strokeColor(android.R.color.black)
                        .fillColor(Color.argb(125,255,0,0)));
                sendDirectionRequest();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
               /* String loc = dataSnapshot.getKey();
                String welcome = dataSnapshot.getValue(LocationObject.class).getWelcome();
                //String welcome = dataSnapshot.
                Log.d("THIS IS WHAT WELCOME IS", "Welcome is " + welcome);
                Toast toast= Toast.makeText(MapsActivity.this, welcome, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                View view = toast.getView();
                view.setBackgroundColor(Color.BLUE);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView welcomeText = (TextView) toastLayout.getChildAt(0);
                welcomeText.setTextSize(30);
                toast.show();*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

       /* myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("NEW VAL ", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("NEW VAL", "Failed to read value.", error.toException());
            }
        });
*/
        if(marker!=null){
            marker.remove();
        }
        map = mapReady;
        map.getUiSettings().setZoomControlsEnabled(true);
        if(markerPoints.size()<1) {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            marker = mapReady.addMarker(new MarkerOptions().position(currentLatLng)
                    .title("Marker in current Location"));
            markerPoints.add(currentLatLng);
            dest = new LatLng(38.901118, -77.048847);
            markerPoints.add(dest);
            mapReady.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

            pointLat = dest.latitude;
            pointLon = dest.longitude;
            sendDirectionRequest();
        }

        int alpha = 127; // 50% transparent
        int value = 0;
        //Color myColour = new Color(255, value, value, alpha);
      /*  Circle circle = map.addCircle(new CircleOptions()
                .center(new LatLng(-33.87365, 151.20689))
                .radius(10000)
                .strokeColor(android.R.color.black)
                .fillColor(Color.argb(125,255,0,0)));*/

       /* Polygon polygon = map.addPolygon(new PolygonOptions()
                .add(new LatLng(38.899584, -77.048130), new LatLng(38.899584, -77.046697), new LatLng(38.898816, -77.046681), new LatLng(38.898346, -77.047299))
                .strokeColor(android.R.color.black)
                .fillColor(Color.argb(125,255,0,0)));*/

        // You can now create a LatLng Object for use with maps
        //double lat = location.getLatitude();
        //  double
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }
    private void sendDirectionRequest(){
        //double pointLat = dest.latitude;
        //double pointLon = dest.longitude;
        Log.d(" size of marker points ", String.valueOf(markerPoints.size()));
        Log.d(" first marker ", String.valueOf(markerPoints.get(0)));
        Log.d(" second marker ", String.valueOf(markerPoints.get(1)));
        if (markerPoints.size() >= 2) {
            LatLng origin = (LatLng) markerPoints.get(0);
            LatLng dest = (LatLng) markerPoints.get(1);
            Log.d("origin is ", String.valueOf(origin));
            Log.d("dest is ", String.valueOf(dest));
            // requestDirection(origin, dest);


            // Getting URL to the Google Directions API
            //  while(lineOptionsSize==0) {

            LatLng waypoint = new LatLng(pointLat,pointLon);
            String url = getDirectionsUrl(origin, dest, waypoint);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
         //   pointLat += .001065;
           // pointLon -=-0.0014;

            //  }
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest, LatLng waypoint){


        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        //waypoints
        String str_waypoints = "waypoints="+waypoint.latitude+","+waypoint.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+str_waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&alternatives=true&mode=walking";//"&waypoints=optimize:true|38.901315, -77.043397|38.901315, -77.041740";

        return url;
    }



    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Error downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            Log.d(" THE RESULT ", result);
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            Log.d("HERE ", "IN ON POST EXECUTE");
            if(polyline != null) {
                polyline.remove();
            }
            PolylineOptions lineOptions = null;
         //   PolylineOptions route = new PolylineOptions(); //final route
            MarkerOptions markerOptions = new MarkerOptions();

           // lineOptions.


            ////WANT THE LAT TO BE .001065 above
            lineOptions = new PolylineOptions();

            Log.d("Result size: ", String.valueOf(result.size()));
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                //lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                Log.d("Path size: ", String.valueOf(path.size()));

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
//                    Log.d("points size: ", String.valueOf(point.size()));

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    //GO BACK HERE!!!
                    //if(points.contains(position)){
                  //  Log.d(" already contains: ", "lat: "+String.valueOf(lat) +", lon: "+String.valueOf(lng));
                    if(twoTimes(points, position)){
                        Log.d(" in four times ", " four times");
                        points.subList(points.indexOf(position)+1, points.size()).clear();
                    }

                    // Log.d(" already contains: ", "lat: "+String.valueOf(lat) +", lon: "+String.valueOf(lng));
                    //points.remove(position);
                    //}
                    else{
                        points.add(position);

                    }

                }


                Log.d(" new points size ", String.valueOf(points.size()));
                // Adding all the points in the route to LineOptions
                //lineOptions.addAll(points);
                double tolerance = 10; // meters
                boolean isLocationOnPath=false;
                for (LatLng cluster : avoid){
                    Log.d(" first cluster ", "lat: "+String.valueOf(cluster.latitude) +", lon: "+String.valueOf(cluster.longitude));
                    isLocationOnPath = PolyUtil.isLocationOnPath(cluster, points, true, tolerance);
                    if(isLocationOnPath==true){
                        break;
                    }
                }
                //  boolean isLocationOnPath = PolyUtil.isLocationOnPath(avoid, points, true, tolerance);

                if(isLocationOnPath == false){
                    Log.d(" not in path ", "not in path");
                    // route = lineOptions;
                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(Color.GREEN);
                    break;
                }
                points.clear();

            }

            // Drawing polyline in the Google Map for the i-th route
            //lineOptionsSize=lineOptions.getPoints().size();
            Log.d(" line options size ", String.valueOf(lineOptions.getPoints().size()));
            polyline = map.addPolyline(lineOptions);
            if(lineOptions.getPoints().size()==0){
               // if()
                pointLat += .001065;
                pointLon -=-0.0014;
                //pointLat += .001079;
                //pointLon -=-0.0029;
                sendDirectionRequest();
            }
        }
    }

    public static boolean twoTimes(ArrayList<LatLng> list, LatLng position)
    {
        int numCount = 0;

        for (LatLng thisPosition : list) {
            if (thisPosition.equals(position)) numCount++;
        }

        return numCount >1;
    }
}


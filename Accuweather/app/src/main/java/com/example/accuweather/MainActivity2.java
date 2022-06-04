package com.example.accuweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationListenerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {
    private ImageView homeRL;
    private TextView cityNameTV, temperatureTV, conditionTV, btempTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private Button searchIV;
    private ImageView iconIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private static final int PERMISSION_CODE = 1;
    private String cityName;
    private mBatInfoReceiver myBatInfoReceiver;
    private boolean button_state = false;
    private LocationSettingsRequest.Builder builder;
    private final int Request_check_code = 8989;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBatInfoReceiver = new mBatInfoReceiver();
        this.registerReceiver(this.myBatInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ScheduleTemp.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("TAG")
                .build();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main2);

        GraphView graph = findViewById(R.id.idGraph);
        btempTV = findViewById(R.id.idTVBTemp);
        homeRL = findViewById(R.id.idRLHome);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemp);
        conditionTV = findViewById(R.id.idTVCondition);
        weatherRV = findViewById(R.id.idRVWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);  // <------------------------------------------------------------------RT error
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        turnOnGPS();


        LineGraphSeries<DataPoint> battery = new LineGraphSeries<>(new DataPoint[]
                {
                        new DataPoint(0, 30),
                        new DataPoint(1, 32),
                        new DataPoint(2, 34),
                        new DataPoint(3, 32),
                        new DataPoint(4, 31),
                        new DataPoint(5, 38),
                        new DataPoint(6, 36),
                        new DataPoint(7, 40),
                        new DataPoint(8, 35)
                });
        LineGraphSeries<DataPoint> local = new LineGraphSeries<>(new DataPoint[]
                {
                        new DataPoint(0, 31),
                        new DataPoint(1, 38),
                        new DataPoint(2, 36),
                        new DataPoint(3, 40),
                        new DataPoint(4, 35),
                        new DataPoint(5, 30),
                        new DataPoint(6, 32),
                        new DataPoint(7, 34),
                        new DataPoint(8, 32)
                });
        graph.addSeries(battery);
        graph.addSeries(local);
        local.setColor(Color.YELLOW);
        battery.setColor(Color.BLUE);




        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!button_state) {
                    searchIV.setBackground(getDrawable(R.drawable.button_click_pressed));
                    searchIV.setText(getString(R.string.end));
                    if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    }
                    button_state=true;
                }
                else{
                    createAlert();
                }
            }
        });
    }

    private void turnOnGPS(){
        com.google.android.gms.location.LocationRequest request1 = new com.google.android.gms.location.LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.QUALITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request1);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode())
                    {
                        case LocationSettingsStatusCodes
                                .RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity2.this,Request_check_code);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }catch (ClassCastException ex){

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:{
                            break;
                        }
                    }
                }
            }
        });
    }

    private void createAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity2.this,"Stopped",Toast.LENGTH_SHORT).show();
                searchIV.setBackground(getDrawable(R.drawable.button_click));
                searchIV.setText(getString(R.string.start));
                button_state=false;
                cityNameTV.setHint("City Name");
                cityNameTV.setText("");
                temperatureTV.setText(getString(R.string.temprature_main));
                btempTV.setText(getString(R.string.temprature_main));
                conditionTV.setText((getString(R.string.weather_info)));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                String city=null;
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity2.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0)
                            city = addresses.get(0).getLocality()+", "+addresses.get(0).getAdminArea();
                        if (city == null) {
                            Toast.makeText(MainActivity2.this, "Unable to find your location!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            cityNameTV.setText(city);
                            float temp = myBatInfoReceiver.get_temp();
                            String message = temp + Character.toString((char) 176);
                            Log.d("bt", message);
                            btempTV.setText(message);
//                          inserttempdata();
                            getWeatherInfo(city);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Please provide permissions",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/current.json?key=9d5edf8b10e249b4a2f193014221304&q="+cityName+"&aqi=no";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                homeRL.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(String.format("%s", temperature));
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    //weatherRVAdapter.notifyDataSetChanged();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity2.this, "Please enter valid city name.",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static String batteryTemperature(Context context)
    {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        float  temp   = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)) / 10;
        return String.valueOf(temp);
    }

//    private void inserttempdata() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_IN_CDATA, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                inCityResponse(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put(JSONField.city_name, et_cust_psswd1.getText().toString());
//                params.put(JSONField.city_temp, et_cust_phn.getText().toString());
//                params.put(JSONField.city_icon, et_cust_email.getText().toString());
//                params.put(JSONField.mob_temp=, et_cust_add.getText().toString());
//                params.put(JSONField.temp_time, et_cust_pin.getText().toString());
//                params.put(JSONField.temp_date, et_cust_pin.getText().toString());
//                Log.d("R", String.valueOf(params));
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
//        requestQueue.add(stringRequest);
//
//    }
//
//    private void inCityResponse(String response) {
//        Log.d("RESPONSE", response);
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(response);
//            String city = cityEdt.getText().toString();
//            if(city.isEmpty()){
//                Toast.makeText(MainActivity2.this,"Please Enter city name", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                cityNameTV.setText(cityName);
//                cityNameTV1.setText(cityName);
//                getWeatherInfo(city);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
}

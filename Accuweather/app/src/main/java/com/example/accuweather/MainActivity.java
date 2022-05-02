//package com.example.accuweather;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.BatteryManager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.textfield.TextInputEditText;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//public class MainActivity extends AppCompatActivity {
//    private ImageView homeRL;
//    private ProgressBar loadingPB;
//    private TextView cityNameTV, cityNameTV1, temperatureTV, conditionTV;
//    //private RecyclerView weatherRV;
//    private TextInputEditText cityEdt;
//    private ImageView iconIV,searchIV;
//    //private ArrayList<WeatherRVModal> weatherRVModalArrayList;
//
//    //private WeatherRVAdapter weatherRVAdapter;
////    private LocationManager locationManager;
////    private int PERMISSION_CODE = 1;
////    private String cityName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //       getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        setContentView(R.layout.activity_main);
//
//        GraphView graph = findViewById(R.id.idGraph);
//        homeRL = findViewById(R.id.idRLHome);
//        loadingPB = findViewById(R.id.idPBLoading);
//        cityNameTV = findViewById(R.id.idTVCityName);
//        cityNameTV1 = findViewById(R.id.idTVCityName1);
//        temperatureTV = findViewById(R.id.idTVTemp);
//        conditionTV = findViewById(R.id.idTVCondition);
//        //weatherRV=findViewById(R.id.idRVWeather);
//       // cityEdt = findViewById(R.id.idEdtCity);
//        iconIV = findViewById(R.id.idIVIcon);
//       // searchIV = findViewById(R.id.idIVSearch);
//        //weatherRVModalArrayList = new ArrayList<>();
//        //weatherRVAdapter = new WeatherRVAdapter(this,weatherRVModalArrayList);
//        //weatherRV.setAdapter(weatherRVAdapter);  // <------------------------------------------------------------------RT error
//        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
////        LineGraphSeries<DataPoint> battery = new LineGraphSeries<>(new DataPoint[]
////                {
////                        new DataPoint(0, 30),
////                        new DataPoint(1, 32),
////                        new DataPoint(2, 34),
////                        new DataPoint(3, 32),
////                        new DataPoint(4, 31),
////                        new DataPoint(5, 38),
////                        new DataPoint(6, 36),
////                        new DataPoint(7, 40),
////                        new DataPoint(8, 35)
////                });
////        LineGraphSeries<DataPoint> local = new LineGraphSeries<>(new DataPoint[]
////                {
////                        new DataPoint(0, 31),
////                        new DataPoint(1, 38),
////                        new DataPoint(2, 36),
////                        new DataPoint(3, 40),
////                        new DataPoint(4, 35),
////                        new DataPoint(5, 30),
////                        new DataPoint(6, 32),
////                        new DataPoint(7, 34),
////                        new DataPoint(8, 32)
////                });
////        graph.addSeries(battery);
////        graph.addSeries(local);
////        local.setColor(Color.YELLOW);
//
//
//
//
//
//
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
////        }
////        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
////        cityName = getCityName(location.getLongitude(),location.getLatitude());
////        getWeatherInfo(cityName);
//
////        searchIV.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                String city = cityEdt.getText().toString();
////                if(city.isEmpty()){
////                    Toast.makeText(MainActivity.this,"Please Enter city name", Toast.LENGTH_SHORT).show();
////                }
////                else{
////                    cityNameTV.setText(cityName);
////                    cityNameTV1.setText(cityName);
////                    //getWeatherInfo(city);
////                }
////            }
////        });
////
////    }
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        if(requestCode==PERMISSION_CODE){
////            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
////                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
////            }
////            else{
////                Toast.makeText(this,"Please provide permissions",Toast.LENGTH_SHORT).show();
////                finish();
////            }
////        }
////    }
//
////    private String getCityName(double longitude, double latitude){
////        String cityName = "Not Found";
////        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
////        try {
////            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
////
////            for(Address adr : addresses){
////                if(adr!=null){
////                    String city = adr.getLocality();
////                    if(city!=null && !city.equals("")){
////                        cityName=city;
////                    }
////                    else{
////                        Toast.makeText(this,"City not found!",Toast.LENGTH_SHORT).show();
////                    }
////                }
////            }
////        }
////        catch (IOException e){
////            e.printStackTrace();
////        }
////        return cityName;
////    }
////
////    private void getWeatherInfo(String cityName){
////        String url = "http://api.weatherapi.com/v1/current.json?key=9d5edf8b10e249b4a2f193014221304&q="+cityName+"&aqi=no";
////        cityNameTV.setText(cityName);
////        cityNameTV1.setText(cityName);
////        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
////        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
////            @Override
////            public void onResponse(JSONObject response) {
////                loadingPB.setVisibility(View.GONE);
////                homeRL.setVisibility(View.VISIBLE);
////                weatherRVModalArrayList.clear();
////
////                try {
////                    String temperature = response.getJSONObject("current").getString("temp_c");
////                    temperatureTV.setText(String.format("%s°c", temperature));
////                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
////                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
////                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
////                    conditionTV.setText(condition);
////                    //weatherRVAdapter.notifyDataSetChanged();
////                }
////                catch (JSONException e){
////                    e.printStackTrace();
////                }
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Toast.makeText(MainActivity.this, "Please enter valid city name.",Toast.LENGTH_SHORT).show();
////            }
////        });
////        requestQueue.add(jsonObjectRequest);
////    }
////
//    }
//}
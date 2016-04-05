package com.example.machine2.ne;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailPage extends Activity {

    TextView tvDesription;
    TextView tvTemparature;
    TextView tvPressure;
    TextView tvHumidity;
    TextView tvWind;
    TextView tvCity;

    ImageView imageView;

    RequestQueue queue;

    ProgressDialog progressDialog;
    Bundle bundle;

    String data;
    String url;
    String description;
    String icon;
    String base;
    String tempincelsius;
    String pressure;
    String humidity;
    String windspeed;
    String winddegree;
    String cityname;
    String countryname;

    JsonObjectRequest jsObjRequest;
    JSONArray jsonArray;
    JSONObject jsonObject;
    JSONObject object;
    JSONObject jsonObject1;
    JSONObject jsonObj;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        tvDesription = (TextView) findViewById(R.id.textView8);
        imageView = (ImageView) findViewById(R.id.showicon);
        tvTemparature = (TextView) findViewById(R.id.textView9);
        tvPressure = (TextView) findViewById(R.id.textView3);
        tvHumidity = (TextView) findViewById(R.id.textView4);
        tvWind = (TextView) findViewById(R.id.textView7);
        tvCity = (TextView) findViewById(R.id.textView5);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Loading Weather Details"); // print dialog while fetching data from the network
        progressDialog.show();

        queue = Volley.newRequestQueue(this);
        // Getting the data from previous activity and passing that data into url and displaying all the informations related to that particular data.
        bundle = getIntent().getExtras();
        data = bundle.getString("data");
        url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&APPID=45df4fca7d202600be0e657e2d0a9dcd";
        jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            // JSON response will be obtained in this method if there are no network issues
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                progressDialog.dismiss();
                try {
                    jsonArray = new JSONArray(response.getString("weather"));
                    jsonObject = jsonArray.getJSONObject(0);
                    description = jsonObject.getString("description");
                    tvDesription.setText(description);

                    icon = jsonObject.getString("icon");
                    base = "http://openweathermap.org/img/w/" + icon + ".png";
                    new DownloadImageTask(imageView).execute(base);

                    object = response.getJSONObject("main");
                    tempincelsius = object.getString("temp");
                    tvTemparature.setText(tempincelsius + " °C");

                    pressure = object.getString("pressure");
                    humidity = object.getString("humidity");

                    tvPressure.setText(pressure + " hPa");
                    tvHumidity.setText(humidity + "%");

                    jsonObject1 = new JSONObject(response.getString("wind"));
                    windspeed = jsonObject1.getString("speed");
                    winddegree = jsonObject1.getString("deg");
                    tvWind.setText(windspeed + " mps " + winddegree + " °");

                    cityname = response.getString("name");
                    jsonObj = new JSONObject(response.getString("sys"));
                    countryname = jsonObj.getString("country");
                    tvCity.setText(cityname + "," + countryname);

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            //If there is any error in network connection ,then this method will be executed
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), " Network Error ", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsObjRequest);


    }

}
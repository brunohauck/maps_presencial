package com.mentorandroid.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Context ctx;
    private List<MapItem> mapItemList = new ArrayList<MapItem>();
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.editText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResults();
            }
        });
    }
    private void getResults() {
        if (Util.isNetworkAvailable(ctx)) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading Results");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(ctx);
            String  input = editText.getText().toString();
            input = input.replace(" ", "+");
            String url ="http://maps.googleapis.com/maps/api/geocode/json?address="+input+"&sensor=false";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mapItemList = null;
                            parseResult(response.toString());
                            Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
                            intent.putExtra("object", (Serializable) mapItemList);
                            startActivity(intent);
                            pDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("DEBUG", "ERROR");

                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            Log.d("DEBUG", "NETWORK UNVAILABLE");
        }
    }
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            String status = response.optString("status");
            if(status.equals("ZERO_RESULTS")){
                //noResults.setVisibility(View.VISIBLE);
            }
            if (null == mapItemList) {
                mapItemList = new ArrayList<MapItem>();
            }
            int i;
            for (i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                if(status.equals("OK")){
                    //noResults.setVisibility(View.GONE);
                    MapItem mapItemRow = new MapItem();
                    mapItemRow.setFlag_display_all(false);
                    mapItemRow.setFormatted_address(post.optString("formatted_address"));
                    JSONObject geometryPost = post.optJSONObject("geometry");
                    JSONObject geometryLocation = geometryPost.optJSONObject("location");
                    Geometry geometry = new Geometry();
                    geometry.setLat(geometryLocation.optDouble("lat"));
                    geometry.setLng(geometryLocation.optDouble("lng"));
                    mapItemRow.setGeometry(geometry);
                    mapItemList.add(mapItemRow);
                }
            }
            //Adding a fake row to show all locations in the map
            if(i>1) {
                MapItem fakeRow = new MapItem();
                fakeRow.setFlag_display_all(true);
                fakeRow.setFormatted_address("Display All on Map");
                mapItemList.add(fakeRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /*
    * package com.avenuecode.androidchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avenuecode.androidchallenge.Adapter.LocationAdapter;
import com.avenuecode.androidchallenge.models.Geometry;
import com.avenuecode.androidchallenge.models.MapItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Context ctx;
    private RecyclerView mRecyclerView;
    private LocationAdapter adapter;
    private EditText editText;
    private TextView noResults;
    private ImageView logo;
    private List<MapItem> mapItemList = new ArrayList<MapItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_locations);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        editText = (EditText) findViewById(R.id.editText);
        logo = (ImageView) findViewById(R.id.imageViewLogo);
        noResults = (TextView) findViewById(R.id.textViewNoResults);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.setVisibility(View.GONE);
                getResults();
            }
        });
    }

    private void getResults() {
        if (Util.isNetworkAvailable(ctx)) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading Results");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(ctx);
            String  input = editText.getText().toString();
            input = input.replace(" ", "+");
            String url ="http://maps.googleapis.com/maps/api/geocode/json?address="+input+"&sensor=false";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mapItemList = null;
                            parseResult(response.toString());
                            adapter = new LocationAdapter(SearchActivity.this, mapItemList);
                            mRecyclerView.setAdapter(adapter);
                            pDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("DEBUG", "ERROR");

                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            Log.d("DEBUG", "NETWORK UNVAILABLE");
        }
    }
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            String status = response.optString("status");
            if(status.equals("ZERO_RESULTS")){
                noResults.setVisibility(View.VISIBLE);
            }
            if (null == mapItemList) {
                mapItemList = new ArrayList<MapItem>();
            }
            int i;
            for (i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                if(status.equals("OK")){
                    noResults.setVisibility(View.GONE);
                    MapItem mapItemRow = new MapItem();
                    mapItemRow.setFlag_display_all(false);
                    mapItemRow.setFormatted_address(post.optString("formatted_address"));
                    JSONObject geometryPost = post.optJSONObject("geometry");
                    JSONObject geometryLocation = geometryPost.optJSONObject("location");
                    Geometry geometry = new Geometry();
                    geometry.setLat(geometryLocation.optDouble("lat"));
                    geometry.setLng(geometryLocation.optDouble("lng"));
                    mapItemRow.setGeometry(geometry);
                        mapItemList.add(mapItemRow);
                }
            }
            //Adding a fake row to show all locations in the map
            if(i>1) {
                MapItem fakeRow = new MapItem();
                fakeRow.setFlag_display_all(true);
                fakeRow.setFormatted_address("Display All on Map");
                mapItemList.add(fakeRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

    *
    * */

}

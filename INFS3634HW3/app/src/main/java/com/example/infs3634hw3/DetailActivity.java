package com.example.infs3634hw3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String imageUrl;
    private int favouriteStatus = 0;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent1 = getIntent();
        id = intent1.getStringExtra("id");

        //intent to determine whether cat is already favourited
        favouriteStatus = intent1.getIntExtra("favouriteStatus", 0);

        //initiating the initial constraint layout
        final ConstraintLayout activity_detail = findViewById(R.id.activity_detail);

        //fetching information to population detail page
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.thecatapi.com/v1/images/search?breed_id=" + id;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //creating java classes to match API
                Gson gson = new Gson();
                CatDetail[] catDetailArray = gson.fromJson(response,CatDetail[].class);
                ArrayList<CatDetail> catDetailArrayList = new ArrayList<CatDetail>(Arrays.asList(catDetailArray));
                CatDetail catDetailObject = catDetailArrayList.get(0);
                Cat[] catArrayObject = catDetailObject.getBreeds();
                ArrayList<Cat> catArrayListObject = new ArrayList<Cat>(Arrays.asList(catArrayObject));
                Cat catObject = catArrayListObject.get(0);
                CatWeight catWeightArrayObject = catObject.getWeight();

                //setting information on screen to match the created java classes
                TextView catName1 = activity_detail.findViewById(R.id.catName1);
                catName1.setText(catObject.getName());
                TextView catDescription = activity_detail.findViewById(R.id.catDescription);
                catDescription.setText(catObject.getDescription());
                ImageView catImage = activity_detail.findViewById(R.id.catImage);
                Glide.with(getApplicationContext()).load(catDetailObject.getUrl()).into(catImage);
                TextView catOrigin = activity_detail.findViewById(R.id.catOrigin);
                catOrigin.setText(catObject.getOrigin());
                TextView catWeight = activity_detail.findViewById(R.id.catWeight);
                catWeight.setText(catWeightArrayObject.getMetric()  + " kg");
                TextView catTemperament = activity_detail.findViewById(R.id.catTemperament);
                catTemperament.setText(catObject.getTemperament());
                TextView catLifeSpan = activity_detail.findViewById(R.id.catLifeSpan);
                catLifeSpan.setText(catObject.getLife_span() + " years");
                TextView catWikipediaURL = activity_detail.findViewById(R.id.catWikipediaURL);
                catWikipediaURL.setText(catObject.getWikipedia_url());
                TextView catDogFriendlinessLevel = activity_detail.findViewById(R.id.catDogFriendlinessLevel);
                catDogFriendlinessLevel.setText((Integer.toString(catObject.getDog_friendly()) + " / 5"));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener);
        requestQueue.add(stringRequest);

        //initiating the favourite button
        final ImageButton favouritesButton = findViewById(R.id.favouritesButton);

        //determine the initial state for the favourite button
        if(favouriteStatus == 0){
            favouritesButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else if (favouriteStatus == 1) {
            favouritesButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        //handling when a cat is favourited
        favouritesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(favouriteStatus == 0){
                    favouritesButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favouriteStatus = 1;
                } else if (favouriteStatus == 1){
                    favouritesButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    favouriteStatus = 0;
                }
            }
        });

        //handling the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                if(favouriteStatus == 1){
                    intent1.putExtra("id", id);
                } else if (favouriteStatus == 0){
                    intent1.putExtra("removeId", id);
                }
                startActivity(intent1);
            }
        });
    }
}
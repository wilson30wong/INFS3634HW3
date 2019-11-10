package com.example.infs3634hw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private RequestQueue requestQueue;
    public static ArrayList<Cat> favouritesCatArrayList = new ArrayList<Cat>();
    public static ArrayList<String> favouritesCatIdArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiating the fragment manager and the initial searchFragment
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_slot, new SearchFragment());
        fragmentTransaction.commit();

        Intent intent1 = getIntent();
        String id = intent1.getStringExtra("id");
        String removeId = intent1.getStringExtra("removeId");

        //handles whether to add a cat to the favourites list when returning from a detail page
        for(int i = 0; i < MainActivity.favouritesCatIdArrayList.size(); i++){
            if(removeId != null){
                if(removeId.equals(MainActivity.favouritesCatIdArrayList.get(i))){
                    favouritesCatIdArrayList.remove(removeId);
                    favouritesCatArrayList.remove(i);
                    break;
                }
            } else{
                if(id.equals(MainActivity.favouritesCatIdArrayList.get(i))){
                    id = null;
                    break;
                }
            }
        }
        if(id != null){
            favouritesCatIdArrayList.add(id);
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = "https://api.thecatapi.com/v1/images/search?breed_id=" + id;
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    CatDetail[] catDetailArray = gson.fromJson(response,CatDetail[].class);
                    ArrayList<CatDetail> catDetailArrayList = new ArrayList<CatDetail>(Arrays.asList(catDetailArray));
                    CatDetail catDetailObject = catDetailArrayList.get(0);
                    Cat[] catArrayObject = catDetailObject.getBreeds();
                    ArrayList<Cat> catArrayListObject = new ArrayList<Cat>(Arrays.asList(catArrayObject));
                    Cat catObject = catArrayListObject.get(0);
                    favouritesCatArrayList.add(catObject);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener);
            requestQueue.add(stringRequest);
        }

        //handling the bottomNavBar
        BottomNavigationView buttonNavBar = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        buttonNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.SearchMenu: {
                        fragmentManager.beginTransaction().replace(R.id.fragment_slot, new SearchFragment()).commit();
                    } break;
                    case R.id.FavouritesMenu: {
                        fragmentManager.beginTransaction().replace(R.id.fragment_slot, new FavouritesFragment()).commit();
                    } break;
                }
                return true;
            }
        });

    }
}

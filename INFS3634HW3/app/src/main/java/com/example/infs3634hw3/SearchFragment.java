package com.example.infs3634hw3;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private RequestQueue requestQueue;
    private String url;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_search, container, false);

        //initiating requestQueue
        requestQueue = Volley.newRequestQueue(this.getContext());

        //initiating searchBar and searchButton
        final EditText searchBar = frameLayout.findViewById(R.id.searchBar);
        ImageButton searchButton = frameLayout.findViewById(R.id.searchButton);

        //initiating recyclerView
        final RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        recyclerView = frameLayout.findViewById(R.id.rv_search);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //populating full list of cats into recyclerView
        url = "https://api.thecatapi.com/v1/breeds";
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SearchAdapter searchAdapter = new SearchAdapter();
                Gson gson = new Gson();
                Cat[] catArray = gson.fromJson(response,Cat[].class);
                ArrayList<Cat> catArrayList = new ArrayList<Cat>(Arrays.asList(catArray));
                searchAdapter.setData(catArrayList);
                recyclerView.setAdapter(searchAdapter);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener);
        requestQueue.add(stringRequest);

        //handling enter button on keyboard of searchBar
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(frameLayout.getContext().INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String searchQuery = searchBar.getText().toString().toLowerCase();
                    if(searchQuery.length() == 0){
                        url = "https://api.thecatapi.com/v1/breeds";
                    } else{
                        url =  "https://api.thecatapi.com/v1/breeds/search?q=" + searchQuery;
                    }
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            SearchAdapter searchAdapter = new SearchAdapter();
                            Gson gson = new Gson();
                            Cat[] catArray = gson.fromJson(response,Cat[].class);
                            ArrayList<Cat> catArrayList = new ArrayList<Cat>(Arrays.asList(catArray));
                            searchAdapter.setData(catArrayList);
                            recyclerView.setAdapter(searchAdapter);
                        }
                    };
                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    };
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener);
                    requestQueue.add(stringRequest);
                    return true;
                }
                return false;
            }
        });

        //handling searchButton click to populating recyclerView based on the searchBar query
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(frameLayout.getContext().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String searchQuery = searchBar.getText().toString().toLowerCase();
                if(searchQuery.length() == 0){
                    url = "https://api.thecatapi.com/v1/breeds";
                } else{
                    url =  "https://api.thecatapi.com/v1/breeds/search?q=" + searchQuery;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SearchAdapter searchAdapter = new SearchAdapter();
                        Gson gson = new Gson();
                        Cat[] catArray = gson.fromJson(response,Cat[].class);
                        ArrayList<Cat> catArrayList = new ArrayList<Cat>(Arrays.asList(catArray));
                        searchAdapter.setData(catArrayList);
                        recyclerView.setAdapter(searchAdapter);
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
        });

        //returning the framelayout
        return frameLayout;
    }

}

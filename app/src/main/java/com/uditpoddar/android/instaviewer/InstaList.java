package com.uditpoddar.android.instaviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class InstaList extends AppCompatActivity {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private SwipeRefreshLayout swipeContainer;

    private InstaPicAdapter instaPicAdapter;

    private List<InstaPic> instaPics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_list);

        instaPics = new ArrayList<InstaPic>();

        instaPicAdapter = new InstaPicAdapter(this, instaPics);

        ListView lvPictures = (ListView)findViewById(R.id.lvPictures);
        lvPictures.setAdapter(instaPicAdapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                updateInstaList();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        updateInstaList();
    }

    private void updateInstaList() {
        client.get("https://api.instagram.com/v1/media/popular?client_id=7a47e3faf99c4bc092b3b0779a05d854", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    List<InstaPic> updatedInstaPics = new ArrayList<InstaPic>();
                    if ( response.getJSONObject("meta").getInt("code") == 200) {
                        JSONArray respInstPics = response.getJSONArray("data");
                        for ( int i = 0; i < respInstPics.length(); i++ ) {
                            updatedInstaPics.add(InstaPic.of(respInstPics.getJSONObject(i)));
                        }
                    }
                    instaPics.clear();
                    instaPics.addAll(updatedInstaPics);
                    instaPicAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(InstaList.this, "Failed to fetch latested popular items", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}

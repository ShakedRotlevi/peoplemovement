package com.example.shakedrotlevi.peoplemovementapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shakedrotlevi on 12/8/17.
 */

public class SearchActivity extends AppCompatActivity {

    // Define the Teacher Firebase DatabaseReference
    private DatabaseReference databaseEvents;

    // Define a String ArrayList for the teachers
    private ArrayList<String> eventsList = new ArrayList<>();

    // Define a ListView to display the data
    private ListView listViewEvents;

    // Define an ArrayAdapter for the list
    private ArrayAdapter<String> arrayAdapter;

    /**
     * onCreate method
     */


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(SearchActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_map:
                    Log.d("myTag", "Clicked Map");
                    startActivity(new Intent(SearchActivity.this, MapsActivity.class));
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Associate the Teacher Firebase Database Reference with the database's teacher object
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");
        //databaseEvents = databaseEvents.child("events");

        // Associate the teachers' list with the corresponding ListView
        listViewEvents = (ListView) findViewById(R.id.list_events);

        // Set the ArrayAdapter to the ListView
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventsList);
        listViewEvents.setAdapter(arrayAdapter);


        // Attach a ChildEventListener to the teacher database, so we can retrieve the teacher entries
        databaseEvents.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Get the value from the DataSnapshot and add it to the teachers' list
                Log.d(" THis is the VALUE ", " THE VALUE IS ");
              //  Log.d(" HERE " , dataSnapshot.getValue().toString());
                dataSnapshot.child("members");
                Group event = dataSnapshot.getValue(Group.class);
                String eventName = event.getName();
                eventsList.add(eventName);

                // Notify the ArrayAdapter that there was a change
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Here´s the problem! The super.OnItemClick
                //doesn´t work.//

                //Here´s the problem! The super. doesn´t work.//

                String event = eventsList.get(position);

                Intent intent = new Intent(SearchActivity.this, GroupActivity.class);
                intent.putExtra("group", event);
                startActivity(intent);
            }
        });


    }

}
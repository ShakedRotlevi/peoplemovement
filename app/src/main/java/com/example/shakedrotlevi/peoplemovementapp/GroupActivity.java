package com.example.shakedrotlevi.peoplemovementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by shakedrotlevi on 2/13/18.
 */

public class GroupActivity extends AppCompatActivity {
    String group;
    TextView name;
    TextView startLoc;
    TextView endLoc;
    TextView time;
    TextView description;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(GroupActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_map:
                    Log.d("myTag", "Clicked Map");
                    startActivity(new Intent(GroupActivity.this, MapsActivity.class));
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
        Intent i = getIntent();
        group = i.getStringExtra("group");
        setContentView(R.layout.activity_group);

        name = (TextView)findViewById(R.id.name);
        startLoc = (TextView)findViewById(R.id.startLoc);
        endLoc = (TextView)findViewById(R.id.endLoc);
        time = (TextView)findViewById(R.id.time);
        description = (TextView)findViewById(R.id.description);
        //final Group groupEvent;

        Query query = FirebaseDatabase.getInstance().getReference().child("events").orderByChild("name").equalTo(group);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(" THIS IS THE NAME: ", (String)childDataSnapshot.child("name").getValue());
                    name.setText((String)childDataSnapshot.child("name").getValue());
                    startLoc.setText((String)childDataSnapshot.child("startLoc").getValue());
                    endLoc.setText((String)childDataSnapshot.child("endLoc").getValue());
                    time.setText((String)childDataSnapshot.child("time").getValue());
                    description.setText((String)childDataSnapshot.child("description").getValue());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}

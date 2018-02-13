package com.example.shakedrotlevi.peoplemovementapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shakedrotlevi on 12/8/17.
 */

/* have the following 5 fields:
Group name, event start time, event start location, event end location, event description"=
 */
public class CreateGroupActivity extends AppCompatActivity{
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(CreateGroupActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_map:
                    Log.d("myTag", "Clicked Map");
                    startActivity(new Intent(CreateGroupActivity.this, MapsActivity.class));
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_join:
                    startActivity(new Intent(CreateGroupActivity.this, SearchActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    public void onClickAdd(View button) {
        // Do click handling here
        final EditText nameField = (EditText) findViewById(R.id.editView1);
        String name = nameField.getText().toString();

        final EditText timeField = (EditText) findViewById(R.id.editView2);
        String time = timeField.getText().toString();

        final EditText startField = (EditText) findViewById(R.id.editView3);
        String start = startField.getText().toString();
        final EditText endField = (EditText) findViewById(R.id.editView4);
        String end = endField.getText().toString();
        final EditText descriptionField = (EditText) findViewById(R.id.editView5);
        String description = descriptionField.getText().toString();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Group group= new Group(name, time, start, end, description);
        //List<group> events = new ArrayList<>();
        //events.add(name);
        DatabaseReference refEvents = database.getReference("events");
        // this new, empty ref only exists locally
        //DatabaseReference newChildRef = refEvents.push();
       // newChildRef.setValue({name:'name'});
        refEvents.push().setValue(group);
        Log.d(" PUSHED EVENT", " PUSHED EVENT");
    }
}

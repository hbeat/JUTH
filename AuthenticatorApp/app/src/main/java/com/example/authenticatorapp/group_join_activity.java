package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class group_join_activity extends AppCompatActivity {

    private String group_id,user_id,user_group_id,groupName,groupDesc;
    private TextView groupTextName,groupTextDesc;
    private Button joinGroupBtn,delGroupBtn;
    private DatabaseReference dataRef;
    private FirebaseAuth fAuth;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join_activity);
        group_id = getIntent().getExtras().get("group_id").toString();
        groupTextName = (TextView) findViewById(R.id.group_intent_name);
        groupTextDesc = (TextView) findViewById(R.id.group_intent_desc);
        joinGroupBtn = (Button) findViewById(R.id.join_group_button);
        delGroupBtn = (Button) findViewById(R.id.delete_group_button);
        fAuth = FirebaseAuth.getInstance();
        user_id = fAuth.getCurrentUser().getUid();

        dataRef= FirebaseDatabase.getInstance().getReference();

        retrieveGroupInfo();
        mToolbar = (Toolbar) findViewById(R.id.create_group_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Join Group");


        joinGroupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                joinGroup();
                Toast.makeText(getApplicationContext(), "Refresh before chat!", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //finish();
            }
        });

        delGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroup();
                //Toast.makeText(getApplicationContext(), "Group deleted!", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });


    }

    private void retrieveGroupInfo(){


        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Groups").child(group_id).child("group_name").getValue()!= null &&
                        snapshot.child("Groups").child(group_id).child("group_description").getValue()!= null ){
                    groupName = snapshot.child("Groups").child(group_id).child("group_name").getValue().toString();
                    groupDesc = snapshot.child("Groups").child(group_id).child("group_description").getValue().toString();
                }

                user_group_id = snapshot.child("Users").child(user_id).child("Group").getValue().toString();
                groupTextName.setText(groupName);
                groupTextDesc.setText(groupDesc);
                if(group_id.equals(user_group_id)){

                    joinGroupBtn.setEnabled(false);
                    joinGroupBtn.setText("JOINED");
                    delGroupBtn.setVisibility(View.VISIBLE);
                    delGroupBtn.setEnabled(true);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void joinGroup(){
        dataRef.child("Groups").child(AllMethods.group_id).child("Users").child(user_id).removeValue();
        dataRef.child("Users").child(user_id).child("Group").setValue(group_id);
        dataRef.child("Groups").child(group_id).child("Users").child(user_id).setValue(AllMethods.name);
        joinGroupBtn.setText("JOINED");
        joinGroupBtn.setEnabled(false);


    }

    private void deleteGroup(){
        dataRef.child("Groups").child(group_id).removeValue();
        dataRef.child("Users").child(user_id).child("Group").setValue("");
        finish();

    }

}
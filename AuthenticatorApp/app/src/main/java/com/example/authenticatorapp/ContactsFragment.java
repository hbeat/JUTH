package com.example.authenticatorapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseDatabase database;
    DatabaseReference memberdb;
    FirebaseAuth fAuth;
    RecyclerView rvMembers;
    List<String> members;
    String user_id;
    MemberAdapter memberAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        rvMembers = (RecyclerView)v.findViewById(R.id.rvMembers);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        memberdb = database.getReference("Groups").child(AllMethods.group_id).child("Users");
        memberdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dss, @Nullable String previousChildName) {
                String mem = dss.getValue(String.class);
                members.add(mem);
                displayMember(members);
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dss, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dss) {
                String mem = dss.getValue(String.class);
                List<String> newMembers = new ArrayList<String>();

                for(String m: members){
                    if(!m.equals(mem)){
                        newMembers.add(m);
                    }
                }
                members = newMembers;
                displayMember(members);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dss, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onResume(){
        super.onResume();
        members = new ArrayList<>();
    }

    private void displayMember(List<String> members) {
        rvMembers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        memberAdapter = new MemberAdapter(getContext(), members, memberdb);
        rvMembers.setAdapter(memberAdapter);
    }
}
package com.example.authenticatorapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static String TAG = "chatFragment";
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    FirebaseAuth fAuth;
    List<Message> messages;
    String user_id;

    RecyclerView rvMessage;
    EditText etMessage;
    Button btSend;
    Button btRefresh;

    public ChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatsFragment newInstance(String param1, String param2) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.btSend) {
            if (!TextUtils.isEmpty(etMessage.getText().toString())) {
                Message message = new Message(etMessage.getText().toString(), AllMethods.name, user_id);
                etMessage.setText("");
                messagedb.push().setValue(message);
            }
        }
        else if(view.getId() == R.id.btRefresh){
            startActivity(new Intent(getActivity(), MainActivity.class));

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        rvMessage = (RecyclerView)v.findViewById(R.id.rvMessage);
        etMessage = (EditText)v.findViewById(R.id.messageBox);
        btSend = (Button)v.findViewById(R.id.btSend);
        btRefresh = (Button)v.findViewById(R.id.btRefresh);
        btSend.setOnClickListener(this);
        btRefresh.setOnClickListener(this);
        messages = new ArrayList<>();


        return v;
    }

    @Override
    public void onStart(){
        Log.i(TAG,"onStart");
        super.onStart();
        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dss) {
                AllMethods.name = dss.child(user_id).child("Name").getValue().toString();
                AllMethods.group_id = dss.child(user_id).child("Group").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = database.getReference("Groups").child(AllMethods.group_id).child("group_messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dss, @Nullable String previousChildName) {
                Message message = dss.getValue(Message.class);
                message.setKey(dss.getKey());
                messages.add(message);
                displayMessages(messages);
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dss, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dss) {
//                Message message = dss.getValue(Message.class);
//                message.setKey(dss.getKey());
//
//                List<Message> newMessages = new ArrayList<Message>();
//
//                for(Message m: messages){
//                    if(!m.getKey().equals(message.getKey())){
//                        newMessages.add(m);
//                    }
//                }
//                messages = newMessages;
//                displayMessages(messages);
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
        messages = new ArrayList<>();
    }

    private void displayMessages(List<Message> messages) {
        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessageAdapter(getContext(), messages, messagedb);
        rvMessage.setAdapter(messageAdapter);
    }
}
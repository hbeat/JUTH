package com.example.authenticatorapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupsFragment() {
        // Required empty public constructor
    }

    //Database handling
    private RecyclerView groupRecyclerView;
    private DatabaseReference groupRef;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId,userName;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
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
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();


        FirebaseRecyclerOptions<Groups_module> group_list = new FirebaseRecyclerOptions.Builder<Groups_module>().setQuery(groupRef,Groups_module.class).build();
        FirebaseRecyclerAdapter<Groups_module,GroupViewHolder> adapter = new FirebaseRecyclerAdapter<Groups_module, GroupViewHolder>(group_list) {
            @Override
            protected void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull Groups_module model) {
                holder.group_name.setText(model.getGroup_name());
                holder.group_description.setText(model.getGroup_description());
                if(model.getImage()==null){
                    holder.image.setImageResource(R.drawable.usertest);

                }
                else {
                    Picasso.get().load(model.getImage()).into(holder.image);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String group_id = getRef(position).getKey();
                        Intent groupIntent = new Intent(getActivity(),group_join_activity.class);
                        groupIntent.putExtra("group_id",group_id);
                        startActivity(groupIntent);
                    }
                });
            }


            @NonNull
            @Override
            public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.groups_display_layout,viewGroup,false);
                GroupViewHolder groupViewHolder = new GroupViewHolder(view);
                return groupViewHolder;
            }
        };

        groupRecyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_list);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupRecyclerView.setAdapter(adapter);
        adapter.startListening();
        return view;


    }


    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView group_name,group_description;
        CircleImageView image;


        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            group_name = itemView.findViewById(R.id.group_layout_name);
            group_description = itemView.findViewById(R.id.group_layout_desc);
            image = itemView.findViewById(R.id.group_profile_image);
        }
    }
}
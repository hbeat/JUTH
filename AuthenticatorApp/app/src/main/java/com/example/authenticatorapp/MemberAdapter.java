package com.example.authenticatorapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberAdapterViewHolder> {

    Context context;
    List<String> members;
    DatabaseReference memberdb;

    public MemberAdapter(Context context, List<String> members, DatabaseReference memberdb){
        this.context = context;
        this.members = members;
        this.memberdb = memberdb;

    }

    @NonNull
    @Override
    public MemberAdapter.MemberAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member,parent,false);
        return new MemberAdapter.MemberAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapterViewHolder holder, int position) {
        String member = members.get(position);
        Log.i("Name: ", member);
        if(member.equals(AllMethods.name)){
            holder.tvTitle.setText("You");
            holder.tvTitle.setGravity(Gravity.CENTER);
            holder.l1.setBackgroundColor(Color.parseColor("#FAB30F"));
        }
        else{
            holder.tvTitle.setText(member);
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MemberAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        LinearLayout l1;
        public MemberAdapterViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle2);
            l1 = (LinearLayout) itemView.findViewById((R.id.l2message));
        }

    }
}


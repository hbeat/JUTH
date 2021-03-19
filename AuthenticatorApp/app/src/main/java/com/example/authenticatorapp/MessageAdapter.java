package com.example.authenticatorapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    StorageReference storageReference;
    Context context;
    List<Message> messages;
    DatabaseReference messagedb;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messagedb){
        this.context = context;
        this.messages = messages;
        this.messagedb = messagedb;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MessageAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        Message message = messages.get(position);

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+message.getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.image);
            }
        });

        if(message.getName().equals(AllMethods.name)){
            holder.tvTitle.setText("You: " + message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.l1.setBackgroundColor(Color.parseColor("#FAB30F"));

        }
        else{
            holder.tvTitle.setText(message.getName() + ":" + message.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        LinearLayout l1;
        CircleImageView image;
        public MessageAdapterViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle1);
            l1 = (LinearLayout) itemView.findViewById((R.id.l1message));
            image = (CircleImageView) itemView.findViewById(R.id.imUser);
        }

    }
}

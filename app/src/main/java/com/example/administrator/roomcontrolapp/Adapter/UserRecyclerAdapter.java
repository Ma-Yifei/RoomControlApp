package com.example.administrator.roomcontrolapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.UI.LoginActivity;
import com.example.administrator.roomcontrolapp.UI.MainActivity;
import com.example.administrator.roomcontrolapp.UI.Menu.ChangeUserActivity;
import com.example.administrator.roomcontrolapp.util.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {
    private View view;
    private ArrayList<User> users = new ArrayList<User>();

    private ChangeUserActivity context;

    public UserRecyclerAdapter(ChangeUserActivity context,ArrayList<User> users){
        this.context = context;
        this.users = users;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final User user = users.get(position);
        holder.userName.setText(user.getUsername());


        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.remove(position);
                users.add(user);
                Gson gson = new Gson();
                String json = gson.toJson(users);
                SharedPreferences sp = context.getSharedPreferences("users",Context.MODE_PRIVATE);
                sp.edit().putString("user",json).apply();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        public ViewHolder(View itemView) {
            super(itemView);
            userName = view.findViewById(R.id.user_name);
        }
    }
}

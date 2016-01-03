package com.example.json.foogt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.json.foogt.R;
import com.example.json.foogt.entity.User;

import java.util.ArrayList;

/**
 * Created by yeye on 2016/1/3.
 */
public class MFriendAdapter extends RecyclerView.Adapter<MFriendAdapter.ViewHolder> {
    private ArrayList<User> list;

    public MFriendAdapter(ArrayList<User> list) {
        super();
        this.list = list;
    }

    @Override
    public MFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people, parent, false);
        return new MFriendAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MFriendAdapter.ViewHolder holder, int position) {
        holder.username.setText(list.get(position).getUsername());
        if (list.get(position).getUserIntro().length() > 20) {
            String intro = list.get(position).getUserIntro();
            holder.userintro.setText(intro.substring(0, 16)+"...");
        } else {
            holder.userintro.setText(list.get(position).getUserIntro());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView userintro;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.txt_people_name);
            userintro = (TextView) itemView.findViewById(R.id.txt_people_intro);
        }
    }
}

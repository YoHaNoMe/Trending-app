package com.yohanome.moviediscussion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.models.Actor;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<Actor> actors;

    public CastAdapter(){
        this.actors = null;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cast_section_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        if(actors != null){
            Actor actor = actors.get(position);
            holder.actorName.setText(actor.getName());
            Picasso.get().load(actor.getProfileImage()).into(holder.profileImage);
        }else{
            holder.actorName.setText("Name: " + position);
            holder.profileImage.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
    }

    @Override
    public int getItemCount() {
        if(actors != null)
            return actors.size();
        return 4;
    }

    public void setActors(List<Actor> actors){
        this.actors = actors;
        notifyDataSetChanged();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder{
        TextView actorName;
        ImageView profileImage;
        CastViewHolder(View itemView){
            super(itemView);
            actorName = itemView.findViewById(R.id.actor_name_textView);
            profileImage = itemView.findViewById(R.id.actor_profileImage_ImageView);
        }
    }
}

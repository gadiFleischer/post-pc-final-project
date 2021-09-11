package com.example.tripplanner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.models.TripModel;

import java.io.Serializable;

class TripItemViewHolder extends RecyclerView.ViewHolder implements Serializable,View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    TextView countryCodeView;
    TextView tripNameView;
    ImageView actionButton;
    private static final String TAG = "MyViewHolder";



    public TripItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.countryCodeView = itemView.findViewById(R.id.countryCode);
        this.tripNameView = itemView.findViewById(R.id.tripName);
        this.actionButton = itemView.findViewById(R.id.actionButton);
        this.actionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showPopupMenu(v);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(actionButton.getContext(), view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener( this);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popup_edit:
                Log.d(TAG, "onMenuItemClick: action_popup_edit @ " + getAdapterPosition());
                return true;
            case R.id.action_popup_delete:
                Log.d(TAG, "onMenuItemClick: action_popup_delete @ " + getAdapterPosition());
                return true;
            default:
                return false;
        }
    }
}

public class TripItemAdapter extends RecyclerView.Adapter<TripItemViewHolder> implements Serializable {
    TripItemsHolder itemHolder;
    Context context;
    LayoutInflater inflater;

    public TripItemAdapter(TripItemsHolder itemHolder, Context context){
        this.itemHolder = itemHolder;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TripItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.trip_item, parent, false);
        return new TripItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripItemViewHolder holder, int position) {
        TripModel curTrip = this.itemHolder.getCurrentItems().get(position);
        holder.tripNameView.setText(curTrip.title);
        holder.countryCodeView.setText(curTrip.countryCode);
        holder.tripNameView.setOnClickListener(view ->{
            Intent tripDetailsEvent = new Intent(this.context, TripDetails.class);
            tripDetailsEvent.putExtra("tripId", curTrip.id);
            this.context.startActivity(tripDetailsEvent);
        });

        }


    @Override
    public int getItemCount() {
        return this.itemHolder.getCurrentItems().size();
    }

}

package com.example.tripplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.models.TripModel;

class TripItemViewHolder extends RecyclerView.ViewHolder{
    TextView taskDescription;
    CheckBox checkBox;
    ImageView deleteButton;


    public TripItemViewHolder(@NonNull View itemView) {
        super(itemView);
//        this.taskDescription = itemView.findViewById(R.id.taskDescription);
//        this.checkBox = itemView.findViewById(R.id.markCheckbox);
//        this.deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}

public class TripItemAdapter extends RecyclerView.Adapter<TripItemViewHolder>{
    private TripItemsHolder itemHolder;
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
        TripModel curItem = this.itemHolder.getCurrentItems().get(position);
//        holder.taskDescription.setText(curItem.getTaskDescription());
//        holder.checkBox.setChecked(curItem.getIsDone());
//        holder.checkBox.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                int position = holder.getLayoutPosition();
//                TodoItem item = itemHolder.getCurrentItems().get(position);
//                if (holder.checkBox.isChecked()){
//                    itemHolder.markItemDone(position);
//                    notifyDataSetChanged();
//                }
//                else{
//                    itemHolder.markItemInProgress(position);
//                    notifyDataSetChanged();
//                }
//            }
//        });

//        holder.taskDescription.setOnClickListener(view ->{
//            Intent editIntent = new Intent(this.context, EditScreenActivity.class);
//            editIntent.putExtra("Item", curItem);
//            this.context.startActivity(editIntent);
//        });


//        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
//                                                   @Override
//                                                   public void onClick(View view){
//                                                       itemHolder.deleteItem(curItem);
//                                                       notifyDataSetChanged();
//                                                   }
//                                               }
//        );
    }

    @Override
    public int getItemCount() {
        return this.itemHolder.getCurrentItems().size();
    }
}

package com.example.tripplanner;

import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripItemsHolder implements Serializable {
    private List<TripModel> todoItems;

    public TripItemsHolder(){
        this.todoItems = new ArrayList<>();
    }

    public List<TripModel> getCurrentItems() {
        return new ArrayList<>(this.todoItems);
    }

//    @Override
//    public void addNewInProgressItem(String description){
//        this.todoItems.add(0,new TripModel());
//        this.sortList();
//    }

    public void markItemDone(int index) throws IndexOutOfBoundsException {
        if (index >= this.todoItems.size()){
            throw new IndexOutOfBoundsException();
        }
        TripModel item = this.todoItems.get(index);
//        this.sortList();
    }

    public void markItemInProgress(int index) throws IndexOutOfBoundsException {
        if (index >= this.todoItems.size()){
            throw new IndexOutOfBoundsException();
        }
        TripModel item = this.todoItems.get(index);
//        this.sortList();
    }

    public void deleteItem(TripModel item){
        this.todoItems.remove(item);
    }

//    private void sortList(){
//        Collections.sort(this.todoItems, (item1, item2) ->{
//
//            if (item1.startDate.equals(item2.startDate)){
//                return Integer.compare(item2.endDate,item1.endDate);
//            }
//            if (item1.getIsDone()){
//                return 1;
//            }
//            return -1;
//        });
//    }

//    @Override
//    public void editItem(TripModel item){
//        for (int i=0; i<todoItems.size(); i++){
//            if (todoItems.get(i).getCreationTimestamp().equals(item.getCreationTimestamp())){
//                this.todoItems.set(i,item);
//            }
//        }
//        sortList();
//    }

    public void setItems(List<TripModel> items){
        this.todoItems = items;
    }
}

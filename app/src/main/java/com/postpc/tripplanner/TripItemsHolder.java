package com.postpc.tripplanner;

import com.postpc.tripplanner.models.TripModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TripItemsHolder implements Serializable {
    private List<TripModel> myTripsList;

    public TripItemsHolder(){
        this.myTripsList = new ArrayList<>();
    }

    public List<TripModel> getCurrentItems() {
        return new ArrayList<>(this.myTripsList);
    }

    public TripModel getItemByIndex(int index){
        return myTripsList.get(index);
    }

    public void addNewInProgressItem(TripModel newTrip){

        this.myTripsList.add(0,newTrip);
    }

    public void markItemDone(int index) throws IndexOutOfBoundsException {
        if (index >= this.myTripsList.size()){
            throw new IndexOutOfBoundsException();
        }
        TripModel item = this.myTripsList.get(index);

    }

    public void markItemInProgress(int index) throws IndexOutOfBoundsException {
        if (index >= this.myTripsList.size()){
            throw new IndexOutOfBoundsException();
        }
        TripModel item = this.myTripsList.get(index);

    }

    public void deleteItem(TripModel item){
        this.myTripsList.remove(item);
    }


    public void setItems(List<TripModel> items){
        this.myTripsList = items;
    }
}

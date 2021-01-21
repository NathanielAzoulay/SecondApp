package com.example.travel_app_secondapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app_secondapp.databinding.RegisteredRowBinding;
import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

/**
 * Adapter representing the registered travels fragment's recyclerView adapter
 */
public class registeredAdapter extends RecyclerView.Adapter<registeredAdapter.viewHolder> {

    List<Travel> travels;
    IRegistered iRegistered;

    /**
     * CTOR for the adapter gets 2 params
     * @param travels the list of travels which we want to present
     * @param iRegistered the interface of the registered travels's fragment
     */
    public registeredAdapter(List<Travel> travels, IRegistered iRegistered) {
        this.travels = travels;
        this.iRegistered = iRegistered;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RegisteredRowBinding registeredRowBinding = RegisteredRowBinding.inflate(layoutInflater, parent, false);
        registeredRowBinding.setRegistered(iRegistered);
        return new viewHolder(registeredRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Travel travel = travels.get(position);
        holder.registeredRowBinding.setTravel(travel);
        holder.registeredRowBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }

    /**
     * viewHolder is needed to improve user performance/presentation
     */
    public class viewHolder extends RecyclerView.ViewHolder {

        RegisteredRowBinding registeredRowBinding;

        public viewHolder(@NonNull RegisteredRowBinding binding) {
            super(binding.getRoot());
            registeredRowBinding = binding;
        }
    }

    /**
     * Interface of al the functions that the registered fragment need to implement
     */
    public interface IRegistered {
        void accept(String selectedItem, Travel travel);
    }

}

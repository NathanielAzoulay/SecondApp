package com.example.travel_app_secondapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app_secondapp.databinding.RegisteredRowBinding;
import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

public class registeredAdapter extends RecyclerView.Adapter<registeredAdapter.viewHolder> {

    List<Travel> travels;
    IRegistered iRegistered;

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


    public class viewHolder extends RecyclerView.ViewHolder {

        RegisteredRowBinding registeredRowBinding;

        public viewHolder(@NonNull RegisteredRowBinding binding) {
            super(binding.getRoot());
            registeredRowBinding = binding;
        }
    }

    public interface IRegistered {
        void accept(Travel travel);

    }

}

package com.example.travel_app_secondapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app_secondapp.databinding.HistoryRowBinding;
import com.example.travel_app_secondapp.databinding.HistoryRowBinding;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;

import java.util.List;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.viewHolder> {

    List<Travel> travels;
    historyAdapter.IHistory iHistory;

    public historyAdapter(List<Travel> travels, historyAdapter.IHistory iHistory) {
        this.travels = travels;
        this.iHistory = iHistory;
    }

    @NonNull
    @Override
    public historyAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HistoryRowBinding historyRowBinding = HistoryRowBinding.inflate(layoutInflater, parent, false);
        historyRowBinding.setHistory(iHistory);
        return new historyAdapter.viewHolder(historyRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull historyAdapter.viewHolder holder, int position) {
        Travel travel = travels.get(position);
        holder.historyRowBinding.setTravel(travel);
        holder.historyRowBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {

        HistoryRowBinding historyRowBinding;

        public viewHolder(@NonNull HistoryRowBinding binding) {
            super(binding.getRoot());
            historyRowBinding = binding;
        }
    }

    public interface IHistory {
        void send(Travel travel);
        void sendEmail(String emailAddress);
    }

}

package com.example.travel_app_secondapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app_secondapp.databinding.HistoryRowBinding;
import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

/**
 * Adapter representing the history fragment's recyclerView adapter
 */
public class historyAdapter extends RecyclerView.Adapter<historyAdapter.viewHolder> {

    List<Travel> travels;
    historyAdapter.IHistory iHistory;

    /**
     * CTOR for the adapter gets 2 params
     * @param travels the list of travels which we want to present
     * @param iHistory the interface of history's fragment
     */
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

    /**
     * viewHolder is needed to improve user performance/presentation
     */
    public class viewHolder extends RecyclerView.ViewHolder {

        HistoryRowBinding historyRowBinding;

        public viewHolder(@NonNull HistoryRowBinding binding) {
            super(binding.getRoot());
            historyRowBinding = binding;
        }
    }

    /**
     * Interface of al the functions that the history fragment need to implement
     */
    public interface IHistory {
        void confirm(Travel travel);

        void sendEmail(String emailAddress);
    }

}

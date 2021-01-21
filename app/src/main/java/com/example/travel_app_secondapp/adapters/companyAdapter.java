package com.example.travel_app_secondapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app_secondapp.databinding.CompanyRowBinding;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;

import java.util.List;

/**
 * Adapter representing the company fragment's recyclerView adapter
 */
public class companyAdapter extends RecyclerView.Adapter<companyAdapter.viewHolder> {

    List<Travel> travels;
    ICompany iCompany;
    /**
     * CTOR for the adapter gets 2 params
     * @param travels the list of travels which we want to present
     * @param iCompany the interface of company's fragment
     */
    public companyAdapter(List<Travel> travels, ICompany iCompany) {
        this.travels = travels;
        this.iCompany = iCompany;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CompanyRowBinding companyRowBinding = CompanyRowBinding.inflate(layoutInflater, parent, false);
        companyRowBinding.setCompany(iCompany);
        return new viewHolder(companyRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Travel travel = travels.get(position);
        holder.CompanyRowBinding.setTravel(travel);
        holder.CompanyRowBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }


    /**
     * viewHolder is needed to improve user performance/presentation
     */
    public class viewHolder extends RecyclerView.ViewHolder {

        CompanyRowBinding CompanyRowBinding;

        public viewHolder(@NonNull CompanyRowBinding binding) {
            super(binding.getRoot());
            CompanyRowBinding = binding;
        }
    }

    /**
     * Interface of al the functions that the company fragment need to implement
     */
    public interface ICompany {
        void send(Travel travel);

        boolean isSendButtonEnabled(Travel travel);

        void sendEmail(String emailAddress);

        void phoneCall(String phoneNumber);

        String getPlace(UserLocation location);

        List<String> getPlaces(List<UserLocation> locations);
    }

}

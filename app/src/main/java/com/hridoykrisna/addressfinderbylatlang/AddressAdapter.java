package com.hridoykrisna.addressfinderbylatlang;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    Context context;
    List<Address> addressList;

    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.addressTV.setText(
                " Country: "
                        + address.getCountryName()
                        + "\n Country Code: "
                        + address.getCountryCode()
                        +"\n Locality: "
                        + address.getLocality()
                        +"\n Admin Area: "
                        + address.getAdminArea()
                        +"\n Sub Admin Area: "
                        + address.getSubAdminArea()
                        +"\n Get Postal Code: "
                        + address.getPostalCode()
                        +"\n Get Feature: "
                        + address.getFeatureName()
                        +"\n\n Address: "
                        + address.getAddressLine(0)
        );
    }

    @Override
    public int getItemCount() {
        try {
            return addressList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTV = itemView.findViewById(R.id.addressTVid);
        }
    }
}

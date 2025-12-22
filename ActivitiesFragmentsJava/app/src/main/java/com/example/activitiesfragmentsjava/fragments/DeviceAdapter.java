package com.example.activitiesfragmentsjava.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitiesfragmentsjava.R;
import com.example.activitiesfragmentsjava.data.DeviceData;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private final ArrayList<DeviceData> deviceDataList;
    private final OnDeviceActionListener listener;

    public DeviceAdapter(ArrayList<DeviceData> deviceDataList, OnDeviceActionListener listener) {
        this.deviceDataList = deviceDataList;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceData deviceData = deviceDataList.get(position);
        holder.deviceName.setText(deviceData.getDeviceName());
        holder.manufacturer.setText(deviceData.getManufacturer());
        holder.serialNumber.setText(deviceData.getSerialNumber());
        holder.description.setText(deviceData.getDescription());

        holder.editButton.setOnClickListener(v -> listener.onEdit(deviceData, position));
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(deviceData, position));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_item, parent, false);
        return new ViewHolder(view);
    }

    public interface OnDeviceActionListener {
        void onEdit(DeviceData deviceData, int position);

        void onDelete(DeviceData deviceData, int position);
    }

    @Override
    public int getItemCount() {
        return deviceDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView deviceName;
        public final TextView manufacturer;
        public final TextView serialNumber;
        public final TextView description;
        public final Button editButton;
        public final Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.textViewDeviceName);
            manufacturer = itemView.findViewById(R.id.textViewManufacturer);
            serialNumber = itemView.findViewById(R.id.textViewSerialNumber);
            description = itemView.findViewById(R.id.textViewDescription);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

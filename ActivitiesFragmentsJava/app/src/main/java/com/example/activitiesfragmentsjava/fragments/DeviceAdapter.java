package com.example.activitiesfragmentsjava.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitiesfragmentsjava.R;
import com.example.activitiesfragmentsjava.data.DeviceData;

import java.util.Objects;

public class DeviceAdapter extends ListAdapter<DeviceData, DeviceAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<DeviceData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DeviceData>() {
                @Override
                public boolean areItemsTheSame(@NonNull DeviceData oldItem, @NonNull DeviceData newItem) {
                    String oldId = oldItem.getId();
                    String newId = newItem.getId();
                    if (oldId != null && newId != null && !oldId.isEmpty() && !newId.isEmpty()) {
                        return oldId.equals(newId);
                    }
                    return Objects.equals(oldItem.getDeviceName(), newItem.getDeviceName())
                            && Objects.equals(oldItem.getManufacturer(), newItem.getManufacturer())
                            && Objects.equals(oldItem.getSerialNumber(), newItem.getSerialNumber())
                            && Objects.equals(oldItem.getDescription(), newItem.getDescription());
                }

                @Override
                public boolean areContentsTheSame(@NonNull DeviceData oldItem, @NonNull DeviceData newItem) {
                    return Objects.equals(oldItem.getDeviceName(), newItem.getDeviceName())
                            && Objects.equals(oldItem.getManufacturer(), newItem.getManufacturer())
                            && Objects.equals(oldItem.getSerialNumber(), newItem.getSerialNumber())
                            && Objects.equals(oldItem.getDescription(), newItem.getDescription())
                            && Objects.equals(oldItem.getId(), newItem.getId());
                }
            };

    private final OnDeviceActionListener listener;

    public DeviceAdapter(OnDeviceActionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceData deviceData = getItem(position);
        holder.deviceName.setText(deviceData.getDeviceName());
        holder.manufacturer.setText(deviceData.getManufacturer());
        holder.serialNumber.setText(deviceData.getSerialNumber());
        holder.description.setText(deviceData.getDescription());

        holder.editButton.setOnClickListener(v -> listener.onEdit(deviceData));
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(deviceData));
    }

    public interface OnDeviceActionListener {
        void onEdit(DeviceData deviceData);

        void onDelete(DeviceData deviceData);
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

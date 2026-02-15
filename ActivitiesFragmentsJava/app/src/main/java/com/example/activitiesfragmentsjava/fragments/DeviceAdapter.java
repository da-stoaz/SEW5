package com.example.activitiesfragmentsjava.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitiesfragmentsjava.data.DeviceData;
import com.example.activitiesfragmentsjava.databinding.DeviceListItemBinding;

import java.util.Objects;

public class DeviceAdapter extends ListAdapter<DeviceData, DeviceAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<DeviceData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DeviceData>() {
                @Override
                public boolean areItemsTheSame(@NonNull DeviceData oldItem, @NonNull DeviceData newItem) {
                    String oldId = oldItem.getId();
                    String newId = newItem.getId();
                    return oldId != null && oldId.equals(newId);
                }

                @Override
                public boolean areContentsTheSame(@NonNull DeviceData oldItem, @NonNull DeviceData newItem) {
                    return Objects.equals(oldItem.getDeviceName(), newItem.getDeviceName())
                            && Objects.equals(oldItem.getManufacturer(), newItem.getManufacturer())
                            && Objects.equals(oldItem.getSerialNumber(), newItem.getSerialNumber())
                            && Objects.equals(oldItem.getDescription(), newItem.getDescription());
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
        DeviceListItemBinding binding = DeviceListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceData deviceData = getItem(position);
        holder.binding.textViewDeviceName.setText(deviceData.getDeviceName());
        holder.binding.textViewManufacturer.setText(deviceData.getManufacturer());
        holder.binding.textViewSerialNumber.setText(deviceData.getSerialNumber());
        holder.binding.textViewDescription.setText(deviceData.getDescription());

        holder.binding.buttonEdit.setOnClickListener(v -> listener.onEdit(deviceData));
        holder.binding.buttonDelete.setOnClickListener(v -> listener.onDelete(deviceData));
    }

    public interface OnDeviceActionListener {
        void onEdit(DeviceData deviceData);

        void onDelete(DeviceData deviceData);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final DeviceListItemBinding binding;

        public ViewHolder(@NonNull DeviceListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

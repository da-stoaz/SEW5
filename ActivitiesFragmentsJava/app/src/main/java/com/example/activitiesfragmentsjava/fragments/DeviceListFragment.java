package com.example.activitiesfragmentsjava.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.activitiesfragmentsjava.CreateDeviceActivity;
import com.example.activitiesfragmentsjava.R;
import com.example.activitiesfragmentsjava.data.DeviceData;
import com.example.activitiesfragmentsjava.databinding.DialogDeviceBinding;
import com.example.activitiesfragmentsjava.databinding.FragmentDeviceListBinding;
import com.example.activitiesfragmentsjava.network.DeviceApiService;

import java.util.List;

public class DeviceListFragment extends Fragment implements DeviceAdapter.OnDeviceActionListener {

    private FragmentDeviceListBinding binding;
    private DeviceAdapter adapter;
    private DeviceApiService apiService;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = new DeviceApiService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeviceListBinding.inflate(inflater, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL)
        );

        adapter = new DeviceAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateDeviceActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDevices();
    }

    private void loadDevices() {
        apiService.getAllDevices(new DeviceApiService.Callback<List<DeviceData>>() {
            @Override
            public void onSuccess(List<DeviceData> result) {
                if (binding == null) {
                    return;
                }
                adapter.submitList(result);
                updateEmptyView(result.isEmpty(), null);
            }

            @Override
            public void onError(Exception e) {
                if (binding == null) {
                    return;
                }
                String message = "Load failed: " + e.getMessage();
                updateEmptyView(true, message);
            }
        });
    }

    private void updateEmptyView(boolean isEmpty, @Nullable String message) {
        if (binding == null) {
            return;
        }
        if (isEmpty) {
            if (message != null) {
                binding.emptyView.setText(message);
            } else {
                binding.emptyView.setText(R.string.no_devices_found);
            }
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyView.setVisibility(View.GONE);
        }
    }

    private void showEditDialog(@NonNull DeviceData device) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        DialogDeviceBinding dialogBinding = DialogDeviceBinding.inflate(inflater);
        builder.setView(dialogBinding.getRoot());

        dialogBinding.editTextDeviceName.setText(device.getDeviceName());
        dialogBinding.editTextManufacturer.setText(device.getManufacturer());
        dialogBinding.editTextSerialNumber.setText(device.getSerialNumber());
        dialogBinding.editTextDescription.setText(device.getDescription());
        builder.setTitle("Edit Device");

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = dialogBinding.editTextDeviceName.getText().toString();
            String manufacturer = dialogBinding.editTextManufacturer.getText().toString();
            String serialNumber = dialogBinding.editTextSerialNumber.getText().toString();
            String description = dialogBinding.editTextDescription.getText().toString();

            if (name.isEmpty() || manufacturer.isEmpty()) {
                showToast("Name and Manufacturer are required");
                return;
            }

            DeviceData newDeviceData = new DeviceData(name, manufacturer, serialNumber, description);
            apiService.updateDevice(
                    device.getId(),
                    newDeviceData,
                    mutationCallback("Device updated", "Error updating device")
            );
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public void onEdit(DeviceData deviceData) {
        showEditDialog(deviceData);
    }

    @Override
    public void onDelete(DeviceData deviceData) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Device")
                .setMessage("Are you sure you want to delete this device?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    apiService.deleteDevice(
                            deviceData.getId(),
                            mutationCallback("Device deleted", "Error deleting device")
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private DeviceApiService.Callback<Void> mutationCallback(String successMessage, String errorPrefix) {
        return new DeviceApiService.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                showToast(successMessage);
                loadDevices();
            }

            @Override
            public void onError(Exception e) {
                showToast(errorPrefix + ": " + e.getMessage());
            }
        };
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}

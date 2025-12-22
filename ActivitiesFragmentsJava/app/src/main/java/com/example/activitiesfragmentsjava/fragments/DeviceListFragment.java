package com.example.activitiesfragmentsjava.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitiesfragmentsjava.CreateDeviceActivity;
import com.example.activitiesfragmentsjava.R;
import com.example.activitiesfragmentsjava.data.DeviceData;
import com.example.activitiesfragmentsjava.network.DeviceApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DeviceListFragment extends Fragment implements DeviceAdapter.OnDeviceActionListener {

    private static final String ARG_DEVICES = "deviceDataList";

    private ArrayList<DeviceData> deviceDataList = new ArrayList<>();
    private DeviceAdapter adapter;
    private TextView emptyView;
    private RecyclerView recyclerView;
    private DeviceApiService apiService;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    public static DeviceListFragment newInstance(ArrayList<DeviceData> deviceDataList) {
        DeviceListFragment fragment = new DeviceListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_DEVICES, deviceDataList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We initialize the list but we will fetch data from API
        apiService = new DeviceApiService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.emptyView);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DeviceAdapter(deviceDataList, this);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateDeviceActivity.class);
            startActivity(intent);
        });

        return view;
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
                deviceDataList.clear();
                deviceDataList.addAll(result);
                adapter.notifyDataSetChanged();
                updateEmptyView();
            }

            @Override
            public void onError(Exception e) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error loading devices: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        if (deviceDataList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void showEditDialog(@NonNull DeviceData device) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device, null);
        builder.setView(dialogView);

        EditText editDeviceName = dialogView.findViewById(R.id.editTextDeviceName);
        EditText editManufacturer = dialogView.findViewById(R.id.editTextManufacturer);
        EditText editSerialNumber = dialogView.findViewById(R.id.editTextSerialNumber);
        EditText editDescription = dialogView.findViewById(R.id.editTextDescription);

        // Pre-fill data for editing
        editDeviceName.setText(device.getDeviceName());
        editManufacturer.setText(device.getManufacturer());
        editSerialNumber.setText(device.getSerialNumber());
        editDescription.setText(device.getDescription());
        builder.setTitle("Edit Device");

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = editDeviceName.getText().toString();
            String manufacturer = editManufacturer.getText().toString();
            String serialNumber = editSerialNumber.getText().toString();
            String description = editDescription.getText().toString();

            if (name.isEmpty() || manufacturer.isEmpty()) {
                Toast.makeText(getContext(), "Name and Manufacturer are required", Toast.LENGTH_SHORT).show();
                return;
            }

            DeviceData newDeviceData = new DeviceData(name, manufacturer, serialNumber, description);

            // Update existing device
            String id = device.getId();
            apiService.updateDevice(id, newDeviceData, new DeviceApiService.Callback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(getContext(), "Device updated", Toast.LENGTH_SHORT).show();
                    loadDevices();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), "Error updating device: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Device")
                .setMessage("Are you sure you want to delete this device?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    String id = deviceData.getId();
                    apiService.deleteDevice(id, new DeviceApiService.Callback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            Toast.makeText(getContext(), "Device deleted", Toast.LENGTH_SHORT).show();
                            loadDevices();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getContext(), "Error deleting device: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

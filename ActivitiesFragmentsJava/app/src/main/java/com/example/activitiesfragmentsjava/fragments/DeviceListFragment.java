package com.example.activitiesfragmentsjava.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activitiesfragmentsjava.R;
import com.example.activitiesfragmentsjava.data.DeviceData;

public class DeviceListFragment extends Fragment {

    private static final String ARG_DEVICE = "deviceData";

    private DeviceData deviceData;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    public static DeviceListFragment newInstance(DeviceData deviceData) {
        DeviceListFragment fragment = new DeviceListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DEVICE, deviceData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            // API 35: type-safe getParcelable
            deviceData = args.getParcelable(ARG_DEVICE, DeviceData.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_list, container, false);

        TextView deviceName = view.findViewById(R.id.textViewDeviceName);
        TextView manufacturer = view.findViewById(R.id.textViewManufacturer);
        TextView serialNumber = view.findViewById(R.id.textViewSerialNumber);
        TextView description = view.findViewById(R.id.textViewDescription);

        if (deviceData != null) {
            deviceName.setText(deviceData.getDeviceName());
            manufacturer.setText(deviceData.getManufacturer());
            serialNumber.setText(deviceData.getSerialNumber());
            description.setText(deviceData.getDescription());
        }

        return view;
    }
}
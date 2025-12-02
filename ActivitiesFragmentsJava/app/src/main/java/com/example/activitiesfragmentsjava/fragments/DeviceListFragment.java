package com.example.activitiesfragmentsjava.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitiesfragmentsjava.R;
import com.example.activitiesfragmentsjava.data.DeviceData;

import java.util.ArrayList;

public class DeviceListFragment extends Fragment {

    private static final String ARG_DEVICES = "deviceDataList";

    private ArrayList<DeviceData> deviceDataList;

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
        Bundle args = getArguments();
        if (args != null) {
            deviceDataList = args.getParcelableArrayList(ARG_DEVICES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DeviceAdapter adapter = new DeviceAdapter(deviceDataList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

package com.example.activitiesfragmentsjava.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activitiesfragmentsjava.R;
import com.example.activitiesfragmentsjava.data.ItemData;

public class ItemDataFragment extends Fragment {

    private static final String ARG_ITEM_DATA = "itemData";

    private ItemData itemData;

    public static ItemDataFragment newInstance(ItemData itemData) {
        ItemDataFragment fragment = new ItemDataFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM_DATA, itemData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemData = getArguments().getParcelable(ARG_ITEM_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (itemData != null) {
            TextView deviceName = view.findViewById(R.id.deviceName);
            TextView manufacturer = view.findViewById(R.id.manufacturer);
            TextView serialNumber = view.findViewById(R.id.serialNumber);
            TextView description = view.findViewById(R.id.description);

            deviceName.setText(itemData.getDeviceName());
            manufacturer.setText(itemData.getManufacturer());
            serialNumber.setText(itemData.getSerialNumber());
            description.setText(itemData.getDescription());
        } else {
            Toast.makeText(getContext(), "Error: Item data is missing.", Toast.LENGTH_LONG).show();
        }
    }
}

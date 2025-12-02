package com.example.activitiesfragmentsjava.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class DeviceData implements Parcelable {

    private String Id;

    private String DeviceName;

    public static final Creator<DeviceData> CREATOR = new Creator<DeviceData>() {
        @Override
        public DeviceData createFromParcel(Parcel in) {
            return new DeviceData(in);
        }

        @Override
        public DeviceData[] newArray(int size) {
            return new DeviceData[size];
        }
    };

    public DeviceData() {

    }

    private String Manufacturer;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public LocalDateTime getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        UpdatedAt = updatedAt;
    }

    private String SerialNumber;

    private String Description;

    private LocalDateTime UpdatedAt;


    public DeviceData(String deviceName, String manufacturer, String serialNumber, String description) {
        DeviceName = deviceName;
        Manufacturer = manufacturer;
        SerialNumber = serialNumber;
        Description = description;
    }

    protected DeviceData(Parcel in) {
        Id = in.readString();
        DeviceName = in.readString();
        Manufacturer = in.readString();
        SerialNumber = in.readString();
        Description = in.readString();
        if (in.readByte() == 1) {
            UpdatedAt = LocalDateTime.parse(in.readString());
        } else {
            UpdatedAt = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(DeviceName);
        dest.writeString(Manufacturer);
        dest.writeString(SerialNumber);
        dest.writeString(Description);
        if (UpdatedAt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(UpdatedAt.toString());
        }
    }
}

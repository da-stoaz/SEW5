package net.stoaz.firstandroidproject.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DataClass implements Parcelable {

    public DataClass() {
        this.count = 0;
    }

    public DataClass(int count) {
        this.count = count;
    }

    protected DataClass(Parcel in) {
        count = in.readInt();
    }

    public static final Creator<DataClass> CREATOR = new Creator<DataClass>() {
        @Override
        public DataClass createFromParcel(Parcel in) {
            return new DataClass(in);
        }

        @Override
        public DataClass[] newArray(int size) {
            return new DataClass[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(count);
    }
}

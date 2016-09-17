package com.danil.recyclerbindableadapter.sample.grid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Danil on 11.10.2015.
 */
public class GridExampleItem implements Parcelable {
    private Integer value;
    private int type;

    public GridExampleItem(Integer value, int type) {
        this.value = value;
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.value);
        dest.writeInt(this.type);
    }

    protected GridExampleItem(Parcel in) {
        this.value = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<GridExampleItem> CREATOR = new Parcelable.Creator<GridExampleItem>() {
        @Override
        public GridExampleItem createFromParcel(Parcel source) {
            return new GridExampleItem(source);
        }

        @Override
        public GridExampleItem[] newArray(int size) {
            return new GridExampleItem[size];
        }
    };
}

package com.princeparadoxes.danil.recyclerbindableadapter.grid;

/**
 * Created by Danil on 11.10.2015.
 */
public class GridExampleItem {
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
}

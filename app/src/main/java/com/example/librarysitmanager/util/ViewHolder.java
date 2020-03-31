package com.example.librarysitmanager.util;

import android.widget.TextView;

public class ViewHolder {
    private TextView name;
    private TextView say;

    public void setName(TextView name) {
        this.name = name;
    }

    public void setSay(TextView say) {
        this.say = say;
    }

    public TextView getName() {
        return name;
    }

    public TextView getSay() {
        return say;
    }
}

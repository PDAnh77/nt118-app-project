package com.nhom22.studentmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] items;
    private int[] icons;

    public CustomSpinnerAdapter(Context context, String[] items, int[] icons) {
        super(context, R.layout.spinner_item, items);
        this.context = context;
        this.items = items;
        this.icons = icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    private View createCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.spinner_item, parent, false);

        ImageView icon = view.findViewById(R.id.spinner_icon);
        TextView text = view.findViewById(R.id.spinner_text);

        icon.setImageResource(icons[position]);
        text.setText(items[position]);

        return view;
    }
}
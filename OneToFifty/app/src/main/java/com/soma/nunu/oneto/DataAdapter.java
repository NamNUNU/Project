package com.soma.nunu.oneto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends ArrayAdapter<user> {

    private final LayoutInflater inflater;

    DataAdapter(Context context, ArrayList<user> list)
    {
        super(context, 0, list);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.item, null);
        TextView numView = (TextView) convertView.findViewById(R.id.tv1);
        TextView nameView = (TextView)convertView.findViewById(R.id.tv2);
        TextView timeView = (TextView)convertView.findViewById(R.id.tv3);

        final user pi = getItem(position);

        numView.setText(pi.num);
        nameView.setText(pi.name);
        timeView.setText(pi.time);

        return convertView;
    }
}



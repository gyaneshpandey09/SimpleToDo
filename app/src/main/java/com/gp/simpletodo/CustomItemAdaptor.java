package com.gp.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
This is a custom adapter class to display the items on the list efficiently
with their details of priority, completion status & a scheduled date.
 */

public class CustomItemAdaptor extends ArrayAdapter<Item> {
    private final Context context;
    private final ArrayList<Item> values;

    public CustomItemAdaptor(Context context, ArrayList<Item> items) {

        super(context, 0, items);
        this.context = context;
        this.values = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the list item for this position
        Item i = values.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item, parent, false);

        // Lookup view for data population
        TextView itemName = (TextView) convertView.findViewById(R.id.itemText);
        TextView itemDate = (TextView) convertView.findViewById(R.id.itemDate);

        // Populate the data into the template view using the data object
        itemName.setText(i.getText());
        itemDate.setText(i.getDate().toString());

        // Return the completed view to render on screen
        return convertView;
    }

}

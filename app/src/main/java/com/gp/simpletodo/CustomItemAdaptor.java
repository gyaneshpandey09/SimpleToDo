package com.gp.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the list item for this position
        final Item i = values.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        // Lookup view for data population
        TextView itemName = (TextView) convertView.findViewById(R.id.itemText);
        TextView itemDate = (TextView) convertView.findViewById(R.id.itemDate);

        // Populate the data into the template view using the data object
        itemName.setText(i.getText());
        itemDate.setText(i.getDate());

        //Sets up a Click listener on each row of the list
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditItemActivity.class);
                i.setPosition(position);
                intent.putExtra("item", i);
                ((Activity) context).startActivityForResult(intent, 1);

            }
        });

        //Sets up a Long Click listener on each row of the list
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteItem(values.get(position));
                values.remove(position);
                notifyDataSetChanged();
                return true;

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    //This method deletes the item from the List. It assumes that items are not duplicate.
    public void deleteItem(Item item){
        SQLiteDatabase db = MainActivity.databaseHelper.getWritableDatabase();
        db.delete(MainActivity.TABLE_ITEMS, MainActivity.KEY_ITEM_TEXT + " = ?", new String[]{item.getText()});
    }

}

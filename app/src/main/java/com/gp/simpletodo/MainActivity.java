package com.gp.simpletodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

// A Simple Todo List, with custom adapter

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> items;
    CustomItemAdaptor itemsAdapter;
    ListView lvItems;
    int listPos;
    private final int REQUEST_CODE = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";
    private static final String KEY_ITEM_DATE = "date";
    // Get singleton instance of database
    private ItemsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvItems = (ListView) findViewById(R.id.lvItems);

        databaseHelper = ItemsDatabaseHelper.getInstance(this); //Instantiate the DB
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        readItems();
        itemsAdapter = new CustomItemAdaptor(this, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
        setupItemClickListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Item i = new Item(itemText);
        itemsAdapter.add(i);
        writeItems(i);
    }

    //Method which listens to the click on an item and then launches an Edit Item Page
    private void setupItemClickListener(){
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listPos = position;
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("item", items.get(listPos));
                        startActivityForResult(i, REQUEST_CODE);
                    }

                    ;
                });
    }

    //Method which listens to a long click to delete an item
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(

                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        deleteItem(items.get(pos));
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return false;
                    }

                    ;
                });
    }

    private void readItems(){
        List<Item> items = databaseHelper.getAllItems();
        this.items = new ArrayList<Item>();

        for (Item item : items) {
            this.items.add(item);
            // do something
            System.out.println("From DB : "+item.getText());
            System.out.println("From DB : "+item.getDate().toString());
        }
    }

    private void writeItems(Item item){
        // Add sample item to the database
        databaseHelper.addItem(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String oldItemString = data.getExtras().getString("oldItemString");
        String newItemString = data.getExtras().getString("newItemString");
        String itemDate = data.getExtras().getString("itemDate");

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_TEXT, newItemString);
        values.put(KEY_ITEM_DATE, itemDate);
        int rows = db.update(TABLE_ITEMS, values, KEY_ITEM_TEXT + "= ?", new String[]{oldItemString});

        items.set(listPos, new Item(newItemString, itemDate) );
        itemsAdapter.notifyDataSetChanged();
    }

    //This method deletes the item from the List. It assumes that items are not duplicate.
    public void deleteItem(Item item){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ITEM_TEXT + "= ?", new String[]{item.getText()});
    }

}

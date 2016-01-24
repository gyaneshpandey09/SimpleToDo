package com.gp.simpletodo;

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

// A Simple Todo List, with custom adapter

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> items;
    CustomItemAdaptor itemsAdapter;
    ListView lvItems;
    public static int listPos;
    private final int REQUEST_CODE = 1;

    // Table Names
    public static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    public static final String KEY_ITEM_TEXT = "text";
    private static final String KEY_ITEM_DATE = "date";
    // Get singleton instance of database
    static ItemsDatabaseHelper databaseHelper;

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

    //Method which listens to a long click to delete an item
    private void setupOnClickListener(){
    lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, final View view,
                                int position, long id) {
            final String item = (String) parent.getItemAtPosition(position);
            view.animate().setDuration(2000).alpha(0)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(item);
                            itemsAdapter.notifyDataSetChanged();
                            view.setAlpha(1);
                        }
                    });
        }
    });
    }

    private void readItems(){
        items = databaseHelper.getAllItems();
    }

    private void writeItems(Item item){
        // Add sample item to the database
        databaseHelper.addItem(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Item it = (Item)data.getSerializableExtra("item");
        items.set(it.getPosition(), it);
        itemsAdapter.notifyDataSetChanged();
    }

    //This method deletes the item from the List. It assumes that items are not duplicate.
    public void deleteItem(Item item){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ITEM_TEXT + " = ?", new String[]{item.getText()});
    }

}

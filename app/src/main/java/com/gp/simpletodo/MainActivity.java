package com.gp.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    int listPos;
    private final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvItems = (ListView) findViewById(R.id.lvItems);
//        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        items.add("First Item");
        items.add("Second Item");

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
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    //Method which listens to the click on an item and then launches an Edit Item Page
    private void setupItemClickListener(){
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                        listPos = position;
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("itemText", items.get(position));
                        startActivityForResult(i, REQUEST_CODE);
                    }
                    ;
                });
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(

                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return false;
                    }

                    ;
                });
    }

    private void readItems(){
        File filedDr = getFilesDir();
        System.out.println(filedDr);

        File todoFile = new File(filedDr, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File filedDr = getFilesDir();
        File todoFile = new File(filedDr, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String str = data.getExtras().getString("editItem");
        items.set(listPos, str);
        itemsAdapter.notifyDataSetChanged();
        writeItems();


    }
}

package com.gp.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText editItem;
    String newItemString;
    private final int REQUEST_CODE = 1;
    private int RESULT_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editItem = (EditText)findViewById(R.id.editText);
        newItemString = getIntent().getStringExtra("itemText");
        editItem.setText(newItemString);
    }

    public void onSave(View v){
        editItem = (EditText)findViewById(R.id.editText);
        Intent data = new Intent();
        data.putExtra("editItem", editItem.getText().toString());
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}

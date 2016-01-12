package com.gp.simpletodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    EditText editItem;
    static EditText itemDate;
    static Intent data;
    String oldItemString;
    String newItemString;
    private final int REQUEST_CODE = 1;
    private int RESULT_OK = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";


    // Get singleton instance of database
    private ItemsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper = ItemsDatabaseHelper.getInstance(this); //Instantiate the DB
        editItem = (EditText)findViewById(R.id.editText);
        itemDate = (EditText)findViewById(R.id.itemDate);

        Item i = (Item) getIntent().getSerializableExtra("item");

        editItem.setText(i.getText());
        itemDate.setText(i.getDate());

        //Query the database to set the Date String from the Database for this item.
        itemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        oldItemString = editItem.getText().toString();
    }

    public void onSave(View v){

        editItem = (EditText)findViewById(R.id.editText);
        newItemString = editItem.getText().toString();

        data = new Intent();
        data.putExtra("oldItemString", oldItemString);
        data.putExtra("newItemString", newItemString);

        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment  extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Set the date as the Date text
            String date = (month+1) + "/" + day + "/" + year;
            data.putExtra("itemDate", date);
            itemDate.setText(date);
        }
    }

}

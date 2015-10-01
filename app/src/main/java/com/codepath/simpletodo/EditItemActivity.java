package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.joda.time.DateTime;

public class EditItemActivity extends Activity {
    public static final String EDIT_TEXT_KEY = "editText";
    public static final String ITEM_INDEX_KEY = "itemIndex";
    public static final String DUE_DATE_KEY = "dueDate";
    private int itemPosition;
    private DateTime dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String editTextValue = getIntent().getStringExtra(EDIT_TEXT_KEY);
        itemPosition = getIntent().getIntExtra(ITEM_INDEX_KEY, -1); // TODO do some error checking?
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(editTextValue);

        long dueDateMillis = getIntent().getLongExtra(DUE_DATE_KEY, -1);
        dueDate = dueDateMillis == -1 ? null : new DateTime(dueDateMillis);
        if (dueDate != null) {
            DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
            datePicker.updateDate(dueDate.getYear(), dueDate.getMonthOfYear() - 1, dueDate.getDayOfMonth());

            TimePicker timePicker = (TimePicker) findViewById(R.id.dueDateTime);
            timePicker.setCurrentHour(dueDate.getHourOfDay());
            timePicker.setCurrentMinute(dueDate.getMinuteOfHour());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onEditItem(View view) {
        EditText etName = (EditText) findViewById(R.id.editText);
        Intent data = new Intent();
        data.putExtra(EDIT_TEXT_KEY, etName.getText().toString());
        data.putExtra(ITEM_INDEX_KEY, itemPosition);
        data.putExtra(DUE_DATE_KEY, getDueDate().getMillis());
        setResult(RESULT_OK, data);
        finish();
    }

    private DateTime getDueDate() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker) findViewById(R.id.dueDateTime);
        return new DateTime(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    }
}

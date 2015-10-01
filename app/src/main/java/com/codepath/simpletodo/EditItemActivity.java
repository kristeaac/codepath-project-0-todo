package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends Activity {
    public static final String EDIT_TEXT_KEY = "editText";
    public static final String ITEM_INDEX_KEY = "itemIndex";
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String editTextValue = getIntent().getStringExtra(EDIT_TEXT_KEY);
        itemPosition = getIntent().getIntExtra(ITEM_INDEX_KEY, -1); // TODO do some error checking?
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(editTextValue);
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
        setResult(RESULT_OK, data);
        finish();
    }
}

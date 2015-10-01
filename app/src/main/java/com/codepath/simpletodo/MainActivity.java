package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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


public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 20;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        listViewItems.setAdapter(itemsAdapter);
        setupListViewLongClickListener();
        setupListViewItemClickListener();
    }

    private void setupListViewLongClickListener() {
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private void setupListViewItemClickListener() {
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                String itemAtPosition = listViewItems.getItemAtPosition(position).toString();
                intent.putExtra(EditItemActivity.EDIT_TEXT_KEY, itemAtPosition);
                intent.putExtra(EditItemActivity.ITEM_INDEX_KEY, position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void readItems() {
        File filesDirectory = getFilesDir();
        File todoFile = new File(filesDirectory, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDirectory = getFilesDir();
        File todoFile = new File(filesDirectory, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void onAddItem(View view) {
        EditText editText = (EditText) findViewById(R.id.edNewItem);
        String itemText = editText.getText().toString();
        itemsAdapter.add(itemText);
        writeItems();
        editText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editText = data.getExtras().getString(EditItemActivity.EDIT_TEXT_KEY);
            int itemPosition = data.getExtras().getInt(EditItemActivity.ITEM_INDEX_KEY);
            updateItem(itemPosition, editText);
        }
    }

    private void updateItem(int position, String text) {
        items.set(position, text);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}

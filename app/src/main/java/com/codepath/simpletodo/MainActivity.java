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

import com.codepath.simpletodo.data.DatabaseHelper;
import com.codepath.simpletodo.data.TodoItem;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 20;
    private ArrayList<String> itemText;
    private ArrayList<TodoItem> items;
    private ArrayAdapter<String> itemsAdapter;
    ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, itemText);
        listViewItems.setAdapter(itemsAdapter);
        setupListViewLongClickListener();
        setupListViewItemClickListener();
    }

    private void setupListViewLongClickListener() {
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemText.remove(position);
                itemsAdapter.notifyDataSetChanged();
                deleteItem(items.get(position));
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
        DatabaseHelper helper = DatabaseHelper.getHelper(getApplicationContext());
        Iterable<TodoItem> itr = helper.readAll(TodoItem.class);
        itemText = new ArrayList<String>();
        items = new ArrayList<TodoItem>();
        for (TodoItem todoItem : itr) {
            itemText.add(todoItem.getValue());
            items.add(todoItem);
        }
    }

    private void deleteItem(TodoItem todoItem) {
        DatabaseHelper helper = DatabaseHelper.getHelper(getApplicationContext());
        helper.delete(todoItem);
    }

    private void writeItems() {
        DatabaseHelper helper = DatabaseHelper.getHelper(getApplicationContext());
        helper.write(items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        EditText editText = (EditText) findViewById(R.id.edNewItem);
        String itemText = editText.getText().toString();
        TodoItem item = new TodoItem(itemText);
        itemsAdapter.add(itemText);
        items.add(item);
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
        items.get(position).setValue(text);
        itemText.set(position, text);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}

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
import com.codepath.simpletodo.data.TodoItemAdapter;
import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 20;
    private List<TodoItem> items;
    private TodoItemAdapter itemsAdapter;
    ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new TodoItemAdapter(getApplicationContext(), items);
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
                TodoItem itemAtPosition = (TodoItem) listViewItems.getItemAtPosition(position);
                intent.putExtra(EditItemActivity.EDIT_TEXT_KEY, itemAtPosition.getValue());
                Optional<DateTime> dueDate = itemAtPosition.getDueDate();
                intent.putExtra(EditItemActivity.DUE_DATE_KEY, dueDate.isPresent() ? dueDate.get().getMillis() : null);
                intent.putExtra(EditItemActivity.ITEM_INDEX_KEY, position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void readItems() {
        DatabaseHelper helper = DatabaseHelper.getHelper(getApplicationContext());
        Iterable<TodoItem> itr = helper.readAll(TodoItem.class);
        items = new ArrayList<TodoItem>();
        for (TodoItem todoItem : itr) {
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
        items.add(item);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
        editText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editText = data.getExtras().getString(EditItemActivity.EDIT_TEXT_KEY);
            int itemPosition = data.getExtras().getInt(EditItemActivity.ITEM_INDEX_KEY);
            DateTime dueDate = new DateTime(data.getExtras().getLong(EditItemActivity.DUE_DATE_KEY));
            updateItem(itemPosition, editText, dueDate);
        }
    }

    private void updateItem(int position, String text, DateTime dueDate) {
        TodoItem item = items.get(position);
        item.setValue(text);
        item.setDueDate(dueDate);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}

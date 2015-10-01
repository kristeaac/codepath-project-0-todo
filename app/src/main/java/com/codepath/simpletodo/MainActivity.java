package com.codepath.simpletodo;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.codepath.simpletodo.data.DatabaseHelper;
import com.codepath.simpletodo.data.TodoItem;
import com.codepath.simpletodo.data.TodoItemAdapter;
import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements EditTodoItemDialogFragment.EditTodoItemDialogListener {
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

                showEditDialogFragment(position);
            }
        });
    }

    private void showEditDialogFragment(int position) {
        TodoItem itemAtPosition = (TodoItem) listViewItems.getItemAtPosition(position);

        FragmentManager fm = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(EditTodoItemDialogFragment.EDIT_TEXT_KEY, itemAtPosition.getValue());
        bundle.putInt(EditTodoItemDialogFragment.ITEM_INDEX_KEY, position);
        if (itemAtPosition.getPriority().isPresent()) {
            String priorityString = itemAtPosition.getPriority().get().name();
            bundle.putString(EditTodoItemDialogFragment.PRIORITY_KEY, priorityString);
        }
        Optional<DateTime> dueDate = itemAtPosition.getDueDate();
        if (dueDate.isPresent()) {
            bundle.putLong(EditTodoItemDialogFragment.DUE_DATE_KEY, dueDate.get().getMillis());
        }
        EditTodoItemDialogFragment editNameDialog = (EditTodoItemDialogFragment) EditTodoItemDialogFragment.instantiate(getApplicationContext(), "com.codepath.simpletodo.EditTodoItemDialogFragment", bundle);
        editNameDialog.setTargetFragment(editNameDialog, REQUEST_CODE);
        editNameDialog.show(fm, "fragment_edit_name");
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
    public void onFinishEditDialog(String value, int itemPosition, DateTime dueDate, TodoItem.Priority priority) {
        updateItem(itemPosition, value, dueDate, priority);
    }

    private void updateItem(int position, String text, DateTime dueDate, TodoItem.Priority priority) {
        TodoItem item = items.get(position);
        item.setValue(text);
        item.setDueDate(dueDate);
        item.setPriority(priority);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}

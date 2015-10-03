package com.codepath.simpletodo.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.google.common.base.Optional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("E, d MMM yyyy HH:mm");
    private Context context;
    private List<TodoItem> items;

    public TodoItemAdapter(Context context, List<TodoItem> items) {
        super(context, R.layout.listview_todo_item_row, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_todo_item_row, parent, false);

        TodoItem item = items.get(position);
        TextView itemText = (TextView) view.findViewById(R.id.todoItemText);
        itemText.setText(item.getValue());

        Optional<DateTime> dueDateOptional = item.getDueDate();
        if (dueDateOptional.isPresent()) {
            DateTime dueDate = dueDateOptional.get();
            TextView dueDateText = (TextView) view.findViewById(R.id.todoItemDueDate);
            dueDateText.setText(String.format("Due %s", FORMATTER.print(dueDate)));
        }

        Optional<TodoItem.Priority> priorityOptional = item.getPriority();
        TextView priorityText = (TextView) view.findViewById(R.id.itemPriority);

        if (priorityOptional.isPresent()) {
            TodoItem.Priority priority = priorityOptional.get();
            int priorityBorder = R.drawable.right_border_high;
            switch (priority) {
                case HIGH:
                    priorityBorder = R.drawable.right_border_high;
                    break;
                case MEDIUM:
                    priorityBorder = R.drawable.right_border_medium;
                    break;
                case LOW:
                    priorityBorder = R.drawable.right_border_low;
                    break;
            }
            priorityText.setText(priority.name());
            Drawable drawable = getContext().getResources().getDrawable(priorityBorder);
            priorityText.setBackground(drawable);

        } else {
            priorityText.setText("");
        }

        return view;
    }
}

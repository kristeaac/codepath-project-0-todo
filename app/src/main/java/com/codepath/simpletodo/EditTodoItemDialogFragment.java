package com.codepath.simpletodo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.codepath.simpletodo.data.TodoItem;

import org.joda.time.DateTime;

import javax.annotation.Nullable;

public class EditTodoItemDialogFragment extends DialogFragment {
    public static final String EDIT_TEXT_KEY = "editText";
    public static final String ITEM_INDEX_KEY = "itemIndex";
    public static final String DUE_DATE_KEY = "dueDate";
    public static final String PRIORITY_KEY = "priority";
    private int itemPosition;
    private DateTime dueDate;
    private View fragmentView;

    public EditTodoItemDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        fragmentView = layoutInflater.inflate(R.layout.fragment_edit_item, viewGroup);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String editTextValue = getArguments().getString(EDIT_TEXT_KEY);
        itemPosition = getArguments().getInt(ITEM_INDEX_KEY);
        EditText editText = (EditText) view.findViewById(R.id.editText);
        editText.setText(editTextValue);

        Long dueDateMillis = getArguments().getLong(DUE_DATE_KEY);
        dueDate = dueDateMillis == null || dueDateMillis == 0 ? null : new DateTime(dueDateMillis);
        if (dueDate != null) {
            DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
            datePicker.updateDate(dueDate.getYear(), dueDate.getMonthOfYear() - 1, dueDate.getDayOfMonth());

            TimePicker timePicker = (TimePicker) view.findViewById(R.id.dueDateTime);
            timePicker.setCurrentHour(dueDate.getHourOfDay());
            timePicker.setCurrentMinute(dueDate.getMinuteOfHour());
        }

        Button saveButton = (Button) view.findViewById(R.id.saveEditButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditItem(fragmentView);
            }
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.itemPrioritySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.item_priorities, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String priorityString = getArguments().getString(PRIORITY_KEY);
        if (priorityString != null) {
            TodoItem.Priority priority = TodoItem.Priority.valueOf(priorityString);
            spinner.setSelection(priority.ordinal());
        }
    }

    public void onEditItem(View view) {
        EditText etName = (EditText) view.findViewById(R.id.editText);
        String value = etName.getText().toString();
        dueDate = getDueDate(view);
        Spinner spinner = (Spinner) view.findViewById(R.id.itemPrioritySpinner);
        TodoItem.Priority priority = TodoItem.Priority.valueOf((String) spinner.getSelectedItem());
        EditTodoItemDialogListener activity = (EditTodoItemDialogListener) getActivity();
        activity.onFinishEditDialog(value, itemPosition, dueDate, priority);
        this.dismiss();
    }

    private DateTime getDueDate(View view) {
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.dueDateTime);
        return new DateTime(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    }

    public interface EditTodoItemDialogListener {

        void onFinishEditDialog(String value, int itemPosition, DateTime dueDate, TodoItem.Priority priority);

    }


}

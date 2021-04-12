package com.example.tasks.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.helper.DateConverterUtil;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private ViewHolder mViewHolder = new ViewHolder();
    private TaskViewModel mViewModel;
    private List<Integer> mListPriorityId = new ArrayList<>();
    private int mTaskId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Botão de voltar nativo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.mViewHolder.editDescription = findViewById(R.id.edit_description);
        this.mViewHolder.spinnerPriority = findViewById(R.id.spinner_priority);
        this.mViewHolder.checkComplete = findViewById(R.id.check_complete);
        this.mViewHolder.buttonDatePicker = findViewById(R.id.button_date);
        this.mViewHolder.buttonSave = findViewById(R.id.button_save);

        // ViewModel
        this.mViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Cria observadores
        this.loadObservers();

        this.mViewModel.getListPriority();

        this.eventClick();

        this.loadDataFromActivity();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        String date = this.mFormat.format(c.getTime());
        this.mViewHolder.buttonDatePicker.setText(date);
    }


    public void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, this, year, month, day).show();
    }

    private void loadDataFromActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID);
            mViewHolder.buttonSave.setText(this.getString(R.string.update_task));
            this.mViewModel.load(this.mTaskId);
        }
    }

    private void loadObservers() {
        this.mViewModel.listPriority.observe(this, list -> loadSpinner(list));

        this.mViewModel.taskFeedback.observe(this, feedback -> {
            if (feedback.isSuccess()) {
                if (mTaskId == 0) {
                    toast(getApplicationContext().getString(R.string.task_created));
                } else {
                    toast(getApplicationContext().getString(R.string.task_updated));
                }
            } else {
                toast(feedback.getMessage());
            }
        });

        this.mViewModel.taskLoad.observe(this, taskModel -> {
            mViewHolder.editDescription.setText(taskModel.getDescription());
            mViewHolder.checkComplete.setChecked(taskModel.getComplete());
            mViewHolder.buttonDatePicker.setText(DateConverterUtil.dateInternationalToPTBR(taskModel.getDueDate()));
            mViewHolder.spinnerPriority.setSelection(getIndexListPriority(taskModel.getPriorityId()));

        });
    }

    private int getIndexListPriority(int priorityId) {
        for (int i = 0; i < this.mListPriorityId.size(); i++) {
            if (this.mListPriorityId.get(i) == priorityId)
                return i;
        }
        return 0;
    }

    private void loadSpinner(List<PriorityModel> list) {
        List<String> listPriorities = new ArrayList<>();
        for (PriorityModel p : list) {
            this.mListPriorityId.add(p.getId());
            listPriorities.add(p.getDescription());
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listPriorities);
        mViewHolder.spinnerPriority.setAdapter(adapter);
    }

    private void eventClick() {
        this.mViewHolder.buttonDatePicker.setOnClickListener(v -> showDatePicker());
        this.mViewHolder.buttonSave.setOnClickListener(v -> handlerSave());
    }

    private void handlerSave() {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(this.mTaskId);
        taskModel.setDescription(this.mViewHolder.editDescription.getText().toString());
        taskModel.setComplete(this.mViewHolder.checkComplete.isChecked());
        taskModel.setDueDate(this.mViewHolder.buttonDatePicker.getText().toString());
        taskModel.setPriorityId(this.mListPriorityId.get(this.mViewHolder.spinnerPriority.getSelectedItemPosition()));

        this.mViewModel.save(taskModel);
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editDescription;
        Spinner spinnerPriority;
        CheckBox checkComplete;
        Button buttonDatePicker;
        Button buttonSave;
    }
}
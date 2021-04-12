package com.example.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.Feedback;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.service.repository.PriorityRepository;
import com.example.tasks.service.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private PriorityRepository mPriorityRepository;
    private TaskRepository mTaskRepository;

    private MutableLiveData<Feedback> mTaskFeedback = new MutableLiveData<>();
    public LiveData<Feedback> taskFeedback = this.mTaskFeedback;

    private MutableLiveData<TaskModel> mTaskLoad = new MutableLiveData<>();
    public LiveData<TaskModel> taskLoad = this.mTaskLoad;

    private MutableLiveData<List<PriorityModel>> mListPriority = new MutableLiveData<>();
    public LiveData<List<PriorityModel>> listPriority = this.mListPriority;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        this.mPriorityRepository = new PriorityRepository(application);
        this.mTaskRepository = new TaskRepository(application);
    }

    public void getListPriority() {
        List<PriorityModel> list = this.mPriorityRepository.getList();
        this.mListPriority.setValue(list);
    }

    public void save(TaskModel taskModel) {

        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mTaskFeedback.setValue(new Feedback());
            }

            @Override
            public void onFail(String message) {
                mTaskFeedback.setValue(new Feedback(message));
            }
        };

        if (taskModel.getId() == 0) {
            this.mTaskRepository.create(taskModel, listener);
        }else{
            this.mTaskRepository.update(taskModel, listener);
        }
    }

    public void load(int mTaskId) {
        this.mTaskRepository.load(mTaskId, new APIListener<TaskModel>() {
            @Override
            public void onSuccess(TaskModel result) {
                mTaskLoad.setValue(result);
            }

            @Override
            public void onFail(String message) {
                mTaskFeedback.setValue(new Feedback(message));
            }
        });
    }
}

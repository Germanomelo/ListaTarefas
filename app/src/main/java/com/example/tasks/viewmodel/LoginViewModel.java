package com.example.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.helper.FingerprintHelper;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.Feedback;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.repository.PersonRepository;
import com.example.tasks.service.repository.PriorityRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private final PersonRepository mPersonRepository;
    private final PriorityRepository mPriorityRepository;

    private MutableLiveData<Feedback> mLogin = new MutableLiveData<>();
    public LiveData<Feedback> login = this.mLogin;

    private MutableLiveData<Boolean> mFingerprint = new MutableLiveData<>();
    public LiveData<Boolean> fingerprint = this.mFingerprint;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.mPersonRepository = new PersonRepository(application);
        this.mPriorityRepository = new PriorityRepository(application);
    }

    public void login(String email, String password) {
        this.mPersonRepository.login(email, password, new APIListener<PersonModel>() {
            @Override
            public void onSuccess(PersonModel result) {
                result.setEmail(email);
                mPersonRepository.saveUserDate(result);

                mLogin.setValue(new Feedback());
            }

            @Override
            public void onFail(String message) {
                mLogin.setValue(new Feedback(message));
            }
        });
    }

    public void isFingerprintAvailable() {
        PersonModel person = this.mPersonRepository.getUserDate();
        boolean logged = person != null;

        this.mPersonRepository.saveUserDate(person);

        if(FingerprintHelper.isAvailable(getApplication())){
            //logged = true; //hack para logar sem usuario salvo, o servidor saiu do ar
            this.mFingerprint.setValue(logged);
        }

        if(!logged){
            this.mPriorityRepository.all(new APIListener<List<PriorityModel>>() {
                @Override
                public void onSuccess(List<PriorityModel> result) {
                    mPriorityRepository.save(result);
                }

                @Override
                public void onFail(String message) {

                }
            });
        }
    }
}

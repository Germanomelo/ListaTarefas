package com.example.tasks.service.repository;

import android.content.Context;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.repository.local.SecurityPreferences;
import com.example.tasks.service.repository.remote.PersonService;
import com.example.tasks.service.repository.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonRepository extends BaseRepository{

    private PersonService mPersonService;
    private SecurityPreferences mSecurityPreferences;
    private Context mContext;

    public PersonRepository(Context context) {
        super(context);
        this.mPersonService = RetrofitClient.createService(PersonService.class);
        this.mSecurityPreferences = new SecurityPreferences(context);
    }

    public void create(String name, String email, String password, final APIListener<PersonModel> listener) {
        Call<PersonModel> call = this.mPersonService.create(name,email,password,true);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                }else{
                    listener.onFail(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                listener.onFail(mContext.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }

    public void login(String email, String password, final APIListener<PersonModel> listener){
        Call<PersonModel> call = this.mPersonService.login(email,password);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else{
                    listener.onFail(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                listener.onFail(mContext.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }

    public void saveUserDate(PersonModel personModel){
        this.mSecurityPreferences.savePersonModel(personModel);
        if(personModel !=null)
        RetrofitClient.saveHeaders(personModel.getToken(), personModel.getPersonKey());
    }

    public void clearUserDate(){
        this.mSecurityPreferences.removePersonModel();
    }

    public PersonModel getUserDate(){
        return this.mSecurityPreferences.getPersonModel();
    }

}

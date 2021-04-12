package com.example.tasks.service.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tasks.service.model.PersonModel;
import com.google.gson.Gson;

/**
 * SharedPreferences
 */
public class SecurityPreferences {

    private static final String PERSON_MODEL = "personModelKey";
    private SharedPreferences mSharedPreferences;

    public SecurityPreferences(Context context){
        this.mSharedPreferences = context.getSharedPreferences("TasksShared", context.MODE_PRIVATE);
    }

    public void savePersonModel(PersonModel personModel){
        String pm = new Gson().toJson(personModel);
        saveStoreString(PERSON_MODEL, pm);
    }

    public PersonModel getPersonModel(){
        String pm = this.getStoreString(PERSON_MODEL);
        if(pm.equals("")){
            return null;
        }
        return new Gson().fromJson(pm,PersonModel.class);
    }

    public void removePersonModel(){
        remove(PERSON_MODEL);
    }

    private void saveStoreString(String key, String value){
        this.mSharedPreferences.edit().putString(key, value).apply();
    }

    private String getStoreString(String key){
        return this.mSharedPreferences.getString(key,"");
    }

    private void remove(String key){
        this.mSharedPreferences.edit().remove(key);
    }


}

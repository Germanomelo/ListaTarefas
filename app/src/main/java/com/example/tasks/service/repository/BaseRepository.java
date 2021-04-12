package com.example.tasks.service.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.example.tasks.R;
import com.example.tasks.service.helper.ConnectionNetworkUtil;
import com.google.gson.Gson;

import okhttp3.ResponseBody;

public class BaseRepository {

    Context mContext;

    public BaseRepository(Context context) {
        this.mContext = context;
    }

    /**
     * Faz a lógica de erro na requisição
     */
    public String handleFailure(ResponseBody response) {
        try {
            return new Gson().fromJson(response.string(), String.class);
        } catch (Exception e) {
            return mContext.getString(R.string.ERROR_UNEXPECTED);
        }
    }

    /**
     * Verifica se existe conexão com internet
     */
    Boolean isConnectionAvailable() {
        return ConnectionNetworkUtil.isConnectionAvailable(this.mContext);
    }
}

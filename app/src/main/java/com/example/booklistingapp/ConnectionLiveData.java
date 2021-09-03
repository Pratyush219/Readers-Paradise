package com.example.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.HashSet;

public class ConnectionLiveData extends LiveData<Boolean> {
    private String TAG = "C-Manager";
    private ConnectivityManager.NetworkCallback callback;
    private final ConnectivityManager manager;
    private final HashSet<Network> validNetworks;

    public ConnectionLiveData(Context context){
        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        validNetworks = new HashSet<>();
    }

    @Override
    protected void onActive() {
        callback = createnetworkCallback();
        NetworkRequest networkRequest =
                new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build();
        manager.registerNetworkCallback(networkRequest, callback);

    }

    @Override
    protected void onInactive() {
        manager.unregisterNetworkCallback(callback);
    }

    private void checkValidNetworks(){
        postValue(validNetworks.size() > 0);
    }
    private ConnectivityManager.NetworkCallback createnetworkCallback(){

        return new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
                boolean hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                if(hasInternet)
                    validNetworks.add(network);
                checkValidNetworks();
            }

            @Override
            public void onLost(@NonNull Network network) {
                validNetworks.remove(network);
                checkValidNetworks();
            }

            @Override
            public void onUnavailable() {
                postValue(false);
            }
        };
    }
}

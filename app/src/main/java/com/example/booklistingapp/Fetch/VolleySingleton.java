package com.example.booklistingapp.Fetch;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static volatile VolleySingleton mInstance;
    private final RequestQueue mRequestQueue;
    private VolleySingleton(Context context){
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public static VolleySingleton getInstance(Context context){
        if(mInstance == null){
            synchronized (VolleySingleton.class) {
                if (mInstance == null) {
                    mInstance = new VolleySingleton(context);
                }
            }
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}


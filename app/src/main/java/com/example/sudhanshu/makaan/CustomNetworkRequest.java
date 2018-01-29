package com.example.sudhanshu.makaan;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

/**
 * Created by sudhanshu on 1/24/18.
 */

public class CustomNetworkRequest {

    private Context mContext;
    private  static CustomNetworkRequest mInstance;
    private RequestQueue requestQueue;
    private Gson gson;
    private ImageLoader imageLoader;


    /**
     * Class method for initialising the request queue
     * **/
    public static CustomNetworkRequest getInstance(Context context){
        if(null == mInstance){
            mInstance = new CustomNetworkRequest(context);
        }

        return mInstance;
    }


    /**
     * A help method for getting the request queue
     * throws exception if setup is not done
     * **/
    public RequestQueue getRequestQueue(){

        if(null == requestQueue){
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }

        return requestQueue;
    }
    public Gson getGson(){
        if(null==gson)
            gson=new Gson();
        return gson;
    }
    /**
     * A help method for getting the request queue
     * throws exception if setup is not done
     * */
    public void callGetApi(Response.Listener<String> listener, Response.ErrorListener errorListener, String url){
        if(null == requestQueue){
            throw (new IllegalStateException("Request is not initialized yet"));
        }

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);

        requestQueue.add(stringRequest);
    }


    /**
     *
     * Private constructor
     * **/
    private CustomNetworkRequest(Context context) {
        mContext=context;
        requestQueue= getRequestQueue();
    }

    public ImageLoader getImageLoader() {
        if(imageLoader==null){
            imageLoader=new ImageLoader(requestQueue,
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap>
                                cache = new LruCache<String, Bitmap>(20);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }
        return imageLoader;
    }
}

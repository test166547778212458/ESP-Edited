package connection;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Location.LocationFinder;


public class RequestQueueSingleton{

    private static final String TAG = RequestQueueSingleton.class.getSimpleName();

    private RequestQueue requestQueue;

    private static RequestQueueSingleton Instance;


    private RequestQueueSingleton() {
        Instance = this;
    }

    public static synchronized RequestQueueSingleton getInstance() {
        if(Instance == null){
            Instance = new RequestQueueSingleton();
        }
        return Instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(LocationFinder.getInstance().getApp());
        }
        Log.d(TAG,requestQueue.toString());
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}

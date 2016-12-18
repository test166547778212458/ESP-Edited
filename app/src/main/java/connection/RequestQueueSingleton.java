package connection;

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

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

}

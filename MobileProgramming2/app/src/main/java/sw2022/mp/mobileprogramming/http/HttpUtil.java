package sw2022.mp.mobileprogramming.http;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by sineuncheol on 2017. 11. 8..
 */

public class HttpUtil {
    private final static boolean DEBUG = true;
    private final static String TAG = "HttpUtil";
    private static final String BASE_URL = "http://54.180.39.199/";
    public interface CompleteAPIListener{
        void complete(JSONObject response);
    }

    //*****************************  MEMBER ********************************//
    public static final String MemberLogin = "api/user/login/";

    public static final String LOCATIONUPLOAD = "api/user/locationUpload/";

    //*****************************  IMAGE ********************************//
    public static final String LOCATIONWITHIMAGE = "api/location/locationUploadWithImage/";




    private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
    static final int DEFAULT_TIMEOUT = 20 * 1000;

    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {


        if (params == null) {
            params = new RequestParams();
        }
        Log.d("JSONParser", "************************************* START API *************************************");
        Log.d("JSONParser", "URL : " + getAbsoluteUrl(url));
        Log.d("JSONParser", params.toString());
        responseHandler.setTag(url);
        client.setURLEncodingEnabled(true);
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static boolean networkCheck(Context context)
    {
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


}
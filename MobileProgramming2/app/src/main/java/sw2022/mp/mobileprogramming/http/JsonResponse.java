package sw2022.mp.mobileprogramming.http;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class JsonResponse extends JsonHttpResponseHandler {
    private final static boolean DEBUG = true;
    private final static String TAG = "JsonResponse";

    private static void debugThrowable(String TAG, Throwable t) {
        if (t != null) {

        }
    }

    private static void debugResponse(String TAG, String response) {
        if (response != null) {
        }
    }

    private static void debugHeaders(String TAG, Header[] headers) {
        if (headers != null) {
            StringBuilder builder = new StringBuilder();
            for (Header h : headers) {
                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
                builder.append(_h);
                builder.append("\n");
            }
        }
    }

    private String getTagString(){
        if(getTag()!=null){
            return getTag().toString();
        }else
            return "";
    }

    private void getError(Header[] headers, String responseString, Throwable throwable){
        if(headers!=null) {
            debugHeaders(getTagString(), headers);
        }if(responseString!=null) {
            debugResponse(getTagString(), responseString);
        }if(throwable!=null) {
            debugThrowable(getTagString(), throwable);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        getError(headers, responseString, throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        String error = "";
        if(errorResponse!=null)
            error = errorResponse.toString();
        getError(headers, error, throwable);

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        String error = "";
        if(errorResponse!=null)
            error = errorResponse.toString();
        getError(headers, error, throwable);

    }
}

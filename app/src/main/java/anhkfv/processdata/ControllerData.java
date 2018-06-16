package anhkfv.processdata;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import anhkfv.infomation.detail.InfomationDetail;

public class ControllerData {
    public static final String TAG = "TAG";

    public static final String WAURL="https://script.google.com/macros/s/AKfycbzwkWLhOllZtivljGR0pP3euceTXUxyX282qM36luXTICrieOc/exec?";
    // EG : https://script.google.com/macros/s/AKfycbwXXXXXXXXXXXXXXXXX/exec?
//Make Sure '?' Mark is present at the end of URL
    private static Response response;

    public static JSONObject readAllData() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=readAll")
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }


    public static JSONObject insertData(String id, String name) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=insert&id="+id+"&name="+name)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject updateData(String id, InfomationDetail detail) {
        try {
            Log.d("Gia tri update: ", WAURL+"action=update&id="+id+"&money="+detail.getMoney()+"&idMoney="+detail.getIdMoney()+"&approval="+detail.getApproval()+"&info="+detail.getInfo());
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=update&id="+id+"&money="+detail.getMoney()+"&idMoney="+detail.getIdMoney()+"&approval="+detail.getApproval()+"&info="+detail.getInfo())
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject readData(String id) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=read&id="+id)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject deleteData(String id) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=delete&id="+id)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }
}

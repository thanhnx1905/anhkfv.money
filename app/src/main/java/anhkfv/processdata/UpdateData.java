package anhkfv.processdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.anhth.myapplication.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import anhkfv.infomation.detail.InfomationDetail;

public class UpdateData extends AsyncTask<Void, Void, Void> {
    ProgressDialog dialog;
    int jIndex;
    int x;

    String result=null;
    Context mContext;
    String id;
    InfomationDetail detail;

    public UpdateData(Context mContext, String id, InfomationDetail detail){
        this.mContext = mContext;
        this.id = id;
        this.detail = detail;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(mContext);
        dialog.setTitle("Hey Wait Please..."+x);
        dialog.setMessage("I am getting your JSON");
        dialog.show();

    }

    @Nullable
    @Override
    protected Void doInBackground(Void... params) {
        JSONObject jsonObject = ControllerData.updateData(id, detail);
        Log.i(ControllerData.TAG, "Json obj ");

        try {
            if (jsonObject != null) {

                result=jsonObject.getString("result");

            }
        } catch (JSONException je) {
            Log.i(ControllerData.TAG, "" + je.getLocalizedMessage());
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(dialog!= null) {
            dialog.dismiss();
        }
        Toast.makeText(mContext, result,
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }


}

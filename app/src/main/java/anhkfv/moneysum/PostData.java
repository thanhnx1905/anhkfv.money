package anhkfv.moneysum;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhth.myapplication.MainActivity;
import com.example.anhth.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import anhkfv.infomation.detail.InfomationDetail;
import anhkfv.infomation.detail.Person;
import anhkfv.person.DialogMultipleChoiceAdapter;
import anhkfv.processdata.ControllerData;
import anhkfv.processdata.UpdateData;


public class PostData extends AppCompatActivity {
    private ProgressDialog progress;


    EditText tInfo, tmoney;
    TextView tdate,tidMoney, tPerson;
    Button button;
    ImageButton imageDate, imgPerson;
    CheckBox checkApproval;
    String date, money, idMoney, info;
    boolean valueApproval;
    List<Person> itemList = new ArrayList<>();
    InfomationDetail infomation ;
    Intent i;
    boolean update, approval;
    private final  static  String DATE_FORMAT ="yyyy-MM-dd";
    private final  static SimpleDateFormat dateFomat = new SimpleDateFormat(DATE_FORMAT);
    Date currentDate = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        i = getIntent();
        itemList = (List<Person>) i.getSerializableExtra("persons");
        infomation = (InfomationDetail) i.getSerializableExtra("infomation");
        if(infomation != null){
            update = true;
        }else{
            update = false;
        }
        Toast.makeText(PostData.this, "so pt: "+itemList.size()+"", Toast.LENGTH_LONG).show();
        button = (Button)findViewById(R.id.btn_submit);
        tmoney = (EditText)findViewById(R.id.input_money);
        tidMoney = (TextView) findViewById(R.id.input_id_money);
        tPerson = (TextView) findViewById(R.id.input_person);
        tInfo = (EditText)findViewById(R.id.input_info);
        tdate = (TextView)findViewById(R.id.textDatePost);
        imageDate = (ImageButton)findViewById(R.id.imageDate);
        imgPerson = (ImageButton)findViewById(R.id.imagePerson);
        checkApproval = (CheckBox)findViewById(R.id.checkApproval);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(PostData.this);
        if( prefs.getInt("anhkfv", 0)== 1){
            approval = true;
        }else{
            checkApproval.setEnabled(false);
        }
        if(update){
            tmoney.setText(infomation.getMoney().toString());
            tidMoney.setText(infomation.getIdMoney());
            tPerson.setText(infomation.getPersonName());
            tInfo.setText(infomation.getInfo());
            tdate.setText(dateFomat.format(infomation.getDate()));
            checkApproval.setChecked(infomation.getApproval().equals("1") ? true: false);
            imageDate.setEnabled(false);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = tdate.getText().toString();
                money = tmoney.getText().toString();
                idMoney = tidMoney.getText().toString();
                info = tInfo.getText().toString();
                valueApproval = checkApproval.isChecked();
                if(idMoney.equals("") || money.equals("") || info.equals("")){
                    Toast.makeText(PostData.this, "Vui Lòng Nhập Tất cả Cột", Toast.LENGTH_LONG).show();
                }else {
                    if(update){
                        InfomationDetail detailTemp = infomation;
                        detailTemp.setMoney(Float.parseFloat(tmoney.getText().toString()));
                        detailTemp.setIdMoney(tidMoney.getText().toString());
                        detailTemp.setApproval(valueApproval ? "1" : "0");
                        detailTemp.setInfo(tInfo.getText().toString());
                        new UpdateDataCommon(PostData.this, infomation.getKeyRandom(), detailTemp).execute();
                    }else {
                        new SendRequest().execute();
                    }
                }

            }

        }   );


        if(infomation != null){
            currentDate = infomation.getDate();
        }
        tdate.setText((1900+currentDate.getYear()) + "-" + (currentDate.getMonth()+1) + "-" +currentDate.getDate() );
        imageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PostData.this,
                        android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        tdate.setText(year + "-" + (monthOfYear + 1) + "-" +dayOfMonth );
                    }
                }, 1900+currentDate.getYear(), currentDate.getMonth(), currentDate.getDate());
                datePickerDialog.show();
            }
        });

        imgPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // public void show() {
                    final DialogMultipleChoiceAdapter adapter =
                            new DialogMultipleChoiceAdapter(PostData.this, itemList);

                    new AlertDialog.Builder(PostData.this).setTitle("Chọn người")
                            .setAdapter(adapter, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String textPer="", textName = "";
                                    for(Person p : adapter.getCheckedItem()){
                                        String personId = "" ;
                                        String personName = "";
                                        if(p.isCheckAll()){
                                            personId = p.getPersonId();
                                            personName = p.getPersonName();
                                        }

                                        if(p.isCheckOne()){
                                            personId = p.getPersonId()+"*";
                                            personName = p.getPersonName()+"*";
                                        }
                                        textPer += personId+"_";
                                        textName += personName+", ";
                                    }
                                    tidMoney.setText(textPer);
                                    tPerson.setText(textName);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }

           // }
        });
    }

    public PostData() {
        super();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(PostData.this, MainActivity.class);
                this.startActivity(intent);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        protected void onPreExecute(){
            dialog = new ProgressDialog(PostData.this);
            // dialog.setTitle("Hey Wait Please..."+x);
            dialog.setMessage("Chờ load dữ liệu ....");
            dialog.show();
        }

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("https://script.google.com/macros/s/AKfycbzsKHT5UqDYA5bAes-czUizuHMPSej3Ni7liYln3O7uptptFlFv/exec");
                JSONObject postDataParams = new JSONObject();
                String id= "17KDSX9sX6EZitMEH7zF2X4EdjS4LZsLaLtxw_9EDxK8";

                postDataParams.put("date", date);
                postDataParams.put("money",money);
                postDataParams.put("idMoney",idMoney);
                postDataParams.put("approval", approval ? (valueApproval ? 1 : 0) : 0);
                postDataParams.put("info", info);
                postDataParams.put("id", id);
                postDataParams.put("keyRandom", System.currentTimeMillis());


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    class UpdateDataCommon extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        int jIndex;
        int x;

        String result = null;
        Context mContext;
        String id;
        InfomationDetail detail;

        public UpdateDataCommon(Context mContext, String id, InfomationDetail detail) {
            this.mContext = mContext;
            this.id = id;
            this.detail = detail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Đang load dữ liệu ...!");
            dialog.show();

        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = ControllerData.updateData(id, detail);
            Log.i(ControllerData.TAG, "Json obj ");

            try {
                if (jsonObject != null) {

                    result = jsonObject.getString("result");

                }
            } catch (JSONException je) {
                Log.i(ControllerData.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) {
                dialog.dismiss();
            }
            Toast.makeText(mContext, result,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            finish();
        }
    }
    }

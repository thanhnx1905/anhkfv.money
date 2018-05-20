package anhkfv.moneysum;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhth.myapplication.MainActivity;
import com.example.anhth.myapplication.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import anhkfv.infomation.detail.Person;
import anhkfv.person.DialogMultipleChoiceAdapter;


public class PostData extends AppCompatActivity {
    private ProgressDialog progress;


    EditText tInfo, tmoney;
    TextView tdate,tidMoney, tPerson;
    Button button;
    ImageButton imageDate, imgPerson;
    String date, money, idMoney, info;
    List<Person> itemList = new ArrayList<>();
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
         i = getIntent();
        itemList = (List<Person>) i.getSerializableExtra("persons");
        Toast.makeText(PostData.this, "so pt: "+itemList.size()+"", Toast.LENGTH_LONG).show();
        button=(Button)findViewById(R.id.btn_submit);
        tmoney=(EditText)findViewById(R.id.input_money);
        tidMoney=(TextView) findViewById(R.id.input_id_money);
        tPerson=(TextView) findViewById(R.id.input_person);
        tInfo=(EditText)findViewById(R.id.input_info);
        tdate = (TextView)findViewById(R.id.textDatePost);
        imageDate = (ImageButton)findViewById(R.id.imageDate);
        imgPerson = (ImageButton)findViewById(R.id.imagePerson);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = tdate.getText().toString();
                money = tmoney.getText().toString();
                idMoney = tidMoney.getText().toString();
                info = tInfo.getText().toString();

                new SendRequest().execute();

            }

        }   );

        Date currentDate = new Date();
        tdate.setText((1900+currentDate.getYear()) + "-" + (currentDate.getMonth()+1) + "-" +currentDate.getDate() );
        imageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PostData.this,
                        android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        tdate.setText(year + "-" + (monthOfYear + 1) + "-" +dayOfMonth );
//                        mYear = year;
//                        mMonth = monthOfYear;
//                        mDay = dayOfMonth;
                    }
                }, 2018, 01, 01);
                datePickerDialog.show();
            }
        });

        imgPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // public void show() {
//                    if (itemList.isEmpty()) {
//                        itemList.add(new Person("A", "1_2"));
//                        itemList.add(new Person("B", "1_3"));
//                        itemList.add(new Person("C", "2_3"));
//                    }

                    final DialogMultipleChoiceAdapter adapter =
                            new DialogMultipleChoiceAdapter(PostData.this, itemList);

                    new AlertDialog.Builder(PostData.this).setTitle("Select Image")
                            .setAdapter(adapter, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Toast.makeText(PostData.this,
//                                            "getCheckedItem = " + adapter.getCheckedItem(),
//                                            Toast.LENGTH_SHORT).show();
                                    String textPer="", textName = "";

                                    for(Person p : adapter.getCheckedItem()){
                                        textPer += p.getPersonId()+"_";
                                        textName += p.getPersonName()+", ";
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
                // https://script.google.com/macros/s/AKfycbyuAu6jWNYMiWt9X5yp63-hypxQPlg5JS8NimN6GEGmdKZcIFh0/exec
                JSONObject postDataParams = new JSONObject();

                //int i;
                //for(i=1;i<=70;i++)


                //    String usn = Integer.toString(i);

                String id= "17KDSX9sX6EZitMEH7zF2X4EdjS4LZsLaLtxw_9EDxK8";

                postDataParams.put("date", date);
                postDataParams.put("money",money);
                postDataParams.put("idMoney",idMoney);
                postDataParams.put("approval","0");
                postDataParams.put("info", info);
                postDataParams.put("id", id);


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
}

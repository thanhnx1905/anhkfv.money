package com.example.anhth.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anhkfv.infomation.detail.InfomationDetail;
import anhkfv.infomation.detail.Person;
import anhkfv.moneysum.PostData;

public class CalcMoney  extends AppCompatActivity {
    TextView startDate, endDate, tmoneyPer, tMoneyGroup;
    ImageButton btnStart, btnEnd, process;
    List<InfomationDetail> infomations;
    List<Person> persons;
    String textCalcPerson = "",  textGroupPerson = "";
    Date startDateCurrent, endDateCurrent;
    private final  static  String DATE_FORMAT ="yyyy-MM-dd";
    private final  static SimpleDateFormat dateFomat = new SimpleDateFormat(DATE_FORMAT);

    Map<String, Float> moneyPerson = new HashMap<>();
    Map<String, Person> personMap = new HashMap<>();
    Map<String, Float> moneyGroup = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calcmoney);

        startDate = (TextView)findViewById(R.id.textStartDate);
        endDate = (TextView)findViewById(R.id.textEndDate);
        tmoneyPer = (TextView)findViewById(R.id.textMoneyPer);
        tMoneyGroup = (TextView)findViewById(R.id.textMoneyGroup);

        btnStart = (ImageButton)findViewById(R.id.imageStartDate);
        btnEnd = (ImageButton)findViewById(R.id.imageEndDate);
        process = (ImageButton)findViewById(R.id.imageProcess);

        persons = (List<Person>) getIntent().getSerializableExtra("persons");
        infomations = (List<InfomationDetail>) getIntent().getSerializableExtra("infomation");
        for(Person data : persons){
            personMap.put(data.getPersonId(), data);
        }

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textCalcPerson ="";
                textGroupPerson ="";
                calc(infomations, startDateCurrent, endDateCurrent);
                for (String key : moneyPerson.keySet()) {
                    textCalcPerson += personMap.get(key).getPersonName()+":"+ moneyPerson.get(key)+ "K    |";
                }
                tmoneyPer.setText(textCalcPerson);

                calcGroup(moneyPerson, personMap);
                for (String key : moneyGroup.keySet()) {
                    textGroupPerson += key+":"+ moneyGroup.get(key)+ "K    |";
                }
                tMoneyGroup.setText(textGroupPerson);
            }
        });
        startDateCurrent = newCurrentDate();
        endDateCurrent = endCurent();
        Toast.makeText(CalcMoney.this, "startDate: "+ endDateCurrent.toString(), Toast.LENGTH_LONG).show();
        startDate.setText((1900+startDateCurrent.getYear()) + "-" + (startDateCurrent.getMonth()+1) + "-" +(startDateCurrent.getDate()) );
        endDate.setText((1900+endDateCurrent.getYear()) + "-" + (endDateCurrent.getMonth()+1) + "-" +endDateCurrent.getDate() );
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CalcMoney.this,
                        android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        startDate.setText(year + "-" + (monthOfYear + 1) + "-" +dayOfMonth );
                        try {
                            startDateCurrent = dateFomat.parse(startDate.getText().toString());
                            Toast.makeText(CalcMoney.this, "startDate: "+ startDateCurrent.toString(), Toast.LENGTH_LONG).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1900+startDateCurrent.getYear(), startDateCurrent.getMonth(), startDateCurrent.getDate());
                datePickerDialog.show();
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CalcMoney.this,
                        android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        endDate.setText(year + "-" + (monthOfYear + 1) + "-" +dayOfMonth );
                        try {
                            endDateCurrent = dateFomat.parse(endDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1900+endDateCurrent.getYear(), endDateCurrent.getMonth(), endDateCurrent.getDate());
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(CalcMoney.this, MainActivity.class);
                this.startActivity(intent);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void calc(List<InfomationDetail> infomations, Date startDate, Date endDate){
        moneyPerson = new HashMap<>();
        for(InfomationDetail value : infomations){
            Log.d("gia tri datecurent: ", value.getDate()+" |"+ startDate.toString()+ " |" + endDate.toString());

            if((value.getDate().after(startDate) || equalDate(value.getDate(), startDate)) && (value.getDate().before(endDate) ||  equalDate(value.getDate(), endDate))) {
                Log.d("vao: ", "ok");
                String[] ids = value.getIdMoney().split("_");
                int countSpecial = StringUtils.countMatches(value.getIdMoney(), "*");
                int countAll = ids.length*2 - countSpecial;
                for (int i = 0; i < ids.length; i++) {
                    boolean containChar = ids[i].contains("*");
                    String key = containChar ?  ids[i].split("\\*")[0]: ids[i];
                    if (moneyPerson.containsKey(key)) {
                        moneyPerson.put(key, moneyPerson.get(key) + (containChar ? (value.getMoney() / countAll) : (value.getMoney() / countAll)*2));
                    } else {
                        moneyPerson.put(key, containChar ? (value.getMoney() / countAll) : (value.getMoney() / countAll)*2);
                    }
                }
            }
        }
        return ;
    }

    private void calcGroup(Map<String, Float> infoMap, Map<String, Person> personMap){
        moneyGroup = new HashMap<>();
        for (String key : infoMap.keySet()) {
            String group = personMap.get(key).getGroup();
            if(moneyGroup.containsKey(group)){
                moneyGroup.put(group, infoMap.get(key)+ moneyGroup.get(group)) ;
            }else{
                moneyGroup.put(group, infoMap.get(key)) ;
            }
        }

    }
    private String getListIdPerson(String idMoney){
        return null;
    }

    private Date newCurrentDate (){
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        return now.getTime();
    }

    private Date endCurent(){
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.HOUR_OF_DAY, 23);
        return now.getTime();
    }

    private boolean equalDate(Date select, Date compare){
        if(select.getYear() == compare.getYear()
                && select.getMonth() == compare.getMonth()
                && select.getDate() == compare.getDate()){
            return true;
        }
        return false;
    }
}

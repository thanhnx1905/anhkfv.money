package com.example.anhth.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anhkfv.infomation.detail.InfomationDetail;
import anhkfv.infomation.detail.InfomationDetailAdapter;
import anhkfv.infomation.detail.Person;
import anhkfv.internet.InternetConnection;
import anhkfv.money.database.JSONDetailParser;
import anhkfv.money.database.Keys;
import anhkfv.moneysum.PostData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final  static  String DATE_FORMAT ="yyyy-MM-dd";
    private final  static SimpleDateFormat dateFomat = new SimpleDateFormat(DATE_FORMAT);

    private List<InfomationDetail> movieList = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();
    private Map<String, Person> personMap = new HashMap<>();
    private RecyclerView recyclerView;
    private InfomationDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent = new Intent(getApplicationContext(), PostData.class);
                intent.putExtra("persons", (Serializable)personList);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new InfomationDetailAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
       // loadDataServer();
    }
    private void loadDataServer() {
        if (InternetConnection.checkConnection(getApplicationContext())) {
            movieList.clear();
            new GetDataTask().execute();
        } else {
            Toast.makeText(MainActivity.this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this, "ssss", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
       // movieList.clear();
        loadDataServer();
    }

    /**
     * Creating Get Data Task for Getting Data From Web
     */
    class GetDataTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        int jIndex;
        int jIndPer;
        int x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
//            movieList = new ArrayList<>();
//            x=movieList.size();
//
//            if(x==0)
//                jIndex=0;
//            else
//                jIndex=x;

            dialog = new ProgressDialog(MainActivity.this);
           // dialog.setTitle("Hey Wait Please..."+x);
            dialog.setMessage("Chờ load dữ liệu ....");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            JSONObject jsonObject = JSONDetailParser.getDataFromWeb();

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {
                    /**
                     * Check Length...
                     */
                    if(jsonObject.length() > 0) {
                        /**
                         * Getting Array named "contacts" From MAIN Json Object
                         */
                        JSONArray arrayPerson = jsonObject.getJSONArray("persons");
                        int lenArrayPer = arrayPerson.length();
                        Person persTemp;
                        jIndPer = 0;
                        for( ; jIndPer < lenArrayPer; jIndPer++) {
                            JSONObject innerObject = arrayPerson.getJSONObject(jIndPer);
                            String personName = innerObject.getString(Keys.PERSON_KEY_NAME);
                            String personId = innerObject.getString(Keys.PERSON_KEY_ID);
                            persTemp = new Person(personName, personId);
                            personList.add(persTemp);
                            personMap.put(personId, persTemp);
                        }

                        JSONArray array = jsonObject.getJSONArray("records");
                        int lenArray = array.length();
                        if(lenArray > 0) {
                            for( ; jIndex < lenArray; jIndex++) {
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String date = innerObject.getString(Keys.SUM_KEY_DATE);
                                String idMoney = innerObject.getString(Keys.SUM_KEY_ID_MONEY);
                                //String person = innerObject.getString(Keys.SUM_KEY_ID_MONEY);
                                String money = innerObject.getString(Keys.SUM_KEY_MONEY);
                                String approval = innerObject.getString(Keys.SUM_KEY_APPROVAL);
                                String info = innerObject.getString(Keys.SUM_KEY_INFO);
                                try {
                                    //dateFomat.setTimeZone();
                                    Date dateT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(date.replaceAll("Z$", "+0000"));
                                    movieList.add(InfomationDetail.createInfoDetail(dateT, namePerson(idMoney), idMoney, Float.parseFloat(money), approval, info));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(JSONDetailParser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        private String namePerson(String idMoney){
            String [] id = idMoney.split("_");
            String name = "";
            for(int i = 0; i< id.length; i++){
                Person per = personMap.get(id[i]);
                name += (per == null ? "" : per.getPersonName()) +", ";
            }
          return name;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            /**
             * Checking if List size if more than zero then
             * Update ListView
             */
            if(movieList.size() > 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                //Snackbar.make(findViewById(R.id.parentLayout), "No Data Found", Snackbar.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "NO FOUND DATA", Toast.LENGTH_LONG).show();
            }
        }
    }
}



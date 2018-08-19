package com.example.navneet.myjsonparser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private ListView lv;

    //Url to get the contacts JSON
    private static String Url = "https://api.androidhive.info/contacts/";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = findViewById(R.id.lv_list);
        new GetContacts().execute();
    }

    /*
     * Asnyc Task Class to get json by making http call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing Progress Dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MyHttpHandler sh = new MyHttpHandler();

            //Making a request to url and receiving the response
            String jsonStr = sh.makeServiceCall(Url);
            Log.e(TAG, "doInBackground: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    //Getting JSON Array
                    JSONArray contacts = jsonObject.getJSONArray("contacts");

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject object = contacts.getJSONObject(i);

                        String id = object.getString("id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                        String address = object.getString("address");
                        String gender = object.getString("gender");

                        //Phone node is containg another object

                        JSONObject phone = object.getJSONObject("phone");

                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        //Hashmap for storing each contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        contactList.add(contact);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "doInBackground: "+e.getMessage() );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "JSON Parsing error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
            else{
                Log.e(TAG, "Couldn't get json from server" );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Couldn't get json from server Check logcat for possible errors!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //dismiss the progressDialog
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            /*
             * Updating the parsed json in the listview
             */
            ListAdapter adapter=new SimpleAdapter(MainActivity.this,
                    contactList,R.layout.list_item,
                    new String[]{"name","email","mobile"},
                    new int[]{R.id.name,R.id.email,R.id.mobile});
            lv.setAdapter(adapter);
        }
    }


}

package com.example.employeedirectoryapplication;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import adapter.EmployeeListAdapter;
import helper.DBHelper;
import model.EmployeeModel;

public class MainActivity extends AppCompatActivity  {
    RecyclerView employeeRecyclerView;
    String url = "http://www.mocky.io/v2/5d565297300000680030a986";

    ProgressBar progressBar;
    DBHelper dbHelper;
    ArrayList<EmployeeModel> employeeList;
    EmployeeListAdapter employeeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeeRecyclerView = (RecyclerView) findViewById(R.id.employee_recyclerView);
        employeeRecyclerView.setHasFixedSize(true);
        employeeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employeeList = new ArrayList();

        dbHelper = new DBHelper(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new FetchDataTask().execute(url);
        progressBar.setVisibility(View.VISIBLE);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                InputStream inputStream = null;
                String result = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);

                try {

                    HttpResponse response = client.execute(httpGet);
                    inputStream = response.getEntity().getContent();

                    // convert inputstream to string
                    if (inputStream != null) {
                        result = convertInputStreamToString(inputStream);
                        Log.i("App", "Data received:" + result);
                    } else
                        result = "Failed to fetch data";

                    return result;

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String jsonString) {
                //parse the JSON data and then display
                storeJsonData(jsonString);
            }
        }


    private String convertInputStreamToString(InputStream inputStream) throws IOException{
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                String line = "";
                String result = "";
                while((line = bufferedReader.readLine()) != null)
                    result += line;

                inputStream.close();
                return result;
    }

    void storeJsonData(String jsonString) {
        dbHelper.insertJsonString(jsonString);
        parseJsonData();
        progressBar.setVisibility(View.INVISIBLE);
    }

    void parseJsonData() {
        Cursor rs = dbHelper.getAllData();
        rs.moveToFirst();

        String jsonString = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));

        if (!rs.isClosed()) {
        rs.close();
        }

         try {
            JSONArray  jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                EmployeeModel employeeModel = new EmployeeModel();

                URL url = new URL(jsonObject.getString("profile_image"));
                employeeModel.urlImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                employeeModel.name = jsonObject.getString("name");
                employeeModel.userName = jsonObject.getString("username");
                employeeModel.email = jsonObject.getString("email");

                JSONObject address = jsonObject.getJSONObject("address");
                employeeModel.street = address.getString("street");
                employeeModel.suite = address.getString("suite");
                employeeModel.city = address.getString("city");
                employeeModel.zipcode = address.getString("zipcode");

                employeeModel.phone = jsonObject.getString("phone");
                employeeModel.website = jsonObject.getString("website");

                JSONObject company = jsonObject.getJSONObject("company");
                employeeModel.companyName = company.getString("name");

                employeeList.add(employeeModel);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        employeeListAdapter = new EmployeeListAdapter(this, employeeList);
        employeeRecyclerView.setAdapter(employeeListAdapter);

    }
}
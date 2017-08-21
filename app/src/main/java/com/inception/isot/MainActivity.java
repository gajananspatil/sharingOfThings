package com.inception.isot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;
import java.util.Iterator;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG,"onCreate, Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter( getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPages(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //autoLogin();

    }

    private void setupViewPages(ViewPager viewPager)
    {
        mSectionsPageAdapter.addFragment(new Tab1MyThings(), "MyThings");
        mSectionsPageAdapter.addFragment(new Tab2Groups(), "Groups");

        viewPager.setAdapter(mSectionsPageAdapter);
    }

    public void addViewPage(ViewPager viewPager, Fragment fragment, String name)
    {
        SectionsPageAdapter adapter = (SectionsPageAdapter) viewPager.getAdapter();
        adapter.addFragment(fragment,name);
        adapter.notifyDataSetChanged();
    }

    public void addViewPage(int position, ViewPager viewPager, Fragment fragment, String name)
    {
        SectionsPageAdapter adapter = (SectionsPageAdapter) viewPager.getAdapter();
        adapter.addFragment(position, fragment,name);
        adapter.notifyDataSetChanged();
    }


    private void autoLogin()
    {
        SharedPreferences preferences = getBaseContext().getSharedPreferences(getString(R.string.shared_preference),MODE_PRIVATE);
        String userName = preferences.getString("username",null);
        String password = preferences.getString("password",null);

        if( userName == null)
        {
            startActivity( new Intent(this, LoginActivity.class));
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if( userName != null && password != null) {
            params.add("userMobileNo", userName);
            params.add("password", password);

            String serverIp = getResources().getString(R.string.serviceURL);

            final ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
            Dialog.setMessage("Please wait..");
            Dialog.show();
            // params.p
            client.post("http://139.59.57.136:8080/backend-service-management/api/userLogin",
                //serverIp + "userLogin",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code
                    // '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Dialog.dismiss();
                        // Hide Progress Dialog
                        try {
                            // JSON Object
                            String resp = new String(responseBody);
                            JSONObject userObj = new JSONObject(resp);
                            SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            Iterator<String> iter = userObj.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                String value = userObj.getString(key);
                                editor.putString(key, value);
                            }

                            editor.commit();
                            //LoginActivity.setUserLogin(true);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occurred [Server's JSON response might be invalid]!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    // When the response returned by REST has Http response code
                    // other than '200'
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {

                        Dialog.dismiss();

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        else if (statusCode == 400) {
                            String resp = new String(responseBody);
                            Toast.makeText(getApplicationContext(),
                                    resp, Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
                                    Toast.LENGTH_LONG).show();
                        }
                        try {
                            Thread.sleep(3500);
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                        } catch (Exception e) {

                        }
                    }
                });
        }
    }

}

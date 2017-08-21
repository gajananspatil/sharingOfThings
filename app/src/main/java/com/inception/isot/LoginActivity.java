package com.inception.isot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText mLoginIdView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginIdView = (EditText) findViewById(R.id.loginId);
        mPasswordView = (EditText) findViewById(R.id.password);

        View mRegisterView = findViewById(R.id.link_to_register);
        mRegisterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"User redirected to Register activity");

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    private void doUserLogin()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userMobileNo", mLoginIdView.getText().toString());
        params.add("password", mPasswordView.getText().toString());

        String serverIp = getResources().getString(R.string.serviceURL);

        final ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        Dialog.setMessage("Please wait..");
        Dialog.show();
        // params.p
        client.post( "http://139.59.57.136:8080/backend-service-management/api/userLogin",
                //serverIp + "userLogin",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code
                    // '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            // Hide Progress Dialog
                            Dialog.dismiss();
                            // JSON Object
                            String resp = new String( responseBody );
                            JSONObject userObj = new JSONObject(resp);
                            SharedPreferences preferences = getSharedPreferences( getString(R.string.shared_preference), MODE_PRIVATE );
                            SharedPreferences.Editor editor = preferences.edit();

                            Iterator<String> iter = userObj.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();

                                String value = userObj.getString(key);
                                editor.putString(key,value);
                            }

                            editor.commit();
                            //isLoggedIn = true;
                            Toast.makeText(
                                    getApplicationContext(),
                                    " You are successfully logged in!",
                                    Toast.LENGTH_LONG).show();

                            Thread.sleep(3500);

                            Intent complaintsListIntent = new Intent(
                                    getApplicationContext(),
                                    MainActivity.class);
                            complaintsListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(complaintsListIntent);
                            finish();
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured [Server's JSON response might be invalid]!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }

                    }

                    // When the response returned by REST has Http response code
                    // other than '200'
                    @Override
                    public void onFailure( int statusCode, Header[] headers, byte[] responseBody, Throwable throwable )
                    {
                        String resp = new String(responseBody);
                        Dialog.dismiss();
                        // Hide Progress Dialog
                        // prgDialog.hide();
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
                        else if (statusCode == 400)
                        {
                            try {
                                JSONObject msgOjb = new JSONObject(resp);
                                String msg = msgOjb.getString("message");
                                Toast.makeText(getApplicationContext(),
                                        "Login Failed due to "+msg,
                                        Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException je)
                            {
                                Log.e(TAG,"Failed to parse error json:"+je.getMessage());
                                Toast.makeText(getApplicationContext(),
                                        "Login Failed due", Toast.LENGTH_LONG).show();
                            }

                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

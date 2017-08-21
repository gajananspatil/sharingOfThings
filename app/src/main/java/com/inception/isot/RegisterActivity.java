package com.inception.isot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity {
    private static final  String TAG = "RegisterActivity";
    private Integer minPassLen ;

    TextView vFirstName ;
    TextView vLastName ;
    TextView vMobile ;
    TextView vPassword ;
    TextView vConfirmPassword ;
    TextView vEmailId ;
    TextView vAddress ;
    Animation animShake;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setTitle(getString(R.string.register_activity_title));
        setContentView( R.layout.activity_regsiter);

        vFirstName      = (TextView) findViewById( R.id.SignUp_FirstName );
        vLastName       = (TextView) findViewById( R.id.SignUp_LastName );
        vMobile         = (TextView) findViewById( R.id.SignUp_MobileNo );
        vPassword       = (TextView) findViewById( R.id.SignUp_password );
        vConfirmPassword= (TextView) findViewById( R.id.SignUp_confPassword );
        vEmailId        = (TextView) findViewById( R.id.SignUp_Email );
        vAddress        = (TextView) findViewById( R.id.SignUp_Address);

        animShake = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.shake );
    }

    public void resetUIFields()
    {
        vFirstName.setText("");
        vLastName.setText("");
        vMobile.setText("");
        vPassword.setText("");
        vConfirmPassword.setText("");
        vEmailId.setText("");
        vAddress.setText("");
    }

    public boolean validateSignUpFrom()
    {

        Log.d( TAG, "Validate Sign Up Form details");

        String name = vFirstName.toString().trim();

        if( name.isEmpty() )
        {
            vFirstName.setError( "Enter Valid First Name" );
            vFirstName.setAnimation(animShake);
            vFirstName.startAnimation(animShake);

            vFirstName.requestFocus();
            return  false;
        }

        String lastName = vLastName.toString().trim();

        if( name.isEmpty() )
        {
            vLastName.setError( "Enter Valid Last Name" );
            vLastName.setAnimation(animShake);
            vLastName.startAnimation(animShake);

            vLastName.requestFocus();
            return  false;
        }

        String mobileNo = vMobile.toString();
        if( mobileNo.isEmpty() || mobileNo.length() < getResources().getInteger( R.integer.mobileNoLength )  )
        {
            vMobile.setError("Enter valid mobile Number");
            vMobile.setAnimation(animShake);
            vMobile.startAnimation(animShake);

            vMobile.requestFocus();
            return false;
        }
        String email = vEmailId.toString().trim();
        if( email.isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() )
        {
            vEmailId.setError("Enter valid email id");
            vEmailId.setAnimation(animShake);
            vEmailId.startAnimation(animShake);

            vEmailId.requestFocus();
            return false;
        }


        minPassLen = getResources().getInteger( R.integer.minPasswordLength);
        String password = vPassword.toString().trim();

        if( password.isEmpty() || password.length() < minPassLen )
        {
            vPassword.setError("Password must be at least 6 characters long");
            vPassword.setAnimation(animShake);
            vPassword.startAnimation(animShake);

            vPassword.requestFocus();
            return false;
        }

        String confPassword = vConfirmPassword.toString().trim();

        if( confPassword.isEmpty() || confPassword.length() < minPassLen )
        {
            vConfirmPassword.setError("Password must be at least 6 characters long");
            vConfirmPassword.setAnimation(animShake);
            vConfirmPassword.startAnimation(animShake);

            vConfirmPassword.requestFocus();
            return false;
        }

        if( !password.equals( confPassword ))
        {
            vConfirmPassword.setError("Passwords do not match");
            vConfirmPassword.setAnimation(animShake);
            vConfirmPassword.startAnimation(animShake);

            vConfirmPassword.requestFocus();
            return false;
        }

        String address =  vAddress.toString().trim();
        if( address.isEmpty() )
        {
            vAddress.setError( "Enter valid address" );
            vAddress.requestFocus();
            return  false;
        }

        return true;
    }
    /** Called when the user clicks the Submit button */
    public void signUpSubmit(View view) {


        AsyncHttpClient client = new AsyncHttpClient();
        String firstName = "";
        StringEntity entity;
        try
        {
            JSONObject params = new JSONObject();

            params.put( "username", vMobile);
            params.put( "firstName",vFirstName.toString().trim());
            params.put( "lastName", vLastName.toString().trim() );
            params.put( "password", vPassword.toString().trim());
            params.put( "emailId", vEmailId.toString().trim());
            params.put( "mobileNo", vMobile);
            params.put( "address", vAddress.toString().trim());

            firstName = vFirstName.getText().toString().trim();
            entity = new StringEntity( params.toString(),"UTF-8");
        }
        catch (JSONException json)
        {
            Log.e(TAG,"JSON Exception:"+json.getMessage());
            Toast.makeText( getApplicationContext(),
                    firstName + ", Error in processing details, try again.",
                    Toast.LENGTH_LONG ).show();
            resetUIFields();
            return;
        }

        String serverUrl = getResources().getString(R.string.serviceURL);

        final ProgressDialog Dialog = new ProgressDialog(RegisterActivity.this);
        Dialog.setMessage("Please wait..");
        Dialog.show();

        final String finalFirstName = firstName;
        client.post( getApplicationContext(), "http://139.59.57.136:8080/backend-service-management/api/userRegistration",
                entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

                // When the response returned by REST has Http response code
                // '200'
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Dialog.dismiss();
                    // Hide Progress Dialog

                    Toast.makeText( getApplicationContext(),
                            finalFirstName + " Registration successful !",
                            Toast.LENGTH_LONG ).show();
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {

                    }
                    Intent LoginIntent = new Intent( getApplicationContext(), LoginActivity.class);
                    LoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);

                }

                // When the response returned by REST has Http response code
                // other than '200'
                @Override
                public void onFailure( int statusCode, Header[] headers, byte[] responseBody, Throwable throwable )
                {

                    Dialog.dismiss();
                    resetUIFields();
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
                            String resp = new String(responseBody);
                            JSONObject errorMsg = new JSONObject(resp);
                            Toast.makeText(getApplicationContext(),
                                    resp,Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException exp)
                        {
                            Log.e(TAG,"Register call exception: "+exp.getMessage());
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected Error occurred,[Server's response might be invalid]! ",
                                    Toast.LENGTH_LONG).show();
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

package com.example.dsouchon.myapplication;

import android.content.pm.ActivityInfo;
import android.hardware.TriggerEvent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;


public class MainLogin extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(MainLogin.this,"Click Exit App button to exit", Toast.LENGTH_SHORT).show();




    }

    public void browser1(View view){

        Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.miid.co.zw/EventOrganisers/Register"));
        startActivity(browserIntent);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        setContentView(R.layout.activity_login);

        ProgressBar mprogressbar;
        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
        mprogressbar.setVisibility(View.GONE);





       /* this directs user to mainactivity if they are logged in ,still under testing

        if(Local.isSet(getApplicationContext(), "LoggedIn"))

        {
            String loggedInUser = Local.Get(getApplicationContext(), "LoggedIn");
            if (loggedInUser.length() > 0) {

                Intent intent = new Intent(MainLogin.this, MainActivity.class);


                startActivity(intent);
            }
        }  */



    }


    public void closeApplication(View view) {
        moveTaskToBack (true);

    }


    //makes app login go through to events
    public void setupEvent(View view) {
        Intent intent = new Intent(MainLogin.this, SetupEvent.class );
        finish();
        startActivity(intent);
    }




/* //this is the actionbar menue

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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

        if (id == R.id.Logoffmenu){
            startActivity(new Intent(this,MainLogin.class));
        }

        return super.onOptionsItemSelected(item);
    }

*/

    public void accessControl(View view) {
        Button buttonEventSetup = (Button)findViewById(R.id.buttonEventSetup);
        buttonEventSetup.setVisibility(View.GONE);

        Intent intent = new Intent(MainLogin.this, MainActivity21.class );
        finish();
        startActivity(intent);
    }

    public void Logoff(View view) {

        Local.Set(getApplicationContext(), "LoggedIn", "");


    }
    public void Login(View view) {
        final AlertDialog ad=new AlertDialog.Builder(this).create();
        MySOAPCallActivity cs = new MySOAPCallActivity();

        ProgressBar mprogressbar;
        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
        mprogressbar.setVisibility(View.VISIBLE);



        try {
            EditText userName = (EditText) findViewById(R.id.editUserName);
            EditText password = (EditText) findViewById(R.id.editPassword);
            String user = userName.getText().toString();
            String pwd = password.getText().toString();
            LoginParams params = new LoginParams(cs, user, pwd);

            Local.Set(getApplicationContext(), "UserName", user);
            Local.Set(getApplicationContext(), "Password", pwd);

            new CallSoapLogin().execute(params);
            // new CallSoapGetCurrentEvents().execute(params);

        } catch (Exception ex) {
            ad.setTitle("Error!");
            ad.setMessage(ex.toString());
        }
        ad.show();
    }

    public class CallSoapLogin extends AsyncTask<LoginParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(LoginParams... params) {
            return params[0].foo.Login(params[0].username, params[0].password);
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            try {

                TextView loginResult =(TextView)findViewById(R.id.labelLoginResult);
                loginResult.setVisibility(View.VISIBLE);
                loginResult.setText(result);

                // Button buttonUnsetEvent = (Button)findViewById(R.id.buttonUnsetEvent);
                // buttonUnsetEvent.setEnabled(true);

                //Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
                //spinner2.setEnabled(true);

                boolean LoginSuccessful = false;

                ProgressBar mprogressbar;
                mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                mprogressbar.setVisibility(View.GONE);

                if(result.toLowerCase().contains("success"))
                {
                    LoginSuccessful = true;
                }

                if (LoginSuccessful)
                {
                    String user = Local.Get(getApplicationContext(), "UserName");
                    Local.Set(getApplicationContext(), "LoggedIn", user);


                    //this directs people to the set event activity once login is succesfull
                    Intent intent = new Intent(MainLogin.this, SetupEvent.class );
                    startActivity(intent);





                }



            } catch (Exception ex) {
                String e3 = ex.toString();
            }

        }



    }
    private static class LoginParams {
        MySOAPCallActivity foo;
        String username;
        String password;


        LoginParams(MySOAPCallActivity foo, String username, String password) {
            this.foo = foo;
            this.username = username;
            this.password = password;

        }
    }
}

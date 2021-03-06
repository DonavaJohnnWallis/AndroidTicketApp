package com.example.dsouchon.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class BarcodeScanner extends AppCompatActivity {





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

        if (id == R.id.homebutton){
            startActivity(new Intent(this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }




    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);





        //hides progress bar

        ProgressBar mprogressbar;
        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
        mprogressbar.setVisibility(View.INVISIBLE);

        Button scanbutton = (Button)findViewById(R.id.buttonScanTag);
        scanbutton.setVisibility(View.GONE);


        //Make yes no buttons invisible

        Button buttonNo = (Button)findViewById(R.id.buttonNo);

        buttonNo.setVisibility(View.INVISIBLE);



        if(Local.isSet(getApplicationContext(), "EventName")) {
            TextView textEvent = (TextView) findViewById(R.id.textEventName);
            textEvent.setText(Local.Get(getApplicationContext(), "EventName"));
        }

        final  AlertDialog ad=new AlertDialog.Builder(this).create();


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        // Get name and email from global/application context
        final String eventImageString  = globalVariable.getEventImage();

        if(eventImageString != null && eventImageString.length() > 0) {
            ImageView imageViewEventImage = (ImageView) findViewById(R.id.imageViewEventImage);
            byte[] decodedString = Base64.decode(eventImageString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewEventImage.setImageBitmap(decodedByte);
        }
        else {
            Intent intent = new Intent(BarcodeScanner.this, SetupEvent.class );
            startActivity(intent);

        }








        Button buttonYes = (Button)findViewById(R.id.buttonYes);
        buttonYes.setVisibility(View.INVISIBLE);
        buttonYes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MySOAPCallActivity cs = new MySOAPCallActivity();
                try{

                    //Make yes no buttons invisible
                    Button buttonYes = (Button)findViewById(R.id.buttonYes);
                    buttonYes.setVisibility(View.INVISIBLE);
                    Button scanbutton = (Button)findViewById(R.id.buttonScanTag);
                    Button buttonNo = (Button)findViewById(R.id.buttonNo);
                    Button next = (Button)findViewById(R.id.Next);



                    EditText editTagNumber = (EditText)findViewById(R.id.editTagNumber);

                    String tagNo = editTagNumber.getText().toString();

                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                    // Get name and email from global/application context
                    final String eventName  = globalVariable.getEventName();

                    if(eventName.length() > 0) {


                        //TextView hidden = (TextView) findViewById(R.id.labelScanResult);

                        //String Reason = hidden.getText().toString();

                        String Reason = "Allow Access";

                        AllowDenyParams params = new AllowDenyParams(cs, tagNo, Reason, false, eventName);

                        new CallSoapAllowDenyAccess().execute(params);

                        //this shows button scan next tag button once user has verfied ticket

                        scanbutton.setVisibility(View.GONE);
                        buttonNo.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);




                    }
                }
                catch(Exception ex) {
              //      ad.setTitle("Error!");
                //    ad.setMessage(ex.toString());
                }
              //  ad.show();

            }
        });






    }

   // restarts activity once patron is allowed entry
    public void Next(View view) {

        Intent intent = new Intent(BarcodeScanner.this, BarcodeScanner.class );
        startActivity(intent);
        finish();

        Button scancode = (Button)findViewById(R.id.scanner2);
        scancode.setVisibility(View.VISIBLE);



    }


    public void ButtonNo_Click(View view)
    {

        TextView labelScanResult = (TextView)findViewById(R.id.labelScanResult);
        TextView nameSurname = (TextView)findViewById(R.id.nameSurname);
        TextView idNumber = (TextView)findViewById(R.id.idNumber);
        TextView ticketClass = (TextView)findViewById(R.id.ticketClass);
        EditText editTagNumber = (EditText)findViewById(R.id.editTagNumber);
        Button yesbutton = (Button)findViewById(R.id.buttonYes);
        Button scanbutton = (Button)findViewById(R.id.scanner2);
        //hides progress bar
        ProgressBar mprogressbar;
        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
        mprogressbar.setVisibility(View.INVISIBLE);



        ImageView imageViewProfilePic = (ImageView) findViewById(R.id.imageViewProfilePic);

        imageViewProfilePic.setImageResource(R.drawable.ico_profile_icon);

        labelScanResult.setText("");
        nameSurname.setText("");
        idNumber.setText("");
        ticketClass.setText("");
        editTagNumber.setText("");

        //Make yes no buttons invisible
        Button buttonNo = (Button)findViewById(R.id.buttonNo);
        scanbutton.setVisibility(View.VISIBLE);
        buttonNo.setVisibility(View.INVISIBLE);

        yesbutton.setVisibility(View.INVISIBLE  );


    }


    //Logic business after the web service complete here
//Do the thing that modify the UI in a function like this
    private void onTaskCompleted(Object _response)
    {


    }

    public void openSetupNow(View view) {
        Intent in = new Intent(BarcodeScanner.this, MainActivity.class);
        startActivity(in);
        finish();


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(BarcodeScanner.this,"Click Home Button to exit", Toast.LENGTH_SHORT).show();




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

    private static class TagParams {
        MySOAPCallActivity foo;
        String tagNumber;
        String eventName;



        TagParams(MySOAPCallActivity foo, String tagNumber, String eventName) {
            this.foo = foo;
            this.tagNumber = tagNumber;
            this.eventName = eventName;

        }
    }

    private static class AllowDenyParams {
        MySOAPCallActivity foo;
        String TagNumber;
        String ReasonForBlocking;
        boolean Block;
        String EventName;


        AllowDenyParams(MySOAPCallActivity foo, String TagNumber, String ReasonForBlocking, boolean Block, String EventName) {
            this.foo = foo;
            this.TagNumber = TagNumber;
            this.ReasonForBlocking = ReasonForBlocking;
            this.Block = Block;
            this.EventName = EventName;

        }
    }







    public class CallSoapTicketValidForEvent extends AsyncTask<TagParams, Void, String> {




        private Exception exception;

        @Override
        protected String doInBackground(TagParams... params) {
            return params[0].foo.TicketValidForEvent(params[0].tagNumber, params[0].eventName) ;




        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed


            //hides progress bar

            ProgressBar mprogressbar;
            mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
            mprogressbar.setVisibility(View.VISIBLE);

            try {
                //TextView currentEventsResult =(TextView)findViewById(R.id.labelGetCurrentEventsResult);
                //currentEventsResult.setText(result);


                TextView labelScanResult = (TextView) findViewById(R.id.labelScanResult);
                TextView idNumber = (TextView) findViewById(R.id.idNumber);

                //sets tick button to invisible, tick will show whe idNumber > 1






                Button buttonYes = (Button)findViewById(R.id.buttonYes);
                buttonYes.setVisibility(View.INVISIBLE);


                if (result.toLowerCase().contains("no valid ticket") || result.toLowerCase().contains("not found") || result.toLowerCase().contains("no active"))//"no valid ticket"
                {
                    labelScanResult.setText(result);
                    //hides progress bar when information is complete
                    mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                    mprogressbar.setVisibility(View.INVISIBLE);


                } else {
                    labelScanResult.setText("Success!" + " Allow entry?");

                    //TableLayout tableLayout = (TableLayout) findViewById(R.id.tab);
                    //tableLayout.removeAllViews();


                    String[] rows = result.toString().split("\\n?\\n");
                    String profilePic = rows[3];

                    if (profilePic.length() > 0) {
                        ImageView imageViewProfilePic = (ImageView) findViewById(R.id.imageViewProfilePic);
                        byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageViewProfilePic.setImageBitmap(decodedByte);


                        //hides progress bar when information is complete
                        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                        mprogressbar.setVisibility(View.INVISIBLE);
                    }


                    TextView nameSurname = (TextView) findViewById(R.id.nameSurname);
                    nameSurname.setText(rows[0].toUpperCase());
                    idNumber.setText(rows[1].toUpperCase());
                    TextView ticketClass = (TextView) findViewById(R.id.ticketClass);
                    ticketClass.setText(rows[2].toUpperCase());


                    //shows tick icon when id number value is greater then 1
                    if (idNumber.length() > 1) {

                        buttonYes.setVisibility(View.VISIBLE);







                    }








                }
            }
            catch (Exception ex)
            {
                String error = ex.getMessage();

            }
        }





    }


    public class CallSoapAllowDenyAccess extends AsyncTask<AllowDenyParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(AllowDenyParams... params) {
            return params[0].foo.AllowDenyAccess(params[0].TagNumber, params[0].ReasonForBlocking, params[0].Block, params[0].EventName);
        }

        protected void onPostExecute(String result) {

            //TextView labelFinalConfirmation = (TextView)findViewById(R.id.labelFinalConfirmation);
            //labelFinalConfirmation.setText(result);

            TextView labelScanResult = (TextView)findViewById(R.id.labelScanResult);
            labelScanResult.setText(result);



            //TableLayout tableLayout = (TableLayout) findViewById(R.id.tab);
            //tableLayout.removeAllViews();

        }





    }
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";


    //product barcode mode
    public void scanBar(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "CODE_39");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(BarcodeScanner.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }


    // start qr stuff  //product qr code mode
    public void scanQR(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(BarcodeScanner.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }
    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {



                //get the extras that are returned from the intent
                String tagNo = intent.getStringExtra( "SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                String TIC = "tic";


                 EditText editTagNumber = (EditText)findViewById(R.id.editTagNumber);
                editTagNumber.setText(tagNo) ;


                MySOAPCallActivity cs = new MySOAPCallActivity();


                //tagNo = editTagNumber.getText().toString();

                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                // Get name and email from global/application context
                String eventName  = globalVariable.getEventName();
                if(Local.isSet(getApplicationContext(), "EventName"))
                {
                    eventName = Local.Get(getApplicationContext(), "EventName");

                }
                if(eventName.length() > 0) {

                    TagParams params = new TagParams(cs, tagNo, eventName);

                    //Make yes no buttons visible
                    Button scanbutton = (Button)findViewById(R.id.scanner2);

                    Button buttonYes = (Button)findViewById(R.id.buttonYes);
                    Button buttonNo = (Button)findViewById(R.id.buttonNo);
                    buttonYes.setVisibility(View.VISIBLE);
                    buttonNo.setVisibility(View.VISIBLE);
                    scanbutton.setVisibility(View.INVISIBLE);


                    TextView nameSurname = (TextView) findViewById(R.id.nameSurname);
                    nameSurname.setText("");
                    TextView idNumber = (TextView) findViewById(R.id.idNumber);
                    idNumber.setText("");
                    TextView ticketClass = (TextView) findViewById(R.id.ticketClass);
                    ticketClass.setText("");



                    new CallSoapTicketValidForEvent().execute(params);
                }

            }

            ;
        }
    }





















    @Override
    protected void onResume(){

        super.onResume();



    }
















}


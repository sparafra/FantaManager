package com.example.spara.fantacalciomanager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity
{


    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    int Crediti = 500;

    Button btn_gotoAsta;

    ListView listSocieta;
    ImageView banner;
    ImageView addSocieta_login;
    EditText editCrediti;
    TextView txtNomeSocieta;
    ProgressDialog pd;

    boolean easteregg = true;
    int clicktoeaster = 5;
    int clickTime = 0;

    List<String> arraySocieta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println("onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /* //Mail Floating Action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */



        //MY CODE


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        banner = findViewById(R.id.banner);
        btn_gotoAsta = findViewById(R.id.btn_gotoAsta);
        listSocieta = (ListView) findViewById(R.id.listSocieta);
        addSocieta_login = findViewById(R.id.addSocieta_login);
        editCrediti = findViewById(R.id.editCrediti);
        txtNomeSocieta = findViewById(R.id.txtSocieta);

        arraySocieta = new ArrayList<>();

        ImageView imgTransparent = findViewById(R.id.imageView);
        imgTransparent.setAlpha(230);



        try {

            // Check whether this app has write external storage permission or not.
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // If do not grant write external storage permission.
            if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
            {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }else {

                // Save email_public.txt file to /storage/emulated/0/DCIM folder
                Toast.makeText(getApplicationContext(), "Save to public external storage success. File Path ", Toast.LENGTH_LONG).show();



            }


        }catch (Exception ex)
        {

            Toast.makeText(getApplicationContext(), "Save to public external storage failed. Error message is " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }




        addSocieta_login.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI


                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(1000);

                        addSocieta_login.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                        v.startAnimation(animation1);
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                addSocieta_login.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }
                });


                arraySocieta.add(txtNomeSocieta.getText().toString());
                updateList();


            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                if(easteregg) {
                    clickTime++;
                    if (clickTime == clicktoeaster) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Per sempre membro della famiglia del FantaFinando", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        LinearLayout toastContentView = (LinearLayout) toast.getView();
                        ImageView imageView = new ImageView(getApplicationContext());
                        imageView.setImageResource(R.drawable.ottavio);
                        toastContentView.addView(imageView, 0);
                        toast.show();

                        clickTime = 0;
                    }
                }
            }
        });



        btn_gotoAsta.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(2000);
                        v.startAnimation(animation1);                    }
                });

                if(arraySocieta.size() > 0) {
                    String Line = "";
                    for (int k = 0; k < arraySocieta.size(); k++) {
                        Line = Line + arraySocieta.get(k);
                        if (k < arraySocieta.size() - 1)
                            Line = Line + "\n";
                    }

                    FileHelper.saveToFile(Line, "Societa.txt", false);

                    Crediti = Integer.valueOf(editCrediti.getText().toString());


                    Intent I = new Intent(Login.this, MainActivity.class);
                    I.putExtra("Crediti", Crediti);
                    startActivity(I);
                    Login.this.finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Deve esserci almeno 1 Societa per partecipare all'asta", Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    @Override
    protected void onStart()
    {
        super.onStart();
        System.out.println("onStart");



    }
    private void showLoadingDialog() {
        pd = new ProgressDialog(this, R.style.DialogTheme);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void updateList()
    {
        List<HashMap<String, String>> listitems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listitems, R.layout.list_item_normal, new String[]{"First Line"}, new int[]{R.id.text1});
        for(int i=0; i<arraySocieta.size(); i++) {

            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("First Line", arraySocieta.get(i));
            listitems.add(resultMap);

        }
        listSocieta.setAdapter(adapter);
    }


}

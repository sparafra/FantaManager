package com.example.spara.fantacalciomanager;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
{


    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    int Crediti = 500;

    Button btn_save;
    Button btn_skip;
    Spinner spChangeGiocatore;
    Spinner spChangeSquadra;

    ListView listSocieta;
    ListView listSquadra;

    ImageView addGiocatore;
    ImageView deleteGiocatore;
    EditText editCosto;

    String lastSocietaSelected;
    String lastGiocatoreSelected;
    int PoslastGiocatoreSelected;

    ProgressDialog pd;

    List<String> arrayPortieri;
    List<String> arrayDifensori;
    List<String> arrayCentrocampisti;
    List<String> arrayAttaccanti;

    List<String> arrayPortieriDeleted;
    List<String> arrayDifensoriDeleted;
    List<String> arrayCentrocampistiDeleted;
    List<String> arrayAttaccantiDeleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        btn_save = findViewById(R.id.btn_save);
        btn_skip = findViewById(R.id.btn_skip);
        listSocieta = (ListView) findViewById(R.id.listSquadre);
        listSquadra = (ListView) findViewById(R.id.listGiocatoriSquadra);
        addGiocatore = findViewById(R.id.addGiocatore);
        deleteGiocatore = findViewById(R.id.deleteGiocatore);

        editCosto = findViewById(R.id.costo);

        ImageView imgTransparent = findViewById(R.id.imageView);
        imgTransparent.setAlpha(230);


        Spinner spChangeRuolo = (Spinner) findViewById(R.id.sp_change_ruolo);
    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterChangeRuolo = ArrayAdapter.createFromResource(this,
                R.array.order_filter_array, R.layout.row);
    // Specify the layout to use when the list of choices appears
        adapterChangeRuolo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        spChangeRuolo.setAdapter(adapterChangeRuolo);


        spChangeGiocatore = (Spinner) findViewById(R.id.sp_change_giocatore);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterChangeGiocatore = ArrayAdapter.createFromResource(this,
                R.array.order_filter_array2, R.layout.row);
        // Specify the layout to use when the list of choices appears
        adapterChangeGiocatore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spChangeGiocatore.setAdapter(adapterChangeGiocatore);


        spChangeSquadra = (Spinner) findViewById(R.id.sp_change_squadra);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterChangeSquadra = ArrayAdapter.createFromResource(this,
                R.array.order_filter_array3, R.layout.row);
        // Specify the layout to use when the list of choices appears
        adapterChangeSquadra.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spChangeSquadra.setAdapter(adapterChangeSquadra);


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


                List<String> Societa = FileHelper.ReadFileOutArray(MainActivity.this, "Societa.txt");
                ArrayAdapter<String> adapterChangeSquadre = new ArrayAdapter<String>(MainActivity.this, R.layout.row, Societa);
                // Specify the layout to use when the list of choices appears
                adapterChangeSquadre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spChangeSquadra.setAdapter(adapterChangeSquadre);


                for(int k=0; k<Societa.size(); k++)
                {
                    FileHelper.createFile(Societa.get(k) + ".txt");
                }


                updateSquadInfo();

                //Carico i giocatori negli array

                arrayPortieri = FileHelper.ReadFileOutArray(MainActivity.this, "Portieri.txt");
                arrayDifensori = FileHelper.ReadFileOutArray(MainActivity.this, "Difensori.txt");
                arrayCentrocampisti = FileHelper.ReadFileOutArray(MainActivity.this, "Centrocampisti.txt");
                arrayAttaccanti = FileHelper.ReadFileOutArray(MainActivity.this, "Attaccanti.txt");

                AnalyzePlayerAndSort(arrayPortieri);
                AnalyzePlayerAndSort(arrayDifensori);
                AnalyzePlayerAndSort(arrayCentrocampisti);
                AnalyzePlayerAndSort(arrayAttaccanti);

                arrayPortieriDeleted = new ArrayList<>();
                arrayDifensoriDeleted = new ArrayList<>();
                arrayCentrocampistiDeleted = new ArrayList<>();
                arrayAttaccantiDeleted = new ArrayList<>();

            }


        }catch (Exception ex)
        {

            Toast.makeText(getApplicationContext(), "Save to public external storage failed. Error message is " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }


        //ListView Click on Item Event
        listSocieta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);

                HashMap<String, String> Map = (HashMap) parent.getItemAtPosition(position);
                String selectedItem = Map.get("First Line");
                System.out.println(selectedItem);
                lastSocietaSelected = selectedItem;

                List<String> Squadra = FileHelper.ReadFileOutArray(MainActivity.this, selectedItem+".txt");

                ArrayList<String> Portieri = new ArrayList<>();
                ArrayList<String> Dif = new ArrayList<>();
                ArrayList<String> Cen = new ArrayList<>();
                ArrayList<String> Att = new ArrayList<>();
                for(int k=0; k<Squadra.size(); k++)
                {
                    String ruolo = Squadra.get(k).substring(Squadra.get(k).indexOf("-")+1, Squadra.get(k).lastIndexOf("-")).trim();
                    String Nome = Squadra.get(k).substring(0, Squadra.get(k).indexOf("-")).trim();
                    String Costo = Squadra.get(k).substring(Squadra.get(k).lastIndexOf("-")+1).trim();
                    if(ruolo.equals("P"))
                    {
                        Portieri.add(Nome + "   " + "Portiere" + "   " + Costo);
                    }
                    else if(ruolo.equals("D"))
                    {
                        Dif.add(Nome + "   " + "Difensore" + "   " + Costo);
                    }
                    else if(ruolo.equals("C"))
                    {
                        Cen.add(Nome + "   " + "Centrocampista" + "   " + Costo);
                    }
                    else if(ruolo.equals("A"))
                    {
                        Att.add(Nome + "   " + "Attaccante" + "   " + Costo);
                    }
                }

                List<HashMap<String, String>> listitems = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, listitems, R.layout.list_item_mini, new String[]{"First Line"}, new int[]{R.id.text1});

                for(int k=0; k<Portieri.size(); k++)
                {
                    HashMap<String, String> resultMap = new HashMap<>();
                    resultMap.put("First Line", Portieri.get(k));
                    //resultMap.put("Second Line", Portieri.get(k));
                    listitems.add(resultMap);
                }
                for(int k=0; k<Dif.size(); k++)
                {
                    HashMap<String, String> resultMap = new HashMap<>();
                    resultMap.put("First Line", Dif.get(k));
                    //resultMap.put("Second Line", "");
                    listitems.add(resultMap);
                }
                for(int k=0; k<Cen.size(); k++)
                {
                    HashMap<String, String> resultMap = new HashMap<>();
                    resultMap.put("First Line", Cen.get(k));
                    //resultMap.put("Second Line", "");
                    listitems.add(resultMap);
                }
                for(int k=0; k<Att.size(); k++)
                {
                    HashMap<String, String> resultMap = new HashMap<>();
                    resultMap.put("First Line", Att.get(k));
                    //resultMap.put("Second Line", "");
                    listitems.add(resultMap);
                }

                listSquadra.setAdapter(adapter);



            }
        });
        listSquadra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);

                HashMap<String, String> Map = (HashMap) parent.getItemAtPosition(position);
                String selectedItem = Map.get("First Line");
                System.out.println(selectedItem);
                lastGiocatoreSelected = selectedItem;
                PoslastGiocatoreSelected = position;



            }
        });

        spChangeRuolo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();

                String FileName = "";
                List<String> Giocatori;

                switch (spChangeRuolo.getSelectedItem().toString())
                {
                    case "Portiere":
                        Giocatori = arrayPortieri;
                        break;
                    case "Difensore":
                        Giocatori = arrayDifensori;
                        break;
                    case "Centrocampista":
                        Giocatori = arrayCentrocampisti;
                        break;
                    case "Attaccante":
                        Giocatori = arrayAttaccanti;
                        break;
                    default:
                        Giocatori = null;
                }

                /*
                for(int k=0; k<Giocatori.size(); k++)
                {
                    String Squadra = Giocatori.get(k).substring(Giocatori.get(k).lastIndexOf("-")+1);
                    //Squadra = Squadra.substring(Portieri.lastIndexOf("\t"))
                    System.out.println(Squadra);
                    String Giocatore;
                    if(Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-")).indexOf(" ") != -1)
                        Giocatore = Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-")).substring(0, Giocatori.get(k).lastIndexOf(" ")+2) + ".";
                    else
                        Giocatore = Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-"))+ ".";
                    /*
                    if(Giocatore.trim().length() == 2)
                        Giocatore = Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-"));
                    *s/
                    Giocatori.set(k, Giocatore + " (" + Squadra.trim() + ")");
                }
                Collections.sort(Giocatori);
                */

                ArrayAdapter<String> adapterChangeGiocatore = new ArrayAdapter<String>(MainActivity.this, R.layout.row, Giocatori);
                // Specify the layout to use when the list of choices appears
                adapterChangeGiocatore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spChangeGiocatore.setAdapter(adapterChangeGiocatore);

                System.out.println(Giocatori.toString());

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        addGiocatore.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI


                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(1000);

                        addGiocatore.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                        v.startAnimation(animation1);
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                addGiocatore.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }
                });

                showLoadingDialog();

                String SquadraSelected = spChangeSquadra.getItemAtPosition(spChangeSquadra.getSelectedItemPosition()).toString();
                System.out.println(SquadraSelected);

                String Giocatore = spChangeGiocatore.getItemAtPosition(spChangeGiocatore.getSelectedItemPosition()).toString();
                String RuoloSelected = spChangeRuolo.getItemAtPosition(spChangeRuolo.getSelectedItemPosition()).toString().trim();
                String Ruolo = "";
                String Costo = editCosto.getText().toString();

                if (RuoloSelected.equals("Portiere")) {
                    Ruolo = "P";
                } else if (RuoloSelected.equals("Difensore")) {
                    Ruolo = "D";
                } else if (RuoloSelected.equals("Centrocampista")) {
                    Ruolo = "C";
                } else if (RuoloSelected.equals("Attaccante")) {
                    Ruolo = "A";
                }
                String line = Giocatore + "-" + Ruolo + "-" + Costo;
                FileHelper.saveToFile(line, SquadraSelected + ".txt", true);


                updateSquadInfo();

                List<String> Giocatori = null;

                if (RuoloSelected.equals("Portiere")) {
                    arrayPortieriDeleted.add(arrayPortieri.get(spChangeGiocatore.getSelectedItemPosition()));
                    arrayPortieri.remove(spChangeGiocatore.getSelectedItemPosition());
                    Giocatori = arrayPortieri;
                } else if (RuoloSelected.equals("Difensore")) {
                    arrayDifensoriDeleted.add(arrayDifensori.get(spChangeGiocatore.getSelectedItemPosition()));
                    arrayDifensori.remove(spChangeGiocatore.getSelectedItemPosition());
                    Giocatori = arrayDifensori;
                } else if (RuoloSelected.equals("Centrocampista")) {
                    arrayCentrocampistiDeleted.add(arrayCentrocampisti.get(spChangeGiocatore.getSelectedItemPosition()));
                    arrayCentrocampisti.remove(spChangeGiocatore.getSelectedItemPosition());
                    Giocatori = arrayCentrocampisti;
                } else if (RuoloSelected.equals("Attaccante")) {
                    arrayAttaccantiDeleted.add(arrayAttaccanti.get(spChangeGiocatore.getSelectedItemPosition()));
                    arrayAttaccanti.remove(spChangeGiocatore.getSelectedItemPosition());
                    Giocatori = arrayAttaccanti;
                }
                ArrayAdapter<String> adapterChangeGiocatore = new ArrayAdapter<String>(MainActivity.this, R.layout.row, Giocatori);
                // Specify the layout to use when the list of choices appears
                adapterChangeGiocatore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spChangeGiocatore.setAdapter(adapterChangeGiocatore);

                pd.dismiss();

            }
        });

        deleteGiocatore.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI


                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(1000);

                        deleteGiocatore.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                        v.startAnimation(animation1);
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                deleteGiocatore.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }
                });

                showLoadingDialog();




                System.out.println(lastGiocatoreSelected);
                //String NomeGiocatore = lastGiocatoreSelected.substring(0, lastGiocatoreSelected.indexOf("\t")).trim();
                System.out.println("Pos Giocatore Selezionato: " + PoslastGiocatoreSelected);
                List<String> Squadra = FileHelper.ReadFileOutArray(MainActivity.this, lastSocietaSelected + ".txt");

                String line = "";
                for(int k=0; k<Squadra.size(); k++)
                {
                    String Nome = Squadra.get(k).substring(0, Squadra.get(k).indexOf("-")).trim();
                    String Ruolo = Squadra.get(k).substring(Squadra.get(k).indexOf("-")+1, Squadra.get(k).lastIndexOf("-")).trim();
                    String Costo = Squadra.get(k).substring(Squadra.get(k).lastIndexOf("-")+1).trim();

                    if(PoslastGiocatoreSelected != k) {
                        line = line + Nome + "-" + Ruolo + "-" + Costo;
                        //System.out.println(Nome);
                    }
                    else
                    {
                        if(Ruolo.equals("P"))
                        {
                            arrayPortieri.add(Nome);
                            for(int i=0; i<arrayPortieriDeleted.size(); i++)
                            {
                                if(arrayPortieriDeleted.get(i).equals(Nome)) {
                                    System.out.println(Nome);
                                    System.out.println(arrayPortieriDeleted.get(i));
                                    arrayPortieriDeleted.remove(i);
                                }
                            }
                            Collections.sort(arrayPortieri);

                        }
                        else if(Ruolo.equals("D"))
                        {
                            arrayDifensori.add(Nome);
                            for(int i=0; i<arrayDifensoriDeleted.size(); i++)
                            {
                                if(arrayDifensoriDeleted.get(i).equals(Nome)) {
                                    System.out.println(Nome);
                                    System.out.println(arrayDifensoriDeleted.get(i));
                                    arrayDifensoriDeleted.remove(i);
                                }
                            }

                            Collections.sort(arrayDifensori);
                        }
                        else if(Ruolo.equals("C"))
                        {
                            arrayCentrocampisti.add(Nome);
                            for(int i=0; i<arrayCentrocampistiDeleted.size(); i++)
                            {
                                if(arrayCentrocampistiDeleted.get(i).equals(Nome)) {
                                    System.out.println(Nome);
                                    System.out.println(arrayCentrocampistiDeleted.get(i));
                                    arrayCentrocampistiDeleted.remove(i);
                                }
                            }
                            Collections.sort(arrayCentrocampisti);
                        }
                        else if(Ruolo.equals("A"))
                        {
                            arrayAttaccanti.add(Nome);
                            for(int i=0; i<arrayAttaccantiDeleted.size(); i++)
                            {
                                if(arrayAttaccantiDeleted.get(i).equals(Nome)) {
                                    System.out.println(Nome);
                                    System.out.println(arrayAttaccantiDeleted.get(i));
                                    arrayAttaccantiDeleted.remove(i);
                                }
                            }
                            Collections.sort(arrayAttaccanti);
                        }


                    }
                    if(PoslastGiocatoreSelected != k && k < Squadra.size()-2)
                        line = line + "\n";
                }
                //System.out.println(line);
                FileHelper.saveToFile(line, lastSocietaSelected + ".txt", false);

                String RuoloSelected = spChangeRuolo.getItemAtPosition(spChangeRuolo.getSelectedItemPosition()).toString().trim();
                List<String> Giocatori = null;

                if (RuoloSelected.equals("Portiere")) {
                    Giocatori = arrayPortieri;
                } else if (RuoloSelected.equals("Difensore")) {
                    Giocatori = arrayDifensori;
                } else if (RuoloSelected.equals("Centrocampista")) {
                    Giocatori = arrayCentrocampisti;
                } else if (RuoloSelected.equals("Attaccante")) {
                    Giocatori = arrayAttaccanti;
                }
                ArrayAdapter<String> adapterChangeGiocatore = new ArrayAdapter<String>(MainActivity.this, R.layout.row, Giocatori);
                // Specify the layout to use when the list of choices appears
                adapterChangeGiocatore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spChangeGiocatore.setAdapter(adapterChangeGiocatore);

                updateSquadInfo();

                pd.dismiss();

            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
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

                showLoadingDialog();
                spChangeGiocatore.setSelection(spChangeGiocatore.getSelectedItemPosition() +1 );
                pd.dismiss();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
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

                Toast toast = Toast.makeText(getApplicationContext(), "Per sempre membro della famiglia del FantaFinando", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastContentView = (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.ottavio);
                toastContentView.addView(imageView, 0);
                toast.show();
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

    public void updateSquadInfo()
    {
        List<String> Societa = FileHelper.ReadFileOutArray(MainActivity.this, "Societa.txt");
        List<HashMap<String, String>> listitems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listitems, R.layout.list_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.text1, R.id.text2});
        for(int i=0; i<Societa.size(); i++) {

            List<String> Squadra = FileHelper.ReadFileOutArray(MainActivity.this, Societa.get(i) + ".txt");

            ArrayList<String> Portieri = new ArrayList<>();
            ArrayList<String> Dif = new ArrayList<>();
            ArrayList<String> Cen = new ArrayList<>();
            ArrayList<String> Att = new ArrayList<>();
            float CostoTotale = 0;
            for (int k = 0; k < Squadra.size(); k++) {

                System.out.println("Riga " + k + " Squadra: " + Squadra.get(k));

                String ruolo = "";
                String Nome = "";
                String Costo = "";

                if(Squadra.get(k).indexOf("-") != -1) {
                    ruolo = Squadra.get(k).substring(Squadra.get(k).indexOf("-")+1, Squadra.get(k).lastIndexOf("-")).trim();
                    Nome = Squadra.get(k).substring(0, Squadra.get(k).indexOf("-")).trim();
                    Costo = Squadra.get(k).substring(Squadra.get(k).lastIndexOf("-")+1).trim();
                }
                /*
                else
                {
                    ruolo = Squadra.get(k).substring(Squadra.get(k).indexOf("   "), Squadra.get(k).lastIndexOf("    ")).trim();
                    Nome = Squadra.get(k).substring(0, Squadra.get(k).indexOf("   ")).trim();
                    Costo = Squadra.get(k).substring(Squadra.get(k).lastIndexOf("   ")).trim();
                }
                */

                if (ruolo.equals("P")) {
                    Portieri.add(Nome + "   " + ruolo + "   " + Costo);
                } else if (ruolo.equals("D")) {
                    Dif.add(Nome + "    " + ruolo + "   " + Costo);
                } else if (ruolo.equals("C")) {
                    Cen.add(Nome + "    " + ruolo + "   " + Costo);
                } else if (ruolo.equals("A")) {
                    Att.add(Nome + "    " + ruolo + "   " + Costo);
                }
                CostoTotale += Float.valueOf(Costo);
            }
            String Data = "Crediti: " + (Crediti-CostoTotale) + "   " + "P: " + Portieri.size() +"/3" + "   " + "D: " + Dif.size() +"/8" + "    " + "C: " + Cen.size() +"/8" + "    " + "A: " + Att.size() + "/6";

            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("First Line", Societa.get(i));
            resultMap.put("Second Line", Data);
            listitems.add(resultMap);

        }
        listSocieta.setAdapter(adapter);

    }


    public void AnalyzePlayerAndSort(List<String> Giocatori)
    {

        for(int k=0; k<Giocatori.size(); k++)
        {
            String Squadra = Giocatori.get(k).substring(Giocatori.get(k).lastIndexOf("-")+1);
            //Squadra = Squadra.substring(Portieri.lastIndexOf("\t"))
            System.out.println(Squadra);
            String Giocatore;
            if(Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-")).indexOf(" ") != -1)
                Giocatore = Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-")).substring(0, Giocatori.get(k).lastIndexOf(" ")+2) + ".";
            else
                Giocatore = Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-"))+ ".";
                    /*
                    if(Giocatore.trim().length() == 2)
                        Giocatore = Giocatori.get(k).substring(0, Giocatori.get(k).indexOf("-"));
                    */
            Giocatori.set(k, Giocatore + " (" + Squadra.trim() + ")");
        }
        Collections.sort(Giocatori);
    }


}

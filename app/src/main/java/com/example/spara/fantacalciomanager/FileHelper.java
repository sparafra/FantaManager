package com.example.spara.fantacalciomanager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tan on 2/18/2016.
 */
public class FileHelper {
    //final static String fileName = "Portieri.txt";
    static String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FantaFinando/";
    final static String TAG = FileHelper.class.getName();


    public static  String ReadFile(Context context, String FileName){
        String line = null;
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FantaFinando/" ;
        try {
            FileInputStream fileInputStream = new FileInputStream (new File(path + FileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }

    public static ArrayList<String> ReadFileOutArray(Context context, String FileName){
        String line = null;
        ArrayList<String> array = new ArrayList<>();
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FantaFinando/" ;
        try {
            FileInputStream fileInputStream = new FileInputStream (new File(path + FileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
                array.add(line);
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return array;
    }
    public static boolean saveToFile( String data, String FileName, boolean Append){
        try {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FantaFinando/" ;
            new File(path  ).mkdir();
            File file = new File(path + FileName);

            if (!file.exists() ) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, Append);
            if(!data.equals(""))
                fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());


            return true;
        }  catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }  catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return  false;


    }

    public static boolean createFile(String FileName)
    {
        try {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FantaFinando/" ;
            new File(path  ).mkdir();
            File file = new File(path + FileName);

            if (!file.exists() ) {
                file.createNewFile();
            }
            else
            {
                file.delete();
                file.createNewFile();
            }


            return true;
        }  catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }  catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return  false;
    }

    public static ArrayList<String> downloadFromUrl(final String urlWebService) {

        try {
            String line = null;
            ArrayList<String> array = new ArrayList<>();
            URL url = new URL(urlWebService);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
                array.add(line);
            }
            line = stringBuilder.toString();

            bufferedReader.close();
            return array;
        } catch (Exception e) {
            return null;
        }

    }

    public static String getPath(){return path;}

}
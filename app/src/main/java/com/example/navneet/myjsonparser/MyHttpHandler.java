package com.example.navneet.myjsonparser;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MyHttpHandler {

    private static final String TAG=MyHttpHandler.class.getSimpleName();

    public MyHttpHandler() {

    }
    public String makeServiceCall(String reqUrl){

        String response=null;
        try{
            URL url=new URL(reqUrl);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            //Reading the response from the URL
            InputStream inputStream=new BufferedInputStream(connection.getInputStream());
            response=convertStreamToString(inputStream);

        }
        catch (MalformedURLException e){
            Log.d(TAG, "makeServiceCall: "+e.getMessage());
        }
        catch (ProtocolException e){
            Log.d(TAG, "makeServiceCall: "+e.getMessage());
        }
        catch (IOException e){
            Log.d(TAG, "makeServiceCall: "+e.getMessage());
        }
        catch (Exception e){
            Log.d(TAG, "makeServiceCall: "+e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream inputStream) {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb=new StringBuilder();
        String line;

        try {
            while((line=reader.readLine())!=null){
                sb.append(line).append('\n');
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}

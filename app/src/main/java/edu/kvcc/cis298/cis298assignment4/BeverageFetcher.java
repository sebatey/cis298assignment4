package edu.kvcc.cis298.cis298assignment4;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BeverageFetcher{

    private static final String TAG = "BEVERAGE_FETCHER";
    private static final String WEB_ADDRESS = "http://barnesbrothers.homeserver.com/beverageapi/";

    private byte[] getUrlBytes(String urlSpec) throws IOException{
        Log.i(TAG,"get URL Bytes started");
        //Create a new URL object from the url string that was passed in
        URL url = new URL(urlSpec);

        //Create a new HTTP connection from the newly created URL object
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        Log.i(TAG,"connection created");
        try{

            //Create an output stream to read the data from the URL
            //and create an input stream from the newly created connection
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();
            Log.i(TAG,"input and output streams created");
            //Check the response code from the HTTP request
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + "\n" +
                                      ": with " + urlSpec);
            }

            //int to hold how many bytes are read in
            int bytesRead = 0;

            //Create a byte to be a buffer
            byte[] buffer = new byte[1024];

            while((bytesRead = inputStream.read(buffer)) > 0){
                //While there are bytes to read we write them to
                //the output stream
                outputStream.write(buffer, 0, bytesRead);
            }
            Log.i(TAG,"bytes read");
            outputStream.close();
            inputStream.close();

            return outputStream.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        Log.i(TAG,"Get URL String started");
        return new String(getUrlBytes((urlSpec)));
    }

    public List<Beverage> fetchBeverages(){
        Log.i(TAG,"Beginning to fetchBeverages()");
        List<Beverage> beverages = new ArrayList<>();

        try{
            String url = Uri.parse(WEB_ADDRESS)
                    .buildUpon()
                    .build()
                    .toString();
            Log.i(TAG,"Webaddress url string created");
            String jsonString = getUrlString(url);

            JSONArray jsonArray = new JSONArray(jsonString);
            Log.i(TAG,"jsonArray created");
            parseBeverages(beverages, jsonArray);
        } catch(JSONException e){
            Log.e(TAG,e.getMessage());
        } catch(IOException e){
            Log.e(TAG,e.getMessage());
        }
        Log.i(TAG,"Get URL String finished");
        return beverages;
    }

    private void parseBeverages(List<Beverage> beverages, JSONArray jsonArray)
            throws IOException, JSONException{

        Log.i(TAG,"Beginning to parse beverages");

        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject beverageJsonObject= jsonArray.getJSONObject(i);

            //Get the information out of the JSONObject
            String idString = beverageJsonObject.getString("id");
            String nameString = beverageJsonObject.getString("name");
            String packString = beverageJsonObject.getString("pack");
            double priceDouble = beverageJsonObject.getDouble("price");
            boolean activeBoolean = (beverageJsonObject.getInt("isActive") == 1);

            //Create the new beverage from the parsed information and add it to the list of beverages
            Beverage newBeverage = new Beverage(idString, nameString, packString, priceDouble, activeBoolean);

            beverages.add(newBeverage);
            Log.i(TAG, "Added Beverage: " + i);
        }
        Log.i(TAG,"finished parsing beverages");
    }
}

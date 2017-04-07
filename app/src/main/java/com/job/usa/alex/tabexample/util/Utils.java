package com.job.usa.alex.tabexample.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.job.usa.alex.tabexample.util.aws.Parser;
import com.job.usa.alex.tabexample.util.aws.SearchObject;
import com.job.usa.alex.tabexample.util.aws.SignedRequestsHelper;
import com.job.usa.alex.tabexample.util.aws.UrlParameterHandler;
import com.job.usa.alex.tabexample.util.walmart.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SuperMaster on 4/3/2017.
 */

public class Utils {

        static HashMap<String,String> walmartItem;
        static ArrayList<SearchObject> awsItems;

        public static HashMap<String,String> getWalmartData(String url) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            final String jsonStr = sh.makeServiceCall(url);

            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray items= jsonObj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);

                        String itemId = c.getString("itemId");
                        String name = c.getString("name");
                        String msrp = c.getString("msrp");
                        String salePrice = c.getString("salePrice");
                        String productTrackingUrl = c.getString("productTrackingUrl");
                        String mediumImage = c.getString("mediumImage");
                        String productUrl=c.getString("productUrl");

                        walmartItem = new HashMap<>();

                        // adding each child node to HashMap key => value
                        walmartItem.put("itemId", itemId);
                        walmartItem.put("name", name);
                        walmartItem.put("msrp", msrp);
                        walmartItem.put("salePrice", salePrice);
                        walmartItem.put("productTrackingUrl", productTrackingUrl);
                        walmartItem.put("mediumImage", mediumImage);
                        walmartItem.put("productUrl", productUrl);
                    }
                } catch (final JSONException e) {
                    Log.e("Utils Class", "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e("Utils Class", "Couldn't get json from server.");
            }
            return walmartItem;
    }

    public static ArrayList<SearchObject> getAWSData(String upcValue) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String,String> params=UrlParameterHandler.getInstance().buildMapForItemSearch(upcValue);
        String service_url=new SignedRequestsHelper().sign(params);
        Parser myParser=new Parser();
        NodeList list=myParser.getResponceNodeList(service_url);
        awsItems=new ArrayList<SearchObject>();
        for(int i=0;i<list.getLength();i++){
            awsItems.add(i,myParser.getSearchObject(list,i));
        }
        return awsItems;
    }

    public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    private static InputStream OpenHttpConnection(String strURL) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return inputStream;
    }
}

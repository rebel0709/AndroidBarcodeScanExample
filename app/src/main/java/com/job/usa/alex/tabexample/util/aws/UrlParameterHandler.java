package com.job.usa.alex.tabexample.util.aws;

import java.util.HashMap;
import java.util.Map;
/*
    java file for passing the GET request parameters building the query parameters.
    UrlParameterHandler.java file is responsible for maintaining the record of the parameters in Map
    Where Map key corresponds to parameter key and Map Value corresponds to query parameter valid value.
* */
public class UrlParameterHandler {

    public static UrlParameterHandler paramHandler;
    private UrlParameterHandler() {}


    public static synchronized UrlParameterHandler getInstance(){
        if(paramHandler==null){
            paramHandler=new UrlParameterHandler();
            return paramHandler;
        }
        return paramHandler;
    }

    public  Map<String,String> buildMapForItemSearch(String upcValue){
        Map<String, String> myparams = new HashMap<String, String>();
        myparams.put("Service", "AWSECommerceService");
        myparams.put("Operation", "ItemLookup");
        myparams.put("ResponseGroup", "Large");
        myparams.put("SearchIndex", "All");//for searching mobile apps
        myparams.put("AssociateTag", "amazons0dfb-20");
        myparams.put("IdType", "UPC");
        myparams.put("ItemId", upcValue);
        //myparams.put("Version", "2009-10-01");
        //myparams.put("ContentType", "text/xml");
        //myparams.put("Keywords", "games");
        //myparams.put("MaximumPrice","1000");
        //myparams.put("Sort","price");
        return myparams;
    }

}
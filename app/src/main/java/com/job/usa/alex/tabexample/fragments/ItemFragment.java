package com.job.usa.alex.tabexample.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.job.usa.alex.tabexample.R;
import com.job.usa.alex.tabexample.util.Utils;
import com.job.usa.alex.tabexample.util.aws.SearchObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ItemFragment extends Fragment {

    public static ItemFragment newInstance(String upcValue) {
        ItemFragment f = new ItemFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("UPCValue", upcValue);
        f.setArguments(args);

        return f;
    }

    public ItemFragment() {

    }

    public String getUPCValue() {
        return getArguments().getString("UPCValue", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String upcValue=getUPCValue();
        if(upcValue.trim().length()!=0){
            View table_container=inflater.inflate(R.layout.fragment_item, container, false);
            TableLayout view=(TableLayout)table_container.findViewById(R.id.table_layout);
            new ProgressTask(getUPCValue(), view).execute();
            return table_container;
        }
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    ProgressDialog pDialog;
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {

        String walmart_api_url;
        HashMap<String, String> walmart_item;
        Bitmap walmart_item_image;
        ArrayList<SearchObject> aws_items;
        String upcValue;
        TableLayout parentView;

        public ProgressTask(String value, TableLayout view){
            this.upcValue=value;
            this.walmart_api_url="http://api.walmartlabs.com/v1/items?apiKey=38sje7cnbsjvdj98zd7b3yh4&upc="+value+"&format=json";
            this.parentView=view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ItemFragment.this.getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            this.walmart_item=Utils.getWalmartData(this.walmart_api_url);
            try {
                String url = this.walmart_item.get("mediumImage");
                BitmapFactory.Options bmOptions;
                bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;
                this.walmart_item_image = Utils.loadBitmap(url, bmOptions);
            }
            catch (Exception e) {
                // handle it
            }
            try {
                aws_items=Utils.getAWSData(this.upcValue);
                for(int i=0;i<aws_items.size();i++){
                    aws_items.get(i).loadBrandImage();
                }
                ItemFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupItemScreen();//walmart_item, aws_items);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

        void setupItemScreen() {
            TextView tv;
            ImageView iv;
            if(this.walmart_item==null){
                parentView.removeViewAt(0);
            }else {
                tv=(TextView)parentView.findViewById(R.id.wal_product_name);tv.setText(this.walmart_item.get("name").toString());
                tv=(TextView)parentView.findViewById(R.id.wal_price_1);tv.setText(this.walmart_item.get("msrp").toString());
                tv=(TextView)parentView.findViewById(R.id.wal_price_2);tv.setText(this.walmart_item.get("salePrice").toString());
                tv=(TextView)parentView.findViewById(R.id.wal_track_url);tv.setText("Go to Site");
                tv.setOnClickListener(new WalMartUrlClickHandler(this.walmart_item.get("productUrl").toString()));
                iv=(ImageView)parentView.findViewById(R.id.walmart_img);
                iv.setImageBitmap(this.walmart_item_image);
            }

            for(int i=0;i<this.aws_items.size();i++){
                Activity ctx=ItemFragment.this.getActivity();
                SearchObject one=aws_items.get(i);

                View subView=ctx.getLayoutInflater().inflate(R.layout.aws_product,null);
                tv=(TextView)subView.findViewById(R.id.aws_product_name);tv.setText(one.getTitle());
                tv=(TextView)subView.findViewById(R.id.aws_price_1);tv.setText(one.getPrice());
                tv=(TextView)subView.findViewById(R.id.aws_track_url);tv.setText("Go to Site");
                tv.setOnClickListener(new WalMartUrlClickHandler(one.getUrl()));
                iv=(ImageView)subView.findViewById(R.id.aws_img);
                iv.setImageBitmap(one.getBrandBitmap());
                parentView.addView(subView);
            }
        }
    }

    class WalMartUrlClickHandler implements View.OnClickListener {
        String url;
        WalMartUrlClickHandler(String url){
            this.url=url;
        }
        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.url));
            startActivity(browserIntent);
        }
    }
}

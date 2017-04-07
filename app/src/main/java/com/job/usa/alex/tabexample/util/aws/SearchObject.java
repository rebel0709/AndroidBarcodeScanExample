package com.job.usa.alex.tabexample.util.aws;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.job.usa.alex.tabexample.util.Utils;

/**
 * Created by SuperMaster on 4/3/2017.
 */

public class SearchObject {
    private String url;
    private String id;
    private String imageUrl;
    private String title;
    private Bitmap brandImage;
    private String price;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle(){return title;}
    public String getUrl(){return url;};

    public void loadBrandImage(){
        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        this.brandImage = Utils.loadBitmap(this.imageUrl, bmOptions);
    }
    public Bitmap getBrandBitmap(){ return this.brandImage;}

    public void setPrice(String price) { this.price = price; }
    public String getPrice(){ return this.price;}
}

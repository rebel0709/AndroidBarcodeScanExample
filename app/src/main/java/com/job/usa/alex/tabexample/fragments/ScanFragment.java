package com.job.usa.alex.tabexample.fragments;


import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.job.usa.alex.tabexample.MainActivity;
import com.job.usa.alex.tabexample.R;
import com.job.usa.alex.tabexample.contentprovider.HistoryProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler{

    public ScanFragment() {
        // Required empty public constructor
    }

    private ZXingScannerView mScannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        }, 2000);
        MainActivity parentCtx=(MainActivity)getActivity();
        parentCtx.fireUPCValue(rawResult.getText());

        ContentValues values = new ContentValues();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date curDate = new Date();

        values.put(HistoryProvider.TIMESTAMP, dateFormat.format(curDate));
        values.put(HistoryProvider.CODE_TYPE, rawResult.getBarcodeFormat().toString());
        values.put(HistoryProvider.CODE_VALUE, rawResult.getText());
        Uri uri = parentCtx.getContentResolver().insert(HistoryProvider.CONTENT_URI, values);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}

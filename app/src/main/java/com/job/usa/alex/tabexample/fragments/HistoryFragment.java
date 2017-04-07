package com.job.usa.alex.tabexample.fragments;


import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.job.usa.alex.tabexample.MainActivity;
import com.job.usa.alex.tabexample.R;
import com.job.usa.alex.tabexample.contentprovider.HistoryProvider;
import com.job.usa.alex.tabexample.contentprovider.LogRecord;
import com.job.usa.alex.tabexample.util.adapter.HistoryListAdaptor;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends ListFragment implements AdapterView.OnItemClickListener {


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<LogRecord> array=loadHistoryData();
        HistoryListAdaptor adaptor=new HistoryListAdaptor(getActivity(),array);
        //this.getListView().setAdapter(adaptor);
        setListAdapter(adaptor);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<LogRecord> array=loadHistoryData();
        HistoryListAdaptor adaptor=new HistoryListAdaptor(getActivity(),array);
        //this.getListView().setAdapter(adaptor);
        setListAdapter(adaptor);
    }
    private ArrayList<LogRecord> loadHistoryData() {
        // Retrieve student records
        String URL = "content://com.job.usa.alex.tabexample.contentprovider.HistoryProvider";
        Activity ctx=this.getActivity();
        Uri codes = Uri.parse(URL);
        Cursor c = ctx.getContentResolver().query(codes, null, null, null, "_id");
        ArrayList<LogRecord> array=new ArrayList<LogRecord>();
        if (c.moveToFirst()) {
            do{
              array.add(new LogRecord(c.getString(c.getColumnIndex( HistoryProvider.TIMESTAMP)),c.getString(c.getColumnIndex( HistoryProvider.CODE_TYPE)),c.getString(c.getColumnIndex( HistoryProvider.CODE_VALUE))));
            } while (c.moveToNext());
        }
        return array;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogRecord one = (LogRecord) getListView().getAdapter().getItem(position);
        MainActivity parentCtx=(MainActivity)getActivity();
        parentCtx.fireUPCValue(one.getCodeValue());
    }
}

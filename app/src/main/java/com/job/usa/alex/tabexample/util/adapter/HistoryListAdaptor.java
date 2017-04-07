package com.job.usa.alex.tabexample.util.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.job.usa.alex.tabexample.R;
import com.job.usa.alex.tabexample.contentprovider.LogRecord;

import java.util.ArrayList;

/**
 * Created by SuperMaster on 4/4/2017.
 */

public class HistoryListAdaptor extends ArrayAdapter<LogRecord> {
    private final Context context;
    private final ArrayList<LogRecord> values;

    public HistoryListAdaptor(Context context, ArrayList<LogRecord> values) {
        super(context, -1);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("MY TAG",String.valueOf(position));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.history_item, parent, false);
        TextView txtView1 = (TextView) rowView.findViewById(R.id.timestamp);
        TextView txtView2 = (TextView) rowView.findViewById(R.id.barcode_data);
        LogRecord one= values.get(position);
        txtView1.setText(one.getTimeStamp());
        txtView2.setText(one.getCodeType()+" , "+one.getCodeValue());
        return rowView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Nullable
    @Override
    public LogRecord getItem(int position) {
        return values.get(position);
    }
}

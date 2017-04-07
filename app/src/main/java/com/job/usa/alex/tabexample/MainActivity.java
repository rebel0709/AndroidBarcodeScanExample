package com.job.usa.alex.tabexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.job.usa.alex.tabexample.fragments.HistoryFragment;
import com.job.usa.alex.tabexample.fragments.ItemFragment;
import com.job.usa.alex.tabexample.fragments.ScanFragment;

public class MainActivity extends AppCompatActivity {

    private String upcValue;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationView();
    }

    private void setupNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        if (navigation != null) {
            // Select first menu item by default and show Fragment accordingly.
            Menu menu = navigation.getMenu();
            selectFragment(menu.getItem(1));

            // Set action to perform when any menu-item is selected.
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
    }

    protected void selectFragment(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.scan_product:
                // Action to perform when Home Menu item is selected.
                pushFragment(new ScanFragment());
                break;
            case R.id.scaned_history:
                // Action to perform when Bag Menu item is selected.
                pushFragment(new HistoryFragment());
                break;
            case R.id.current_item:
                // Action to perform when Account Menu item is selected.
                pushFragment(ItemFragment.newInstance(upcValue));
                break;
        }

    }
    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.content, fragment);
                ft.commit();
            }
        }
    }

    public void fireUPCValue(String value) {
        this.upcValue=value;
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        selectFragment(menu.getItem(2));
        this.upcValue="";
    }
}
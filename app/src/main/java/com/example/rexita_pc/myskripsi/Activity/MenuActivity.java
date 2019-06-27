package com.example.rexita_pc.myskripsi.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.example.rexita_pc.myskripsi.Fragment.DasboardFragment;
import com.example.rexita_pc.myskripsi.Fragment.FragmentAddData;
import com.example.rexita_pc.myskripsi.Fragment.LogoutFragment;
import com.example.rexita_pc.myskripsi.R;

import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        Fragment fragment = new DasboardFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_area, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                fragment = new DasboardFragment();
            }
            else if (id == R.id.navigation_plus) {
                fragment = new FragmentAddData();
            }
            else if (id == R.id.navigation_account) {
                fragment = new LogoutFragment();
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.layout_area, fragment);
                ft.commit();
            }
            return true;
        }
    };
}

package com.example.ayush.missystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ayush.missystem.SqliteHelper.DbHelper;
import com.example.ayush.missystem.SqliteHelper.OtherSettingDbHelper;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ConstraintLayout constraintLayout1, constraintLayout2, constraintLayout3, constraintLayout4;

    LinearLayout home, family, individual, death;
    Button button1, button2, button3, button4;
    TextView deathscount, families, individuals, houses;

    ArrayList<String> wardNos = new ArrayList<>();
    int ward_id = 1;
    int death_count,
            family_count,
            individuals_count,
            house_count;
    Spinner spinner;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ward_id = Constants.ward_id;

        initdb();
//        updateViews();
//        initViews();

        Log.d("ward_id_db", ":" + ward_id);

        home = findViewById(R.id.linearlayout1);
        family = findViewById(R.id.linearlayout2);
        individual = findViewById(R.id.linearlayout3);
        death = findViewById(R.id.linearlayout4);

        constraintLayout1 = findViewById(R.id.constraintlayout1);
        constraintLayout2 = findViewById(R.id.constraintlayout2);
        constraintLayout4 = findViewById(R.id.constraintlayout4);

        button1 = findViewById(R.id.home_btn);
        button1.setOnClickListener(this);
        button2 = findViewById(R.id.family_btn);
        button2.setOnClickListener(this);
        button3 = findViewById(R.id.individual_btn);
        button3.setOnClickListener(this);
        button4 = findViewById(R.id.death_btn);
        button4.setOnClickListener(this);

        deathscount = findViewById(R.id.death_value);
        families = findViewById(R.id.family_value);
        individuals = findViewById(R.id.individual_value);
        houses = findViewById(R.id.home_value);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initdb();
        initward(Constants.ward_id);
        updateViews();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.ward_select_menu, menu);

        MenuItem item = menu.findItem(R.id.ward_select);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wardNos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) item.getActionView();
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(ward_id - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                OtherSettingDbHelper dbHelper = new OtherSettingDbHelper(getApplicationContext());
                dbHelper.getWritableDatabase();
                switch (position) {

                    case 0:
                        ward_id = 1;
                        initward(ward_id);
                        initdb();
                        updateViews();
                        break;

                    case 1:
                        ward_id = 2;
                        initward(ward_id);
                        initdb();
                        updateViews();
                        break;

                    case 2:
                        ward_id = 3;
                        initward(ward_id);
                        initdb();
                        updateViews();
                        break;
                    case 3:
                        ward_id = 4;
                        initward(ward_id);
                        initdb();
                        updateViews();
                        break;
                    case 4:
                        ward_id = 4;
                        initward(ward_id);
                        initdb();
                        updateViews();
                        break;
                    case 5:
                        ward_id = 6;
                        initward(ward_id);
                        initdb();
                        updateViews();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                ward_id = Constants.ward_id;
            }
        });

//        return super.onCreateOptionsMenu(menu);    // Once uncomment and check this line and comment the below one "return true;"

        return true;
    }

    private void initward(int ward_id) {

        OtherSettingDbHelper odh = new OtherSettingDbHelper(getApplicationContext());

        Constants.ward_id = ward_id;
        odh.getWritableDatabase();
        odh.ChangeWardID(ward_id);
        odh.close();
    }

    private void initdb() {

        OtherSettingDbHelper odh = new OtherSettingDbHelper(getApplicationContext());
        odh.getReadableDatabase();
        ward_id = Integer.parseInt(odh.GetWardId());
        odh.close();

        DbHelper dbHelper = new DbHelper(getApplicationContext());
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbHelper.getReadableDatabase();
        wardNos = dbHelper.getWard();
        death_count = dbHelper.countData("deaths", ward_id);
        house_count = dbHelper.countData("houses", ward_id);
        family_count = dbHelper.countData("families", ward_id);
        individuals_count = dbHelper.countIndividuals(ward_id);

        Constants.DEATH_ROW_SIZE = death_count;
        dbHelper.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateViews() {

        deathscount.setText(String.valueOf(death_count));
        individuals.setText(String.valueOf(individuals_count));
        families.setText(String.valueOf(family_count));
        houses.setText(String.valueOf(house_count));
    }

    //--------------- Switch case for clicking on buttons --------------------------//

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.home_btn:

                constraintLayout1.setVisibility(View.VISIBLE);
                constraintLayout2.setVisibility(View.GONE);
//                constraintLayout3.setVisibility(View.GONE);
                constraintLayout4.setVisibility(View.GONE);
//                setContentView(R.layout.activity_home_form_view);
//                Intent intent1 = new Intent(MainActivity.this, DeathFormViewActivity.class);
//                startActivity(intent1);
                break;

            case R.id.family_btn:

                setContentView(R.layout.activity_family_form_view);

//                Intent intent2 = new Intent(MainActivity.this, DeathFormViewActivity.class);
//                startActivity(intent2);
                break;

            case R.id.individual_btn:

                Intent intent3 = new Intent(MainActivity.this, DeathFormViewActivity.class);
                startActivity(intent3);
                break;

            case R.id.death_btn:

                Intent intent4 = new Intent(MainActivity.this, DeathFormViewActivity.class);
                startActivity(intent4);
                break;
        }
    }
}

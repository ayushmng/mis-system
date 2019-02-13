package com.example.ayush.missystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ayush.missystem.SqliteHelper.DbHelper;

public class DeathFormViewActivity extends AppCompatActivity {

    Button button1, button2;
    EditText editText1, editText2, editText3;

    String value_house_no, value_death_oneyear, value_no_of_death, value_cause, value_cause_other, value_ward_id, value_updateby;
    public int ward_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death_form_view);

        editText1 = findViewById(R.id.house_no);
        editText2 = findViewById(R.id.no_of_death);
        editText3 = findViewById(R.id.death_reason_other);

        ward_id = Constants.ward_id;
        getValuesFromDatabase();
        initautohouseData();
        intilizeSpinnerData();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadData();
            }
        });
    }

    private void getDataFromField() {

        value_house_no = editText1.getText().toString();
        value_no_of_death = no_of_death.getText().toString();
        value_cause_other = death_reason_other.getText().toString();
        value_ward_id = String.valueOf(ward_id);
        value_updateby = String.valueOf("1");
    }

    private void uploadData() {

        DbHelper db = new DbHelper(this);
        db.getWritableDatabase();
        db.AddDeathData(value_house_no, value_death_oneyear, value_no_of_death, value_cause, value_cause_other, value_ward_id, value_updateby);
        db.close();
    }
}

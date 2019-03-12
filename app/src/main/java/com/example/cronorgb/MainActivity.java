package com.example.cronorgb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author rafa_
 * @version 2.0
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bcrono;
    Button btemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bcrono = findViewById(R.id.bCrono);
        btemp = findViewById(R.id.bTemp);
        bcrono.setOnClickListener(this);
        btemp.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCrono:
                Intent intCron = new Intent(MainActivity.this, crono.class);
                startActivity(intCron);
                break;
            case R.id.bTemp:
                Intent intTemp = new Intent(MainActivity.this, tempo.class);
                startActivity(intTemp);
                break;
        }
    }
}

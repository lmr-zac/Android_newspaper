package qz.rg.newspaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import qz.rg.newspaper.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        findViewById(R.id.iv_back).setOnClickListener(v->finish());
    }
}
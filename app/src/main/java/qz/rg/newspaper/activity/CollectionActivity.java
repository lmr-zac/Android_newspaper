package qz.rg.newspaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import qz.rg.newspaper.R;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        findViewById(R.id.iv_back).setOnClickListener(v->finish());
    }
}
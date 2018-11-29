package com.example.npc.doannhung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UnlockActivity extends AppCompatActivity {

    EditText txtPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_activity);

        txtPin = findViewById(R.id.txtPin);
    }

    public void SendPin(View view) {
        if(txtPin.getText().length()<=0){
            Toast.makeText(getApplicationContext(),"Enter PIN please!",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
            intent.putExtra("PIN",txtPin.getText().toString());
            setResult(66,intent);
            finish();
        }
    }
}

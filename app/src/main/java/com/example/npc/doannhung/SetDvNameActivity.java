package com.example.npc.doannhung;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetDvNameActivity extends AppCompatActivity {

    EditText txtNewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dv_name);

        txtNewName = findViewById(R.id.txtNewDiviceName);
        txtNewName.setText(MainActivity.LOCK_NAME);
    }

    public void changeDeviceName(View view) {
        if(txtNewName.getText().toString().length()>0){
            SharedPreferences preferences = getSharedPreferences(MainActivity.SAVE_FILE,this.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(MainActivity.SAVE_FILE,txtNewName.getText().toString());
            editor.commit();
            Toast.makeText(getApplicationContext(),"Device Name had changed",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Enter new device name, please!",Toast.LENGTH_SHORT).show();
        }
    }
}

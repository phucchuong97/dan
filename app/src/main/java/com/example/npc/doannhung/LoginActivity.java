package com.example.npc.doannhung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView lblRegister;
    private EditText txtUser, txtPass;
    private CheckBox chkRemember;

    public static final String SH_PRE_NAME = "info_login";
    private SharedPreferences sharedPreferences;
    private final String USER = "username";
    private final String PASS = "password";
    private final String CHECK_SAVE = "checkSave";
    public static final String LOGIN_STATE = "loginState";

    private String user = "";
    private String pass = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        sharedPreferences = getSharedPreferences(SH_PRE_NAME, MODE_PRIVATE);

        addControls();
        addEvents();

        ReadInfo();
        boolean check = sharedPreferences.getBoolean(LOGIN_STATE,false);
        if(check)
            btnLogin.performClick();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });
        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ReadInfo();
        btnLogin.performClick();
    }

    private void handleRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void handleLogin() {
        if (txtUser.getText().length() <= 0) {
            Toast.makeText(getApplicationContext(), "Enter username, please.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtPass.getText().length() <= 0) {
            Toast.makeText(getApplicationContext(), "Enter password, please.", Toast.LENGTH_SHORT).show();
            return;
        }

        CheckInfo();

    }

    private void ReadInfo() {

        boolean check =sharedPreferences.getBoolean(CHECK_SAVE,false);
        if(check){
            txtUser.setText(sharedPreferences.getString(USER,""));
            txtPass.setText(sharedPreferences.getString(PASS,""));
            chkRemember.setChecked(true);
        }
    }

    private void SaveInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(chkRemember.isChecked()) {
            user = txtUser.getText().toString();
            pass = txtPass.getText().toString();
            editor.putString(USER, user);
            editor.putString(PASS, pass);
            editor.putBoolean(CHECK_SAVE, true);
        }else{
            editor.putBoolean(CHECK_SAVE,false);
        }
        editor.putBoolean(LOGIN_STATE,true);
        editor.commit();
    }

    private void CheckInfo() {
        user = txtUser.getText().toString();
        pass = txtPass.getText().toString();

        // lay du lieu tu database
        if ("abc".equals(user) && "123".equals(pass)) {

            SaveInfo();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "username or password is not correct", Toast.LENGTH_SHORT).show();
        }
    }


    private void addControls() {
        txtPass = findViewById(R.id.txtPassword);
        txtUser = findViewById(R.id.txtUserName);
        btnLogin = findViewById(R.id.btnLogin);
        lblRegister = findViewById(R.id.lblRegister);
        chkRemember = findViewById(R.id.chkSaveInfo);
    }

}

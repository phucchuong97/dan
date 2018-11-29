package com.example.npc.doannhung;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeUnlockPin extends AppCompatActivity {

    private EditText txtCurPass, txtNewPass, txtConfirm;
    private static final String OK = "ok";
    private static final String FAIL = "fail";
    private static Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_unlock_pin);
        context = this;

        txtCurPass = findViewById(R.id.txtCurPass);
        txtNewPass = findViewById(R.id.txtNewPass);
        txtConfirm = findViewById(R.id.txtComfirm);
    }

    public void changeUnlockPass(View view) {
        /**
         * b1: gui pass sang khoa
         * b2: neu ok thi doi ben arduino, dong man hinh
         *      else: show thong bao sai
         */
        if (check()) {
            MainActivity.bt.sendMessage(txtCurPass.getText().toString() + "," + txtNewPass.getText().toString());
        }
    }


    public static Handler handler_ChangePass = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (context != null && message.what == Bluetooth.MESSAGE_READ) {
                int bytes = message.arg1;
                byte[] buffer = (byte[]) message.obj;
                String s = new String(buffer, 0, bytes - 1);
                AlertDialog builder;
                if (s.equals(OK)) {
                    //Toast.makeText(context, "Change PIN successfully", Toast.LENGTH_SHORT).show();
                    builder = new AlertDialog.Builder(context).setTitle("inform").setMessage("Change PIN successfully").show();

                }
                if (s.equals(FAIL)) {
                    //Toast.makeText(context, "Change PIN unsuccessfully", Toast.LENGTH_SHORT).show();
                    builder = new AlertDialog.Builder(context).setTitle("inform").setMessage("Change PIN unsuccessfully").show();
                }
            }
            return false;
        }
    });

    private boolean check() {
        if (txtConfirm.getText().toString().length() > 0) {
            String newPass = txtNewPass.getText().toString();
            String confirm = txtConfirm.getText().toString();
            if (newPass.length() > 0 && confirm.length() > 0) {
                if (newPass.equals(confirm)) {
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "New password and confirm do not match !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter new password and confirm, please.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter current password, please.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        context = null;
    }
}
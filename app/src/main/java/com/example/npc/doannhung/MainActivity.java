package com.example.npc.doannhung;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BLUETOOTH = 6;
    private static final int REQUEST_UNLOCK = 66;
    private static final String TAG = "Main";

    public static Bluetooth bt;
    public static BluetoothAdapter bluetoothAdapter;

    public static String LOCK_NAME="HC-05";
    public static final String SAVE_FILE="deviceName";

    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init a bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            new AlertDialog.Builder(MainActivity.this).setTitle("Not compatible")
                    .setMessage("your device is not support bluetooth").setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).show();
        }

        // init LOCK_NAME
        // if cant find device in the sharedpreference, use the default name.
        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_FILE,MODE_PRIVATE);
        String name = sharedPreferences.getString(SAVE_FILE,"");
        if(name.length()>0) LOCK_NAME = name;

        addControls();
        addEvents();
    }

    private void addEvents() {
        handlingRefresh();
    }

    private void addControls() {
        bt = new Bluetooth(bluetoothAdapter,handler);
        txtStatus = findViewById(R.id.txtstatus);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mRefresh:
                handlingRefresh();
                return true;
            case R.id.mHistory:

                return true;
            case R.id.mTerminal:
                Intent intent = new Intent(MainActivity.this,TerminalActivity.class);
                startActivity(intent);
                return true;
            case R.id.mSetDvName:
                Intent intent1 = new Intent(MainActivity.this,SetDvNameActivity.class);
                startActivity(intent1);
                return true;
            case R.id.mAbout:

                return true;
        }
        return false;
    }

    private void handlingRefresh() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }
        if(bluetoothAdapter.isEnabled()){
            // if bluetooth enabled, auto connected to HC-05
            connectService(LOCK_NAME);
        }
    }


    public void connectService(String Device) {
        try {
            txtStatus.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice(Device);
                Log.d(TAG, "Btservice started - listening");
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                txtStatus.setText("Bluetooth Not enabled");
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to start bt ", e);
            txtStatus.setText("Unable to connect " + e);
        }
    }


    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1){
                        case Bluetooth.STATE_CONNECTED:
                            txtStatus.setText("Connected");
                            break;
                        case Bluetooth.STATE_NONE:
                            txtStatus.setText("");
                            break;
                        case Bluetooth.STATE_CONNECTING:
                            txtStatus.setText("Connecting...");
                            break;
                    }
                    break;

                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    TerminalActivity.handler_Terminal.obtainMessage(Bluetooth.MESSAGE_READ,msg.arg1,msg.arg2,msg.obj).sendToTarget();
                    ChangeUnlockPin.handler_ChangePass.obtainMessage(Bluetooth.MESSAGE_READ,msg.arg1,msg.arg2,msg.obj).sendToTarget();
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME " + msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST " + msg);
                    break;
            }
            return false;
        }
    });

    public void handlingUnlock(View view) {
        Intent intent = new Intent(MainActivity.this,UnlockActivity.class);
        startActivityForResult(intent,REQUEST_UNLOCK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_UNLOCK && resultCode==REQUEST_UNLOCK){
            String s = data.getStringExtra("PIN");
            bt.sendMessage(s);
        }
    }

    public void handlingChangeUnlockPin(View view) {
        Intent intent = new Intent(MainActivity.this,ChangeUnlockPin.class);
        startActivity(intent);
    }
}
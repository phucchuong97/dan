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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BLUETOOTH = 6;
    public static final int REQUEST_DEVICE_NAME = 66;
    private static final String TAG = "Main";
    public static Bluetooth bt;
    public static BluetoothAdapter bluetoothAdapter;
    //
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
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        addControls();
        addEvents();
    }

    private void addEvents() {

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
            case R.id.mScan:
                handlingScan();
                return true;
            case R.id.mHistory:

                return true;
            case R.id.mTerminal:
                Intent intent = new Intent(MainActivity.this,TerminalActivity.class);
                startActivity(intent);
                return true;
            case R.id.mSignOut:
                handlingSignout();
                return true;
            case R.id.mAbout:

                return true;
        }
        return false;
    }

    private void handlingSignout() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SH_PRE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LoginActivity.LOGIN_STATE, false);
        editor.commit();
        startActivity(intent);
        finish();
    }

    private void handlingScan() {
        if (bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
            startActivityForResult(intent, REQUEST_DEVICE_NAME);
        } else {
            Toast.makeText(MainActivity.this, "Bluetooth is off", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DEVICE_NAME && resultCode == REQUEST_DEVICE_NAME) {
            String name = data.getStringExtra("name");
            Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
            connectService(name);
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
                //txtStatus.setText("Connected");
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

            //String s = "";
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    //s = "MESSAGE_STATE_CHANGE: " + msg.arg1;
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    //s = "MESSAGE_WRITE ";
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    //s = "MESSAGE_READ " + new String((byte[]) msg.obj);

                    TerminalActivity.handler_Terminal.obtainMessage(Bluetooth.MESSAGE_READ,msg.arg1,msg.arg2,msg.obj).sendToTarget();
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME " + msg);
                    //s = "MESSAGE_DEVICE_NAME ";
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST " + msg);
                    //s = "MESSAGE_TOAST ";
                    break;
            }
            //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            return false;
        }
    });
}
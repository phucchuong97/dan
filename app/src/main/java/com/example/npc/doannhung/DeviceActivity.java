package com.example.npc.doannhung;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class DeviceActivity extends AppCompatActivity {

    private ArrayList<BTDevice> pairedDevice;
    private ArrayAdapter<BTDevice> adapterPaired;
    private ListView lvPairedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);


        setTitle("Choose device");
        this.setFinishOnTouchOutside(false);
        addControls();
        addEvents();
    }

    private void addEvents() {

            Set<BluetoothDevice> list = MainActivity.bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : list) {
                BTDevice d = new BTDevice(device.getName(), device.getAddress());
                pairedDevice.add(d);
            }
            adapterPaired.notifyDataSetChanged();
    }

    private void addControls() {
        lvPairedDevice = findViewById(R.id.lvPairedDevice);
        pairedDevice = new ArrayList<>();
        adapterPaired = new ArrayAdapter<>(DeviceActivity.this,android.R.layout.simple_list_item_1,pairedDevice);
        lvPairedDevice.setAdapter(adapterPaired);

        lvPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getIntent();
                intent.putExtra("name",pairedDevice.get(i).getName());
                //Log.e("Selected Item",pairedDevice.get(i).getAddress());
                setResult(MainActivity.REQUEST_DEVICE_NAME,intent);
                finish();
            }
            });
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

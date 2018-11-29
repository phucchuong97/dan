package com.example.npc.doannhung;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TerminalActivity extends AppCompatActivity {

    private static ArrayList<String> data = new ArrayList<>();
    private TextView txtSend;
    private Button btnSend;
    private ListView lvSendReceive;
    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.terminal_layout);

        addControls();
        addEvents();
    }

    private void addEvents() {

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = txtSend.getText().toString();

                MainActivity.bt.sendMessage(s);

                data.add(s + "\n");
                txtSend.setText("");
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void addControls() {
        txtSend = findViewById(R.id.txtSend);
        btnSend = findViewById(R.id.btnSend);
        lvSendReceive = this.findViewById(R.id.lvSendReceive);

        data.clear();
        adapter = new ArrayAdapter<>(TerminalActivity.this, android.R.layout.simple_list_item_1, data);
        lvSendReceive.setAdapter(adapter);
    }

    public static Handler handler_Terminal = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == Bluetooth.MESSAGE_READ) {
                int bytes = message.arg1;
                byte[] buffer = (byte[]) message.obj;
                String s = new String(buffer, 0, bytes - 1);
                showMessage(s);
            }

            return false;
        }
    });

    private static void showMessage(String s) {
        if (adapter != null) {
            data.add(s + "\n");
            adapter.notifyDataSetChanged();
        }
    }
}

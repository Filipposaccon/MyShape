package com.example.myshape;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1;
    String listElement;
    BluetoothDevice targetDevice;
    BluetoothSocket btSocket;
    OutputStreamWriter writer;
    int ConnessioneOK = -1;
    int colori[]=new int [3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // il dispositivo non supporta Bluetooth
            Toast.makeText(this, "Il dispositivo non supporta la connettività bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button btnTermina = (Button) findViewById(R.id.btnTermina);
        btnTermina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("rgb"));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // se il bluetooth  è abilitato elenca i dipositivi accoppiati
        else elencaPairedDevices();

        super.onPostCreate(savedInstanceState);
    }

    private void elencaPairedDevices() {

        // insieme dei dispositivi associati
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        ListView listView1 = (ListView) findViewById(R.id.lvDispositivi);

        // elenca in un ArrayList i dispositivi associati
        ArrayList<String> arrayList1 = new ArrayList<>();
        for (BluetoothDevice pairedDevice : pairedDevices)
            arrayList1.add(pairedDevice.getName());

        // crea un ArrayAdapter per l'elenco dei dispositivi
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, R.layout.paired_device_row, arrayList1);
        listView1.setAdapter(arrayAdapter1);

        // attiva un listener per il listView
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // individua un dispositivo della lista a cui inviare "Ciao Mondo!"
                listElement = (String) parent.getItemAtPosition(position);
                ConnessioneOK = 0;
                sayHelloToDevice(listElement);
            }
        });

    }

    // Il metodo stabilisce una connessione seriale con il dispositivo ed invia la
    // stringa "Ciao Mondo!
    private void sayHelloToDevice(String deviceName) {
        Intent myIntent = new Intent(Bluetooth.this, MainActivity.class);


        UUID SPP_UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        // individua il dispositivo  partire dal suo nome
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        targetDevice = null;
        for (BluetoothDevice pairedDevice : pairedDevices)
            if (pairedDevice.getName().equals(deviceName)) {
                targetDevice = pairedDevice;
                break;
            }

        // se il dispositivo non viene trovato nella lista viene segnalato l'errore
        if (targetDevice == null) {
            Toast.makeText(this, "Dispositivo non trovato", Toast.LENGTH_SHORT).show();
            ConnessioneOK = 1;
            return;
        }

        // crea un socket di connessione con il dispositivo
        btSocket = null;
        try {
            btSocket = targetDevice.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            Toast.makeText(this, "Impossibile creare un socket seriale con il dispositivo", Toast.LENGTH_SHORT).show();
            ConnessioneOK = 2;
            return;
        }

        // connessione con il dispositivo
        try {
            btSocket.connect();
            Bluetooth.this.startActivity(myIntent);
        } catch (IOException e) {
            Toast.makeText(this, "Impossibile connettersi con il dispositivo", Toast.LENGTH_SHORT).show();
            ConnessioneOK = 3;
            return;
        }

        try {
            writer = new OutputStreamWriter(btSocket.getOutputStream());
            writer.write("Ciao Mondo!\r\n");
            writer.flush();
        } catch (IOException e) {
            Toast.makeText(this, "Impossibile inviare il messaggio al dispositivo", Toast.LENGTH_SHORT).show();
        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            colori = intent.getIntArrayExtra("rgb");
            System.out.println(colori.toString());
            inviacolori();
        }
    };
    void inviacolori()
    {
        try {
            System.out.println("Messaggio:"+colori[0]+"-"+colori[1]+"-"+colori[2]);
            writer = new OutputStreamWriter(btSocket.getOutputStream());
            String messaggio= colori[0]+"-"+colori[1]+"-"+colori[2];
            writer.write(messaggio);
            writer.flush();
        } catch (IOException e) {
            Toast.makeText(this, "Impossibile inviare il messaggio al dispositivo", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // controlla l'esito di una richiesta di abilitazione del bluetooth
        if (requestCode == REQUEST_ENABLE_BT)

            // se l'utente ha risposto NO oppure non si è abilitato il bluetooth
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth non abilitato", Toast.LENGTH_SHORT).show();
                finish();
            } else elencaPairedDevices();

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((ConnessioneOK == 0) || (ConnessioneOK > 3)) {
            try {
                btSocket.close();
                Toast.makeText(this, "Chiusura della comunicazione con il dispositivo", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Impossibile chiudere la comunicazione con il dispositivo", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}

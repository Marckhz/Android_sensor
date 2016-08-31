package com.example.marco.sensor;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.DropBoxManager;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView texto;
    private TextView texto1;
    private TextView texto2;
    private Socket socket;
    private float[] arraySensor = new float[3];
    private String socketOutput;
    private OutputStream outputStream;


    public MainActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        texto = (TextView) findViewById(R.id.textView);
        texto1 = (TextView) findViewById(R.id.textView2);
        texto2 = (TextView) findViewById(R.id.textView3);

        new Thread(new SocketThread()).start();
    }
    class SocketThread implements Runnable {

            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.0.6", 12349);
                }
                catch (UnknownHostException b){
                    b.printStackTrace();
                }
                catch (IOException b1){
                    b1.printStackTrace();
                }

            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(final SensorEvent event) {

        texto.setText(Float.toString(event.values[0]));
        texto1.setText(Float.toString(event.values[1]));
        texto2.setText(Float.toString(event.values[2]));/* cambio Float.toString por String.valueO */

        arraySensor[0] = event.values[0];
        arraySensor[1] = event.values[1];
        arraySensor[2] = event.values[2];

        socketOutput = (Float.toString(event.values[0]));

        try {
            PrintWriter output = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            output.println(socketOutput);
        }catch (java.io.IOException b) {
                b.printStackTrace();
        }


    }
}








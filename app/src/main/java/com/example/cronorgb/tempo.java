package com.example.cronorgb;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.cronorgb.R.*;
import static com.example.cronorgb.R.id.editTIni;

public class tempo extends AppCompatActivity {
    /**
     * Constantes
     */
    private static final String LOGCRONOMETRO = "LogsCronometro";

    /**
     *  Variables
     */
    private boolean salir;
    private boolean parado;
    private boolean iniciar;
    private boolean reiniciar;
    private boolean corriendo;

    /**
     * Variables tiempo
     */
    private long tiempoCorriendo;
    private long tiempoInicioParado;
    private long tiempoInicioCorriendo;
    private long tiempoParado;
    private long tiempoAhora;
    private long tiempoTotal;
    private long tiempoInicioTotal;


    /**
     * Botones
     */
    private Button btIniciar;
    private Button btParar;
    private Button btFinalizar;

    /**
     * TextView
     */
    TextView tiempoIni;
    private TextView tvContador;


    /**
     * Hilos
     */
    private tempo.IniciarCronometro temporizador;

    /**
     * Arrays
     */
    private String tiempos [];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_tempo);

        tiempoIni = (TextView)findViewById(editTIni);

        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btIniciar.setEnabled(false);
                btParar.setEnabled(true);
                btFinalizar.setEnabled(true);

                Log.i(LOGCRONOMETRO,"Iniciamos el Temporizador");
                temporizador = new tempo.IniciarCronometro();
                temporizador.execute();
                temporizador.iniciar();
            }
        });

        btParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btParar.getText().toString().compareTo(getResources().getString(string.reiniciar))==0){
                    temporizador.reiniciar();
                } else {
                    temporizador.parar();
                }
            }
        });

        btFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               temporizador.cancel(true);
                btIniciar.setEnabled(false);
                btParar.setEnabled(false);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class IniciarCronometro extends AsyncTask<Void,String,Void> {

        void iniciar(){
            if(parado){
                parado = false;
                tiempoInicioCorriendo = System.currentTimeMillis();
                tiempoParado = 0;
            }
        }

        void parar(){
            if (tiempoInicioParado==0){
                tiempoInicioParado = System.currentTimeMillis();
            }
            parado = true;
            corriendo = false;
            btParar.setText(getResources().getString(string.reiniciar));
        }

        void reiniciar(){
            parado = false;
            corriendo = true;
            tiempoParado = tiempoAhora-tiempoInicioParado-tiempoParado;
            btParar.setText(getResources().getString(string.parar));
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btParar.setEnabled(true);
            btFinalizar.setEnabled(true);

            tvContador.setText((editTIni);

            corriendo = true;
            tiempoInicioTotal = System.currentTimeMillis();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            tvContador.setText(values[0]);

        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            while (!isCancelled()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tiempoAhora = System.currentTimeMillis();
                tiempos[0] = obtenerTiempoTranscurrido(tiempoInicioTotal,tiempoAhora,0);
                publishProgress(tiempos);
            }
            return null;
        }
    }

    private void iniciarVariables(){
        parado = true;
        iniciar = false;
        reiniciar = false;
        salir = false;
        tiempoCorriendo = 0;

        btFinalizar = findViewById(id.btFinalizar);
        btIniciar = findViewById(id.btIniciar);
        btParar = findViewById(id.btParar);

        tvContador = findViewById(id.contador);

        tiempos = new String [1];
    }

    private String obtenerTiempoTranscurrido(long startTime, long timeNow, long timeStopped) {
        long diferencia;
        int hours, minutes, seconds, millis;
        String strMillis;

        diferencia = timeNow - startTime-timeStopped;
        hours = (int) diferencia / 3600000;
        diferencia = diferencia % 3600000;
        minutes = (int) diferencia / 60000;
        diferencia = diferencia % 60000;
        seconds = (int) diferencia / 1000;
        diferencia = diferencia % 1000;
        millis = (int) diferencia;
        try {
            strMillis = String.format("%03d", millis).substring(0, 2);
            millis = Integer.parseInt(strMillis);
        } catch (IndexOutOfBoundsException e) {
        }

        String res;
        res = String.format("%d:%02d:%02d.%02d", hours, minutes,
                seconds, millis);
        return res;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


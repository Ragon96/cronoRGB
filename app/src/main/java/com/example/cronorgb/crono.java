package com.example.cronorgb;

import android.annotation.SuppressLint;
import android.content.Intent;
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


public class crono extends AppCompatActivity {
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
    private TextView tvContadorTotal;
    private TextView tvContadorCorriendo;
    private TextView tvContadorPausa;


    /**
     * Hilos
     */
    private IniciarCronometro cronometro;

    /**
     * Arrays
     */
    private String tiempos [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crono);
        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btIniciar.setEnabled(false);
                btParar.setEnabled(true);
                btFinalizar.setEnabled(true);

                cronometro = new IniciarCronometro();
                cronometro.execute();
                cronometro.iniciar();
            }
        });

        btParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btParar.getText().toString().compareTo(getResources().getString(R.string.reiniciar))==0){cronometro.reiniciar();
                } else {
                    cronometro.parar();
                }
            }
        });

        btFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cronometro.cancel(true);
                btIniciar.setEnabled(false);
                btParar.setEnabled(false);
            }
        });
    }

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
            btParar.setText(getResources().getString(R.string.reiniciar));
        }

        void reiniciar(){
            parado = false;
            corriendo = true;
            tiempoParado = tiempoAhora-tiempoInicioParado-tiempoParado;
            btParar.setText(getResources().getString(R.string.parar));
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btParar.setEnabled(true);
            btFinalizar.setEnabled(true);

            tvContadorCorriendo.setText(getResources().getString(R.string.tiempo_inicial));
            tvContadorPausa.setText(getResources().getString(R.string.tiempo_inicial));
            tvContadorTotal.setText(getResources().getString(R.string.tiempo_inicial));

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

            if(corriendo){
                tvContadorCorriendo.setText(values[0]);
            } else if (parado){
                tvContadorPausa.setText(values[1]);
            }

            tvContadorTotal.setText(values[2]);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            Intent intCron = new Intent(crono.this, resultados.class);
            startActivity(intCron);
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
                if (corriendo) {
                    tiempos[0] = obtenerTiempoTranscurrido(tiempoInicioCorriendo,tiempoAhora,tiempoParado);
                } else {
                    tiempos[1] = obtenerTiempoTranscurrido(tiempoInicioParado,tiempoAhora,tiempoParado);
                }
                tiempos[2] = obtenerTiempoTranscurrido(tiempoInicioTotal,tiempoAhora,0);
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
        tiempoParado = 0;
        tiempoTotal = 0;

        btFinalizar = findViewById(R.id.btFinalizar);
        btIniciar = findViewById(R.id.btIniciar);
        btParar = findViewById(R.id.btParar);

        tvContadorCorriendo = findViewById(R.id.contador_corriendo);
        tvContadorPausa = findViewById(R.id.contador_pausa);
        tvContadorTotal = findViewById(R.id.contador_total);

        tiempos = new String [3];
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

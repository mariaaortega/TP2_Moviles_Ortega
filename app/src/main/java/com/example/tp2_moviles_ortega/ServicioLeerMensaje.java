package com.example.tp2_moviles_ortega;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;

public class ServicioLeerMensaje extends Service {
    private static boolean runFlag = true;
   // Uri sms =  Uri.parse("content://sms/inbox");
    public ServicioLeerMensaje() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread trabajador = new Thread(new Contador());
        trabajador.start();
        runFlag=true;
        return START_STICKY;

    }

    @Override
    public void onDestroy(){
        runFlag = false;
        Log.d("salida","Servicio parado");
         }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Contador implements Runnable{
        private String remitente;
        private String cuerpo_sms;
        private String fecha;

        public void run(){
            //long contador = 0;
            while(runFlag==true){
                Uri sms =  Uri.parse("content://sms");
                Cursor c_sms=getContentResolver().query(sms,null,null,null,null);
                if (c_sms.getCount()>0){
                    try{
                        int cant_sms= 5;
                        if(c_sms.getCount()<5){
                            cant_sms= c_sms.getCount();
                        }
                        while(c_sms.moveToNext() && c_sms.getPosition()<cant_sms){
                            fecha=c_sms.getString(c_sms.getColumnIndexOrThrow(Telephony.Sms.DATE));
                            remitente=c_sms.getString(c_sms.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                            cuerpo_sms=c_sms.getString(c_sms.getColumnIndexOrThrow(Telephony.Sms.BODY));
                            Log.d("Fecha",new Date(Long.parseLong(fecha)).toString());
                            Log.d("Remitente",remitente);
                            Log.d("Contenido_sms",cuerpo_sms);
                        }

                        Thread.sleep(9000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }


              //  Log.d("Salida", contador+"");
            }
        }
    }
}

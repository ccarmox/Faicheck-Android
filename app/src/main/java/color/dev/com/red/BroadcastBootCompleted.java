/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            FragmentoHorario.tarea.renovarRecordatorios(context);
        }
    }

}
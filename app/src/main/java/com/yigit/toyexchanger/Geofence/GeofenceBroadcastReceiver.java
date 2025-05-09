package com.yigit.toyexchanger.Geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }
//        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "DİKKAT!! Tehlikeli alana girdiniz!", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("TEHLİKEDESİN","Dikkat !! Tehlikeli Alana Girdiniz!", "", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "TEHLİKE BÖLGEDESİNİZ", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("TEHLİKEDESİN","TEHLİKEli BÖLGEDESİNİZ ŞUAN", "", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "Tehlikeli Alandan Çıktınız!", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GÜVENDESİN","Tehlikeli Alandan Çıktınız!", "", MapsActivity.class);
                break;
        }

    }
}

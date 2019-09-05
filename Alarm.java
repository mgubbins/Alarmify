package com.example.mgubb.alarmify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by mgubb on 8/17/2019.
 */

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm......", Toast.LENGTH_LONG).show();

        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.startSong(context);
    }
}

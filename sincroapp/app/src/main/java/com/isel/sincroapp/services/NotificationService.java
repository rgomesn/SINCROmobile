package com.isel.sincroapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.DistanceInfraction;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.Radar;
import com.isel.sincroapp.data.entities.RedLightInfraction;
import com.isel.sincroapp.data.entities.SpeedInfraction;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.main.MainViewModel;
import com.isel.sincroapp.data.model.main.MainViewModelFactory;
import com.isel.sincroapp.ui.infraction_detail.InfractionDetailActivity;
import com.isel.sincroapp.util.ServerResult;
import com.isel.sincroapp.util.Utils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationService extends LifecycleService {
    private static final String CHANNEL_ID = "20";
    private Citizen citizen;
    private User user;
    private String token;
    public static long NOTIFY_INTERVAL = 300 * 1000; // 300 seconds - 5 minutes

    public NotificationService() {

    }

    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            citizen = (Citizen) intent.getSerializableExtra("citizen");
            user = (User) intent.getSerializableExtra("user");
            token = intent.getStringExtra("token");
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("sync_time")) {
                    NOTIFY_INTERVAL = Long.parseLong(sharedPreferences.getString("sync_time", String.valueOf(NOTIFY_INTERVAL)));
                    mTimer = new Timer();
                    mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(citizen, user, token, NotificationService.this), 0, NOTIFY_INTERVAL);
                    Toast.makeText(NotificationService.this, "Intervalo de sincronização alterado", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(citizen, user, token, this), 0, NOTIFY_INTERVAL);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    class TimeDisplayTimerTask extends TimerTask {
        private Citizen citizen;
        private User user;
        private String token;
        private SINCROServerService sincroServerService;
        private Context context;
        private MutableLiveData<ServerResult<?>> newInfractionsServerResult = new MutableLiveData<>();

        public TimeDisplayTimerTask(Citizen citizen, User user, String token, Context context) {
            this.citizen = citizen;
            this.user = user;
            this.token = token;
            this.context = context;
            Retrofit retrofit = Utils.getRetrofit(context, token);
            sincroServerService = retrofit.create(SINCROServerService.class);
        }

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getNewInfractions(citizen);
                }
            });
        }

        public void getNewInfractions(final Citizen citizen) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));

            sincroServerService.getNewInfractions(jsonObject).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        JsonObject obj = response.body();
                        Gson gson = new Gson();
                        List<Infraction> infractionList = new ArrayList<Infraction>();
                        Type sListType = new TypeToken<ArrayList<SpeedInfraction>>() {}.getType();
                        Type rlListType = new TypeToken<ArrayList<RedLightInfraction>>() {}.getType();
                        Type dListType = new TypeToken<ArrayList<DistanceInfraction>>() {}.getType();
                        infractionList.addAll((Collection<? extends Infraction>) gson.fromJson(obj.get("speedInfractions"), sListType));
                        infractionList.addAll((Collection<? extends Infraction>) gson.fromJson(obj.get("redLightInfractions"), rlListType));
                        infractionList.addAll((Collection<? extends Infraction>) gson.fromJson(obj.get("distanceInfractions"), dListType));

                        NotificationManager notificationManager = (NotificationManager)
                                getSystemService(NOTIFICATION_SERVICE);

                        int id = 1;

                        for (Infraction i : infractionList) {
                            /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationService.this);
                            mBuilder.setContentTitle("Nova multa");
                            mBuilder.setContentText("Veículo: " + i.getLicence_plate());
                            mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
                            Intent resultIntent = new Intent(NotificationService.this, InfractionDetailActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("citizen", citizen);
                            bundle.putSerializable("user", user);
                            bundle.putString("token", token);
                            bundle.putSerializable("infraction", i);

                            resultIntent.putExtras(bundle);

                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                    new Random().nextInt(), resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(resultPendingIntent);

                            notificationManager.notify(id++, mBuilder.build());*/
                            Intent intent = new Intent(NotificationService.this, InfractionDetailActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("citizen", citizen);
                            bundle.putSerializable("user", user);
                            bundle.putString("token", token);
                            bundle.putSerializable("infraction", i);

                            intent.putExtras(bundle);

                            PendingIntent pendingIntent =
                                    PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle("Nova infração")
                                    .setContentText("Veículo: " + i.getLicence_plate())
                                    .setContentIntent(pendingIntent);

                            /*NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "SINCROapp", NotificationManager.IMPORTANCE_HIGH);

                            notificationManager.createNotificationChannel(mChannel);*/

                            notificationManager.notify(id++, mBuilder.build());
                        }

                        newInfractionsServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                    }
                    else {
                        newInfractionsServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    newInfractionsServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
                }
            });

            sincroServerService.getNewInfractions(jsonObject);
        }
    }
}

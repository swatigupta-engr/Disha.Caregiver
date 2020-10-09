package com.zuccessful.trueharmony.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zuccessful.trueharmony.R;
import com.zuccessful.trueharmony.application.SakshamApp;
import com.zuccessful.trueharmony.pojo.Medication;
import com.zuccessful.trueharmony.pojo.MedicineRecord;
import com.zuccessful.trueharmony.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zuccessful.trueharmony.activities.LoginActivity.PREF_CID;
import static com.zuccessful.trueharmony.activities.LoginActivity.PREF_PID;

public class MedService extends Service {
    private FirebaseFirestore db;
    private SakshamApp app;
     SimpleDateFormat sdf;
    Uri soundUri;
    public MedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate()
    {
        Log.d("TAG"," MedService");
        app = SakshamApp.getInstance();
        db = app.getFirebaseDatabaseInstance();
        sdf = Utilities.getSimpleDateFormat();
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        getMedDetails();
        return START_STICKY;
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void getMedDetails()
    {
        db.collection("patient_med_reports/" + getApplicationContext().getSharedPreferences(PREF_CID, MODE_PRIVATE).getString(PREF_PID, "error") +
                "/" + sdf.format(new Date())).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (doc.getType()) {
                                case ADDED:
                                    Log.d("TAG", "New Msg: " + doc.getDocument().toObject(Message.class));
                                    break;
                                case MODIFIED:
                                    MedicineRecord ob=doc.getDocument().toObject(MedicineRecord.class);
                                    Log.d("TAG", "Modified Msg: " + ob.getName()+" "+ob.getTime());
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"channel1")
                                            .setSmallIcon(R.drawable.ic_saksham_icon)
                                            .setContentTitle("Saksham Notification")
                                            .setContentText("New response for : "+ob.getName()+" "+ob.getTime())
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setSound(soundUri);
                                    NotificationManager mNotificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    {
                                        String channelId = "Mychannel_id";
                                        CharSequence name = getString(R.string.channel_name);
                                        String description = getString(R.string.channel_description);
                                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                        NotificationChannel channel = new NotificationChannel(channelId, name,importance);
                                        mNotificationManager.createNotificationChannel(channel);
                                        builder.setChannelId(channelId);
                                    }

                                    mNotificationManager.notify(0, builder.build());

                                    break;
                                case REMOVED:
                                    Log.d("TAG", "Removed Msg: " + doc.getDocument().toObject(Message.class));
                                    break;
                            }
                        }
                        Log.d("TAG","--- change occured");
                    }
                });
    }
}

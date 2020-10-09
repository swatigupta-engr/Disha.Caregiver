package com.zuccessful.trueharmony.adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.zuccessful.trueharmony.R;
import com.zuccessful.trueharmony.application.SakshamApp;
import com.zuccessful.trueharmony.pojo.DailyRoutine;
import com.zuccessful.trueharmony.pojo.DailyRoutineRecord;
import com.zuccessful.trueharmony.pojo.Medication;
import com.zuccessful.trueharmony.pojo.MedicineRecord;
import com.zuccessful.trueharmony.pojo.WeeklyTask;
import com.zuccessful.trueharmony.utilities.Constants;
import com.zuccessful.trueharmony.utilities.Utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BeginAgainAdapter extends RecyclerView.Adapter<BeginAgainAdapter.MyViewHolder>
{

    private ArrayList<WeeklyTask> dailytaskList;
    private SakshamApp app=SakshamApp.getInstance();;
    private FirebaseFirestore db=app.getFirebaseDatabaseInstance();
    private SimpleDateFormat sdf;
    private String t_date;
    private HashMap<String,Boolean> map;

    ArrayList<String> distinct_tasks=new ArrayList<>();
    ArrayList<String> distinct_subtasks=new ArrayList<>();
    ArrayList<String> distinct_subtasks_status=new ArrayList<>();

    private Boolean setFirebase=false;
    private Context mContext;
    private ArrayList<DailyRoutine> alarms ;
    DocumentReference ref;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, subtask_name;
        public CheckBox checkBox_p, checkBox_c;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.med_name);
            subtask_name = (TextView) view.findViewById(R.id.subtask_name);
            checkBox_p = (CheckBox) view.findViewById(R.id.checkbox_patient);
            checkBox_c= (CheckBox) view.findViewById(R.id.checkbox_caregiver);
            checkBox_c.setVisibility(View.GONE);
            checkBox_p.setEnabled(false);
        }
    }

    public BeginAgainAdapter(ArrayList<WeeklyTask> dailylist, Context c)
    {
        Log.v("tag","in begin again adapter");

        this.dailytaskList = dailylist;
        sdf = Utilities.getSimpleDateFormat();
        t_date=sdf.format(new Date());
        mContext=c;
/*
        try {
          //  alarms = Utilities.getListOfDailyRoutineAlarms();

        }catch (Exception e){

        }
        if(Utilities.getDailyLog_bool()!=null && Utilities.getDailyLog_bool().containsKey(t_date)==true)
        {
            Log.d("logss","Entry found for today");
            map= Utilities.getDailyLog_bool().get(t_date);
            Log.d("logss",map+"");
        }*/
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_begin_again, parent, false);
        Log.d("logss","on create view holder");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d("logss","In bind view holder");
        final SimpleDateFormat sdf = Utilities.getSimpleDateFormat();
        final String today = sdf.format(new Date());
        WeeklyTask rec = dailytaskList.get(position);
        final String name=rec.getName();

        Log.v("RECORD:",rec+"");
      //  final String time=rec.getTime();
        holder.name.setText( (distinct_tasks.get(position)));


      //  holder.name.setText( (rec.getName()));
      //  holder.time.setText(rec.getTime());
//Log.v("swati:::::",map.get(name)+"/"+name);
        if(map!=null && map.containsKey(name))
        {
            Log.d("saumya","Entry for today exists");
            Boolean status= map.get(name);
            if(status) {
                holder.checkBox_c.setChecked(true);
            }
            else
            {
                holder.checkBox_c.setChecked(false);
            }
        }


      /*  try {

            for (int j = 0; j < alarms.size(); j++) {
                Map<String, Boolean> days = alarms.get(j).getDays();
                {
                    ArrayList<String> slot_list = alarms.get(j).getReminders();
                    int slots = slot_list.size();
                    for (int k = 0; k < slots; k++) {
                        final String time1 = slot_list.get(k);
                        Log.v("data", alarms.get(j).getName() + "..." + name);
                        if (alarms.get(j).getName().equalsIgnoreCase(name))

                            ref = db.collection("patient_daily_routine_reports/" + app.getAppUser(null).getId() + "/" + today + "/").document(name + " " + time1);

                        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    Log.v("Exists", document.exists() + "");

                                    if (document.exists()) {

                                        Log.d("logss", " setting using firebase");

                                        try {
                                            Long status = (Long) document.get("done");
                                            //   Boolean status = document.getBoolean("done");
                                            Log.v("Swati: taken status:", name + " name" + status + "");
                                            if (status == 1)
                                                holder.checkBox_p.setChecked(true);
                                            else
                                                holder.checkBox_p.setChecked(false);
                                            // Boolean status2 = document.getBoolean("caregiver_status");
                                            // holder.checkBox_c.setChecked(status2);
                                            setFirebase = true;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else {
                                    Log.d("logss", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            }


        }catch (Exception e){}

*/

        // ArrayList<WeeklyTask> tasks = Utilities.getListOfWeeklytasks();
        ArrayList<WeeklyTask> tasks=dailytaskList;
        Log.v("__ aren",distinct_tasks+""+distinct_tasks.size());

        for (int j = 0; j < distinct_tasks.size(); j++)
        {   String current_date_cap;
            Calendar calendar = Calendar.getInstance();

            Date date = calendar.getTime();

            current_date_cap  =capitalize(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()).toLowerCase());
            final HashMap<String ,String >subtasks=   tasks.get(j).getSubtasks();
            Log.v("current", subtasks.get(current_date_cap)+"");
           // String subtask_name= (subtasks.get(current_date_cap).split("\\$")[0]);

            String subtask_name="";
            String status="false";
              subtask_name= (distinct_subtasks.get(position));
                  status= distinct_subtasks_status.get(position);




         Log.v("seeting",subtask_name);
            holder.subtask_name.setText(subtask_name);

            // String status=Boolean.getBoolean(subtasks.get(current_date_cap).split("\\$")[0]);
          //  String status= (subtasks.get(current_date_cap).split("\\$")[1]);

            Log.v("__ subtasks_____",subtasks+"\n current cdate"+current_date_cap);

            try {
                Log.v("Status:::", status + "");
                if(status.equalsIgnoreCase(("true")))
                    holder.checkBox_p.setChecked(true);
                else holder.checkBox_p.setChecked(false);
            }catch (Exception e){
                e.printStackTrace();
            }
            final Boolean patient_status=holder.checkBox_p.isChecked();
            Log.d("logss"," status for patient "+name+" "+""+" "+patient_status);
        }

     /*   if(setFirebase==false && map!=null && map.containsKey(name+","+""))
        {
            Log.d("logss"," setting using local storage");
            Boolean v= map.get(name);

            if(v==true) {
                holder.checkBox_c.setChecked(true);
            }

            else
            {
                holder.checkBox_c.setChecked(false);
            }
        }*/

        holder.checkBox_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                if(checkBox.isChecked())
                {
                    Log.d("logss",name+" checkbox tick");
                    //  db.collection("patient_daily_routine_reports/"+app.getAppUser(null).getId() + "/" + today+"/").document(name+" "+time).update("caregiver_status",true);
                 //   Utilities.saveDailyLog(name,t_date,time,true,mContext);
                }
                else
                {
                    Log.d("logss",name+ " checkbox Un-tick");
                    //   db.collection("patient_daily_routine_reports/"+app.getAppUser(null).getId() + "/" + today+"/").document(name+" "+time).update("caregiver_status",false);
                  //  Utilities.removeEntryDailyLog(name,t_date);
                }
            }
        });



    }

    public void replaceMeds(ArrayList<WeeklyTask> taskList) {
        Log.d("logss","replacing medss"+taskList);
        dailytaskList = taskList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {



        int subtask_size=0;
        String sub_task_name=null;

        String distinct_subtask_status="false";

        for (int j = 0; j < dailytaskList.size(); j++) {
            String current_date_cap;
            Calendar calendar = Calendar.getInstance();
            if (!distinct_tasks.contains(dailytaskList.get(j).getName()))

            { Date date = calendar.getTime();
            Log.v("swati123", dailytaskList.get(j).getName() + "");

            current_date_cap = capitalize(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()).toLowerCase());
            final HashMap<String, String> subtasks = dailytaskList.get(j).getSubtasks();
              sub_task_name = subtasks.get(current_date_cap).split("\\$")[0];

               distinct_subtask_status=  subtasks.get(current_date_cap).split("\\$")[1];
               Log.v("distinct ",distinct_subtasks+"");
            if(!distinct_subtasks.contains(sub_task_name)&& sub_task_name.length()>0)

            {  distinct_subtasks.add((sub_task_name));
                distinct_subtasks_status.add(distinct_subtask_status);
            }


            if(sub_task_name.length()>0){
                distinct_tasks.add(dailytaskList.get(j).getName());

            }

            Log.v("my::",distinct_tasks+"\n"+distinct_subtasks);
            }

            //    subtask_size = (subtasks.get(current_date_cap).split("\\$")[0]).length();
            //    }
        }

            return

                    distinct_tasks.size();

    }
/*
    String change_language( String activity){
        String hindi_activity=activity;
        String langPrefType   = Utilities.getDataFromSharedpref(  mContext.getApplicationContext(), Utilities.KEY_LANGUAGE_PREF);
        if(langPrefType!=null) {
            int lang = Integer.parseInt(langPrefType);
            Log.v("Lang",langPrefType+" "+lang);

            if(lang==1) {
                //language is hindi
                if (activity.equalsIgnoreCase("Swimming")) {
                    hindi_activity = "तैरना";
                } else if (activity.equalsIgnoreCase("Walk")) {
                    hindi_activity = "सैर करना";

                } else if (activity.equalsIgnoreCase("Run")) {
                    hindi_activity = "भागना";

                } else if (activity.equalsIgnoreCase("Play")) {
                    hindi_activity = "खेलना";

                }
                if (activity.equalsIgnoreCase("Cycling")) {
                    hindi_activity = "साइकिल चलाना";

                }

                if (activity.equalsIgnoreCase("Exercise")) {
                    hindi_activity = "व्यायाम";

                } else if (activity.equalsIgnoreCase("Other")) {
                    hindi_activity = "अन्य";

                } else if (activity.equalsIgnoreCase("Wake-Up")) {
                    hindi_activity = "उठ जाओ ";

                } else if (activity.equalsIgnoreCase("Brush")) {
                    hindi_activity = "ब्रश";

                } else if (activity.equalsIgnoreCase("Bath")) {
                    hindi_activity = "स्नान";

                } else if (activity.equalsIgnoreCase("Sleep")) {
                    hindi_activity = "सोना";

                } else if (activity.equalsIgnoreCase("Breakfast")) {
                    hindi_activity = "नाश्ता";

                } else if (activity.equalsIgnoreCase("Lunch")) {
                    hindi_activity = "दोपहर का भोजन";

                } else if (activity.equalsIgnoreCase("Dinner")) {
                    hindi_activity = "रात का भोजन";

                }
            } else{
                //language is english

                hindi_activity=activity;
            }

        }else {
//            language is english by default
            hindi_activity=activity;

        }
        return hindi_activity;
    }
*/
public static String capitalize(String str)
{
    if(str == null) return str;
    return str.substring(0, 1).toUpperCase() + str.substring(1);
}
}


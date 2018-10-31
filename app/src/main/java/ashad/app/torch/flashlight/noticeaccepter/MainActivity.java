package ashad.app.torch.flashlight.noticeaccepter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements adapterofitem.Onitemclicklistner {
    RecyclerView recyclerView;
    adapterofitem adapterofitemyo;
    ArrayList<String> noticetiotle;
    ArrayList<String> dateandtime;
    ArrayList<String> noticetitle;
    ArrayList<String> urlofnotice;
    ArrayList<String> sendedby;
    ArrayList<String> approvedornot;
    ArrayList<String> UniqueId;
    int position = 0;



    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference db=database.getReference();
    DatabaseReference userref = db.child("Images").child("URL");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
        }
        else
        {
            showdailog();
        }



        noticetiotle=new ArrayList<>();
        dateandtime=new ArrayList<>();
        noticetitle=new ArrayList<>();
        urlofnotice=new ArrayList<>();
        sendedby=new ArrayList<>();
        approvedornot=new ArrayList<>();
        UniqueId=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerviewofcomalin1);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        adapterofitemyo=new adapterofitem(noticetitle,getApplicationContext());

        adapterofitemyo.setonitemclicklistner(this);
        recyclerView.setAdapter(adapterofitemyo);


//        Query query=userref.child("Images").child("URL").child("approvedornot").equalTo("False");



        userref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                doyourwork(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                doyourwork(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                doyourwork(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                doyourwork(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Error " + databaseError ,Toast.LENGTH_LONG).show();

            }
        });


    }



    @Override
    public void onitemclick(int position) {

        Intent i=new Intent(getApplicationContext(),noticeindetail.class);

        i.putExtra("titleofnotice",noticetitle.get(position));
        i.putExtra("noticetiotle",noticetiotle.get(position));
        i.putExtra("dateandtime",dateandtime.get(position));
        i.putExtra("urlofnotice",urlofnotice.get(position));
        i.putExtra("sendedby",sendedby.get(position));
        i.putExtra("approvedornot",approvedornot.get(position));
        i.putExtra("UniqueId",UniqueId.get(position));
        startActivity(i);

    }

    public void doyourwork(DataSnapshot dataSnapshot)
    {
        Log.i("gettingwhat"," are date datasnaplshot"+dataSnapshot);


        try {
            nticegetter nticegetteryo=dataSnapshot.getValue(nticegetter.class);

            String approvedornotcheck=nticegetteryo.getApprovedornot();
            Log.i("approevedornot","is"+approvedornotcheck);
            String noticetext=nticegetteryo.getNotice();
            String noticedate=nticegetteryo.getDateandtime();
            String noticetitlemain=nticegetteryo.getTitle();
            String noticeurlofnotice=nticegetteryo.getImageurl();
            String sendedbyby=nticegetteryo.getSendedby();
            String UUniqueId=nticegetteryo.getUniqueId();
            if (approvedornotcheck.equals("False"))
            {
                dateandtime.add(noticedate);
                noticetiotle.add(noticetext);
                noticetitle.add(noticetitlemain);
                urlofnotice.add(noticeurlofnotice);
                sendedby.add(sendedbyby);
                approvedornot.add(approvedornotcheck);
                UniqueId.add(UUniqueId);
    //        Log.i("gettingwhat"," are date arraay"+dateandtime);
    //        Log.i("gettingwhat"," are text arraay"+noticetiotle);
    //        Log.i("gettingwhat"," are title arraay"+noticetitle);
    //        Log.i("gettingwhat"," are url arraay"+urlofnotice);

                adapterofitemyo.notifyItemInserted(position);
                adapterofitemyo.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        Log.i("gettingwhat"," are date "+noticedate);
//        Log.i("gettingwhat"," are text"+noticetext);
//        Log.i("gettingwhat"," are title"+noticetitlemain);
//        Log.i("gettingwhat"," are url"+noticeurlofnotice);



    }






    public void showdailog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.Theme_AppCompat_Light_Dialog_Alert));
        builder.setMessage("Opps! Something went wrong are you connected to internet ")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        //todo bhar ka rasta dika o
                        startActivity(i);
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();




    }



}

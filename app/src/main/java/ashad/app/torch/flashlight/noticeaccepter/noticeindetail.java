package ashad.app.torch.flashlight.noticeaccepter;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class noticeindetail extends AppCompatActivity {

    TextView detailtextoftitle,detailtextofdate,detailtextofdescription,sendedby,approvedornot;
    Button accept,reject,report;
    ImageView detailimageviewofnotice;
    ProgressBar progressBar;
    ScrollView needtohide;
    String urlofpic;
    String UniqueId;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference db=database.getReference();
    DatabaseReference userref = db.child("Images").child("URL");
    DatabaseReference approvedtrigger=db.child("NotificationApproved");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeindetail);






     //TextView detailtextoftitle,detailtextofdate,detailtextofdescription;
        //    ImageView detailimageviewofnotice;

        detailtextoftitle=findViewById(R.id.detailtextoftitle);
        detailtextofdate=findViewById(R.id.detailtextofdate);
        detailtextofdescription=findViewById(R.id.detailtextofdescription);
        detailimageviewofnotice=findViewById(R.id.detailimageviewofnotice);
        progressBar = findViewById(R.id.progressBar2);
        needtohide=findViewById(R.id.needtohide);
        sendedby=findViewById(R.id.sendedby);
        approvedornot=findViewById(R.id.approvedornot);
        accept=findViewById(R.id.accept);
        reject=findViewById(R.id.reject);
        report=findViewById(R.id.report);
        Intent i=getIntent();
//  i.putExtra("titleofnotice",noticetitle);
//        i.putExtra("noticetiotle",noticetiotle);
//        i.putExtra("dateandtime",dateandtime);
//        i.putExtra("urlofnotice",urlofnotice);
        UniqueId=i.getStringExtra("UniqueId");

        detailtextoftitle.setText(i.getStringExtra("titleofnotice"));
        detailtextofdate.setText(i.getStringExtra("dateandtime"));
        detailtextofdescription.setText(i.getStringExtra("noticetiotle"));
        approvedornot.setText(i.getStringExtra("approvedornot"));
        sendedby.setText(i.getStringExtra("sendedby"));
//glide now
        //Picasso.with(mcontext).load(urlofimmage).fit().centerInside().into(holder.imageofnews);
        urlofpic=i.getStringExtra("urlofnotice");
        Log.i("gettingwhat","image url"+urlofpic);

//        Picasso.get().load(urlofpic).fit().centerInside().into(detailimageviewofnotice);
//
//        Glide.with(getApplicationContext()).load(urlofpic).into(detailimageviewofnotice);
        Toast.makeText(getApplicationContext(), "Loading..." , Toast.LENGTH_SHORT).show();
        Glide.with(getApplicationContext())
                .load(urlofpic).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Loading Failed " + e , Toast.LENGTH_SHORT).show();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(detailimageviewofnotice);


        detailimageviewofnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),Imageinzoom.class);

                i.putExtra("urlofpic",urlofpic);
                startActivity(i);
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //accept notice

                userref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {


                            nticegetter nticegetteryo=dataSnapshot.getValue(nticegetter.class);

                            try {
                                String UUniqueIdneweverytime=nticegetteryo.getUniqueId();
                                Log.i("UUniqueis ","is "+UUniqueIdneweverytime);
                                Log.i("UUniqueis ","is "+UniqueId);


                                if (UUniqueIdneweverytime.equals(UniqueId))
                                {
                                    int o=0;
                                    Log.i("HOWMANY","is "+o++);
                                    Log.i("approvedor","not is "+UUniqueIdneweverytime);
                                    DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                    String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                    String path = "/" + dataSnapshot.getKey() + "/" + "approvedornot";
                                    String needtoupdatelike = userref.child(path).getKey();



                                    userref.child(path).setValue("True");
                                    Toast.makeText(noticeindetail.this, "DONE", Toast.LENGTH_SHORT).show();
                                    approvedtrigger.push().setValue(0);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(),"Error " + databaseError ,Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });






    }








}

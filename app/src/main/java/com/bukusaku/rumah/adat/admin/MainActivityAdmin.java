package com.bukusaku.rumah.adat.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bukusaku.rumah.adat.R;
import com.bukusaku.rumah.adat.activity.AdminAdd;
import com.bukusaku.rumah.adat.activity.AdminRemove;
import com.bukusaku.rumah.adat.mnmain;
import com.bukusaku.rumah.adat.models.UserMode;
import com.bukusaku.rumah.adat.panduan;
import com.bukusaku.rumah.adat.play.Main;
import com.bukusaku.rumah.adat.play.hello.HelloSceneformActivity;
import com.bukusaku.rumah.adat.tentang;
import com.bukusaku.rumah.adat.viewPager.CustomViewPager;
import com.bukusaku.rumah.adat.viewPager.FloatingActionMenu;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityAdmin extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    UserMode U = new UserMode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.floatingMenu);
        menu.setClickEvent(new FloatingActionMenu.ClickEvent() {
            @Override
            public void onClick(int index) {
                if (index == 0){
                    Intent i = new Intent(MainActivityAdmin.this, AdminAdd.class);
                    startActivity(i);
                }else if (index == 2){
                    Intent i = new Intent(MainActivityAdmin.this, AdminRemove.class);
                    startActivity(i);
                }
                //Log.d("TAG", String.valueOf(index)); // index of clicked menu item
            }
        });
        /*IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE,false);
                String networkStatus = isNetworkAvailable ? "Connected":"Disconnected";
                Snackbar.make(findViewById(R.id.content_main),"Network Status : "+ networkStatus,Snackbar.LENGTH_LONG).setDuration(7200).show();
            }
        }, intentFilter);*/

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PerpustakaanAdmin.this, AdminAddBook.class);
                startActivity(i);
            }
        });*/


    }
        //==============================================================================================

}
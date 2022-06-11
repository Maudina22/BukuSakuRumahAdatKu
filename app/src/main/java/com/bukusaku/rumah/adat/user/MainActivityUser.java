package com.bukusaku.rumah.adat.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bukusaku.rumah.adat.R;
import com.bukusaku.rumah.adat.mnmain;
import com.bukusaku.rumah.adat.panduan;
import com.bukusaku.rumah.adat.play.hello.HelloSceneformActivity;
import com.bukusaku.rumah.adat.tentang;

public class MainActivityUser extends AppCompatActivity {
    Button btnmain, btncoba, btnpanduan, btntentang, btnkeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        btnmain = findViewById(R.id.btnmain);
        btnmain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), mnmain.class);
                startActivity(i);
            }
        });

        btnpanduan = findViewById(R.id.btnpanduan);
        btnpanduan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), panduan.class);
                startActivity(i);
            }
        });

        btntentang = findViewById(R.id.btntentang);
        btntentang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), tentang.class);
                startActivity(i);
            }
        });

        btnkeluar = findViewById(R.id.btnkeluar);
        btnkeluar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });
////////
        btncoba = findViewById(R.id.btncoba);
        btncoba.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HelloSceneformActivity.class);
                finish();
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed(){
        showAlertDialog();
    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Anda Yakin Ingin Keluar?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

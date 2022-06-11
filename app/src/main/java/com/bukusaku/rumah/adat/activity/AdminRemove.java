package com.bukusaku.rumah.adat.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bukusaku.rumah.adat.R;
import com.bukusaku.rumah.adat.models.Objek;

public class AdminRemove extends AppCompatActivity implements View.OnClickListener {


    private Button findBook;
    private TextInputLayout editBid1;
    FirebaseFirestore db;
    private ProgressDialog progressDialog;
    Objek b1;


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_remove_objek);
        findBook=(Button)findViewById(R.id.findBook);
        editBid1=(TextInputLayout)findViewById(R.id.editBid1);
        FirebaseApp.initializeApp(this);
        db=FirebaseFirestore.getInstance();
        findBook.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        b1=new Objek();



        findBook.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        if(v==findBook) {
            if (editBid1.getEditText().getText().toString().trim().isEmpty()) {

                progressDialog.cancel();
                editBid1.setError("Objek Id Required ");
                editBid1.setErrorEnabled(true);
                return;
            }

            String id = editBid1.getEditText().getText().toString().trim();
            db.document("Book/" + id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            AlertDialog.Builder alert= new AlertDialog.Builder(AdminRemove.this);
                            b1 = task.getResult().toObject(Objek.class);
                            String temp = "Judul : " + b1.getTitle() + "\nCategory : " + b1.getType() + "\nNo. of Units : " + b1.getTotal();
                            progressDialog.cancel();
                            alert.setMessage(temp).setTitle("Please Confirm !").setCancelable(false).setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                    progressDialog.setMessage("Removing ... ");
                                    progressDialog.show();
                                    if(b1.getAvailable()==b1.getTotal())
                                    {
                                        db.document("Book/"+editBid1.getEditText().getText().toString().trim()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.cancel();
                                                Toast.makeText(AdminRemove.this, "Book Removed", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.cancel();
                                                Toast.makeText(AdminRemove.this, "Try Again !", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else
                                    {   progressDialog.cancel();
                                        Toast.makeText(AdminRemove.this, "This Book is issued to Users !\nReturn before Removing this Book.", Toast.LENGTH_LONG).show();

                                    }

                                }
                            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog alertDialog=alert.create();
                            alertDialog.show();


                        } else {
                            progressDialog.cancel();
                            Toast.makeText(AdminRemove.this, "No such Book found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.cancel();
                        Toast.makeText(AdminRemove.this, "Try Again !", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
}



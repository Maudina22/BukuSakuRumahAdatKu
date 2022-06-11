package com.bukusaku.rumah.adat.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bukusaku.rumah.adat.admin.MainActivityAdmin;
import com.bukusaku.rumah.adat.user.MainActivityUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.bukusaku.rumah.adat.R;
import com.bukusaku.rumah.adat.SharedPref;
import com.bukusaku.rumah.adat.models.UserMode;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    FirebaseFirestore db;
    TextInputLayout editID,editPass;
    Button buttonSignIn;
    TextView toSignUp;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        FirebaseApp.initializeApp(SignInActivity.this);
        editID = (TextInputLayout) findViewById(R.id.editID);
        editPass = (TextInputLayout) findViewById(R.id.editPass);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        toSignUp = (TextView) findViewById(R.id.toSignUp);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null) {
           progressDialog.setMessage("Harap Tunggu ... Membuat Anda Masuk!");
           progressDialog.show();
           String cur = auth.getCurrentUser().getEmail().trim();
           db.document("User/"+cur).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {
                   UserMode obj=documentSnapshot.toObject(UserMode.class);
                   if(obj.getType()==0)
                   {
                       progressDialog.cancel();
                       startActivity(new Intent(getApplicationContext(), MainActivityUser.class));
                       finish();

                   }
                   else
                   {
                       progressDialog.cancel();
                       startActivity(new Intent(getApplicationContext(), MainActivityAdmin.class));
                       finish();

                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   progressDialog.cancel();
                   Toast.makeText(SignInActivity.this, "Harap Masuk Lagi", Toast.LENGTH_SHORT).show();
               }
           });

        }
        buttonSignIn.setOnClickListener(this);
        toSignUp.setOnClickListener(this);
    }

    private boolean verifyEmailId()
    {
        String emailId=editID.getEditText().getText().toString().trim();
        if(emailId.isEmpty())
        {   editID.setErrorEnabled(true);
            editID.setError("Email Required");
            return true;
        }
        else
        {
            editID.setErrorEnabled(false);
            return false;
        }
    }


    private boolean verifyPass()
    {
        String pass=editPass.getEditText().getText().toString().trim();
        if(pass.isEmpty())
        {   editPass.setErrorEnabled(true);
            editPass.setError("Password Required");
            return true;
        }
        else
        {
            editPass.setErrorEnabled(false);
            return false;
        }
    }

    private void signinUser() {

        boolean res= (verifyEmailId()|verifyPass());
        if(res==true)
            return;
        String id=editID.getEditText().getText().toString().trim();
        String pass=editPass.getEditText().getText().toString().trim();
        progressDialog.setMessage("Masuk ... ");
        progressDialog.show();

        auth.signInWithEmailAndPassword(id,pass).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String id=editID.getEditText().getText().toString().trim();
                    db.collection("User").whereEqualTo("email",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            UserMode obj=new UserMode();
                            for(QueryDocumentSnapshot doc:queryDocumentSnapshots)
                               obj=doc.toObject(UserMode.class);


                            db.document("User/"+auth.getCurrentUser().getEmail()).update("fcmToken", SharedPref.getInstance(SignInActivity.this).getToken())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(SignInActivity.this, "Registered for Notifications Successfully !", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(SignInActivity.this, "Registration for Notifications Failed !\nPlease Sign in Again to Retry", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                            if(obj.getType()==0)
                            {
                                progressDialog.cancel();
                                Toast.makeText(SignInActivity.this,"Signed in !",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivityUser.class));
                                finish();

                            }
                            else
                            {
                                progressDialog.cancel();
                                Toast.makeText(SignInActivity.this,"Signed in !",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivityAdmin.class));
                                finish();

                            }
                        }
                    });
                }

                else
                {   progressDialog.cancel();
                    Toast.makeText(SignInActivity.this,"Email/Password Salah atau Sambungan Buruk! Coba lagi ",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v == buttonSignIn)
            signinUser();

        else if (v == toSignUp) {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(getResources().getDrawable(R.drawable.ic_close)).setTitle("Keluar")
                .setMessage("Anda yakin ingin keluar?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                        finish();
                    }
                }).setNegativeButton("Tidak", null).show();
    }
}


package com.bukusaku.rumah.adat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.bukusaku.rumah.adat.R;
import com.bukusaku.rumah.adat.models.UserMode;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout editName, editPass1, editID, editPass, editCardNo, editAlamat;
    Button buttonRegister;
    TextView toSignIn;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    CheckBox check1;
    FirebaseFirestore db;
    int temp;
    int type;
    int card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editID = (TextInputLayout) findViewById(R.id.editID);
        editPass = (TextInputLayout) findViewById(R.id.editPass);
        editPass1=(TextInputLayout)findViewById(R.id.editPass1);
        editName=(TextInputLayout)findViewById(R.id.editName);
        editAlamat=(TextInputLayout)findViewById(R.id.editAlamat);
        editCardNo=(TextInputLayout)findViewById(R.id.editCardNo);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        toSignIn = (TextView) findViewById(R.id.toSignIn);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        check1=(CheckBox)findViewById(R.id.check1);

        editPass1.setEnabled(true);
        editPass.setEnabled(true);
        editName.setEnabled(true);
        editAlamat.setEnabled(true);
        editID.setEnabled(true);
        editCardNo.setEnabled(false);

        editCardNo.setErrorEnabled(false);

        editID.setErrorEnabled(false);
        editName.setErrorEnabled(false);
        editAlamat.setErrorEnabled(false);
        editPass.setErrorEnabled(false);
        editPass1.setErrorEnabled(false);

        FirebaseApp.initializeApp(this);
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        buttonRegister.setOnClickListener(this);
        toSignIn.setOnClickListener(this);
        check1.setOnClickListener(this);

    }

    private boolean verifyName()
    {

        String name=editName.getEditText().getText().toString().trim();
        if(name.isEmpty())
        {   editName.setErrorEnabled(true);
            editName.setError("Name Required");
            return true;
        }
        else
        {
            editName.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyAlamat()
    {

        String alamat=editAlamat.getEditText().getText().toString().trim();
        if(alamat.isEmpty())
        {   editAlamat.setErrorEnabled(true);
            editAlamat.setError("Alamat Required");
            return true;
        }
        else
        {
            editAlamat.setErrorEnabled(false);
            return false;
        }
    }

    /*private boolean verifyCardNo()
    {
        String cardNo=editCardNo.getEditText().getText().toString().trim();
        if(cardNo.isEmpty())
        {   editCardNo.setErrorEnabled(true);
            editCardNo.setError("Card No. Required");
            return true;
        }
        else
        {
            editCardNo.setErrorEnabled(false);
            return false;
        }
    }*/

    private boolean verifyEmailId()
    {
        String emailId=editID.getEditText().getText().toString().trim();
        if(emailId.isEmpty())
        {   editID.setErrorEnabled(true);
            editID.setError(" Email ID Required");
            return true;
        }
        else if(!emailId.endsWith("@gmail.com"))
        {
            editID.setErrorEnabled(true);
            editID.setError(" Gunakan Gmail Akun");
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
            editPass.setError(" Password Required");
            return true;
        }
        else
        {
            editPass.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyPass1()
    {
        String pass1=editPass1.getEditText().getText().toString().trim();
        String pass=editPass.getEditText().getText().toString().trim();
        if(pass1.isEmpty())
        {   editPass1.setErrorEnabled(true);
            editPass1.setError("Confirm Password Required");
            return true;
        }
        else if(pass.equals(pass1))
        {
            editPass1.setErrorEnabled(false);
            return false;
        }
        else
        {
            editPass1.setErrorEnabled(true);
            editPass1.setError("Passwords do not match");
            return true;
        }
    }

    private boolean verifyCard1()
    {
        db.collection("User").whereEqualTo("card",FieldValue.increment(50)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                   temp=task.getResult().size();
                }
            }
        });
        if(temp==0)
            return false;
        else
            return true;

    }

    private void registerUser()
    {
        boolean res= (verifyName()|verifyAlamat()|verifyEmailId()|verifyPass()|verifyPass1());
        if(res==true) return;

        String id=editID.getEditText().getText().toString().trim();
        String pass=editPass.getEditText().getText().toString().trim();
        progressDialog.setMessage("Mendaftarkan Pengguna ...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(id,pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String id=editID.getEditText().getText().toString().trim();
                    String name=editName.getEditText().getText().toString().trim();
                    String alamat=editAlamat.getEditText().getText().toString().trim();

                    type=0;
                    //int card=Integer.parseInt(editCardNo.getEditText().getText().toString().trim());
                    db.collection("User").document(id).set(new UserMode(name,alamat,id,type)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this,"Berhasil Terdaftar !",Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Silakan Coba Lagi !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    progressDialog.cancel();
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(SignUpActivity.this,"Sudah terdaftar ! ",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Registrasi gagal ! Coba lagi ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v==check1)
        {
            if(check1.isChecked())
                buttonRegister.setEnabled(true);
            else
                buttonRegister.setEnabled(false);
        }
        else if(v==buttonRegister)
            registerUser();

        else if(v==toSignIn) {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }
}
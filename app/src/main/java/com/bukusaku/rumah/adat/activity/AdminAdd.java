package com.bukusaku.rumah.adat.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.bukusaku.rumah.adat.R;
import com.bukusaku.rumah.adat.models.Objek;
import com.squareup.picasso.Picasso;

public class AdminAdd extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputLayout editTitle;
    private TextInputLayout editBid;
    private TextInputLayout editUnits;
    private Spinner spinner1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private StorageTask mUploadTask;
    Button button1,button2;
    ImageView img;
    String type;
    ProgressDialog p1;

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_objek);
        FirebaseApp.initializeApp(this);
        editTitle=(TextInputLayout)findViewById(R.id.editTitle);
        editBid=(TextInputLayout) findViewById(R.id.editBid);
        editUnits=(TextInputLayout)findViewById(R.id.editUnits);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button_choose_image);
        img = (ImageView)findViewById(R.id.chosenImageView);
        p1=new ProgressDialog(this);
        p1.setCancelable(false);
        db=FirebaseFirestore.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("Objek");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        button1.setOnClickListener(this);
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(img);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private boolean verifyTitle()
    {
        String t=editTitle.getEditText().getText().toString().trim();
        if(t.isEmpty())
        {   editTitle.setErrorEnabled(true);
            editTitle.setError("Judul Required");
            return true;
        }
        else
        {
            editTitle.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyBid()
    {
        String b=editBid.getEditText().getText().toString().trim();
        if(b.isEmpty())
        {   editBid.setErrorEnabled(true);
            editBid.setError("ID Buku Required");
            return true;
        }
        else
        {
            editBid.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyUnits()
    {
        String u=editUnits.getEditText().getText().toString().trim();
        if(u.isEmpty())
        {   editUnits.setErrorEnabled(true);
            editUnits.setError("No. of Units Required");
            return true;
        }
        else
        {
            editUnits.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyCategory()
    {
        if (type.equals("Pilih Kategori Objek"))
        {
            Toast.makeText(this, "Silakan pilih Kategori Objek!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void addBook()
    {

        boolean res=(verifyBid()|verifyTitle()|verifyUnits()|verifyCategory());
        if(res==true)
            return;
        else
        {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileReference.getDownloadUrl().toString();
            p1.setMessage("Menambahkan Objek");
            p1.show();
            final String id=editBid.getEditText().getText().toString().trim();
            int id1=Integer.parseInt(id);
            ////////
            db.document("Objek/"+id1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if((task.isSuccessful())&&(task.getResult().exists()==false)) {
                        String id=editBid.getEditText().getText().toString().trim();
                        String title=editTitle.getEditText().getText().toString().trim();
                        String units=editUnits.getEditText().getText().toString().trim();
                        String image=task.getResult().toString();
                        int id1=Integer.parseInt(id);
                        int unit1=Integer.parseInt(units);
                        Objek b=new Objek(title,type,unit1,id1,image);
                        db.document("Book/"+id1).set(b).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {   p1.cancel();
                                    Toast.makeText(AdminAdd.this, "Objek Added !", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {   p1.cancel();
                                    Toast.makeText(AdminAdd.this, "Try Again !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                        {
                            p1.cancel();
                            Toast.makeText(AdminAdd.this, "This Objek is already added \n or Bad Connection !", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {

       addBook();

    }
}

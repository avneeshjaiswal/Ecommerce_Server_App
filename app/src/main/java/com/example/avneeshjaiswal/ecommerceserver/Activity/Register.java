package com.example.avneeshjaiswal.ecommerceserver.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.avneeshjaiswal.ecommerceserver.Model.User;
import com.example.avneeshjaiswal.ecommerceserver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    EditText edt_name,edt_password,edt_number;
    Button btn_register;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_name = findViewById(R.id.edt_name);
        edt_password = findViewById(R.id.edt_pass);
        edt_number = findViewById(R.id.edt_phone);
        //edt_repassword = findViewById(R.id.edt_repass);
        btn_register = findViewById(R.id.signUp);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting progress dialog box
                final ProgressDialog progressDialog = new ProgressDialog(Register.this);
                progressDialog.setMessage("Please Wait.....");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(edt_number.getText().toString()).exists()){
                            progressDialog.dismiss();
                            Toast.makeText(Register.this,"Number already exist",Toast.LENGTH_LONG).show();
                            }else {
                            progressDialog.dismiss();

                            User user = new User(edt_name.getText().toString(), edt_password.getText().toString());

                            table_user.child(edt_number.getText().toString()).setValue(user);
                            Log.i(TAG, "name: " + user.getName() + " number: " + user.getPhone() + " password:" + user.getPassword());
                            Toast.makeText(Register.this, "SignUp successfull", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent i = new Intent(Register.this, Login.class);
                            startActivity(i);
                        }}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}

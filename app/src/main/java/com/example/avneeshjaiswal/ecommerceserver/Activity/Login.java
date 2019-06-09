package com.example.avneeshjaiswal.ecommerceserver.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avneeshjaiswal.ecommerceserver.Common.Common;
import com.example.avneeshjaiswal.ecommerceserver.Model.User;
import com.example.avneeshjaiswal.ecommerceserver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText edt_number,edt_password;
    Button login;
    TextView forget_password,new_user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_number = findViewById (R.id.edt_number);
        edt_password = findViewById (R.id.edt_password);
        login = findViewById (R.id.btn_login);
        forget_password = findViewById (R.id.forget);
        new_user = findViewById (R.id.new_user);

        //setting firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("User");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edt_number.getText().toString(),edt_password.getText().toString());
            }
        });

    }

    private void signIn(String s, String s1) {
        final ProgressDialog mDialog = new ProgressDialog(Login.this);
        mDialog.setMessage("Please wait..");
        mDialog.show();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(edt_number.getText().toString()).exists()) {

                    //getting user information
                    mDialog.dismiss();
                    User user = dataSnapshot.child(edt_number.getText().toString()).getValue(User.class);
                    String pass = dataSnapshot.child(edt_number.getText().toString()).child("Password").getValue().toString();
                    //String name = dataSnapshot.child(edt_number.getText().toString()).child("Name").getValue().toString();
                    String res_id = dataSnapshot.child(edt_number.getText().toString()).child("res_id").getValue().toString();
                    Log.d(TAG, "onDataChange: pass " + pass);

                    Log.d(TAG,"restaurant_id "+res_id);
                    /*Bundle bundle = new Bundle();
                    bundle.putString("Restaurant_id",""+res_id);
*/

                    if (user.getPassword().equals(pass)) {
                        /*Bundle bundle = new Bundle();
                        bundle.putString("Restaurantid ",res_id);*/
                        Intent i = new Intent(Login.this, MainActivity.class);
                         //This is for a String
                        i.putExtra("Restaurant_id",res_id);
//                        i.putExtras(bundle);
                        Common.currentUser = user;
                        startActivity(i);
                        finish();
                        //login
                    } else {
                        Toast.makeText(Login.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(Login.this, "User doesnot exist!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

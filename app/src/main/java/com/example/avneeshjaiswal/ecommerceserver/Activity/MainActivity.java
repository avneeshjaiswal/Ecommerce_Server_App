package com.example.avneeshjaiswal.ecommerceserver.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avneeshjaiswal.ecommerceserver.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtName;
    Button add_food,update_res_info,maintain_order,discount,banner_management;
    String Token;
    String res_id = "";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);


        //FloatingActionButton fab =  findViewById(R.id.fab);
       // progress = findViewById(R.id.progress);
        add_food = findViewById(R.id.add_food);
        /*update_food = findViewById(R.id.update_food);
        delete_food = findViewById(R.id.delete_food);*/
        update_res_info = findViewById(R.id.res_info);
        maintain_order = findViewById(R.id.maintain_order);
        discount = findViewById(R.id.discount);
        banner_management = findViewById(R.id.banner);
        //chat = findViewById(R.id.chat);
       // feedback = findViewById(R.id.feedBack);


        /*Bundle bundle = getIntent().getExtras();
        String res_id = bundle.getString("Restaurantid");//this is for String*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        res_id = bundle.getString("Restaurant_id");

        Log.d(TAG,"res_id"+res_id);


        /*Intent i = getIntent();
        Bundle extra = i.getExtras();

        String id = extra.getString(Intent.EXTRA_TEXT);
        Log.d("tag","intent - "+id);*/
        /*if(extra!=null){
            Toast.makeText(MainActivity.this,"restaurant_id"+extra,Toast.LENGTH_SHORT).show();
        }*/



        banner_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,BannerManagement.class);
                startActivity(i);
            }
        });
        add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String res_id = getIntent().getStringExtra("Restaurant_id");
                //Log.d("tag","id- "+res_id);
                /*final Bundle id= new Bundle();
                id.putString("key",""+res_id);*/
                Intent i = new Intent(MainActivity.this,AddFood.class);
                i.putExtra("Restaurant_id",res_id);
                //i.putExtra("Res_id",Common.currentUser.getPhone());
                startActivity(i);
            }
        });

        update_res_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,UpdateResInfo.class);
                String res_id = getIntent().getStringExtra("Restaurant_id");
                i.putExtra("Res_id",res_id);
                startActivity(i);

            }
        });

        maintain_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,OrderStatus.class);
                startActivity(i);
            }
        });

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Discount.class);
                startActivity(i);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       /* //sending token
        Token = FirebaseInstanceId.getInstance().getToken();
        Log.e("MainActivity","Token.."+Token);
        updateToken(Token);
*/

    }
   /* private void updateToken(String token) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference tokens = firebaseDatabase.getReference("Tokens");
        Token data = new Token(token,true);//true because this token send from client app
        //Log.e("MainActivity","After current User"+Common.currentUser.getPhone());
        tokens.child(Common.currentUser.getPhone()).setValue(data);
        //Log.e("MainActivity","After current User"+Common.currentUser.getPhone());
    }
*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(MainActivity.this,OrderStatus.class);
            startActivity(intent);

        } else if (id == R.id.nav_sign_out) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

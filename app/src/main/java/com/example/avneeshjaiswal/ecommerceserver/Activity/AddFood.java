package com.example.avneeshjaiswal.ecommerceserver.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.avneeshjaiswal.ecommerceserver.Common.Common;
import com.example.avneeshjaiswal.ecommerceserver.Interface.ItemClickListener;
import com.example.avneeshjaiswal.ecommerceserver.Model.Food;
import com.example.avneeshjaiswal.ecommerceserver.Model.Request;
import com.example.avneeshjaiswal.ecommerceserver.R;
import com.example.avneeshjaiswal.ecommerceserver.View.FoodView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.UUID;

import static com.example.avneeshjaiswal.ecommerceserver.Common.Common.PICK_IMAGE_REQUEST;

public class AddFood extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Food newFood;
    public static final String TAG = "AddFood";
    FloatingActionButton fab;
    RelativeLayout rootlayout;
    EditText edt_food_name,edt_food_desc,edt_food_price;
    Button addFood,Image;
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    Uri saveUri;
    /*FirebaseStorage storage;
    StorageReference storageReference;
*/
    String res_id = "";
    FirebaseRecyclerAdapter<Food,FoodView> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        foodList = firebaseDatabase.getReference("Food");
//        storageReference = storage.getReference();

        // Log.d(TAG,"storage reference"+storageReference);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Log.d(TAG, "storage reference" + storageReference);

        //recyclerview
        recyclerView = findViewById(R.id.recycler_food_list);
        recyclerView.setHasFixedSize(true);

        rootlayout = findViewById(R.id.activityAddfoolayout);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();

                // Toast.makeText(AddFood.this, "floating buttton", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        res_id = bundle.getString("Restaurant_id");

        //getting intent
        if (getIntent() != null) {
            res_id = getIntent().getStringExtra("Restaurant_id");
            Log.d(TAG, "id " + res_id);
        }
        if (!res_id.isEmpty()) {
            Log.d(TAG, "res_id " + res_id);
            loadFood(res_id);
        }
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if(requestCode == PICK_IMAGE_REQUEST && requestCode == RESULT_OK && data != null && data.getData() != null){
            saveUri = data.getData();

            Image.setText(R.string.image_selected);
        }*/
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                saveUri = data.getData();
                Toast.makeText(this,"Uri "+saveUri,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"save uri==null",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadImage() {
        if(saveUri != null){
            Log.d("AddFood","uri "+saveUri);
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(AddFood.this,"Uploading...",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new food if image upload and we can get download link
                                    newFood = new Food();
                                    newFood.setFood_name(edt_food_name.getText().toString());
                                    newFood.setDescription(edt_food_desc.getText().toString());
                                    newFood.setPrice(edt_food_price.getText().toString());
                                    newFood.setRes_id(res_id);
                                    newFood.setImage(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(AddFood.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");
                }
            });
        }else{
            Toast.makeText(AddFood.this, "Cannot load image", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddFoodDialog() {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddFood.this);
            alertDialog.setTitle("Add New Food In Menu");
            alertDialog.setMessage("Please Fill the Information");

            LayoutInflater inflater = this.getLayoutInflater();
            View add_food_layout = inflater.inflate(R.layout.add_new_food,null);
            alertDialog.setView(add_food_layout);

           edt_food_name = add_food_layout.findViewById(R.id.edt_food_name);
           edt_food_desc = add_food_layout.findViewById(R.id.edt_food_description);
           edt_food_price = add_food_layout.findViewById(R.id.edt_food_price);
           addFood = add_food_layout.findViewById(R.id.btn_upload);
           Image = add_food_layout.findViewById(R.id.btn_image);


//setting up button functionality
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                //Toast.makeText(AddFood.this, "choose image", Toast.LENGTH_SHORT).show();
            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeImage(item);
                //Toast.makeText(AddFood.this,"food added",Toast.LENGTH_SHORT).show();
                uploadImage();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //just create new food
                    if(newFood != null){
                        foodList.push().setValue(newFood);
                        loadFood(res_id);
                        Snackbar.make(rootlayout,"New Food"+newFood.getFood_name()+" was added",Snackbar.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddFood.this,"new food is null",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    newFood = null;
                    loadFood(res_id);
                }
            });
            alertDialog.show();





        }

    private void loadFood(String res_id) {
        Query load = foodList.orderByChild("res_id").equalTo(res_id);
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(load,Food.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodView holder, int position, @NonNull Food model) {
                Log.d(TAG, "food_name : " + model.getFood_name() + " Food_image : " + model.getFood_name());
                holder.textFoodName.setText(model.getFood_name());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imgFoodImage);
                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       /* Toast.makeText(FoodList.this, "" + clickItem.getFood_name(), Toast.LENGTH_SHORT).show();
                        // Log.d(TAG, "onCreate: food list "+local);
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());//sending food id to other activity
                        startActivity(foodDetail);*/
                    }


                });

            }

            @Override
            public FoodView onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodView(itemView);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

   /* @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE)){

            showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));


        }else if(item.getTitle().equals(Common.DELETE)){

            deleteFood(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {
        foodList.child(key).removeValue();
    }

    private void showUpdateFoodDialog(final String key, final Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddFood.this);
        alertDialog.setTitle("Edit Food In Menu");
        alertDialog.setMessage("Please Fill the Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_new_food,null);

        edt_food_name = add_food_layout.findViewById(R.id.edt_food_name);
        edt_food_desc = add_food_layout.findViewById(R.id.edt_food_description);
        edt_food_price = add_food_layout.findViewById(R.id.edt_food_price);

        edt_food_name.setText(item.getFood_name());
        edt_food_desc.setText(item.getDescription());
        edt_food_price.setText(item.getPrice());

        //setting up button functionality
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //just create new food
                if(newFood != null){
                    //update info
                    item.setFood_name(edt_food_name.getText().toString());
                    item.setPrice(edt_food_price.getText().toString());
                    item.setDescription(edt_food_desc.getText().toString());

                    foodList.child(key).setValue(item);


                    Snackbar.make(rootlayout,"Food"+item.getFood_name()+" was edited",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void changeImage(final Food item) {
        if(saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(AddFood.this,"Uploading...",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new food if image upload and we can get download link
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(AddFood.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");
                }
            });
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}

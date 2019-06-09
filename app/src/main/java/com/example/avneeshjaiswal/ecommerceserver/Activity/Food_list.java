/*
package com.example.avneeshjaiswal.ecommerceserver.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.avneeshjaiswal.ecommerceserver.Interface.ItemClickListener;
import com.example.avneeshjaiswal.ecommerceserver.R;
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

import java.util.UUID;

import static com.example.avneeshjaiswal.ecommerceserver.Common.Common.PICK_IMAGE_REQUEST;

*/
/**
 * Created by avneesh jaiswal on 24-Feb-18.
 *//*


public class Food_list extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton floatingActionButton;
    Food newFood;

    RelativeLayout rootlayout;
    EditText edt_food_name,edt_food_desc,edt_food_price;
    Button btnImageSelect,btnUpload;

    //firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference food_list;
    FirebaseStorage storage;
    StorageReference storageReference;

    Uri saveUri;
    String res_id = "";

    FirebaseRecyclerAdapter<Food,FoodView> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        //firebase config
        firebaseDatabase = FirebaseDatabase.getInstance();
        food_list = firebaseDatabase.getReference("Food");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //init
        recyclerView = findViewById(R.id.recycler_food_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootlayout = findViewById(R.id.rootLayout);

        //floating action button
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });

        if(getIntent() != null){
            res_id = getIntent().getStringExtra("res_id");
        }
        if(!res_id.isEmpty()){
            loadFoodList(res_id);
        }
        loadFoodList(res_id);

    }

    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Food_list.this);
        alertDialog.setTitle("Add New Food In Menu");
        alertDialog.setMessage("Please Fill the Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_new_food,null);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //just create new food
                if(newFood != null){
                    Snackbar.make(rootlayout,"New Food"+newFood.getFood_name()+" was added",Snackbar.LENGTH_SHORT).show();
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

        edt_food_name = add_food_layout.findViewById(R.id.edt_food_name);
        edt_food_desc = add_food_layout.findViewById(R.id.edt_food_description);
        edt_food_price = add_food_layout.findViewById(R.id.edt_food_price);
        btnImageSelect = add_food_layout.findViewById(R.id.btn_upload_image);
        btnUpload = add_food_layout.findViewById(R.id.btn_add_food);

        //setting up button functionality
        btnImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
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
                            Toast.makeText(Food_list.this,"Uploading...",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new food if image upload and we can get download link
                                     newFood = new Food();
                                     newFood.setFood_name(edt_food_name.getText().toString());
                                     newFood.setDescription(edt_food_desc.getText().toString());
                                     newFood.setPrice(edt_food_price.getText().toString());
                                     newFood.setRes_id(res_id);
                                     newFood.setFood_Image(saveUri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(Food_list.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");
                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && requestCode == RESULT_OK && data != null && data.getData() != null){
            saveUri = data.getData();
            btnImageSelect.setText("Image Selected!");
        }
    }

    private void loadFoodList(String res_id) {

        Query listFoodByResId = food_list.orderByChild("res_id").equalTo(res_id);
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(listFoodByResId,Food.class).build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodView holder, int position, @NonNull Food model) {
                holder.txtFoodName.setText(model.getFood_name());
                Picasso.with(getBaseContext()).load(model.getFood_Image()).into(holder.foodImage);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongCLick) {

                    }
                });
            }

            @Override
            public FoodView onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodView(itemView);
            }
        };
        adapter.startListening();

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
*/

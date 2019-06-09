package com.example.avneeshjaiswal.ecommerceserver.View;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avneeshjaiswal.ecommerceserver.Common.Common;
import com.example.avneeshjaiswal.ecommerceserver.Interface.ItemClickListener;
import com.example.avneeshjaiswal.ecommerceserver.R;

public class FoodView extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView textFoodName;
    public ImageView imgFoodImage;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    public FoodView(View itemView) {
        super(itemView);

        textFoodName = itemView.findViewById(R.id.food_name);
        imgFoodImage = itemView.findViewById(R.id.food_image);
        //imgFavImage = itemView.findViewById(R.id.fav);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);

    }
}

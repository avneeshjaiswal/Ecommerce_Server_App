package com.example.avneeshjaiswal.ecommerceserver.View;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avneeshjaiswal.ecommerceserver.Common.Common;
import com.example.avneeshjaiswal.ecommerceserver.Interface.ItemClickListener;
import com.example.avneeshjaiswal.ecommerceserver.R;

public class BannerView extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
public TextView banner_name;
public ImageView banner_image;

private ItemClickListener itemClickListener;

public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

        }

public BannerView(View itemView) {
        super(itemView);

        banner_name = itemView.findViewById(R.id.banner_name);
        banner_image = itemView.findViewById(R.id.banner_image);
        //imgFavImage = itemView.findViewById(R.id.fav);

        itemView.setOnCreateContextMenuListener(this);

        }


@Override
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);

        }
}

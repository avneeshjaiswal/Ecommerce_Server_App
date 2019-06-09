package com.example.avneeshjaiswal.ecommerceserver.View;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.avneeshjaiswal.ecommerceserver.Interface.ItemClickListener;
import com.example.avneeshjaiswal.ecommerceserver.R;

/**
 * Created by avneesh jaiswal on 24-Feb-18.
 */

public class OrderView extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener, View.OnCreateContextMenuListener{
    public TextView OrderId,OrderStatus,OrderPhone,OrderAddress,OrderDate;
    //public Button btnEdit,btnRemove,btnDetail,btnDirection;
    private ItemClickListener itemClickListener;

    public OrderView(View v) {
        super(v);

        OrderId = v.findViewById(R.id.order_id);
        OrderStatus = v.findViewById(R.id.order_status);
        OrderAddress = v.findViewById(R.id.order_address);
        OrderPhone = v.findViewById(R.id.order_phone);
        OrderDate = v.findViewById(R.id.order_date);

        v.setOnClickListener(this);
        v.setOnCreateContextMenuListener(this);
        v.setOnLongClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,1,getAdapterPosition(),"Delete");

    }


    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}

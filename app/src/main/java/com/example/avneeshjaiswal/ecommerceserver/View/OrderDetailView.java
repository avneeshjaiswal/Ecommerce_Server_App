package com.example.avneeshjaiswal.ecommerceserver.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.avneeshjaiswal.ecommerceserver.Model.Order;
import com.example.avneeshjaiswal.ecommerceserver.R;

import java.util.List;

/**
 * Created by avneesh jaiswal on 26-Feb-18.
 */

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name,quantity,price;

    public MyViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantity);
        price = itemView.findViewById(R.id.product_price);
    }
}
public class OrderDetailView extends RecyclerView.Adapter<MyViewHolder> {

    List<Order> myOrders;

    public OrderDetailView(List<Order> myOrders) {
        this.myOrders = myOrders;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name: %s",order.getProductName()));
        holder.quantity.setText(String.format("Quantity: %s",order.getQuantity()));
        holder.price.setText(String.format("Price: %s",order.getPrice()));

    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}

package com.example.scanner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;

public class ItemProductAdapter extends RecyclerView.Adapter<ItemProductAdapter.GithubViewHolder>{

    private Context context;
    private ProductSchema[] schemasData;
    public static int quantity[] = new int[5];
    public static int amount[] = new int[5];
    public static long productId[] = new long[5];
    public static String _id[] = new String[5];
    public static String machine;
    public static String key_razorpay;

    public ItemProductAdapter(Context context, ProductSchema[] schemas){
        this.context = context;
        this.schemasData = schemas;
        for(int i = 0; i< 5; i++){
            quantity[i] = 0;
            productId[i] = 0;
            amount[i] = 0;
        }
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public GithubViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product_layout, parent, false);
        return new GithubViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemProductAdapter.GithubViewHolder holder, int position) {
        ProductSchema schemas = schemasData[position];
        machine = schemas.getMachine();
        _id[position] = schemas.getId();
        key_razorpay = schemas.getKey_razorpay();
        int price = schemas.getPrice();
        int total_avilable_quantity = schemas.getQuantity();
        long product_id = schemas.getProduct_id();
        productId[position] = product_id;
        if(total_avilable_quantity < 6){
            holder.onlyLeft.setText("Only "+String.valueOf(total_avilable_quantity)+" avilable...");
        }



        if (schemas.getStatus()){
            holder.priceProd.setText(schemas.getPrice()+" ₹");
            Glide.with(holder.imgProd.getContext()).load(schemas.getImage()).override(800, 800).into(holder.imgProd);

            holder.positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(quantity[position] >= total_avilable_quantity){
                        Toast.makeText(context.getApplicationContext(), "Only "+String.valueOf(total_avilable_quantity)+" avilable.", Toast.LENGTH_SHORT).show();
                    }else{
                        if (quantity[position] < 5) {
                            quantity[position] = quantity[position] + 1;
                            amount[position] = quantity[position] * price;
                            holder.showQuantity.setText(String.valueOf(quantity[position]));
                        } else {
                            Toast.makeText(context.getApplicationContext(), "You can select only 5 at a time.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            holder.negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity[position] > 0){
                        quantity[position] = quantity[position]-1;
                        amount[position] =quantity[position]*price;
                        holder.showQuantity.setText(String.valueOf(quantity[position]));
                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {

        return schemasData.length;
    }

    public class GithubViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProd;
        TextView priceProd, showQuantity, onlyLeft;
        Button positiveButton, negativeButton;
        public GithubViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProd= itemView.findViewById(R.id.imgProd);
            priceProd = itemView.findViewById(R.id.priceProd);
            showQuantity = itemView.findViewById(R.id.showQuantity);
            positiveButton = itemView.findViewById(R.id.positiveBtn);
            negativeButton = itemView.findViewById(R.id.negativeBtn);
            onlyLeft = itemView.findViewById(R.id.onlyLeft);

        }
    }

}






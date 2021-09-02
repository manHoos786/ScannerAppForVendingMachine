package com.example.scanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ShowCheckoutAdapter extends RecyclerView.Adapter<ShowCheckoutAdapter.GithubViewHolder> {

    Context context;
    int [] quantity;
    int [] price;


    public ShowCheckoutAdapter(Context context, int[] quantity, int[] price) {
        this.context = context;
        this.quantity = quantity;
        this.price = price;
    }

    @NonNull
    @NotNull
    @Override
    public ShowCheckoutAdapter.GithubViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.show_checkout_option, parent, false);
        return  new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShowCheckoutAdapter.GithubViewHolder holder, int position) {
        Log.e("position ", String.valueOf(position));
        int count = 0;
        int i = 0;
        while(count <position+1){
            if(quantity[i] != 0){
                holder.countNumber.setText(String.valueOf(position+1));
                holder.priceOfProduct.setText(String.valueOf(price[i])+" ₹");
                holder.quantityOfProduct.setText(String.valueOf(quantity[i]));
                count++;
            }
            i++;
        }

    }
    @Override
    public int getItemCount() {
        int count = 0;
        for(int i = 0; i<quantity.length; i++){
            if(quantity[i] !=0){
                count++;
            }
        }
        return count;
    }

    public class GithubViewHolder extends RecyclerView.ViewHolder{
        TextView priceOfProduct, quantityOfProduct, countNumber;
        public GithubViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            priceOfProduct= itemView.findViewById(R.id.productPrice);
            quantityOfProduct = itemView.findViewById(R.id.productQuantity);
            countNumber = itemView.findViewById(R.id.countNumber);

        }
    }}

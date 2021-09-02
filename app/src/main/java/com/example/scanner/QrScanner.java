package com.example.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.MalformedInputException;

public class QrScanner extends AppCompatActivity {
    TextView textView;
    PrograssBar prograssBar;
    Button pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        prograssBar = new PrograssBar(QrScanner.this);

        pay = findViewById(R.id.proceedToPay);

        final RecyclerView productList = findViewById(R.id.productList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        productList.setLayoutManager(layoutManager);

        prograssBar.startPrograssBar();

        Intent intent = getIntent();
        String url = intent.getStringExtra("a");
//        String url = "https://obscure-cove-38079.herokuapp.com/showProduct/machine2";
////        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        if(url == null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            prograssBar.dismissPrograssBar();
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            ProductSchema[] productSchemas = gson.fromJson(response, ProductSchema[].class);
                            productList.setAdapter(new ItemProductAdapter(getApplicationContext(), productSchemas));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prograssBar.dismissPrograssBar();
                    Toast.makeText(QrScanner.this, "Network error please try again", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(QrScanner.this, MainActivity.class));
                }
            });
            queue.add(stringRequest);
        }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for(int i =0 ; i< ItemProductAdapter.quantity.length; i++){
                    count = count + ItemProductAdapter.quantity[i];
                }
                if(count != 0){
                    Intent intent = new Intent(QrScanner.this, PaymentActivity.class);
                    intent.putExtra("quantity", ItemProductAdapter.quantity);
                    intent.putExtra("amount", ItemProductAdapter.amount);
                    intent.putExtra("product_id", ItemProductAdapter.productId);
                    intent.putExtra("machine", ItemProductAdapter.machine);
                    intent.putExtra("_id", ItemProductAdapter._id);
                    intent.putExtra("key_razorpay", ItemProductAdapter.key_razorpay);
                    startActivity(intent);
                }else{
                    Toast.makeText(QrScanner.this, "Please select any product.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
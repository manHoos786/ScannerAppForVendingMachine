package com.example.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    TextView grandTotal;
    String total_amount_to_be_paid;
    String machine;
    String key_razorpay;
    public static Button payNowButton;
    JSONObject notesObject;
    PrograssBar prograssBar;

    int []quantity1;
    int []amount;
    long []product;
    String []_id;
    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payNowButton = findViewById(R.id.button);
        grandTotal = findViewById(R.id.grandTotal);



        Checkout.preload(PaymentActivity.this);
        prograssBar = new PrograssBar(PaymentActivity.this);
        Intent intent = getIntent();
        quantity1 = intent.getIntArrayExtra("quantity");
        amount = intent.getIntArrayExtra("amount");
        product = intent.getLongArrayExtra("product_id");
        _id = intent.getStringArrayExtra("_id");
        machine = intent.getStringExtra("machine");
        key_razorpay = intent.getStringExtra("key_razorpay");

        recyclerView = findViewById(R.id.payment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShowCheckoutAdapter(this, quantity1, amount);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for(int i = 0; i<amount.length; i++){
            total = total + amount[i];
        }

        total_amount_to_be_paid = String.valueOf(total);
        grandTotal.setText(total_amount_to_be_paid+" ₹");


        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prograssBar.startPrograssBar();
                getOrderId();
            }
        });
    }



    public void getOrderId() {
        double total = Double.parseDouble(total_amount_to_be_paid);
        total = total * 100;
        int amount = (int)total;
        final String[] order_id = new String[1];
        String url = "https://authenticate-my-app.herokuapp.com/api/payment/order/"+key_razorpay;
        JSONObject object = new JSONObject();
        try {
            object.put("amount", amount);
            object.put("currency", "INR");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("sub");
                    order_id[0] = jsonObject.getString("id");
                    Log.e("response", jsonObject.getString("id"));
                    startPayment(order_id[0]);
                } catch (JSONException e) {
                    Log.e("error is here", String.valueOf(e));
                    prograssBar.dismissPrograssBar();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prograssBar.dismissPrograssBar();
                Log.e("eror", String.valueOf(error));
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }

    public void startPayment(String s){

        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(key_razorpay);
        notesObject = new JSONObject();
        Log.e("machine is", machine);
        try {
            notesObject.put("quantity", Arrays.toString(quantity1));
            notesObject.put("product_id",Arrays.toString(product));
            notesObject.put("_id_array",Arrays.toString(_id));
            notesObject.put("machine",machine);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Aakash Sharma");
            options.put("description", "Please pay here");
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("order_id",s);
            options.put("notes", notesObject);
            options.put("remember_customer", true);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "sharmaaakash170@gmail.com");
            preFill.put("contact", "7860290907");
            options.put("prefill", preFill);
            prograssBar.dismissPrograssBar();
            co.open(activity, options);


        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
        intent.putExtra("price", total_amount_to_be_paid);
        intent.putExtra("id", s);
        startActivity(intent);
        Log.e("OnPaymentError", "payment successfull");

    }
    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG",  "error code "+String.valueOf(i)+" -- Payment failed "+s.toString()  );
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}


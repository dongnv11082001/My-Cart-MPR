package hanu.a2_1901040058.mycart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040058.mycart.adapters.CartAdapter;
import hanu.a2_1901040058.mycart.db.ProductManager;
import hanu.a2_1901040058.mycart.models.Product;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnBack;
    RecyclerView rvCart;
    CartAdapter adapter;
    List<Product> lstCart;
    public TextView tvPrice, txtTotalPrice, tvEmpty;
    ProductManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mapping();
        manager = ProductManager.getInstance(this);
        lstCart = manager.getAllData();
        int numberPr = lstCart.size();
        check(numberPr);

        txtTotalPrice.setText(manager.countPrice() + " VND");

        btnBack.setOnClickListener(this);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(lstCart, this);
        rvCart.setAdapter(adapter);
    }

    private void mapping() {
        tvEmpty = findViewById(R.id.tvEmpty);
        rvCart = findViewById(R.id.rvCart);
        tvPrice = findViewById(R.id.tvPrice);
        btnBack = findViewById(R.id.btnBack);
        txtTotalPrice = findViewById(R.id.tvTotalPrice);
    }

    private void check(int number) {
        if (number == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                Intent i = new Intent(CartActivity.this, MainActivity.class);
                startActivity(i);
                break;
            default:
        }
    }
}


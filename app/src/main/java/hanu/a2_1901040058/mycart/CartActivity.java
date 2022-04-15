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
    List<Product> productListCart;
    public TextView tvPrice, txtTotalPrice, tvEmpty;
    ProductManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tvEmpty = findViewById(R.id.tvEmpty);
        rvCart = findViewById(R.id.rvCart);
        tvPrice = findViewById(R.id.tvPrice);
        btnBack = findViewById(R.id.btnBack);
        txtTotalPrice = findViewById(R.id.tvTotalPrice);

        manager = ProductManager.getInstance(this);
        productListCart = manager.allProducts();

        if (productListCart.size() > 0) {
            tvEmpty.setVisibility(View.INVISIBLE);
        } else {
            tvEmpty.setVisibility(View.VISIBLE);
        }

        txtTotalPrice.setText(manager.countProduct() + " VND");

        btnBack.setOnClickListener(this);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(productListCart, this);
        rvCart.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}


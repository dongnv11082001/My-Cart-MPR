package hanu.a2_1901040058.mycart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import hanu.a2_1901040058.mycart.adapters.CartAdapter;
import hanu.a2_1901040058.mycart.db.ProductManager;
import hanu.a2_1901040058.mycart.models.Product;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    ProductManager manager;
    List<Product> productList;
    public TextView tvEmpty, tvTotalPrice, tvPrice;
    RecyclerView rvCart;
    ImageButton btnBack;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCart = findViewById(R.id.rvCart);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPrice = findViewById(R.id.tvPrice);
        // get ref to back button
        btnBack = findViewById(R.id.btnBack);

        manager = ProductManager.getInstance(CartActivity.this);
        productList = manager.getAllProducts();


        if (productList.size() > 0) {
            tvEmpty.setVisibility(View.INVISIBLE);
        } else {
            tvEmpty.setVisibility(View.VISIBLE);
        }

        rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        adapter = new CartAdapter(productList);
        rvCart.setAdapter(adapter);

        // handle click event
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent(hanu.a2_1901040058.mycart.CartActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}

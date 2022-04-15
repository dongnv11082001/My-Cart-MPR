package hanu.a2_1901040058.mycart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040058.mycart.adapters.ProductAdapter;
import hanu.a2_1901040058.mycart.db.ProductManager;
import hanu.a2_1901040058.mycart.models.Product;

public class MainActivity extends AppCompatActivity {
    ImageButton btnCart;
    RecyclerView rvProducts;
    RestLoader task;
    TextView searchBar;
    private final String API_PRODUCT = "https://mpr-cart-api.herokuapp.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get ref recyclerview
        rvProducts = findViewById(R.id.rvProducts);

        // load products
        task = new RestLoader(rvProducts, this);
        task.execute(API_PRODUCT);

        searchBar = findViewById(R.id.edSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_main, menu);

        // get ref to cart button
        btnCart = findViewById(R.id.btnCart);

        // handle click event go to cart
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(hanu.a2_1901040058.mycart.MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
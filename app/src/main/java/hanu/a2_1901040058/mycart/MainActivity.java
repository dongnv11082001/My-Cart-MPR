package hanu.a2_1901040058.mycart;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040058.mycart.adapters.ProductAdapter;
import hanu.a2_1901040058.mycart.db.ProductManager;
import hanu.a2_1901040058.mycart.models.Product;

public class MainActivity extends AppCompatActivity {
    RestLoader task;
    TextView searchBar;
    ImageButton btnSearch;
    RecyclerView rvProducts;
    ProductManager productManager;
    public List<Product> productList;
    public ProductAdapter productAdapter;

    private String API_PRODUCT = "https://mpr-cart-api.herokuapp.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        rvProducts = findViewById(R.id.rvProducts);
        searchBar = findViewById(R.id.edSearch);

        productManager = new ProductManager(this);
        productList = productManager.allProducts();

        productAdapter = new ProductAdapter(productList);
        rvProducts.setAdapter(productAdapter);
        rvProducts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        task = new RestLoader(this, rvProducts, productList);
        task.execute(API_PRODUCT);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Product> searchProduct = new ArrayList<>();

                for (int i = 0; i < task.productList.size(); i++) {
                    Log.i("onTextChanged: ", task.productList.get(i).getName());
                    if (task.productList.get(i).getName().toLowerCase().contains(String.valueOf(s))) {
                        searchProduct.add(task.productList.get(i));
                    }
                }

                setAdapter(searchProduct);
                String.valueOf(s);
                if (String.valueOf(s) == "") {
                    setAdapter(task.productList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnCart:
                Intent intent = new Intent(hanu.a2_1901040058.mycart.MainActivity.this, CartActivity.class);
                startActivity(intent);
                break;

            default:
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAdapter(List<Product> productList) {
        productAdapter = new ProductAdapter(productList);
        rvProducts.setAdapter(productAdapter);
        rvProducts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }
}
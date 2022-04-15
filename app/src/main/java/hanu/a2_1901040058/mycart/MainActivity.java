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
import hanu.a2_1901040058.mycart.models.Product;

public class MainActivity extends AppCompatActivity {
    TextView searchBar;
    RecyclerView rvProducts;
    ImageButton btnSearch;
    public List<Product> productList;
    public ProductAdapter productAdapter;
    RestLoader task;
    private String API_PRODUCT = "https://mpr-cart-api.herokuapp.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        rvProducts = findViewById(R.id.rvProducts);
        searchBar = findViewById(R.id.edSearch);

        task = new RestLoader(this, rvProducts, productList);
        task.execute(API_PRODUCT);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Product> searchProduct = new ArrayList<>();
                for (int i = 0; i < task.listProduct().size(); i++) {
                    Log.i("onTextChanged: ", task.listProduct().get(i).getName());
                    if (task.listProduct().get(i).getName().toLowerCase().contains(String.valueOf(s))) {
                        searchProduct.add(task.listProduct().get(i));
                    }
                }

                adapter(searchProduct);
                String.valueOf(s);
                if (String.valueOf(s) == "") {
                    adapter(task.listProduct());
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

    public void adapter(List<Product> list) {
        productAdapter = new ProductAdapter(this, list);
        rvProducts.setAdapter(productAdapter);
        rvProducts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }
}
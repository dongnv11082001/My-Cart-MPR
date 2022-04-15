package hanu.a2_1901040058.mycart;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_1901040058.mycart.adapters.ProductAdapter;
import hanu.a2_1901040058.mycart.models.Product;

public class RestLoader extends AsyncTask<String, Void, String> {
    List<Product> productList;
    RecyclerView rvProducts;
    Context context;
    ProductAdapter productAdapter;

    public RestLoader(RecyclerView rvProducts, Context context) {
        this.productList = new ArrayList<>();
        this.rvProducts = rvProducts;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            Scanner sc = new Scanner(inputStream);
            StringBuilder result = new StringBuilder();
            String line;

            while (sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }

            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null) {
            try {
                JSONArray root = new JSONArray(s);
                for (int i = 0; i < root.length(); i++) {
                    JSONObject productObject = root.getJSONObject(i);
                    int id = productObject.getInt("id");
                    String thumbnail = productObject.getString("thumbnail");
                    String name = productObject.getString("name");
                    int unitPrice = productObject.getInt("unitPrice");

                    Product product = new Product(id, thumbnail, name, unitPrice);
                    productList.add(product);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // adapter
        productAdapter = new ProductAdapter(productList);
        rvProducts.setAdapter(productAdapter);
        rvProducts.setLayoutManager(new GridLayoutManager(context, 2));
    }
}

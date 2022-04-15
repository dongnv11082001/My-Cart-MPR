package hanu.a2_1901040058.mycart;

import android.content.Context;
import android.os.AsyncTask;

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
    URL url;
    HttpURLConnection urlConnection;
    ProductAdapter adapter;
    Context context;
    RecyclerView recyclerView;
    List<Product> list;
    public List<Product> listProduct(){
        return list;
    }
    public RestLoader(Context context, RecyclerView recyclerView, List<Product> list) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.list = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();

            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
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
        if (s != null){
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject jsonProduct = jsonArray.getJSONObject(i);

                    int productId = jsonProduct.getInt("id");
                    String productThumbnail = jsonProduct.getString("thumbnail");
                    String productName = jsonProduct.getString("name");
                    int productPrice = jsonProduct.getInt("unitPrice");
                    Product product = new Product(productId, productThumbnail, productName, productPrice);
                    list.add(product);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ProductAdapter(context, list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);
    }
}

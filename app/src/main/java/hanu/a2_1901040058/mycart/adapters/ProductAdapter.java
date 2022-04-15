package hanu.a2_1901040058.mycart.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hanu.a2_1901040058.mycart.R;
import hanu.a2_1901040058.mycart.db.ProductManager;
import hanu.a2_1901040058.mycart.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Context
        Context context = parent.getContext();

        // inflater
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate
        View itemView = inflater.inflate(R.layout.item_product, parent, false);

        return new ProductHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // view holder
    protected class ProductHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivProduct;
        TextView tvName, tvPrice;
        ImageButton btnAdd;
        private Context context;

        public ProductHolder(@NonNull View itemView, Context context) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            ivProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);

            this.context = context;
        }

        public void bind(Product product) {

            // set text
            tvName.setText(product.getName());
            tvPrice.setText(product.getUnitPrice() + "VND");

            // download image
            ImageDownloader task = new ImageDownloader();
            task.execute(product.getThumbnail());

            // handle click add button
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductManager manager = ProductManager.getInstance(context);

                    Product productDb = manager.findProductById(product.getId());

                    if (productDb == null) {
                        product.increaseQuantity();
                    } else {
                        productDb.increaseQuantity();
                    }

                    if (manager.addProduct(product) || manager.updateQuantity(productDb)) {
                        Toast.makeText(context, "Add product successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Add fail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return bitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ivProduct.setImageBitmap(bitmap);
            }
        }

    }
}

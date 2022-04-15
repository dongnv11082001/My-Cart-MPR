package hanu.a2_1901040058.mycart.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hanu.a2_1901040058.mycart.CartActivity;
import hanu.a2_1901040058.mycart.R;
import hanu.a2_1901040058.mycart.db.ProductManager;
import hanu.a2_1901040058.mycart.models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    List<Product> productList;


    public CartAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_cart, parent, false);

        return new CartHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // view holder
    protected class CartHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView imgCart;
        TextView tvName, tvPrice, tvQuantity, tvTotalOfEachProduct;
        ImageButton btnInc, btnDec;
        CartActivity activity;
        ProductManager manager;

        public CartHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalOfEachProduct = itemView.findViewById(R.id.tvTotalOfEachProduct);
            btnInc = itemView.findViewById(R.id.btnInc);
            btnDec = itemView.findViewById(R.id.btnDec);
            imgCart = itemView.findViewById(R.id.imgCart);
            this.context = context;
            activity = new CartActivity();
        }

        public void bind(Product product) {

            ThumbnailDownloader task = new ThumbnailDownloader();
            task.execute(product.getThumbnail());

            tvName.setText(product.getName());
            tvPrice.setText(product.getUnitPrice() + "VND");
            tvQuantity.setText(product.getQuantity() + "");
            tvTotalOfEachProduct.setText(product.getTotalPrice() + "VND");

            btnInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     manager = ProductManager.getInstance(context);

                    product.increaseQuantity();

                    if (manager.updateQuantity(product)) {
                        notifyDataSetChanged();
                        activity.tvTotalPrice.setText(product.getTotalPrice() + "VND");
                    }
                }
            });

            btnDec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager = ProductManager.getInstance(context);
                    if (product.getQuantity() == 1) {
                        int id = (int) product.getId();
                        dialogClick(id);
                        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                        alert.setTitle("Confirm");
                        alert.setMessage("Do you want to delete this product?");
                        alert.setIcon(R.drawable.ic_baseline_remove_24);

                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            boolean delete_pr = false;

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productList.remove(product);
                                delete_pr = manager.delete(id);
                                Toast.makeText(activity, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                activity.tvTotalPrice.setText(manager.countPrice() + "VND");
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = alert.create();
                        dialog.show();
                    } else {
                        product.decreaseQuantity();
                        activity.tvTotalPrice.setText(manager.countPrice() + "VND");
                    }
                    notifyDataSetChanged();
                }
            });
        }

        public void dialogClick(int position) {
        }

        public class ThumbnailDownloader extends AsyncTask<String, Void, Bitmap> {
            URL image_url;
            HttpURLConnection urlConnection;

            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    image_url = new URL(strings[0]);
                    urlConnection = (HttpURLConnection) image_url.openConnection();
                    urlConnection.connect();

                    InputStream is = urlConnection.getInputStream();
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
                imgCart.setImageBitmap(bitmap);
            }
        }
    }

}

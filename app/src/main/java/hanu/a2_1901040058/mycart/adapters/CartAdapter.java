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
    List<Product> productListCart;
    private CartActivity cartActivity;
    ProductManager productManager;

    public CartAdapter(List<Product> productListCart, CartActivity cartActivity) {
        this.productListCart = productListCart;
        this.cartActivity = cartActivity;
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
        Product product = productListCart.get(position);

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productListCart.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        ImageView imgCart;
        TextView tvName, tvPrice, tvNumberPr, tvTotalOfEachProduct;
        ImageButton btnInc, btnDec;
        Context context;

        public CartHolder(@NonNull View itemView, Context context) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNumberPr = itemView.findViewById(R.id.tvQuantity);
            tvTotalOfEachProduct = itemView.findViewById(R.id.tvTotalOfEachProduct);
            btnInc = itemView.findViewById(R.id.btnInc);
            btnDec = itemView.findViewById(R.id.btnDec);
            imgCart = itemView.findViewById(R.id.imgCart);
        }

        public void bind(Product product) {
            tvName.setText(product.getName());
            tvNumberPr.setText(product.getQuantity() + "");
            tvPrice.setText(product.getUnitPrice() + " VND");
            tvTotalOfEachProduct.setText(product.getTotalPrice() + "\nVND");

            btnDec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productManager = ProductManager.getInstance(context);
                    if (product.getQuantity() == 1) {
                        int id = (int) product.getId();
                        AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity);
                        alert.setTitle("Confirm")
                                .setMessage("Do you want to delete this product?")
                                .setIcon(android.R.drawable.ic_delete)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        productListCart.remove(product);
                                        boolean isDeleted = productManager.delete(id);

                                        if (isDeleted) {
                                            Toast.makeText(cartActivity, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                            cartActivity.txtTotalPrice.setText(productManager.countProduct() + "VND");
                                        }
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
                        boolean isUpdated = productManager.updateQuantity(product);

                        if (isUpdated) {
                            notifyDataSetChanged();
                            cartActivity.txtTotalPrice.setText(productManager.countProduct() + "VND");
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            btnInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productManager = ProductManager.getInstance(context);
                    product.increaseQuantity();

                    boolean isUpdated = productManager.updateQuantity(product);
                    if (isUpdated) {
                        notifyDataSetChanged();
                        cartActivity.txtTotalPrice.setText(productManager.countProduct() + "VND");
                    }
                }
            });

            ImageDownloader task = new ImageDownloader();
            task.execute(product.getThumbnail());
        }

        public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
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
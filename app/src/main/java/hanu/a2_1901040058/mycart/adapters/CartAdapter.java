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
    List<Product> cartLines;
    private CartActivity cartActivity;
    ProductManager productManager;

    public CartAdapter(List<Product> cartLines, CartActivity cartActivity) {
        this.cartLines = cartLines;
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
        Product product = cartLines.get(position);

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return cartLines.size();
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
                        dialogClick(id);
                        AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity);
                        alert.setTitle("Confirm");
                        alert.setMessage("Do you want to delete this product ?");
                        alert.setIcon(R.drawable.ic_baseline_remove_24);


                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            boolean delete_pr = false;

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cartLines.remove(product);
                                delete_pr = productManager.delete(id);
                                Toast.makeText(cartActivity, "Delete Successfull", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                cartActivity.txtTotalPrice.setText(productManager.countPrice() + "VND");
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
                        cartActivity.txtTotalPrice.setText(productManager.countPrice() + "VND");

                    }
                    notifyDataSetChanged();
                }
            });

            btnInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productManager = ProductManager.getInstance(context);
                    product.increaseQuantity();
                    notifyDataSetChanged();
                    cartActivity.txtTotalPrice.setText(productManager.countPrice() + "VND");

                }
            });

            ThumbnailLoader task = new ThumbnailLoader();
            task.execute(product.getThumbnail());

        }

        public void dialogClick(int position) {

        }

        public class ThumbnailLoader extends AsyncTask<String, Void, Bitmap> {
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
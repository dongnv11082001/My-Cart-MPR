package hanu.a2_1901040058.mycart.db;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040058.mycart.models.Product;

public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        while (!isLast()) {
            Product product = getProduct();
            productList.add(product);
        }

        return productList;
    }

    public Product getProduct() {
        moveToNext();

        long id = getLong(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_ID));
        String thumbnail = getString(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_THUMBNAIL));
        String name = getString(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_NAME));
        int unitPrice = getInt(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_PRICE));
        int quantity = getInt(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_QUANTITY));

        Product product = new Product(id, thumbnail, name, unitPrice, quantity);

        return product;
    }

    public Product getProductByID() {
        Product product = null;
        if (!isLast()) {
            product = getProduct();
        }

        return product;
    }
}

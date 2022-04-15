package hanu.a2_1901040058.mycart.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040058.mycart.models.Product;

public class ProductCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {

        long id = getLong(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_ID));

        String thumbnail = getString(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_THUMBNAIL));
        String name = getString(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_NAME));

        int unitPrice = getInt(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_PRICE));
        int quantity = getInt(getColumnIndex(DBSchema.ProductTable.Cols.COLUMN_QUANTITY));

        Product product = new Product(id, thumbnail, name, unitPrice, quantity);

        return product;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()) {
            Product product = getProduct();
            products.add(product);

            moveToNext();
        }
        return products;
    }


    public Product getProductByID() {
        Product product = null;
        moveToFirst();
        if (!isAfterLast()) {
            product = getProduct();
        }

        return product;
    }
}

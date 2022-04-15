package hanu.a2_1901040058.mycart.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1901040058.mycart.models.Product;


public class ProductManager {
    private static ProductManager instance;

    private static final String INSERT_STMT =
            "INSERT INTO " + DBSchema.ProductTable.NAME + "(id, name, thumbnail, unitPrice, quantity) VALUES (?, ?, ?, ?, ?)";

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

    public ProductManager(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addProduct(Product product) {
        SQLiteStatement statement = db.compileStatement(INSERT_STMT);

        statement.bindLong(1, product.getId());
        statement.bindString(3, product.getThumbnail());
        statement.bindString(2, product.getName());
        statement.bindDouble(4, product.getUnitPrice());
        statement.bindString(5, String.valueOf(product.getQuantity()));

        long id = statement.executeInsert();
        if (id > 0) {
            product.setId(id);
            return true;
        }

        return false;
    }

    public boolean updateQuantity(Product product) {
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.ProductTable.Cols.COLUMN_ID, product.getId());
        cv.put(DBSchema.ProductTable.Cols.COLUMN_NAME, product.getName());
        cv.put(DBSchema.ProductTable.Cols.COLUMN_THUMBNAIL, product.getThumbnail());
        cv.put(DBSchema.ProductTable.Cols.COLUMN_PRICE, product.getUnitPrice());
        cv.put(DBSchema.ProductTable.Cols.COLUMN_QUANTITY, product.getQuantity());

        int result = db.update(DBSchema.ProductTable.NAME, cv, DBSchema.ProductTable.Cols.COLUMN_ID + "= ?", new String[]{product.getId() + ""});
        return result > 0;
    }

    public Product findProductById(long id) {
        String sql = "SELECT * FROM " + DBSchema.ProductTable.NAME + " WHERE " + DBSchema.ProductTable.Cols.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id + ""});

        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getProductByID();
    }

    public List<Product> allProducts() {
        String query = "SELECT * FROM " + DBSchema.ProductTable.NAME;
        Cursor cursor = db.rawQuery(query, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        return cursorWrapper.getProducts();
    }

    public boolean delete(long id) {
        int result = db.delete(DBSchema.ProductTable.NAME,
                DBSchema.ProductTable.Cols.COLUMN_ID + "= ?", new String[]{id + ""});

        return result > 0;
    }

    public int countProduct() {
        String query = "SELECT SUM(" + DBSchema.ProductTable.Cols.COLUMN_QUANTITY + " * " + DBSchema.ProductTable.Cols.COLUMN_PRICE + ") AS total FROM " + DBSchema.ProductTable.NAME;
        Cursor cursor = db.rawQuery(query, null);

        int total = 0;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            total = cursor.getInt(0);
        }
        total = cursor.getInt(0);
        return total;
    }
}

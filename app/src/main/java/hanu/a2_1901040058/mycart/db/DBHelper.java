package hanu.a2_1901040058.mycart.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "products.db";
    private static final int DB_VERSION = 2;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DBSchema.ProductTable.NAME
                + "("
                + DBSchema.ProductTable.Cols.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + DBSchema.ProductTable.Cols.COLUMN_THUMBNAIL + " TEXT, "
                + DBSchema.ProductTable.Cols.COLUMN_NAME + " TEXT, "
                + DBSchema.ProductTable.Cols.COLUMN_PRICE + " DOUBLE, "
                + DBSchema.ProductTable.Cols.COLUMN_QUANTITY + " INTEGER"
                + ");"
                ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DBSchema.ProductTable.NAME);

        onCreate(db);
    }
}

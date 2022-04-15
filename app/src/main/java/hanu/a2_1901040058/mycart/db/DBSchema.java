package hanu.a2_1901040058.mycart.db;

public class DBSchema {
    public final class ProductTable {
        public static final String NAME = "products";

        public final class Cols {
            public static final String COLUMN_ID = "id";
            public static final String COLUMN_THUMBNAIL = "thumbnail";
            public static final String COLUMN_PRICE = "unitPrice";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_QUANTITY = "quantity";
        }
    }
}

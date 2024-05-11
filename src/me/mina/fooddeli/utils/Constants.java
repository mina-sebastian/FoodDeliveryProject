package me.mina.fooddeli.utils;

public class Constants {

    public static final String JDBC_DATABASE_NAME = "pao";

    public static final String DELIVERY_PERSONS = JDBC_DATABASE_NAME+ ".delivery_persons";
    public static final String REVIEWS_TABLE = JDBC_DATABASE_NAME+ ".reviews";
    public static final String MENU_ITEMS_TABLE = JDBC_DATABASE_NAME+ ".menu_items";
    public static final String ORDER_ITEMS_TABLE = JDBC_DATABASE_NAME+ ".order_items";
    public static final String ORDERS_TABLE = JDBC_DATABASE_NAME+ ".orders";
    public static final String RESTAURANTS_TABLE = JDBC_DATABASE_NAME+ ".restaurants";
    public static final String USERS_TABLE = JDBC_DATABASE_NAME+ ".users";

    public static final String JDBC_DRIVER = "jdbc:mysql://localhost:3307/" + JDBC_DATABASE_NAME;
    public static final String JDBC_PWD = "pwd";
    public static final String JDBC_USER = "root";

    public static final String AUDIT_FILE = "audit.csv";
}
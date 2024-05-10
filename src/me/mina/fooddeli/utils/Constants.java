package me.mina.fooddeli.utils;

public class Constants {

    public static final String JDBC_DATABASE_NAME = "pao";
    public static final String DELIVERY_PERSONS = JDBC_DATABASE_NAME+ ".delivery_persons";

    public static final String JDBC_DRIVER = "jdbc:mysql://localhost:3307/" + JDBC_DATABASE_NAME;
    public static final String JDBC_PWD = "pwd";
    public static final String JDBC_USER = "root";

    public static final String AUDIT_FILE = "audit.csv";
}
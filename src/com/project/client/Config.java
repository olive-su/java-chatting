package com.project.client;

/** DB 정보 저장 클래스*/

public class Config {
    /** AWS RDS DB */
//    private static String host = "java-chatting.cixz2neezc20.ap-northeast-2.rds.amazonaws.com";
//    private static String user = "admin";
//    private static String pwd = "javaadmin123";
//    private static String port = "3306";
//    private static String name = "Chatting-DB";

    private static String host = "127.0.0.1";
    private static String user = "root";
    private static String pwd = "1234";
    private static String port = "3306";
    private static String name = "java-chatting";

    public static String getHost() {
        return host;
    }
    public static String getUser() { return user; }
    public static String getPwd() {
        return pwd;
    }
    public static String getPort() {
        return port;
    }
    public static String getName() { return name; }
}

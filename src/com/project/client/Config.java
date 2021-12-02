package com.project.client;

/** DB 정보 저장 클래스*/

public class Config {
    private static String host = "java-chatting.cixz2neezc20.ap-northeast-2.rds.amazonaws.com";
    private static String user = "admin";
    private static String pwd = "javaadmin123";
    private static String port = "3306";
    private static String name = "Chatting-DB";

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

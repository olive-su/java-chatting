package com.project.common;

// Client,Server=>공통으로 사용하는 클래스
// 내부 프로토콜 정의 클래스

public class Function {
    public static final String LOGIN = "100"; // 대기실 접속
    public static final String SHOW = "110"; // 대기실 창 띄움
    public static final String CREATEROOM = "160"; // 방 만들기
    public static final String ROOMUSERTOWAIT = "170"; // 대화방 인원 (to 대기실)
    public static final String ROOMUSERTOROOM = "175"; // 대화방 인원 (to 대화방)
    public static final String WAITUSER = "180"; // 대기실 인원
    public static final String ENTER = "200"; // 대화방 입장
    public static final String ROOMTITLETOROOM = "202"; // 대화방 이름 보여주기 (to 대화방)
    public static final String MESSAGE = "300"; // 메시지 전송
    public static final String EXIT = "400"; // 대화방 퇴장
}

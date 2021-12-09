package com.project.server;
import com.project.common.*;

import java.util.*;
import java.io.*;//입출력 (서버와 클라이언트 통신)
import java.net.*; //다른 컴퓨터와 연결

public class Service extends Thread {
    //Service : 접속 클라이언트
    Room myRoom;//클라이언트가 입장한 대화방

    //소켓관련 입출력서비스
    BufferedReader in;
    OutputStream out;

    int roomIdx; // 방 인덱스
    Vector<Service> allV;//모든 사용자(대기실사용자 + 대화방사용자)
    Vector<Service> waitV;//대기실 사용자
    Vector<Room> roomV;//개설된 대화방 Room-vs(Vector) : 대화방사용자

    Socket s;

    String userId, userName, userPos = "대기실";

    public Service(Socket s, Server server) {
        roomIdx = server.roomIdx;
        allV = server.allV;
        waitV = server.waitV;
        roomV = server.roomV;

        this.s = s;

        try {

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = s.getOutputStream();

            start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }//생성자

    @Override

    public void run() {
        try {
            while (true) {
                String msg = in.readLine();//클라이언트의 모든 메시지를 받기
                if (msg == null) return; //비정상적인 종료
                if (msg.trim().length() > 0) {
                    System.out.println("from Client: " + msg + ":" +
                            s.getInetAddress().getHostAddress());
                    //서버에서 상황 모니터
                    String msgs[] = msg.split("\\|\\|");
                    String protocol = msgs[0];
                    switch (protocol) {
                        case Function.LOGIN: //대기실 접속
                            // [msg]"100||test|테스트"
                            String loginMsg[] = msgs[1].split("\\|");
                            userId = loginMsg[0];
                            userName = loginMsg[1];
                            allV.add(this);//전체사용자에 등록
                            waitV.add(this);//대기실사용자에 등록
                            messageWait("160||" + getRoomInfo());
                            messageWait("180||" + getWaitUser());
                            break;

                        case Function.CREATEROOM: // 방만들기 (대화방 입장)
                            myRoom = new Room();
                            myRoom.setIdx(Integer.toString(++roomIdx));
                            myRoom.setTitle(msgs[1]);
                            myRoom.setCount(1);
                            myRoom.setLeader(userName);

                            roomV.add(myRoom); // 방 정보 추가

                            // 대기실 -> 대화방 이동
                            userPos = msgs[1];
                            waitV.remove(this);
                            myRoom.userV.add(this);
                            messageRoom("200||" + userName); // 입장 알림

                            //대기실 사용자들에게 방정보를 전달
                            //예) 대화방명:JavaLove
                            //-----> roomInfo(JList) :  JavaLove--1
                            messageWait("160||" + getRoomInfo());
                            messageWait("180||" + getWaitUser());
                            break;

                        case Function.ROOMUSERTOWAIT: //(대기실에서) 대화방 인원정보
                            System.out.println("msgs[1] : "+msgs[1]);
                            messageTo("170||" + getRoomUser(msgs[1]));
                            break;

                        case Function.ROOMUSERTOROOM: //(대화방에서) 대화방 인원정보
                            messageRoom("175||" + getRoomUser());
                            break;

                        case Function.ENTER: //방들어가기 (대화방 입장) ----> msgs[] = {"200","자바방"}
                            for (int i = 0; i < roomV.size(); i++) {//방이름 찾기!!
                                Room r = roomV.get(i);
                                if (r.getIdx().equals(msgs[1])) {//일치하는 방 찾음!!
                                    myRoom = r;
                                    myRoom.setCount(myRoom.getCount()+1);//인원수 1증가
                                    break;
                                }
                            }//for

                            //대기실----> 대화방 이동!!
                            userPos = msgs[1];
                            waitV.remove(this);
                            myRoom.userV.add(this);
                            messageRoom("200||" + userName);//방인원에게 입장 알림

                            //들어갈 방의 title전달
                            messageTo("202||" + myRoom.getTitle());
                            messageWait("160||" + getRoomInfo());
                            messageWait("180||" + getWaitUser());
                            break;

                        case Function.MESSAGE: //메시지
                            messageRoom("300|| [" + userName + "] ❧ " + msgs[1]);
                            //클라이언트에게 메시지 보내기
                            break;

                        case Function.EXIT: //대화방 퇴장
                            myRoom.setCount(myRoom.getCount()-1);//인원수 감소
                            if (myRoom.getCount() < 1){ // 이 방에 인원이 없을 경우 방 리스트에서 방 삭제
                                roomV.remove(myRoom);
                            }

                            messageRoom("400||" + userName);//방인원들에게 퇴장 알림!!

                            //대화방----> 대기실 이동!!
                            myRoom.userV.remove(this);
                            waitV.add(this);

                            //대화방 퇴장후 방인원 다시출력
                            messageRoom("175||" + getRoomUser());

                            //대기실에 방정보 다시출력
                            messageWait("160||" + getRoomInfo());
                            messageWait("180||" + getWaitUser());
                            break;

                    }//서버 switch
                }//if
            }//while
        } catch (IOException e) {
            System.out.println("★");
            e.printStackTrace();
        }
    }//run

    public String getRoomInfo() {
        String str = "";
        for (int i = 0; i < roomV.size(); i++) {
            //"1::자바방::1|2::오라클방::1|3::JDBC방::1"
            Room r = roomV.get(i);
            str += r.getIdx() + "::" + r.getTitle() + "::" + r.getCount();
            if (i < roomV.size() - 1) str += "|";
        }
        return str;
    }//getRoomInfo

    public String getRoomUser() {// 같은 방 인원정보(방 들어갔을때)
        String str = "";
        for (int i = 0; i < myRoom.userV.size(); i++) {
            //"길동|라임|주원"
            Service ser = myRoom.userV.get(i);
            str += ser.userName;
            if (i < myRoom.userV.size() - 1) str += "|";
        }
        return str;
    }//getRoomUser

    public String getRoomUser(String roomIdx) {// 방 제목 클릭 시 방 인원 정보
        String str = "";

        for (int i = 0; i < roomV.size(); i++) {
            //"길동|라임|주원"
            Room r = roomV.get(i);
            if (r.getIdx().equals(roomIdx)) {
                for (int j = 0; j < r.userV.size(); j++) {
                    Service ser = r.userV.get(j);
                    str += ser.userName;
                    if (j < r.userV.size() - 1) str += "|";
                }
                break;
            }
        }
        return str;
    }//getRoomUser

    public String getWaitUser() { // 대기실 인원 정보
        String str = "";
        for (int i = 0; i < waitV.size(); i++) {
            //"길동|라임|주원"
            Service ser = waitV.get(i);
            str += ser.userName;
            if (i < waitV.size() - 1) str += "|";
        }
        return str;
    }//getWaitUser

    public void messageAll(String msg) {//전체사용자
        //접속된 모든 클라이언트(대기실+대화방)에게 메시지 전달
        for (int i = 0; i < allV.size(); i++) {//벡터 인덱스
            Service service = allV.get(i); //각각의 클라이언트 얻어오기
            try {
                service.messageTo(msg);
            } catch (IOException e) {
                //에러발생 ---> 클라이언트 접속 끊음!!
                allV.remove(i--); //접속 끊긴 클라이언트를 벡터에서 삭제!!
                System.out.println("클라이언트 접속 끊음!!");
            }
        }
    }//messageAll

    public void messageWait(String msg) {//대기실 사용자
        for (int i = 0; i < waitV.size(); i++) {//벡터 인덱스

            Service service = waitV.get(i); //각각의 클라이언트 얻어오기
            try {
                service.messageTo(msg);
            } catch (IOException e) {

                //에러발생 ---> 클라이언트 접속 끊음!!
                waitV.remove(i--); //접속 끊긴 클라이언트를 벡터에서 삭제!!
                System.out.println("클라이언트 접속 끊음!!");
            }
        }
    }//messageWait

    public void messageRoom(String msg) {//대화방사용자
        for (int i = 0; i < myRoom.userV.size(); i++) {//벡터 인덱스

            Service service = myRoom.userV.get(i); //각각의 클라이언트 얻어오기

            try {
                service.messageTo(msg);
            } catch (IOException e) {
                //에러발생 ---> 클라이언트 접속 끊음!!

                myRoom.userV.remove(i--); //접속 끊긴 클라이언트를 벡터에서 삭제!!
                System.out.println("클라이언트 접속 끊음!!");
            }
        }
    }//messageAll

    public void messageTo(String msg) throws IOException {
        //특정 클라이언트에게 메시지 전달 (실제 서버--->클라이언트 메시지 전달)
        out.write((msg + "\n").getBytes());

    }
}

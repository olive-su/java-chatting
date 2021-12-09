package com.project.client;

import javax.swing.*;

import com.project.common.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.Arrays;

public class ClientMainForm extends JFrame implements ActionListener, Runnable {
    User user = new User(); // 현재 사용자 객체
    CardLayout card = new CardLayout();
    Login login;
    SignUp signUp;
    WaitRoom waitRoom;
    ChatRoom chatRoom;

    // 네트워크
    Socket s; // 소켓
    BufferedReader in; // 수신
    OutputStream out; // 송신

    // DB 정보(Config 클래스로 부터 받아옴)
    String url = "jdbc:mysql://" + Config.getHost() + ":" + Config.getPort()
            + "/" + Config.getName() + "?useUnicode=true&characterEncoding=utf8";
    String uid = Config.getUser();
    String upw = Config.getPwd();

    String selectedRoom;

    public ClientMainForm() {
        login = new Login();
        signUp = new SignUp();
        waitRoom = new WaitRoom();
        chatRoom = new ChatRoom();

        login.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 로그인 이벤트 처리
        login.b1.addActionListener(this); // 로그인 버튼
        login.b2.addActionListener(this); // 회원가입 버튼

        // 대기실 이벤트 처리
        waitRoom.bt_create.addActionListener(this);
        waitRoom.bt_enter.addActionListener(this);
        waitRoom.bt_exit.addActionListener(this);
        waitRoom.roomInfo.addMouseListener(new MouseAdapter() {

            @Override

            public void mouseClicked(MouseEvent e) {
                String str = waitRoom.roomInfo.getSelectedValue(); //"1::자바방::2"
                if (str == null) return;
                System.out.println("STR=" + str);
                selectedRoom = str.substring(0, str.indexOf(":"));
                //"1::자바방"  <----  substring(0,3)

                //대화방 내의 인원정보 (대기실에서 우측에 표시)
                sendMsg(Function.ROOMUSERTOWAIT+ "||" + selectedRoom); // 방 인덱스 전송함
            }

        });

        // 채팅방 이벤트 처리
        chatRoom.sendTF.addActionListener(this);
        chatRoom.bt_exit.addActionListener(this);
    }


    public static void main(String[] args) {
        new ClientMainForm();
    }

    // 서버연결 -> 로그인 윈도우(default)
    public void connection(User user) {
        // 서버연결 => 로그인 요청
        try { // AWS 배포 계정 : "3.38.106.160"
            s = new Socket("localhost",1120); // server 가동해둔 IP 주소 (AWS 서버 가동해야함)
            // in: 서버 메시지 읽기객체    서버=msg=>클라이언트
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // out: 메시지 쓰기객체    클라이언트=msg=>서버
            out = s.getOutputStream();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 연결이 되면 지시를 받는다
        new Thread(this).start();
        sendMsg(Function.LOGIN + "||" + user.getUserId() + "|" + user.getUserName() +"\n");//(대기실)접속 알림
        waitRoom.setVisible(true);
        login.dispose();
    }

    public void sendMsg(String msg) {//서버에게 메시지 보내기
        try {
            out.write((msg + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//sendMsg

    @Override
    public void actionPerformed(ActionEvent e) {

        String userId=login.tf.getText();
        String userPassword=login.pf.getText();
        String userName = null;

        if(e.getSource()==login.b1) { // 로그인 버튼 눌렀을 때
            if(login.loginCheck(userId, userPassword)) { // 아이디 - 비밀번호 매칭 성공
                user.setUserId(userId);
                user.setUserPassword(userPassword);
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = null;
                    PreparedStatement pst = null;
                    ResultSet rst = null;
                    con = DriverManager.getConnection(url, uid, upw);
                    String sql1 = "SELECT user_name FROM user WHERE user_id=?";
                    pst = con.prepareStatement(sql1);
                    pst.setString(1, userId);
                    rst = pst.executeQuery();
                    if (rst.next()) {
                        user.setUserName(rst.getString("user_name"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                connection(user);
            }
        }
        else if(e.getSource()==login.b2) { // 회원가입
            signUp.setVisible(true);
            signUp.revalidate();
            signUp.repaint();
        }
        else if(e.getSource()==chatRoom.sendTF) {// DB에 메시지 저장, 화면 출력
            String sql2 = "INSERT INTO chat(user_id, msg) VALUES(?, ?)";

            // 입력된 데이터 읽기
            String msg=chatRoom.sendTF.getText();

            if (msg.length() > 0) {
                sendMsg(Function.MESSAGE + "||" + msg);
                chatRoom.sendTF.setText("");

                try { // DB에 메시지 저장
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = null;
                    PreparedStatement pst = null;
                    con = DriverManager.getConnection(url, uid, upw);
                    pst = con.prepareStatement(sql2);
                    pst.setString(1, userId); // 쿼리의 첫 번째 '?'에 들어갈 값
                    pst.setString(2, msg); // 쿼리의 두 번째 '?'에 들어갈 값
                    pst.executeUpdate();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getSource() == waitRoom.bt_create) {//방만들기 요청
            String title = JOptionPane.showInputDialog(this, "방제목:");

            //방제목을 서버에게 전달
            sendMsg(Function.CREATEROOM + "||" + title);

            chatRoom.setTitle("ʚ 버디메신저 ❧ [" + title + "] ɞ");

            sendMsg(Function.ROOMUSERTOROOM + "||");//대화방내 인원정보 요청

            waitRoom.setVisible(false); //창 끄기
            chatRoom.setVisible(true); //대화방이동
            chatRoom.revalidate();
            chatRoom.repaint();

        }
        else if (e.getSource() == waitRoom.bt_enter) {//방들어가기 요청

            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(this, "방을 선택해주세요!");
                return;
            }

            sendMsg(Function.ENTER + "||" + selectedRoom);

            sendMsg(Function.ROOMUSERTOROOM + "||");//대화방내 인원정보 요청

            waitRoom.setVisible(false); //창 끄기
            chatRoom.setVisible(true);
            chatRoom.revalidate();
            chatRoom.repaint();

        }
        else if (e.getSource() == chatRoom.bt_exit) {//대화방 나가기 요청

            sendMsg(Function.EXIT + "||");

            chatRoom.setVisible(false); //창 끄기
            waitRoom.setVisible(true);
            waitRoom.revalidate();
            waitRoom.repaint();

        }
        else if (e.getSource() == waitRoom.bt_exit) {// 나가기(프로그램종료) 요청
            System.exit(0);//현재 응용프로그램 종료하기
        }

    }

    /**
     * <서버가 보낸 메시지 읽기>
     * run 메소드 사용 이유 : GUI 프로그램 실행에 영향 X
     * 스레드메소드는 동시실행
     * */
    @Override
    public void run() {

        try {
            while (true) {
                String msg = in.readLine(); // msg : 서버가 보낸 메시지
                String msgs[] = msg.split("\\|\\|");
                String protocol = msgs[0];
                switch (protocol) {
                    case Function.CREATEROOM:// 방 만들기
                        //방 정보를 List에 뿌리기
                        if (msgs.length > 1) { // 개설된 방이 한개 이상일 때 실행
                            String roomNames[] = msgs[1].split("\\|");
                            //"1::방이름::1|2::방이름이야::1|3::방이름입니다::2"
                            waitRoom.roomInfo.setListData(roomNames);
                        }
                        break;

                    case Function.ROOMUSERTOWAIT://(대기실에서) 대화방 인원정보
                        System.out.println(msgs[1]);
                        String roomUsers[] = msgs[1].split("\\|");
                        System.out.println(Arrays.toString(roomUsers));
                        waitRoom.roomUser.setListData(roomUsers);
                        break;

                    case Function.ROOMUSERTOROOM://(대화방에서) 대화방 인원정보
                        String myRoomUsers[] = msgs[1].split("\\|");
                        chatRoom.liUser.setListData(myRoomUsers);
                        break;

                    case Function.WAITUSER://대기실 인원정보
                        String waitNames[] = msgs[1].split("\\|");
                        waitRoom.waitInfo.setListData(waitNames);
                        break;

                    case Function.ENTER://대화방 입장
                        chatRoom.ta.append("   ʚʚʚʚʚʚʚʚʚʚʚʚ [" + msgs[1] + "] 님이 입장하셨습니다. ɞɞɞɞɞɞɞɞɞɞɞɞ\n");
                        chatRoom.ta.setCaretPosition(chatRoom.ta.getText().length());
                        break;

                    case Function.ROOMTITLETOROOM://개설된 방의 타이틀 제목 얻기
                        chatRoom.setTitle("채팅방-[" + msgs[1] + "]");
                        break;

                    case Function.MESSAGE: // 메시지 전송 [msg]"MESSAGE||안녕하세요"
                        chatRoom.ta.append(msgs[1] + "\n");
                        chatRoom.ta.setCaretPosition(chatRoom.ta.getText().length());
                        break;

                    case Function.EXIT://대화방 퇴장
                        chatRoom.ta.append("   ʚʚʚʚʚʚʚʚʚʚʚʚ [" + msgs[1] + "] 님이 퇴장하셨습니다. ʚʚʚʚʚʚʚʚʚʚʚʚ\n");
                        chatRoom.ta.setCaretPosition(chatRoom.ta.getText().length());
                        break;
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

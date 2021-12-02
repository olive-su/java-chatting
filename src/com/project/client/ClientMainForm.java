package com.project.client;

import java.util.*;
import javax.swing.*;
import com.project.common.Function;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.sql.*;

public class ClientMainForm extends JFrame implements ActionListener, Runnable {
    CardLayout card = new CardLayout();
    Login login = new Login();
    SignUp signUp = new SignUp();
    WaitRoom wr = new WaitRoom();
    // 네트워크
    Socket s; // 소켓
    BufferedReader in; // 수신
    OutputStream out; // 송신

    // DB 정보 Config 클래스로 부터 받아옴
    String url = "jdbc:mysql://" + Config.getHost() + ":" + Config.getPort()
            + "/" + Config.getName() + "?useUnicode=true&characterEncoding=utf8";
    String uid = Config.getUser();
    String upw = Config.getPwd();


    public ClientMainForm() {
        setLayout(card);
        add("LOGIN", login);
        add("WR", wr);
        setSize(640, 480);
        setTitle("버디메신저");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        login.b1.addActionListener(this); // 로그인 버튼
        login.b2.addActionListener(this); // 회원가입 버튼

        wr.tf.addActionListener(this);
    }

    public static void main(String[] args) {
        new ClientMainForm();
    }

    public void connection(String id, String name) {
        // 서버연결 => 로그인 요청
        try {
            s = new Socket("3.38.106.160",1120); // server 가동해둔 IP 주소 (AWS 서버 가동해야함)
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = s.getOutputStream();
            // 연결이 되면 로그인 요청
            out.write((Function.LOGIN + "|" + id + "|" + name +"\n").getBytes());
        } catch (Exception ex) {
        }
        // 연결이 되면 지시를 받는다
        new Thread(this).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        String userId=login.tf.getText();
        String userPassword=login.pf.getText();
        String userName = null;

        if(e.getSource()==login.b1) { // 로그인 버튼 눌렀을 때
            if(login.loginCheck(userId, userPassword)) { // 로그인 성공 시
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
                        userName = rst.getString("user_name");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                connection(userId, userName);
            }
        }
        else if(e.getSource()==login.b2) { // 회원가입
            signUp.fr.setVisible(true);
        }
        else if(e.getSource()==wr.tf) // DB에 메시지 저장, 화면 출력
        {
            String sql2 = "INSERT INTO chat(user_id, msg) VALUES(?, ?)";

            // 입력된 데이터 읽기
            String msg=wr.tf.getText();

            try {
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

            if(msg.length()<1) return;

            try {
                out.write((Function.WAITCHAT+"|"+msg+"\n").getBytes());
            }catch(Exception ex) {}

            wr.tf.setText("");
        }

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            while (true) {
                String msg = in.readLine();
                StringTokenizer st = new StringTokenizer(msg, "|");
                int protocol = Integer.parseInt(st.nextToken());
                switch (protocol) {
                    case Function.LOGIN: {
                        String[] data= {
                                st.nextToken(),
                                st.nextToken()
                        };
                        wr.model2.addRow(data);
                    }
                    break;
                    case Function.MYLOG: {
                        setTitle(st.nextToken());
                        card.show(getContentPane(), "WR");
                    }
                    break;
                    case Function.WAITCHAT:{
                        wr.bar.setValue(wr.bar.getMaximum());
                        wr.ta.append(st.nextToken()+"\n");
                    }
                }

            }
        } catch (Exception ex) {
        }

    }
}

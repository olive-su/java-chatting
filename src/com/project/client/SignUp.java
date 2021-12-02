package com.project.client;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

import com.project.common.User;

public class SignUp extends JFrame implements ActionListener{
    JTextField tf1, tf2, tf3;
    JLabel l1, l2, l3;
    JButton bt1, bt2;
    JFrame fr = new JFrame();
    //Frame은 전역변수에 있어야함

    public SignUp(){
        fr.setTitle("버디메신저 회원가입");
        fr.setBounds(0, 0, 640, 480);
        fr.setLayout(null);

        l1 = new JLabel("아이디");
        l1.setBounds(180, 100, 100, 30);
        fr.add(l1);

        tf1 = new JTextField();
        tf1.setBounds(280, 100 , 100, 30);
        fr.add(tf1);

        l2 = new JLabel("비밀번호");
        l2.setBounds(180, 150, 100, 30);
        fr.add(l2);

        tf2 = new JPasswordField();
        tf2.setBounds(280, 150 , 100, 30);
        fr.add(tf2);

        l3 = new JLabel("닉네임");
        l3.setBounds(180, 200, 100, 30);
        fr.add(l3);

        tf3 = new JTextField();
        tf3.setBounds(280, 200 , 100, 30);
        fr.add(tf3);

        bt1 = new JButton("등록");
        bt1.setBounds(180, 300, 80, 40);
        fr.add(bt1);

        bt2 = new JButton("취소");
        bt2.setBounds(280, 300, 80, 40);
        fr.add(bt2);

        bt1.addActionListener(this);
        bt2.addActionListener(this);
        //add액션리스너(this): 이 클래스에서 정한 행동 취한다.
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource().equals(bt1)){
            //TF에서 얻은 값으로 빈 클래스를 set한다.
            User user = new User();
            user.setUserId(tf1.getText());
            user.setUserPassword(tf2.getText());
            user.setUserName(tf3.getText());

            insertUser(user);
        }else if(e.getSource().equals(bt2)){
            //프레임 창 끄기
            fr.dispose();

        }
    }

    static void insertUser(User user){
        // DB 정보 Config 클래스로 부터 받아옴
        String url = "jdbc:mysql://" + Config.getHost() + ":" + Config.getPort()
                + "/" + Config.getName() + "?useUnicode=true&characterEncoding=utf8";
        String uid = Config.getUser();
        String upw = Config.getPwd();

        Connection con = null;
        PreparedStatement pst = null;
        String sql = "INSERT INTO user(user_id, user_password, user_name) VALUES(?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, uid, upw);
            String userId = user.getUserId();
            String userPassword = user.getUserPassword();
            String userName = user.getUserName();

            pst = con.prepareStatement(sql);
            pst.setString(1, userId); // 쿼리의 첫 번째 '?'에 들어갈 값
            pst.setString(2, userPassword); // 쿼리의 두 번째 '?'에 들어갈 값
            pst.setString(3, userName); // 쿼리의 세 번째 '?'에 들어갈 값
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new SignUp();
    }

}
package com.project.client;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JPanel{
    private Image back;
    private JLabel la1, la2, la3, la4;
    JTextField tf;       // default로 지정하여 ClientMainForm에서 이용
    JTextField pf;   // default로 지정하여 ClientMainForm에서 이용
    JButton b1,b2;      // default로 지정하여 ClientMainForm에서 이용

    public Login()
    {
        //이미지 정보 읽기
        back=Toolkit.getDefaultToolkit().getImage("C:\\Users\\1466s\\IdeaProjects\\java_chatting_project\\image\\background.jpg");
        setLayout(null); //직접 배치
        la1=new JLabel("버디메신저", JLabel.CENTER);
        la1.setFont(new Font("", Font.BOLD, 50));
        la1.setBounds(160, 100, 300, 70);
        add(la1);
        la2=new JLabel("made by. Kim Su Gyeong", JLabel.CENTER);
        la2.setBounds(160, 160, 300, 70);
        add(la2);
        //로그인 부분 아이디와 아이디 칠 textField
        la3=new JLabel("아이디",JLabel.RIGHT);
        la3.setBounds(180, 250, 80, 30);
        tf=new JTextField();
        tf.setBounds(265, 250, 150, 30);
        add(la3); add(tf);

        //로그인 부분 비밀번호와 비밀번호 칠 textField
        la4=new JLabel("비밀번호",JLabel.RIGHT);
        la4.setBounds(180, 295, 80, 30);
        pf=new JPasswordField();
        pf.setBounds(265, 295, 150, 30);
        add(la4); add(pf);

        b1 = new JButton("로그인");
        b2 = new JButton("회원가입");
        JPanel p = new JPanel(); //패널을 배치하는 이유, 가운데 맞추기 어려워서 패널로 잡아준다
        p.add(b1);
        p.add(b2);
        p.setOpaque(false); // setOpaque -투명모드
        p.setBounds(200, 345, 235, 35);
        add(p);
    }

    public boolean loginCheck(String userId, String userPassword) {
        // DB 정보 Config 클래스로 부터 받아옴
        String url = "jdbc:mysql://" + Config.getHost() + ":" + Config.getPort()
                + "/" + Config.getName() + "?useUnicode=true&characterEncoding=utf8";
        String uid = Config.getUser();
        String upw = Config.getPwd();

        boolean flag = false;

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        String sql = "SELECT user_password FROM user WHERE user_id=?";
        String getPass = null;

        try{
            // 쿼리 수행
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, uid, upw);
            pst = con.prepareStatement(sql);
            pst.setString(1, userId);
            rst = pst.executeQuery();

            // 쿼리 결과값과 입력 pwd 값 비교
            if (rst.next()) {
                getPass = rst.getString("user_password"); // 쿼리 결과값
                if (getPass.equals(userPassword)) {
                    flag = true;
                } else {
                System.out.println("비밀번호가 일치하지 않습니다."); }
            }
            else {System.out.print("아이디가 존재하지 않습니다.");};

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // 쿼리 실행 후 모두 종료
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }


    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
    }

}
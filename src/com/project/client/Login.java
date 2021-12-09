package com.project.client;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame{
    private Image back;
    private JLabel l1, l2;
    JTextField tf;       // default로 지정하여 ClientMainForm에서 이용
    JTextField pf;   // default로 지정하여 ClientMainForm에서 이용
    JButton b1,b2;      // default로 지정하여 ClientMainForm에서 이용

    static JPanel title=new JPanel() { // 타이틀 이미지 추가
        Image image=new ImageIcon(Login.class.getResource("./image/logo.png")).getImage();
        public void paint(Graphics g) {//그리는 함수
            g.drawImage(image, 0, 0, null);//background를 그려줌
        }
    };

    static JPanel background=new JPanel() { // 배경 이미지 추가
        Image background=new ImageIcon(Login.class.getResource("./image/background.jpg")).getImage();
        public void paint(Graphics g) {
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    };

    public Login()
    {
        setSize(500, 500);
        setTitle("ʚ 버디메신저 ❧ 로그인 ɞ");
        //이미지 정보 읽기
//        back=Toolkit.getDefaultToolkit().getImage("C:\\Users\\1466s\\IdeaProjects\\java_chatting_project\\image\\background.jpg");
        setLayout(null); //직접 배치
        title.setLayout(null); //레이아웃 설정하기 위해.
        title.setBounds(80, 80, 400, 600);//패널의 위치와 크기.
        add(title);//프레임에 패널을 추가
        //로그인 부분 아이디와 아이디 칠 textField
        l1=new JLabel("아이디",JLabel.RIGHT);
        l1.setBounds(100, 250, 80, 30);
        tf=new JTextField();
        tf.setBounds(185, 250, 150, 30);
        add(l1); add(tf);

        //로그인 부분 비밀번호와 비밀번호 칠 textField
        l2=new JLabel("비밀번호",JLabel.RIGHT);
        l2.setBounds(100, 295, 80, 30);
        pf=new JPasswordField();
        pf.setBounds(185, 295, 150, 30);
        add(l2); add(pf);

        b1 = new JButton("로그인");
        b2 = new JButton("회원가입");
        JPanel p = new JPanel(); //패널을 배치하는 이유, 가운데 맞추기 어려워서 패널로 잡아준다
        p.add(b1);
        p.add(b2);
        p.setOpaque(false); // setOpaque -투명모드
        p.setBounds(120, 345, 235, 35);
        add(p);
        background.setLayout(null);
        background.setBounds(0, 0, 500, 500);
        add(background);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
                System.out.println("비밀번호 불일치"); }
            }
            else {
                JOptionPane.showMessageDialog(null, "아이디가 존재하지 않습니다.");
                System.out.print("아이디 미존재");
            };

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


}
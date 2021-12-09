package com.project.client;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.*;

import com.project.common.User;

public class SignUp extends JFrame implements ActionListener{
    JTextField tf1, tf2, tf3;
    JLabel l0, l1, l2, l3, l4;
    JButton b1, b2;
    //Frame은 전역변수에 있어야함

    static JPanel title=new JPanel() { // 타이틀 이미지 추가
        Image image=new ImageIcon(Login.class.getResource("./image/logo2.png")).getImage();
        public void paint(Graphics g) {//그리는 함수
            g.drawImage(image, 0, 0, null);//background를 그려줌
        }
    };

    static JPanel background=new JPanel() { // 배경 이미지 추가
        Image background=new ImageIcon(Login.class.getResource("./image/background2.jpg")).getImage();
        public void paint(Graphics g) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    };

    public SignUp(){
        setSize(500, 500);
        setTitle("ʚ 버디메신저 ❧ 회원가입 ɞ");
        setBounds(0, 0, 500, 500);
        setLayout(null); //직접 배치
        title.setLayout(null);
        title.setBounds(105, 0, 300, 300);
        add(title);
        l0=new JLabel("회원가입", JLabel.CENTER);
        l0.setFont(new Font("", Font.BOLD, 40));
        l0.setBounds(90, 100, 300, 70);
        add(l0);

        l1 = new JLabel("아이디");
        l1.setBounds(100, 190, 80, 30);
        add(l1);

        tf1 = new JTextField();
        tf1.setBounds(185, 190 , 150, 30);
        add(tf1);

        l2 = new JLabel("비밀번호");
        l2.setBounds(100, 225, 80, 30);
        add(l2);

        tf2 = new JPasswordField();
        tf2.setBounds(185, 225 , 150, 30);
        add(tf2);

        l3 = new JLabel("비밀번호는 영문과 특수문자, 숫자를 포함하며 8자 이상이어야 합니다.");
        l3.setForeground(new Color(204, 61, 61));
        l3.setBounds(45, 255, 500, 30);
        add(l3);

        l4 = new JLabel("닉네임");
        l4.setBounds(100, 295, 80, 30);
        add(l4);

        tf3 = new JTextField();
        tf3.setBounds(185, 295 , 150, 30);
        add(tf3);

        b1 = new JButton("확인");
        b2 = new JButton("취소");
        JPanel p = new JPanel(); //패널을 배치하는 이유, 가운데 맞추기 어려워서 패널로 잡아준다
        p.add(b1);
        p.add(b2);
        p.setOpaque(false); // setOpaque -투명모드
        p.setBounds(120, 345, 235, 35);
        add(p);
        b1.addActionListener(this);
        b2.addActionListener(this);
        //add액션리스너(this): 이 클래스에서 정한 행동 취한다.
        background.setLayout(null);
        background.setBounds(0, 0, 500, 500);
        add(background);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource().equals(b1)){
            //TF에서 얻은 값으로 빈 클래스를 set한다.
            User user = new User();
            user.setUserId(tf1.getText());
            user.setUserPassword(tf2.getText());
            user.setUserName(tf3.getText());

            if(checkPassword(user.getUserPassword())){
                insertUser(user);
            } else {
                JOptionPane.showMessageDialog(null, "비밀번호 형식이 일치하지 않습니다.", "비밀번호 오류", JOptionPane.ERROR_MESSAGE);
            }

        }else if(e.getSource().equals(b2)){
            //프레임 창 끄기
            dispose();

        }
    }

    private boolean checkPassword(String password) {

        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern passPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher = passPattern.matcher(password);

        if (!passMatcher.find()) {
            return false;
        }
        else {return true;}
    }

    static void insertUser(User user){
        // DB 정보 Config 클래스로 부터 받아옴
        String url = "jdbc:mysql://" + Config.getHost() + ":" + Config.getPort()
                + "/" + Config.getName() + "?useUnicode=true&characterEncoding=utf8";
        String uid = Config.getUser();
        String upw = Config.getPwd();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        String sql1 = "SELECT * FROM user WHERE user_id=?";
        String sql2 = "INSERT INTO user(user_id, user_password, user_name) VALUES(?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, uid, upw);
            String userId = user.getUserId();
            String userPassword = user.getUserPassword();
            String userName = user.getUserName();

            pst = con.prepareStatement(sql1);
            pst.setString(1, userId);
            rst = pst.executeQuery();
            if(rst.next()){ // 동일 아이디 존재
                JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.", "아이디 오류", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    pst = con.prepareStatement(sql2);
                    pst.setString(1, userId); // 쿼리의 첫 번째 '?'에 들어갈 값
                    pst.setString(2, userPassword); // 쿼리의 두 번째 '?'에 들어갈 값
                    pst.setString(3, userName); // 쿼리의 세 번째 '?'에 들어갈 값
                    pst.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new SignUp();
    }

}
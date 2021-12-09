package com.project.client;

import java.awt.Color;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ChatRoom extends JFrame{
    //채팅방
    JTextField sendTF;
    JLabel la_msg;

    JTextArea ta;
    JScrollPane sp_ta,sp_list;

    JList<String> liUser;
    JButton bt_exit;

    JPanel p;
    public ChatRoom() {
        setTitle("ʚ 버디메신저 ❧ 채팅방 ɞ");
        sendTF = new JTextField(15);
        la_msg = new JLabel("Message");

        ta = new JTextArea();
        ta.setLineWrap(true); //TextArea 가로길이를 벗어나는 text 발생시 자동 줄바꿈 발생
        liUser = new JList<String>();

        sp_ta = new JScrollPane(ta);
        sp_list = new JScrollPane(liUser);
        sp_list.setBorder(new TitledBorder("대화 참여자"));
        bt_exit = new JButton("나가기");

        p = new JPanel();

        sp_ta.setBounds(10,10,380,390);
        la_msg.setBounds(10,410,60,30);
        sendTF.setBounds(70,410,320,30);

        sp_list.setBounds(400,10,120,350);
        bt_exit.setBounds(400,380,120,60);

        p.setLayout(null);
        p.setBackground(new Color(255, 167, 167));
        p.add(sp_ta);
        p.add(la_msg);
        p.add(sendTF);
        p.add(sp_list);
        p.add(bt_exit);

        add(p);
        setBounds(300,200,550,500);
        //setVisible(true);
        sendTF.requestFocus();

    }//생성자

}
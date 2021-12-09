package com.project.client;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class WaitRoom extends JFrame{
    JList<String> roomInfo, roomUser, waitInfo;
    JScrollPane sp_roomInfo, sp_roomUser, sp_waitInfo;
    JButton bt_create, bt_enter, bt_exit;

    JPanel p;

    public WaitRoom() {
        setTitle("ʚ 버디메신저 ❧ 대기실 ɞ");

        roomInfo = new JList<String>();
        roomInfo.setBorder(new TitledBorder("방 정보"));

        roomUser = new JList<String>();
        roomUser.setBorder(new TitledBorder("인원 정보"));
        waitInfo = new JList<String>();
        waitInfo.setBorder(new TitledBorder("대기실 정보"));

        sp_roomInfo = new JScrollPane(roomInfo);
        sp_roomUser = new JScrollPane(roomUser);
        sp_waitInfo = new JScrollPane(waitInfo);

        bt_create = new JButton("방 만들기");
        bt_enter = new JButton("입장하기");
        bt_exit = new JButton("나가기");

        p = new JPanel();

        sp_roomInfo.setBounds(10, 10, 300, 300);
        sp_roomUser.setBounds(320, 10, 150, 300);
        sp_waitInfo.setBounds(10, 320, 300, 130);

        bt_create.setBounds(320, 330, 150, 30);
        bt_enter.setBounds(320, 370, 150, 30);
        bt_exit.setBounds(320, 410, 150, 30);

        p.setLayout(null);
        p.setBackground(new Color(178, 204, 255));
        p.add(sp_roomInfo);
        p.add(sp_roomUser);
        p.add(sp_waitInfo);
        p.add(bt_create);
        p.add(bt_enter);
        p.add(bt_exit);

        add(p);
        setBounds(300, 200, 500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

}
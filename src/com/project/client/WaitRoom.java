package com.project.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class WaitRoom extends JPanel {
    JTable table1, table2;
    DefaultTableModel model1, model2;
    JTextField tf;
    JTextArea ta;
    JButton b1, b2, b3, b4, b5, b6;
    JScrollBar bar;

    public WaitRoom() {

        String[] col2 = { "대화 참여자" };
        String[][] row2 = new String[0][2];

        model2 = new DefaultTableModel(row2, col2);
        table2 = new JTable(model2);
        JScrollPane js2 = new JScrollPane(table2);

        ta = new JTextArea();
        ta.setEditable(false);
        JScrollPane js3 = new JScrollPane(ta);
        bar = js3.getVerticalScrollBar();

        tf = new JTextField();

        // 배치
        setLayout(null);
//        js1.setBounds(10, 15, 600, 500);
        js2.setBounds(440, 15, 160, 420);
//        add(js1);
        add(js2);

        js3.setBounds(10, 15, 420, 380);
        add(js3);

        tf.setBounds(10, 402, 420, 30);
        add(tf);

    }
}

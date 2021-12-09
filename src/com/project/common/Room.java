package com.project.common;
import com.project.server.Service;

import java.util.Vector;

public class Room {//대화방의 정보표현 객체

    private String idx;// 방 고유 인덱스
    private String title;//방 이름
    private int count;//방 인원수
    private String leader;//방장(방 개설자)
    public Vector<Service> userV;//userV: 같은 방에 접속한 Client정보 저장

    public String getIdx() {
        return idx;
    }
    public void setIdx(String idx) {
        this.idx = idx;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getLeader() {
        return leader;
    }
    public void setLeader(String leader) {
        this.leader = leader;
    }

    public Room() {
        userV = new Vector<>();
    }
}
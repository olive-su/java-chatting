# 자바 채팅 프로그램

## 목차
1. [개요](##Ⅰ.-개요)
2. [UML 설계서](##Ⅱ.-UML-설계서)
3. [디렉토리 구성도](##Ⅲ.-디렉토리-구성도)
4. [실행 화면](##Ⅳ.-실행-화면)
5. [프로젝트 추진 일정](##Ⅴ.-프로젝트-추진-일정)
6. [개발 후기](##Ⅵ.-개발-후기)

<br>
<br>

---

## Ⅰ. 개요
1. 기간
- 2021.10.27. ~ 2021.12.09.
<br>

2. 목적
- 자바 프로그래밍의 실시간 통신 프로세스를 이해한다.
- 로컬 DB를 통한 데이터 저장을 통해 데이터  DB 관리 능력을 향상시킨다.
- 자바의 인터페이스 시스템인 JFrame, Swing의 내부 모듈을 이용해보며 UI 설계 관점을 향상 시킨다.
<br>

3. 범위
- Java Swing과 socket을 이용한 실시간 채팅프로그램 
- 채팅방을 통한 다자간 채팅 및 로그인, 회원가입을 통한 회원관리 기능 구현
<br>

4. 개발 도구

|구분| 명칭|
|-----|---|
| OS  | Windows 10|
| Language | Java |
| DB | MySQL|

</br>
</br>

---
## Ⅱ. UML 설계서
<img src="https://user-images.githubusercontent.com/67156494/157795546-43cc87ff-67f3-4637-b80e-9b58de24703a.png">
</br>
</br>

---
## Ⅲ. 디렉토리 구성도
```
.
└── project
    ├── client
    │   ├── ChatRoom.java  // 채팅창
    │   ├── ClientMainForm.java // 메인 시작(전체 모듈 연결)
    │   ├── Config.java // DB 정보
    │   ├── Login.java // 로그인
    │   ├── SignUp.java // 회원가입
    │   ├── WaitRoom.java // 대기실
    │   └── image
    │       ├── background.jpg
    │       ├── background2.jpg
    │       ├── logo.png
    │       └── logo2.png
    ├── common
    │   ├── Function.java // 소켓통신 프로토콜 정의
    │   ├── Room.java // 방 정보 클래스
    │   └── User.java // 유저 정보 클래스
    └── server
        ├── Server.java // 서버
        └── Service.java // 소켓통신 메인 서버
```

</br>
</br>

---
 ## Ⅳ. 실행 화면
 ### 1. 로그인
  
<img src="https://user-images.githubusercontent.com/67156494/157796672-0bc4ad50-ddc5-4c76-b614-c39b4b9e7efc.png" width="500" height="500">

<details>
  <summary> 오류 메시지 </summary>
  </br>
  
  1-1. 아이디 미 존재  
    <img src="https://user-images.githubusercontent.com/67156494/157796681-6e4fffe0-b1d7-4b44-bb12-5875284d37bf.png" width="500" height="500">  
    </br>
    </br>
 
  
  1-2. 비밀번호 불일치
    </br>
    <img src="https://user-images.githubusercontent.com/67156494/157796470-0623b2ad-874f-4fd7-98f1-a21bba724338.png" width="500" height="500">
</details>

</br>
</br>
  
 ### 2. 회원가입
 
 <img src="https://user-images.githubusercontent.com/67156494/157797274-727c59c0-1ab1-499c-adac-3eaeea16385b.png" width="500" height="500">  

<details>
  <summary> 오류 메시지 </summary>
  <br>
  
  2-1. 아이디 존재  
    <img src="https://user-images.githubusercontent.com/67156494/157797283-65ad9132-c40b-4f9b-8aef-0502fb15bc2d.png" width="500" height="500">  
    <br/>
    <br/>
 
  
  2-2. 비밀번호 형식 불일치
    <br/>
    <img src="https://user-images.githubusercontent.com/67156494/157797290-eb8b67d9-8e2d-4293-86ad-ca06d1cacaff.png" width="500" height="500">  
</details>
 
</br>
</br>
  
 ### 3. 대기실
 
 `
 방 표시 형식 : "[방 번호]::[방 이름]::[참여 인원]"
 `

 3-1. 방 만들기
  <br>
 
 <img src="https://user-images.githubusercontent.com/67156494/157797778-937ff171-a355-4376-9323-468623f65727.png" width="500" height="500">  

<br>
<br>

3-2. 입장하기
 <br>
 
 <img src="https://user-images.githubusercontent.com/67156494/157797858-ce35b3c5-6118-4c8b-ab81-50472462563f.png" width="500" height="500">  

</br>
</br>
 
  ### 4. 채팅방
 <br>
 
 <img src="https://user-images.githubusercontent.com/67156494/157798039-f157ab8f-946a-4af9-bb31-84e3d60e5af0.png" width="500" height="500">  
 
</br>
</br>

---
## Ⅴ. 프로젝트 추진 일정
| 단계                 |      세부 단계           |  |                 |        프로젝트 일정         |                 |                 |
| :----: | --------------- | :---: | :---: | :---: | :---: | :---: |
|                      |                 | 1주차(10.27.~)                         | 2주차(11.03.~) | 3주차(11.10.~) | 4주차(11.17.~) | 5주차(11.24.~) |
| **설계**                 | 프로젝트 계획   | O                                      |                 |                 |                 |                 |
|                      | 프로토타입 제작 |                                        | O               |                 |                 |                 |
| **개발**                 | DB 연결         |                                        | O               |                 |                 |                 |
|  |       로그인-회원가입 구현          |                                        |                 | O               |                 |                 |
|     |         실시간 통신 구현         |                                        |                 |                 | O               |                 |
| **테스트**              | 테스트          |                                        |                 |                 |                 | O               |
| **안정화**               | 기능 보완       |                                        |                 |                 |                 | O               |

<br>
<br>

---
## Ⅵ. 업데이트 방향

<회원 관리 시스템>
- 비밀번호 암호화 진행 후, DB에 저장하기
  -  시스템의 보안 취약점 보완을 위해 암호화를 진행한 뒤 DB에 저장한다.

<채팅방 시스템>
- 인원 수 제한
  - 사용자 증가 시, 시스템 과부하가 되는 걸 방지하기 위해 인원 수 제한 시스템을 도입한다.
- 공개 / 비공개 채팅방 구현
  - 공개 / 비공개 채팅방 기능을 도입하여 사용자들의 프라이버시를 보장한다.
- 방 정보 변경
  - 방장 권한 넘겨주기, 비공개 방 비밀번호 변경과 같이 방 생성 옵션을 추가한다.

  

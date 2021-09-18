# YUTalk

자바로 Thread와 Socket 통신을 사용하여 구현한 채팅 서버입니다.

## Getting Started / 어떻게 시작하나요?

Intellij로 프로젝트를 열고 프로젝트를 빌드하여 실행하시면 됩니다.

### Prerequisites / 선행 조건

아래 사항들이 설치가 되어있어야 하며 데이터베이스 테이블도 생성하셔야 합니다.

```
JDK 1.8 이상, Intellij, MySQL Server, mysql-connector-java
```

### mysql-connector-java 라이브러리 설치
1. [mysql-connector-java](https://dev.mysql.com/downloads/connector/j/) 에서 Platform Independent 선택후 다운로드
2. 다운로드 받은 압축파일을 압축을 해제
3. Intellij에서 File > Project Structure > Libraries > + > Java 선택 후 압축 해제한 파일중 mysql-connector-java-버전명.jar 파일을 선택하여 라이브러리 추가

### 데이터베이스 테이블 생성
Intellij에서 터미널을 열고 MySQL 서버를 접속하여 아래의 명령어를 입력합니다.
```
source init.sql
```

## Built With / 누구랑 만들었나요?

* [박세훈](https://github.com/psh3253) - 프로젝트 전체 설계 및 제작

## Function / 기능
+ YUTalk의 요청 처리
+ 사용자 데이터 관리

## Technology / 기술

+ Thread를 사용하여 여러 클라이언트와 통신
+ Socket 통신을 사용하여 클라이언트와 데이터 교환
+ MySQL을 사용하여 사용자의 정보, 채팅방, 채팅 내용 등을 저장

## License / 라이센스

이 프로젝트는 GPL-3.0 라이센스로 라이센스가 부여되어 있습니다. 자세한 내용은 LICENSE 파일을 참고하세요.
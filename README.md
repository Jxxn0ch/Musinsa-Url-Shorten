## URL 축소 프로젝트



### 개발환경

-----

- Mac OS
- Intellij IDEA
- Java 11
- Spring Boot 2.3.0

### 실행방법

-----

1. 프로젝트를 받고자 하는 경로에서 `git clone https://github.com/Jxxn0ch/Url-Shorten.git` 실행
2. Clone 된 경로로 이동 후 `./gradlew bootrun` 실행
3. 브라우저에서 <http://localhost> 접속
4. 입력창에 축소 시키고자 하는 주소 입력
5. 반환되는 항목 중 **축소된 주소** 의 URL 클릭

### 작동방식

-----

1. 변환하고자 하는 주소를 입력받는다
2. Base62에서 url에서 사용하지 못하는 문자 두개를 제외한 62개의 문자를 기준으로, 랜덤함수를 호출하여 총 8자리의 텍스트를 만든다
3. 기존 입력받은 주소와 임의로 생성한 8자리 문자를 In-memory DB에 적재한다
4. localhost/임의8자리 텍스트를 주소창에 입력 시, DB 검증 후 원주소로 Redirect 시킨다

감사합니다




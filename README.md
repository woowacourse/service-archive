# Question & Answer Archive

# 로컬 개발 환경 설정

---

## git clone(submodule까지 clone)

```bash
git clone --recurse-submodules https://github.com/woowacourse/archive.git
```

## 로컬 서버 시작시 config location 설정

- IntelliJ -> Run -> Edit Configurations...
- kotlin.camp.nextstep.Application.kt 추가 또는 선택 후 VM Options에 다음 설정 추가

```
-Dspring.config.location=classpath:/config/
```

- Test 수행을 위하여 Template -> JUnit 선택 후 VM options에도 추가

![test config](./resources/setting_test_config.png)

## submodule 저장소에 변경 내용 반영

```bash
git submodule foreach git pull

git submodule foreach git add .

git submodule foreach git commit -m "commit message"

git submodule foreach git push origin master
```

### 목적

> Slack에서 휘발되는 질문 응답 내용들을 주기적으로 저장하고 싶다.

---

### 아카이빙 기능

1. 슬랙 특정 채널의 질문을 조회한다.
2. 질문에 해당하는 응답을 조회한다.
 - 슬랙의 Thread는 질문 timestamp에 작성된 history를 의미한다. 이를 기준으로 조회한다. 
3. 질문/응답 리스트를 DB에 저장한다.
4. 수동으로 데이터를 추가할 수 있어야 한다.

---

## 추후 작업 계획

### 공통 요구사항

1. 조회 결과 DTO 작성
2. 예외처리 추가
3. 도메인 설정
4. 배포 라인 구성
5. 프로퍼티 설정

### 아카이빙 기능 요구사항

1. 수동으로 데이터를 추가할 수 있어야 한다. (인가 정책은?)
2. 질문이 동기화된 이후에 응답이 달릴 경우에도 동기화되어야 한다.

### 웹 기능 요구사항

1. 질문을 조회할 수 있어야 한다. (검색 조건은?)
2. 질문별 대화를 조회할 수 있어야 한다.
3. 조회 결과에 User 정보가 매칭되어 있어야 한다.
4. 수동으로 동기화가 가능하여야 한다.

### 배치 작업 요구사항
 
1. 주기적으로 히스토리와 사용자 정보를 업데이트 한다. 
 - 질문/응답의 경우 글 작성 시간, 사용자 정보의 경우 업데이트 시간을 기준으로 동기화한다.

### 사용자 데이터 관리 요구사항

1. 슬랙의 유저 정보를 조회한다.
2. 슬랙의 유저 정보를 DB에 저장한다.

---

1. 채널과 토큰 정보를 디비에서 관리하는 것은 어떨까
2. 공지사항 등 여러 채널을 아카이빙 할 필요는 없을까
3. 슬랙에서 이전 질문 이력을 질문할 니즈는 없을까 (봇 등을 이용해서)

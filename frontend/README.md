# ssafy-vue

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


# [0713]
### 회원가입 폼 에러
<img src="/uploads/25917fba7ddf8fa6b859160d351653ec/image.png">

다음과 같이 open을 true로 하면
=> 해결 완료 (오타가 문제였다.. 오타확인 필수!!)

<img src="/uploads/a9f580d7797c8e765db6dc5379c0e79b/image.png">

해당 폼이 완성된다(아직 css를 조절하지 않음..)

내일 에러를 처리하고 로그인 회원가입에 대한 디테일한 규칙을 코딩할 것


# [0714]

## 회원가입 유효성 검사

```jsx
rules: {
        department: [
          { required: false, message: '최대 30자까지 입력 가능합니다.',trigger: 'blur', min:3, max: 30}
        ],
        position:[
          { required: false, message: '최대 30자까지 입력 가능합니다.', max:30}
        ],
        name: [
          { required: true, message: '필수 입력 항목입니다.'},
          { required: true,  message: '최대 30자까지 입력 가능합니다.',  max:30 }
          // 중복된 아이디 체크 에러 메세지
        ],
        id: [
          { required: true, message: '필수 입력 항목입니다.'},
          { required: true, message: '최대 16 글짜까지 입력 가능합니다.', max:16 }
        ],
        password: [
          { required: true, message: '필수 입력 항목입니다.'},
          { required: true, message: '최소 9 글자를 입력해야합니다.', trigger: 'blur',min: 9},
          { required: true, message: '최대 16 글자까지 입력가능합니다.', trigger: 'blur',max: 16}
        ],
        passwordCheck:[
          {required: true, message: '필수 입력 항목입니다.'},
          { required: true, message: '최소 9 글자를 입력해야합니다.', trigger: 'blur',min: 9},
          { required: true, message: '최대 16 글자까지 입력가능합니다.', trigger: 'blur',max: 16}
          //password와 동일하지 않은경우 에러 메세지

        ]
      },
```

- department(직책) : 최대 30자까지 입력 가능
- position(부서) : 최대 30자까지 입력 가능
- name(이름) : [필수] 최대 30자까지 입력 가능
- id(아이디) : [필수] 최대 16글자까지 입력 가능

중복된 아이디 체크 에러 메세지 구현

(만약 db에 해당 id가 있으면 true, 없으면 false 리턴?)

- password(패스워드) : [필수] : 최대 16글자까지 입력 가능

영문+숫자+특수문자 체크 구현

(password가 영문, 숫자, 특수문자로 주어지지 않는다면, 에러 메세지 출력 구현)

?

- passwordCheck(패스워드 확인) : [필수] 최대 16글자까지 입력 가능

password와 동일한지 체크

----------------------------------------------------------------------------------------------------------


# [0715]

회원 아이디 중복 && 비밀번호 확인

## Optional??

- JAVA 8 버전부터 지원하는 Optional에 대해서 알아보자

개발을 할 때 가장 많이 발생하는 예외 중 하나가 바로 NPE(NullPointerException)이다. NPE를 피하기 위해서는 null을 검사하는 로직을 추가해야하는데, null 검사를 해야하는 변수가 많은 경우 코드가 복잡해지고 로직이 상당히 번거롭다.

Optional은 null이 올 수 있는 값을 감싸는 Wrapper 클래스로, 참조하더라도 NPE가 발생하지 않도록 도와준다. Optional 클래스는 아래와 같은 value에 값을 저장하기 때문에 null이더라도 바로 NPE가 발생하지 않으며, 클래스이기 때문에 각종 메소드를 제공해준다.

```java
/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class UserRepositorySupport {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QUser qUser = QUser.user;

    public Optional<User> findUserByUserId(String userId) {
        User user = jpaQueryFactory.select(qUser).from(qUser)
                .where(qUser.userId.eq(userId)).fetchOne();
        if(user == null) {
        	return Optional.empty();
        }
        return Optional.ofNullable(user);
    }
}
```

### 스켈레톤 코드

```java
//UserServiceImpl
@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		User user = userRepositorySupport.findUserByUserId(userId).get();
		return user;
}
```

유저 정보를 조회하기를 할 경우 다음과 같은 에러가 발생한다.

- java.util.NoSuchElementException: No value present

### 수정 후

```java
@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		Optional<User> user = userRepositorySupport.findUserByUserId(userId);

        if (user.isPresent()) {
            User findUser = user.get();
            return findUser;
        }
        return null;
}
```

Optional(?)로 가져온 user 정보가 있을 시 아이디를 리턴한다. 없으면 null을 리턴한다.

이 부분에서 결국 null로 리턴하면 Optional을 쓰는 방법이 잘못 된 거 같다....

확인 해봐야겠다.

---

---

---

---

---

## VUE3 변경점

다음 사이트를 참고하여 쉽게 적응할 수 있었다.

data와 method 등의 선언이 setup으로 바뀐점을 쉽게 확인 할 수 있었습니다.

[Vue 3 변경점 정리: 무엇이 바뀌나요?](https://velog.io/@bluestragglr/Vue3-%EB%AC%B4%EC%97%87%EC%9D%B4-%EB%B0%94%EB%80%8C%EB%82%98%EC%9A%94)

## 비밀번호 check

password 와 passwordCheck가 같을 때 store.dispatch를 한다.

같지 않을 경우 alert 창으로 "비밀번호가 동일하지 않습니다." 출력

```jsx
if(state.form.passwordCheck == state.form.password){

            store.dispatch('root/requestJoin', {
              department: state.form.department,
              position: state.form.position,
              name: state.form.name,
              id: state.form.id,
              password: state.form.password,
              passwordCheck: state.form.passwordCheck
              })

            .then(function (result) {
              console.log("result.id" +result.id)
              if(result.status === 200){
                alert("회원가입 성공")
                emit('closeJoinDialog')
              }

            })
            .catch(function (err) {
              alert(err)
            })
          }else{
            alert("비밀번호가 동일하지 않습니다.")
          }
```

리팩토링 하는 과정을 거쳐서 에러를 따로 해결하는 방법을 공부한 후 처리할 예정

### 패스워드 영문, 숫자, 특수문자 포함 체크

```jsx
<-- 비밀번호에서 영문, 숫자, 특수문자 확인-->
<el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password @blur="checkPassword"></el-input>
        <div v-if="!state.passwordFlag">유효하지 않는 비밀번호입니다.</div>
        <div v-else>유효한 비밀번호입니다.</div>
 </el-form-item>
```

```java
//비밀번호 숫자,영문,특수문자 확인
    const checkPassword = function() {
      console.log("upw" + state.form.password)
      console.log("passwordFlag" + state.passwordFlag)
      if (/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$/.test(state.form.password)){
        state.passwordFlag = true
      } else {
        state.passwordFlag = false
      }
    }
```

현재는 영어 소문자, 대문자, 숫자를 활용한 유효성 검사를 했다.

내일 특수문자를 추가할 예정

# [0716]

### 특수문자 추가

```java
//비밀번호 숫자,영문,특수문자 확인
    const checkPassword = function() {
      if (/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(state.form.password)){
        state.passwordFlag = true
      } else {
        state.passwordFlag = false
      }
    }
```

### 비밀번호 확인 유무 변경

store에 dispatch하기전 확인 → 유효성 검사

### 기존 코드

```jsx
//dispatch 하기전 패스워드 확인
if(state.form.passwordCheck == state.form.password){
		store.dispatch('root/requestJoin', {
          department: state.form.department,
          position: state.form.position,
          name: state.form.name,
          id: state.form.id,
          password: state.form.password,
          passwordCheck: state.form.passwordCheck
          })

        .then(function (result) {
          console.log("result.id" +result.id)
          if(result.status === 200){
            alert("회원가입 성공")
            emit('closeJoinDialog')
          }

        })
        .catch(function (err) {
          alert(err)
        })
      }else{
        alert("비밀번호가 동일하지 않습니다.")
      }
```

### 바꾸고 난후

input에 Flag로 일치 유무를 확인한다.

```jsx

<-- 비밀번호가 동일한지 확인-->
<el-form-item prop="passwordCheck" label="비밀번호 확인" :label-width="state.formLabelWidth">
   <el-input v-model="state.form.passwordCheck" autocomplete="off" show-password @blur="passwordCheckValid"></el-input>
    <div v-if="!state.passwordCheckFlag">비밀번호가 동일하지 않습니다.</div>
</el-form-item>
```

```java
//비밀번호 일치 유무 확인
    const passwordCheckValid = function(){
      if(state.form.password === state.form.passwordCheck){
        state.passwordCheckFlag = true
      }else{
        state.passwordCheckFlag = false
      }
    }
```

## test

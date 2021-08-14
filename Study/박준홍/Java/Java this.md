# Java `this`

- 개념
  - `this`는 인스턴스의 자기 자신을 의미한다.
- 역할
  - 자기 자신의 메모리를 가르킨다.
  - 생성자에서 다른 생성자를 호출할 경우에 사용한다.
  - 인스턴스 자신의 주소를 반환할 때 사용한다.



## 자기 자신의 메모리를 가르키는 `this`

```java
public class StudentTest {
	public static void main(String[] args) {
		Student studentPark = new Student();
		
		studentPark.setStudentID(150);
		studentPark.setStudentName("피망");
	}
}
```

- setter 설정을 해보자.

```java
public class Student {
	
	private int studentID; //학번
	private String studentName; //학생 이름
	
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
}
```

- 여기에서의 this 키워드는 자기 자신의 메모리를 가르키기 때문에 멤버변수의 studentID와 동일한 의미가 된다.

- 이 상황에서 `this`를 사용하지 않는다면?
  - 매개변수 이름과 대입하는 이름이 같기 때문에 구분할 수 없다.
    그로 인한 에러가 발생한다.
    물론, 같게 하지 않아도 되지만, 가독성 면에서 this 로 해서 가는 것이 좋다.



## 생성자에서 다른 생성자를 호출할 경우 사용하는 `this`

- 한 클래스에서 매개변수가 없는 생성자와 매개변수가 있는 생성자를 만든다.
  매개변수가 없는 생성자에서 매개변수가 있는 생성자를 참조해서 메인 함수에서 생성자를 호출할 때, 값이 자동으로 대입된다.


	
```java
public class Student {
    private int studentID; //학번
    private String studentName; //학생 이름

    public Student() {
        this(150, "피망");
    }

    public Student(int studentID, String studentName) {
        this.studentID = studentID;
        this.studentName = studentName;
    }

    public void showInfo() {
        System.out.println("학번: " + studentID);
        System.out.println("이름: " + studentName);
    }
}
```

- 이 경우에는 매개 변수가 없는 생성자에서 this 를 사용해서 다른 생성자를 호출할 경우에는 첫 번째 구문이 되어야 한다.



## 인스턴스 자신의 주소를 반환할 때 사용한다.

```java
public class Student {	
	private int studentID; //학번
	private String studentName; //학생 이름
 
	public Student(int studentID, String studentName) {
		this.studentID = studentID;
		this.studentName = studentName;
	}
	
	public Student getSelf() {
		return this;
	}
}

public class StudentTest {
	public static void main(String[] args) {
		
		Student studentPark = new Student(200, "Park");
		System.out.println(studentPark);
		
		Student test = studentPark.getSelf();
		System.out.println(test);
	}
}
```

- 출력값은 같다.
- this가 가르치는 위치와 생성된 객체가 가르치는 위치가 같다.
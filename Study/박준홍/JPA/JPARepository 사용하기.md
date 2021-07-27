# JPA Repository 사용하기





## 우리는 메소드명을 설정하는 것으로 DB 에 접근할 수 있다!

- 상황

  - 어떤 String 을 받아서 TopicAgree 와 TopicOpposite 컬럼에 있는 값을 꺼내고 싶다.
  - 어떤 String 을 받아서 title 컬럼에 있는 값을 찾아서 꺼내고 싶다.

- 방법

  ```java
  public interface RoomRepository extends JpaRepository<Room, Long>{
  	
      Optional<Room> findByTopicAgreeOrTopicOpposite(String topicAgree, String topicOpposite);
      Optional<Room> findByTitle(String title);
  	
  }
  ```

  - 이런 방식으로 메소드의 이름을 짓는 것으로 만들 수 있다.



### 기본적인 기능

| method    | 기능                                     |
| --------- | ---------------------------------------- |
| save()    | 레코드 저장                              |
| findOne() | pk 로 레코드 하나 찾기                   |
| findAll() | 전체 레코드 불러오기, 정렬과 페이징 가능 |
| count()   | 레코드 갯수                              |
| delete()  | 레코드 삭제                              |



### Query 메소드 포함하기

| 키워드           | 설명                                      | 예시                                                  |
| ---------------- | ----------------------------------------- | ----------------------------------------------------- |
| And              | 여러 필드를 and 로 검색한다.              | `findByTopicAAndTopicB(String topicA, String topicB)` |
| Or               | 여러 필드를 or로 검색한다.                | `findByTopicAOrTopicB(String topicA, String topicB)`  |
| Between          | 필드의 두 값 사이에 있는 항목을 검색한다. | `findByCreatedAtBetween(Date fromDate, Date toDate)`  |
| LessThan         | 해당 값보다  작은 항목 검색한다.          | `findByAgeLessThan(int age)`                          |
| GreaterThanEqual | 해당 값보다 크거나 같은 항목을 검색한다.  | `findByAgeGreaterThanEqul(int age)`                   |
| Like             | like 검색                                 | `findByNameLike(String name)`                         |
| IsNull           | null 인 항목 검색                         | `findByJobIsNull()`                                   |
| In               | 여러 값중에 하나인 항목 검색              |                                                       |
| OrderBy          | 검색 결과를  정렬하여 전달한다.           | `findByEmailOrderByNameAsc(String email)`             |

- 반드시 CamelCase 로 이름을 지어야 한다.
  ( 언더바 `_` 사용하면 안된다!)





## Optional 에 대해서!

- 상황
  - Optional이 뭔지 몰라서 찾아보다가 컬렉션을 반환하는 Spring Data JPA Repository 메서드는 null을 반환하지 않고 비어있는 컬렉션을 반환해준다는 것을 알았다.
    리턴받고 싶은 것이 컬렉션으로 쭉~ 나열되는 것이라면 Optional을 쓰지 않아도 된다는 것이다.



## Like 에 대해서

- 상황

  - 우리가 검색을 하는데 있어서 아무것도 모르고 하면 항상 가득채워서? 검색을 하게 된다.
    ex) 제목이 title 이면 title 을 끝까지 다 입력하는 것...
    그런데 우리가 검색을 할 때는 이러지 않는다! 그러면 어떻게 하는데!!

- 해결

  - Query 에서는 Like 라는 친구가 이러한 고민을 해결해줄 수 있다.

- 방법

  - | 용도            | 사용                                          |
    | --------------- | --------------------------------------------- |
    | Like : 검색어   | `findByUsernameLike(String username)`         |
    | Like : 검색어 % | `findByUsernameStartingWith(String username)` |
    | Like : %검색어  | `findByUsernameEndingWith(String username)`   |
    | Like : %검색어% | `findByUsernameContaining(String username)`   |

  - `%` : 퍼센트 기호는 0, 1 또는 여러 문자를 나타낸다.
  - `_` : 하나의 단일 문자을 나타낸다.

- 예시

  - ```java
    List<Room> findByTopicAgreeContainingOrTopicOppositeContaining(String topicAgree, String topicOpposite);
    ```

    - `Or` 와 같은 연결시켜주는 것이라면 각각에 붙여줘야 한다.


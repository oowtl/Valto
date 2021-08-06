# Example kurento-java-helloworld



- 실행하기

  ```bash
  git clone https://github.com/Kurento/kurento-tutorial-java.git
  cd kurento-tutorial-java/kurento-hello-world
  git checkout master
  mvn -U clean spring-boot:run \
      -Dspring-boot.run.jvmArguments="-Dkms.url=ws://{KMS_HOST}:8888/kurento"
  ```

  - ```bash
     mvn -U clean spring-boot:run \
    >     -Dspring-boot.run.jvmArguments="-Dkms.url=ws://52.78.54.210:8888/kurento"
    
    ```

    - 내가 사용하는 것
    - EC2의 퍼블릭 ip로 간다.

  - **주의할점**

    - `{KMS_HOST}` 이 부분을 설정해줘야 한다.
      반드시 설정해주자. 만약에 설정을 하지 않았다면

      ```bash
      [INFO] ------------------------------------------------------------------------
      [INFO] BUILD FAILURE
      [INFO] ------------------------------------------------------------------------
      [INFO] Total time:  9.364 s
      [INFO] Finished at: 2021-08-03T16:21:03+09:00
      [INFO] ------------------------------------------------------------------------
      [ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:2.3.5.RELEASE:run (default-cli) on project kurento-hello-world: Application finished with exit code: 1 -> [Help 1]
      [ERROR]
      [ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
      [ERROR] Re-run Maven using the -X switch to enable full debug logging.
      [ERROR]
      [ERROR] For more information about the errors and possible solutions, please read the following articles:
      [ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
      ```

      이런 에러가 발생할 것이다.





# Code 분석





## Application

```java
@SpringBootApplication
@EnableWebSocket
public class Application implements WebSocketConfigurer
{
  @Bean
  public Handler handler()
  {
    return new Handler();
  }

  @Bean
  public KurentoClient kurentoClient()
  {
    return KurentoClient.create();
  }

  @Bean
  public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
    ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
    container.setMaxTextMessageBufferSize(32768);
    return container;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
  {
    registry.addHandler(handler(), "/helloworld");
  }

  public static void main(String[] args) throws Exception
  {
    SpringApplication.run(Application.class, args);
  }
}
```

- `@EnableWebSocket
  public class Application implements WebSocketConfigurer`
  - websocket 을 사용하기 위해서 나온 것

- ```java
   @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
      registry.addHandler(handler(), "/helloworld");
    }
  ```

  - WebSocketHandler 를 추가한다.

- ```java
  public KurentoClient kurentoClient()  {
      return KurentoClient.create();
  }
  ```

  - `KurentoClient` class
    - 미디어서버에 미디어 파이프라인을 생성한다.

- ```java
  @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
      ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
      container.setMaxTextMessageBufferSize(32768);
      return container;
    }
  ```

  - `ServletServerContainerFactoryBean`
    - Server Container 로 알려진 javax.servlet.ServletContext 속성 아래에 접근할 수 잇는 인스턴스가 하나라서 이것을 선언하고 setter 를 사용하는 것으로 ServerContainer 를 구성할 수 있다.
    - configuration property 설정
      - message buffer size 나 idle, timeout 등의 runtime 특성을 조절할 수 있다.
    - 선언할 수 있는 것
      1. `setMaxBinaryMessageBufferSize( [Integer] bufferSize )`
      2. `setMaxSessionIdleTimeout( [Long] timeoutlnMillis )`
      3. `setMaxTextMessageBufferSize( [Integer] bufferSize )`


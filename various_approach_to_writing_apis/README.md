# Various Approach To Writing APIs

## GET API 만들기 

GET API는 웹 애플리케이션 서버에서 값을 가져올 때 사용하는 API 

~~~
@RestController 
@RequestMapping("/api/v1/get-api") 
public class GetController {
    
}
~~~

- 클래스 수준에서 @RequestMapping을 설정하면 내부에 선언한 메서드의 URL 리소스 앞에 @RequestMapping의 값이 공통 값으로 추가됨 

### @RequestMapping으로 구현하기 

- @RequestMapping 어노테이션을 별다른 설정 없이 선언하면 HTTP의 모든 요청을 받는다. 
- 그러나, GET 형식의 요청만 받기 위해서는 어노테이션에 별도 설정이 필요하다. 

~~~
@RestController 
@RequestMapping("/api/v1/get-api") 
public class GetController {
    // http://localhost:8080/api/v1/get-api/hello 
    @RequestMapping(value = "/hello", method = RequestMethod.GET) 
    public String getHello() {
        return "Hello World";
    }
}
~~~

- @RequestMapping 어노테이션의 method 요소 값을 RequestMethod.GET으로 설정하면 요청 형식을 GET으로만 설정 가능 

스프링 4.3 버전 이후로는 새로 나온 어노테이션을 사용하기 때문에 @RequestMapping 어노테이션은 더 이상 사용하지 않음. (특별히 사용해야 하는 내용이 아닐 경우)
- @GetMapping 
- @PostMapping 
- @PutMapping 
- @DeleteMapping 

### 매개변수가 없는 GET 메서드 구현 

~~~
@GetMapping(value = "/name")
public String getName() {
    return "Flature";
}
~~~

### @PathVariable을 활용한 GET 메서드 구현 

실무 환경에서는 매개변수를 받지 않는 메서드가 거의 쓰이지 않는다. 웹 통신의 기본 목적은 데이터를 주고 받기 위함이기 때문에, 대부분 매개변수를 받는 매서드를 작성한다. 
- 매개변수를 받을 때 자주 쓰이는 방법 중 하나는 URL 자체에 값을 담아 요청하는 것 

~~~
@GetMapping(value = "/variable1/{variable}") 
public String getVariable1(@PathVariable String variable) {
    return variable;
}
~~~

- {}로 표시된 위치의 값을 받아 요청한다. 
- 값을 간단히 전달할 때 주로 사용하는 방법이며, GET 요청에서 많이 사용된다. 
- 이런 방식으로 코드를 작성할 때는 몇 가지 지켜야 할 규칙이 있다. @GetMapping 어노테이션의 값으로 URL을 입력할 때 중괄호를 사용해 어느 위치에서 값을 받을 지 지정해야 한다. 
- 또한, 매개변수와 그 값을 연결하기 위해 3번 줄과 같이 @PathVariable을 명시하며, **@GetMapping 어노테이션과 @PathVariable에 지정된 변수의 이름을 동일**하게 맞춰야 한다. 

> 만약 @GetMapping 어노테이션에 지정한 변수의 이름과 메서드 매개변수의 이름을 동일하게 맞추기 어렵다면? 
> 
> @PathVariable 뒤에 괄호를 열어 @GetMapping 어노테이션의 변수명을 지정한다. 
> ~~~
> @GetMapping(value = "/variable2/{variable}") 
> public String getVariable2(@PathVariable("variable") String var) {
>   return var;
> }
> ~~~

### @RequestParam을 활용한 GET 메서드 구현 

GET 요청을 구현할 때 앞에서 살펴본 방법처럼 URL 경로에 값을 담아 요청을 보내는 방법 외에도 쿼리 형식으로 값을 전달할 수 있다. 즉 URL에서 ?를 기준으로 우측에 {키}={값} 형태로 구성된 요청을 전송하는 방법이다. 
- @RequestParam 어노테이션을 명시해 쿼리 값과 매핑하면 된다. 

~~~
@GetMapping(value = "/request1") 
public String getRequestParam1(
    @RequestParam String name, 
    @RequestParam String email, 
    @RequestParam String organization
) {
    return name + " " + email + " " + organization;  
}
~~~

만약, 쿼리스트링에 어떤 값이 들어올지 모른다면 Map 객체를 활용할 수 있다. 

~~~
@GetMapping(value = "/request2") 
public String getRequestParam2(@RequestParam Map<String, String> param) {
    StringBuilder sb = new StringBuilder();
    
    param.entrySet().forEach(map -> {
        sb.append(map.getKey() + " : " + map.getValue() + "\n");
    });
    
    return sb.toString(); 
}
~~~

- 위의 코드로 작성하면 값에 상관없이 요청을 받을 수 있다. 

> **URL과 URI의 차이** 
> 
> URL은 우리가 흔히 말하는 웹 주소를 의미, 리소스가 어디에 있는지 알려주기 위한 경로이다. 반면 URI는 특정 리소스를 식별할 수 있는 식별자를 의미한다. 
> 
> 웹에서는 URL을 통해 리소스가 어느 서버에 위치해 있는지 알 수 있으며, 그 서버에 접근해서 리소스에 접근하기 위해서는 대부분 URI가 필요하다. 

### DTO 객체를 활용한 GET 메서드 구현 

DTO는 Data Transfer Object의 약자로, 다른 레이어 간의 데이터 교환에 활용된다. 간략하게 설명하자면 각 클래스 및 인터페이스를 호출하면서 전달하는 매개변수로 사용되는 데이터 객체이다. 

DTO는 데이터를 교환하는 용도로만 사용하는 객체이기 때문에 DTO에는 별도의 로직이 포함되지 않는다. 

> DTO와 VO 
> 
> DTO와 VO의 역할을 서로 엄밀하게 구분하지 않고 사용할 때가 많다. 대부분 문제가 발생하지 않지만, 정확하게 구분하자면 역할과 사용법에서 차이가 있다. 
> 
> 먼저 VO는 데이터 그 자체로 의미가 있는 객체를 의미한다. VO의 가장 특징적인 부분은 읽기 전용 (Read-Only)로 설계한다는 점이다. VO는 값을 변경할 수 없게 만들어 데이터의 신뢰성을 유지해야 한다. 
> 
> DTO는 데이터 전송을 위해 사용되는 데이터 컨테이너이다. 같은 애플리케이션 내부에서 사용되는 것이 아니라 다른 서버(시스템)로 전달하는 경우에만 사용한다. 
> 
> DTO가 다른 레이어 간에 데이터 교환에 활용된다고 설명했다. 여기서 레이어는 애플리케이션 내부에 정의된 레이어일 수도 있고 인프라 관점에서 서버 아키텍처 상의 레이어일 수도 있다. 이러한 개념의 혼용이 DTO와 VO의 차이를 흐리게 만들기 때문에 팀 내부적으로 용어나 개념의 역할 범위를 설정하고 합의해서 사용하는 것이 좋다. 

~~~
public class MemberDto {
    private String name;
    private String email;
    private String organization; 
    
    public String getName() {
        return name; 
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email; 
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOrganization() {
        return organization; 
    }
    
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    @Override
    public String toString() {
        return name + " " + email + " " + organization;
    }
}
~~~

- DTO 클래스에는 전달하고자 하는 필드 객체를 선언하고 getter/setter 메서드를 구현한다. 
- DTO 클래스에 선언된 필드는 컨트롤러의 메서드에서 쿼리 파라미터의 키와 매핑된다. 즉, 쿼리스트링의 키가 정해져 있지만 받아야 할 파라미터가 많을 경우에는 DTO 객체를 활용해 코드의 가독성을 높일 수 있다. 

~~~
@GetMapping(value = "/request3") 
public String getRequestParam3(MemberDto memberDto) {
    return memberDto.toString();
}
~~~

## POST API 만들기 

POST API는 웹 애플리케이션을 통해 데이터베이스 등의 저장소에 리소스를 저장할 때 사용하는 API이다. 
- POST API에서는 저장하고자 하는 리소스나 값을 HTTP body에 담아 서버에 전달한다. 
- 그래서 URI가 GET API에 비해 간단하다. 

### @RequestMapping으로 구현하기 

POST API에서 @RequestMapping을 사용하는 방법은 GET API와 크게 다르지 않다. 
- 요청 처리 메서드를 정의할 때 method = RequestMethod.POST로 설정하는 부분을 제외하면 GET API와 동일하다. 

~~~
@RequestMapping(value = "/domain", method = RequestMethod.POST) 
public String postExample() {
    return "Hello Post API";
}
~~~

### @RequestBody를 활용한 POST 메서드 구현 

일반적으로 POST 형식의 요청은 클라이언트가 서버에 리소스를 저장하는데 사용한다. 그러므로 클라이언트의 요청 트래픽에 값이 포함되어 있다. 
- POST 요청에서는 리소스를 담기 위해 HTTP Body에 값을 넣어 전송한다. 
- Body 영역에 작성되는 값은 일정한 형태를 취한다. 일반적으로 JSON(JavaScript Object Notation) 형식으로 전송된다. 

~~~
@PostMapping(value = "/member") 
public String postMember(@RequestBody Map<String, Object> postData) {
    StringBuilder sb = new StringBuilder(); 
    
    postData.entrySet().foreach(map -> {
        sb.append(map.getKey() + " : " + map.getValue() + "\n");
    });
    
    return sb.toString();
}
~~~

- @RequestBody 어노테이션은 HTTP의 Body 내용을 해당 어노테이션이 지정된 객체에 매핑하는 역할을 한다. 

> JSON이란?
> 
> 자바스크립트의 객체 문법을 따르는 문자 기반의 데이터 포맷이다. 현재는 자바스크립트 외에도 다수의 프로그래밍 환경에서 사용한다. 대체로 네트워크를 통해 데이터를 전달할 때 사용하며, 문자열 형태로 작성되기 때문에 파싱하기도 쉽다는 장점이 있다. 

~~~
@PostMapping(value = "/member2") 
public String postMember(@RequestBody MemberDto memberDto) {
    return memberDto.toString();
}
~~~

- 요청 메시지에 들어갈 값이 정해져 있다면, DTO 객체를 매개변수 삼아 작성할 수 있다.
- MemberDto의 멤버 변수를 요청 메시지의 키와 매핑해 값을 가져온다. 

## PUT API 만들기 

PUT API는 웹 애플리케이션 서버를 통해 데이터베이스 같은 저장소에 존재하는 리소스 값을 업데이트 하는 데 사용한다. 
- POST API와 비교하면 요청을 받아 실제 데이터베이스에 반영하는 과정에서 차이가 있지만 컨트롤러 클래스를 구현하는 방법은 POST API와 거의 동일하다. 
  - 리소스를 서버에 전달하기 위해 HTTP Body를 활용해야 하기 때문이다. 

### @RequestBody를 활용한 PUT 메서드 구현 

PUT API는 POST 메서드와 마찬가지로 값을 HTTP Body에 담아 전달한다. 

~~~
@PutMapping(value = "/member") 
public String putMember(@RequestBody Map<String, Object> putData) {
    StringBuilder sb = new StringBuilder();
    
    putData.entrySet().forEach(map -> {
        sb.append(map.getKey() + " : " + map.getValue() + "\n");
    });
    
    return sb.toString(); 
}
~~~

- 서버에 어떤 값이 들어올지 모르는 경우에는 Map 객체를 활용해 값을 받을 수 있다.
- 대부분의 경우 API를 개발한 쪽에서 작성한 명세(specification)를 웹 사이트를 통해 클라이언트나 사용자에게 올바른 방법을 안내한다. 
- 만약 서버에 들어오는 요청에 담겨 있는 값이 정해져 있는 경우라면 DTO 객체 활용하기 

~~~
@PutMapping(value = "/member1") 
public String postMemberDto1(@RequestBody MemberDto memberDto) {
    return memberDto.toString(); 
}

@PutMapping(value = "/member2") 
public String postMemberDto2(@RequestBody MemberDto memberDto) {
    return memberDto; 
}
~~~

- 첫번째 코드는 String을 반환 : Content-type이 text/plain으로 전달된다.
- 두번째 코드는 DTO를 반환 : Content-type이 application/json으로 전달된다. 

> @RestController 어노테이션이 지정된 클래스는 @ResponseBody를 생략할 수 있는데, @ResponseBody 어노테이션은 자동으로 값을 JSON과 같은 형식으로 변환해서 전달하는 역할을 수행 

### ResponseEntity를 활용한 PUT 메서드 구현 

스프링 프레임워크에서는 HttpEntity라는 클래스가 있다. HttpEntity는 헤더와 Body로 구성된 HTTP 요청과 응답을 구성하는 역할을 수행한다. 
- RequestEntity와 ResponseEntity는 HttpEntity를 상속받아 구현한 클래스이다. 
  - 그 중 ResponseEntity는 서버에 들어온 요청에 대해 응답 데이터를 구성해서 전달할 수 있게 한다. 

~~~
public class ResponseEntity<T> extends HttpEntity<T> {
    private final Object status; 
    ... 
}
~~~

- ResponseEntity는 HttpEntity로부터 HttpHeaders와 Body를 가지고 자체적으로 HttpStatus를 구현한다. 
- 이 클래스를 활용하면 응답 코드 변경은 물론 Header와 Body를 더욱 쉽게 구성할 수 있다. 

~~~
@PutMapping(value = "/member3") 
public ResponseEntity<MemberDto> postMemberDto3(@RequestBody MemberDto memberDto) {
    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(memberDto);
}
~~~

## DELETE API 만들기 

DELETE API는 웹 애플리케이션 서버를 거쳐 데이터베이스 등의 저장소에 있는 리소스를 삭제할 때 사용한다. 
- 서버에서는 클라이언트로부터 리소스를 식별할 수 있는 값을 받아 데이터베이스나 캐시에 있는 리소스를 조회하고 삭제하는 역할을 수행한다. 
- 컨트롤러를 통해 값을 받는 단계에서는 간단한 값을 받기 때문에 GET 메서드와 같이 URI에 값을 넣어 요청을 받는 형식으로 구현된다. 

### @PathVariable과 @RequestParam을 활용한 DELETE 메서드 구현 

~~~
@DeleteMapping(value = "/{variable}") 
public String DeleteVariable(@PathVariable String variable) {
    return variable; 
}
~~~

- @DeleteMapping 어노테이션에 정의한 value의 이름과 메서드의 매개변수 이름을 동일하게 설정해야 삭제할 값이 주입된다. 

~~~
@DeleteMapping(value = "/request1") 
public String getRequestParam(@RequestParam String email) {
    return "e-mail : " + email;
}
~~~

- @RequestParam 어노테이션을 통해 쿼리스트링 값도 받을 수 있다. 

## REST API 명세를 문서화하는 방법 - Swagger 

API를 개발하면 명세를 관리해야 한다. 
- **명세란 해당 API가 어떤 로직을 수행**하는지 설명하고 **이 로직을 수행하기 위해 어떤 값을 요청**하며, **이에 따른 응답값으로는 무엇을 받을 수 있는지를 정리**한 자료이다. 
- API는 개발 과정에서 계속 변경되므로 작성한 명세 문서도 주기적인 업데이트가 필요하다. 
- 명세 작업은 번거롭고 시간 또한 오래 걸린다. 
- 이 같은 문제를 해결하기 위해 등장한 것이 바로 Swagger이다. 

Swagger를 사용하기 위해서는 의존성을 추가해야 한다. 그리고 설정 코드를 작성해야 한다. 
- 이 클래스는 설정(Configuration)에 관한 클래스이므로 config 패키지 안에 생성하는 것이 좋다. 

~~~
@Configuration 
@EnableSwagger2 
public class SwaggerConfiguration {
    @Bean 
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) 
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.springboot.api")) 
            .paths(PathSelectors.any())
            .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Spring Boot API Test with Swagger")
            .description("설명 부분")
            .version("1.0.0")
            .build();
    }
}
~~~

이제 기존 코드에 Swagger 명세를 추가해주자. 

~~~
@ApiOperation(value = "GET 메서드 예제", notes = "@RequestParam을 활용한 GET Method") 
@GetMapping(value = "/request1") 
public String getRequestParam(
    @ApiParam(value="이름", required=true) @RequestParam String name, 
    @ApiParam(value="이메일", required=true) @RequestParam String email, 
    @ApiParam(value="회사", required=true) @RequestParam String organization
) {
    return name + " " + email + " " + organization;
}
~~~

- @ApiOperation : 대상 API의 설명을 작성하기 위한 어노테이션 
- @ApiParam: 매개변수에 대한 설명 및 설정을 위한 어노테이션. 메서드의 매개변수뿐 아니라 DTO 객체를 매개변수로 사용할 경우 DTO 클래스 내의 매개변수에도 정의 가능 

> Swagger에서는 API 명세 관리뿐 아니라 직접 통신도 시도 가능 

## 로깅 라이브러리 - Logback 

로깅(logging) : 애플리케이션이 동작하는 동안 시스템의 상태나 동작 정보를 시간순으로 기록하는 것 
- 개발 영역 중 비기능 요구사항에 속한다. (사용자나 고객에게 필요한 기능은 아니라는 것)
- 하지만, 로깅은 디버깅하거나 개발 이후 발생한 문제를 해결할 때 원인을 분석하는 데 꼭 필요한 요소이다. 

자바 진영에서 많이 사용하는 것은 Logback이다. 
- Logback은 log4j 이후에 출시된 로깅 프레임워크로서 slf4j를 기반으로 구현됐으며, 과거에 사용된 log4j에 비해 월등한 성능을 지닌다. 
- 스프링 부트 spring-boot-starter-web 라이브러리 내부에 내장되어 있어 별도의 의존성을 추가하지 않아도 사용 가능하다. 

### Logback 특징 

- 크게 5개의 로그 레벌(TRACE, DEBUG, INFO, WARN, ERROR)을 설정할 수 있다. 
  - ERROR : 로직 수행 중에 시스템에 심각한 문제가 발생해서 애플리케이션의 작동이 불가능한 경우를 의미 
  - WARN : 시스템 에러의 원인이 될 수 있는 경고 레벨을 의미 
  - INFO : 애플리케이션의 상태 변경과 같은 정보 전달을 위해 사용 
  - DEBUG : 애플리케이션의 디버깅을 위한 메시지를 표시하는 레벨을 의미 
  - TRACE : DEBUG 레벨보다 더 상세한 메시지를 표현하기 위한 레벨을 의미 
- 실제 운영 환경과 개발 환경에서 각각 다른 출력 레벨을 설정해서 로그 확인 가능 
- Logback의 설정 파일을 일정 시간마다 스캔해서 애플리케이션을 재가동하지 않아도 설정 변경 가능 
- 별도의 프로그램 지원 없이도 자체적으로 로그 파일을 압축 가능 
- 저장된 로그 파일에 대한 보관 기간 등을 설정해서 관리 가능 

### Logback 설정 

일반적으로 classpath에 있는 설정 파일을 자동으로 참조하므로 Logback 설정 파일은 resource 폴더 안에 생성한다. 
- 스프링 - logback.xml, 스프링부트 - logback-spring.xml

logback 설정에서 가장 중요한 영역은 Appender와 Root 영역이다. 

#### Appender 

로그의 형태를 설정하고 어떤 방법으로 출력할지를 설정하는 곳이다. Appender 자체는 하나의 인터페이스를 의미하며, 하위에 여러 구현체가 존재한다. 
- Logback의 설정 파일을 이용하면 각 구현체를 등록해서 로그를 원하는 형식으로 출력 가능하다. 

Appender의 대표적인 구현체 
- ConsoleAppender : 콘솔에 로그를 출력 
- FileAppender : 파일에 로그를 저장 
- RollingFileAppender : 여러 개의 파일을 순회하면서 로그를 저장 
- SMTPAppender : 메일로 로그를 전송 
- DBAppender : 데이터베이스에 로그를 저장 

#### Root 

설정 파일에 정의된 Appender를 활용하려면 Root 영역에서 Appender를 참조해서 로깅 레벨을 설정한다. 
- 만약 특정 패키지에 대해 다른 로깅 레벨을 설정하고 싶다면 root 대신 logger를 사용해 지정 가능 

### Logback 적용하기 

Logback은 출력할 메시지를 Appender에게 전달할 Logger 객체를 각 클래스에 정의해서 사용한다. 

~~~
@RestController 
@RequestMapping("/api/v1/get-api") 
public class GetController {
    private final Logger LOGGER = LoggerFactory.getLogger(GetController.class);
    
    ...
}
~~~

- Logger 선언 
- Logger는 LoggerFactory를 통해 객체를 생성, 이때 클래스의 이름을 함께 지정해서 클래스의 정보를 Logger에서 가져가게 된다. 

~~~
@RequestMapping(value = "/hello", method = RequestMethod.GET) 
public String getHello() {
    LOGGER.info("getHelo 메서드가 호출되었습니다.");
    return "Hello World";
}

@GetMapping(value = "/name") 
public String getName() {
    LOGGER.info("getName 메서드가 호출되었습니다.");
    return "Flature";
}
~~~

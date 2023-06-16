package dbwls.spring_mvc.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        response.getWriter().write("ok");
    }

    /**
     * spring 에서 제공하는 inputStream, Writer 를 사용한 버전으로 servlet 을 사용할 필요가 없을 때
     * HttpServletRequest, HttpServletResponse 대신 사용할 수 있음
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        responseWriter.write("ok");
    }

    /**
     * 스프링에서 제공하는 HTTP header, body 정보를 쉽게 조회하도록 도와주는 HttpEntity 객체 이용
     * 메세지 바디를 직접 조회, 직접 반환, 헤더 정보 포함 등 각종 기능 제공, 하지만 View 조회는 X
     * 요청 파라미터는 GET 으로 query parameter 오는 것, POST 로 HTML의 Form Data를 전송할 때 만 사용
     * 위 두 경우를 제외한 나머지는 모두 HttpEntity 등을 사용해서 데이터를 직접 조회해야 함
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {

        String body = httpEntity.getBody();
        log.info("messageBody = {}", body);

        return new HttpEntity<>("ok");
    }

    /**
     * HttpEntity 를 상속받아 더 다양한 기능을 제공하는 RequestEntity, ResponseEntity 객체 사용 가능
     * request, response 에 맞는 다양한 기능들을 추가로 제공
     */
    @PostMapping("/request-body-string-v3-2")
    public ResponseEntity<String> requestBodyStringV3_2(RequestEntity<String> requestEntity) {

        String body = requestEntity.getBody();
        log.info("messageBody = {}", body);

        return new ResponseEntity<String>("ok", HttpStatus.CREATED);
    }

    // 스프링은 HTTP 메세지 바디를 변환하여 전달할 때 HttpMessageConverter 기능 사용
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {

        log.info("messageBody = {}", messageBody);

        return "ok";
    }
}

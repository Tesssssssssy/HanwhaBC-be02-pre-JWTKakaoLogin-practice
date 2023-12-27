package com.example.jwtpractice231222.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {
    @Value("${kakao.api-key}")
    private String apiKey;

    public String getKakaoToken(String code) {
        HttpHeaders headers2 = new HttpHeaders();

        // postman에서 설정한 걸 java code로 설정하겠다.
        headers2.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");     // kakao 공식 문서 가이드 지침
        params.add("client_id", apiKey);                    // 내 애플리케이션 - REST API key
        params.add("redirect_uri", "http://localhost:8080/member/kakao");
        params.add("code", code);                               // 인가 코드

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers2);
        RestTemplate restTemplate2 = new RestTemplate();
        /*
         <RestTemplate>
         · Spring 3.0 부터 지원하는 Spring의 HTTP 통신 템플릿
         · HTTP 요청 후 JSON, XML, String 과 같은 응답을 받을 수 있는 템플릿
         · Blocking I/O 기반의 동기방식을 사용하는 템플릿
         · RESTful 형식에 맞추어진 템플릿
         · Header, Content-Tpye등을 설정하여 외부 API 호출
         · Server to Server 통신에 사용

        - HTTP 통신을 위한 도구로 RESTful API 웹 서비스와의 상호작용을 쉽게 외부 도메인에서 데이터를 가져오거나 전송할 때
          사용되는 스프링 프레임워크의 클래스를 의미합니다.

        - 다양한 HTTP 메서드(GET, POST, PUT, DELETE 등)를 사용하며 원격 서버와 ‘동기식 방식’으로
          JSON, XML 등의 다양한 데이터 형식으로 통신합니다.

        - 동기식 방식으로 요청을 보내고 응답을 받을 때까지 블로킹되며, 요청과 응답이 완료되기 전까지 다음 코드로 진행되지 않는다.
          원격 서버와 통신할 때는 응답을 기다리는 동안 대기해야 한다.
        */


        ResponseEntity<Object> response = restTemplate2.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                Object.class
        );
        String result = "" + response;
        String accessToken = result.split(",")[1].split("=")[1];

        return accessToken;
    }

    public String getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Object> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Object.class
        );
        System.out.println(response);

        String result = "" + response.getBody();
        String userName = result.split("nickname=")[1].split("}")[0];

        return userName;
    }
}

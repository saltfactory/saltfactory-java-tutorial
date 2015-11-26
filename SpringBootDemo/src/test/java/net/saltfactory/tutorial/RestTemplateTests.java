package net.saltfactory.tutorial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static com.jcabi.matchers.XhtmlMatchers.hasXPath;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * filename : RestTemplateTests.java
 * author   : saltfactory<saltfactory@gmail.com>
 * created  : 11/25/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootDemoApplication.class)
@WebIntegrationTest("server.port=0")
public class RestTemplateTests {

    Logger logger = Logger.getLogger(this.getClass());

    @Value("${local.server.port}")
    int port;

    @Autowired
    ArticlesService articlesService;

    private String baseUrl;

    RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:" +  String.valueOf(port);
    }

    private String jsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private String getUrlString(String path) {
        return "http://localhost:" + String.valueOf(port) + path;
    }


    @Test
    public void testIndex() throws Exception {

        URI uri = URI.create(baseUrl+ "/api/articles");
//        String responseString = restTemplate.getForObject(uri, String.class);
        List<Article> resultArticles = Arrays.asList(restTemplate.getForObject(uri, Article[].class));

        // 컨트롤러 결과를 로깅
//        logger.info(responseString);

        // 컨트롤러 결과를 확인하기 위한 데이터 가져오기
        List<Article> articles = articlesService.getArticles();
//        String jsonString = jsonStringFromObject(articles);

        // 컨트롤러의 결과와 JSON 문자열로 비교
//        assertThat(responseString, is(equalTo(jsonString)));
        assertThat(resultArticles.size(), is(equalTo(articles.size())));
        assertThat(resultArticles.get(0).getId(), is(equalTo(articles.get(0).getId())));
    }

    @Test
    public void testShow() throws Exception {

        long id = 1;
        Article article = articlesService.getArticle(id);

        URI uri = URI.create(baseUrl + "/api/articles/" +id);
        Article _article = restTemplate.getForObject(uri, Article.class);

        assertThat(_article.getId(), is(article.getId()));

//        String jsonString = this.jsonStringFromObject(article);
//        assertThat(responseString, is(equalTo(jsonString)));
//        logger.info(responseString);
    }


    @Test
    public void testCreate() throws Exception {

        URI uri = URI.create(baseUrl + "/api/articles");

        Article article = new Article();
        article.setTitle("testing create article");
        article.setContent("test content");

        Comment comment = new Comment();
        comment.setContent("test comment1");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        article.setComments(comments);

        Article resultArticle = restTemplate.postForObject(uri, article, Article.class);

        assertThat(resultArticle.getTitle(), is(equalTo(article.getTitle())));


//        String responseString = restTemplate.postForObject(uri, article, String.class);
//        String jsonString = jsonStringFromObject(article);
//
//        assertThat(responseString, is(equalTo(jsonString)));
//        logger.info(responseString);
    }

    @Test
    public void testDelete() throws Exception {

        long id = 1;
        URI uri = URI.create(baseUrl + "/api/articles/" + id);

//        Article article = articlesService.getArticle(id);
//        restTemplate.delete(uri);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

        String jsonString = jsonStringFromObject(articlesService.deleteArticle(id));

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(equalTo(jsonString)));

        logger.info(responseEntity.getBody());
    }

    @Test
    public void testPut() throws Exception {
        long id = 1;

        URI uri = URI.create(baseUrl + "/api/articles/" +id);

        Article article = articlesService.getArticle(id);
        article.setTitle("testing create article");
        article.setContent("test content");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Article> entity = new HttpEntity(article, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);

        String jsonString = jsonStringFromObject(article);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(equalTo(jsonString)));
    }

    @Test
    public void testPatch() throws Exception {
        long id = 1;

        URI uri = URI.create(baseUrl + "/api/articles/" +id);

        Article article = articlesService.getArticle(id);
        article.setTitle("testing create article");
        article.setContent("test content");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Article> entity = new HttpEntity(article, headers);

        ClientHttpRequestFactory httpRequestFactory =  new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(httpRequestFactory);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PATCH, entity, String.class);

        String jsonString = jsonStringFromObject(article);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(equalTo(jsonString)));
    }

    @Test
    public void testNewArticle() throws Exception {
        URI uri = URI.create(baseUrl + "/articles/new");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        HttpEntity<String> entity = new HttpEntity<>(headers);


//        String responseString = restTemplate.getForObject(uri), String.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(responseEntity.getHeaders().getContentType(), is(equalTo(mediaType)));
        assertThat(responseEntity.getBody(), hasXPath("//input[@name='title']"));

        logger.info(responseEntity.getBody());
    }

    @Test
    public void testSubmit() throws Exception {

        URI uri = URI.create(baseUrl + "/articles");

        Article article = new Article();
        article.setTitle("testing create article");
        article.setContent("test content");

        Comment comment = new Comment();
        comment.setContent("test comment1");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        article.setComments(comments);

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        article.setFile(file);

//        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.add("title", article.getTitle());
//        multiValueMap.add("content", article.getContent());
//        multiValueMap.add("comments[0].content", article.getComments().get(0).getContent());
//
//        ByteArrayResource resource = new ByteArrayResource(article.getFile().getBytes()){
//            @Override
//            public String getFilename() throws IllegalStateException {
//                return article.getFile().getOriginalFilename();
//            }
//        };
//        multiValueMap.add("file", resource);
//
////        String responseString = restTemplate.postForObject(uri, article, String.class);

        MultiValueMap<String, Object> multiValueMap = new MultiValueMapConverter(article).convert();

        String responseString = restTemplate.postForObject(uri, multiValueMap, String.class);
        String jsonString = jsonStringFromObject(article);

        assertThat(responseString, is(equalTo(jsonString)));
        
    }



}
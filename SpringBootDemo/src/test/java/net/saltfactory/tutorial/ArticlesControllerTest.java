package net.saltfactory.tutorial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by saltfactory<saltfactory@gmail.com> on 11/21/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootDemoApplication.class)
public class ArticlesControllerTest {

    Logger logger = Logger.getLogger(this.getClass());

    private MockMvc mockMvc;

    @Autowired
    private ArticlesController articlesController;

    @Autowired
    private ArticlesService articlesService;

    @Before
    public void setUp() throws Exception {
        mockMvc = standaloneSetup(articlesController).build();
    }

    private String jsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Test
    public void testIndex() throws Exception {
        List<Article> articles = articlesService.getArticles();
        String jsonString = this.jsonStringFromObject(articles);

        MvcResult result = mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(equalTo(jsonString)))
                .andReturn();

        logger.info(result.getResponse().getContentAsString());

    }

    @Test
    public void testShow() throws Exception {
        long id = 1;
        Article article = articlesService.getArticle(id);
        String jsonString = this.jsonStringFromObject(article);

        MvcResult result = mockMvc.perform(get("/api/articles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(equalTo(jsonString))).andReturn();
        logger.info(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreate() throws Exception {
        Article article = new Article();
        article.setTitle("testing create article");
        article.setContent("test content");

        Comment comment = new Comment();
        comment.setContent("test comment1");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        article.setComments(comments);

        String jsonString = this.jsonStringFromObject(article);

        MvcResult result = mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(jsonString))).andReturn();

        logger.info(result.getResponse().getContentAsString());
    }

    @Test
    public void testPatch() throws Exception {
        long id = 1;
        Article article = articlesService.getArticle(id);
        article.setTitle("testing create article");
        article.setContent("test content");

        String jsonString = this.jsonStringFromObject(article);

        MvcResult result = mockMvc.perform(patch("/api/articles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(jsonString))).andReturn();

        logger.info(result.getResponse().getContentAsString());
    }

    @Test
    public void testUpdate() throws Exception {
        long id = 1;
        Article article = articlesService.getArticle(id);
        article.setTitle("testing create article");
        article.setContent("test content");

        String jsonString = this.jsonStringFromObject(article);

        MvcResult result = mockMvc.perform(put("/api/articles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(jsonString))).andReturn();

        logger.info(result.getResponse().getContentAsString());
    }


    @Test
    public void testDestroy() throws Exception {
        long id = 1;
        List<Article> articles = articlesService.deleteArticle(id);
        String jsonString = this.jsonStringFromObject(articles);

        mockMvc.perform(delete("/api/articles/{id}", id)
                .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(jsonString)));
    }



}
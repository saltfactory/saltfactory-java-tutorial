package net.saltfactory.tutorial;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by saltfactory<saltfactory@gmail.com> on 11/21/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootDemoApplication.class)
public class ArticlesServiceTests {

    @Autowired
    ArticlesService articlesService;

    @Autowired
    FixturesProperty fixturesProperty;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetArticles() throws Exception {
        List<Article> articles = articlesService.getArticles();
        assertThat(articles, is(notNullValue()));
        assertThat(articles.size(), is(3));
    }

    @Test
    public void testGetArticle() throws Exception{
        long id = 1;
        Article article = articlesService.getArticle(id);

        List<Article> articles = fixturesProperty.getArticles();
        Article demoArticle = articles.stream()
                .filter(a -> a.getId() == id)
                .collect(Collectors.toList()).get(0);

        assertThat(article.getId(), is(equalTo(demoArticle.getId())));
    }

    @Test
    public void testDeleteArticle() throws Exception {
        long id = 1;
        List<Article> demoArticles = new ArrayList<>(fixturesProperty.getArticles());
        List<Article> articles = articlesService.deleteArticle(id);
        assertThat(articles.size(), not(demoArticles.size()));
    }
}
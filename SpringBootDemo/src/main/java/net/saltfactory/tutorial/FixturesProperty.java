package net.saltfactory.tutorial;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saltfactory<saltfactory@gmail.com> on 11/21/15.
 */
@Component
@ConfigurationProperties(locations = {"fixtures.yml"}, prefix = "fixtures")
public class FixturesProperty {
    @NestedConfigurationProperty
    private List<Article> articles = new ArrayList<>();

    public List<Article> getArticles() {
        return articles;
    }

}

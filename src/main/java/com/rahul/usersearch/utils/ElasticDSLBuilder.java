package com.rahul.usersearch.utils;

import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
public class ElasticDSLBuilder {

    /**
     * This method converts domain search request to elastic dsl.
     * Also handles pagination for search results.
     *
     * @return
     */
    public static SearchSourceBuilder createDSLQuery() throws IOException {

        final String query = "{\n" +
                "    \"wildcard\": {\n" +
                "      \"username\": {\n" +
                "        \"value\": \"*a*\"\n" +
                "      }\n" +
                "    }\n" +
                "  }";


        final SearchSourceBuilder qBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.wrapperQuery(query))
                .size(getRandomNumberUsingNextInt(3000, 9000));

        return qBuilder;

    }

    public static String fullDSL() throws IOException {

        final String query = "{\n" +
                "  \"size\": 200,\n" +
                "  \"query\": {\n" +
                "    \"match_all\": {}\n" +
                "  },\n" +
                "  \"collapse\": {\n" +
                "    \"field\": \"address.city.keyword\"\n" +
                "  }\n" +
                "}";
        return query;

    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}

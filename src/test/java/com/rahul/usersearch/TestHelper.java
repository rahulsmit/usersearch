package com.rahul.usersearch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.usersearch.model.user.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import org.elasticsearch.search.SearchHit;

public class TestHelper {

  public static List<User> fetchMockUsers(){
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    try (InputStream inputStream = TestHelper.class.getClassLoader().getResourceAsStream(
        "mock_data/usersearch_data.json")) {
      return objectMapper.readValue(inputStream, new TypeReference<List<User>>() { });
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public static SearchHit fetchMockSearchHit(){
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    try (InputStream inputStream = TestHelper.class.getClassLoader().getResourceAsStream(
        "mock_data/search_hit.json")) {
      Map<String, Object> temp =  objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() { });
      return SearchHit.createFromMap(temp);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
}

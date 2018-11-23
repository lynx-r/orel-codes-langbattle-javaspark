package com.workingbit.langbattle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.lang.String.format;
import static spark.Spark.*;

/**
 * User: aleksey
 * Date: 23/11/2018
 * Time: 06:45
 */
public class LangBattleApplication {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

  public static void main(String[] args) {
    after((req, resp) -> {
      resp.header("Content-Type", "application/json");
    });

    post("/", (req, res) -> {
      Map map = objectMapper.readValue(req.body(), Map.class);
      var firstName = (String) map.get("first_name");
      var lastName = (String) map.get("last_name");
      var currentDate = FORMATTER.format(ZonedDateTime.now());
      return objectMapper.writeValueAsString(
          Map.of(
              "id", map.get("id"),
              "first_name", format("%s %s", firstName, DigestUtils.md5Hex(firstName)),
              "last_name", format("%s %s", lastName, DigestUtils.md5Hex(lastName)),
              "current_time", currentDate,
              "say", "Java is the best"
          )
      );
    });

    System.out.println("Spark started on http://localhost:" + port());
  }
}

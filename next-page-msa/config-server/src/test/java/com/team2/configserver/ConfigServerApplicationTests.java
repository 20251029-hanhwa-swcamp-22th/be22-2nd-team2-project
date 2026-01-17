package com.team2.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.profiles.active=native",
    "spring.cloud.config.server.git.uri=",
    "spring.cloud.config.server.git.clone-on-start=false"
})
class ConfigServerApplicationTests {

  @Test
  void contextLoads() {
  }

}

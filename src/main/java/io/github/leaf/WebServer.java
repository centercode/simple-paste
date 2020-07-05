package io.github.leaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.util.Map;

@SpringBootApplication
public class WebServer {

  private static Logger logger = LoggerFactory.getLogger(WebServer.class);

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(WebServer.class, args);
    RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
    Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
    handlerMethods.keySet().forEach(it -> logger.info(it.toString()));
    logger.info("==== Boot Success ====");
  }
}
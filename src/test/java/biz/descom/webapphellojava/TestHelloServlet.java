package biz.descom.webapphellojava;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests for class HelloServlet
 * Created by remigius on 12.05.2017.
 */
public class TestHelloServlet {
  @Test
  public void getGreeting() {
    assertEquals("Hello World", HelloServlet.getGreeting(null));
    assertEquals("Hello World", HelloServlet.getGreeting(""));
    assertEquals("Hello Remi", HelloServlet.getGreeting("Remi"));
  }
}

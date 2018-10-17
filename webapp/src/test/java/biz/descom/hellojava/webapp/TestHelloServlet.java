package biz.descom.hellojava.webapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests for class HelloServlet
 * Created by remigius on 12.05.2017.
 */
public class TestHelloServlet {
  @Test
  public void getGreeting() {
    assertEquals("Howdy World", HelloServlet.getGreeting(null));
    assertEquals("Howdy World", HelloServlet.getGreeting(""));
    assertEquals("Howdy Remi", HelloServlet.getGreeting("Remi"));
  }
}

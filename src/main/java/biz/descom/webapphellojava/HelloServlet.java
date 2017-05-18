package biz.descom.webapphellojava;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * servlet that says hello
 * Created by remigius on 12.05.2017.
 */
public class HelloServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String name = request.getParameter("name");
    String greeting = getGreeting(name);
    String html = getHtml(greeting);
    response.getOutputStream().println(html);
  }

  private String getHtml(String greeting) {
    return "<!DOCTYPE html>\n" +
            "<html\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>" + greeting + "</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <h1>" + greeting + "</h1>" +
            "</body>\n" +
            "</html>";
  }

  static String getGreeting(String name) {
    return "Hello " + (name == null || name.length() == 0 ? "World" : name);
  }
}

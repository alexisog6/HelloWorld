package biz.descom.webapphellojava;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * servlet that says hello
 * Created by remigius on 12.05.2017.
 */
public class HelloServlet extends HttpServlet {
  private static Properties buildProperties = getBuildProperties();
  private static String buildTime = getProperty("build-time");
  private static String commitId = getProperty("commit-id");
  private static String host = getHost();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String servletPath = request.getServletPath();
    String html = null;
    if ("/kill".equals(servletPath)) {
      if (isDoubleO(request)) {
        html = getKillHtml("AAAAAARRRRRRGGGHHH");
        System.out.println("OMG, somebody shot me!");
        new Thread(this::kill).start();
      } else {
        html = getKillHtml("No No No");
      }
    }
    if (html == null) {
      String name = request.getParameter("name");
      String greeting = getGreeting(name);
      html = getHtml(greeting);
    }
    // caching: see https://stackoverflow.com/questions/49547/how-to-control-web-page-caching-across-all-browsers
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setHeader("Expires", "0"); // Proxies.
    response.setHeader("Connection", "close"); // close connection
    response.getOutputStream().println(html);
  }

  private boolean isDoubleO(HttpServletRequest request) {
    return "true".equals(buildProperties.getProperty("canKill"));
  }

  private String getHtml(String greeting) {
    return "<!DOCTYPE html>\n" +
            "<html\n" +
            "<head>\n" +
            "  <link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\">\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>" + greeting + "</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <h1>" + greeting + "</h1>\n" +
            "  <p>Current Time: " + getTime() + "</p>\n" +
            "  <p>Host: " + host + "</p>\n" +
            "  <p>Build Time: " + buildTime + "</p>\n" +
            "  <p>Commit ID: " + commitId + "</p>\n" +
            "  <p><a href='kill'>kill server</a></p>\n" +
            "</body>\n" +
            "</html>";
  }

  private String getKillHtml(String cry) {
    return "<!DOCTYPE html>\n" +
            "<html\n" +
            "<head>\n" +
            "  <link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\">\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>" + cry + "!!!</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <h1>" + cry + "!!!</h1>\n" +
            "  <p><a href='javascript:history.back()'>back</a></p>\n" +
            "</body>\n" +
            "</html>";
  }

  static String getGreeting(String name) {
    return "Hello " + (name == null || name.length() == 0 ? "World" : name);
  }

  private void kill() {
    try (Socket socket = new Socket("localhost", 8005)) {
      Thread.sleep(1000);
      if (socket.isConnected()) {
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        pw.println("SHUTDOWN");
        pw.close();
        System.out.println("Goodbye world.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String getTime() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  }

  private static Properties getBuildProperties() {
    Properties properties = new Properties();
    try (InputStream stream = HelloServlet.class.getResourceAsStream("/build.properties")) {
      properties.load(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

  private static String getProperty(String key) {
    String value = buildProperties == null ? "n/a" : buildProperties.getProperty(key);
    return value == null || value.length() == 0 ? "unknown" : value;
  }


  private static String getHost() {
    String value = System.getenv("HOSTNAME");
    if (value == null) {
      value = System.getenv("COMPUTERNAME");
    }
    if (value == null) {
      value = "unknown";
    }
    return value;
  }
}

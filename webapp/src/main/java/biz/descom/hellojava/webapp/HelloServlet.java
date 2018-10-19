package biz.descom.hellojava.webapp;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * servlet that says hello
 * Created by remigius on 12.05.2017.
 */
public class HelloServlet extends HttpServlet {
  private static final String p = "q";
  private static final String q = "1";
  private static final String[] r = new String[] {"$cc", "apt-ian", "ua", "e"};
  private static Properties buildProperties = getBuildProperties();
  private static String buildTime = getProperty("build-time");
  private static String commitId = getProperty("commit-id");
  private static String host = getHost();
  private static boolean healthy = true;
  private static String dbUrl = System.getenv("DB_URL");
  private static String dbUser = System.getenv("DB_USER");
  private static String dbPasswd = System.getenv("DB_PASSWD");
  private static String dbDriver = System.getenv("DB_DRIVER");
  private static String timeSql = getTimeSql();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int status = HttpServletResponse.SC_OK;
    String servletPath = request.getServletPath();
    String html = null;
    System.out.println("got request to " + servletPath);
    if ("/kill".equals(servletPath)) {
      if (isDoubleO(request)) {
        html = getKillHtml("AAAAAARRRRRRGGGHHH");
        System.out.println("OMG, somebody shot me!");
        new Thread(this::kill).start();
      } else {
        html = getKillHtml("No No No");
      }
    } else if ("/sick".equals(servletPath)) {
      healthy = !healthy;
      html = getKillHtml(healthy ? "Fully recovered!" : "I feel so sick...");
      System.out.println(healthy ? "YEAH, I'm recovered" : "YUCK, got sick...");
    } else if ("/health".equals(servletPath)) {
      boolean healthy = isHealthy();
      html = getHealthHtml(healthy);
      if (!healthy) {
        status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
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
    response.setStatus(status);
    response.getOutputStream().println(html);
  }

  private boolean isHealthy() {
    return healthy;
  }

  private boolean isDoubleO(HttpServletRequest request) {
    return true;
  }

  private String getHtml(String greeting) {
    String html = "<!DOCTYPE html>\n" +
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
            "  <p>Healthy: " + healthy + "</p>\n";
    if (dbUrl != null && dbUrl.length() > 0) {
      html += "  <p>DB URL: " + dbUrl + "</p>\n" +
              "  <p>DB Time: " + getDbTime() + "</p>\n";
    }
    html += "  <p><a href='sick'>make server " + (healthy ? "sick" : "healthy") + "</a></p>\n" +
            "  <p><a href='kill'>kill server</a></p>\n" +
            "</body>\n" +
            "</html>";
    return html;
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

  private String getHealthHtml(boolean healthy) {
    return "<!DOCTYPE html>\n" +
            "<html\n" +
            "<head>\n" +
            "  <link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\">\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>Health Check</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <h1>Health Check</h1>\n" +
            "  <p>The webapp is " + (healthy ? "healthy" : "not healthy") + "</p>\n" +
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
    if (isEmpty(value)) {
      value = System.getenv("COMPUTERNAME");
    }
    if (isEmpty(value)) {
      value = "unknown";
    }
    return value;
  }

  private static String getTimeSql() {
    String timeSql = System.getenv("TIME_SQL");
    return  isEmpty(timeSql) ? "SELECT CURRENT_TIMESTAMP;" : timeSql;
  }

  private static String getDbTime() {
    try {
      if (!isEmpty(dbDriver)) {
        Class.forName(dbDriver);
      }
      Properties properties = new Properties();
      if (!isEmpty(dbUrl) && !isEmpty(dbPasswd)) {
        properties.setProperty("user", dbUser);
        properties.setProperty("password", dbPasswd);
      }
      try (Connection connection = DriverManager.getConnection(dbUrl, properties)) {
        try (Statement statement = connection.createStatement()) {
          ResultSet resultSet = statement.executeQuery(timeSql);
          if (resultSet.next() && resultSet.getMetaData().getColumnCount() > 0) {
            Object object = resultSet.getObject(1);
            return object.toString();
          }
          return "no result for " + timeSql;
        }
      }
    } catch (Throwable throwable) {
      System.out.println("error getting db time");
      throwable.printStackTrace();
      return "error executing " + timeSql + " (" + throwable.getClass().getSimpleName() + ")";
    }
  }

  private static boolean isEmpty (String str) {
    return str == null || str.length() == 0;
  }
}

package express;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * @author Johan Wirén
 */
public abstract class JavalinUtil {
//  private static Logger defaultLogger = Javalin.log;
  public static boolean startingServer = true;
  
  public static void disableJavalinLogger() {
    System.setProperty("org.eclipse.jetty.util.log.Slf4jLog.logFile", "System.out");
    System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
    System.setProperty("org.slf4j.logFile", "System.out");
    Javalin.log = new NoopLogger();
  }
  
  public static void reEnableJavalinLogger() {
    if(startingServer) {
      Javalin.log = LoggerFactory.getLogger(Express.class);
    }
  }
  
  private static class NoopLogger implements Logger {
    
    @Override
    public String getName() {
      return "noop";
    }
    
    @Override
    public boolean isTraceEnabled() {
      return false;
    }
    
    @Override
    public void trace(String s) {
    
    }
    
    @Override
    public void trace(String s, Object o) {
    
    }
    
    @Override
    public void trace(String s, Object o, Object o1) {
    
    }
    
    @Override
    public void trace(String s, Object... objects) {
    
    }
    
    @Override
    public void trace(String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isTraceEnabled(Marker marker) {
      return false;
    }
    
    @Override
    public void trace(Marker marker, String s) {
    
    }
    
    @Override
    public void trace(Marker marker, String s, Object o) {
    
    }
    
    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
    
    }
    
    @Override
    public void trace(Marker marker, String s, Object... objects) {
    
    }
    
    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isDebugEnabled() {
      return false;
    }
    
    @Override
    public void debug(String s) {
    
    }
    
    @Override
    public void debug(String s, Object o) {
    
    }
    
    @Override
    public void debug(String s, Object o, Object o1) {
    
    }
    
    @Override
    public void debug(String s, Object... objects) {
    
    }
    
    @Override
    public void debug(String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isDebugEnabled(Marker marker) {
      return false;
    }
    
    @Override
    public void debug(Marker marker, String s) {
    
    }
    
    @Override
    public void debug(Marker marker, String s, Object o) {
    
    }
    
    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
    
    }
    
    @Override
    public void debug(Marker marker, String s, Object... objects) {
    
    }
    
    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isInfoEnabled() {
      return false;
    }
    
    @Override
    public void info(String s) {
    
    }
    
    @Override
    public void info(String s, Object o) {
    
    }
    
    @Override
    public void info(String s, Object o, Object o1) {
    
    }
    
    @Override
    public void info(String s, Object... objects) {
    
    }
    
    @Override
    public void info(String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isInfoEnabled(Marker marker) {
      return false;
    }
    
    @Override
    public void info(Marker marker, String s) {
    
    }
    
    @Override
    public void info(Marker marker, String s, Object o) {
    
    }
    
    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
    
    }
    
    @Override
    public void info(Marker marker, String s, Object... objects) {
    
    }
    
    @Override
    public void info(Marker marker, String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isWarnEnabled() {
      return false;
    }
    
    @Override
    public void warn(String s) {
    
    }
    
    @Override
    public void warn(String s, Object o) {
    
    }
    
    @Override
    public void warn(String s, Object... objects) {
    
    }
    
    @Override
    public void warn(String s, Object o, Object o1) {
    
    }
    
    @Override
    public void warn(String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isWarnEnabled(Marker marker) {
      return false;
    }
    
    @Override
    public void warn(Marker marker, String s) {
    
    }
    
    @Override
    public void warn(Marker marker, String s, Object o) {
    
    }
    
    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
    
    }
    
    @Override
    public void warn(Marker marker, String s, Object... objects) {
    
    }
    
    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isErrorEnabled() {
      return false;
    }
    
    @Override
    public void error(String s) {
    
    }
    
    @Override
    public void error(String s, Object o) {
    
    }
    
    @Override
    public void error(String s, Object o, Object o1) {
    
    }
    
    @Override
    public void error(String s, Object... objects) {
    
    }
    
    @Override
    public void error(String s, Throwable throwable) {
    
    }
    
    @Override
    public boolean isErrorEnabled(Marker marker) {
      return false;
    }
    
    @Override
    public void error(Marker marker, String s) {
    
    }
    
    @Override
    public void error(Marker marker, String s, Object o) {
    
    }
    
    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
    
    }
    
    @Override
    public void error(Marker marker, String s, Object... objects) {
    
    }
    
    @Override
    public void error(Marker marker, String s, Throwable throwable) {
    
    }
  }
}

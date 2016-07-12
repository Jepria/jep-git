package com.technology.jep.jepria.server.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class dbLoadTestServlet extends HttpServlet {
  static Logger logger = Logger.getLogger(dbLoadTestServlet.class.getName());
  private String message;

    public void init() throws ServletException {
        message = "Hello World";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>");
        
        touchDataSourceForCheckLoad();
    }
    
    public void destroy() {
        // do nothing.
    }

  /*
   * TODO Убрать после проверки JavaMelody
   */
  private Db db = null;

  private void touchDataSourceForCheckLoad() {
    trace("touchDataSourceForCheckLoad() START");
    
    for(int i = 0; i < 100000; i++) {
      Integer operatorId = doJepAuthentication("NagornyyS", "123");
      trace(i + ": doJepAuthentication(\"NagornyyS\", \"123\") = " + operatorId);
    }
    
    trace("touchDataSourceForCheckLoad() END");
  }

  static private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private Integer doJepAuthentication(String username, String password) {
    Integer operatorId = null;
    // Проверим логин/пароль пользователя.
    try {
      operatorId = logon(getDb(), username, password);
    } catch (SQLException ex) {
      logger.error("pkg_Operator.logon error", ex);
    }

    return operatorId;
  }

  
  public static final Integer logon(Db db, String login, String password) throws SQLException {
//    trace("logon(Db db, " + login + ", " + password + ")");
    
    Integer result = null;
    String sqlQuery = 
      " begin" 
      + "  ? := pkg_Operator.Login(?, ?);" 
      + "  ? := pkg_Operator.GetCurrentUserID();" 
      + " end;";
    try {
      CallableStatement callableStatement = db.prepare(sqlQuery);
      // Установим Логин.
      if(login != null) callableStatement.setString(2, login);  
      else callableStatement.setNull(2, Types.VARCHAR); 
      // Установим Пароль.
      if(password != null) callableStatement.setString(3, password);   
      else callableStatement.setNull(3, Types.VARCHAR);  

      callableStatement.registerOutParameter(1, Types.VARCHAR);
      callableStatement.registerOutParameter(4, Types.INTEGER);

      callableStatement.execute();

      result = new Integer(callableStatement.getInt(4));
      if(callableStatement.wasNull())result = null;

    } finally {
//      sleep(10000);
//      db.closeStatement(sqlQuery);
      db.closeAll();
    }

    return result;
  }
  
  /**
   * Получим соединение с базой данных.
   */
  private Db getDb() {
    if (this.db == null) {
      this.db = new Db("jdbc/RFInfoDS");
    }
    return this.db;
  }


  private static void trace(String message) {
    logger.trace("dbLoadTestServlet: " + message);
//    System.out.println("dbLoadTestServlet: " + message);
  }
}

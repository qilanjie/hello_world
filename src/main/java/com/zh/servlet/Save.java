package com.zh.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Save extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/sdzh?serverTimezone=UTC";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "qwh";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user="" ; //获取服务端数据
        String password ="";
        String saveDate="";
        Connection conn = null;
        Statement stmt = null;
        try {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(req.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            user= jsonObject.getString("user");
            password=jsonObject.getString("password");
            saveDate=jsonObject.getString("datetime");
            System.out.println(responseStrBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开一个连接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行 SQL 查询
            stmt = conn.createStatement();
            //编写预处理 SQL 语句
            String sql= "INSERT INTO userdemo(username,password,savedate) VALUES(?,?,?)";

//实例化 PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

//传入参数，这里的参数来自于一个带有表单的jsp文件，很容易实现
//            ps.setString(1, req.getParameter("id"));
            ps.setString(1,user);
            ps.setString(2, password);
            ps.setString(3,saveDate);
//            ps.setString(4, req.getParameter("alexa"));
//            ps.setString(5, req.getParameter("country"));

//执行数据库更新操作，不需要SQL语句
            ps.executeUpdate();
//            sql = "SELECT id, name, url FROM websites1";
//            ps = conn.prepareStatement(sql);

//获取查询结果
//            ResultSet rs = ps.executeQuery();

            // 展开结果集数据库
//            while(rs.next()){
//                // 通过字段检索
//                int id  = rs.getInt("userid");
//                String name = rs.getString("username");
//                String pwd = rs.getString("password");
//
//                // 输出数据
//                out.println("用户ID: " + id);
//                out.println(", 用户名: " + name);
//                out.println(", 密码: " + pwd);
//                out.println("<br />");
//            }
//            out.println("</font>"+"</body></html>");
//
//            // 完成后关闭
//            rs.close();
            ps.close();
            stmt.close();
            conn.close();
        } catch(Exception se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        }// 处理 Class.forName 错误
        finally{
            // 最后是用于关闭资源的块
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        Map<String,String> params = new HashMap<String,String>();
        params.put("user",user);
        params.put("password",password);
        String string= JSON.toJSONString(params);
        out.println(string);
        out.flush();
        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}

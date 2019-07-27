package com.dgut;

import com.dgut.springboot.bean.User;
import com.dgut.springboot.redis.RedisConfig;
import com.dgut.springboot.redis.RedisPoolFactory;
import com.dgut.springboot.service.GoodsService;
import com.dgut.springboot.util.MD5Util;
import com.dgut.springboot.util.ValidatorUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.*;

public class test {
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisConfig redisConfig;
    @Test
    public void test(){
        System.out.println(1);
    }
    @Test
    public void MD5Test(){
        System.out.println(DigestUtils.md5Hex("123"));
    }
    @Test
    public void validatorUtilTest(){
        System.out.println(ValidatorUtil.isEmail("41sfd654646@qq.com"));
        System.out.println(ValidatorUtil.isEmail("4654646qq.com"));
    }

    @Test
    public void MD5Test1(){
        String inputPass = "123456";
//        System.out.println(DigestUtils.md5Hex("123"));
        System.out.println(MD5Util.inputPassToDBPass(inputPass, "1a2b3c4d"));
    }
    @Test
    public void StringFormatTest(){
        String str = "123123:%s";
        String s = String.format(str,123);
        System.out.println(s);
    }
    @Test
    public void goodsServiceTest(){
        System.out.println(goodsService.getGoodsList());
    }


    @Test
    public void testRDS(){

                // 声明Connection对象
                Connection con;
                // 驱动程序名
                String driver = "com.mysql.jdbc.Driver";
                // URL指向要访问的数据库名mydata
                String url = "jdbc:mysql://rm-wz92ro.mysql.rds.aliyuncs.com:3306/guanguan";
                // MySQL配置时的用户名
                String user = "root";
                // MySQL配置时的密码
                String password = "*******";
                // 遍历查询结果集
                try {
                    // 加载驱动程序
                    Class.forName(driver);
                    // 1.getConnection()方法，连接MySQL数据库！！
                    con = DriverManager.getConnection(url, user, password);
                    if (!con.isClosed())
                        System.out.println("Succeeded connecting to the Database!");
                    // 2.创建statement类对象，用来执行SQL语句！！
                    Statement statement = con.createStatement();
                    // 要执行的SQL语句
                    String sql = "select * from task";
                    // 3.ResultSet类，用来存放获取的结果集！！
                    ResultSet rs = statement.executeQuery(sql);
                    System.out.println("-----------------");
                    System.out.println("执行结果如下所示:");
                    System.out.println("-----------------");
                    System.out.println("任务id" + "\t" + "任务内容");
                    System.out.println("-----------------");
                    String taskID = null;
                    String task = null;
                    while (rs.next()) {
                        // 获取stuname这列数据
                        taskID = rs.getString("TaskNum");
                        // 获取stuid这列数据
                        task = rs.getString("Task_Content");

                        // 输出结果
                        System.out.println(taskID + "\t" + task);
                    }
                    rs.close();
                    con.close();
                } catch (ClassNotFoundException e) {
                    // 数据库驱动类异常处理
                    System.out.println("Sorry,can`t find the Driver!");
                    e.printStackTrace();
                } catch (SQLException e) {
                    // 数据库连接失败异常处理
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    System.out.println("数据库数据成功获取！！");
                }
            }






}

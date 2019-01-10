package cn.itcast;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;


public class TestJDBC {
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@192.168.174.129:1521:orcl";
    String username = "scott";
    String password = "tiger";

    Connection conn;
    PreparedStatement pst;
    CallableStatement cst;
    ResultSet rs;
    @Before
    public void init() throws Exception {
        //1. 加载驱动
        Class.forName(driver);
        // 2. 获取连接
        conn = DriverManager.getConnection(url, username, password);
    }
    /**
     * 测试存储函数
     * create or replace function getYearsalByEmpnoFun(eno in number) return number
     *
     * {?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
     */
    @Test
    public void testFunction() throws Exception {
        //3. sql语句
        String sql = "{?= call getYearsalByEmpnoFun(?)}";
        //4. 执行语句
        cst = conn.prepareCall(sql);
        //设置输入型占位符
        cst.setInt(2,7788);
        //设置输出类型
        cst.registerOutParameter(1, OracleTypes.NUMBER);
        //执行sql语句
        cst.executeUpdate();
        //5. 获取结果
        int ys = cst.getInt(1);
        System.out.println(ys);
    }
    /**
     * 测试存储过程输出为游标类型
     * create or replace procedure getEmpsByDeptno(dno in number, emps out sys_refcursor)
     * {call <procedure-name>[(<arg1>,<arg2>, ...)]}
     */
    @Test
    public void testProcedureOutCursor() throws Exception {
        //3. sql语句
        String sql = " {call getEmpsByDeptno(?,?)}";
        //4. 执行语句
        cst = conn.prepareCall(sql);
        //设置输入型占位符
        cst.setInt(1,20);
        //设置输出类型
        cst.registerOutParameter(2, OracleTypes.CURSOR);
        //执行sql语句
        cst.executeUpdate();
        //5. 获取结果
        rs = (ResultSet) cst.getObject(2);
        while (rs.next()){
            System.out.println(rs.getInt("empno")+rs.getString("ename"));
        }
    }
    /**
     * 测试存储过程
     * create or replace procedure getYearsalByEmpno(eno in number, yearsal out number)
     * {call <procedure-name>[(<arg1>,<arg2>, ...)]}
     */
    @Test
    public void testProcedure() throws SQLException {
        //3. sql语句
        String sql = " {call getYearsalByEmpno(?,?)}";
        //4. 执行语句
        cst = conn.prepareCall(sql);
        //设置输入型占位符
        cst.setInt(1,7788);
        //设置输出类型
        cst.registerOutParameter(2, OracleTypes.NUMBER);
        //执行sql语句
        cst.executeUpdate();
        //5. 获取结果
        int yearSal = cst.getInt(2);
        System.out.println(yearSal);
    }
    /**
     * 查询员工表中的记录
     */
    @Test
    public void test() throws SQLException {
        //3. sql语句
        String sql = "select * from emp";
        //4. 执行语句
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();
        //5. 获取结果
        while (rs.next()){
            System.out.println(rs.getInt("empno")+rs.getString("ename"));
        }
    }
    @After
    public void close() throws SQLException {
        if (rs != null){
            rs.close();
        }
        if (pst != null){
            pst.close();
        }
        if (conn != null){
            conn.close();
        }


    }
}

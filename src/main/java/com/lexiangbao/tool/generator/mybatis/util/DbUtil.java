package com.lexiangbao.tool.generator.mybatis.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lexiangbao.tool.generator.mybatis.Generator;
import com.lexiangbao.tool.generator.mybatis.entity.Table;

/**
 * 
 * 数据库操作工具类</p>
 * 1.获取所有表信息
 * 2.获取指定表的字段信息
 * 3.获取指定表的主键信息
 */	
public class DbUtil {
    protected static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private static String url;

    private static String driverName;

    private static String userName;

    private static String password;
     
    static {
        String resourceFile = "config";
        try {
            ResourceBundle rb = ResourceBundle.getBundle(resourceFile);
            url = rb.getString("jdbc_url");
            driverName = rb.getString("driverClassName");
            userName = rb.getString("jdbc_username");
            password = rb.getString("jdbc_password");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    /**
     *  获取数据库连接</p>
     * @return 数据库的连接，connection对象
     * @since 1.0
     */
    private static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, userName, password);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }
    
    /**
     * 关闭数据库连接</p>
     * @param conn 待关闭的Connection 对象
     * @since 1.0
     */
    private static void closeConn(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * 
     * 获取数据库中的所有表信息</p>
     * @return 表信息集合
     * @see   Table
     * @since 1.0
     */
    public static List<Table> getTables() {
        List<Table> tableList = new ArrayList<Table>();
        try {
            Connection conn = getConn();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs;
            String[] types = { "TABLE" };
            rs = metaData.getTables(null, null, "%", types);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME"); // 表名
                String tableType = rs.getString("TABLE_TYPE"); // 表类型
                String remarks = rs.getString("REMARKS"); // 表备注
                System.out.println(a30(tableName) + "|");
                tableList.add(new Table(tableName, remarks, tableType));
            }
            closeConn(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tableList;
    }

    public static Map<String, Object> getTableColumns(String tableName) {
        String className = SqlUtil.dbNameToJavaName(tableName, true);
        if (tableName.indexOf("_") > 0) {
            className = SqlUtil.dbNameToJavaName(tableName.substring(tableName.indexOf("_") + 1, tableName.length()),
                    true);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
        Map<Integer, String> jdbcJavaTypes = getJdbcJavaTypes();
        Map<Integer, String> shortJdbcJavaTypes = getShortJdbcJavaTypes();
        Map<Integer, String> jdbcTypes = getJdbcTypes();
        try {
            Connection conn = getConn();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet primaryKeysRs = metaData.getPrimaryKeys(null, null, tableName);
            List<String> primaryKeysList = new ArrayList<String>();
            while (primaryKeysRs.next()) {
                primaryKeysList.add(primaryKeysRs.getString("COLUMN_NAME"));
            }
            primaryKeysRs.close();
            if (primaryKeysList.size() == 0) {
                System.out.println("Warn:Table with no PK not supported. Use the first column as default PK");

            }
            if (primaryKeysList.size() > 1) {
                System.out.println("Warn:Composite PK not supported. Use the first column as default PK");
            }
            // 获取列
            ResultSet rs1 = metaData.getColumns(null, null, tableName, "%");
            boolean isExsitPk = false;
            while (rs1.next()) {
                Map<String, Object> rb = new HashMap<String, Object>();
                String qb = rs1.getString("COLUMN_NAME");
                String pb = SqlUtil.dbNameToJavaName(qb, false);
                String ob = "get" + SqlUtil.dbNameToJavaName(qb, true);
                String nb = "set" + SqlUtil.dbNameToJavaName(qb, true);
                if (isExsitPk) {
                    rb.put("fieldType", jdbcJavaTypes.get(rs1.getInt("DATA_TYPE")));
                    rb.put("shortFieldType",shortJdbcJavaTypes.get(rs1.getInt("DATA_TYPE")));
                    rb.put("isPk", Boolean.valueOf(false));
                } else {
                    if (primaryKeysList.size() < 1) {// 如果不存在主键，则使用第一个属性作为主键
                        rb.put("isPk", Boolean.valueOf(true));
                        rb.put("shortFieldType","String");
                        rb.put("fieldType",String.class.getName());
                        result.put("hasPk", Boolean.valueOf(false));
                        isExsitPk = true;
                    } else {
                        if (primaryKeysList.get(0).equals(qb)) {
                            rb.put("isPk", Boolean.valueOf(true));
                            //rb.put("shortFieldType","String");
                            //rb.put("fieldType",String.class.getName());
                            rb.put("shortFieldType",shortJdbcJavaTypes.get(rs1.getInt("DATA_TYPE")));
                            rb.put("fieldType", jdbcJavaTypes.get(rs1.getInt("DATA_TYPE")));
                            isExsitPk = true;
                        }else{
                            rb.put("fieldType", jdbcJavaTypes.get(rs1.getInt("DATA_TYPE")));
                            rb.put("shortFieldType",shortJdbcJavaTypes.get(rs1.getInt("DATA_TYPE")));
                        	rb.put("isPk", Boolean.valueOf(false));
                        }
                    }
                }
                rb.put("columnName", qb);
                rb.put("fieldName", pb);
                rb.put("XfieldName", SqlUtil.dbNameToJavaName(qb, true));
                rb.put("getterMethodName", ob);
                rb.put("setterMethodName", nb);
                rb.put("setterMethodName", nb);
                rb.put("jdbcType", jdbcTypes.get(rs1.getInt("DATA_TYPE")));
                rb.put("length", rs1.getInt("COLUMN_SIZE"));
                rb.put("label", rs1.getString("REMARKS"));
                // System.out.println("columnName:"+qb+"--fieldName:"+pb+"--getterMethodName:"+ob+"--setterMethodName:"+nb+"--fieldType:"+rb.get("fieldType"));
                columnList.add(rb);
                closeConn(conn);

            }
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
        result.put("columns", columnList);
        result.put("proxyName", SqlUtil.dbNameToJavaName(className, false)+Generator.dao_suffix);
        result.put("className", className);
        result.put("tableName", tableName);
        result.put("currentDate", LocalDate.now());

        return result;
    }

    /**
     * 获得一个表的主键信息
     */
    public static void getAllPrimaryKeys(String tableName) {
        try {
            Connection conn = getConn();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");// 列名
                short keySeq = rs.getShort("KEY_SEQ");// 序列号(主键内值1表示第一列的主键，值2代表主键内的第二列)
                String pkName = rs.getString("PK_NAME"); // 主键名称
                System.out.println(columnName + "-" + keySeq + "-" + pkName);
            }
            closeConn(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
	public static Map<String, Object> getBySql(String sql,String queryName) throws Exception {
		Connection conn = getConn();
		ResultSetMetaData q = conn.createStatement().executeQuery(sql).getMetaData();
		Set v = new HashSet();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
		Map<Integer, String> jdbcJavaTypes = getJdbcJavaTypes();
		Map<Integer, String> jdbcTypes = getJdbcTypes();
		Map<Integer, String> shortJdbcJavaTypes = getShortJdbcJavaTypes();
		String tableName  = null;
		for (int i = 0; i < q.getColumnCount(); i++) {
			if(tableName==null){
				tableName = q.getTableName(i+1);
			}
			String h = q.getColumnLabel(i + 1);
			if (v.contains(h)) {
				throw new Exception("Duplicated column " + h);
			}
			v.add(h);
			if (!h.matches("\\D\\w*")) {
				throw new Exception("Invalid column name: " + h);
			}
			//
			 Map<String, Object> rb = new HashMap<String, Object>();
             String qb = h;
             String pb = SqlUtil.dbNameToJavaName(qb, false);
             String ob = "get" + SqlUtil.dbNameToJavaName(qb, true);
             String nb = "set" + SqlUtil.dbNameToJavaName(qb, true);
             rb.put("columnName", qb);
             rb.put("fieldName", pb);
             rb.put("getterMethodName", ob);
             rb.put("setterMethodName", nb);
             rb.put("setterMethodName", nb);
             rb.put("fieldType", jdbcJavaTypes.get(q.getColumnType(i+1)));
             rb.put("jdbcType", jdbcTypes.get(q.getColumnType(i+1)));
             rb.put("shortFieldType",shortJdbcJavaTypes.get(q.getColumnType(i+1)));
             rb.put("length", q.getColumnDisplaySize(i+1));
             rb.put("label",  q.getColumnLabel(i+1) );
             // System.out.println("columnName:"+qb+"--fieldName:"+pb+"--getterMethodName:"+ob+"--setterMethodName:"+nb+"--fieldType:"+rb.get("fieldType"));
             columnList.add(rb);
		}
		 	result.put("columns", columnList);
		 	result.put("itemName", SqlUtil.dbNameToJavaName(queryName, true));
		 	
			String className = SqlUtil.dbNameToJavaName(tableName, true);
        	if (tableName.indexOf("_") > 0) {
        		className = SqlUtil.dbNameToJavaName(tableName.substring(tableName.indexOf("_") + 1, tableName.length()),
                    true);
        	}
	        result.put("className", className);
		return result;
	}

    public static void main(String[] args) throws Exception {
    	getBySql("select t1.id,t1.user_name,t2.fullname,t2.comment_count from com_user t1 LEFT JOIN com_user_detail t2 on t2.user_id=t1.id","getUserQuery");
	}

    // public static List<>

    private static Map<Integer, String> getJdbcJavaTypes() {
        Map<Integer, String> jdbcJavaTypes = new HashMap<Integer, String>();
        
        jdbcJavaTypes.put(Integer.valueOf(2003),Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-5),Long.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-2), "byte[]");

        jdbcJavaTypes.put(Integer.valueOf(-7), Integer.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(2004), "byte[]");

        jdbcJavaTypes.put(Integer.valueOf(16), Boolean.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(1), String.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(2005),String.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(70), Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(91), Date.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(2001), Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(8), Double.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(6), Double.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(4),Integer.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(2000), Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-16),String.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-4),"byte[]");

        jdbcJavaTypes.put(Integer.valueOf(-1),String.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-15), String.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(2011),String.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-9), String.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(0), Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(1111), Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(7),Float.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(2006), Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(5), Short.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(2002), Object.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(92),Date.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(93), Date.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-6), Byte.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-3),"byte[]");

        jdbcJavaTypes.put(Integer.valueOf(12),String.class.getName());
        return jdbcJavaTypes;
    }
    
    private static Map<Integer, String> getShortJdbcJavaTypes() {
        Map<Integer, String> jdbcJavaTypes = new HashMap<Integer, String>();
        
        jdbcJavaTypes.put(Integer.valueOf(2003),"Object");

        jdbcJavaTypes.put(Integer.valueOf(-5),"Long");

        jdbcJavaTypes.put(Integer.valueOf(-2), "byte[]");

        jdbcJavaTypes.put(Integer.valueOf(-7), "Integer");

        jdbcJavaTypes.put(Integer.valueOf(2004), "byte[]");

        jdbcJavaTypes.put(Integer.valueOf(16), "Boolean");

        jdbcJavaTypes.put(Integer.valueOf(1), "String");

        jdbcJavaTypes.put(Integer.valueOf(2005),"String");

        jdbcJavaTypes.put(Integer.valueOf(70), "Object");

        jdbcJavaTypes.put(Integer.valueOf(91), Date.class.getName());
        
        jdbcJavaTypes.put(Integer.valueOf(3), "Double");

        jdbcJavaTypes.put(Integer.valueOf(2001),"Object");

        jdbcJavaTypes.put(Integer.valueOf(8),"Double");

        jdbcJavaTypes.put(Integer.valueOf(6), "Double");

        jdbcJavaTypes.put(Integer.valueOf(4),"Integer");

        jdbcJavaTypes.put(Integer.valueOf(2000), "Object");

        jdbcJavaTypes.put(Integer.valueOf(-16),"String");

        jdbcJavaTypes.put(Integer.valueOf(-4),"byte[]");

        jdbcJavaTypes.put(Integer.valueOf(-1),"String");

        jdbcJavaTypes.put(Integer.valueOf(-15), "String");

        jdbcJavaTypes.put(Integer.valueOf(2011),"String");

        jdbcJavaTypes.put(Integer.valueOf(-9), "String");

        jdbcJavaTypes.put(Integer.valueOf(0), "Object");

        jdbcJavaTypes.put(Integer.valueOf(1111), "Object");

        jdbcJavaTypes.put(Integer.valueOf(7),"Float");

        jdbcJavaTypes.put(Integer.valueOf(2006), "Object");

        jdbcJavaTypes.put(Integer.valueOf(5), "Short");

        jdbcJavaTypes.put(Integer.valueOf(2002), "Object");

        jdbcJavaTypes.put(Integer.valueOf(92),Date.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(93), Timestamp.class.getName());

        jdbcJavaTypes.put(Integer.valueOf(-6), "Byte");

        jdbcJavaTypes.put(Integer.valueOf(-3),"byte[]");

        jdbcJavaTypes.put(Integer.valueOf(12),"String");
        return jdbcJavaTypes;
    }
    
    
    private static Map<Integer, String> getJdbcTypes() {
        Map<Integer, String> jdbcJavaTypes = new HashMap<Integer, String>();
        jdbcJavaTypes.put(Integer.valueOf(2003), "ARRAY");
        jdbcJavaTypes.put(Integer.valueOf(-5), "BIGINT");
        jdbcJavaTypes.put(Integer.valueOf(-2), "BINARY");
        jdbcJavaTypes.put(Integer.valueOf(-7), "BIT");
        jdbcJavaTypes.put(Integer.valueOf(2004), "BLOB");
        jdbcJavaTypes.put(Integer.valueOf(16), "BOOLEAN");
        jdbcJavaTypes.put(Integer.valueOf(1), "CHAR");
        jdbcJavaTypes.put(Integer.valueOf(2005), "CLOB");
        jdbcJavaTypes.put(Integer.valueOf(70), "DATALINK");
        jdbcJavaTypes.put(Integer.valueOf(91), "DATE");
        jdbcJavaTypes.put(Integer.valueOf(3), "DECIMAL");
        jdbcJavaTypes.put(Integer.valueOf(2001), "DISTINCT");
        jdbcJavaTypes.put(Integer.valueOf(8), "DOUBLE");
        jdbcJavaTypes.put(Integer.valueOf(6), "FLOAT");
        jdbcJavaTypes.put(Integer.valueOf(4), "INTEGER");
        jdbcJavaTypes.put(Integer.valueOf(2000), "JAVA_OBJECT");
        jdbcJavaTypes.put(Integer.valueOf(-4), "LONGVARBINARY");
        jdbcJavaTypes.put(Integer.valueOf(-1), "LONGVARCHAR");
        jdbcJavaTypes.put(Integer.valueOf(-15), "NCHAR");
        jdbcJavaTypes.put(Integer.valueOf(2011), "NCLOB");
        jdbcJavaTypes.put(Integer.valueOf(-9), "NVARCHAR");
        jdbcJavaTypes.put(Integer.valueOf(-16), "LONGNVARCHAR");
        jdbcJavaTypes.put(Integer.valueOf(0), "NULL");
        jdbcJavaTypes.put(Integer.valueOf(2), "NUMERIC");
        jdbcJavaTypes.put(Integer.valueOf(1111), "OTHER");
        jdbcJavaTypes.put(Integer.valueOf(7), "REAL");
        jdbcJavaTypes.put(Integer.valueOf(2006), "REF");
        jdbcJavaTypes.put(Integer.valueOf(5), "SMALLINT");
        jdbcJavaTypes.put(Integer.valueOf(2002), "STRUCT");
        jdbcJavaTypes.put(Integer.valueOf(92), "TIME");
        jdbcJavaTypes.put(Integer.valueOf(93), "TIMESTAMP");
        jdbcJavaTypes.put(Integer.valueOf(-6), "TINYINT");
        jdbcJavaTypes.put(Integer.valueOf(-3), "VARBINARY");
        jdbcJavaTypes.put(Integer.valueOf(12), "VARCHAR");
        return jdbcJavaTypes;
    }

    private static String a30(String str) {
        while (str.length() < 30) {
            str = str + " ";
        }
        return str;
    }

}

package com.crh2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crh2.database.DatabaseUtil;

/**
 * 功能：提供统一的查询方法
 * @author huhui
 *
 */
public class SQLHelper {
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Statement stmt = null;

	/**
	 * 查询数据
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public List<Object> query(String sql, Object parameters[]) {
		List<Object> list = new ArrayList<Object>();
		try {
			conn = DatabaseUtil.getConnection();
			ps = conn.prepareStatement(sql);
			// 给sql中的问号赋值
			prepareCommand(ps, parameters);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			// 可以得到有多少列
			int columnNum = rsmd.getColumnCount();
			// 将数据封装到list中
			while (rs.next()) {
				Object[] objects = new Object[columnNum];
				for (int i = 0; i < objects.length; i++) {
					objects[i] = rs.getObject(i + 1);
				}
				list.add(objects);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(rs, ps, conn);
		}
		return list;
	}

	/**
	 *  插入数据操作
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public boolean update(String sql, Object parameters[]) {
		boolean b = false;
		try {
			conn = DatabaseUtil.getConnection();
			ps = conn.prepareStatement(sql);
			prepareCommand(ps, parameters);
			int i = ps.executeUpdate();
			if (i == 1) {
				b = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(rs, ps, conn);
		}
		return b;
	}
	
	/**
	 * 插入之后，返回插入值的主键id
	 * @param sql
	 * @return
	 */
	public int insertAndGetId(String sql){
		int id = -1;
		try {
			conn = DatabaseUtil.getConnection();
			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			ps.execute();
			rs = ps.getGeneratedKeys();
			if(rs !=null && rs.next()){
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(rs, ps, conn);
		}
		return id;
	}
	
	/**
	 * 批量数据插入操作
	 * @param sqlList
	 */
	public void batchInsert(ArrayList<String> sqlList){
		try {
			conn = DatabaseUtil.getConnection();
			conn.setAutoCommit(false);//取消自动提交
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			for(String sqlString : sqlList){
				stmt.addBatch(sqlString);
			}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			if(conn != null){
				try {
					conn.rollback();//回滚
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(rs, stmt, conn);
		}
	}
	
	/**
	 * 参数转换
	 * @param pstm
	 * @param params
	 */
	public void prepareCommand(PreparedStatement pstm, Object[] params) {
		if (params == null || params.length == 0) {
			return;
		}
		try {
			for (int i = 0; i < params.length; i++) {
				int parameterIndex = i + 1;
				// String
				if (params[i].getClass() == String.class) {
					pstm.setString(parameterIndex, params[i].toString());
				}
				// Short
				else if (params[i].getClass() == short.class) {
					pstm.setShort(parameterIndex, Short.parseShort(params[i]
							.toString()));
				}
				// Long
				else if (params[i].getClass() == long.class) {
					pstm.setLong(parameterIndex, Long.parseLong(params[i]
							.toString()));
				}
				// Integer
				else if (params[i].getClass() == Integer.class) {
					pstm.setInt(parameterIndex, Integer.parseInt(params[i]
							.toString()));
				}
				// Date
				else if (params[i].getClass() == Date.class) {
					java.util.Date dt = (java.util.Date) params[i];
					pstm.setDate(parameterIndex,
							new java.sql.Date(dt.getTime()));
				}
				// Byte
				else if (params[i].getClass() == byte.class) {
					pstm.setByte(parameterIndex, (Byte) params[i]);
				}
				// Float
				else if (params[i].getClass() == float.class) {
					pstm.setFloat(parameterIndex, Float.parseFloat(params[i]
							.toString()));
				}
				// Boolean
				else if (params[i].getClass() == boolean.class) {
					pstm.setBoolean(parameterIndex, Boolean
							.parseBoolean(params[i].toString()));
				} else {
					throw new Exception("参数准备出错:数据类型不可见"
							+ params[i].getClass().toString());
				}
			}
		} catch (Exception e) {
		}
	}

}

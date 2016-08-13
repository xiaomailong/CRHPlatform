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
 * ���ܣ��ṩͳһ�Ĳ�ѯ����
 * @author huhui
 *
 */
public class SQLHelper {
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Statement stmt = null;

	/**
	 * ��ѯ����
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public List<Object> query(String sql, Object parameters[]) {
		List<Object> list = new ArrayList<Object>();
		try {
			conn = DatabaseUtil.getConnection();
			ps = conn.prepareStatement(sql);
			// ��sql�е��ʺŸ�ֵ
			prepareCommand(ps, parameters);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			// ���Եõ��ж�����
			int columnNum = rsmd.getColumnCount();
			// �����ݷ�װ��list��
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
	 *  �������ݲ���
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
	 * ����֮�󣬷��ز���ֵ������id
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
	 * �������ݲ������
	 * @param sqlList
	 */
	public void batchInsert(ArrayList<String> sqlList){
		try {
			conn = DatabaseUtil.getConnection();
			conn.setAutoCommit(false);//ȡ���Զ��ύ
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
					conn.rollback();//�ع�
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
	 * ����ת��
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
					throw new Exception("����׼������:�������Ͳ��ɼ�"
							+ params[i].getClass().toString());
				}
			}
		} catch (Exception e) {
		}
	}

}

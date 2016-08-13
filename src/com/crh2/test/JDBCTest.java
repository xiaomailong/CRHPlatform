package com.crh2.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.crh2.database.DatabaseUtil;

public class JDBCTest {

	/**
	 * @param args
	 */
	private int SIZE = 10000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JDBCTest test = new JDBCTest();
//		test.testGeneralInsert();
//		test.batchInsert();
		test.test1();
	}
	
	public void test1(){
		ArrayList<String> al = new ArrayList<String>();
		al.add("00");
		al.add("11");
		al.add("22");
		al.add("33");
		al.add("44");
		al.add("55");
		al.add("66");
		al.add("77");
		this.test2(al);
		System.out.println("al值");
		for(int i=0;i<al.size();i++){
			System.out.println(al.get(i));
		}
	}
	
	public void test2(ArrayList<String> al){
		ArrayList<String> list = new ArrayList<String>(al);
		for(int i=list.size()-1;i>2;i--){
			System.out.println("正在移除"+list.get(i));
			list.remove(i);
		}
	}
	
	
	//批量插入
	public void batchInsert() {
		System.out.println("方法二开始");
		long start = System.currentTimeMillis();
		Connection conn = DatabaseUtil.getConnection();
		Statement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			for(int x = 0; x < 10000; x++){  
				   stmt.addBatch("INSERT INTO test VALUES(1,2,3,4,5,6)");  
				}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DatabaseUtil.close(null, null, conn);
		}
		System.out.println("方法二耗时："+(System.currentTimeMillis()-start));
	}
	
	public void testGeneralInsert(){
		System.out.println("方法一开始");
		long start = System.currentTimeMillis();
		for(int i=0;i<SIZE;i++){
			this.generalInsert();
		}
		System.out.println("方法一耗时："+(System.currentTimeMillis()-start));
	}
	
	//普通的插入
	public void generalInsert(){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseUtil.getConnection();
			ps = conn.prepareStatement("insert into test values(?,?,?,?,?,?)");
			ps.setInt(1, 1);
			ps.setInt(2, 2);
			ps.setInt(3, 3);
			ps.setInt(4, 4);
			ps.setInt(5, 5);
			ps.setInt(6, 6);
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(null, ps, conn);
		}
	}

}

package com.crh2.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.crh2.dao.SQLHelper;

/**
 * �洢txt�ļ������·��
 * @author huhui
 *
 */
public class PathFileService {
	
	/**
	 * �õ����ݿ���Ĭ�ϵ�txt����·��
	 * @return
	 */
	public static String getDefaultPath(){
		SQLHelper sqlHelper = new SQLHelper();
		String sql = "SELECT path FROM traintypesavepath";
		List list = sqlHelper.query(sql, null);
		return ((Object [])list.get(0))[0].toString();
	}
	
	/**
	 * ��txt��д����
	 * @param lineContent
	 */
	public static void writeIntoPathTxt(String lineContent) {
		BufferedWriter output = null;
		try {
			File f = new File(getDefaultPath());
			if (!f.exists()) {
				f.createNewFile();// �������ļ��򴴽�
			}
			output = new BufferedWriter(new FileWriter(f));
			output.write(lineContent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
}

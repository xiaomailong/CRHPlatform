/**
 * ������������ݣ���д�����ݿ�
 */
package com.crh2.test;

import com.crh2.dao.SQLHelper;

public class CalSectionData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalSectionData csd = new CalSectionData();
		System.out.println("���㿪ʼ");
		csd.CalAndWrite();
		System.out.println("д�����");
	}
	
	//������������ݣ�������������
	public void CalAndWrite(){
		double start = 0,end = 0,length = 115219;
		String sql = "";
		SQLHelper sqlHelper = new SQLHelper();
		while(end<=length){
			start = end;
			end = start + 7000;//�е�
			sql = "insert into sectiondata values(null,"+start+","+end+",1)";
			//�������ݿ�
			sqlHelper.update(sql, null);
			start = end;
			end = start + 190;//û��
			sql = "insert into sectiondata values(null,"+start+","+end+",0)";
			//�������ݿ�
			sqlHelper.update(sql, null);
		}
	}

}

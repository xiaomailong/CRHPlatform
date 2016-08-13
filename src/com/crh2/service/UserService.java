/**
 * ���ܣ�User��service
 */
package com.crh2.service;

import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.Users;

public class UserService {

	SQLHelper sqlHelper = new SQLHelper();

	// �жϵ�¼�Ƿ�Ϸ�
	public boolean checkUsers(Users user) {
		String sql = "select * from users where name=? and password=?";
		String parameters[] = { user.getName(), user.getPassword() };
		List<Object> list = sqlHelper.query(sql, parameters);
		if (list.size() != 0) {
			return true;
		} else {
			return false;
		}
	}
}

package com.crh2.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * 将用户痕迹写入txt文档
 * @author huhui
 *
 */
public class WriteIntoTxt {

	/**
	 * 往txt中写数据
	 * @param strList
	 * @param fileName
	 */
	public static void writeIntoTxt(ArrayList<String> strList,String fileName) {
		String content = "";
		BufferedWriter output = null;
		try {
			File f = new File("C:\\"+fileName);
			if (!f.exists()) {
				f.createNewFile();// 不存在文件则创建
			}
			output = new BufferedWriter(new FileWriter(f,true));
			for(String str:strList){
				content += str + "\r\n";
				output.write(content);
				content = "";//清空content
			}
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

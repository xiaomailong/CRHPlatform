package com.crh.test;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcelTest {

	public static void main(String[] args) throws IOException,
			RowsExceededException, WriteException {
		String fname = "C:\\test.xls";

		File f = new File(fname);

		WritableWorkbook wb = Workbook.createWorkbook(f);
		WritableSheet wsheet = wb.createSheet("��һҳ", 0);

		WritableFont font1 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		WritableCellFormat format1 = new WritableCellFormat(font1);

		wsheet.addCell(new Label(0, 0, "����", format1));
		wsheet.addCell(new Label(1, 0, "���", format1));
		wsheet.addCell(new Label(2, 0, "����", format1));// ǰ���Ǹ�2�Ǳ�ʾ�У������0��ʾ��ǰ��

		wsheet.addCell(new Label(0, 1, "������", format1));
		wsheet.addCell(new Label(1, 1, "�Ϲ�", format1));
		// wsheet.addCell(new Label(2,1, "1",format1));//ǰ���Ǹ�2�Ǳ�ʾ�У������1��ʾ��ǰ��
		jxl.write.Number number = new jxl.write.Number(2, 1, 1);// д��һ�����ָ�ʽ�ġ�
		wsheet.addCell(number);

		wb.write();
		wb.close();

	}

}

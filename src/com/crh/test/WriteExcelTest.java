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
		WritableSheet wsheet = wb.createSheet("第一页", 0);

		WritableFont font1 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		WritableCellFormat format1 = new WritableCellFormat(font1);

		wsheet.addCell(new Label(0, 0, "名字", format1));
		wsheet.addCell(new Label(1, 0, "外号", format1));
		wsheet.addCell(new Label(2, 0, "排行", format1));// 前面那个2是表示列，后面的0表示当前行

		wsheet.addCell(new Label(0, 1, "张三疯", format1));
		wsheet.addCell(new Label(1, 1, "老鬼", format1));
		// wsheet.addCell(new Label(2,1, "1",format1));//前面那个2是表示列，后面的1表示当前行
		jxl.write.Number number = new jxl.write.Number(2, 1, 1);// 写入一个数字格式的。
		wsheet.addCell(number);

		wb.write();
		wb.close();

	}

}

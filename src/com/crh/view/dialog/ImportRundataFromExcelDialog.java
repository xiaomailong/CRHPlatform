/**
 * 2014.11.3
 * ��Excel�ļ��е����������ݽ���
 */
package com.crh.view.dialog;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.crh2.calculate.MergeLists;
import com.crh2.calculate.excel.ImportExcel;
import com.crh2.javabean.Rundata;
import com.crh2.util.MyUtillity;

/**
 * ��Excel�ļ��е����г���������
 * @author huhui
 *
 */
public class ImportRundataFromExcelDialog extends JDialog {
	private JTextField textField_1;
	/**
	 *  �����ļ�ѡ����
	 */
	private JFileChooser excelFileChooser;
	/**
	 *  �����ļ�������
	 */
	private FileNameExtensionFilter fileFilter;

	public ImportRundataFromExcelDialog() {
		setBounds(100, 100, 517, 288);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		setTitle("�������ݵ���");
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u6570\u636E\u5BFC\u5165", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(28, 81, 337, 21);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JButton button = new JButton("�ļ�ѡ��");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excelFileChooser = new JFileChooser();
				fileFilter = new FileNameExtensionFilter(".xls", "xls");
				excelFileChooser.setFileFilter(fileFilter);
				excelFileChooser.setVisible(true);//��ʾ
				int retval = excelFileChooser.showOpenDialog(ImportRundataFromExcelDialog.this);
				 if(retval ==JFileChooser.APPROVE_OPTION){
					//�õ��û�ѡ���ļ��ľ���·��
					String filePath = excelFileChooser.getSelectedFile().getAbsolutePath();
					//���ļ�·���ı���ֵ
					textField_1.setText(filePath.trim());
				 }
			}
		});
		button.setBounds(380, 80, 93, 23);
		panel.add(button);
		
		JButton importBtn = new JButton("����");
		importBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String excelPath = textField_1.getText().trim();
				ImportExcel importExcel = new ImportExcel(excelPath);
				ArrayList<Rundata> rundataList = importExcel.readRundata();
				if(rundataList.size() != 0){ 
					MergeLists.ftRundataMergedList = rundataList;
					JOptionPane.showMessageDialog(ImportRundataFromExcelDialog.this, "���ݵ���ɹ���");
				}else{
					JOptionPane.showMessageDialog(ImportRundataFromExcelDialog.this, "���ݵ���ʧ�ܣ�");
				}
			}
		});
		importBtn.setBounds(126, 161, 93, 23);
		panel.add(importBtn);
		
		JButton button_2 = new JButton("ȡ��");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_2.setBounds(260, 161, 93, 23);
		panel.add(button_2);
		setModal(true);
		setVisible(true);
	}
}

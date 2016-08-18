package com.crh.view.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.crh.service.TrainEditPanelService;
import com.crh.view.dialog.CreateTrainDialog;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainFormation;
import com.crh2.javabean.TrainInfo;
import com.crh2.util.MyTools;

/**
 * �г��༭ҳ��
 * @author huhui
 *
 */
public class TrainEditPanel extends JPanel {
    private static JTextField               maxV;
    private JTextField                      maxEV;
    private JTextField                      conV;
    private JTextField                      powerConf;
    private JTextField                      mzmax;
    private JTextField                      a200;
    private JTextField                      mzmin;
    private JTextField                      J;
    private JTextField                      slope;
    private JTextField                      em1;
    private JTextField                      ar;
    private JTextField                      dv;
    private JTextField                      launchf;
    private JTextField                      powerFac;
    private JTextField                      gearm2;
    private static JTextField               M;
    private JTextField                      a;
    private JTextField                      b;
    private JTextField                      c;
    private JTextField                      tu1;
    private JTextField                      tu2;
    private JTextField                      bu1;
    private JTextField                      bu2;

    private static final JComboBox<Integer> trainIdComboBox   = new JComboBox<Integer>();
    private static final JComboBox<String>  trainNameComboBox = new JComboBox<String>();

    /**
     * ���С��г����ơ�
     */
    private Integer[]                       trainCategoryIdArray;
    private String[]                        trainCategoryNameArray;
    /**
     * �����table
     */
    private TrainFormationTablePanel        trainFormationTablePanel;
    /**
     * ����ͼƬ
     */
    private DisplayCarsPanel                displayCarsPanel;

    private static JTextArea                infoCollectorText;

    public TrainEditPanel() {
        setLayout(new GridLayout(2, 1));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "�г���Ϣ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panel);
        panel.setLayout(null);

        JLabel label = new JLabel("�г����ƣ�");
        label.setBounds(20, 23, 60, 15);
        panel.add(label);

        //��ʼ������������
        trainFormationTablePanel = new TrainFormationTablePanel();
        //��ʼ���г�ͼƬ���
        displayCarsPanel = new DisplayCarsPanel();
        displayCarsPanel.setSize(1276, 138);
        displayCarsPanel.setLocation(10, 209);

        // ��ʼ��JComboBox������
        this.getAllTrainCategoryToArray();

        trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainCategoryIdArray));
        trainIdComboBox.setBounds(888, 20, 102, 21);
        panel.add(trainIdComboBox);
        trainIdComboBox.setVisible(false);

        trainNameComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1.����JComboBox����
                int selectedIndex = trainNameComboBox.getSelectedIndex();
                trainIdComboBox.setSelectedIndex(selectedIndex);
                // 2.�ı�����ϵ�����ֵ
                if (trainIdComboBox.getItemCount() == 0) {
                    assignParametersOnPanel(new TrainInfo(), trainFormationTablePanel.generateEightDefaultCars());
                } else {
                    int id = (Integer) trainIdComboBox.getSelectedItem();
                    assignParametersOnPanel(TrainEditPanelService.getTrainInfoByCategoryId(id), TrainEditPanelService.getTrainFormationByTrainCategoryId(id));
                }
                displayCarsPanel.showCars();
                trainInfoCollector();
            }
        });
        trainNameComboBox.setModel(new DefaultComboBoxModel<String>(trainCategoryNameArray));
        trainNameComboBox.setBounds(90, 20, 102, 21);
        panel.add(trainNameComboBox);

        JButton deleteTrainButton = new JButton("ɾ���г�");
        deleteTrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (trainNameComboBox.getItemCount() == 1) {
                    JOptionPane.showMessageDialog(TrainEditPanel.this, "���һ�г�����ɾ����");
                } else {
                    int option = JOptionPane.showConfirmDialog(TrainEditPanel.this, "ȷ��ɾ����");
                    if (option == 0) {
                        int id = (Integer) trainIdComboBox.getSelectedItem();
                        int index = trainIdComboBox.getSelectedIndex();
                        deleteTrainCategory(id, index);
                    }
                }
            }
        });
        deleteTrainButton.setBounds(226, 19, 93, 23);
        panel.add(deleteTrainButton);

        JButton createTrainButton = new JButton("�½��г�");
        createTrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateTrainDialog createTrainDialog = new CreateTrainDialog(TrainEditPanel.this);
            }
        });
        createTrainButton.setBounds(347, 19, 93, 23);
        panel.add(createTrainButton);

        JButton button = new JButton("���浱ǰ����");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkCarTypeConf()) {
                    int id = (Integer) trainIdComboBox.getSelectedItem();
                    saveTrainInfoAndFormation(id);
                } else {
                    JOptionPane.showMessageDialog(TrainEditPanel.this, "���������÷�ʽ���롰�������͡���һ�£�");
                }
            }
        });
        button.setBounds(467, 19, 115, 23);
        panel.add(button);

        JLabel label_1 = new JLabel("*ѡ���Ѵ��ڵģ��������µ��г����ƣ����б༭");
        label_1.setForeground(new Color(255, 0, 0));
        label_1.setBounds(605, 23, 267, 15);
        panel.add(label_1);

        JSeparator separator = new JSeparator();
        separator.setBounds(5, 48, 2000, 1);
        panel.add(separator);

        JLabel label_2 = new JLabel("����ٶ�");
        label_2.setBounds(20, 59, 60, 15);
        panel.add(label_2);

        maxV = new JTextField();
        maxV.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                trainInfoCollector();
            }
        });
        maxV.setBounds(71, 56, 66, 21);
        panel.add(maxV);
        maxV.setColumns(10);

        JLabel lblKmh = new JLabel("km/h");
        lblKmh.setBounds(140, 59, 34, 15);
        panel.add(lblKmh);

        JLabel label_3 = new JLabel("��������ٶ�");
        label_3.setBounds(194, 59, 72, 15);
        panel.add(label_3);

        maxEV = new JTextField();
        maxEV.setColumns(10);
        maxEV.setBounds(269, 56, 66, 21);
        panel.add(maxEV);

        JLabel label_4 = new JLabel("km/h");
        label_4.setBounds(338, 59, 34, 15);
        panel.add(label_4);

        JLabel label_5 = new JLabel("���������ٶ�");
        label_5.setBounds(397, 59, 77, 15);
        panel.add(label_5);

        conV = new JTextField();
        conV.setBounds(472, 56, 66, 21);
        panel.add(conV);
        conV.setColumns(10);

        JLabel label_6 = new JLabel("km/h");
        label_6.setBounds(543, 59, 34, 15);
        panel.add(label_6);

        JLabel label_7 = new JLabel("�������÷�ʽ");
        label_7.setBounds(605, 59, 72, 15);
        panel.add(label_7);

        powerConf = new JTextField();
        powerConf.setBounds(680, 56, 66, 21);
        panel.add(powerConf);
        powerConf.setColumns(10);

        JLabel label_8 = new JLabel("�������");
        label_8.setBounds(20, 111, 60, 15);
        panel.add(label_8);

        JLabel label_9 = new JLabel("��С����");
        label_9.setBounds(20, 155, 60, 15);
        panel.add(label_9);

        JLabel label_10 = new JLabel("����ϵ��");
        label_10.setBounds(20, 199, 60, 15);
        panel.add(label_10);

        JLabel label_11 = new JLabel("�����¶�");
        label_11.setBounds(20, 246, 60, 15);
        panel.add(label_11);

        JLabel label_12 = new JLabel("���Ч��");
        label_12.setBounds(20, 295, 60, 15);
        panel.add(label_12);

        mzmax = new JTextField();
        mzmax.setEditable(false);
        mzmax.setColumns(10);
        mzmax.setBounds(71, 108, 66, 21);
        panel.add(mzmax);

        JLabel label_13 = new JLabel("t");
        label_13.setBounds(140, 111, 34, 15);
        panel.add(label_13);

        a200 = new JTextField();
        a200.setColumns(10);
        a200.setBounds(247, 108, 88, 21);
        panel.add(a200);

        JLabel lblMs = new JLabel("KV");
        lblMs.setBounds(338, 111, 54, 15);
        panel.add(lblMs);

        mzmin = new JTextField();
        mzmin.setEditable(false);
        mzmin.setColumns(10);
        mzmin.setBounds(71, 152, 66, 21);
        panel.add(mzmin);

        JLabel label_15 = new JLabel("t");
        label_15.setBounds(140, 155, 34, 15);
        panel.add(label_15);

        J = new JTextField();
        J.setColumns(10);
        J.setBounds(71, 196, 66, 21);
        panel.add(J);

        slope = new JTextField();
        slope.setColumns(10);
        slope.setBounds(71, 243, 66, 21);
        panel.add(slope);

        em1 = new JTextField();
        em1.setColumns(10);
        em1.setBounds(71, 292, 66, 21);
        panel.add(em1);

        JLabel label_16 = new JLabel("��");
        label_16.setBounds(140, 246, 34, 15);
        panel.add(label_16);

        ar = new JTextField();
        ar.setColumns(10);
        ar.setBounds(416, 292, 78, 21);
        ar.setVisible(false);
        panel.add(ar);

        JLabel label_19 = new JLabel("����ٶ�");
        label_19.setBounds(194, 199, 60, 15);
        panel.add(label_19);

        dv = new JTextField();
        dv.setColumns(10);
        dv.setBounds(247, 196, 88, 21);
        panel.add(dv);

        JLabel lblMs_1 = new JLabel("m/s");
        lblMs_1.setBounds(338, 199, 54, 15);
        panel.add(lblMs_1);

        JLabel label_20 = new JLabel("��������");
        label_20.setBounds(194, 246, 60, 15);
        panel.add(label_20);

        launchf = new JTextField();
        launchf.setEditable(false);
        launchf.setColumns(10);
        launchf.setBounds(247, 243, 88, 21);
        panel.add(launchf);

        JLabel lblNton = new JLabel("N");
        lblNton.setBounds(338, 246, 54, 15);
        panel.add(lblNton);

        JLabel label_21 = new JLabel("��������");
        label_21.setBounds(194, 295, 60, 15);
        panel.add(label_21);

        powerFac = new JTextField();
        powerFac.setColumns(10);
        powerFac.setBounds(247, 292, 88, 21);
        panel.add(powerFac);

        JLabel label_22 = new JLabel("����Ч��");
        label_22.setBounds(194, 155, 60, 15);
        panel.add(label_22);

        gearm2 = new JTextField();
        gearm2.setColumns(10);
        gearm2.setBounds(247, 152, 90, 21);
        panel.add(gearm2);

        JLabel label_24 = new JLabel("ָ���г�����");
        label_24.setBounds(814, 59, 72, 15);
        panel.add(label_24);

        M = new JTextField();
        M.setEditable(false);
        M.setColumns(10);
        M.setBounds(888, 56, 66, 21);
        panel.add(M);

        JLabel label_25 = new JLabel("t");
        label_25.setBounds(956, 59, 54, 15);
        panel.add(label_25);

        JSeparator separator_1 = new JSeparator();
        separator_1.setOrientation(SwingConstants.VERTICAL);
        separator_1.setBounds(1047, 48, 2, 272);
        panel.add(separator_1);

        JSeparator separator_2 = new JSeparator();
        separator_2.setOrientation(SwingConstants.VERTICAL);
        separator_2.setBounds(416, 106, 1, 155);
        panel.add(separator_2);

        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(416, 106, 11, 1);
        panel.add(separator_3);

        JLabel label_28 = new JLabel("��������");
        label_28.setBounds(427, 99, 54, 15);
        panel.add(label_28);

        JLabel lblNewLabel = new JLabel("W0 = a + b*V + c*V^2  (KN)");
        lblNewLabel.setBounds(427, 120, 192, 15);
        panel.add(lblNewLabel);

        JSeparator separator_4 = new JSeparator();
        separator_4.setBounds(475, 106, 131, 1);
        panel.add(separator_4);

        JSeparator separator_5 = new JSeparator();
        separator_5.setOrientation(SwingConstants.VERTICAL);
        separator_5.setBounds(605, 106, 1, 155);
        panel.add(separator_5);

        JSeparator separator_6 = new JSeparator();
        separator_6.setBounds(416, 260, 190, 1);
        panel.add(separator_6);

        JLabel lblA = new JLabel("a =");
        lblA.setBounds(437, 153, 54, 15);
        panel.add(lblA);

        a = new JTextField();
        a.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                //������������  a*1000
                String aStr = a.getText().trim();
                if (!aStr.equals("")) {
                    try {
                        launchf.setText((Double.parseDouble(aStr) * 1000) + "");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(TrainEditPanel.this, "��������ȷ������");
                    }
                }
            }
        });
        a.setColumns(10);
        a.setBounds(467, 150, 102, 21);
        panel.add(a);

        JLabel lblB = new JLabel("b =");
        lblB.setBounds(437, 192, 54, 15);
        panel.add(lblB);

        b = new JTextField();
        b.setColumns(10);
        b.setBounds(467, 189, 102, 21);
        panel.add(b);

        JLabel lblC = new JLabel("c =");
        lblC.setBounds(437, 234, 54, 15);
        panel.add(lblC);

        c = new JTextField();
        c.setColumns(10);
        c.setBounds(467, 231, 102, 21);
        panel.add(c);

        JLabel label_36 = new JLabel("\u7275\u5F15\u7C98\u7740\u7279\u6027\u516C\u5F0F");
        label_36.setBounds(669, 102, 114, 15);
        panel.add(label_36);

        JSeparator separator_12 = new JSeparator();
        separator_12.setBounds(660, 109, 11, 1);
        panel.add(separator_12);

        JSeparator separator_13 = new JSeparator();
        separator_13.setBounds(768, 109, 241, 1);
        panel.add(separator_13);

        JSeparator separator_14 = new JSeparator();
        separator_14.setOrientation(SwingConstants.VERTICAL);
        separator_14.setBounds(1009, 109, 1, 65);
        panel.add(separator_14);

        JSeparator separator_15 = new JSeparator();
        separator_15.setOrientation(SwingConstants.VERTICAL);
        separator_15.setBounds(660, 109, 1, 65);
        panel.add(separator_15);

        JSeparator separator_16 = new JSeparator();
        separator_16.setBounds(660, 174, 350, 1);
        panel.add(separator_16);

        JLabel label_37 = new JLabel("\u5E72\u8F68");
        label_37.setBounds(669, 122, 34, 15);
        panel.add(label_37);

        JLabel label_38 = new JLabel("\u03BC =");
        label_38.setBounds(700, 123, 54, 15);
        panel.add(label_38);

        tu1 = new JTextField();
        tu1.setFont(new Font("����", Font.PLAIN, 12));
        tu1.setColumns(10);
        tu1.setBounds(731, 119, 267, 21);
        panel.add(tu1);

        JLabel label_41 = new JLabel("�г���Ϣ����");
        label_41.setBounds(1064, 59, 77, 15);
        panel.add(label_41);

        infoCollectorText = new JTextArea();
        infoCollectorText.setEditable(false);
        infoCollectorText.setBounds(1064, 80, 221, 230);
        panel.add(infoCollectorText);

        JLabel label_14 = new JLabel("\u7F51\u538B");
        label_14.setBounds(194, 111, 72, 15);
        panel.add(label_14);

        JLabel label_39 = new JLabel("\u6E7F\u8F68");
        label_39.setBounds(669, 150, 34, 15);
        panel.add(label_39);

        JLabel label_40 = new JLabel("\u03BC =");
        label_40.setBounds(700, 151, 54, 15);
        panel.add(label_40);

        tu2 = new JTextField();
        tu2.setFont(new Font("����", Font.PLAIN, 12));
        tu2.setColumns(10);
        tu2.setBounds(731, 147, 267, 21);
        panel.add(tu2);

        JLabel label_42 = new JLabel("\u7535\u5236\u52A8\u7C98\u7740\u7279\u6027\u516C\u5F0F");
        label_42.setBounds(668, 188, 114, 15);
        panel.add(label_42);

        JSeparator separator_18 = new JSeparator();
        separator_18.setBounds(659, 195, 11, 1);
        panel.add(separator_18);

        JSeparator separator_19 = new JSeparator();
        separator_19.setBounds(773, 195, 236, 1);
        panel.add(separator_19);

        JSeparator separator_20 = new JSeparator();
        separator_20.setOrientation(SwingConstants.VERTICAL);
        separator_20.setBounds(1008, 195, 1, 65);
        panel.add(separator_20);

        JSeparator separator_21 = new JSeparator();
        separator_21.setOrientation(SwingConstants.VERTICAL);
        separator_21.setBounds(659, 195, 1, 65);
        panel.add(separator_21);

        JSeparator separator_22 = new JSeparator();
        separator_22.setBounds(659, 260, 350, 1);
        panel.add(separator_22);

        JLabel label_43 = new JLabel("\u5E72\u8F68");
        label_43.setBounds(668, 208, 34, 15);
        panel.add(label_43);

        JLabel label_44 = new JLabel("\u03BC =");
        label_44.setBounds(699, 209, 54, 15);
        panel.add(label_44);

        bu1 = new JTextField();
        bu1.setFont(new Font("����", Font.PLAIN, 12));
        bu1.setColumns(10);
        bu1.setBounds(730, 205, 267, 21);
        panel.add(bu1);

        JLabel label_45 = new JLabel("\u6E7F\u8F68");
        label_45.setBounds(668, 236, 34, 15);
        panel.add(label_45);

        JLabel label_46 = new JLabel("\u03BC =");
        label_46.setBounds(699, 237, 54, 15);
        panel.add(label_46);

        bu2 = new JTextField();
        bu2.setFont(new Font("����", Font.PLAIN, 12));
        bu2.setColumns(10);
        bu2.setBounds(730, 233, 267, 21);
        panel.add(bu2);

        //��ʼ�����ֵ
        initParametersOnPanel();

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "�г���������", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panel_1);
        panel_1.setLayout(null);

        JButton button_1 = new JButton("���");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                trainFormationTablePanel.appendOneLine();
                displayCarsPanel.showCars();
                trainInfoCollector();
            }
        });
        button_1.setBounds(1296, 69, 56, 23);
        panel_1.add(button_1);

        JButton button_2 = new JButton("ɾ��");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                trainFormationTablePanel.removeOneLine();
                displayCarsPanel.showCars();
                trainInfoCollector();
            }
        });
        button_2.setBounds(1296, 102, 56, 23);
        panel_1.add(button_2);

        trainFormationTablePanel.setSize(1276, 182);
        trainFormationTablePanel.setLocation(10, 17);
        panel_1.add(trainFormationTablePanel);

        //��ʾ����ͼƬ
        panel_1.add(displayCarsPanel);
        displayCarsPanel.showCars();

        //�г���Ϣ����
        trainInfoCollector();
    }

    /**
     *  ��ʼ������ϵĲ���
     */
    public void initParametersOnPanel() {
        if (trainIdComboBox.getItemCount() == 0) {
            assignParametersOnPanel(new TrainInfo(), trainFormationTablePanel.generateEightDefaultCars());
        } else {
            int id = (Integer) trainIdComboBox.getSelectedItem();
            assignParametersOnPanel(TrainEditPanelService.getTrainInfoByCategoryId(id), TrainEditPanelService.getTrainFormationByTrainCategoryId(id));
        }
    }

    /**
     *  �����ݱ�train_category��������г����ƣ�����ֵ
     */
    public void getAllTrainCategoryToArray() {
        ArrayList<TrainCategory> tc = TrainEditPanelService.getAllTrainCategory();
        trainCategoryIdArray = new Integer[tc.size()];
        trainCategoryNameArray = new String[tc.size()];
        for (int i = 0; i < tc.size(); i++) {
            trainCategoryIdArray[i] = tc.get(i).getId();
            trainCategoryNameArray[i] = tc.get(i).getName();
        }
    }

    /**
     *  ������ϵĲ�����ֵ
     * @param trainInfo
     * @param trainFormationList
     */
    public void assignParametersOnPanel(TrainInfo trainInfo, ArrayList<TrainFormation> trainFormationList) {
        // 1.�г���Ϣ
        maxV.setText(trainInfo.getMaxV() + "");
        maxEV.setText(trainInfo.getMaxEv() + "");
        conV.setText(trainInfo.getConV() + "");
        powerConf.setText(trainInfo.getPowerConf());
        mzmax.setText(trainInfo.getMzmax() + "");
        a200.setText(trainInfo.getA200() + "");
        mzmin.setText(trainInfo.getMzmin() + "");
        J.setText(trainInfo.getJ() + "");
        slope.setText(trainInfo.getSlope() + "");
        em1.setText(trainInfo.getEm1() + "");
        ar.setText(trainInfo.getAr() + "");
        dv.setText(trainInfo.getDv() + "");
        launchf.setText(trainInfo.getLaunchf() + "");
        powerFac.setText(trainInfo.getPowerFac() + "");
        gearm2.setText(trainInfo.getGearm2() + "");
        M.setText(trainInfo.getM() + "");
        a.setText(trainInfo.getA() + "");
        b.setText(trainInfo.getB() + "");
        c.setText(trainInfo.getC() + "");
        tu1.setText(trainInfo.getTu1());
        tu2.setText(trainInfo.getTu2());
        bu1.setText(trainInfo.getBu1());
        bu2.setText(trainInfo.getBu2());

        // 2.�г�����
        if (trainFormationList.size() == 0) {
            trainFormationList = trainFormationTablePanel.generateEightDefaultCars();
        }
        trainFormationTablePanel.fillLines(trainFormationList);
    }

    /**
     * �½�����
     * @param newTrainCategoryItem
     */
    public static void createTrainCategory(TrainCategory newTrainCategoryItem) {
        TrainCategory newItem = newTrainCategoryItem;
        trainIdComboBox.addItem(newItem.getId());
        trainNameComboBox.addItem(newItem.getName());
    }

    /**
     *  ���泵��
     * @param trainCategoryId
     */
    public void saveTrainInfoAndFormation(int trainCategoryId) {
        // 1.�г���Ϣ����
        //a.У��
        String maxVStr = maxV.getText().trim();
        String maxEVStr = maxEV.getText().trim();
        String conVStr = conV.getText().trim();
        String powerConfStr = powerConf.getText().trim();
        String mzmaxStr = mzmax.getText().trim();
        String a200Str = a200.getText().trim();
        String mzminStr = mzmin.getText().trim();
        String JStr = J.getText().trim();
        String slopeStr = slope.getText().trim();
        String em1Str = em1.getText().trim();
        String arStr = ar.getText().trim();
        String dvStr = dv.getText().trim();
        String launchfStr = launchf.getText().trim();
        String powerFacStr = powerFac.getText().trim();
        String gearm2Str = gearm2.getText().trim();
        String MStr = M.getText().trim();
        String aStr = a.getText().trim();
        String bStr = b.getText().trim();
        String cStr = c.getText().trim();
        String tu1Str = tu1.getText().trim();
        String tu2Str = tu2.getText().trim();
        String bu1Str = bu1.getText().trim();
        String bu2Str = bu2.getText().trim();

        if (!maxVStr.equals("") && !maxEVStr.equals("") && !conVStr.equals("") && !powerConfStr.equals("") && !mzmaxStr.equals("") && !a200Str.equals("")
            && !mzminStr.equals("") && !JStr.equals("") && !slopeStr.equals("") && !em1Str.equals("") && !arStr.equals("") && !dvStr.equals("")
            && !launchfStr.equals("") && !powerFacStr.equals("") && !gearm2Str.equals("") && !MStr.equals("") && !aStr.equals("") && !bStr.equals("")
            && !cStr.equals("") && !tu1Str.equals("") && !tu2Str.equals("") && !bu1Str.equals("") && !bu2Str.equals("")) {
            try {
                double sum = Double.parseDouble(maxVStr) + Double.parseDouble(maxEVStr) + Double.parseDouble(conVStr) + Double.parseDouble(mzmaxStr)
                             + Double.parseDouble(a200Str) + Double.parseDouble(mzminStr) + Double.parseDouble(JStr) + Double.parseDouble(slopeStr)
                             + Double.parseDouble(em1Str) + Double.parseDouble(arStr) + Double.parseDouble(dvStr) + Double.parseDouble(launchfStr)
                             + Double.parseDouble(powerFacStr) + Double.parseDouble(gearm2Str) + Double.parseDouble(MStr) + Double.parseDouble(aStr)
                             + Double.parseDouble(bStr) + Double.parseDouble(cStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "��������ȷ������");
                e.printStackTrace();
            }
            //b.��֯javabean����
            TrainInfo trainInfo = new TrainInfo();
            trainInfo.setMaxV(Double.parseDouble(maxVStr));
            trainInfo.setMaxEv(Double.parseDouble(maxEVStr));
            trainInfo.setConV(Double.parseDouble(conVStr));
            trainInfo.setPowerConf(powerConfStr);
            trainInfo.setMzmax(Double.parseDouble(mzmaxStr));
            trainInfo.setA200(Double.parseDouble(a200Str));
            trainInfo.setMzmin(Double.parseDouble(mzminStr));
            trainInfo.setJ(Double.parseDouble(JStr));
            trainInfo.setSlope(Double.parseDouble(slopeStr));
            trainInfo.setEm1(Double.parseDouble(em1Str));
            trainInfo.setAr(Double.parseDouble(arStr));
            trainInfo.setDv(Double.parseDouble(dvStr));
            trainInfo.setLaunchf(Double.parseDouble(launchfStr));
            trainInfo.setPowerFac(Double.parseDouble(powerFacStr));
            trainInfo.setGearm2(Double.parseDouble(gearm2Str));
            trainInfo.setM(Double.parseDouble(MStr));
            trainInfo.setA(Double.parseDouble(aStr));
            trainInfo.setB(Double.parseDouble(bStr));
            trainInfo.setC(Double.parseDouble(cStr));
            trainInfo.setTu1(tu1Str);
            trainInfo.setTu2(tu2Str);
            trainInfo.setBu1(bu1Str);
            trainInfo.setBu2(bu2Str);

            TrainEditPanelService.saveTrainInfo(trainInfo, trainCategoryId);

            // 2.������Ϣ����
            TrainEditPanelService.saveTrainFormation(trainFormationTablePanel.getTableData(), trainCategoryId);

            JOptionPane.showMessageDialog(this, "����ɹ�!");
        } else {
            JOptionPane.showMessageDialog(this, "��������Ϊ�գ�");
            return;
        }
    }

    /**
     * ɾ���г�����
     * @param trainCategoryId
     * @param index
     */
    public void deleteTrainCategory(int trainCategoryId, int index) {
        TrainEditPanelService.deleteTrainCategory(trainCategoryId);
        //��������JComboBox
        trainNameComboBox.removeItemAt(index);
        trainIdComboBox.removeItemAt(index);
        JOptionPane.showMessageDialog(this, "ɾ���ɹ���");
    }

    /**
     *  ��TestArea��ֵ���г���Ϣ����
     */
    public static void trainInfoCollector() {
        infoCollectorText.setText("");
        String trainName = (String) trainNameComboBox.getSelectedItem();
        infoCollectorText.append("�г����ƣ�" + trainName + "\r\n");
        int rowCount = TrainFormationTablePanel.trainFormationTableModel.getRowCount();
        infoCollectorText.append("���鵥Ԫ������" + rowCount + "\r\n");
        infoCollectorText.append("�г���������������" + rowCount + "\r\n");
        double sumTrainWeight = 0, sumEmptyTrainWeight = 0;
        int sumPassage = 0;//ÿ���˿�80kg
        String maxSpeed;
        for (int i = 0; i < rowCount; i++) {
            sumEmptyTrainWeight += Double.parseDouble((String) TrainFormationTablePanel.trainFormationTableModel.getValueAt(i, 13));
            try {
                sumPassage += Integer.parseInt((String) TrainFormationTablePanel.trainFormationTableModel.getValueAt(i, 12));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "��������ȷ�����֣�");
                return;
            }
        }
        sumTrainWeight = sumEmptyTrainWeight + sumPassage * 0.08;
        M.setText(MyTools.numFormat(sumTrainWeight));//���г�������ֵ
        maxSpeed = maxV.getText().trim();
        infoCollectorText.append("�г����أ�" + MyTools.numFormat(sumTrainWeight) + "t" + "\r\n");
        infoCollectorText.append("��Ա����" + sumPassage + "\r\n");
        infoCollectorText.append("�ճ�������" + MyTools.numFormat(sumEmptyTrainWeight) + "t" + "\r\n");
        infoCollectorText.append("�г���������ٶȣ�" + maxSpeed + "km/h" + "\r\n");
    }

    /**
     * У�顰�������÷�ʽ�������г��������Ƿ�һ��
     * @return
     */
    public boolean checkCarTypeConf() {
        boolean b = false;
        int rowCount = TrainFormationTablePanel.trainFormationTableModel.getRowCount();
        int m = 0, n = 0;//mΪ����������nΪ�ϳ�����
        for (int i = 0; i < rowCount; i++) {
            String carType = (String) TrainFormationTablePanel.trainFormationTableModel.getValueAt(i, 2);
            if (carType.equals("����")) {
                m++;
            } else if (carType.equals("�ϳ�")) {
                n++;
            }
        }
        String[] inputCarType = MyTools.getNumStrFromStr(powerConf.getText().trim()).split(",");
        if (Integer.parseInt(inputCarType[0]) == m && Integer.parseInt(inputCarType[1]) == n) {
            b = true;
        }
        return b;
    }

}

/**
 *  �����г��༭���档�г����������е�table
 * @author huhui
 *
 */
class TrainFormationTablePanel extends JPanel {

    private JTable                  trainFormationTable;
    private Vector<String>          columnNamesVector;                                                                             // ����
    private JScrollPane             trainFormationTableScrollPane;
    public static DefaultTableModel trainFormationTableModel;
    private String[]                trainTypeArray  = { "����", "�ϳ�" };
    private String[]                trainClassArray = { "VIPͷ��", "���̳�", "˫������ʽVIP��", "�๦�ܲͳ�/���������쳵", "˫����ȳ�", "�Ǽʳ�����", "һ����ͷ��" };

    public TrainFormationTablePanel() {
        this.setLayout(new GridLayout(1, 1, 0, 0));
        this.initTrainInfoTable();
        this.add(trainFormationTableScrollPane);
        this.setSize(680, 750);
        this.setVisible(true);
    }

    // ��ʼ��trainInfoTable
    public void initTrainInfoTable() {
        columnNamesVector = new Vector<String>();
        columnNamesVector.add("���鵥Ԫ");
        columnNamesVector.add("�������");
        columnNamesVector.add("��������");
        columnNamesVector.add("����");
        columnNamesVector.add("���峤��(m)");
        columnNamesVector.add("������(m)");
        columnNamesVector.add("����߶�(m)");
        columnNamesVector.add("ת������(m)");
        columnNamesVector.add("����");
        columnNamesVector.add("������");
        columnNamesVector.add("�������(t)");
        columnNamesVector.add("��������(m)");
        columnNamesVector.add("��Ա(��)");
        columnNamesVector.add("����ʱ����(t)");

        trainFormationTableModel = new DefaultTableModel(columnNamesVector, 0) {
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return true;
            }
        };
        trainFormationTable = new JTable(trainFormationTableModel);
        trainFormationTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    TrainEditPanel.trainInfoCollector();
                }
            }
        });
        trainFormationTable.putClientProperty("terminateEditOnFocusLost", true);
        trainFormationTableScrollPane = new JScrollPane(trainFormationTable);

        // ����table�п�
        trainFormationTable.getColumnModel().getColumn(7).setPreferredWidth(79);
        trainFormationTable.getColumnModel().getColumn(13).setPreferredWidth(79);

        // ��table�����JComboBox
        JComboBox<String> trainTypeComboBox = new JComboBox<String>(trainTypeArray);
        trainFormationTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(trainTypeComboBox));
        JComboBox<String> trainClassComboBox = new JComboBox<String>(trainClassArray);
        trainFormationTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(trainClassComboBox));
    }

    /**
     *  ���һ��
     */
    public void appendOneLine() {
        TrainFormation trainFormation = new TrainFormation(0, 0, 2);
        Vector<String> line = new Vector<String>();
        line.add(trainFormation.getUnitNo() + "");
        line.add(trainFormation.getCarriageNo() + "");
        line.add(trainFormation.getCarriageType());
        line.add(trainFormation.getCarriageCategory());
        line.add(trainFormation.getLength() + "");
        line.add(trainFormation.getWidth() + "");
        line.add(trainFormation.getHeight() + "");
        line.add(trainFormation.getAxleLength() + "");
        line.add(trainFormation.getAxleNum() + "");
        line.add(trainFormation.getDynamicAxleNum() + "");
        line.add(trainFormation.getAxleWeight() + "");
        line.add(trainFormation.getCarriageDis() + "");
        line.add(trainFormation.getPassenger() + "");
        line.add(trainFormation.getCarriageWeight() + "");

        trainFormationTableModel.addRow(line);
        int rowCount = trainFormationTable.getRowCount();
        Rectangle rect = trainFormationTable.getCellRect(rowCount - 1, 0, true);
        trainFormationTable.scrollRectToVisible(rect);

        //�������ӵĳ���ŵ������ڶ���λ��
        trainFormationTableModel.moveRow(rowCount - 2, rowCount - 2, rowCount - 1);
        //���á����鵥Ԫ��
        resetUnitNo();
    }

    /**
     *  ɾ��һ��
     */
    public void removeOneLine() {
        int selectedIndex = trainFormationTable.getSelectedRow();
        int rowCount = trainFormationTable.getRowCount();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(TrainFormationTablePanel.this, "��ѡ��һ����ɾ����");
            return;
        }
        if (selectedIndex == 0 || selectedIndex == rowCount - 1) {
            JOptionPane.showMessageDialog(TrainFormationTablePanel.this, "��β���в���ɾ����");
            return;
        }
        int option = JOptionPane.showConfirmDialog(TrainFormationTablePanel.this, "ȷ��ɾ����");
        if (option == 0) {
            trainFormationTableModel.removeRow(selectedIndex);
            resetUnitNo();
        }
    }

    /**
     * �������ñ��鵥Ԫ��ֵ
     */
    public void resetUnitNo() {
        int rowCount = trainFormationTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            trainFormationTableModel.setValueAt((i + 1) + "", i, 0);
        }
    }

    /**
     *  ��trainFormation��Ϣ����ڱ����
     * @param trainFormationList
     */
    public void fillLines(ArrayList<TrainFormation> trainFormationList) {
        trainFormationTableModel.setRowCount(0);//����ձ��
        for (TrainFormation trainFormation : trainFormationList) {
            Vector<String> line = new Vector<String>();
            line.add(trainFormation.getUnitNo() + "");
            line.add(trainFormation.getCarriageNo() + "");
            line.add(trainFormation.getCarriageType());
            line.add(trainFormation.getCarriageCategory());
            line.add(trainFormation.getLength() + "");
            line.add(trainFormation.getWidth() + "");
            line.add(trainFormation.getHeight() + "");
            line.add(trainFormation.getAxleLength() + "");
            line.add(trainFormation.getAxleNum() + "");
            line.add(trainFormation.getDynamicAxleNum() + "");
            line.add(trainFormation.getAxleWeight() + "");
            line.add(trainFormation.getCarriageDis() + "");
            line.add(trainFormation.getPassenger() + "");
            line.add(trainFormation.getCarriageWeight() + "");

            trainFormationTableModel.addRow(line);
            int rowCount = trainFormationTable.getRowCount();
            Rectangle rect = trainFormationTable.getCellRect(rowCount - 1, 0, true);
            trainFormationTable.scrollRectToVisible(rect);
        }
    }

    /**
     * ��ʼ��8�ڱ����г�
     * @return
     */
    public ArrayList<TrainFormation> generateEightDefaultCars() {
        ArrayList<TrainFormation> list = new ArrayList<TrainFormation>();
        //��1��Ϊ��ͷ
        TrainFormation car_1 = new TrainFormation(1, 1, 0);
        list.add(car_1);
        //��2��3Ϊ���ȳ�
        TrainFormation car_2 = new TrainFormation(2, 2, 1);
        TrainFormation car_3 = new TrainFormation(3, 3, 2);
        list.add(car_2);
        list.add(car_3);
        //��4��Ϊ�ͳ�
        TrainFormation car_4 = new TrainFormation(4, 4, 3);
        list.add(car_4);
        //��5��Ϊһ�ȳ�
        TrainFormation car_5 = new TrainFormation(5, 5, 1);
        list.add(car_5);
        //��6��7Ϊ���ȳ�
        TrainFormation car_6 = new TrainFormation(6, 6, 2);
        TrainFormation car_7 = new TrainFormation(7, 7, 2);
        list.add(car_6);
        list.add(car_7);
        //��8��Ϊ��ͷ
        TrainFormation car_8 = new TrainFormation(8, 8, 0);
        list.add(car_8);

        return list;
    }

    /**
     * ��ȡ����trainFormationTable������
     * @return
     */
    public ArrayList<TrainFormation> getTableData() {
        if (trainFormationTableModel.getColumnCount() == 0) {
            return null;
        } else {
            //У��JTable����
            for (int m = 0; m < trainFormationTableModel.getRowCount(); m++) {
                for (int n = 0; n < 14 && n != 2 && n != 3; n++) {
                    String cellStr = ((Vector) trainFormationTableModel.getDataVector().elementAt(m)).elementAt(n).toString().trim();
                    if (cellStr.equals("")) {
                        JOptionPane.showMessageDialog(TrainFormationTablePanel.this, "��������Ϊ��");
                        return null;
                    }
                }
            }

            ArrayList<TrainFormation> list = new ArrayList<TrainFormation>();
            try {
                for (int i = 0; i < trainFormationTableModel.getRowCount(); i++) {
                    TrainFormation trainFormation = new TrainFormation();
                    Vector line = (Vector) trainFormationTableModel.getDataVector().elementAt(i);
                    trainFormation.setUnitNo(Integer.parseInt(line.get(0).toString().trim()));
                    trainFormation.setCarriageNo(Integer.parseInt(line.get(1).toString().trim()));
                    trainFormation.setCarriageType(line.get(2).toString().trim());
                    trainFormation.setCarriageCategory(line.get(3).toString().trim());
                    trainFormation.setLength(Double.parseDouble(line.get(4).toString().trim()));
                    trainFormation.setWidth(Double.parseDouble(line.get(5).toString().trim()));
                    trainFormation.setHeight(Double.parseDouble(line.get(6).toString().trim()));
                    trainFormation.setAxleLength(Double.parseDouble(line.get(7).toString().trim()));
                    trainFormation.setAxleNum(Integer.parseInt(line.get(8).toString().trim()));
                    trainFormation.setDynamicAxleNum(Integer.parseInt(line.get(9).toString().trim()));
                    trainFormation.setAxleWeight(Double.parseDouble(line.get(10).toString().trim()));
                    trainFormation.setCarriageDis(Double.parseDouble(line.get(11).toString().trim()));
                    trainFormation.setPassenger(Integer.parseInt(line.get(12).toString().trim()));
                    trainFormation.setCarriageWeight(Double.parseDouble(line.get(13).toString().trim()));
                    trainFormation.setSumDynamicAxle(getDynamicAxleNum());
                    list.add(trainFormation);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(TrainFormationTablePanel.this, "��������ȷ������");
                return null;
            }
            return list;
        }
    }

    /**
     * �����г��������
     * @return ����� = �����Ķ�����Ŀ֮��
     */
    public int getDynamicAxleNum() {
        int num = 0;
        for (int m = 0; m < trainFormationTableModel.getRowCount(); m++) {
            String cellStr = ((Vector) trainFormationTableModel.getDataVector().elementAt(m)).elementAt(2).toString().trim();
            if (cellStr.equals("����")) {
                int n = Integer.parseInt(((Vector) trainFormationTableModel.getDataVector().elementAt(m)).elementAt(9).toString().trim());
                num += n;
            }
        }
        return num;
    }

}

/**
 * �����г��༭���档��ʾ�г�����ͼƬ
 * @author huhui
 *
 */
class DisplayCarsPanel extends JPanel {

    public DisplayCarsPanel() {
        setLayout(null);
        setBorder(new LineBorder(Color.LIGHT_GRAY));
        setBackground(Color.white);
        setBounds(132, 206, 1220, 112);
    }

    /**
     * ��ʾ���г����ͼƬ
     */
    public void showCars() {
        //���Ƴ�����ͼƬ
        this.removeAll();
        this.repaint();
        int rowCount = TrainFormationTablePanel.trainFormationTableModel.getRowCount();
        //��߳�ͷ
        JLabel tlLabel = new JLabel();
        tlLabel.setBounds(10, 10, 96, 21);
        tlLabel.setIcon(new ImageIcon(Panel.class.getResource("/images/tl.jpg")));
        add(tlLabel);
        //�м�����г�   "VIPͷ��", "���̳�", "˫������ʽVIP��", "�๦�ܲͳ�/���������쳵", "˫����ȳ�", "�Ǽʳ�����", "һ����ͷ��"
        for (int i = 1; i < rowCount - 1; i++) {
            String cellValue = (String) TrainFormationTablePanel.trainFormationTableModel.getValueAt(i, 3);
            String picFileName = "";
            if (cellValue.equals("���̳�") || cellValue.equals("˫������ʽVIP��") || cellValue.equals("˫����ȳ�")) {
                picFileName = "1.jpg";
            } else if (cellValue.equals("�Ǽʳ�����")) {
                picFileName = "2.jpg";
            } else if (cellValue.equals("�๦�ܲͳ�/���������쳵")) {
                picFileName = "canche.jpg";
            }
            JLabel picLabel = new JLabel();
            picLabel.setBounds(10 + 96 * (i % 12), (i / 12) * 23 + 10, 96, 21);//ÿ�з�12��ͼƬ�����23������
            picLabel.setIcon(new ImageIcon(Panel.class.getResource("/images/" + picFileName)));
            add(picLabel);
        }
        //�ұ߳�ͷ
        JLabel trLabel = new JLabel();
        trLabel.setBounds(10 + 96 * ((rowCount - 1) % 12), ((rowCount - 1) / 12) * 23 + 10, 96, 21);
        trLabel.setIcon(new ImageIcon(Panel.class.getResource("/images/tr.jpg")));
        add(trLabel);
    }

}

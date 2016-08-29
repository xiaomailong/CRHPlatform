package com.crh.view.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.crh.service.TractionConfPanelService;
import com.crh.view.panel.TractionConfPanel;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.util.MyTools;
import com.crh2.util.MyUtillity;

/**
 * 列车牵引配置信息对话框
 * @author huhui
 *
 */
public class TractionParameterDialog extends JDialog {
    private JTextField k1;
    private JTextField k2;
    private JTextField D;
    private JTextField N0;
    private JTextField N2;
    private JTextField N;
    private JTextField T;
    private JTextField P0;
    private JTextField P1;
    private JTextField v1;
    private JTextField v2;
    private JTextField F1;
    private JTextField F2;
    private JTextField Fst;
    //电流参数
    private JTextField vi1;
    private JTextField I1;
    private JTextField vi2;
    private JTextField I2;
    private JTextField vi3;
    private JTextField I3;

    private int        categoryId = 0;
    private int        tractionId = 0;
    private int        type       = 0;
    private JTextField vu1;
    private JTextField u1;
    private JTextField vu2;
    private JTextField u2;
    private JTextField vu3;
    private JTextField u3;
    private JTextField D0;
    private JTextField D1;

    public TractionParameterDialog(JPanel owner, int trainCategoryId, int tractionId, String trainCategory, int type) {
        super((Frame) SwingUtilities.windowForComponent(owner));
        this.tractionId = tractionId;
        this.categoryId = trainCategoryId;
        this.type = type;
        setSize(884, 548);
        setTitle("牵引特性配置——列车编组名称：" + trainCategory);
        MyUtillity.setFrameOnCenter(this);
        getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(10, 10, 848, 242);
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u7275\u5F15\u7279\u6027\u8BBE\u7F6E", TitledBorder.LEADING,
            TitledBorder.TOP, null, null));
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel label = new JLabel("\u4F20\u52A8\u88C5\u7F6E\u6548\u7387");
        label.setBounds(28, 25, 96, 21);
        panel.add(label);

        k1 = new JTextField();
        k1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                calculateFst();
            }
        });
        k1.setBounds(134, 26, 92, 21);
        panel.add(k1);
        k1.setColumns(10);

        JLabel label_1 = new JLabel("\u9F7F\u8F6E\u4F20\u52A8\u6BD4");
        label_1.setBounds(28, 67, 96, 21);
        panel.add(label_1);

        k2 = new JTextField();
        k2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateFst();
            }
        });
        k2.setColumns(10);
        k2.setBounds(134, 65, 92, 21);
        panel.add(k2);

        JLabel label_2 = new JLabel("\u8F6E\u5F84");
        label_2.setBounds(28, 112, 72, 21);
        panel.add(label_2);

        D = new JTextField();
        D.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateFst();
            }
        });
        D.setColumns(10);
        D.setBounds(134, 112, 92, 21);
        panel.add(D);

        JLabel label_3 = new JLabel("\u52A8\u8F66\u6570\u76EE");
        label_3.setBounds(300, 210, 72, 21);
        panel.add(label_3);

        N0 = new JTextField();
        N0.setEditable(false);
        N0.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateN();
                calculateP1();
                calculateF1();
                calculateF2();
                calculateFst();
            }
        });
        N0.setColumns(10);
        N0.setBounds(410, 210, 92, 21);
        panel.add(N0);

        JLabel label_4 = new JLabel("每个车的电机数量");
        //		label_4.setBounds(300, 28, 128, 21);
        //		panel.add(label_4);

        N2 = new JTextField();
        N2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateN();
                calculateP1();
                calculateF1();
                calculateF2();
                calculateFst();
            }
        });
        N2.setColumns(10);
        //		N2.setBounds(410, 25, 92, 21);
        //		panel.add(N2);

        JLabel label_5 = new JLabel("\u6574\u8F66\u7684\u7535\u673A\u6570\u91CF");
        label_5.setBounds(300, 23, 128, 21);
        panel.add(label_5);

        N = new JTextField();
        N.setEditable(false);
        N.setColumns(10);
        N.setBounds(410, 24, 92, 21);
        panel.add(N);

        JLabel label_6 = new JLabel("\u7535\u673A\u542F\u52A8\u8F6C\u77E9");
        label_6.setBounds(300, 68, 121, 21);
        panel.add(label_6);

        T = new JTextField();
        T.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateFst();
            }
        });
        T.setColumns(10);
        T.setBounds(410, 69, 92, 21);
        panel.add(T);

        JLabel lbln = new JLabel("\u6BCF\u53F0\u7535\u673A\u6700\u5927\u8F6E");
        lbln.setBounds(300, 103, 144, 21);
        panel.add(lbln);

        P0 = new JTextField();
        P0.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateP1();
                calculateF1();
                calculateF2();
            }
        });
        P0.setColumns(10);
        P0.setBounds(410, 119, 92, 21);
        panel.add(P0);

        JLabel label_7 = new JLabel("\u5468\u7275\u5F15\u529F\u7387");
        label_7.setBounds(300, 125, 104, 21);
        panel.add(label_7);

        JLabel label_8 = new JLabel("\u5217\u8F66\u6700\u5927\u8F6E\u5468\u7275\u5F15\u529F\u7387");
        label_8.setBounds(576, 29, 193, 21);
        panel.add(label_8);

        P1 = new JTextField();
        P1.setEditable(false);
        P1.setColumns(10);
        P1.setBounds(713, 25, 92, 21);
        panel.add(P1);

        JLabel label_9 = new JLabel("\u6052\u529F\u7387\u8F93\u51FA\u5BF9\u5E94");
        label_9.setBounds(576, 65, 169, 21);
        panel.add(label_9);

        v1 = new JTextField();
        v1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateF1();
            }
        });
        v1.setColumns(10);
        v1.setBounds(713, 67, 92, 21);
        panel.add(v1);

        JLabel label_10 = new JLabel("\u5F31\u78C1\u5207\u6362\u901F\u5EA6");
        label_10.setBounds(576, 112, 127, 21);
        panel.add(label_10);

        v2 = new JTextField();
        v2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateF2();
            }
        });
        v2.setColumns(10);
        v2.setBounds(713, 109, 92, 21);
        panel.add(v2);

        JLabel label_11 = new JLabel("\u6052\u529F\u533A\u548C\u6052\u8F6C\u77E9\u533A\u8F6C\u6298");
        label_11.setBounds(576, 148, 169, 21);
        panel.add(label_11);

        F1 = new JTextField();
        F1.setEditable(false);
        F1.setColumns(10);
        F1.setBounds(713, 157, 92, 21);
        panel.add(F1);

        JLabel label_12 = new JLabel("\u70B9\u5BF9\u5E94\u7275\u5F15\u529B");
        label_12.setBounds(576, 168, 96, 21);
        panel.add(label_12);

        JLabel label_13 = new JLabel("\u5F31\u78C1\u70B9\u5BF9\u5E94\u7275\u5F15\u529B");
        label_13.setBounds(576, 210, 139, 21);
        panel.add(label_13);

        F2 = new JTextField();
        F2.setEditable(false);
        F2.setColumns(10);
        F2.setBounds(713, 210, 92, 21);
        panel.add(F2);

        JLabel label_14 = new JLabel("\u542F\u52A8\u7275\u5F15\u529B");
        label_14.setBounds(300, 157, 127, 21);
        panel.add(label_14);

        Fst = new JTextField();
        Fst.setEditable(false);
        Fst.setColumns(10);
        Fst.setBounds(410, 157, 92, 21);
        panel.add(Fst);

        JLabel lblM = new JLabel("m");
        lblM.setBounds(236, 116, 54, 15);
        panel.add(lblM);

        JLabel lblNm = new JLabel("NM");
        lblNm.setBounds(512, 72, 54, 15);
        panel.add(lblNm);

        JLabel lblKw = new JLabel("kw");
        lblKw.setBounds(512, 122, 54, 15);
        panel.add(lblKw);

        JLabel label_15 = new JLabel("kw");
        label_15.setBounds(815, 29, 54, 15);
        panel.add(label_15);

        JLabel lblKmh = new JLabel("km/h");
        lblKmh.setBounds(815, 71, 54, 15);
        panel.add(lblKmh);

        JLabel label_16 = new JLabel("km/h");
        label_16.setBounds(815, 116, 54, 15);
        panel.add(label_16);

        JLabel lblKn = new JLabel("KN");
        lblKn.setBounds(815, 161, 54, 15);
        panel.add(lblKn);

        JLabel label_17 = new JLabel("KN");
        label_17.setBounds(815, 213, 54, 15);
        panel.add(label_17);

        JLabel label_18 = new JLabel("KN");
        label_18.setBounds(512, 160, 54, 15);
        panel.add(label_18);

        JLabel label_25 = new JLabel("\u6700\u5C0F\u901F\u5EA6");
        label_25.setBounds(576, 85, 54, 15);
        panel.add(label_25);

        JLabel label_26 = new JLabel("\u65B0\u8F6E");
        label_26.setBounds(28, 160, 54, 15);
        panel.add(label_26);

        D0 = new JTextField();
        D0.setText("0.92");
        D0.setBounds(134, 157, 92, 21);
        panel.add(D0);
        D0.setColumns(10);

        JLabel label_27 = new JLabel("m");
        label_27.setBounds(236, 160, 54, 15);
        panel.add(label_27);

        JLabel label_28 = new JLabel("\u5168\u78E8\u635F");
        label_28.setBounds(28, 213, 54, 15);
        panel.add(label_28);

        D1 = new JTextField();
        D1.setText("0.83");
        D1.setColumns(10);
        D1.setBounds(134, 210, 92, 21);
        panel.add(D1);

        JLabel label_29 = new JLabel("m");
        label_29.setBounds(236, 213, 54, 15);
        panel.add(label_29);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(10, 262, 848, 93);
        panel_1.setBorder(new TitledBorder(null, "\u7535\u6D41\u7279\u6027\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JLabel lblV = new JLabel("v1");
        lblV.setBounds(98, 25, 22, 15);
        panel_1.add(lblV);

        JLabel lblI = new JLabel("I1");
        lblI.setBounds(98, 68, 22, 15);
        panel_1.add(lblI);

        vi1 = new JTextField();
        vi1.setBounds(119, 22, 84, 21);
        panel_1.add(vi1);
        vi1.setColumns(10);

        I1 = new JTextField();
        I1.setColumns(10);
        I1.setBounds(119, 65, 84, 21);
        panel_1.add(I1);

        JLabel lblV_1 = new JLabel("v2");
        lblV_1.setBounds(352, 25, 22, 15);
        panel_1.add(lblV_1);

        JLabel lblI_1 = new JLabel("I2");
        lblI_1.setBounds(352, 68, 22, 15);
        panel_1.add(lblI_1);

        vi2 = new JTextField();
        vi2.setColumns(10);
        vi2.setBounds(373, 23, 84, 21);
        panel_1.add(vi2);

        I2 = new JTextField();
        I2.setColumns(10);
        I2.setBounds(373, 65, 84, 21);
        panel_1.add(I2);

        JLabel lblV_2 = new JLabel("v3");
        lblV_2.setBounds(613, 25, 22, 15);
        panel_1.add(lblV_2);

        JLabel lblI_2 = new JLabel("I3");
        lblI_2.setBounds(613, 68, 22, 15);
        panel_1.add(lblI_2);

        vi3 = new JTextField();
        vi3.setColumns(10);
        vi3.setBounds(634, 23, 84, 21);
        panel_1.add(vi3);

        I3 = new JTextField();
        I3.setColumns(10);
        I3.setBounds(634, 65, 84, 21);
        panel_1.add(I3);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(null);
        panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u7535\u538B\u7279\u6027\u8BBE\u7F6E", TitledBorder.LEADING,
            TitledBorder.TOP, null, null));
        panel_2.setBounds(10, 365, 848, 93);
        getContentPane().add(panel_2);

        JLabel label_19 = new JLabel("\u542F\u52A8\u901F\u5EA6");
        label_19.setBounds(58, 25, 62, 15);
        panel_2.add(label_19);

        vu1 = new JTextField();
        vu1.setText("11.8");
        vu1.setColumns(10);
        vu1.setBounds(119, 22, 84, 21);
        panel_2.add(vu1);

        u1 = new JTextField();
        u1.setText("216.0");
        u1.setColumns(10);
        u1.setBounds(119, 65, 84, 21);
        panel_2.add(u1);

        JLabel label_21 = new JLabel("\u5236\u52A8\u65F6\u7EC8\u7AEF\u901F\u5EA6");
        label_21.setBounds(276, 25, 84, 15);
        panel_2.add(label_21);

        vu2 = new JTextField();
        vu2.setText("118.0");
        vu2.setColumns(10);
        vu2.setBounds(373, 23, 84, 21);
        panel_2.add(vu2);

        u2 = new JTextField();
        u2.setText("198.0");
        u2.setColumns(10);
        u2.setBounds(373, 65, 84, 21);
        panel_2.add(u2);

        vu3 = new JTextField();
        vu3.setText("212.0");
        vu3.setColumns(10);
        vu3.setBounds(634, 23, 84, 21);
        panel_2.add(vu3);

        u3 = new JTextField();
        u3.setText("136.0");
        u3.setColumns(10);
        u3.setBounds(634, 65, 84, 21);
        panel_2.add(u3);

        JLabel label_20 = new JLabel("\u8865\u507F\u7535\u538B1");
        label_20.setBounds(58, 68, 62, 15);
        panel_2.add(label_20);

        JLabel label_22 = new JLabel("\u8865\u507F\u7535\u538B2");
        label_22.setBounds(313, 68, 62, 15);
        panel_2.add(label_22);

        JLabel label_23 = new JLabel("\u7EC8\u7AEF\u901F\u5EA6");
        label_23.setBounds(575, 25, 62, 15);
        panel_2.add(label_23);

        JLabel label_24 = new JLabel("\u7EC8\u7AEF\u7535\u538B");
        label_24.setBounds(575, 68, 62, 15);
        panel_2.add(label_24);

        JButton button = new JButton("另存为新方案");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveTractionParameter(categoryId, 1);
            }
        });
        button.setBounds(188, 477, 104, 23);
        getContentPane().add(button);

        JButton button_1 = new JButton("取消");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        button_1.setBounds(552, 477, 93, 23);
        getContentPane().add(button_1);

        JButton button_3 = new JButton("修改");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveTractionParameter(categoryId, 0);
            }
        });
        button_3.setBounds(374, 477, 93, 23);
        getContentPane().add(button_3);

        // 填充参数
        assignParametersById(tractionId, trainCategoryId);

        setModal(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    /**
     * 保存trainCategoryId的TractionParameterConf
     * @param trainCategoryId
     * @param actionType
     */
    public void saveTractionParameter(int trainCategoryId, int actionType) {
        // 检验空值
        String k1Str = k1.getText().trim();
        String k2Str = k2.getText().trim();
        String DStr = D.getText().trim();
        String N0Str = N0.getText().trim();
        String N2Str = N2.getText().trim();
        String NStr = N.getText().trim();
        String TStr = T.getText().trim();
        String P0Str = P0.getText().trim();
        String P1Str = P1.getText().trim();
        String v1Str = v1.getText().trim();
        String v2Str = v2.getText().trim();
        String F1Str = F1.getText().trim();
        String F2Str = F2.getText().trim();
        String vi1Str = vi1.getText().trim();
        String I1Str = I1.getText().trim();
        String vi2Str = vi2.getText().trim();
        String I2Str = I1.getText().trim();
        String vi3Str = vi3.getText().trim();
        String I3Str = I1.getText().trim();
        String vu1Str = vu1.getText().trim();
        String u1Str = u2.getText().trim();
        String vu2Str = vu1.getText().trim();
        String u2Str = u2.getText().trim();
        String vu3Str = vu1.getText().trim();
        String u3Str = u2.getText().trim();
        String FstStr = Fst.getText().trim();
        //2014.11.29增加“新轮”、“全磨损”
        String D0Str = D0.getText().trim();
        String D1Str = D1.getText().trim();

        if (k1Str.equals("") || k2Str.equals("") || DStr.equals("") || N0Str.equals("") || N2Str.equals("") || NStr.equals("") || TStr.equals("")
            || P0Str.equals("") || P1Str.equals("") || v1Str.equals("") || v2Str.equals("") || F1Str.equals("") || F2Str.equals("") || vi1Str.equals("")
            || vi2Str.equals("") || vi3Str.equals("") || I1Str.equals("") || I2Str.equals("") || I3Str.equals("") || vu1Str.equals("") || vu2Str.equals("")
            || vu3Str.equals("") || u1Str.equals("") || u2Str.equals("") || u3Str.equals("") || FstStr.equals("") || D0Str.equals("") || D1Str.equals("")) {
            JOptionPane.showMessageDialog(this, "参数不能为空！");
            return;
        }
        TrainTractionConf trainTractionConf = new TrainTractionConf();
        trainTractionConf.setK1(Double.parseDouble(k1.getText().trim()));
        trainTractionConf.setK2(Double.parseDouble(k2.getText().trim()));
        trainTractionConf.setD(Double.parseDouble(D.getText().trim()));
        trainTractionConf.setN0(Integer.parseInt(N0.getText().trim()));
        trainTractionConf.setN2(Integer.parseInt(N2.getText().trim()));
        trainTractionConf.setN(Integer.parseInt(N.getText().trim()));
        trainTractionConf.setT(Double.parseDouble(T.getText().trim()));
        trainTractionConf.setP0(Double.parseDouble(P0.getText().trim()));
        trainTractionConf.setP1(Double.parseDouble(P1.getText().trim()));
        trainTractionConf.setV1(Double.parseDouble(v1.getText().trim()));
        trainTractionConf.setV2(Double.parseDouble(v2.getText().trim()));
        trainTractionConf.setF1(Double.parseDouble(F1.getText().trim()));
        trainTractionConf.setF2(Double.parseDouble(F2.getText().trim()));
        trainTractionConf.setVi1(Double.parseDouble(vi1.getText().trim()));
        trainTractionConf.setI1(Double.parseDouble(I1.getText().trim()));
        trainTractionConf.setVi2(Double.parseDouble(vi2.getText().trim()));
        trainTractionConf.setI2(Double.parseDouble(I2.getText().trim()));
        trainTractionConf.setVi3(Double.parseDouble(vi3.getText().trim()));
        trainTractionConf.setI3(Double.parseDouble(I3.getText().trim()));
        trainTractionConf.setFst(Double.parseDouble(Fst.getText().trim()));
        trainTractionConf.setVu1(Double.parseDouble(vu1.getText().trim()));
        trainTractionConf.setU1(Double.parseDouble(u1.getText().trim()));
        trainTractionConf.setVu2(Double.parseDouble(vu2.getText().trim()));
        trainTractionConf.setU2(Double.parseDouble(u2.getText().trim()));
        trainTractionConf.setVu3(Double.parseDouble(vu3.getText().trim()));
        trainTractionConf.setU3(Double.parseDouble(u3.getText().trim()));
        trainTractionConf.setTrainCategoryId(trainCategoryId);
        trainTractionConf.setD0(Double.parseDouble(D0.getText().trim()));
        trainTractionConf.setD1(Double.parseDouble(D1.getText().trim()));

        boolean b = TractionConfPanelService.saveTrainTractionConf(trainTractionConf, trainCategoryId, tractionId, actionType, type);
        if (b) {
            TractionConfPanel.refresh.doClick();//刷新面板
            JOptionPane.showMessageDialog(this, "保存成功！");
        } else {
            JOptionPane.showMessageDialog(this, "保存失败！原因：设计已达到上限");
        }
    }

    /**
     *  根据trainCategorId得到对应的TractionParameterConf，并显示在Dialog中
     * @param tractionId
     * @param trainCategoryId
     */
    public void assignParametersById(int tractionId, int trainCategoryId) {
        TrainTractionConf trainTractionConf = TractionConfPanelService.getTrainTractionConf(tractionId, trainCategoryId);
        // 给各文本框赋值
        k1.setText(trainTractionConf.getK1() + "");
        k2.setText(trainTractionConf.getK2() + "");
        D.setText(trainTractionConf.getD() + "");
        N0.setText(TractionConfPanelService.getDynamicTrainNum(trainCategoryId) + "");
        N2.setText(trainTractionConf.getN2() + "");
        N.setText(TractionConfPanelService.getSumDynamicAxleNum(trainCategoryId) + "");
        T.setText(trainTractionConf.getT() + "");
        P0.setText(trainTractionConf.getP0() + "");
        P1.setText(trainTractionConf.getP1() + "");
        v1.setText(trainTractionConf.getV1() + "");
        v2.setText(trainTractionConf.getV2() + "");
        F1.setText(trainTractionConf.getF1() + "");
        F2.setText(trainTractionConf.getF2() + "");
        vi1.setText(trainTractionConf.getVi1() + "");
        I1.setText(trainTractionConf.getI1() + "");
        vi2.setText(trainTractionConf.getVi2() + "");
        I2.setText(trainTractionConf.getI2() + "");
        vi3.setText(trainTractionConf.getVi3() + "");
        I3.setText(trainTractionConf.getI3() + "");
        vu1.setText(trainTractionConf.getVu1() + "");
        u1.setText(trainTractionConf.getU1() + "");
        vu2.setText(trainTractionConf.getVu2() + "");
        u2.setText(trainTractionConf.getU2() + "");
        vu3.setText(trainTractionConf.getVu3() + "");
        u3.setText(trainTractionConf.getU3() + "");

        Fst.setText(trainTractionConf.getFst() + "");

        D0.setText(trainTractionConf.getD0() + "");
        D1.setText(trainTractionConf.getD1() + "");
    }

    /**
     * 牵引特性部分：计算整车的电机数量：N=N0*N2
     */
    public void calculateN() {
        if (!N0.getText().trim().equals("") && !N2.getText().trim().equals("")) {
            try {
                N.setText((Integer.parseInt(N0.getText().trim()) * Integer.parseInt(N2.getText().trim())) + "");
            } catch (NumberFormatException e) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(this, "请输入正确的数字");
                e.printStackTrace();
            }
        } else {
            N.setText("0");
        }
    }

    /**
     *  计算列车最大轮周牵引功率:P1=N*P0(KW)
     */
    public void calculateP1() {
        if (!N.getText().trim().equals("") && !P0.getText().trim().equals("")) {
            try {
                P1.setText((Double.parseDouble(N.getText().trim()) * Double.parseDouble(P0.getText().trim())) + "");
            } catch (NumberFormatException e) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(this, "请输入正确的数字");
                e.printStackTrace();
            }
        } else {
            P1.setText("0.0");
        }
    }

    /**
     *  计算恒功区和恒转矩区转折点对应牵引力:F1=P1*3.6/v1(KN)
     */
    public void calculateF1() {
        if (!P1.getText().trim().equals("") && !v1.getText().trim().equals("")) {
            try {
                F1.setText(MyTools.numFormat(((Double.parseDouble(P1.getText().trim()) * 3.6) / Double.parseDouble(v1.getText().trim()))) + "");
            } catch (NumberFormatException e) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(this, "请输入正确的数字");
                e.printStackTrace();
            }
        } else {
            F1.setText("0.0");
        }
    }

    /**
     *  计算弱磁点对应牵引力:F2=P1*3.6/v2(KN)
     */
    public void calculateF2() {
        if (!P1.getText().trim().equals("") && !v2.getText().trim().equals("")) {
            try {
                F2.setText(MyTools.numFormat(((Double.parseDouble(P1.getText().trim()) * 3.6) / Double.parseDouble(v2.getText().trim()))) + "");
            } catch (NumberFormatException e) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(this, "请输入正确的数字");
                e.printStackTrace();
            }
        } else {
            F2.setText("0.0");
        }
    }

    /**
     *  计算启动牵引力:Fst=2*N*k1*k2*T/1000/D;(KN)
     */
    public void calculateFst() {
        String NStr = N.getText().trim();
        String k1Str = k1.getText().trim();
        String k2Str = k2.getText().trim();
        String TStr = T.getText().trim();
        String DStr = D.getText().trim();
        if (!NStr.equals("") && !k1Str.equals("") && !k2Str.equals("") && !TStr.equals("") && !DStr.equals("")) {
            try {
                double FstDou = (2 * Double.parseDouble(NStr) * Double.parseDouble(k1Str) * Double.parseDouble(k2Str) * Double.parseDouble(TStr))
                                / (1000 * Double.parseDouble(DStr));
                Fst.setText(MyTools.numFormat(FstDou) + "");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入正确的数字");
                e.printStackTrace();
            }
        } else {
            Fst.setText("0.0");
        }
    }
}

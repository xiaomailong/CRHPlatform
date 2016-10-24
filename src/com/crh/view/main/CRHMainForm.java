package com.crh.view.main;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.crh.view.dialog.RouteDataManagementDialog;
import com.crh.view.panel.AuxiliaryPowerSupplyDCPanel;
import com.crh.view.panel.AuxiliaryPowerSupplyPanel;
import com.crh.view.panel.BrakeConfPanel;
import com.crh.view.panel.FixedTimeSimulationPanel;
import com.crh.view.panel.MinTimeSimulationPanel;
import com.crh.view.panel.RealTimeSimulationPanel;
import com.crh.view.panel.SpecifiedTimeEnergySimulationPanel;
import com.crh.view.panel.TopTargetPanel;
import com.crh.view.panel.TractionConfPanel;
import com.crh.view.panel.TractionLevelConfPanel;
import com.crh.view.panel.TrainEditPanel;
import com.crh.view.panel.WelcomePanel;

/**
 * �����ڣ������������
 * @author huhui
 *
 */
public class CRHMainForm extends JFrame {

    /**
     * tab�˵�
     */
    private JTabbedPane                        tabbedPane                     = null;
    /**
     * ���г��༭����tabҳ
     */
    private TrainEditPanel                     trainEditPanel                 = null;
    /**
     * ������Ŀ�ꡱ��tabҳ
     */
    private TopTargetPanel                     topTargerPanel                 = null;
    /**
     * �������ʱ��ӭ�Ľ���
     */
    private WelcomePanel                       welcomePanel                   = null;
    /**
     * ���޼�ǣ�����á���tabҳ
     */
    private TractionConfPanel                  tractionConfPanel              = null;
    /**
     * ���м�ǣ�����á���tabҳ
     */
    private TractionLevelConfPanel             tractionLevelConfPanel         = null;
    /**
     * ���ƶ����á���tabҳ
     */
    private BrakeConfPanel                     brakeConfPanel                 = null;
    /**
     * ����������ϵͳ������������tabҳ
     */
    private AuxiliaryPowerSupplyPanel          auxiliaryPowerSupplyPanel      = null;
    /**
     * ����������ϵͳ��ֱ��������tabҳ
     */
    private AuxiliaryPowerSupplyDCPanel        auxiliaryPowerSupplyDCPanel    = null;
    /**
     * ����Сʱ�����з��桱��tabҳ
     */
    private MinTimeSimulationPanel             minTimeSimulationPanel         = null;
    /**
     * ����̬���з��桱��tabҳ
     */
    private RealTimeSimulationPanel            realTimeSimulationPanel        = null;
    /**
     * ���̶�ʱ�ֽ��ܿ������С���tabҳ
     */
    private SpecifiedTimeEnergySimulationPanel specifiedTimeSimulationPanel   = null;
    /**
     * ���̶�ʱ�����з��桱��tabҳ
     */
    private FixedTimeSimulationPanel           fixedTimeEnergySimulationPanel = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    CRHMainForm frame = new CRHMainForm();
                    frame.setVisible(true);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// ���
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * ���캯����������ݳ�ʼ��
     */
    public CRHMainForm() {
        setTitle("���ٶ�����ϵͳ�Ż����ƽ̨");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 700);

        JMenuBar menuBar = new JMenuBar();

        setJMenuBar(menuBar);//��������ڣ�ֱ����ʾ�˵���
        JMenu generalDataMenu = new JMenu("ͨ������");
        menuBar.add(generalDataMenu);

        JMenuItem menuItem_4 = new JMenuItem("��·����");
        menuItem_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RouteDataManagementDialog routeDataManagementDialog = new RouteDataManagementDialog(CRHMainForm.this);
            }
        });
        generalDataMenu.add(menuItem_4);

        JMenuItem menuItem_5 = new JMenuItem("�г��༭");
        menuItem_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (trainEditPanel == null) {
                    trainEditPanel = new TrainEditPanel();
                    addPanelToTab("�г��༭", trainEditPanel);
                    tabbedPane.setSelectedComponent(trainEditPanel);
                } else {
                    tabbedPane.setSelectedComponent(trainEditPanel);
                }
            }
        });
        generalDataMenu.add(menuItem_5);

        JMenu topTargetMenu = new JMenu("����Ŀ��");
        menuBar.add(topTargetMenu);

        JMenuItem menuItem_26 = new JMenuItem("����Ŀ��");
        menuItem_26.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (topTargerPanel == null) {
                    topTargerPanel = new TopTargetPanel();
                    addPanelToTab("����Ŀ��", topTargerPanel);
                    tabbedPane.setSelectedComponent(topTargerPanel);
                } else {
                    tabbedPane.setSelectedComponent(topTargerPanel);
                }
            }
        });
        topTargetMenu.add(menuItem_26);

        JMenu tractionMenu = new JMenu("ǣ������");
        menuBar.add(tractionMenu);

        JMenuItem menuItem_6 = new JMenuItem("ǣ���������(�޼�)");
        menuItem_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (tractionConfPanel == null) {
                    tractionConfPanel = new TractionConfPanel();
                    addPanelToTab("ǣ����������(�޼�)", tractionConfPanel);
                    tabbedPane.setSelectedComponent(tractionConfPanel);
                } else {
                    tabbedPane.setSelectedComponent(tractionConfPanel);
                }
            }
        });
        tractionMenu.add(menuItem_6);

        JMenuItem menuItem_7 = new JMenuItem("ǣ���������(�м�)");
        menuItem_7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (tractionLevelConfPanel == null) {
                    tractionLevelConfPanel = new TractionLevelConfPanel();
                    addPanelToTab("ǣ����������(�м�)", tractionLevelConfPanel);
                    tabbedPane.setSelectedComponent(tractionLevelConfPanel);
                } else {
                    tabbedPane.setSelectedComponent(tractionLevelConfPanel);
                }
            }
        });
        tractionMenu.add(menuItem_7);

        JMenuItem menuItem_8 = new JMenuItem("��̬ģ��");
        tractionMenu.add(menuItem_8);

        JMenuItem menuItem_9 = new JMenuItem("��ر�׼��ѯ");
        tractionMenu.add(menuItem_9);

        JMenu menu = new JMenu("�ƶ�ϵͳ");
        menuBar.add(menu);

        JMenuItem menuItem_10 = new JMenuItem("�ƶ��������");
        menuItem_10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (brakeConfPanel == null) {
                    brakeConfPanel = new BrakeConfPanel();
                    addPanelToTab("�ƶ��������", brakeConfPanel);
                    tabbedPane.setSelectedComponent(brakeConfPanel);
                } else {
                    tabbedPane.setSelectedComponent(brakeConfPanel);
                }
            }
        });
        menu.add(menuItem_10);

        JMenu menu_5 = new JMenu("\u7535\u7A7A\u590D\u5408\u5236\u52A8");
        menu.add(menu_5);

        JMenu menu_6 = new JMenu("\u95F8\u76D8\u95F8\u7247\u6E29\u5347\u4EFF\u771F");
        menu_5.add(menu_6);

        JMenuItem menuItem_21 = new JMenuItem("\u80FD\u91CF\u6298\u7B97\u6CD5");
        menu_6.add(menuItem_21);

        JMenuItem menuItem_22 = new JMenuItem("\u6469\u64E6\u529F\u7387\u6CD5");
        menu_6.add(menuItem_22);

        JMenuItem menuItem_11 = new JMenuItem("\u98CE\u6E90\u7CFB\u7EDF\u8BBE\u8BA1");
        menu_5.add(menuItem_11);

        JMenu menu_7 = new JMenu("\u590D\u5408\u5236\u52A8");
        menu_5.add(menu_7);

        JMenuItem menuItem_23 = new JMenuItem("\u7535\u7A7A\u590D\u5408\u5236\u52A8");
        menu_7.add(menuItem_23);

        JMenuItem menuItem_24 = new JMenuItem("\u7A7A\u6C14\u5236\u52A8");
        menu_7.add(menuItem_24);

        JMenu menu_8 = new JMenu("��������ϵͳ");
        menuBar.add(menu_8);

        JMenuItem menuItem_25 = new JMenuItem("�����������縺��");
        menuItem_25.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (auxiliaryPowerSupplyPanel == null) {
                    auxiliaryPowerSupplyPanel = new AuxiliaryPowerSupplyPanel();
                    addPanelToTab("�����������縺��", auxiliaryPowerSupplyPanel);
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyPanel);
                } else {
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyPanel);
                }
            }
        });
        menu_8.add(menuItem_25);

        JMenuItem mntmNewMenuItem = new JMenuItem("ֱ���������縺��");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (auxiliaryPowerSupplyDCPanel == null) {
                    auxiliaryPowerSupplyDCPanel = new AuxiliaryPowerSupplyDCPanel();
                    addPanelToTab("ֱ���������縺��", auxiliaryPowerSupplyDCPanel);
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyDCPanel);
                } else {
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyDCPanel);
                }
            }
        });
        menu_8.add(mntmNewMenuItem);

        JMenu simulationMenu = new JMenu("�ۺϷ���");
        menuBar.add(simulationMenu);

        JMenu menu_1 = new JMenu("��������");
        simulationMenu.add(menu_1);

        JMenu menu_2 = new JMenu("���з���");
        menu_1.add(menu_2);

        JMenuItem menuItem_12 = new JMenuItem("��Сʱ������");
        menuItem_12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (minTimeSimulationPanel == null) {
                    minTimeSimulationPanel = new MinTimeSimulationPanel();
                    addPanelToTab("��Сʱ������", minTimeSimulationPanel);
                    tabbedPane.setSelectedComponent(minTimeSimulationPanel);
                } else {
                    tabbedPane.setSelectedComponent(minTimeSimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_12);

        JMenuItem menuItem_13 = new JMenuItem("��̬����");
        menuItem_13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (realTimeSimulationPanel == null) {
                    realTimeSimulationPanel = new RealTimeSimulationPanel();
                    addPanelToTab("��̬����", realTimeSimulationPanel);
                    tabbedPane.setSelectedComponent(realTimeSimulationPanel);
                } else {
                    tabbedPane.setSelectedComponent(realTimeSimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_13);

        JMenuItem menuItem_14 = new JMenuItem("�̶�ʱ�ֽ��ܿ�������");
        menuItem_14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (specifiedTimeSimulationPanel == null) {
                    specifiedTimeSimulationPanel = new SpecifiedTimeEnergySimulationPanel();
                    addPanelToTab("�̶�ʱ�ֽ��ܿ�������", specifiedTimeSimulationPanel);
                    tabbedPane.setSelectedComponent(specifiedTimeSimulationPanel);
                } else {
                    tabbedPane.setSelectedComponent(specifiedTimeSimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_14);

        JMenuItem menuItem_27 = new JMenuItem("�̶�ʱ������");
        menuItem_27.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (fixedTimeEnergySimulationPanel == null) {
                    fixedTimeEnergySimulationPanel = new FixedTimeSimulationPanel();
                    addPanelToTab("�̶�ʱ������", fixedTimeEnergySimulationPanel);
                    tabbedPane.setSelectedComponent(fixedTimeEnergySimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_27);

        JMenuItem menuItem_15 = new JMenuItem("�������з���");
        menu_1.add(menuItem_15);

        JMenuItem mntmd = new JMenuItem("3D��������");
        menu_1.add(mntmd);

        JMenu menu_3 = new JMenu("��̬����");
        simulationMenu.add(menu_3);

        JMenuItem menuItem_16 = new JMenuItem("��̬����");
        menu_3.add(menuItem_16);

        JMenuItem menuItem_17 = new JMenuItem("ǣ������-ǣ����������");
        menu_3.add(menuItem_17);

        JMenu menu_4 = new JMenu("����ϵͳ��������");
        simulationMenu.add(menu_4);

        JMenuItem menuItem_18 = new JMenuItem("��ѹ������");
        menu_4.add(menuItem_18);

        JMenuItem menuItem_19 = new JMenuItem("�������");
        menu_4.add(menuItem_19);

        JMenuItem menuItem_20 = new JMenuItem("����������");
        menu_4.add(menuItem_20);

        JMenu resultAnalysisMenu = new JMenu("�������");
        menuBar.add(resultAnalysisMenu);

        JMenu helpMenu = new JMenu("����");
        menuBar.add(helpMenu);

        getContentPane().setLayout(new GridLayout(1, 1));
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {//˫���ر�tabҳ
                    Component component = tabbedPane.getSelectedComponent();
                    if (!(component instanceof WelcomePanel)) {
                        tabbedPane.remove(component);
                    }
                    if (component instanceof TrainEditPanel) {
                        trainEditPanel = null;
                    } else if (component instanceof TopTargetPanel) {
                        topTargerPanel = null;
                    } else if (component instanceof TractionConfPanel) {
                        tractionConfPanel = null;
                    } else if (component instanceof TractionLevelConfPanel) {
                        tractionLevelConfPanel = null;
                    } else if (component instanceof BrakeConfPanel) {
                        brakeConfPanel = null;
                    } else if (component instanceof AuxiliaryPowerSupplyPanel) {
                        auxiliaryPowerSupplyPanel = null;
                    } else if (component instanceof AuxiliaryPowerSupplyDCPanel) {
                        auxiliaryPowerSupplyDCPanel = null;
                    } else if (component instanceof MinTimeSimulationPanel) {
                        minTimeSimulationPanel = null;
                    } else if (component instanceof RealTimeSimulationPanel) {
                        realTimeSimulationPanel = null;
                    } else if (component instanceof SpecifiedTimeEnergySimulationPanel) {
                        specifiedTimeSimulationPanel = null;
                    } else if (component instanceof FixedTimeSimulationPanel) {
                        fixedTimeEnergySimulationPanel = null;
                    }
                }
            }
        });
        getContentPane().add(tabbedPane);

        welcomePanel = new WelcomePanel();
        addPanelToTab("��ӭ", welcomePanel);
        tabbedPane.setSelectedComponent(welcomePanel);

        if (isOverTime()) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {

            }
            System.exit(EXIT_ON_CLOSE);
        }

        /*//*������
        fixedTimeEnergySimulationPanel = new FixedTimeSimulationPanel();
        addPanelToTab("��̬����", fixedTimeEnergySimulationPanel);
        tabbedPane.setSelectedComponent(fixedTimeEnergySimulationPanel);*/

        //		new Thread(new ExitThread()).start();
    }

    /**
     * ��tabbedPane������tab
     */
    public void addPanelToTab(String title, JPanel panel) {
        tabbedPane.addTab(title, panel);
    }

    //�����������
    public boolean isOverTime() {
        boolean b = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar deadLine = new GregorianCalendar(2016, 10, 30); //�����2015��7��15�Ź���
        Calendar now = Calendar.getInstance();
        if (now.compareTo(deadLine) >= 0) {
            //now��deadLine��,����-1,
            //now��deadLine��ͬ,����0
            //now��deadLine��,����1
            b = true;
        }
        return b;
    }

}

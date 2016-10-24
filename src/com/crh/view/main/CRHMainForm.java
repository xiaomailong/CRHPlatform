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
 * 软件入口，启动仿真程序
 * @author huhui
 *
 */
public class CRHMainForm extends JFrame {

    /**
     * tab菜单
     */
    private JTabbedPane                        tabbedPane                     = null;
    /**
     * “列车编辑”的tab页
     */
    private TrainEditPanel                     trainEditPanel                 = null;
    /**
     * “顶层目标”的tab页
     */
    private TopTargetPanel                     topTargerPanel                 = null;
    /**
     * 软件启动时欢迎的界面
     */
    private WelcomePanel                       welcomePanel                   = null;
    /**
     * “无级牵引配置”的tab页
     */
    private TractionConfPanel                  tractionConfPanel              = null;
    /**
     * “有级牵引配置”的tab页
     */
    private TractionLevelConfPanel             tractionLevelConfPanel         = null;
    /**
     * “制动配置”的tab页
     */
    private BrakeConfPanel                     brakeConfPanel                 = null;
    /**
     * “辅助供电系统（交流）”的tab页
     */
    private AuxiliaryPowerSupplyPanel          auxiliaryPowerSupplyPanel      = null;
    /**
     * “辅助供电系统（直流）”的tab页
     */
    private AuxiliaryPowerSupplyDCPanel        auxiliaryPowerSupplyDCPanel    = null;
    /**
     * “最小时分运行仿真”的tab页
     */
    private MinTimeSimulationPanel             minTimeSimulationPanel         = null;
    /**
     * “常态运行仿真”的tab页
     */
    private RealTimeSimulationPanel            realTimeSimulationPanel        = null;
    /**
     * “固定时分节能控制运行”的tab页
     */
    private SpecifiedTimeEnergySimulationPanel specifiedTimeSimulationPanel   = null;
    /**
     * “固定时分运行仿真”的tab页
     */
    private FixedTimeSimulationPanel           fixedTimeEnergySimulationPanel = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    CRHMainForm frame = new CRHMainForm();
                    frame.setVisible(true);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// 最大化
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 构造函数，完成数据初始化
     */
    public CRHMainForm() {
        setTitle("高速动车组系统优化设计平台");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 700);

        JMenuBar menuBar = new JMenuBar();

        setJMenuBar(menuBar);//软件不过期，直接显示菜单栏
        JMenu generalDataMenu = new JMenu("通用数据");
        menuBar.add(generalDataMenu);

        JMenuItem menuItem_4 = new JMenuItem("线路数据");
        menuItem_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RouteDataManagementDialog routeDataManagementDialog = new RouteDataManagementDialog(CRHMainForm.this);
            }
        });
        generalDataMenu.add(menuItem_4);

        JMenuItem menuItem_5 = new JMenuItem("列车编辑");
        menuItem_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (trainEditPanel == null) {
                    trainEditPanel = new TrainEditPanel();
                    addPanelToTab("列车编辑", trainEditPanel);
                    tabbedPane.setSelectedComponent(trainEditPanel);
                } else {
                    tabbedPane.setSelectedComponent(trainEditPanel);
                }
            }
        });
        generalDataMenu.add(menuItem_5);

        JMenu topTargetMenu = new JMenu("顶层目标");
        menuBar.add(topTargetMenu);

        JMenuItem menuItem_26 = new JMenuItem("顶层目标");
        menuItem_26.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (topTargerPanel == null) {
                    topTargerPanel = new TopTargetPanel();
                    addPanelToTab("顶层目标", topTargerPanel);
                    tabbedPane.setSelectedComponent(topTargerPanel);
                } else {
                    tabbedPane.setSelectedComponent(topTargerPanel);
                }
            }
        });
        topTargetMenu.add(menuItem_26);

        JMenu tractionMenu = new JMenu("牵引传动");
        menuBar.add(tractionMenu);

        JMenuItem menuItem_6 = new JMenuItem("牵引特性设计(无级)");
        menuItem_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (tractionConfPanel == null) {
                    tractionConfPanel = new TractionConfPanel();
                    addPanelToTab("牵引特性配置(无级)", tractionConfPanel);
                    tabbedPane.setSelectedComponent(tractionConfPanel);
                } else {
                    tabbedPane.setSelectedComponent(tractionConfPanel);
                }
            }
        });
        tractionMenu.add(menuItem_6);

        JMenuItem menuItem_7 = new JMenuItem("牵引特性设计(有级)");
        menuItem_7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (tractionLevelConfPanel == null) {
                    tractionLevelConfPanel = new TractionLevelConfPanel();
                    addPanelToTab("牵引特性配置(有级)", tractionLevelConfPanel);
                    tabbedPane.setSelectedComponent(tractionLevelConfPanel);
                } else {
                    tabbedPane.setSelectedComponent(tractionLevelConfPanel);
                }
            }
        });
        tractionMenu.add(menuItem_7);

        JMenuItem menuItem_8 = new JMenuItem("动态模型");
        tractionMenu.add(menuItem_8);

        JMenuItem menuItem_9 = new JMenuItem("相关标准查询");
        tractionMenu.add(menuItem_9);

        JMenu menu = new JMenu("制动系统");
        menuBar.add(menu);

        JMenuItem menuItem_10 = new JMenuItem("制动特性设计");
        menuItem_10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (brakeConfPanel == null) {
                    brakeConfPanel = new BrakeConfPanel();
                    addPanelToTab("制动特性设计", brakeConfPanel);
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

        JMenu menu_8 = new JMenu("辅助供电系统");
        menuBar.add(menu_8);

        JMenuItem menuItem_25 = new JMenuItem("交流辅助供电负载");
        menuItem_25.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (auxiliaryPowerSupplyPanel == null) {
                    auxiliaryPowerSupplyPanel = new AuxiliaryPowerSupplyPanel();
                    addPanelToTab("交流辅助供电负载", auxiliaryPowerSupplyPanel);
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyPanel);
                } else {
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyPanel);
                }
            }
        });
        menu_8.add(menuItem_25);

        JMenuItem mntmNewMenuItem = new JMenuItem("直流辅助供电负载");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (auxiliaryPowerSupplyDCPanel == null) {
                    auxiliaryPowerSupplyDCPanel = new AuxiliaryPowerSupplyDCPanel();
                    addPanelToTab("直流辅助供电负载", auxiliaryPowerSupplyDCPanel);
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyDCPanel);
                } else {
                    tabbedPane.setSelectedComponent(auxiliaryPowerSupplyDCPanel);
                }
            }
        });
        menu_8.add(mntmNewMenuItem);

        JMenu simulationMenu = new JMenu("综合仿真");
        menuBar.add(simulationMenu);

        JMenu menu_1 = new JMenu("基本仿真");
        simulationMenu.add(menu_1);

        JMenu menu_2 = new JMenu("运行仿真");
        menu_1.add(menu_2);

        JMenuItem menuItem_12 = new JMenuItem("最小时分运行");
        menuItem_12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (minTimeSimulationPanel == null) {
                    minTimeSimulationPanel = new MinTimeSimulationPanel();
                    addPanelToTab("最小时分运行", minTimeSimulationPanel);
                    tabbedPane.setSelectedComponent(minTimeSimulationPanel);
                } else {
                    tabbedPane.setSelectedComponent(minTimeSimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_12);

        JMenuItem menuItem_13 = new JMenuItem("常态运行");
        menuItem_13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (realTimeSimulationPanel == null) {
                    realTimeSimulationPanel = new RealTimeSimulationPanel();
                    addPanelToTab("常态运行", realTimeSimulationPanel);
                    tabbedPane.setSelectedComponent(realTimeSimulationPanel);
                } else {
                    tabbedPane.setSelectedComponent(realTimeSimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_13);

        JMenuItem menuItem_14 = new JMenuItem("固定时分节能控制运行");
        menuItem_14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (specifiedTimeSimulationPanel == null) {
                    specifiedTimeSimulationPanel = new SpecifiedTimeEnergySimulationPanel();
                    addPanelToTab("固定时分节能控制运行", specifiedTimeSimulationPanel);
                    tabbedPane.setSelectedComponent(specifiedTimeSimulationPanel);
                } else {
                    tabbedPane.setSelectedComponent(specifiedTimeSimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_14);

        JMenuItem menuItem_27 = new JMenuItem("固定时分运行");
        menuItem_27.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (fixedTimeEnergySimulationPanel == null) {
                    fixedTimeEnergySimulationPanel = new FixedTimeSimulationPanel();
                    addPanelToTab("固定时分运行", fixedTimeEnergySimulationPanel);
                    tabbedPane.setSelectedComponent(fixedTimeEnergySimulationPanel);
                }
            }
        });
        menu_2.add(menuItem_27);

        JMenuItem menuItem_15 = new JMenuItem("虚拟运行仿真");
        menu_1.add(menuItem_15);

        JMenuItem mntmd = new JMenuItem("3D仿真服务端");
        menu_1.add(mntmd);

        JMenu menu_3 = new JMenu("动态仿真");
        simulationMenu.add(menu_3);

        JMenuItem menuItem_16 = new JMenuItem("常态仿真");
        menu_3.add(menuItem_16);

        JMenuItem menuItem_17 = new JMenuItem("牵引供电-牵引传动仿真");
        menu_3.add(menuItem_17);

        JMenu menu_4 = new JMenu("传动系统温升仿真");
        simulationMenu.add(menu_4);

        JMenuItem menuItem_18 = new JMenuItem("变压器温升");
        menu_4.add(menuItem_18);

        JMenuItem menuItem_19 = new JMenuItem("电机温升");
        menu_4.add(menuItem_19);

        JMenuItem menuItem_20 = new JMenuItem("变流器温升");
        menu_4.add(menuItem_20);

        JMenu resultAnalysisMenu = new JMenu("结果分析");
        menuBar.add(resultAnalysisMenu);

        JMenu helpMenu = new JMenu("帮助");
        menuBar.add(helpMenu);

        getContentPane().setLayout(new GridLayout(1, 1));
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {//双击关闭tab页
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
        addPanelToTab("欢迎", welcomePanel);
        tabbedPane.setSelectedComponent(welcomePanel);

        if (isOverTime()) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {

            }
            System.exit(EXIT_ON_CLOSE);
        }

        /*//*调试用
        fixedTimeEnergySimulationPanel = new FixedTimeSimulationPanel();
        addPanelToTab("常态运行", fixedTimeEnergySimulationPanel);
        tabbedPane.setSelectedComponent(fixedTimeEnergySimulationPanel);*/

        //		new Thread(new ExitThread()).start();
    }

    /**
     * 在tabbedPane中增加tab
     */
    public void addPanelToTab(String title, JPanel panel) {
        tabbedPane.addTab(title, panel);
    }

    //设置软件过期
    public boolean isOverTime() {
        boolean b = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar deadLine = new GregorianCalendar(2016, 10, 30); //软件到2015年7月15号过期
        Calendar now = Calendar.getInstance();
        if (now.compareTo(deadLine) >= 0) {
            //now比deadLine早,返回-1,
            //now与deadLine相同,返回0
            //now比deadLine晚,返回1
            b = true;
        }
        return b;
    }

}

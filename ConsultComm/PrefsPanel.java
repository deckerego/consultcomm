//Standard Components
import java.util.*;
import java.io.*;
//XML Components
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class PrefsPanel extends javax.swing.JFrame {
  protected int timeFormat = ClntComm.MINUTES;
  protected boolean animateIcons = true;
  protected int saveInterval = 60;
  protected int allowedIdle = 0, idleAction;
  protected long countdown;
  protected float countpay;
  protected String idleProject;
  private ClntComm clntComm;
  private String themePack, kdeTheme, gtkTheme;
  private static boolean timeoutLibrary = false;
  private java.text.NumberFormat dollarFormat;

  static {
    try {
      System.loadLibrary("timeout");
      timeoutLibrary = true;
    } catch (UnsatisfiedLinkError e) {
      timeoutLibrary = false;
    }
  }

  /** Creates new form PrefsPanel */
  public PrefsPanel(ClntComm parent) {
    clntComm = parent;
    dollarFormat = java.text.NumberFormat.getInstance();
    dollarFormat.setMinimumFractionDigits(2);
    dollarFormat.setMaximumFractionDigits(2);
    readPrefs();
    initComponents();
    if(!themePack.equals("")) toggleThemePack();
    if(!kdeTheme.equals("")) toggleKDE();
    if(!gtkTheme.equals("")) toggleGTK();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        timeFormatGroup = new javax.swing.ButtonGroup();
        idleGroup = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        prefsPanel = new javax.swing.JPanel();
        prefsInputPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        timeFormatLabel = new javax.swing.JLabel();
        minuteButton = new javax.swing.JRadioButton();
        secondButton = new javax.swing.JRadioButton();
        showIconCheckBox = new javax.swing.JCheckBox();
        save1Label = new javax.swing.JLabel();
        saveField = new javax.swing.JTextField();
        save2Label = new javax.swing.JLabel();
        idleCheckBox = new javax.swing.JCheckBox();
        idleField = new javax.swing.JTextField();
        idleLabel = new javax.swing.JLabel();
        pauseIdleRadioButton = new javax.swing.JRadioButton();
        projectIdleRadioButton = new javax.swing.JRadioButton();
        projectIdleComboBox = new javax.swing.JComboBox(clntComm.getTimes().getAllProjects());
        prefsButtonPanel = new javax.swing.JPanel();
        prefsOKButton = new javax.swing.JButton();
        prefsCancelButton = new javax.swing.JButton();
        flagsPanel = new javax.swing.JPanel();
        flagsInputPanel = new javax.swing.JPanel();
        flagLabel = new javax.swing.JLabel();
        billableCheckBox = new javax.swing.JCheckBox();
        exportCheckBox = new javax.swing.JCheckBox();
        countdownCheckBox = new javax.swing.JCheckBox();
        countdownField = new javax.swing.JTextField();
        payCheckBox = new javax.swing.JCheckBox();
        payField = new javax.swing.JTextField();
        payLabel = new javax.swing.JLabel();
        flagsButtonPanel = new javax.swing.JPanel();
        flagsOKButton = new javax.swing.JButton();
        flagsCancelButton = new javax.swing.JButton();
        skinsPanel = new javax.swing.JPanel();
        skinsInputPanel = new javax.swing.JPanel();
        skinsLabel = new javax.swing.JLabel();
        themeCheckBox = new javax.swing.JCheckBox();
        themeField = new javax.swing.JTextField();
        themeBrowse = new javax.swing.JButton();
        gtkCheckBox = new javax.swing.JCheckBox();
        gtkField = new javax.swing.JTextField();
        gtkBrowse = new javax.swing.JButton();
        kdeCheckBox = new javax.swing.JCheckBox();
        kdeField = new javax.swing.JTextField();
        kdeBrowse = new javax.swing.JButton();
        skinsButtonPanel = new javax.swing.JPanel();
        skinsOKButton = new javax.swing.JButton();
        skinsCancelButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        prefsPanel.setLayout(new java.awt.BorderLayout());

        prefsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showPrefs(evt);
            }
        });

        prefsInputPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        generalLabel.setText("General Properties");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        prefsInputPanel.add(generalLabel, gridBagConstraints);

        timeFormatLabel.setText("Show time in:  ");
        prefsInputPanel.add(timeFormatLabel, new java.awt.GridBagConstraints());

        minuteButton.setForeground(new java.awt.Color(102, 102, 153));
        minuteButton.setSelected(timeFormat == ClntComm.MINUTES);
        minuteButton.setText("Minutes");
        timeFormatGroup.add(minuteButton);
        prefsInputPanel.add(minuteButton, new java.awt.GridBagConstraints());

        secondButton.setForeground(new java.awt.Color(102, 102, 153));
        secondButton.setSelected(timeFormat == ClntComm.SECONDS);
        secondButton.setText("Seconds");
        timeFormatGroup.add(secondButton);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        prefsInputPanel.add(secondButton, gridBagConstraints);

        showIconCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        showIconCheckBox.setSelected(animateIcons);
        showIconCheckBox.setText("Show Animated Icons");
        showIconCheckBox.setMargin(new java.awt.Insets(6, 2, 2, 2));
        showIconCheckBox.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        prefsInputPanel.add(showIconCheckBox, gridBagConstraints);

        save1Label.setText("Save info every ");
        prefsInputPanel.add(save1Label, new java.awt.GridBagConstraints());

        saveField.setColumns(3);
        saveField.setText(Integer.toString(saveInterval));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        prefsInputPanel.add(saveField, gridBagConstraints);

        save2Label.setText(" seconds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        prefsInputPanel.add(save2Label, gridBagConstraints);

        idleCheckBox.setSelected(allowedIdle > 0);
        idleCheckBox.setText("When idle for ");
        idleCheckBox.setEnabled(timeoutLibrary);
        idleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleIdle(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        prefsInputPanel.add(idleCheckBox, gridBagConstraints);

        idleField.setColumns(3);
        idleField.setText(Integer.toString(allowedIdle));
        idleField.setEnabled(timeoutLibrary && idleCheckBox.isSelected());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        prefsInputPanel.add(idleField, gridBagConstraints);

        idleLabel.setText(" seconds");
        idleLabel.setEnabled(timeoutLibrary);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        prefsInputPanel.add(idleLabel, gridBagConstraints);

        pauseIdleRadioButton.setSelected(idleAction == ClntComm.IDLE_PAUSE);
        pauseIdleRadioButton.setText("Pause Timer");
        idleGroup.add(pauseIdleRadioButton);
        pauseIdleRadioButton.setEnabled(timeoutLibrary && idleCheckBox.isSelected());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        prefsInputPanel.add(pauseIdleRadioButton, gridBagConstraints);

        projectIdleRadioButton.setSelected(idleAction == ClntComm.IDLE_PROJECT);
        projectIdleRadioButton.setText("Switch to project ");
        idleGroup.add(projectIdleRadioButton);
        projectIdleRadioButton.setEnabled(timeoutLibrary && idleCheckBox.isSelected());
        prefsInputPanel.add(projectIdleRadioButton, new java.awt.GridBagConstraints());

        projectIdleComboBox.setSelectedItem(idleProject);
        projectIdleComboBox.setEnabled(timeoutLibrary && idleCheckBox.isSelected());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        prefsInputPanel.add(projectIdleComboBox, gridBagConstraints);

        prefsPanel.add(prefsInputPanel, java.awt.BorderLayout.CENTER);

        prefsOKButton.setText("OK");
        prefsOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePrefs(evt);
            }
        });

        prefsButtonPanel.add(prefsOKButton);

        prefsCancelButton.setText("Cancel");
        prefsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        prefsButtonPanel.add(prefsCancelButton);

        prefsPanel.add(prefsButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("General", null, prefsPanel, "");

        flagsPanel.setLayout(new java.awt.BorderLayout());

        flagsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showFlags(evt);
            }
        });

        flagsInputPanel.setLayout(new java.awt.GridBagLayout());

        flagLabel.setText("Project Attribute Flags");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        flagsInputPanel.add(flagLabel, gridBagConstraints);

        billableCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        billableCheckBox.setSelected(clntComm.attributeSet(ClntComm.SHOW_BILLABLE));
        billableCheckBox.setText("Show Billable Flag");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        flagsInputPanel.add(billableCheckBox, gridBagConstraints);

        exportCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        exportCheckBox.setSelected(clntComm.attributeSet(ClntComm.SHOW_EXPORT));
        exportCheckBox.setText("Use Export to Database Flag");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        flagsInputPanel.add(exportCheckBox, gridBagConstraints);

        countdownCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        countdownCheckBox.setSelected(clntComm.attributeSet(ClntComm.SHOW_COUNTDOWN));
        countdownCheckBox.setText("Count down from ");
        countdownCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleCountdown(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        flagsInputPanel.add(countdownCheckBox, gridBagConstraints);

        countdownField.setColumns(6);
        countdownField.setText(minutesToString(countdown));
        countdownField.setEnabled(clntComm.attributeSet(ClntComm.SHOW_COUNTDOWN));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        flagsInputPanel.add(countdownField, gridBagConstraints);

        payCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        payCheckBox.setSelected(clntComm.attributeSet(ClntComm.SHOW_COUNTPAY));
        payCheckBox.setText("Count pay, using $");
        payCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePay(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        flagsInputPanel.add(payCheckBox, gridBagConstraints);

        payField.setColumns(6);
        payField.setText(dollarFormat.format(countpay));
        payField.setEnabled(clntComm.attributeSet(ClntComm.SHOW_COUNTPAY));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        flagsInputPanel.add(payField, gridBagConstraints);

        payLabel.setForeground(new java.awt.Color(102, 102, 153));
        payLabel.setText(" per hour");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        flagsInputPanel.add(payLabel, gridBagConstraints);

        flagsPanel.add(flagsInputPanel, java.awt.BorderLayout.CENTER);

        flagsOKButton.setText("OK");
        flagsOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePrefs(evt);
            }
        });

        flagsButtonPanel.add(flagsOKButton);

        flagsCancelButton.setText("Cancel");
        flagsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        flagsButtonPanel.add(flagsCancelButton);

        flagsPanel.add(flagsButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Flags", null, flagsPanel, "");

        skinsPanel.setLayout(new java.awt.BorderLayout());

        skinsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showSkins(evt);
            }
        });

        skinsInputPanel.setLayout(new java.awt.GridBagLayout());

        skinsLabel.setText("Load Themes/Skins");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        skinsInputPanel.add(skinsLabel, gridBagConstraints);

        themeCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        themeCheckBox.setSelected(!themePack.equals(""));
        themeCheckBox.setText("Use Theme Pack: ");
        themeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleThemePack(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        skinsInputPanel.add(themeCheckBox, gridBagConstraints);

        themeField.setColumns(25);
        themeField.setText(themePack);
        themeField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        skinsInputPanel.add(themeField, gridBagConstraints);

        themeBrowse.setForeground(new java.awt.Color(102, 102, 153));
        themeBrowse.setText("Browse...");
        themeBrowse.setEnabled(false);
        themeBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findTheme(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        skinsInputPanel.add(themeBrowse, gridBagConstraints);

        gtkCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        gtkCheckBox.setSelected(!gtkTheme.equals(""));
        gtkCheckBox.setText("Use GTK Theme: ");
        gtkCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleGTK(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        skinsInputPanel.add(gtkCheckBox, gridBagConstraints);

        gtkField.setColumns(25);
        gtkField.setText(kdeTheme);
        gtkField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        skinsInputPanel.add(gtkField, gridBagConstraints);

        gtkBrowse.setForeground(new java.awt.Color(102, 102, 153));
        gtkBrowse.setText("Browse...");
        gtkBrowse.setEnabled(false);
        gtkBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findGTK(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        skinsInputPanel.add(gtkBrowse, gridBagConstraints);

        kdeCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        kdeCheckBox.setSelected(!kdeTheme.equals(""));
        kdeCheckBox.setText("Use KDE Theme: ");
        kdeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleKDE(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        skinsInputPanel.add(kdeCheckBox, gridBagConstraints);

        kdeField.setColumns(25);
        kdeField.setText(gtkTheme);
        kdeField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        skinsInputPanel.add(kdeField, gridBagConstraints);

        kdeBrowse.setForeground(new java.awt.Color(102, 102, 153));
        kdeBrowse.setText("Browse...");
        kdeBrowse.setEnabled(false);
        kdeBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findKDE(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        skinsInputPanel.add(kdeBrowse, gridBagConstraints);

        skinsPanel.add(skinsInputPanel, java.awt.BorderLayout.CENTER);

        skinsOKButton.setText("OK");
        skinsOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePrefs(evt);
            }
        });

        skinsButtonPanel.add(skinsOKButton);

        skinsCancelButton.setText("Cancel");
        skinsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        skinsButtonPanel.add(skinsCancelButton);

        skinsPanel.add(skinsButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Skins", null, skinsPanel, "");

        getContentPane().add(tabbedPane);

        pack();
    }//GEN-END:initComponents

  private void toggleCountdown(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleCountdown
    if(countdownCheckBox.isSelected()) {
      countdownField.setEnabled(true);
    } else {
      countdownField.setEnabled(false);
      countdown = 0;
    }
  }//GEN-LAST:event_toggleCountdown

  private void togglePay(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togglePay
    if(payCheckBox.isSelected()) {
      payField.setEnabled(true);
    } else {
      payField.setEnabled(false);
      countpay = 0;
    }
  }//GEN-LAST:event_togglePay

  private void toggleIdle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleIdle
    if(idleCheckBox.isSelected()) {
      idleField.setEnabled(true);
      pauseIdleRadioButton.setEnabled(true);
      projectIdleRadioButton.setEnabled(true);
      projectIdleComboBox.setEnabled(true);
    } else {
      idleField.setEnabled(false);
      pauseIdleRadioButton.setEnabled(false);
      projectIdleRadioButton.setEnabled(false);
      projectIdleComboBox.setEnabled(false);
    }
  }//GEN-LAST:event_toggleIdle

  private void showSkins(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showSkins
    getRootPane().setDefaultButton(skinsOKButton);
  }//GEN-LAST:event_showSkins

  private void findKDE(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findKDE
    final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    int returnVal = fc.showOpenDialog(this);
    if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
      File theme = fc.getSelectedFile();
      kdeTheme = theme.toString();
      kdeField.setText(themePack);
    }
  }//GEN-LAST:event_findKDE

  private void findGTK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findGTK
    final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    int returnVal = fc.showOpenDialog(this);
    if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
      File theme = fc.getSelectedFile();
      gtkTheme = theme.toString();
      gtkField.setText(themePack);
    }
  }//GEN-LAST:event_findGTK

  private void findTheme(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findTheme
    final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    int returnVal = fc.showOpenDialog(this);
    if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
      File theme = fc.getSelectedFile();
      themePack = theme.toString();
      themeField.setText(themePack);
    }
  }//GEN-LAST:event_findTheme

  private void toggleKDE(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleKDE
    toggleKDE();
  }//GEN-LAST:event_toggleKDE

  private void toggleKDE() {
    boolean kdetoggle = kdeCheckBox.isSelected();
    boolean themetoggle = kdetoggle || gtkCheckBox.isSelected();
    
    kdeField.setEnabled(kdetoggle);
    kdeBrowse.setEnabled(kdetoggle);

    themeCheckBox.setEnabled(! themetoggle);
    if(themetoggle) themeField.setEnabled(false);
    if(themetoggle) themeBrowse.setEnabled(false);
  }
  
  private void toggleGTK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleGTK
    toggleGTK();
  }//GEN-LAST:event_toggleGTK

  private void toggleGTK() {
    boolean gtktoggle = gtkCheckBox.isSelected();
    boolean themetoggle = gtktoggle || kdeCheckBox.isSelected();
    
    gtkField.setEnabled(gtktoggle);
    gtkBrowse.setEnabled(gtktoggle);

    themeCheckBox.setEnabled(! themetoggle);
    if(themetoggle) themeField.setEnabled(false);
    if(themetoggle) themeBrowse.setEnabled(false);
  }
  
  private void toggleThemePack(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleThemePack
    toggleThemePack();
  }//GEN-LAST:event_toggleThemePack

  private void toggleThemePack() {
    boolean toggle = themeCheckBox.isSelected();
    
    themeField.setEnabled(toggle);
    themeBrowse.setEnabled(toggle);

    kdeCheckBox.setEnabled(! toggle);
    if(toggle) kdeField.setEnabled(false);
    if(toggle) kdeBrowse.setEnabled(false);

    gtkCheckBox.setEnabled(! toggle);
    if(toggle) gtkField.setEnabled(false);
    if(toggle) gtkBrowse.setEnabled(false);
  }
  
  private void showFlags(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showFlags
    getRootPane().setDefaultButton(flagsOKButton);
  }//GEN-LAST:event_showFlags

  private void showPrefs(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showPrefs
    getRootPane().setDefaultButton(prefsOKButton);
  }//GEN-LAST:event_showPrefs
    
    private void savePrefs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePrefs
      savePrefs();
      clntComm.reload();
      exitForm();
    }//GEN-LAST:event_savePrefs
    
    private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
      exitForm();
    }//GEN-LAST:event_cancel
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
      exitForm();
    }//GEN-LAST:event_exitForm
    
    private void exitForm() {
      setVisible(false);
      dispose();
    }
    
    /**
     * Read through preferances file
     */
    private void readPrefs() {
      try {
        PrefsFile prefs = new PrefsFile("ClntComm.def");
        //Get time format
        String timeFormatString = prefs.readFirstString("timeformat", "type");
        if(timeFormatString.equals("seconds")) timeFormat = ClntComm.SECONDS;
        else timeFormat = ClntComm.MINUTES;
        //Get animation flag
        Boolean iconAnimation = prefs.readFirstBoolean("animations", "display");
        if(iconAnimation == null) animateIcons = true;
        else animateIcons = iconAnimation.booleanValue();
        //Get count down interval
        countdown = prefs.readFirstLong("countdown", "minutes");
        //Get count pay amount
        countpay = prefs.readFirstFloat("countpay", "amount");
        //Get save interval
        saveInterval = prefs.readFirstInt("saveinfo", "seconds");
        //Get idle time settings
        allowedIdle = prefs.readFirstInt("idle", "seconds");
        String idleActionString = prefs.readFirstString("idle", "action");
        if(idleActionString.equals("project")) idleAction = ClntComm.IDLE_PROJECT;
        else idleAction = ClntComm.IDLE_PAUSE;
        idleProject = prefs.readFirstString("idle", "project");        
        //Get skins
        themePack = prefs.readFirstString("skin", "theme");
        kdeTheme = prefs.readFirstString("skin", "kde");
        gtkTheme = prefs.readFirstString("skin", "gtk");
      } catch (IOException e) {
        System.err.println("Cannot create new preferences file! "+e);
      } catch (Exception e) {
        System.err.println("Cannot read prefs file: "+e);
        e.printStackTrace(System.out);
      }
    }
    
    private void savePrefs() {
      try {
        PrefsFile prefs = new PrefsFile("ClntComm.def");        
        //Save time format
        if(secondButton.isSelected()) prefs.saveFirst("timeformat", "type", "seconds");
        if(minuteButton.isSelected()) prefs.saveFirst("timeformat", "type", "minutes");
        //Save animation flag
        prefs.saveFirst("animations", "display", showIconCheckBox.isSelected());
        //Save attribute flag settings
        int attributes = ClntComm.SHOW_TOTAL;
        if(billableCheckBox.isSelected()) attributes = attributes | ClntComm.SHOW_BILLABLE;
        if(exportCheckBox.isSelected()) attributes = attributes | ClntComm.SHOW_EXPORT;
        if(countdownCheckBox.isSelected()) attributes = attributes | ClntComm.SHOW_COUNTDOWN;
        if(payCheckBox.isSelected()) attributes = attributes | ClntComm.SHOW_COUNTPAY;
        prefs.saveFirst("attributes", "value", attributes);
        //Save countdown
        prefs.saveFirst("countdown", "minutes", stringToMinutes(countdownField.getText()));
        //Save pay count
        prefs.saveFirst("countpay", "amount", payField.getText());
        //Save save interval
        prefs.saveFirst("saveinfo", "seconds", saveField.getText());
        //Save idle info
        Object projectIdleComboItem = projectIdleComboBox.getSelectedItem();
        String[] attributelist = {"seconds", "action", "project"};
        String[] valuelist = {idleCheckBox.isSelected() ? idleField.getText() : "0",
        projectIdleRadioButton.isSelected() ? "project" : "pause",
        projectIdleComboItem == null ? "" : projectIdleComboItem.toString()};
        prefs.saveFirst("idle", attributelist, valuelist);
        //Save skin settings
        prefs.removeFirstElement("skin");
        if(themeCheckBox.isSelected()) prefs.saveFirst("skin", "theme", themeField.getText());
        if(kdeCheckBox.isSelected()) prefs.saveFirst("skin", "kde", kdeField.getText());
        if(gtkCheckBox.isSelected()) prefs.saveFirst("skin", "gtk", gtkField.getText());
        
        //Write to file
        prefs.write();
      } catch (ParserConfigurationException e) {
        System.err.println("Error writing prefs file: "+e);
      } catch (Exception e) {
        System.err.println("Cannot write to prefs file: "+e);
        e.printStackTrace();
      }
    }
    
    long stringToMinutes(String secs) {
      long minutes;
      StringTokenizer timeWords = new StringTokenizer(secs, ":");
      // If time format is 1:30
      if(timeWords.countTokens() == 2) {
        minutes = Long.parseLong(timeWords.nextToken()) * 60;
        minutes += Long.parseLong(timeWords.nextToken());
      }
      // If time format is 90
      else if(timeWords.countTokens() == 1) {
        minutes =  Long.parseLong(timeWords.nextToken());
        // Else we have the wrong format
      } else minutes = 0L;
      return minutes;
    }
    
    public String minutesToString(long minutes) {
      long hours = minutes / 60L;
      minutes -= hours * 60L;
      
      String hourString = Long.toString(hours);
      String minuteString = minutes < 10 ? "0"+minutes : Long.toString(minutes);
      return ""+hourString+":"+minuteString;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel skinsLabel;
    private javax.swing.JTextField saveField;
    private javax.swing.JPanel skinsButtonPanel;
    private javax.swing.JTextField gtkField;
    private javax.swing.JCheckBox exportCheckBox;
    private javax.swing.JCheckBox countdownCheckBox;
    private javax.swing.JButton flagsCancelButton;
    private javax.swing.JCheckBox kdeCheckBox;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JCheckBox payCheckBox;
    private javax.swing.JPanel flagsButtonPanel;
    private javax.swing.JLabel flagLabel;
    private javax.swing.JRadioButton minuteButton;
    private javax.swing.JCheckBox gtkCheckBox;
    private javax.swing.JButton prefsOKButton;
    private javax.swing.JCheckBox showIconCheckBox;
    private javax.swing.JButton kdeBrowse;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextField countdownField;
    private javax.swing.JButton gtkBrowse;
    private javax.swing.JRadioButton secondButton;
    private javax.swing.JButton skinsOKButton;
    private javax.swing.JRadioButton pauseIdleRadioButton;
    private javax.swing.JPanel flagsInputPanel;
    private javax.swing.JRadioButton projectIdleRadioButton;
    private javax.swing.JPanel flagsPanel;
    private javax.swing.ButtonGroup timeFormatGroup;
    private javax.swing.JButton flagsOKButton;
    private javax.swing.JPanel prefsPanel;
    private javax.swing.JButton themeBrowse;
    private javax.swing.JTextField payField;
    private javax.swing.JTextField themeField;
    private javax.swing.JLabel timeFormatLabel;
    private javax.swing.JTextField kdeField;
    private javax.swing.JLabel payLabel;
    private javax.swing.JPanel prefsInputPanel;
    private javax.swing.JCheckBox billableCheckBox;
    private javax.swing.JPanel prefsButtonPanel;
    private javax.swing.JLabel save2Label;
    private javax.swing.JButton prefsCancelButton;
    private javax.swing.JCheckBox idleCheckBox;
    private javax.swing.JPanel skinsInputPanel;
    private javax.swing.JPanel skinsPanel;
    private javax.swing.JCheckBox themeCheckBox;
    private javax.swing.ButtonGroup idleGroup;
    private javax.swing.JComboBox projectIdleComboBox;
    private javax.swing.JTextField idleField;
    private javax.swing.JButton skinsCancelButton;
    private javax.swing.JLabel save1Label;
    private javax.swing.JLabel idleLabel;
    // End of variables declaration//GEN-END:variables
    
}

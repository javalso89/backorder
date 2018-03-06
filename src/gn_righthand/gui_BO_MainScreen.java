package gn_righthand;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import jxl.Sheet;
import jxl.write.WriteException;
import sun.misc.BASE64Encoder;
import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;


public class gui_BO_MainScreen extends javax.swing.JFrame {
    
    //VARIABLES DECLARATION
    
    private String sUser = "";//User's Oracle e-mail
    private String sPass = "";//Encrypted SSO pass
     private boolean bONLINE = false;
    
    //Data Bases Paths
    String sLocBoPlDBPath = "C:\\Users\\jfalvara\\Desktop\\Jav\\Progra\\DB Argentina Consults\\BackordersPlanning_DB.txt"; //DEVELOPMENT PHASE PATH
    String sLocBoArDBPath = "C:\\Users\\jfalvara\\Desktop\\Jav\\Progra\\DB Argentina Consults\\Backorders_DB.txt"; //DEVELOPMENT PHASE PATH
    private String sRemBoPlDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_Test_Env/BackordersPlanning_DB.txt"; //DEVELOPMENT PHASE PATH
    private String sRemBoArDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_Test_Env/Backorders_DB.txt";//DEVELOPMENT PHASE PATH
    
    /*
    String sLocBoPlDBPath = "C:\\Program Files (x86)\\Oracle Spares Planning\\GN Righthand\\Data Files\\BackordersPlanning_DB.txt"; //PRODUCTION PHASE PATH
    String sLocBoArDBPath = "C:\\Program Files (x86)\\Oracle Spares Planning\\GN Righthand\\Data Files\\Backorders_DB.txt"; //PRODUCTION PHASE PATH
    private String sRemBoPlDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB/BackordersPlanning_DB.txt"; //PRODUCTION PHASE PATH
    private String sRemBoArDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB/Backorders_DB.txt"; //PRODUCTION PHASE PATH
    */

    
    //Preparing the backorders table model variables
    javax.swing.table.DefaultTableModel tblModelBOPL = new javax.swing.table.DefaultTableModel();
    Object[] BOPLColumn = new Object [24];
    javax.swing.table.DefaultTableModel tblModelBOAR = new javax.swing.table.DefaultTableModel();
    Object[] BOARColumn = new Object [27];
    
    
    //Bidimentional String Array that will store all the data from an .xls ODS backorders file
    private String[][] xlsODSBOMatrix;
    
    //Creating variables to locate colums in the ODS Backorders imported file
    private int iDate_odsbo = -1, iSvRq_odsbo = -1, iTask_odsbo = -1, iISO_odsbo = -1, iItem_odsbo = -1, 
        iQty_odsbo = -1, iDesc_odsbo = -1, iTkSt_odsbo = -1, iPLC_odsbo = -1, iZone_odsbo = -1, iCntry_odsbo = -1;
    
    //ArrayList that will store the complete data base of Backorders Planning entries
    private ArrayList<cls_BO_Data> alBoPlDB = new ArrayList<>();
    //ArrayList that will store the search results on the Backorders Planning Data Base
    private ArrayList<cls_BO_Data> alBoPlSearchResults = new ArrayList<>();
    
    //ArrayList that will store the complete data base of Argentina Backorders entries
    private ArrayList<cls_BO_Data> alBoArDB = new ArrayList<>();
    //ArrayList that will store the search results on the Backorders Data Base
    private ArrayList<cls_BO_Data> alBoArSearchResults = new ArrayList<>();
    
    
    
    
    //Screen counters
    private int iBoPlQTY = 0;
    private int iBoArQTY = 0;
    
    //Backorders counters for new lines
    private int iCHK = 0;
    private int iNEW= 0;
    
    //Global Flag that indicates that the DB Manager tabs is showing the actual Data Base
    //When this flags goes False, it indicates that, what is shown in the screen, are just searching results
    boolean bBOPLFLAG = true;
    boolean bBOARFLAG = true;
    
    public gui_BO_MainScreen(boolean bONLINE, String sUser, String sPass) {
        initComponents();
        System.out.println("Starting Teacher Administrator Module");
        setLocationRelativeTo(null);
        
        this.sUser = sUser;
        this.sPass = sPass;
        this.bONLINE = bONLINE;
        
        
        if ( bONLINE == true ){
            System.out.println("User is working ONLINE. Loading remote Data Bases");
            //Loads the remote DBs into the ArrayList
            downloadRemBoPlDB();
            downloadRemBoArDB();
            //Loads historical QTYs in the corresponding jlabels
            loadRemBoPlQTYHist();
            loadRemBoArQTYHist();
        }
        else{
            System.out.println("User is working OFFLINE. Loading local Data Bases");
            //Loads the local DBs into the ArrayList
            loadBoPlDB();
            loadBoArDB();
            //Loads historical QTYs in the corresponding jlabels
            loadLocBoPlQTYHist();
            loadLocBoArQTYHist();
        }
        
        //Configures the jtables in order to receive and show data
        configBackordersPlTable();
        configBackordersArTable();
        //Loads the corresponding data from the ArrayLists into each jtable
        loadBoPlTable();
        loadBoArTable();
        
        
        
    }

    private gui_BO_MainScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    //TABLES HANDLING METHODS ******************
    
    //Prepares the JTable columns in order to receive the list of Backorders Planning from the Excel file
    private void configBackordersPlTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        tblModelBOPL.addColumn("Task status");
        tblModelBOPL.addColumn("BO Planner");
        tblModelBOPL.addColumn("Last review date");
        tblModelBOPL.addColumn("BO Req Date");
        tblModelBOPL.addColumn("Service Req");
        tblModelBOPL.addColumn("Task number");
        tblModelBOPL.addColumn("Order Number");
        tblModelBOPL.addColumn("PN#");
        tblModelBOPL.addColumn("QTY");
        tblModelBOPL.addColumn("Description");
        tblModelBOPL.addColumn("Alternatives");
        tblModelBOPL.addColumn("Revised ETA");
        tblModelBOPL.addColumn("Path");
        tblModelBOPL.addColumn("Improved ETA");
        tblModelBOPL.addColumn("BO status");
        tblModelBOPL.addColumn("Comments");
        tblModelBOPL.addColumn("ISO 1");
        tblModelBOPL.addColumn("AWB 1");
        tblModelBOPL.addColumn("ISO 2");
        tblModelBOPL.addColumn("AWB 2");
        tblModelBOPL.addColumn("ISO 3");
        tblModelBOPL.addColumn("AWB 3");
        tblModelBOPL.addColumn("Back Order E-mail Title");
        tblModelBOPL.addColumn("Tracking #");
        jtblBOPL.setModel(tblModelBOPL);
        //Allows the user to sort the items ina PartsListColumn
        jtblBOPL.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer FontRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblBOPL.getTableHeader();
        header.setBackground(Color.black);
        header.setForeground(Color.orange);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblBOPL.setAutoResizeMode(jtblBOPL.AUTO_RESIZE_OFF);
        jtblBOPL.setRowHeight(22);
        jtblBOPL.getColumnModel().getColumn(0).setPreferredWidth(115);
        jtblBOPL.getColumnModel().getColumn(0).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(1).setPreferredWidth(100);
        jtblBOPL.getColumnModel().getColumn(1).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(2).setPreferredWidth(110);
        jtblBOPL.getColumnModel().getColumn(2).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(3).setPreferredWidth(125);
        jtblBOPL.getColumnModel().getColumn(3).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(4).setPreferredWidth(100);
        jtblBOPL.getColumnModel().getColumn(4).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);        
        jtblBOPL.getColumnModel().getColumn(5).setPreferredWidth(100);
        jtblBOPL.getColumnModel().getColumn(5).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(6).setPreferredWidth(100);
        jtblBOPL.getColumnModel().getColumn(6).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(7).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(7).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(8).setPreferredWidth(60);
        jtblBOPL.getColumnModel().getColumn(8).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(9).setPreferredWidth(210);
        jtblBOPL.getColumnModel().getColumn(9).setResizable(false);
        //jtblBOPL.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(10).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(10).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(11).setPreferredWidth(110);
        jtblBOPL.getColumnModel().getColumn(11).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(12).setPreferredWidth(140);
        jtblBOPL.getColumnModel().getColumn(12).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(13).setPreferredWidth(110);
        jtblBOPL.getColumnModel().getColumn(13).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(13).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(14).setPreferredWidth(160);
        jtblBOPL.getColumnModel().getColumn(14).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(14).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(15).setPreferredWidth(200);
        jtblBOPL.getColumnModel().getColumn(15).setResizable(false);
        //jtblBOPL.getColumnModel().getColumn(15).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(16).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(16).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(16).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(17).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(17).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(17).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(18).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(18).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(18).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(19).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(19).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(19).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(20).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(20).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(20).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(21).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(21).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(21).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(22).setPreferredWidth(270);
        jtblBOPL.getColumnModel().getColumn(22).setResizable(false);
        //jtblBOPL.getColumnModel().getColumn(22).setCellRenderer(centerRenderer);
        jtblBOPL.getColumnModel().getColumn(23).setPreferredWidth(120);
        jtblBOPL.getColumnModel().getColumn(23).setResizable(false);
        jtblBOPL.getColumnModel().getColumn(23).setCellRenderer(centerRenderer);
        
        //Adding dropdown lists to columns
        TableColumn colBOS = jtblBOPL.getColumnModel().getColumn(14);
        JComboBox droplistBOS = new JComboBox();
        droplistBOS.addItem("NA");
        droplistBOS.addItem("Tekelec BO by Design");
        droplistBOS.addItem("ETA Improved");
        droplistBOS.addItem("Multi Hop sourcing");
        droplistBOS.addItem("Global Stock out");
        droplistBOS.addItem("Argentina BO");
        droplistBOS.addItem("NA (Accepted/Closed/Cancelled)");
        colBOS.setCellEditor(new DefaultCellEditor(droplistBOS));
        
        //Fecha para dropdown list
        
        TableColumn colBOS2 = jtblBOPL.getColumnModel().getColumn(2);
        JComboBox col2ListBox = new JComboBox();
        col2ListBox.addItem("NA");
        colBOS2.setCellEditor(new DefaultCellEditor(col2ListBox));
       
        col2ListBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                col2ListBox.addItem(devuelveFecha());      
            }
        });

    }
    //</editor-fold>
    
    private String devuelveFecha(){
        Date now=new Date();
        SimpleDateFormat fechaFormat = new SimpleDateFormat ("dd/mm/yyyy hh:mm");        
        return fechaFormat.format(now);
    }
    
    //Prepares the JTable columns in order to receive the list of Argentina Backorders from the Excel file
    private void configBackordersArTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        tblModelBOAR.addColumn("BO status");
        tblModelBOAR.addColumn("BO Req Date");
        tblModelBOAR.addColumn("Service Req");
        tblModelBOAR.addColumn("Task number");
        tblModelBOAR.addColumn("Order Number");
        tblModelBOAR.addColumn("PN#");
        tblModelBOAR.addColumn("QTY");
        tblModelBOAR.addColumn("Description");
        tblModelBOAR.addColumn("Task status");
        tblModelBOAR.addColumn("PLC");
        tblModelBOAR.addColumn("Part Criticality");
        tblModelBOAR.addColumn("Part Condition");
        tblModelBOAR.addColumn("Good New Search Assumption");
        tblModelBOAR.addColumn("Alternatives");
        tblModelBOAR.addColumn("Comments");
        tblModelBOAR.addColumn("ISO 1");
        tblModelBOAR.addColumn("AWB 1");
        tblModelBOAR.addColumn("ISO 2");
        tblModelBOAR.addColumn("AWB 2");
        tblModelBOAR.addColumn("ISO 3");
        tblModelBOAR.addColumn("AWB 3");
        tblModelBOAR.addColumn("ISO (MI2 > BUE)");
        tblModelBOAR.addColumn("AWB (MI2 > BUE)");
        tblModelBOAR.addColumn("SIMI (DJAI)");
        tblModelBOAR.addColumn("GSI Task Notes");
        tblModelBOAR.addColumn("Back Order E-mail Title");
        tblModelBOAR.addColumn("Tracking #");
        jtblBOAR.setModel(tblModelBOAR);
        //Allows the user to sort the items ina PartsListColumn
        jtblBOAR.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer FontRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblBOAR.getTableHeader();
        header.setBackground(Color.black);
        header.setForeground(Color.cyan);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblBOAR.setAutoResizeMode(jtblBOAR.AUTO_RESIZE_OFF);
        jtblBOAR.setRowHeight(22);
        jtblBOAR.getColumnModel().getColumn(0).setPreferredWidth(160);
        jtblBOAR.getColumnModel().getColumn(0).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(1).setPreferredWidth(90);
        jtblBOAR.getColumnModel().getColumn(1).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(2).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(2).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(3).setPreferredWidth(100);
        jtblBOAR.getColumnModel().getColumn(3).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(4).setPreferredWidth(100);
        jtblBOAR.getColumnModel().getColumn(4).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);        
        jtblBOAR.getColumnModel().getColumn(5).setPreferredWidth(100);
        jtblBOAR.getColumnModel().getColumn(5).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(6).setPreferredWidth(60);
        jtblBOAR.getColumnModel().getColumn(6).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(7).setPreferredWidth(210);
        jtblBOAR.getColumnModel().getColumn(7).setResizable(false);
        //jtblBOAR.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(8).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(8).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(9).setPreferredWidth(60);
        jtblBOAR.getColumnModel().getColumn(9).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(10).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(10).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(11).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(11).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(12).setPreferredWidth(250);
        jtblBOAR.getColumnModel().getColumn(12).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(13).setPreferredWidth(100);
        jtblBOAR.getColumnModel().getColumn(13).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(13).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(14).setPreferredWidth(200);
        jtblBOAR.getColumnModel().getColumn(14).setResizable(false);
        //jtblBOAR.getColumnModel().getColumn(14).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(15).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(15).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(15).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(16).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(16).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(16).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(17).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(17).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(17).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(18).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(18).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(18).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(19).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(19).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(19).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(20).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(20).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(20).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(21).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(21).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(21).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(22).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(22).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(22).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(23).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(23).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(23).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(24).setPreferredWidth(300);
        jtblBOAR.getColumnModel().getColumn(24).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(24).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(25).setPreferredWidth(300);
        jtblBOAR.getColumnModel().getColumn(25).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(25).setCellRenderer(centerRenderer);
        jtblBOAR.getColumnModel().getColumn(26).setPreferredWidth(120);
        jtblBOAR.getColumnModel().getColumn(26).setResizable(false);
        jtblBOAR.getColumnModel().getColumn(26).setCellRenderer(centerRenderer);
        
        //Adding dropdown lists to columns
        TableColumn colBOS = jtblBOAR.getColumnModel().getColumn(0);
        JComboBox droplistBOS = new JComboBox();
        droplistBOS.addItem("At MI2");
        droplistBOS.addItem("Alternative to Dispatch");
        droplistBOS.addItem("Available at BUE");
        droplistBOS.addItem("CIBU Approval Process");
        droplistBOS.addItem("Closed");
        droplistBOS.addItem("Good New Search");
        droplistBOS.addItem("In transit to BUE");
        droplistBOS.addItem("In transit to MI2");
        droplistBOS.addItem("SIMI/CIBU");
        droplistBOS.addItem("SIMI (DJAI) Approval Process");
        droplistBOS.addItem("Tekelec Part");
        droplistBOS.addItem("Unable to support Part");
        colBOS.setCellEditor(new DefaultCellEditor(droplistBOS));
    }
    //</editor-fold>
    
    //Loads the information from the Backorders Planning Data Base ArrayList into the corresponding JTable
    private void loadBoPlTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Loading data from Backorders Planning ArralyList into screen JTable");
        String sMode = "";
        if ( this.bONLINE == true ){sMode = "Rem.";}
        else{sMode = "Loc.";}
        for ( cls_BO_Data tmp: this.alBoPlDB )
        {
            BOPLColumn[0] = tmp.getTkSt();
            BOPLColumn[1] = tmp.getPlan();
            BOPLColumn[2] = tmp.getRvDt();
            BOPLColumn[3] = tmp.getDate();
            BOPLColumn[4] = tmp.getSvRq();
            BOPLColumn[5] = tmp.getTask();
            BOPLColumn[6] = tmp.getISO();
            BOPLColumn[7] = tmp.getItem();
            BOPLColumn[8] = tmp.getQty();
            BOPLColumn[9] = tmp.getDesc();
            BOPLColumn[10] = tmp.getAlts();
            BOPLColumn[11] = tmp.getReta();
            BOPLColumn[12] = tmp.getPath();
            BOPLColumn[13] = tmp.getIeta();
            BOPLColumn[14] = tmp.getBSta();
            BOPLColumn[15] = tmp.getComm();
            BOPLColumn[16] = tmp.getISO1();
            BOPLColumn[17] = tmp.getAwb1();
            BOPLColumn[18] = tmp.getISO2();
            BOPLColumn[19] = tmp.getAwb2();
            BOPLColumn[20] = tmp.getISO3();
            BOPLColumn[21] = tmp.getAwb3();
            BOPLColumn[22] = tmp.getBOMT();
            BOPLColumn[23] = tmp.getTrak();
            tblModelBOPL.addRow(BOPLColumn);
            jtblBOPL.setModel(tblModelBOPL);
        }
        this.bBOPLFLAG = true;
        this.jlblBOPLFlag.setText("<html>Showing: <font color='green'>BO "+ sMode + " Data Base</font></html>");
        System.out.println("Backorders Planning ArrayList loaded in the Backorders Data Base's JTable");
    }
    //</editor-fold>
    
    //Loads the information from the Argentina Backorders Data Base ArrayList into the corresponding JTable
    private void loadBoArTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Loading data from Argentina Backorders ArralyList into screen JTable");
        String sMode = "";
        if ( this.bONLINE == true ){sMode = "Rem.";}
        else{sMode = "Loc.";}
        for ( cls_BO_Data tmp: this.alBoArDB )
        {
            BOARColumn[0] = tmp.getBSta();
            BOARColumn[1] = tmp.getDate();
            BOARColumn[2] = tmp.getSvRq();
            BOARColumn[3] = tmp.getTask();
            BOARColumn[4] = tmp.getISO();
            BOARColumn[5] = tmp.getItem();
            BOARColumn[6] = tmp.getQty();
            BOARColumn[7] = tmp.getDesc();
            BOARColumn[8] = tmp.getTkSt();
            BOARColumn[9] = tmp.getPLC();
            BOARColumn[10] = tmp.getCrit();
            BOARColumn[11] = tmp.getCond();
            BOARColumn[12] = tmp.getSrAs();
            BOARColumn[13] = tmp.getAlts();
            BOARColumn[14] = tmp.getComm();
            BOARColumn[15] = tmp.getISO1();
            BOARColumn[16] = tmp.getAwb1();
            BOARColumn[17] = tmp.getISO2();
            BOARColumn[18] = tmp.getAwb2();
            BOARColumn[19] = tmp.getISO3();
            BOARColumn[20] = tmp.getAwb3();
            BOARColumn[21] = tmp.getIsMB();
            BOARColumn[22] = tmp.getAwMB();
            BOARColumn[23] = tmp.getSIMI();
            BOARColumn[24] = tmp.getTkNt();
            BOARColumn[25] = tmp.getBOMT();
            BOARColumn[26] = tmp.getTrak();
            tblModelBOAR.addRow(BOARColumn);
            jtblBOAR.setModel(tblModelBOAR);
        }
        this.bBOARFLAG = true;
        this.jlblBOARFlag.setText("<html>Showing: <font color='green'>Argentina BO "+ sMode + " DB</font></html>");
        System.out.println("Backorders Planning ArrayList loaded in the Backorders Data Base's JTable");
    }
    //</editor-fold>
    
    //Cleans the Backorders Planning JTable
    private void cleanBoPlTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = this.tblModelBOPL.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelBOPL.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the Backorders Planning Table \n" + e, "BACKORDERS TOOL MSG", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Cleans the Argentina Backorders JTable
    private void cleanBoArTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = this.tblModelBOAR.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelBOAR.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the Argentina Backorders Table \n" + e, "BACKORDERS TOOL MSG", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Adds new Lines at the end of the Backorder Planning screen table
    private void addBoPlNewLine(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        alBoPlDB.add(new cls_BO_Data("NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA"));
        cleanBoPlTable();
        loadBoPlTable();
        Rectangle cellBounds = this.jtblBOPL.getCellRect(jtblBOPL.getRowCount() - 1, 0, true);
        jtblBOPL.scrollRectToVisible(cellBounds);
    }
    //</editor-fold>  
    
    //Adds new Lines at the end of the Argentina Backorder screen table
    private void addBoArNewLine(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        alBoArDB.add(new cls_BO_Data("NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA"));
        cleanBoArTable();
        loadBoArTable();
        Rectangle cellBounds = this.jtblBOAR.getCellRect(jtblBOAR.getRowCount() - 1, 0, true);
        jtblBOAR.scrollRectToVisible(cellBounds);
    }
    //</editor-fold>  
    
    
    //DATA BASE CONTROL METHODS ******************
    
    //Updates the Backorders Planning Data Base ArrayList according with the changes on the screen Jtable
    private void updateBoPl_al(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Flag value:  "+ this.bBOPLFLAG);
        // If true: Whole DB; False: Search DB
        if ( bBOPLFLAG == true ) {//Whole Data Base
            //Cleans the DB ArrayList
            alBoPlDB.clear();
            String sTkSt="", sPlan="", sRvDt="", sDate="", sSvRq="",
                    sTask="", sISO="", sItem="", sQty="", sDesc="",
                    sAlts="", sReta="", sPath="", sIeta="", sBSta="",
                    sComm="", sISO1="", sAwb1="", sISO2="", sAwb2="", 
                    sISO3="", sAwb3="", sBOMT="", sTrak=""; 
            //Captures each line on the table screen
            for ( int i=0; i < this.jtblBOPL.getRowCount(); i++ )
            {
                sTkSt = jtblBOPL.getValueAt(i, 0).toString();
                sPlan = jtblBOPL.getValueAt(i, 1).toString();
                sRvDt = jtblBOPL.getValueAt(i, 2).toString();
                sDate = jtblBOPL.getValueAt(i, 3).toString();
                sSvRq = jtblBOPL.getValueAt(i, 4).toString();
                sTask = jtblBOPL.getValueAt(i, 5).toString();
                sISO = jtblBOPL.getValueAt(i, 6).toString();
                sItem = jtblBOPL.getValueAt(i, 7).toString();
                sQty = jtblBOPL.getValueAt(i, 8).toString();
                sDesc = jtblBOPL.getValueAt(i, 9).toString();
                sAlts = jtblBOPL.getValueAt(i, 10).toString();
                sReta = jtblBOPL.getValueAt(i, 11).toString();
                sPath = jtblBOPL.getValueAt(i, 12).toString();
                sIeta = jtblBOPL.getValueAt(i, 13).toString();
                sBSta = jtblBOPL.getValueAt(i, 14).toString();
                sComm = jtblBOPL.getValueAt(i, 15).toString();
                sISO1 = jtblBOPL.getValueAt(i, 16).toString();
                sAwb1 = jtblBOPL.getValueAt(i, 17).toString();
                sISO2 = jtblBOPL.getValueAt(i, 18).toString();
                sAwb2 = jtblBOPL.getValueAt(i, 19).toString();
                sISO3 = jtblBOPL.getValueAt(i, 20).toString();
                sAwb3 = jtblBOPL.getValueAt(i, 21).toString();
                sBOMT = jtblBOPL.getValueAt(i, 22).toString();
                sTrak = jtblBOPL.getValueAt(i, 23).toString();
                //Creates new lines in the ArrayList for every captured line from the screen table (this includes the changes made)
                alBoPlDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        "NA", "NA", "NA", "NA", sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        "NA", "NA", "NA", "NA", sBOMT, sTrak, "NA", sPlan, sRvDt, sReta, sPath, sIeta, "NA", "NA", "NA", "NA"));
            }
        }
        else {//Search results DB
            //Updates the search results ArrayList with the results on the screen
            updateBoPlSearchResults();
            //Updates the main DB ArrayList accordijng with the Search Results ArrayList
            for ( cls_BO_Data tmp: this.alBoPlSearchResults ) {
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setTkSt(tmp.getTkSt());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setPlan(tmp.getPlan());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setRvDt(tmp.getRvDt());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setDate(tmp.getDate());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setSvRq(tmp.getSvRq());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setISO(tmp.getISO());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setItem(tmp.getItem());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setQty(tmp.getQty());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setDesc(tmp.getDesc());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setAlts(tmp.getAlts());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setReta(tmp.getReta());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setPath(tmp.getPath());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setIeta(tmp.getIeta());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setBSta(tmp.getBSta());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setComm(tmp.getComm());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setISO1(tmp.getISO1());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setAwb1(tmp.getAwb1());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setISO2(tmp.getISO2());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setAwb2(tmp.getAwb2());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setISO3(tmp.getISO3());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setAwb3(tmp.getAwb3());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setBOMT(tmp.getBOMT());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setTrak(tmp.getTrak());
                alBoPlDB.get(Integer.valueOf(tmp.getPosi())).setPosi("NA");
            }
        }
        JOptionPane.showMessageDialog(this, "The Planning Backorders Data Base has been updated");
        cleanBoPlTable();
        loadBoPlTable();
    }
    //</editor-fold>
    
    //Updates the Argentina Backorders Data Base ArrayList according with the changes on the screen Jtable
    private void updateBoAr_al(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Flag value:  "+ this.bBOARFLAG);
        // If true: Whole DB; False: Search DB
        if ( bBOARFLAG == true ) {
            alBoArDB.clear();
            String sBSta="", sDate="", sSvRq="", sTask="", sISO="",
                sItem="", sQty="", sDesc="", sTkSt="", sPLC="", sCrit="",
                sCond="", sSrAs="", sAlts="", sComm="", sISO1="", sAwb1="", 
                sISO2="", sAwb2="", sISO3="", sAwb3="", sIsMB="", sAwMB="", 
                sSIMI="", sTkNt="", sBOMT="", sTrak=""; 
            
            for ( int i=0; i < this.jtblBOAR.getRowCount(); i++ )
            {
                sBSta = jtblBOAR.getValueAt(i, 0).toString();
                sDate = jtblBOAR.getValueAt(i, 1).toString();
                sSvRq = jtblBOAR.getValueAt(i, 2).toString();
                sTask = jtblBOAR.getValueAt(i, 3).toString();
                sISO = jtblBOAR.getValueAt(i, 4).toString();
                sItem = jtblBOAR.getValueAt(i, 5).toString();
                sQty = jtblBOAR.getValueAt(i, 6).toString();
                sDesc = jtblBOAR.getValueAt(i, 7).toString();
                sTkSt = jtblBOAR.getValueAt(i, 8).toString();
                sPLC = jtblBOAR.getValueAt(i, 9).toString();
                sCrit = jtblBOAR.getValueAt(i, 10).toString();
                sCond = jtblBOAR.getValueAt(i, 11).toString();
                sSrAs = jtblBOAR.getValueAt(i, 12).toString();
                sAlts = jtblBOAR.getValueAt(i, 13).toString();
                sComm = jtblBOAR.getValueAt(i, 14).toString();
                sISO1 = jtblBOAR.getValueAt(i, 15).toString();
                sAwb1 = jtblBOAR.getValueAt(i, 16).toString();
                sISO2 = jtblBOAR.getValueAt(i, 17).toString();
                sAwb2 = jtblBOAR.getValueAt(i, 18).toString();
                sISO3 = jtblBOAR.getValueAt(i, 19).toString();
                sAwb3 = jtblBOAR.getValueAt(i, 20).toString();
                sIsMB = jtblBOAR.getValueAt(i, 21).toString();
                sAwMB = jtblBOAR.getValueAt(i, 22).toString(); 
                sSIMI = jtblBOAR.getValueAt(i, 23).toString();
                sTkNt = jtblBOAR.getValueAt(i, 24).toString();
                sBOMT = jtblBOAR.getValueAt(i, 25).toString();
                sTrak = jtblBOAR.getValueAt(i, 26).toString();
                alBoArDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA","NA", "NA", "NA", "NA", "NA", "NA", "NA"));
            }
        }
        else {
            //Updates the search results ArrayList with the results on the screen
            updateBoArSearchResults();
            
            for ( cls_BO_Data tmp: this.alBoArSearchResults ) {
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setBSta(tmp.getBSta());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setDate(tmp.getDate());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setSvRq(tmp.getSvRq());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setTask(tmp.getTask());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setISO(tmp.getISO());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setItem(tmp.getItem());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setQty(tmp.getQty());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setDesc(tmp.getDesc());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setTkSt(tmp.getTkSt());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setPLC(tmp.getPLC());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setCrit(tmp.getCrit());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setCond(tmp.getCond());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setSrAs(tmp.getSrAs());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setAlts(tmp.getAlts());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setComm(tmp.getComm());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setISO1(tmp.getISO1());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setAwb1(tmp.getAwb1());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setISO2(tmp.getISO2());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setAwb2(tmp.getAwb2());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setISO3(tmp.getISO3());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setAwb3(tmp.getAwb3());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setIsMB(tmp.getIsMB());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setAwMB(tmp.getAwMB());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setSIMI(tmp.getSIMI());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setTkNt(tmp.getTkNt());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setBOMT(tmp.getBOMT());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setTrak(tmp.getTrak());
                alBoArDB.get(Integer.valueOf(tmp.getPosi())).setPosi("NA");
            }
        }
        JOptionPane.showMessageDialog(this, "The Argentina BO Data Base has been updated");
        cleanBoPlTable();
        loadBoPlTable();
    }
    //</editor-fold>
    
    //LOCAL DBS
    
    //Loads the Backorders Planning Data Base from a local .txt file into the Backorders data base ArrayList
    private void loadBoPlDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        this.alBoPlDB.clear();
        this.iBoPlQTY = 0;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sTkSt="", sPlan="", sRvDt="", sDate="", sSvRq="",
                    sTask="", sISO="", sItem="", sQty="", sDesc="",
                    sAlts="", sReta="", sPath="", sIeta="", sBSta="",
                    sComm="", sISO1="", sAwb1="", sISO2="", sAwb2="", 
                    sISO3="", sAwb3="", sBOMT="", sTrak=""; 
        try
        {
            fDataBase = new File(sLocBoPlDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("BO LINES") )
            {
                String [] position = chain.split("\t");
                sTkSt = position[0];
                    sPlan = position[1];
                    sRvDt = position[2];
                    sDate = position[3];
                    sSvRq = position[4];
                    sTask = position[5];
                    sISO = position[6];
                    sItem = position[7];
                    sQty = position[8];
                    sDesc = position[9];
                    sAlts = position[10];
                    sReta = position[11];
                    sPath = position[12];
                    sIeta = position[13];
                    sBSta = position[14];
                    sComm = position[15];
                    sISO1 = position[16];
                    sAwb1 = position[17];
                    sISO2 = position[18];
                    sAwb2 = position[19];
                    sISO3 = position[20];
                    sAwb3 = position[21];
                    sBOMT = position[22];
                    sTrak = position[23];
                alBoPlDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        "NA", "NA", "NA", "NA", sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        "NA", "NA", "NA", "NA", sBOMT, sTrak, "NA", sPlan, sRvDt, sReta, sPath, sIeta, "NA", "NA", "NA", "NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iBoPlQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders Planning local Data Base:\n"
                    + e, "ERROR - loadBoPlDB()", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Loads the Argentina Backorders Data Base from a local .txt file into the Backorders data base ArrayList
    private void loadBoArDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        this.alBoArDB.clear();
        this.iBoArQTY = 0;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sBSta="", sDate="", sSvRq="", sTask="", sISO="",
                sItem="", sQty="", sDesc="", sTkSt="", sPLC="", sCrit="",
                sCond="", sSrAs="", sAlts="", sComm="", sISO1="", sAwb1="", 
                sISO2="", sAwb2="", sISO3="", sAwb3="", sIsMB="", sAwMB="", 
                sSIMI="", sTkNt="", sBOMT="", sTrak=""; 
        try
        {
            fDataBase = new File(sLocBoArDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("BO LINES") )
            {
                String [] position = chain.split("\t");
                sBSta = position[0];
                sDate = position[1];
                sSvRq = position[2];
                sTask = position[3];        
                sISO = position[4];
                sItem = position[5];
                sQty = position[6];
                sDesc = position[7];
                sTkSt = position[8];
                sPLC = position[9];
                sCrit = position[10];
                sCond = position[11];
                sSrAs = position[12];
                sAlts = position[13];
                sComm = position[14];
                sISO1 = position[15];
                sAwb1 = position[16];
                sISO2 = position[17];
                sAwb2 = position[18];
                sISO3 = position[19];
                sAwb3 = position[20];
                sIsMB = position[21];
                sAwMB = position[22];
                sSIMI = position[23];
                sTkNt = position[24];
                sBOMT = position[25];
                sTrak = position[26];
                alBoArDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA","NA", "NA", "NA", "NA", "NA", "NA", "NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iBoArQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders Planning local Data Base:\n"
                    + e, "ERROR - loadBoArDB()", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Updates the local .TXT Backorders Planning Data Base file directly from the Backorders Planning Data Base ArrayList
    public void updateBoPlDB_txt(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        try
        {
            File fDataBase;
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter wr = null;
            
            fDataBase = new File (this.sLocBoPlDBPath); //points to the local .txt Backorders data base file
            fw = new FileWriter(fDataBase);
            bw = new BufferedWriter(fw);
            wr = new PrintWriter(bw);
            
            //Reads, line by line, all the consults that are currently in the Data Base Array List
            for(cls_BO_Data tmp: this.alBoPlDB)
            {
                wr.println( tmp.getTkSt() + "\t" 
                        + tmp.getPlan() + "\t" 
                        + tmp.getRvDt() + "\t" 
                        + tmp.getDate() + "\t" 
                        + tmp.getTask() + "\t" 
                        + tmp.getISO() + "\t" 
                        + tmp.getItem() + "\t"
                        + tmp.getQty() + "\t"
                        + tmp.getDesc() + "\t"
                        + tmp.getAlts() + "\t"
                        + tmp.getReta() + "\t"
                        + tmp.getPath() + "\t"
                        + tmp.getIeta() + "\t"
                        + tmp.getBSta() + "\t"
                        + tmp.getComm() + "\t"
                        + tmp.getISO1() + "\t"
                        + tmp.getAwb1() + "\t"
                        + tmp.getISO2() + "\t"
                        + tmp.getAwb2() + "\t"
                        + tmp.getISO3() + "\t"
                        + tmp.getAwb3() + "\t"
                        + tmp.getBOMT() + "\t"
                        + tmp.getTrak() );
            }
            iBoPlQTY = alBoPlDB.size();
            wr.println("BO LINES");
            wr.println(String.valueOf(iBoPlQTY));
            this.jlblBOPLDBsize.setText("<html>Data Base size:<br>" + iBoPlQTY + " lines</html>");
            wr.close();
            bw.close();
            fw.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(this,"There was an error while updating the local Backorders Planning .TXT Data Base:\n"
                + e, "ERROR - updateBoPlDB_txt()", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Updates the local .TXT Backorders Planning Data Base file directly from the Backorders Planning Data Base ArrayList
    public void updateBoArDB_txt(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    
        try
        {
            File fDataBase;
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter wr = null;
            
            fDataBase = new File (this.sLocBoArDBPath); //points to the local .txt Backorders data base file
            fw = new FileWriter(fDataBase);
            bw = new BufferedWriter(fw);
            wr = new PrintWriter(bw);
            
            //Reads, line by line, all the consults that are currently in the Data Base Array List
            for(cls_BO_Data tmp: this.alBoArDB)
            {
                wr.println( tmp.getBSta() + "\t" 
                        + tmp.getDate() + "\t" 
                        + tmp.getSvRq() + "\t" 
                        + tmp.getTask() + "\t" 
                        + tmp.getISO() + "\t" 
                        + tmp.getItem() + "\t"
                        + tmp.getQty() + "\t"
                        + tmp.getDesc() + "\t"
                        + tmp.getTkSt() + "\t"
                        + tmp.getPLC() + "\t"
                        + tmp.getCrit() + "\t"
                        + tmp.getCond() + "\t"
                        + tmp.getSrAs() + "\t"
                        + tmp.getAlts() + "\t"
                        + tmp.getComm() + "\t"
                        + tmp.getISO1() + "\t"
                        + tmp.getAwb1() + "\t"
                        + tmp.getISO2() + "\t"
                        + tmp.getAwb2() + "\t"
                        + tmp.getISO3() + "\t"
                        + tmp.getAwb3() + "\t"
                        + tmp.getIsMB() + "\t"
                        + tmp.getAwMB() + "\t"
                        + tmp.getSIMI() + "\t"
                        + tmp.getTkNt() + "\t"
                        + tmp.getBOMT() + "\t"
                        + tmp.getTrak() );
            }
            iBoArQTY = alBoArDB.size();
            wr.println("BO LINES");
            wr.println(String.valueOf(iBoArQTY));
            this.jlblBOARDBsize.setText("<html>Data Base size:<br>" + iBoArQTY + " lines</html>");
            wr.close();
            bw.close();
            fw.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(this,"There was an error while updating the local Argentina Backorders .TXT Data Base:\n"
                + e, "ERROR - updateBoArDB_txt()", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Loads and shows the Backorders Planning QTY history in the Data Base main screen from the local .TXT Data Base
    private void loadLocBoPlQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sBOQTY;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocBoPlDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("BO LINES") )
            {
                chain = br.readLine();
            }
            sBOQTY = br.readLine();
            br.close();
            fr.close();
            this.jlblBOPLDBsize.setText("<html>Data Base size:<br>" + sBOQTY + " lines</html>");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders local Data Base:\n"
                    + e, "ERROR - loadLocBoPlQTYHist()", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Loads and shows the Argentina Backorders QTY history in the Data Base main screen from the local .TXT Data Base
    private void loadLocBoArQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sBOQTY;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocBoArDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("BO LINES") )
            {
                chain = br.readLine();
            }
            sBOQTY = br.readLine();
            br.close();
            fr.close();
            this.jlblBOARDBsize.setText("<html>Data Base size:<br>" + sBOQTY + " lines</html>");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders local Data Base:\n"
                    + e, "ERROR - loadLocBoArQTYHist()", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Gets the Backorders Planning QTY history from the local .TXT Data Base and RETURNS its value
    private String getBoPlQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sBOQTY="";
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocBoPlDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("BO LINES") )
            {
                chain = br.readLine();
            }
            sBOQTY = br.readLine();
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders Planning local Data Base \n"
                    + e, "ERROR - getBoPlQTYHist()", JOptionPane.ERROR_MESSAGE );
        }
        return sBOQTY;
    }
    //</editor-fold>
    
    //Gets the Argentina Backorders QTY history from the local .TXT Data Base and RETURNS its value
    private String getBoArQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sBOQTY="";
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocBoArDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("BO LINES") )
            {
                chain = br.readLine();
            }
            sBOQTY = br.readLine();
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Argentina Backorders local Data Base \n"
                    + e, "ERROR - getBoArQTYHist()", JOptionPane.ERROR_MESSAGE );
        }
        return sBOQTY;
    }
    //</editor-fold>
    
    
    //REMOTE DBS
    
    //Loads the Backorders Planning Data Base from the Beehive .txt Backup file into the active ArrayList 
    private void downloadRemBoPlDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Clears the current ArrayList for the Data Base
        alBoPlDB.clear();
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain, sTkSt="", sPlan="", sRvDt="", sDate="", sSvRq="",
                    sTask="", sISO="", sItem="", sQty="", sDesc="",
                    sAlts="", sReta="", sPath="", sIeta="", sBSta="",
                    sComm="", sISO1="", sAwb1="", sISO2="", sAwb2="", 
                    sISO3="", sAwb3="", sBOMT="", sTrak="";
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemBoPlDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
            //If the .txt file is readable then it creates an input stream
            if (urlConn != null && urlConn.getInputStream() != null)
            {
                System.out.println("Remote connection established to Backorders Planning Data Base.\nProceeding to download.");
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("BO LINES") ){
                    String [] position = chain.split("\t");
                    sTkSt = position[0];
                    sPlan = position[1];
                    sRvDt = position[2];
                    sDate = position[3];
                    sSvRq = position[4];
                    sTask = position[5];
                    sISO = position[6];
                    sItem = position[7];
                    sQty = position[8];
                    sDesc = position[9];
                    sAlts = position[10];
                    sReta = position[11];
                    sPath = position[12];
                    sIeta = position[13];
                    sBSta = position[14];
                    sComm = position[15];
                    sISO1 = position[16];
                    sAwb1 = position[17];
                    sISO2 = position[18];
                    sAwb2 = position[19];
                    sISO3 = position[20];
                    sAwb3 = position[21];
                    sBOMT = position[22];
                    sTrak = position[23];
                    alBoPlDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        "NA", "NA", "NA", "NA", sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        "NA", "NA", "NA", "NA", sBOMT, sTrak, "NA", sPlan, sRvDt, sReta, sPath, sIeta, "NA", "NA", "NA", "NA"));
                    chain = br.readLine();
                }
                chain = br.readLine();
                iBoPlQTY = Integer.valueOf(chain);
                System.out.println("Remote Backorders Planning Data Base downloaded.\nClosing threads.");
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Planning Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team\n" + e,"ERROR - downloadRemBoPlDB()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads the Argentina Backorders Data Base from the Beehive .txt Backup file into the active ArrayList 
    private void downloadRemBoArDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Clears the current ArrayList for the Data Base
        alBoArDB.clear();
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain, sBSta="", sDate="", sSvRq="", sTask="", sISO="",
                sItem="", sQty="", sDesc="", sTkSt="", sPLC="", sCrit="",
                sCond="", sSrAs="", sAlts="", sComm="", sISO1="", sAwb1="", 
                sISO2="", sAwb2="", sISO3="", sAwb3="", sIsMB="", sAwMB="", 
                sSIMI="", sTkNt="", sBOMT="", sTrak="";
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemBoArDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
            //If the .txt file is readable then it creates an input stream
            if (urlConn != null && urlConn.getInputStream() != null)
            {
                System.out.println("Remote connection established to Argentina Backorders Data Base.\nProceeding to download.");
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("BO LINES") ){
                    String [] position = chain.split("\t");
                    sBSta = position[0];
                    sDate = position[1];
                    sSvRq = position[2];
                    sTask = position[3];        
                    sISO = position[4];
                    sItem = position[5];
                    sQty = position[6];
                    sDesc = position[7];
                    sTkSt = position[8];
                    sPLC = position[9];
                    sCrit = position[10];
                    sCond = position[11];
                    sSrAs = position[12];
                    sAlts = position[13];
                    sComm = position[14];
                    sISO1 = position[15];
                    sAwb1 = position[16];
                    sISO2 = position[17];
                    sAwb2 = position[18];
                    sISO3 = position[19];
                    sAwb3 = position[20];
                    sIsMB = position[21];
                    sAwMB = position[22];
                    sSIMI = position[23];
                    sTkNt = position[24];
                    sBOMT = position[25];
                    sTrak = position[26];
                    alBoArDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA","NA", "NA", "NA", "NA", "NA", "NA", "NA"));
                    chain = br.readLine();
                }
                chain = br.readLine();
                iBoArQTY = Integer.valueOf(chain);
                System.out.println("Remote Argentina Backorders Data Base downloaded.\nClosing threads.");
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Argentina Backorders Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team" + e,"ERROR - loadRemBoArDB()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Saves the Backorders Planning Data Base from the active ArrayList into the Beehive .txt remote file
    private void uploadRemBoPlDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        URLConnection urlConn = null;
        OutputStreamWriter osw = null;
        try
        {
            System.out.println("Opening URL connection to the Backorders Planning Data Base");
            //Opens the URL connection
            URL url = new URL(sRemBoPlDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "text/plain");
            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            System.out.println("Validating credentials");
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
                        
            //Checks if the URL connection is opened and if there is an output stream available
            if (urlConn != null && urlConn.getOutputStream() != null){
                System.out.println("The URL connection to the Backorders Data Base is up");
                System.out.println("The output stream buffer is available");
                System.out.println("Starting to upload Backorders lines");
                osw = new OutputStreamWriter(urlConn.getOutputStream(),Charset.defaultCharset());
                for(cls_BO_Data tmp: this.alBoPlDB){
                    osw.write(tmp.getTkSt() + "\t" 
                        + tmp.getPlan() + "\t" 
                        + tmp.getRvDt() + "\t"
                        + tmp.getDate() + "\t"
                        + tmp.getSvRq() + "\t"
                        + tmp.getTask() + "\t" 
                        + tmp.getISO() + "\t" 
                        + tmp.getItem() + "\t"
                        + tmp.getQty() + "\t"
                        + tmp.getDesc() + "\t"
                        + tmp.getAlts() + "\t"
                        + tmp.getReta() + "\t"
                        + tmp.getPath() + "\t"
                        + tmp.getIeta() + "\t"
                        + tmp.getBSta() + "\t"
                        + tmp.getComm() + "\t"
                        + tmp.getISO1() + "\t"
                        + tmp.getAwb1() + "\t"
                        + tmp.getISO2() + "\t"
                        + tmp.getAwb2() + "\t"
                        + tmp.getISO3() + "\t"
                        + tmp.getAwb3() + "\t"
                        + tmp.getBOMT() + "\t"
                        + tmp.getTrak() + "\n");
                }
                iBoPlQTY = alBoPlDB.size();
                osw.write("BO LINES\n");
                osw.write(String.valueOf(iBoPlQTY) + "\n");
                jlblBOPLDBsize.setText("<html>Data Base size:<br>" + iBoPlQTY + " lines</html>");
            }
            osw.flush();
            urlConn.getContentLengthLong();
            System.out.println("The remote Backorders Data Base has been updated.\nClosing output stream.");
        }
        catch (Exception e)
        {
//            JOptionPane.showMessageDialog(this,"Exception while writing into the remote Backorders Data Base\n" +
//                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
//                    "If the issue persists please contact the CR Spares Planning Team\n" + e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Saves the Agentina Backorders Data Base from the active ArrayList into the Beehive .txt remote file
    private void uploadRemBoArDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        URLConnection urlConn = null;
        OutputStreamWriter osw = null;
        try
        {
            System.out.println("Opening URL connection to the Argentina Backorders Data Base");
            //Opens the URL connection
            URL url = new URL(sRemBoArDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "text/plain");
            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            System.out.println("Validating credentials");
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
                        
            //Checks if the URL connection is opened and if there is an output stream available
            if (urlConn != null && urlConn.getOutputStream() != null){
                System.out.println("The URL connection to the Argentina Backorders Data Base is up");
                System.out.println("The output stream buffer is available");
                System.out.println("Starting to upload Argentina Backorders lines");
                osw = new OutputStreamWriter(urlConn.getOutputStream(),Charset.defaultCharset());
                for(cls_BO_Data tmp: this.alBoArDB){
                    osw.write(tmp.getBSta() + "\t" 
                        + tmp.getDate() + "\t" 
                        + tmp.getSvRq() + "\t" 
                        + tmp.getTask() + "\t" 
                        + tmp.getISO() + "\t" 
                        + tmp.getItem() + "\t"
                        + tmp.getQty() + "\t"
                        + tmp.getDesc() + "\t"
                        + tmp.getTkSt() + "\t"
                        + tmp.getPLC() + "\t"
                        + tmp.getCrit() + "\t"
                        + tmp.getCond() + "\t"
                        + tmp.getSrAs() + "\t"
                        + tmp.getAlts() + "\t"
                        + tmp.getComm() + "\t"
                        + tmp.getISO1() + "\t"
                        + tmp.getAwb1() + "\t"
                        + tmp.getISO2() + "\t"
                        + tmp.getAwb2() + "\t"
                        + tmp.getISO3() + "\t"
                        + tmp.getAwb3() + "\t"
                        + tmp.getIsMB() + "\t"
                        + tmp.getAwMB() + "\t"
                        + tmp.getSIMI() + "\t"
                        + tmp.getTkNt() + "\t"
                        + tmp.getBOMT() + "\t"
                        + tmp.getTrak() + "\n");
                }
                iBoArQTY = alBoArDB.size();
                osw.write("BO LINES\n");
                osw.write(String.valueOf(iBoArQTY) + "\n");
                jlblBOARDBsize.setText("<html>Data Base size:<br>" + iBoArQTY + " lines</html>");
            }
            osw.flush();
            urlConn.getContentLengthLong();
            System.out.println("The remote Argentina Backorders Data Base has been updated.\nClosing output stream.");
        }
        catch (Exception e)
        {
//            JOptionPane.showMessageDialog(this,"Exception while writing into the remote Backorders Data Base\n" +
//                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
//                    "If the issue persists please contact the CR Spares Planning Team\n" + e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads and shows the Backorders Planning QTY history in the Data Base main screen from the remote .TXT Data Base
    private void loadRemBoPlQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain;
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemBoPlDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
            //If the .txt file is readable then it creates an input stream
            if (urlConn != null && urlConn.getInputStream() != null)
            {
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("BO LINES") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                iBoPlQTY = Integer.valueOf(chain);
            }
            br.close();
            isr.close();
            jlblBOPLDBsize.setText("<html>Data Base size:<br>" + iBoPlQTY + " lines</html>");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Data Base QTY\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team:\n" + e,"ERROR - loadRemBoPlQTYHist()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads and shows the Argentina Backorders QTY history in the Data Base main screen from the remote .TXT Data Base
    private void loadRemBoArQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain;
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemBoArDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
            //If the .txt file is readable then it creates an input stream
            if (urlConn != null && urlConn.getInputStream() != null)
            {
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("BO LINES") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                iBoArQTY = Integer.valueOf(chain);
            }
            br.close();
            isr.close();
            jlblBOARDBsize.setText("<html>Data Base size:<br>" + iBoArQTY + " lines</html>");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Data Base QTY\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team:\n" + e,"ERROR - loadRemBoArQTYHist()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Gets the Backorders Planning QTY history from the Beehive remote .TXT Backorders Data Base and RETUNRS its value
    private String getRemBoPlQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        String sBOQTY="";
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain;
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemBoPlDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
            //If the .txt file is readable then it creates an input stream
            if (urlConn != null && urlConn.getInputStream() != null)
            {
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("BO LINES") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                sBOQTY = chain;
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Data Base QTY\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team>\n" + e,"ERROR - getRemBoPlQTYHist()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return sBOQTY;
    }
    //</editor-fold>
    
    //Gets the Argentina Backorders QTY history from the Beehive remote .TXT Backorders Data Base and RETUNRS its value
    private String getRemBoArQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        String sBOQTY="";
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain;
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemBoArDBPath);
            urlConn = (HttpURLConnection)url.openConnection();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = sUser + ":" + sPass;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
            //If the .txt file is readable then it creates an input stream
            if (urlConn != null && urlConn.getInputStream() != null)
            {
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("BO LINES") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                sBOQTY = chain;
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Data Base QTY\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team>\n" + e,"ERROR - getRemBoArQTYHist()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return sBOQTY;
    }
    //</editor-fold>
    
    
    
    
    
    //OPEN BACKORDERS EXCEL FILE HANDLING
    
    //Identifies the column numbers on the ODS Backorders imported Excel file
    private void locateODSBOColumns(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        //Reset column values
        iDate_odsbo = -1; iSvRq_odsbo = -1; iTask_odsbo = -1;
        iISO_odsbo = -1; iItem_odsbo = -1; iQty_odsbo = -1; 
        iDesc_odsbo = -1; iTkSt_odsbo = -1; iPLC_odsbo = -1; 
        iZone_odsbo = -1; iCntry_odsbo = -1;
        
        //FOR Cycle in order to identify the coumn number depending on the PartsListColumn name
        System.out.println("Detecting ODS Backorders Planning Matrix dimmentions.");
        System.out.println("Columns: " + xlsODSBOMatrix[0].length + " / Rows: " + xlsODSBOMatrix.length);
        System.out.println("Identifying columns");
        for ( int c=0; c<xlsODSBOMatrix[0].length; c++ )
        {
            if ( xlsODSBOMatrix[0][c].equals("ORDER_LINE_CREATION_DATE") ){iDate_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("SR_NUMBER") ){iSvRq_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("TASK_NUMBER") ){iTask_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("ORDER_NUMBER") ){iISO_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("PART_NUMBER") ){iItem_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("ORDERED_QUANTITY") ){iQty_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("ITEM_DESCRIPTION") ){iDesc_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("TASK_STATUS") ){iTkSt_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("UPLOADED_PLC_CODE") ){iPLC_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("SHIP_REGION") ){iZone_odsbo = c;}
            if ( xlsODSBOMatrix[0][c].equals("COUNTRY") ){iCntry_odsbo = c;}
        }
    }
    //</editor-fold>
    
    //Looks for previous lines in the exisiting Backorders DB ArrayList
    //If the line doesn't exist, it adds it to the Backorders data base ArrayList and fulfills the necessary fields
    private void createNewBackorderLines(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Loads the PPSE data sheet from the local PPSE.xls file
        /*loadPPSEfromXLS();*/
        cls_Date_Manager tmpDM = new cls_Date_Manager();
        String sDate = "NA", sZone = "NA", sCtry = "NA";
        iNEW = 0;
        //It starts reading from the 2nd line of the BO XLS File (2D Matrix)
        for ( int i=1; i<xlsODSBOMatrix.length; i++ ){
            //Checks if the line on the XLS file (2D Matrix) already exists in the Backorders DB (ArrayList)
            if ( findBODBLine(xlsODSBOMatrix[i][iSvRq_odsbo], xlsODSBOMatrix[i][iTask_odsbo], xlsODSBOMatrix[i][iItem_odsbo], xlsODSBOMatrix[i][iISO_odsbo]) == -1 ){
                //If the Line is not found, it creates a new one
                //Gets the date from the original xls file and reformats it as yyyy-mm-dd for the DB
                //sDate = tmpDM.formatDate_yyyyMMdd_HHmm(tmpDM.convertMMDDYY_toDate(xlsODSBOMatrix[i][iDate_odsbo]));
                sDate = xlsODSBOMatrix[i][iDate_odsbo];
                //Obtains the Zone and Country
                if ( xlsODSBOMatrix[i][iZone_odsbo].equals("NORTH AMERICA") ){
                    sZone = "NAMER";
                }
                else{
                    sZone = xlsODSBOMatrix[i][iZone_odsbo];
                }
                sCtry = xlsODSBOMatrix[i][iCntry_odsbo];
                //Creates the new Lines in the BO Data Base ArrayList
                this.alBoPlDB.add(new cls_BO_Data("NA",//BO Status
                        sDate,//Request Date
                        xlsODSBOMatrix[i][iSvRq_odsbo],//Service Request
                        xlsODSBOMatrix[i][iTask_odsbo],//Task number
                        xlsODSBOMatrix[i][iISO_odsbo],//ISO number
                        xlsODSBOMatrix[i][iItem_odsbo],//Part number (item)
                        xlsODSBOMatrix[i][iQty_odsbo],//Quantity
                        xlsODSBOMatrix[i][iDesc_odsbo],//Description
                        xlsODSBOMatrix[i][iTkSt_odsbo],//Task Status
                        xlsODSBOMatrix[i][iPLC_odsbo],//PLC
                        "NA",//Criticality
                        "NA",//Condition
                        "NA",//GN Search Assumption
                        "NA",//Alternatives
                        "NA",//Comments
                        "NA",//ISO1
                        "NA",//AWB1
                        "NA",//ISO2
                        "NA",//AWB2
                        "NA",//ISO3
                        "NA",//AWB3
                        "NA",//ISO MI2-BUE
                        "NA",//AWB MI2-BUE
                        "NA",//SIMI
                        "NA",//GSI Task notes
                        "BO: " + sZone + " / " + xlsODSBOMatrix[i][iCntry_odsbo] + 
                                " / SR " + xlsODSBOMatrix[i][iSvRq_odsbo] + 
                                " / TASK " + xlsODSBOMatrix[i][iTask_odsbo] + 
                                " / ITEM " + xlsODSBOMatrix[i][iItem_odsbo] + 
                                " / CUSTOMER ORDER " + xlsODSBOMatrix[i][this.iISO_odsbo] +
                                " / QTY " + xlsODSBOMatrix[i][iQty_odsbo],//Backorder Mail Title
                        "NA",//Mail tracking number 
                        "NA",//Position 
                        "TBD",//Planner 
                        "NA",//Last review date 
                        "NA",//Revised ETA 
                        "NA",//Path 
                        "NA",//Improved ETA
                        "NA",//Root cause
                        xlsODSBOMatrix[i][iZone_odsbo],//Zone
                        xlsODSBOMatrix[i][iCntry_odsbo],//Country
                        "NA"));//XXX1
                iNEW = iNEW + 1;
            }
        }
        //Cleans the PPSE 2D Matrix
        //xlsPPSEMatrix = null;
        //System.gc();
    }
    //</editor-fold>
    
    
    
    
    
    
    
    
    
    
    
    
    //Updates the Backorders search results ArrayList with the current data on the Jtable screen 
    private void updateBoPlSearchResults() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int i = 0;
        for ( cls_BO_Data tmp : alBoPlSearchResults ) {
            //Captures and updates the ArrayList lines according with the values at the screen (including the changes)
            tmp.setTkSt(jtblBOPL.getValueAt(i, 0).toString());
            tmp.setPlan(jtblBOPL.getValueAt(i, 1).toString());
            tmp.setRvDt(jtblBOPL.getValueAt(i, 2).toString());
            tmp.setDate(jtblBOPL.getValueAt(i, 3).toString());
            tmp.setSvRq(jtblBOPL.getValueAt(i, 4).toString());
            tmp.setTask(jtblBOPL.getValueAt(i, 5).toString());
            tmp.setISO(jtblBOPL.getValueAt(i, 6).toString());
            tmp.setItem(jtblBOPL.getValueAt(i, 7).toString());
            tmp.setQty(jtblBOPL.getValueAt(i, 8).toString());
            tmp.setDesc(jtblBOPL.getValueAt(i, 9).toString());
            tmp.setAlts(jtblBOPL.getValueAt(i, 10).toString());
            tmp.setReta(jtblBOPL.getValueAt(i, 11).toString());
            tmp.setPath(jtblBOPL.getValueAt(i, 12).toString());
            tmp.setIeta(jtblBOPL.getValueAt(i, 13).toString());
            tmp.setBSta(jtblBOPL.getValueAt(i, 14).toString());
            tmp.setComm(jtblBOPL.getValueAt(i, 15).toString());
            tmp.setISO1(jtblBOPL.getValueAt(i, 16).toString());
            tmp.setAwb1(jtblBOPL.getValueAt(i, 17).toString());
            tmp.setISO2(jtblBOPL.getValueAt(i, 18).toString());
            tmp.setAwb2(jtblBOPL.getValueAt(i, 19).toString());
            tmp.setISO3(jtblBOPL.getValueAt(i, 20).toString());
            tmp.setAwb3(jtblBOPL.getValueAt(i, 21).toString());
            tmp.setBOMT(jtblBOPL.getValueAt(i, 22).toString());
            tmp.setTrak(jtblBOPL.getValueAt(i, 23).toString());
            i++;
        }
    }
    //</editor-fold>
    
    //Updates the Backorders search results ArrayList with the current data on the Jtable screen 
    private void updateBoArSearchResults() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int i = 0;
        for ( cls_BO_Data tmp : alBoArSearchResults ) {
            tmp.setBSta(jtblBOAR.getValueAt(i, 0).toString());
            tmp.setDate(jtblBOAR.getValueAt(i, 1).toString());
            tmp.setSvRq(jtblBOAR.getValueAt(i, 2).toString());
            tmp.setTask(jtblBOAR.getValueAt(i, 3).toString());
            tmp.setISO(jtblBOAR.getValueAt(i, 4).toString());
            tmp.setItem(jtblBOAR.getValueAt(i, 5).toString());
            tmp.setQty(jtblBOAR.getValueAt(i, 6).toString());
            tmp.setDesc(jtblBOAR.getValueAt(i, 7).toString());
            tmp.setTkSt(jtblBOAR.getValueAt(i, 8).toString());
            tmp.setPLC(jtblBOAR.getValueAt(i, 9).toString());
            tmp.setCrit(jtblBOAR.getValueAt(i, 10).toString());
            tmp.setCond(jtblBOAR.getValueAt(i, 11).toString());
            tmp.setSrAs(jtblBOAR.getValueAt(i, 12).toString());
            tmp.setAlts(jtblBOAR.getValueAt(i, 13).toString());
            tmp.setComm(jtblBOAR.getValueAt(i, 14).toString());
            tmp.setISO1(jtblBOAR.getValueAt(i, 15).toString());
            tmp.setAwb1(jtblBOAR.getValueAt(i, 16).toString());
            tmp.setISO2(jtblBOAR.getValueAt(i, 17).toString());
            tmp.setAwb2(jtblBOAR.getValueAt(i, 18).toString());
            tmp.setISO3(jtblBOAR.getValueAt(i, 19).toString());
            tmp.setAwb3(jtblBOAR.getValueAt(i, 20).toString());
            tmp.setIsMB(jtblBOAR.getValueAt(i, 21).toString());
            tmp.setAwMB(jtblBOAR.getValueAt(i, 22).toString());
            tmp.setSIMI(jtblBOAR.getValueAt(i, 23).toString());
            tmp.setTkNt(jtblBOAR.getValueAt(i, 24).toString());
            tmp.setBOMT(jtblBOAR.getValueAt(i, 25).toString());
            tmp.setTrak(jtblBOAR.getValueAt(i, 26).toString());
            i++;
        }
    }
    //</editor-fold>
    
    
    
    //Updates a temporary Backorders ArrayList with all the info on the corresponding screen Jtable
    private ArrayList<cls_BO_Data> loadTMPscreenBackordersDB() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_BO_Data> tmpBOs = new ArrayList<>();
        String sBSta="", sDate="", sSvRq="", sTask="", sISO="",
            sItem="", sQty="", sDesc="", sTkSt="", sPLC="", sCrit="",
            sCond="", sSrAs="", sAlts="", sComm="", sISO1="", sAwb1="", 
            sISO2="", sAwb2="", sISO3="", sAwb3="", sIsMB="", sAwMB="", 
            sSIMI="", sTkNt="", sBOMT="", sTrak=""; 
        for ( int i=0; i < this.jtblBOPL.getRowCount(); i++ )
        {
            sBSta = jtblBOPL.getValueAt(i, 0).toString();
            sDate = jtblBOPL.getValueAt(i, 1).toString();
            sSvRq = jtblBOPL.getValueAt(i, 2).toString();
            sTask = jtblBOPL.getValueAt(i, 3).toString();
            sISO = jtblBOPL.getValueAt(i, 4).toString();
            sItem = jtblBOPL.getValueAt(i, 5).toString();
            sQty = jtblBOPL.getValueAt(i, 6).toString();
            sDesc = jtblBOPL.getValueAt(i, 7).toString();
            sTkSt = jtblBOPL.getValueAt(i, 8).toString();
            sPLC = jtblBOPL.getValueAt(i, 9).toString();
            sCrit = jtblBOPL.getValueAt(i, 10).toString();
            sCond = jtblBOPL.getValueAt(i, 11).toString();
            sSrAs = jtblBOPL.getValueAt(i, 12).toString();
            sAlts = jtblBOPL.getValueAt(i, 13).toString();
            sComm = jtblBOPL.getValueAt(i, 14).toString();
            sISO1 = jtblBOPL.getValueAt(i, 15).toString();
            sAwb1 = jtblBOPL.getValueAt(i, 16).toString();
            sISO2 = jtblBOPL.getValueAt(i, 17).toString();
            sAwb2 = jtblBOPL.getValueAt(i, 18).toString();
            sISO3 = jtblBOPL.getValueAt(i, 19).toString();
            sAwb3 = jtblBOPL.getValueAt(i, 20).toString();
            sIsMB = jtblBOPL.getValueAt(i, 21).toString();
            sAwMB = jtblBOPL.getValueAt(i, 22).toString(); 
            sSIMI = jtblBOPL.getValueAt(i, 23).toString();
            sTkNt = jtblBOPL.getValueAt(i, 24).toString();
            sBOMT = jtblBOPL.getValueAt(i, 25).toString();
            sTrak = jtblBOPL.getValueAt(i, 26).toString();
            tmpBOs.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                    sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                    sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"));
        }
        return tmpBOs;
    }
    //</editor-fold>
    
    
    //SEARCHING
    
    //Searches for a given text into the Backorders Planning DataBase ArrayList
    //Creates an ArrayList with the results and saves the original positions in the main Backorders Planning DB ArrayList
    //Shows the results in the DB table screen
    public void searchTextBoPlDB(String sText) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        alBoPlSearchResults.clear();
        for ( int i = 0; i < this.alBoPlDB.size(); i++ ) {
            //Looks for the text chain into the different columns (except comments, email tittle and task notes columns)
            if ( (alBoPlDB.get(i).getTkSt().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getPlan().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getRvDt().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getDate().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getSvRq().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getTask().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getISO().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getItem().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getQty().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getDesc().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getAlts().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getReta().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getPath().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getIeta().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getBSta().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getISO1().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getAwb1().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getISO2().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getAwb2().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getISO3().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getAwb3().toUpperCase().indexOf(sText) != -1) ||
                    (alBoPlDB.get(i).getTrak().toUpperCase().indexOf(sText) != -1)) {
                alBoPlSearchResults.add(new cls_BO_Data(alBoPlDB.get(i).getBSta(),
                        alBoPlDB.get(i).getDate(), 
                        alBoPlDB.get(i).getSvRq(),
                        alBoPlDB.get(i).getTask(),
                        alBoPlDB.get(i).getISO(),
                        alBoPlDB.get(i).getItem(),
                        alBoPlDB.get(i).getQty(),
                        alBoPlDB.get(i).getDesc(),
                        alBoPlDB.get(i).getTkSt(),
                        "NA",
                        "NA",
                        "NA",
                        "NA",
                        alBoPlDB.get(i).getAlts(),
                        alBoPlDB.get(i).getComm(),
                        alBoPlDB.get(i).getISO1(),
                        alBoPlDB.get(i).getAwb1(),
                        alBoPlDB.get(i).getISO2(),
                        alBoPlDB.get(i).getAwb2(),
                        alBoPlDB.get(i).getISO3(),
                        alBoPlDB.get(i).getAwb3(),
                        "NA",
                        "NA",
                        "NA",
                        "NA",
                        alBoPlDB.get(i).getBOMT(),
                        alBoPlDB.get(i).getTrak(),
                        String.valueOf(i), //Identifies the position were the value was found
                        alBoPlDB.get(i).getPlan(), 
                        alBoPlDB.get(i).getRvDt(), 
                        alBoPlDB.get(i).getReta(), 
                        alBoPlDB.get(i).getPath(), 
                        alBoPlDB.get(i).getIeta(), 
                        "NA", 
                        "NA",
                        "NA",
                        "NA"));
            }
        }
        //Checks if the process detected results or not
        if ( alBoPlSearchResults.isEmpty() ) {
            //JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
        }
        else {
            //JOptionPane.showMessageDialog(this, alBOSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            //Shows the results in the screen
            this.cleanBoPlTable();
            for (cls_BO_Data tmp : alBoPlSearchResults) {
                try {
                    BOPLColumn[0] = tmp.getTkSt();
                    BOPLColumn[1] = tmp.getPlan();
                    BOPLColumn[2] = tmp.getRvDt();
                    BOPLColumn[3] = tmp.getDate();
                    BOPLColumn[4] = tmp.getSvRq();
                    BOPLColumn[5] = tmp.getTask();
                    BOPLColumn[6] = tmp.getISO();
                    BOPLColumn[7] = tmp.getItem();
                    BOPLColumn[8] = tmp.getQty();
                    BOPLColumn[9] = tmp.getDesc();
                    BOPLColumn[10] = tmp.getAlts();
                    BOPLColumn[11] = tmp.getReta();
                    BOPLColumn[12] = tmp.getPath();
                    BOPLColumn[13] = tmp.getIeta();
                    BOPLColumn[14] = tmp.getBSta();
                    BOPLColumn[15] = tmp.getComm();
                    BOPLColumn[16] = tmp.getISO1();
                    BOPLColumn[17] = tmp.getAwb1();
                    BOPLColumn[18] = tmp.getISO2();
                    BOPLColumn[19] = tmp.getAwb2();
                    BOPLColumn[20] = tmp.getISO3();
                    BOPLColumn[21] = tmp.getAwb3();
                    BOPLColumn[22] = tmp.getBOMT();
                    BOPLColumn[23] = tmp.getTrak();
                    tblModelBOPL.addRow(BOPLColumn);
                    jtblBOPL.setModel(this.tblModelBOPL);
                }
                catch(Exception e){JOptionPane.showMessageDialog(this, "There was an error while loading the Backorders Planning search results\n"
                        + e, "ERROR - searchTextBoPlDB()", JOptionPane.ERROR_MESSAGE);}  
            }
            //Warns the System that the DB table screen is now showing the WebADI search results list
            this.bBOPLFLAG = false;
            this.jlblBOPLFlag.setText("<html>Now showing: <font color='orange'>Search results</font></html>");
        }
    }
    //</editor-fold>
    
    //Searches for a given text into the Argentina Backorders DataBase ArrayList
    //Creates an ArrayList with the results and saves the original positions in the main Argentina Backorders DB ArrayList
    //Shows the results in the DB table screen
    public void searchTextBoArDB(String sText) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        alBoArSearchResults.clear();
        for ( int i = 0; i < this.alBoArDB.size(); i++ ) {
            //Looks for the text chain into the different columns (except comments, email tittle and task notes columns)
            if ( (alBoArDB.get(i).getBSta().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getDate().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getSvRq().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getTask().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getISO().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getItem().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getQty().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getDesc().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getTkSt().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getPLC().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getCrit().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getCond().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getSrAs().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getAlts().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getISO1().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getAwb1().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getISO2().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getAwb2().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getISO3().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getAwb3().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getIsMB().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getAwMB().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getSIMI().toUpperCase().indexOf(sText) != -1) ||
                    (alBoArDB.get(i).getTrak().toUpperCase().indexOf(sText) != -1)) {
                alBoArSearchResults.add(new cls_BO_Data(alBoArDB.get(i).getBSta(),
                        alBoArDB.get(i).getDate(), 
                        alBoArDB.get(i).getSvRq(),
                        alBoArDB.get(i).getTask(),
                        alBoArDB.get(i).getISO(),
                        alBoArDB.get(i).getItem(),
                        alBoArDB.get(i).getQty(),
                        alBoArDB.get(i).getDesc(),
                        alBoArDB.get(i).getTkSt(),
                        alBoArDB.get(i).getPLC(),
                        alBoArDB.get(i).getCrit(),
                        alBoArDB.get(i).getCond(),
                        alBoArDB.get(i).getSrAs(),
                        alBoArDB.get(i).getAlts(),
                        alBoArDB.get(i).getComm(),
                        alBoArDB.get(i).getISO1(),
                        alBoArDB.get(i).getAwb1(),
                        alBoArDB.get(i).getISO2(),
                        alBoArDB.get(i).getAwb2(),
                        alBoArDB.get(i).getISO3(),
                        alBoArDB.get(i).getAwb3(),
                        alBoArDB.get(i).getIsMB(),
                        alBoArDB.get(i).getAwMB(),
                        alBoArDB.get(i).getSIMI(),
                        alBoArDB.get(i).getTkNt(),
                        alBoArDB.get(i).getBOMT(),
                        alBoArDB.get(i).getTrak(),
                        String.valueOf(i), //Identifies the position were the value was found
                        "NA", 
                        "NA", 
                        "NA", 
                        "NA", 
                        "NA", 
                        "NA", 
                        "NA", 
                        "NA",
                        "NA"));
            }
        }
        //Checks if the process detected results or not
        if ( alBoArSearchResults.isEmpty() ) {
            //JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
        }
        else {
            //JOptionPane.showMessageDialog(this, alBOSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            //Shows the results in the screen
            this.cleanBoArTable();
            for (cls_BO_Data tmp : alBoArSearchResults) {
                try {
                    BOARColumn[0] = tmp.getBSta();
                    BOARColumn[1] = tmp.getDate();
                    BOARColumn[2] = tmp.getSvRq();
                    BOARColumn[3] = tmp.getTask();
                    BOARColumn[4] = tmp.getISO();
                    BOARColumn[5] = tmp.getItem();
                    BOARColumn[6] = tmp.getQty();
                    BOARColumn[7] = tmp.getDesc();
                    BOARColumn[8] = tmp.getTkSt();
                    BOARColumn[9] = tmp.getPLC();
                    BOARColumn[10] = tmp.getCrit();
                    BOARColumn[11] = tmp.getCond();
                    BOARColumn[12] = tmp.getSrAs();
                    BOARColumn[13] = tmp.getAlts();
                    BOARColumn[14] = tmp.getComm();
                    BOARColumn[15] = tmp.getISO1();
                    BOARColumn[16] = tmp.getAwb1();
                    BOARColumn[17] = tmp.getISO2();
                    BOARColumn[18] = tmp.getAwb2();
                    BOARColumn[19] = tmp.getISO3();
                    BOARColumn[20] = tmp.getAwb3();
                    BOARColumn[21] = tmp.getIsMB();
                    BOARColumn[22] = tmp.getAwMB();
                    BOARColumn[23] = tmp.getSIMI();
                    BOARColumn[24] = tmp.getTkNt();
                    BOARColumn[25] = tmp.getBOMT();
                    BOARColumn[26] = tmp.getTrak();
                    tblModelBOAR.addRow(BOARColumn);
                    jtblBOAR.setModel(this.tblModelBOAR);
                }
                catch(Exception e){JOptionPane.showMessageDialog(this, "There was an error while loading the Argentina Backorders search results\n"
                        + e, "ERROR - searchTextBoPlDB()", JOptionPane.ERROR_MESSAGE);}  
            }
            //Warns the System that the DB table screen is now showing the WebADI search results list
            this.bBOARFLAG = false;
            this.jlblBOARFlag.setText("<html>Now showing: <font color='orange'>Search results</font></html>");
        }
    }
    //</editor-fold>
    
    //Looks for an object into the exisiting Backorders Planning ArrayList DB and return its position
    private int findBoPlDBPos(cls_BO_Data tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i=0; i<this.alBoPlDB.size(); i++ ){
            if ( tmpCons.getBSta().equals(this.alBoPlDB.get(i).getBSta()) && 
                    tmpCons.getPlan().equals(this.alBoPlDB.get(i).getPlan()) &&
                    tmpCons.getRvDt().equals(this.alBoPlDB.get(i).getRvDt()) &&
                    tmpCons.getDate().equals(this.alBoPlDB.get(i).getDate()) &&
                    tmpCons.getSvRq().equals(this.alBoPlDB.get(i).getSvRq()) &&
                    tmpCons.getTask().equals(this.alBoPlDB.get(i).getTask()) &&
                    tmpCons.getISO().equals(this.alBoPlDB.get(i).getISO()) &&
                    tmpCons.getItem().equals(this.alBoPlDB.get(i).getItem()) &&
                    tmpCons.getQty().equals(this.alBoPlDB.get(i).getQty()) &&
                    tmpCons.getDesc().equals(this.alBoPlDB.get(i).getDesc()) &&
                    tmpCons.getAlts().equals(this.alBoPlDB.get(i).getAlts()) &&
                    tmpCons.getReta().equals(this.alBoPlDB.get(i).getReta()) &&
                    tmpCons.getPath().equals(this.alBoPlDB.get(i).getPath()) &&
                    tmpCons.getIeta().equals(this.alBoPlDB.get(i).getIeta()) &&
                    tmpCons.getBSta().equals(this.alBoPlDB.get(i).getBSta()) &&
                    tmpCons.getComm().equals(this.alBoPlDB.get(i).getComm()) &&
                    tmpCons.getISO1().equals(this.alBoPlDB.get(i).getISO1()) &&
                    tmpCons.getAwb1().equals(this.alBoPlDB.get(i).getAwb1()) &&
                    tmpCons.getISO2().equals(this.alBoPlDB.get(i).getISO2()) &&
                    tmpCons.getAwb2().equals(this.alBoPlDB.get(i).getAwb2()) &&
                    tmpCons.getISO3().equals(this.alBoPlDB.get(i).getISO3()) &&
                    tmpCons.getAwb3().equals(this.alBoPlDB.get(i).getAwb3()) &&
                    tmpCons.getBOMT().equals(this.alBoPlDB.get(i).getBOMT()) &&
                    tmpCons.getTrak().equals(this.alBoPlDB.get(i).getTrak()) ){
                iPos = i;
            }
        }
        System.out.println("Object found at pos: " + iPos);
        return iPos;
    }
    //</editor-fold>
    
    //Looks for an object into the exisiting Argentina Backorders ArrayList DB and return its position
    private int findBoArDBPos(cls_BO_Data tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i=0; i<this.alBoArDB.size(); i++ ){
            if ( tmpCons.getBSta().equals(this.alBoArDB.get(i).getBSta()) && 
                    tmpCons.getDate().equals(this.alBoArDB.get(i).getDate()) &&
                    tmpCons.getSvRq().equals(this.alBoArDB.get(i).getSvRq()) &&
                    tmpCons.getTask().equals(this.alBoArDB.get(i).getTask()) &&
                    tmpCons.getISO().equals(this.alBoArDB.get(i).getISO()) &&
                    tmpCons.getItem().equals(this.alBoArDB.get(i).getItem()) &&
                    tmpCons.getQty().equals(this.alBoArDB.get(i).getQty()) &&
                    tmpCons.getDesc().equals(this.alBoArDB.get(i).getDesc()) &&
                    tmpCons.getTkSt().equals(this.alBoArDB.get(i).getTkSt()) &&
                    tmpCons.getPLC().equals(this.alBoArDB.get(i).getPLC()) &&
                    tmpCons.getCrit().equals(this.alBoArDB.get(i).getCrit()) &&
                    tmpCons.getCond().equals(this.alBoArDB.get(i).getCond()) &&
                    tmpCons.getSrAs().equals(this.alBoArDB.get(i).getSrAs()) &&
                    tmpCons.getAlts().equals(this.alBoArDB.get(i).getAlts()) &&
                    tmpCons.getComm().equals(this.alBoArDB.get(i).getComm()) &&
                    tmpCons.getISO1().equals(this.alBoArDB.get(i).getISO1()) &&
                    tmpCons.getAwb1().equals(this.alBoArDB.get(i).getAwb1()) &&
                    tmpCons.getISO2().equals(this.alBoArDB.get(i).getISO2()) &&
                    tmpCons.getAwb2().equals(this.alBoArDB.get(i).getAwb2()) &&
                    tmpCons.getISO3().equals(this.alBoArDB.get(i).getISO3()) &&
                    tmpCons.getAwb3().equals(this.alBoArDB.get(i).getAwb3()) &&
                    tmpCons.getIsMB().equals(this.alBoArDB.get(i).getIsMB()) &&
                    tmpCons.getAwMB().equals(this.alBoArDB.get(i).getAwMB()) &&
                    tmpCons.getSIMI().equals(this.alBoArDB.get(i).getSIMI()) &&
                    tmpCons.getTkNt().equals(this.alBoArDB.get(i).getTkNt()) &&
                    tmpCons.getBOMT().equals(this.alBoArDB.get(i).getBOMT()) &&
                    tmpCons.getTrak().equals(this.alBoArDB.get(i).getTrak()) ){
                iPos = i;
            }
        }
        return iPos;
    }
    //</editor-fold>
    
    
    
    
    
    //Recognizes if the loaded ODS Backorders xls file has the correct format to work
    private boolean validateODSBOXLSFile(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bFlag = true;
        if ( iDate_odsbo == -1 || 
                iSvRq_odsbo == -1 || 
                iTask_odsbo == -1 || 
                iISO_odsbo == -1 || 
                iItem_odsbo == -1 || 
                iQty_odsbo == -1 ||
                iDesc_odsbo == -1 ||
                iTkSt_odsbo == -1 ||
                iPLC_odsbo == -1 ||
                iZone_odsbo == -1 ||
                iCntry_odsbo == -1){
            bFlag = false;
        }
        if ( bFlag == false ) {
            System.out.println("EXCEL ODS BACKORDERS PLANNING FILE VALITATION FAILED: One or more necessary columns were not found");
        }
        else {
            System.out.println("EXCEL ODS BACKORDERS PLANNING FILE VALITATION PASSED");
        }
        return bFlag;        
    }
    //</editor-fold>
    
    
    
    
    
    //ADDING NEW DATA INTO THE BACKORDERS DATA BASE
    
    //Searches for ODS Backorders lines into the existing data base
    //Returns the Arraylist position where the line is
    private int findBODBLine(String sSvRq, String sTask, String sItem, String sOrNu){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i = 0; i < alBoPlDB.size(); i++ ){
            if ( alBoPlDB.get(i).getSvRq().equals(sSvRq) &&
                    alBoPlDB.get(i).getTask().equals(sTask) &&
                    alBoPlDB.get(i).getItem().equals(sItem) &&
                    alBoPlDB.get(i).getISO().equals(sOrNu) ) {
                iPos = i;
                break;
            }
        }
        return iPos;
    }
   //</editor-fold>
    
    //Searches for Backorders data base line into the downloaded ODS XLS BO file
    //Returns the 2D Matrix position where the line is
    private int findBOXLLine(String sSvRq, String sTask, String sItem, String sOrNu){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        int iPos = -1;
        for ( int i=1; i<xlsODSBOMatrix.length; i++ ){
            if ( xlsODSBOMatrix[i][iSvRq_odsbo].equals(sSvRq) &&
                    xlsODSBOMatrix[i][iTask_odsbo].equals(sTask) &&
                    xlsODSBOMatrix[i][iItem_odsbo].equals(sItem) &&
                    xlsODSBOMatrix[i][iISO_odsbo].equals(sOrNu)){
                iPos = i;
            }
        }
        return iPos;
    }
    //</editor-fold>
    
       
    //Cheks the lines on the Backorders DB ArrayList that are no longer reported in the ODS file
    //If the line is no longer reported is changes its status to "Ready to archive"
    private void checkClosedLines(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        iCHK = 0;
        for ( int r=0; r<alBoPlDB.size(); r++ ){
            if ( findBOXLLine(alBoPlDB.get(r).getSvRq(), alBoPlDB.get(r).getTask(), alBoPlDB.get(r).getItem(), alBoPlDB.get(r).getISO()) == -1 ){
                alBoPlDB.get(r).setTkSt("Ready to archive");
                iCHK = iCHK + 1;
            }
        }
    }
    //</editor-fold>
    
    
    
    //Captures the data in the highlighted line on the Backorders screen and returns an Object of data type
    private cls_BO_Data captureBoPLLine(int iRow) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        cls_BO_Data ptrTMP = new cls_BO_Data();
        ptrTMP.setTkSt(this.jtblBOPL.getValueAt(iRow, 0).toString());
        ptrTMP.setPlan(this.jtblBOPL.getValueAt(iRow, 1).toString());
        ptrTMP.setRvDt(this.jtblBOPL.getValueAt(iRow, 2).toString());
        ptrTMP.setDate(this.jtblBOPL.getValueAt(iRow, 3).toString());
        ptrTMP.setSvRq(this.jtblBOPL.getValueAt(iRow, 4).toString());
        ptrTMP.setTask(this.jtblBOPL.getValueAt(iRow, 5).toString());
        ptrTMP.setISO(this.jtblBOPL.getValueAt(iRow, 6).toString());
        ptrTMP.setItem(this.jtblBOPL.getValueAt(iRow, 7).toString());
        ptrTMP.setQty(this.jtblBOPL.getValueAt(iRow, 8).toString());
        ptrTMP.setDesc(this.jtblBOPL.getValueAt(iRow, 9).toString());
        ptrTMP.setAlts(this.jtblBOPL.getValueAt(iRow, 10).toString());
        ptrTMP.setReta(this.jtblBOPL.getValueAt(iRow, 11).toString());
        ptrTMP.setPath(this.jtblBOPL.getValueAt(iRow, 12).toString());
        ptrTMP.setIeta(this.jtblBOPL.getValueAt(iRow, 13).toString());
        ptrTMP.setBSta(this.jtblBOPL.getValueAt(iRow, 14).toString());
        ptrTMP.setComm(this.jtblBOPL.getValueAt(iRow, 15).toString());
        ptrTMP.setISO1(this.jtblBOPL.getValueAt(iRow, 16).toString());
        ptrTMP.setAwb1(this.jtblBOPL.getValueAt(iRow, 17).toString());
        ptrTMP.setISO2(this.jtblBOPL.getValueAt(iRow, 18).toString());
        ptrTMP.setAwb2(this.jtblBOPL.getValueAt(iRow, 19).toString());
        ptrTMP.setISO3(this.jtblBOPL.getValueAt(iRow, 20).toString());
        ptrTMP.setAwb3(this.jtblBOPL.getValueAt(iRow, 21).toString());
        ptrTMP.setBOMT(this.jtblBOPL.getValueAt(iRow, 22).toString());
        ptrTMP.setTrak(this.jtblBOPL.getValueAt(iRow, 23).toString());
        ptrTMP.setPLC("NA");
        ptrTMP.setCrit("NA");
        ptrTMP.setCond("NA");
        ptrTMP.setSrAs("NA");
        ptrTMP.setIsMB("NA");
        ptrTMP.setAwMB("NA");
        ptrTMP.setSIMI("NA");
        ptrTMP.setTkNt("NA");
        ptrTMP.setPosi("NA");
        ptrTMP.setRoot("NA");
        ptrTMP.setZone("NA");
        ptrTMP.setCtry("NA");
        ptrTMP.setXXX1("NA");
        return ptrTMP;
    }
    //</editor-fold>
    
    //Gets the complete list of mail tracking numbers for any specific line on the Backorders Data Base
    private void getMailTrackings(){
    //<editor-fold defaultstate="collpased" desc="Method Source Code">    
        String sTrackings = "";
        int iRow = jtblBOPL.getSelectedRow();
        sTrackings = jtblBOPL.getValueAt(iRow, 26).toString().replaceAll(">","\n");
        System.out.println(sTrackings);
        gui_InfoNotes tmpIN = new gui_InfoNotes("LIST OF RELATED MAIL TRACKINGS: \n\n" + sTrackings);
        tmpIN.setLocationRelativeTo(this);
        tmpIN.setTitle("MAIL TRACKING NUMBERS");
        tmpIN.setVisible(true);
    }
    //</editor-fold>
    
    
    
    
    
    //GENERAL METHODS
    
    //Opens an Internet URL
    public void openLink(String sLink) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    
        try
        {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(sLink));

        }
        catch(Exception e){JOptionPane.showMessageDialog(this, e.getMessage());}
    }
    //</editor-fold>
    
    private void sendMail(String smailTo, String smailCC, String smailSub, String smailBody, String sCountry, String sOrg) throws IOException, URISyntaxException {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        cls_Mail_Manager tmpMail = new cls_Mail_Manager(smailTo, "NA", smailSub, smailBody, "Parts", sOrg);
        tmpMail.sendMail();
        
    }
    //</editor-fold>
    
    
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlTop = new javax.swing.JPanel();
        jlblTop = new javax.swing.JLabel();
        jpnlMid = new javax.swing.JPanel();
        jtbpnMain = new javax.swing.JTabbedPane();
        jpnlBODB = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblBOPL = new javax.swing.JTable();
        jpnlMidBoPl = new javax.swing.JPanel();
        jlblBOPLFlag = new javax.swing.JLabel();
        jlblImp = new javax.swing.JLabel();
        jbtnBoPlImp = new javax.swing.JButton();
        jlblExp = new javax.swing.JLabel();
        jbtnBoPlExp = new javax.swing.JButton();
        jlblRefresh = new javax.swing.JLabel();
        jbtnBoPlRefresh = new javax.swing.JButton();
        jlblSearch = new javax.swing.JLabel();
        jbtnBoPlSearch = new javax.swing.JButton();
        jtxtBoPlSearch = new javax.swing.JTextField();
        jlblSave = new javax.swing.JLabel();
        jbtnBoPlSave = new javax.swing.JButton();
        jlblRemove = new javax.swing.JLabel();
        jbtnBODel = new javax.swing.JButton();
        jlblAdd = new javax.swing.JLabel();
        jbtnBOAdd = new javax.swing.JButton();
        jlblMail = new javax.swing.JLabel();
        jbtnBoPlMail = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jlblBOPLDBsize = new javax.swing.JLabel();
        jpnlARDB = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblBOAR = new javax.swing.JTable();
        jlblSearch1 = new javax.swing.JLabel();
        jpnlMidBoAr = new javax.swing.JPanel();
        jlblBOARFlag = new javax.swing.JLabel();
        jlblImpAr = new javax.swing.JLabel();
        jbtnBoArImp = new javax.swing.JButton();
        jlblExpAr = new javax.swing.JLabel();
        jbtnBoArExp = new javax.swing.JButton();
        jlblRefreshAr = new javax.swing.JLabel();
        jbtnBoArRefresh = new javax.swing.JButton();
        jlblSearchAr = new javax.swing.JLabel();
        jbtnBoArSearch = new javax.swing.JButton();
        jtxtBoArSearch = new javax.swing.JTextField();
        jSeparator13 = new javax.swing.JSeparator();
        jlblBOARDBsize = new javax.swing.JLabel();
        jpnlTools = new javax.swing.JPanel();
        jtbarMain = new javax.swing.JToolBar();
        jbtnPlnDsk = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jbtnWebADI = new javax.swing.JButton();
        jbtn2ndHop = new javax.swing.JButton();
        jbtnPurFS = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jbtnEndeca = new javax.swing.JButton();
        jbtnODS = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jbtnTracks = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JToolBar.Separator();
        jlblSta = new javax.swing.JLabel();
        jlblUser = new javax.swing.JLabel();
        jpnlBot = new javax.swing.JPanel();
        jbtnExit = new javax.swing.JButton();
        jbtnLogout = new javax.swing.JButton();
        jlblModVer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Backorders Working Module");

        jpnlTop.setBackground(new java.awt.Color(242, 254, 242));
        jpnlTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlblTop.setFont(new java.awt.Font("Bauhaus 93", 0, 24)); // NOI18N
        jlblTop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblTop.setText("BACKORDERS CONTROL");

        javax.swing.GroupLayout jpnlTopLayout = new javax.swing.GroupLayout(jpnlTop);
        jpnlTop.setLayout(jpnlTopLayout);
        jpnlTopLayout.setHorizontalGroup(
            jpnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlTopLayout.setVerticalGroup(
            jpnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblTop)
        );

        jpnlMid.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jtblBOPL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblBOPL);

        jpnlMidBoPl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlblBOPLFlag.setText("Current data base");

        jlblImp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblImp.setText("Import");

        jbtnBoPlImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/import_medium.png"))); // NOI18N
        jbtnBoPlImp.setToolTipText("Import ODS Backorders Data");
        jbtnBoPlImp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoPlImpActionPerformed(evt);
            }
        });

        jlblExp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblExp.setText("Export");

        jbtnBoPlExp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/csvexport2_medium.png"))); // NOI18N
        jbtnBoPlExp.setToolTipText("Export screen data to .csv file");
        jbtnBoPlExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoPlExpActionPerformed(evt);
            }
        });

        jlblRefresh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblRefresh.setText("Refresh");

        jbtnBoPlRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh_2_medium.png"))); // NOI18N
        jbtnBoPlRefresh.setToolTipText("Refresh screen");
        jbtnBoPlRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoPlRefreshActionPerformed(evt);
            }
        });

        jlblSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblSearch.setText("Search");

        jbtnBoPlSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_find_medium.png"))); // NOI18N
        jbtnBoPlSearch.setToolTipText("Search");
        jbtnBoPlSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoPlSearchActionPerformed(evt);
            }
        });

        jtxtBoPlSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtBoPlSearchKeyPressed(evt);
            }
        });

        jlblSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblSave.setText("Save");

        jbtnBoPlSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/save_medium.png"))); // NOI18N
        jbtnBoPlSave.setToolTipText("Save");
        jbtnBoPlSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoPlSaveActionPerformed(evt);
            }
        });

        jlblRemove.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblRemove.setText("Remove");

        jbtnBODel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove_medium.png"))); // NOI18N
        jbtnBODel.setToolTipText("Delete selected line");
        jbtnBODel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBODelActionPerformed(evt);
            }
        });

        jlblAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblAdd.setText("Add");

        jbtnBOAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_medium.png"))); // NOI18N
        jbtnBOAdd.setToolTipText("Add line");
        jbtnBOAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBOAddActionPerformed(evt);
            }
        });

        jlblMail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblMail.setText("Mail");

        jbtnBoPlMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sendmail_medium.png"))); // NOI18N
        jbtnBoPlMail.setToolTipText("Prepare backorder e-mail");
        jbtnBoPlMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoPlMailActionPerformed(evt);
            }
        });

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator11.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jlblBOPLDBsize.setText("Data Base size");

        javax.swing.GroupLayout jpnlMidBoPlLayout = new javax.swing.GroupLayout(jpnlMidBoPl);
        jpnlMidBoPl.setLayout(jpnlMidBoPlLayout);
        jpnlMidBoPlLayout.setHorizontalGroup(
            jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidBoPlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jlblImp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnBoPlImp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnBoPlExp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblExp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jlblBOPLDBsize, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnBoPlMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnBOAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnBODel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnBoPlSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlMidBoPlLayout.createSequentialGroup()
                        .addComponent(jtxtBoPlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnBoPlSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnBoPlRefresh))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoPlLayout.createSequentialGroup()
                        .addComponent(jlblBOPLFlag, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlblRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpnlMidBoPlLayout.setVerticalGroup(
            jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidBoPlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoPlLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoPlLayout.createSequentialGroup()
                                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jbtnBoPlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbtnBoPlRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoPlLayout.createSequentialGroup()
                                .addComponent(jtxtBoPlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoPlLayout.createSequentialGroup()
                                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jbtnBODel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbtnBoPlSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbtnBOAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbtnBoPlMail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoPlLayout.createSequentialGroup()
                                .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlblBOPLDBsize)
                                    .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jbtnBoPlImp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jbtnBoPlExp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(11, 11, 11)))
                        .addGroup(jpnlMidBoPlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblImp)
                            .addComponent(jlblExp)
                            .addComponent(jlblRefresh)
                            .addComponent(jlblSearch)
                            .addComponent(jlblBOPLFlag)
                            .addComponent(jlblSave)
                            .addComponent(jlblRemove)
                            .addComponent(jlblAdd)
                            .addComponent(jlblMail)))
                    .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnlBODBLayout = new javax.swing.GroupLayout(jpnlBODB);
        jpnlBODB.setLayout(jpnlBODBLayout);
        jpnlBODBLayout.setHorizontalGroup(
            jpnlBODBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlBODBLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlBODBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jpnlMidBoPl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnlBODBLayout.setVerticalGroup(
            jpnlBODBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlBODBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnlMidBoPl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jtbpnMain.addTab("Backorders Data Base", jpnlBODB);

        jtblBOAR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jtblBOAR);

        jlblSearch1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblSearch1.setText("Search");

        jpnlMidBoAr.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlblBOARFlag.setText("Current data base");

        jlblImpAr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblImpAr.setText("Import");

        jbtnBoArImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/import_medium.png"))); // NOI18N
        jbtnBoArImp.setToolTipText("Import ODS Backorders Data");
        jbtnBoArImp.setEnabled(false);
        jbtnBoArImp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoArImpActionPerformed(evt);
            }
        });

        jlblExpAr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblExpAr.setText("Export");

        jbtnBoArExp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/csvexport2_medium.png"))); // NOI18N
        jbtnBoArExp.setToolTipText("Export screen data to .csv file");
        jbtnBoArExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoArExpActionPerformed(evt);
            }
        });

        jlblRefreshAr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblRefreshAr.setText("Refresh");

        jbtnBoArRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh_2_medium.png"))); // NOI18N
        jbtnBoArRefresh.setToolTipText("Refresh screen");
        jbtnBoArRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoArRefreshActionPerformed(evt);
            }
        });

        jlblSearchAr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblSearchAr.setText("Search");

        jbtnBoArSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_find_medium.png"))); // NOI18N
        jbtnBoArSearch.setToolTipText("Search");
        jbtnBoArSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBoArSearchActionPerformed(evt);
            }
        });

        jtxtBoArSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtBoArSearchKeyPressed(evt);
            }
        });

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jlblBOARDBsize.setText("Data Base size");

        javax.swing.GroupLayout jpnlMidBoArLayout = new javax.swing.GroupLayout(jpnlMidBoAr);
        jpnlMidBoAr.setLayout(jpnlMidBoArLayout);
        jpnlMidBoArLayout.setHorizontalGroup(
            jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidBoArLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jlblImpAr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnBoArImp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnBoArExp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblExpAr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jlblBOARDBsize, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlMidBoArLayout.createSequentialGroup()
                        .addComponent(jtxtBoArSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnBoArSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnBoArRefresh))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoArLayout.createSequentialGroup()
                        .addComponent(jlblBOARFlag, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlblSearchAr, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlblRefreshAr, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpnlMidBoArLayout.setVerticalGroup(
            jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidBoArLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoArLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoArLayout.createSequentialGroup()
                                .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jbtnBoArSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbtnBoArRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoArLayout.createSequentialGroup()
                                .addComponent(jtxtBoArSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidBoArLayout.createSequentialGroup()
                                .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlblBOARDBsize)
                                    .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jbtnBoArImp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jbtnBoArExp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(11, 11, 11)))
                        .addGroup(jpnlMidBoArLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblImpAr)
                            .addComponent(jlblExpAr)
                            .addComponent(jlblRefreshAr)
                            .addComponent(jlblSearchAr)
                            .addComponent(jlblBOARFlag))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnlARDBLayout = new javax.swing.GroupLayout(jpnlARDB);
        jpnlARDB.setLayout(jpnlARDBLayout);
        jpnlARDBLayout.setHorizontalGroup(
            jpnlARDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlARDBLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlARDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1073, Short.MAX_VALUE)
                    .addComponent(jpnlMidBoAr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jpnlARDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpnlARDBLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jlblSearch1)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jpnlARDBLayout.setVerticalGroup(
            jpnlARDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlARDBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addComponent(jpnlMidBoAr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jpnlARDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpnlARDBLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jlblSearch1)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jtbpnMain.addTab("Argentina Planning DB", jpnlARDB);

        javax.swing.GroupLayout jpnlToolsLayout = new javax.swing.GroupLayout(jpnlTools);
        jpnlTools.setLayout(jpnlToolsLayout);
        jpnlToolsLayout.setHorizontalGroup(
            jpnlToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1093, Short.MAX_VALUE)
        );
        jpnlToolsLayout.setVerticalGroup(
            jpnlToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 421, Short.MAX_VALUE)
        );

        jtbpnMain.addTab("Tools and Utilities", jpnlTools);

        javax.swing.GroupLayout jpnlMidLayout = new javax.swing.GroupLayout(jpnlMid);
        jpnlMid.setLayout(jpnlMidLayout);
        jpnlMidLayout.setHorizontalGroup(
            jpnlMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtbpnMain)
                .addContainerGap())
        );
        jpnlMidLayout.setVerticalGroup(
            jpnlMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtbpnMain)
                .addContainerGap())
        );

        jtbarMain.setFloatable(false);
        jtbarMain.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jtbarMain.setRollover(true);

        jbtnPlnDsk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Desktop_MED.png"))); // NOI18N
        jbtnPlnDsk.setToolTipText("Planner's Desktop");
        jbtnPlnDsk.setFocusable(false);
        jbtnPlnDsk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnPlnDsk.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnPlnDsk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPlnDskActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtnPlnDsk);
        jtbarMain.add(jSeparator8);

        jbtnWebADI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Excel tr MED.png"))); // NOI18N
        jbtnWebADI.setToolTipText("WebADI Template");
        jbtnWebADI.setFocusable(false);
        jbtnWebADI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnWebADI.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnWebADI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWebADIActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtnWebADI);

        jbtn2ndHop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/2ndhop_MED.png"))); // NOI18N
        jbtn2ndHop.setToolTipText("VCP for 2nd hops");
        jbtn2ndHop.setFocusable(false);
        jbtn2ndHop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtn2ndHop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtn2ndHop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn2ndHopActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtn2ndHop);

        jbtnPurFS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/2ndhop_2_MED.png"))); // NOI18N
        jbtnPurFS.setToolTipText("Purchasing FS for 2nd hops");
        jbtnPurFS.setFocusable(false);
        jbtnPurFS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnPurFS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnPurFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPurFSActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtnPurFS);
        jtbarMain.add(jSeparator7);

        jbtnEndeca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Endeca-Logo1_MED.png"))); // NOI18N
        jbtnEndeca.setToolTipText("Go to Endeca");
        jbtnEndeca.setFocusable(false);
        jbtnEndeca.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnEndeca.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnEndeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEndecaActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtnEndeca);

        jbtnODS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ODS_MED.png"))); // NOI18N
        jbtnODS.setToolTipText("Go to ODS");
        jbtnODS.setFocusable(false);
        jbtnODS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnODS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnODS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnODSActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtnODS);

        jSeparator9.setRequestFocusEnabled(false);
        jSeparator9.setVerifyInputWhenFocusTarget(false);
        jtbarMain.add(jSeparator9);

        jbtnTracks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Tracks_MED.png"))); // NOI18N
        jbtnTracks.setToolTipText("Open QuickTracks");
        jbtnTracks.setFocusable(false);
        jbtnTracks.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnTracks.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnTracks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTracksActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtnTracks);

        jSeparator15.setVerifyInputWhenFocusTarget(false);
        jtbarMain.add(jSeparator15);

        jlblSta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblSta.setText("Status");
        jlblSta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jlblSta.setOpaque(true);

        jlblUser.setText("User name and access level");

        jpnlBot.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exit_medium.png"))); // NOI18N
        jbtnExit.setText("Exit");
        jbtnExit.setToolTipText("Exit the Application");
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExitActionPerformed(evt);
            }
        });

        jbtnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout_lil.png"))); // NOI18N
        jbtnLogout.setText("Logout");
        jbtnLogout.setToolTipText("Go back to the Login Screen");
        jbtnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLogoutActionPerformed(evt);
            }
        });

        jlblModVer.setText("Planning Tools. Backorders Module. Dec-2017");

        javax.swing.GroupLayout jpnlBotLayout = new javax.swing.GroupLayout(jpnlBot);
        jpnlBot.setLayout(jpnlBotLayout);
        jpnlBotLayout.setHorizontalGroup(
            jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlBotLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblModVer, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnlBotLayout.setVerticalGroup(
            jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlBotLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlBotLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jlblModVer))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnlBot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpnlMid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtbarMain, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlblSta, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlblUser)
                    .addComponent(jlblSta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnlMid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jtbarMain, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlBot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnPlnDskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPlnDskActionPerformed
        openLink("https://global-ebusiness.oraclecorp.com:443/OA_HTML/RF.jsp?function_id=1021126&resp_id=1702662&resp_appl_id=523&security_group_id=0&lang_code=US&oas=yKpOwWHKT0rC5MTKcUQtyQ..");
    }//GEN-LAST:event_jbtnPlnDskActionPerformed

    private void jbtnWebADIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWebADIActionPerformed
        openLink("https://global-ebusiness.oraclecorp.com/OA_HTML/RF.jsp?function_id=1705633&resp_id=1702662&resp_appl_id=523&security_group_id=0&lang_code=US&oas=a7o9de913PKpCuRYY_Q4bA..");
    }//GEN-LAST:event_jbtnWebADIActionPerformed

    private void jbtn2ndHopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn2ndHopActionPerformed
        openLink("https://global-vcp.oraclecorp.com:443/OA_HTML/RF.jsp?function_id=40814&resp_id=50706&resp_appl_id=724&security_group_id=0&lang_code=US&oas=VVEux5NBpWUy36upRChIrw..");
    }//GEN-LAST:event_jbtn2ndHopActionPerformed

    private void jbtnPurFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPurFSActionPerformed
        openLink("https://global-ebusiness.oraclecorp.com:443/OA_HTML/RF.jsp?function_id=1644&resp_id=1682660&resp_appl_id=30027&security_group_id=0&lang_code=US&oas=z-icV0wf8-T7cignfiThLA..");
    }//GEN-LAST:event_jbtnPurFSActionPerformed

    private void jbtnEndecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEndecaActionPerformed
        openLink("https://eidap.oraclecorp.com/endeca/web/home/index");
    }//GEN-LAST:event_jbtnEndecaActionPerformed

    private void jbtnODSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnODSActionPerformed
        openLink("https://gcwap-ods.oraclecorp.com/analytics/saw.dll?Dashboard&PortalPath=%2Fshared%2FField%20Services%20Spares%2F_portal%2FGSL%20Supply%20Planning%20Dashboard&page=Open%20Back%20Orders");
    }//GEN-LAST:event_jbtnODSActionPerformed

    

    private void jbtnTracksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTracksActionPerformed
        /*
        String sAwb = "";
        int iSelTab = jtbpMain.getSelectedIndex();

        switch ( iSelTab ){
            case 0 : {

                break;
            }
            case 1 : {
                if ( this.jtblBackorders.getSelectedRow() > -1 ) {
                    sAwb = String.valueOf(jtblBackorders.getValueAt(jtblBackorders.getSelectedRow(),jtblBackorders.getSelectedColumn()));
                }
                break;
            }
            case 2 : {
                if ( jtblWebADI.getSelectedRow() > -1 ){
                    sAwb = String.valueOf(jtblWebADI.getValueAt(jtblWebADI.getSelectedRow(), 8));
                }
                break;
            }

        }
        if ( sAwb.equals("NA") ){sAwb = "";}
        QuickTracks tmpQT = new QuickTracks(sAwb);
        tmpQT.txtTracNumb.setText(sAwb);
        tmpQT.setLocationRelativeTo(this);
        tmpQT.setResizable(false);
        tmpQT.setVisible(true);
        */
    }//GEN-LAST:event_jbtnTracksActionPerformed

  
    
    private void jbtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExitActionPerformed
        int opc;
        opc = JOptionPane.showConfirmDialog(this,"Do you want to exit the Application?\n");
        if ( opc == 0 ){
            System.exit(0);
        }
        
    }//GEN-LAST:event_jbtnExitActionPerformed
    
    private void jbtnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLogoutActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to go back to the Login Screen?\n");
        if ( opc == 0 ){
            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            gui_LoginScreen tmpLS = new gui_LoginScreen();
            tmpLS.setLocationRelativeTo(this);
            tmpLS.setVisible(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            dispose();
        }
    }//GEN-LAST:event_jbtnLogoutActionPerformed

    private void jbtnBoPlImpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoPlImpActionPerformed
        xlsODSBOMatrix = null;
        System.out.println("Preparing to import ODS Excel Backorders data sheet");
        //Preparing a class Excel_Manager instance in order to import and create a bidimensional Array with data from .xls or .csv files
        cls_Excel_Manager xlsManager = new cls_Excel_Manager();
        //Imports a File (Excel file) from the HDD
        File fl  = xlsManager.importXLSFile();
        if ( fl == null )
        {
            JOptionPane.showMessageDialog(this, "The File was not imported");
        }
        else
        {
            this.cleanBoPlTable();
            //Gets the first Sheet of the File -if it exists-
            Sheet sh = xlsManager.createExcelSheet(fl);
            //Loads a Bidimentional Array with that Sheet
            xlsODSBOMatrix = xlsManager.loadXLSsheet_toArray(sh);
            //Identifyies the different columns on the BD-Array
            locateODSBOColumns();
            if ( validateODSBOXLSFile() == true ){
                JOptionPane.showMessageDialog(this,"The File was successfully imported");
                //Looks for lines in the BO DB ArrayList that are no longer reported in the new ODS XLS file
                //If any of those lines are not closed yet, it sets a "CHECK" on the BO Status field as a warning for the User
                checkClosedLines();
                //Creates the new lines taken from the ODS Backorders XLS file
                createNewBackorderLines();
                //Cleans and refreshes the BO data base table with the new lines and some lines in CHECK status
                cleanBoPlTable();
                //loads the BO table with the recently updated ArrayList
                loadBoPlTable();
                //Shows the final summary message
                if ( iNEW > 0 && iCHK > 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                        + "New Backorder Lines added: " + iNEW + "\n"
                        + "Lines ready to be closed: " + iCHK + "\n"
                        + "IMPORTANT: CHANGES WILL NOT BE REFLECTED UNTIL YOU SAVE THE DATA BASE.");
                }
                if ( iNEW > 0 && iCHK == 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                        + "New Backorder Lines added: " + iNEW + "\n"
                        + "No lines to close today.\n"
                        + "IMPORTANT: CHANGES WILL NOT BE REFLECTED UNTIL YOU SAVE THE DATA BASE.");
                }
                if ( iNEW == 0 && iCHK > 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                        + "No new lines added.\n"
                        + "Lines ready to be closed: " + iCHK + "\n"
                        + "IMPORTANT: CHANGES WILL NOT BE REFLECTED UNTIL YOU SAVE THE DATA BASE.");
                }
                if ( iNEW == 0 && iCHK == 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                        + "No new lines added.\n"
                        + "No lines to close today.\n");
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "The provided Excel file does not contain the necessary columns. Please double check", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jbtnBoPlImpActionPerformed

    private void jbtnBoPlExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoPlExpActionPerformed
        cls_Excel_Manager tmpXLS = new cls_Excel_Manager();
        try {
            if ( bBOPLFLAG ==false ){
                tmpXLS.exportBackordersPlDBtoCSVFile(this.alBoPlSearchResults);
            }
            else {
                tmpXLS.exportBackordersPlDBtoCSVFile(this.alBoPlDB);
            }
        }
        catch (WriteException ex) {
            Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtnBoPlExpActionPerformed

    private void jbtnBoPlRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoPlRefreshActionPerformed
        jtblBOPL.getRowSorter().setSortKeys(null);
        cleanBoPlTable();
        jtxtBoPlSearch.setText("");
        //Loads the information from the Backorders Data Base ArrayList into the corresponding JTable
        this.loadBoPlTable();
        if ( bONLINE == true ){
            loadRemBoPlQTYHist();
        }
        else{
            loadLocBoPlQTYHist();
        }
        JOptionPane.showMessageDialog(this,"The Backorders Data has been refreshed");
    }//GEN-LAST:event_jbtnBoPlRefreshActionPerformed

    private void jbtnBoPlSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoPlSearchActionPerformed
        if ( jtxtBoPlSearch.getText().equals("") ) {
            cleanBoPlTable();
            loadBoPlTable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else {
            searchTextBoPlDB(jtxtBoPlSearch.getText().toUpperCase());
            //Checks if the process detected results or not
            if ( alBoPlSearchResults.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
            }
            else {
                JOptionPane.showMessageDialog(this, alBoPlSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            }
        }
    }//GEN-LAST:event_jbtnBoPlSearchActionPerformed

    private void jtxtBoPlSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBoPlSearchKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            if ( jtxtBoPlSearch.getText().equals("") ) {
            cleanBoPlTable();
            loadBoPlTable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else {
                searchTextBoPlDB(jtxtBoPlSearch.getText().toUpperCase());
                //Checks if the process detected results or not
                if ( alBoPlSearchResults.isEmpty() ) {
                    JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
                }
                else {
                    JOptionPane.showMessageDialog(this, alBoPlSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
                }
            }
        }
    }//GEN-LAST:event_jtxtBoPlSearchKeyPressed

    private void jbtnBoArExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoArExpActionPerformed
        cls_Excel_Manager tmpXLS = new cls_Excel_Manager();
        try {
            if ( bBOARFLAG ==false ){
                tmpXLS.exportBackordersDBtoCSVFile(this.alBoArSearchResults);
            }
            else {
                tmpXLS.exportBackordersDBtoCSVFile(this.alBoArDB);
            }
        }
        catch (WriteException ex) {
            Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtnBoArExpActionPerformed

    private void jbtnBoArRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoArRefreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnBoArRefreshActionPerformed

    private void jbtnBoArSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoArSearchActionPerformed
        if ( jtxtBoArSearch.getText().equals("") ) {
            cleanBoArTable();
            loadBoArTable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else {
            searchTextBoArDB(jtxtBoArSearch.getText().toUpperCase());
            //Checks if the process detected results or not
            if ( alBoArSearchResults.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
            }
            else {
                JOptionPane.showMessageDialog(this, alBoArSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            }
        }
    }//GEN-LAST:event_jbtnBoArSearchActionPerformed

    private void jtxtBoArSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBoArSearchKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            if ( jtxtBoArSearch.getText().equals("") ) {
            cleanBoArTable();
            loadBoArTable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else {
                searchTextBoArDB(jtxtBoArSearch.getText().toUpperCase());
                //Checks if the process detected results or not
                if ( alBoArSearchResults.isEmpty() ) {
                    JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
                }
                else {
                    JOptionPane.showMessageDialog(this, alBoArSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
                }
            }
        }
    }//GEN-LAST:event_jtxtBoArSearchKeyPressed

    private void jbtnBoPlMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoPlMailActionPerformed
        int iRow = jtblBOPL.getSelectedRow();
        if ( iRow > -1 ){
            try {sendMail("", "", jtblBOPL.getValueAt(jtblBOPL.getSelectedRow(), 22).toString(), "", "na", "na");}
            catch (IOException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
            catch (URISyntaxException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting a line");
        }
    }//GEN-LAST:event_jbtnBoPlMailActionPerformed

    private void jbtnBOAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBOAddActionPerformed
        addBoPlNewLine();
    }//GEN-LAST:event_jbtnBOAddActionPerformed

    private void jbtnBODelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBODelActionPerformed
        //Captures the selected Lines
        int[] selLines = jtblBOPL.getSelectedRows();
        if ( selLines.length > 0 ){
            int opc = JOptionPane.showConfirmDialog(this,"THE SELECTED LINE(s) WILL BE DELETED.\n Do you want to proceed.");
            if ( opc == 0 ){
                //Checks if the current data in the screen is the actual DB or just searching results
                if ( bBOPLFLAG == true ) {
                    for ( int i=0; i<selLines.length; i++ ){
                        //Determines the position of the highlighted lines in the main DB Array List
                        int iDelPos = findBoPlDBPos(captureBoPLLine(selLines[i]));
                        //Captures the data in the highlighted line; finds its position into the BD Array List and deletes it
                        alBoPlDB.remove(iDelPos);
                    }
                }
                else {
                    int iCnt = 0;
                    for ( int i=0; i<selLines.length; i++ ){
                        //The position to be deleted will vary at the moment at a line is deleted
                        alBoPlDB.remove(Integer.valueOf(this.alBoPlSearchResults.get(selLines[i]).getPosi()) - iCnt);
                        iCnt++;
                    }
                }
                JOptionPane.showMessageDialog(this,"The selected line(s) were deleted from your local Backorders Data Base.\nCHANGES WILL NOT AFFECT THE DATA BASE UNTIL YOU SAVE.");
            }
            else {
                JOptionPane.showMessageDialog(this,"No changes applied to the Data Base.");
            }
            jtxtBoPlSearch.setText("");
            //Resets the data base chart sort
            jtblBOPL.getRowSorter().setSortKeys(null);
            //Reloads the data base in the screen and updates the QTYs
            cleanBoPlTable();
            loadBoPlTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting one line at least");
        }
    }//GEN-LAST:event_jbtnBODelActionPerformed

    private void jbtnBoPlSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoPlSaveActionPerformed
        if ( bONLINE == true ){
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite the remote Backorders Planning Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Updates the ArrayList with the info from the JTable
                updateBoPl_al();
                //Updates the remote .txt DB file with the info from the ArrayList
                uploadRemBoPlDB();
                //Updates screen counters
                loadRemBoPlQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
        else{
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite your local Backorders Planning Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Updates the ArrayList with the info from the JTable
                updateBoPl_al();
                //Updates the .txt DB file with the info from the ArrayList
                updateBoPlDB_txt();
                //Updates screen counters
                loadLocBoPlQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_jbtnBoPlSaveActionPerformed

    private void jbtnBoArImpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBoArImpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnBoArImpActionPerformed
    
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(gui_BO_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui_BO_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui_BO_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui_BO_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui_BO_MainScreen().setVisible(true);
            }
        });
    }

    //<editor-fold defaultstate="collapsed" desc="Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JToolBar.Separator jSeparator15;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JButton jbtn2ndHop;
    private javax.swing.JButton jbtnBOAdd;
    private javax.swing.JButton jbtnBODel;
    private javax.swing.JButton jbtnBoArExp;
    private javax.swing.JButton jbtnBoArImp;
    private javax.swing.JButton jbtnBoArRefresh;
    public javax.swing.JButton jbtnBoArSearch;
    private javax.swing.JButton jbtnBoPlExp;
    private javax.swing.JButton jbtnBoPlImp;
    private javax.swing.JButton jbtnBoPlMail;
    private javax.swing.JButton jbtnBoPlRefresh;
    private javax.swing.JButton jbtnBoPlSave;
    public javax.swing.JButton jbtnBoPlSearch;
    private javax.swing.JButton jbtnEndeca;
    private javax.swing.JButton jbtnExit;
    private javax.swing.JButton jbtnLogout;
    private javax.swing.JButton jbtnODS;
    private javax.swing.JButton jbtnPlnDsk;
    private javax.swing.JButton jbtnPurFS;
    private javax.swing.JButton jbtnTracks;
    private javax.swing.JButton jbtnWebADI;
    private javax.swing.JLabel jlblAdd;
    private javax.swing.JLabel jlblBOARDBsize;
    private javax.swing.JLabel jlblBOARFlag;
    private javax.swing.JLabel jlblBOPLDBsize;
    private javax.swing.JLabel jlblBOPLFlag;
    private javax.swing.JLabel jlblExp;
    private javax.swing.JLabel jlblExpAr;
    private javax.swing.JLabel jlblImp;
    private javax.swing.JLabel jlblImpAr;
    private javax.swing.JLabel jlblMail;
    private javax.swing.JLabel jlblModVer;
    private javax.swing.JLabel jlblRefresh;
    private javax.swing.JLabel jlblRefreshAr;
    private javax.swing.JLabel jlblRemove;
    private javax.swing.JLabel jlblSave;
    private javax.swing.JLabel jlblSearch;
    private javax.swing.JLabel jlblSearch1;
    private javax.swing.JLabel jlblSearchAr;
    public javax.swing.JLabel jlblSta;
    private javax.swing.JLabel jlblTop;
    public javax.swing.JLabel jlblUser;
    private javax.swing.JPanel jpnlARDB;
    private javax.swing.JPanel jpnlBODB;
    private javax.swing.JPanel jpnlBot;
    private javax.swing.JPanel jpnlMid;
    private javax.swing.JPanel jpnlMidBoAr;
    private javax.swing.JPanel jpnlMidBoPl;
    private javax.swing.JPanel jpnlTools;
    private javax.swing.JPanel jpnlTop;
    private javax.swing.JToolBar jtbarMain;
    private javax.swing.JTable jtblBOAR;
    private javax.swing.JTable jtblBOPL;
    private javax.swing.JTabbedPane jtbpnMain;
    public javax.swing.JTextField jtxtBoArSearch;
    public javax.swing.JTextField jtxtBoPlSearch;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}

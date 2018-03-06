package gn_righthand;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import sun.misc.BASE64Encoder;



public class gui_MainScreen extends javax.swing.JFrame {

    //VARIABLES DECLARATION SECTION ********************************************************
    
    //Prepares the variables with the User credentials
    private String sUser = "";//User's Oracle e-mail
    private String sPass = "";//Encrypted SSO pass
    private boolean bONLINE = false;
    private String sVer = "";
    private String sName = "";
    private String sPriv = "";
    

    String sLocCoDBPath = "C:\\Users\\jfalvara\\Desktop\\Jav\\Progra\\DB Argentina Consults\\Consults_DB.txt"; //DEVELOPMENT PHASE PATH
    String sLocBoDBPath = "C:\\Users\\jfalvara\\Desktop\\Jav\\Progra\\DB Argentina Consults\\Backorders_DB.txt"; //DEVELOPMENT PHASE PATH
    String sLocWaDBPath = "C:\\Users\\jfalvara\\Desktop\\Jav\\Progra\\DB Argentina Consults\\WebADI_DB.txt"; //DEVELOPMENT PHASE PATH
    String sPPSEDBPath = "C:\\Users\\jfalvara\\Desktop\\Jav\\Progra\\DB Argentina Consults\\PPSE Data Sheet.xls"; //DEVELOPMENT PHASE PATH
//    private String sRemCoDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_Test_Env/Consults_DB.txt"; 
    private String sRemBoDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_Test_Env/Backorders_DB.txt";
    private String sRemWaDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_Test_Env/WebADI_DB.txt";
    
    
//    String sLocCoDBPath = "C:\\Program Files (x86)\\Oracle Spares Planning\\GN Righthand\\Data Files\\Consults_DB.txt"; //PRODUCTION PHASE PATH
//    String sLocBoDBPath = "C:\\Program Files (x86)\\Oracle Spares Planning\\GN Righthand\\Data Files\\Backorders_DB.txt"; //PRODUCTION PHASE PATH
//    String sLocWaDBPath = "C:\\Program Files (x86)\\Oracle Spares Planning\\GN Righthand\\Data Files\\WebADI_DB.txt"; //PRODUCTION PHASE PATH
//    String sPPSEDBPath = "C:\\Program Files (x86)\\Oracle Spares Planning\\GN Righthand\\Data Files\\PPSE Data Sheet.xls"; //PRODUCTION PHASE PATH
    private String sRemCoDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB/Consults_DB.txt"; 
//    private String sRemBoDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB/Backorders_DB.txt";
//    private String sRemWaDBPath = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB/WebADI_DB.txt";
    
    
    
    //Preparing the main screen table model variables
    javax.swing.table.DefaultTableModel tblModelPartsList = new javax.swing.table.DefaultTableModel();
    Object[] PartsListColumn = new Object [8];
    //Preparing the current consults table model variables
    javax.swing.table.DefaultTableModel tblModelConsultsList = new javax.swing.table.DefaultTableModel();
    Object[] ConsultsColumn = new Object [13];
    //Preparing the data base table model variables
    javax.swing.table.DefaultTableModel tblModelDataBase = new javax.swing.table.DefaultTableModel();
    Object[] DataBaseColumn = new Object [14];
    //Preparing the backorders table model variables
    javax.swing.table.DefaultTableModel tblModelBackorders = new javax.swing.table.DefaultTableModel();
    Object[] BOColumn = new Object [27];
    //Preparing the WebADI table model variables
    javax.swing.table.DefaultTableModel tblModelWebADI = new javax.swing.table.DefaultTableModel();
    Object[] WebADIColumn = new Object [15];
    private String [] sarTiers;
    //Bidimentional String Array that will store all the data from an .xls file
    private String[][] xlsDataMatrix;
    //Bidimentional String Array that will store all the data from an .xls ODS backorders file
    private String[][] xlsODSBOMatrix;
    //Bidimentional String Array that will store all the data from an .xls backorders file
    private String[][] xlsBOMatrix;
    //Bidimentional String Array that will store all the data from an .xls WebADI Orders file
    private String[][] xlsWebADIMatrix;
    //Bidimentional String Array that will store all the data from an .xls PPSE Orders file
    private String[][] xlsPPSEMatrix;
    //Creating variables to locate columns in the main data base imported file
    private int iReg = -1, iCountry = -1, iOrgName = -1, iOrgCode = -1, iTier = -1, 
            iPN = -1, iOHTot = -1, iEXTot = -1;
    //Creating variables to locate colums in the ODS Backorders imported file
    private int iDate_odsbo = -1, iSvRq_odsbo = -1, iTask_odsbo = -1, iISO_odsbo = -1, iItem_odsbo = -1, 
        iQty_odsbo = -1, iDesc_odsbo = -1, iTkSt_odsbo = -1, iPLC_odsbo = -1;
    //Creating variables to locate columns in the WebADI Orders imported file
    private int iISO = -1, iLine = -1, iItem = -1, iQTY = -1, iShipMeth = -1, 
            iCreaDate = -1, iSrc = -1, iDes = -1, iSR = -1, iADITsk = -1, iSIMI = -1;
    //Creating variables to locate columns in the PPSE Orders imported file
    private int iItem_ppse = -1, iPLC_ppse = -1, iDisp_ppse = -1, iCrit_ppse = -1;
    //ArrayList that will store the current list of parts and orgs for potential consults
    private ArrayList<cls_PartDataReq> alGNSearchList = new ArrayList<>();
    //ArrayList that will store the complete data base of consults
    private ArrayList<cls_PartDataReq> alCosulDB = new ArrayList<>();
    //ArrayList that will store the complete data base of WebADI entries
    private ArrayList<cls_WebADI_Data> alWebadiDB = new ArrayList<>();
    //ArrayList that will store the complete data base of Backorders entries
    private ArrayList<cls_BO_Data> alBckordDB = new ArrayList<>();
    //ArrayList that will store the complete data base of ppse entries
    private ArrayList<cls_BO_Data> alPPSE_DB = new ArrayList<>();
    //Screen counters
    private int iCoQTY = 0;
    private int iWaQTY = 0;
    private int iBoQTY = 0;
    private int iMaQTY = 0;
    //Backorders counters for new lines
    private int iCHK = 0;
    private int iNEW= 0;
    //ArrayList that will store the search results on the Main Consults Data Base
    private ArrayList<cls_PartDataReq> alConsulSearchResults = new ArrayList<>();
    //ArrayList that will store the search results on the WebADI Data Base
    private ArrayList<cls_WebADI_Data> alWebadiSearchResults = new ArrayList<>();
    //ArrayList that will store the search results on the Backorders Data Base
    private ArrayList<cls_BO_Data> alBckordSearchResults = new ArrayList<>();
    //Global Flag that indicates that the DB Manager tabs is showing the actual Data Base
    //When this flags goes False, it indicates that, what is shown in the screen, are just searching results
    boolean bDBFLAG = true;
    boolean bWAFLAG = true;
    boolean bBOFLAG = true;
    int iConsultedPartsQTY = 0;
    String sTrackings = "";
    String sISO_SIMI_Report = "";//This String will store the report after updating the ISO and SIMIs in the BO-DB form the WebADI file
    //Global variables for USA T3 mails
    String sUSAParts = "";
    String sUSATracking = "";
    
    private int iMODE; //1.Working on Consults - 2. Working on Data Bases
    
    /*
    //Array List that will store all the Lines from the Excel file provided by the User
    private ArrayList<cls_PartData> alPartsList = new ArrayList<>();
    */
    
    //CONSTRUCTORS SECTION ****************************************************************
    public gui_MainScreen(int iMODE, boolean bONLINE, String sUser, String sPass, String sVer, String sName, String sPriv) {
        initComponents();
        System.out.println("Starting Tool");
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("ARGENTINA PLANNING TOOLS - GD RIGHTHAND");
        
        this.sUser = sUser;
        this.sPass = sPass;
        this.bONLINE = bONLINE;
        this.sVer = sVer;
        this.sName = sName;
        this.sPriv = sPriv;
        
        this.iMODE = iMODE;
        enableTabs(this.iMODE);
        
        //Loads the data bases from the corresponding .txt file into ArrayLists
        if ( bONLINE == true ){
            System.out.println("User is working ONLINE. Loading remote Data Bases");
            loadRemConsulDB();
            loadRemBckordDB();
            loadRemWebadiDB();
            //Loads historical QTYs in the corresponding jlabels
            loadRemConsQTYHist();
            loadRemBackordersQTYHist();
            loadRemWebADIQTYHist();
        }
        else{
            System.out.println("User is working OFFLINE. Loading local Data Bases");
            loadConsulDB();
            loadBckordDB();
            loadWebadiDB();
            //Loads historical QTYs in the corresponding jlabels 
            loadConsultsQTYHist();
            loadBackordersQTYHist();
            loadWebADIQTYHist();
        }
        
        //Configures the jtables in order to receive and show data 
        configPartsListTable();
        configNewConsultsTable();
        configConsultsDBTable();
        configBackordersTable();
        configWebADITable();
        //Loads the corresponding data from the ArrayLists into each jtable
        loadConsultsDBTable();
        loadBackordersTable();
        loadWebADITable();
        
        updateConsultsTXTDataBase();
        
        this.jlblTop.setText("GN-Righthand " + this.sVer.substring(0,4) );
        this.jlblUser.setText("<html><font color='blue'>Login name: </font>" + sName + ". <font color='blue'>User Level: </font>" 
                + this.getAccsLevel() + "</html>");
        this.jlblLineQTY.setText("00");
        this.jlstOrgsTots.setMultipleMode(true);
        this.rbtnReplen.setSelected(true);
        
        setaccessLevel();
        
    }

    private gui_MainScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    //MAIN METHODS SECTION ****************************************************************
    
    //Enables only the necessary tabs, depending on the button selected by the User in the login screen
    private void enableTabs(int iSel){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        if ( iSel == 1 ){
            jtbpMain.removeAll();
            jtbpMain.add("Good New Search", this.jpnlMain);
            jtbpMain.add("Selection Tool", this.jpnlTools);
            jtbpMain.add("Consults List", this.jpnlConsults);
            jbtnRST.setEnabled(false);//Disables de Reset button
            jbtnSwitch.setText("Data Bases");
        }
        else{
            jtbpMain.removeAll();
            jtbpMain.add("Consults DB", this.jpnlDataBase);
            jtbpMain.add("Backorders DB", this.jpnlBackorders);
            jtbpMain.add("WebADI DB", this.jpnlWebADI);
            jbtnRST.setEnabled(true);//Enables de Reset button
            jbtnSwitch.setText("New Consults");
        }
    }
    //</editor-fold>
    
    
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
    
    //Indetifies the access level
    private String getAccsLevel(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sLevel = "N/A";
        switch ( this.sPriv ){
            case "Development" : {
                sLevel = "Development Level Control.";
                break;
            }
            case "Admin" : {
                sLevel = "Administrator Level.";
                break;
            }
            case "Reader" : {
                sLevel = "Data Base Reader Level.";
                break;
            }
            case "Offline" : {
                sLevel = "Offline User. Access limited.";
                break;
            }
        } 
        return sLevel;
    }
    //</editor-fold>
    
    //Restrics access to different options depending on the User level privilege
    private void setaccessLevel(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        switch ( sPriv ){
            case "Reader" :{
                //Disabling unnecesary tabs
                jtbpMain.setEnabledAt(0, false);//This action disables the Consults Tab
                jtbpMain.setSelectedIndex(1);//Selects the Backorders Data Base tab
                //Disabling buttons on the bottom panel
                this.jbtnSwitch.setEnabled(false);//Disables the button to switch to the Consults Engine
                
                //Disabling buttons on the right panel
                //this.jbtnPlnDsk.setEnabled(false);
                //this.jbtnWebADI.setEnabled(false);
                //this.jbtn2ndHop.setEnabled(false);
                //this.jbtnPurFS.setEnabled(false);
                //this.jbtnEndeca.setEnabled(false);
                //this.jbtnODS.setEnabled(false);
                this.jbtnRST.setEnabled(false);//Disables the main Reset button
                
                //Disabling buttons on the Backorders tab
                this.jbtnBOImp.setEnabled(false);
                this.jbtnBOMail.setEnabled(false);
                this.jbtnBOAdd.setEnabled(false);
                this.jbtnBODel.setEnabled(false);
                this.jbtnBOSave.setEnabled(false);
                //Disabling buttons on the WebADI tab
                this.jbtnWAdd.setEnabled(false);
                this.jbtnWDel.setEnabled(false);
                this.jbtnWSave.setEnabled(false);
                this.jbtnWImp.setEnabled(false);
                //Disabling options in the main upper menu
                this.jmeitImport.setEnabled(false);
                this.jmeitExport.setEnabled(false);
                this.jmeitTempTools.setEnabled(false);
                this.jmeitUserPriv.setEnabled(false); 
                this.jmenDBbackup.setEnabled(false);
                break;
            }
            case "Offline" :{
                //Disabling unnecesary tabs
                jtbpMain.setEnabledAt(0, false);//This action disables the Consults Tab
                jtbpMain.setSelectedIndex(1);//Selects the Backorders Data Base tab
                //Disabling buttons on the bottom panel
                this.jbtnSwitch.setEnabled(false);//Disables the button to switch to the Consults Engine
                
                //Disabling buttons on the right panel
                //this.jbtnPlnDsk.setEnabled(false);
                //this.jbtnWebADI.setEnabled(false);
                //this.jbtn2ndHop.setEnabled(false);
                //this.jbtnPurFS.setEnabled(false);
                //this.jbtnEndeca.setEnabled(false);
                //this.jbtnODS.setEnabled(false);
                this.jbtnRST.setEnabled(false);//Disables the main Reset button
                
                //Disabling buttons on the Backorders tab
                this.jbtnBOImp.setEnabled(false);
                this.jbtnBOMail.setEnabled(false);
                this.jbtnBOAdd.setEnabled(false);
                this.jbtnBODel.setEnabled(false);
                this.jbtnBOSave.setEnabled(false);
                //Disabling buttons on the WebADI tab
                this.jbtnWAdd.setEnabled(false);
                this.jbtnWDel.setEnabled(false);
                this.jbtnWSave.setEnabled(false);
                this.jbtnWImp.setEnabled(false);
                //Disabling options in the main upper menu
                this.jmeitImport.setEnabled(false);
                this.jmeitExport.setEnabled(false);
                this.jmeitTempTools.setEnabled(false);
                this.jmeitUserPriv.setEnabled(false);
                this.jmenDBbackup.setEnabled(false);
                break;
            }
        }
    }
    //</editor-fold>
    
    //Identifies the column numbers on the data base imported Excel file 
    private void locateColumns(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        //Reset PartsListColumn values
        iReg = -1; iCountry = -1; iOrgName = -1; iOrgCode = -1; iTier = -1; iPN = -1; iOHTot = -1; iEXTot = -1;
        //FOR Cycle in order to identify the coumn number depending on the PartsListColumn name
        System.out.println("Detecting Matrix dimmentions.");
        System.out.println("Columns: " + xlsDataMatrix[0].length + " / Rows: " + xlsDataMatrix.length);
        System.out.println("Identifying columns");
        for ( int c=0; c<xlsDataMatrix[0].length; c++ )
        {
            if ( xlsDataMatrix[0][c].equals("Region") ){iReg = c;}
            if ( xlsDataMatrix[0][c].equals("Country Name") ){iCountry = c;}
            if ( xlsDataMatrix[0][c].equals("OrgName") ){iOrgName = c;}
            if ( xlsDataMatrix[0][c].equals("OrgCode") ){iOrgCode = c;}
            if ( xlsDataMatrix[0][c].equals("Tier") ){iTier = c;}
            if ( xlsDataMatrix[0][c].equals("Part Number") ){iPN = c;}
            if ( xlsDataMatrix[0][c].equals("OnHand") ){iOHTot = c;}
            if ( xlsDataMatrix[0][c].equals("Excess") ){iEXTot = c;}
        }
    }
    //</editor-fold>
    
    //Identifies the column numbers on the ODS Backorders imported Excel file
    private void locateODSBOColumns(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        //Reset column values
        iDate_odsbo = -1; iSvRq_odsbo = -1; iTask_odsbo = -1; iISO_odsbo = -1; iItem_odsbo = -1; 
        iQty_odsbo = -1; iDesc_odsbo = -1; iTkSt_odsbo = -1; iPLC_odsbo = -1;
        
        //FOR Cycle in order to identify the coumn number depending on the PartsListColumn name
        System.out.println("Detecting ODS Backorders Matrix dimmentions.");
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
        }
    }
    //</editor-fold>
    
    //Identifies the column numbers  on the WebADI imported Excel file 
    private void locateWebADIColumns(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        //Reset PartsListColumn values
        iISO = -1; iItem = -1; iQTY = -1; iShipMeth = -1; iCreaDate = -1; iSrc = -1; iDes = -1; iSR = -1; iADITsk = -1; iSIMI = -1;
        //FOR Cycle in order to identify the coumn number depending on the PartsListColumn name
        System.out.println("Detecting WebADI Matrix dimmentions.");
        System.out.println("Columns: " + xlsWebADIMatrix[0].length + " / Rows: " + xlsWebADIMatrix.length);
        System.out.println("Identifying columns");
        for ( int c=0; c<xlsWebADIMatrix[0].length; c++ )
        {
            if ( xlsWebADIMatrix[6][c].equals("Order Number#") ){iISO = c;}
            if ( xlsWebADIMatrix[6][c].equals("Item") ){iItem = c;}
            if ( xlsWebADIMatrix[6][c].equals("Quantity") ){iQTY = c;}
            if ( xlsWebADIMatrix[6][c].equals("Shipping Method") ){iShipMeth = c;}
            if ( xlsWebADIMatrix[6][c].equals("Creation Date") ){iCreaDate = c;}
            if ( xlsWebADIMatrix[6][c].equals("Source Org#") ){iSrc = c;}
            if ( xlsWebADIMatrix[6][c].equals("Destination Org") ){iDes = c;}
            if ( xlsWebADIMatrix[6][c].equals("Ref Number") ){iSR = c;}
            if ( xlsWebADIMatrix[6][c].equals("Task Number") ){iADITsk = c;}
            if ( xlsWebADIMatrix[6][c].equals("ETA Info") ){iSIMI = c;}
        }
    }
    //</editor-fold>
    
    
    
    /*PPSE DATA HANDLING METHODS*/
    
    //Identifies the column numbers  on the PPSE imported Excel file 
    private void locatePPSEColumns(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        //Reset PartsListColumn values
        iItem_ppse = -1; iPLC_ppse = -1; iDisp_ppse = -1; iCrit_ppse = -1;
        //FOR Cycle in order to identify the coumn number depending on the PartsListColumn name
        System.out.println("Detecting PPSE Matrix dimmentions.");
        System.out.println("Columns: " + xlsPPSEMatrix[0].length + " / Rows: " + xlsPPSEMatrix.length);
        System.out.println("Identifying columns");
        for ( int c=0; c<xlsPPSEMatrix[0].length; c++ )
        {
            if ( xlsPPSEMatrix[0][c].equals("Item") ){iItem_ppse = c;}
            if ( xlsPPSEMatrix[0][c].equals("PLC") ){iPLC_ppse = c;}
            if ( xlsPPSEMatrix[0][c].equals("Recovered Part Disposition Code") ){iDisp_ppse = c;}
            if ( xlsPPSEMatrix[0][c].equals("ABC Criticality") ){iCrit_ppse = c;}
        }
        System.out.println("Item: " + iItem_ppse);
        System.out.println("PLC: " + iPLC_ppse);
        System.out.println("Disp: " + iDisp_ppse);
        System.out.println("Crit: " + iCrit_ppse);
    }
    //</editor-fold>
    
    //Load the PPSE Data Base from a local Excel (97-2003) file into a 2D Data Matrix
    private void loadPPSEfromXLS(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        xlsPPSEMatrix = null;
        System.out.println("Preparing to import Excel PPSE sheet");
        //Preparing a class Excel_Manager instance in order to import and create a bidimensional Array with data from .xls or .csv files
        cls_Excel_Manager xlsManager = new cls_Excel_Manager();
        //Imports a File (Excel file) from the HDD
        File fl  = new File(sPPSEDBPath);
        try{
            //Gets the first Sheet of the File -if it exists-
            Sheet sh = xlsManager.createExcelSheet(fl);
            //Creates a Bidimentional Array with that Sheet
            xlsPPSEMatrix = xlsManager.loadXLSsheet_toArray(sh);
            //Identifyies the different columns on the BD-Array
            locatePPSEColumns();
            if ( this.validatePPSEXLSFile() == true ){
            System.out.println("The PPSE Excel File was successfully imported");
            }
            else {
                System.out.println("The local PPSE Excel file does not contain the necessary columns/data. Please double check");
            }
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while reading the PPSE Data File from Desktop.\n New Lines were not loaded. Please refresh.\n" + e, "AGENTINA PLANNING", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Gets the Part Criticality from the loaded PPSE Data 2D-Matrix
    private String getCriticality(String sPart){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sCrit = "NA";
        for ( int i=0; i<xlsPPSEMatrix.length; i++ ){
            if ( xlsPPSEMatrix[i][iItem_ppse].equals(sPart) ){
                sCrit = xlsPPSEMatrix[i][iCrit_ppse];
                break;
            }
        }
        return sCrit;
    }
    //</editor-fold>
    
    //Gets the Part Disposition from the loaded PPSE Data 2D-Matrix
    private String getDisposition(String sPart){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sDisp = "NA";
        for ( int i=0; i<xlsPPSEMatrix.length; i++ ){
            if ( xlsPPSEMatrix[i][iItem_ppse].equals(sPart) ){
                if ( xlsPPSEMatrix[i][iDisp_ppse].equals("N") ){
                    sDisp = "Consumable";
                }
                if ( xlsPPSEMatrix[i][iDisp_ppse].equals("S") ){
                    sDisp = "Repairable";
                }
                break;
            }
        }
        System.out.println("Disp Code: " + sDisp);
        return sDisp;
    }
    //</editor-fold>
    
    
    
    
    
    
    //Gets basic information from a given task and creates an HTML String with it
    private String getTaskInfo(String sTask){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sTaskInfo = "";
        int iPos = -1;
        for ( int i=0; i<alBckordDB.size(); i++ ){
            if ( alBckordDB.get(i).getTask().equals(sTask) ){
                iPos = i;
            }
        }
        if ( iPos > -1 ){
            sTaskInfo = sTaskInfo + "<html><font color='blue'>Status: </font>" + alBckordDB.get(iPos).getTkSt() + "<br>"
                    + "<font color='blue'>Req Date: </font>" + alBckordDB.get(iPos).getDate() + "<br>"
                    + "<font color='blue'>Serv. Req.: </font>" + alBckordDB.get(iPos).getSvRq() + "<br>"
                    + "<font color='blue'>Order #: </font>" + alBckordDB.get(iPos).getISO() + "<br>"
                    + "<font color='blue'>Part: </font>" + alBckordDB.get(iPos).getItem() + "<br>"
                    + "<font color='blue'>QTY needed: </font>" + alBckordDB.get(iPos).getQty() + "<br>"
                    + "<font color='blue'>S.A.: </font>" + alBckordDB.get(iPos).getSrAs() + "</html>";
        }
        return sTaskInfo;
    }
    //</editor-fold>
    
    
    
    /*TEMPORARY METHODS*/
    
    
    private void checkContent(){
        int r=0,c=0;
        r  = Integer.valueOf(JOptionPane.showInputDialog("Set row: "));
        c  = Integer.valueOf(JOptionPane.showInputDialog("Set col: "));
        System.out.println(this.xlsODSBOMatrix[r][c]);
    }
    
    private void checkArrayList(){
        int pos = -1;
        pos = Integer.valueOf(JOptionPane.showInputDialog("Set Pos: "));
        System.out.println(alWebadiDB.get(pos).getCIB());
        System.out.println(alWebadiDB.get(pos).getCom());
    }
    
    
    /*TEMPORARY METHODS END*/
    
    
    //Identifies the kind of activity: 1. Replenishment (Good Excess) / 2. Backorders (Good On-Hand)
    private int checkActivity(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        int flag = -1;
        if ( rbtnReplen.isSelected() ){
            flag = 1;
        }
        else{
            flag = 2;
        }
        return flag;
    }
    //</editor-fold>
    
    
    /*  *****JTABLES MANAGEMENT RELATED METHODS***** */
    
    
    //CONFIGURING TABLES

    //Prepares the JTable columns in order to receive the list of parts and locations from the Excel file
    private void configPartsListTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        tblModelPartsList.addColumn("Region");
        tblModelPartsList.addColumn("Country");
        tblModelPartsList.addColumn("ORG Name");
        tblModelPartsList.addColumn("ORG Code");
        tblModelPartsList.addColumn("Tier");
        tblModelPartsList.addColumn("PN#");
        tblModelPartsList.addColumn("Good OH");
        tblModelPartsList.addColumn("Good Ex");
        jtblParts.setModel(tblModelPartsList);
        //Allows the user to sort the items ina PartsListColumn
        jtblParts.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblParts.getTableHeader();
        header.setBackground(Color.black);
        header.setForeground(Color.white);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblParts.setRowHeight(22);
        jtblParts.getColumnModel().getColumn(0).setPreferredWidth(120);
        jtblParts.getColumnModel().getColumn(0).setResizable(false);
        jtblParts.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblParts.getColumnModel().getColumn(1).setPreferredWidth(180);
        jtblParts.getColumnModel().getColumn(1).setResizable(false);
        jtblParts.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtblParts.getColumnModel().getColumn(2).setPreferredWidth(250);
        jtblParts.getColumnModel().getColumn(2).setResizable(false);
        jtblParts.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtblParts.getColumnModel().getColumn(3).setPreferredWidth(90);
        jtblParts.getColumnModel().getColumn(3).setResizable(false);
        jtblParts.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        jtblParts.getColumnModel().getColumn(4).setPreferredWidth(70);
        jtblParts.getColumnModel().getColumn(4).setResizable(false);
        jtblParts.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);        
        jtblParts.getColumnModel().getColumn(5).setPreferredWidth(130);
        jtblParts.getColumnModel().getColumn(5).setResizable(false);
        jtblParts.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jtblParts.getColumnModel().getColumn(6).setPreferredWidth(70);
        jtblParts.getColumnModel().getColumn(6).setResizable(false);
        jtblParts.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        jtblParts.getColumnModel().getColumn(7).setPreferredWidth(70);
        jtblParts.getColumnModel().getColumn(7).setResizable(false);
        jtblParts.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        
    }
    //</editor-fold>
    
    //Prepares the JTable columns in order to receive the lis of candidates to send follow up mails
    private void configNewConsultsTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        tblModelConsultsList.addColumn("TIER");
        tblModelConsultsList.addColumn("REGION");
        tblModelConsultsList.addColumn("COUNTRY");
        tblModelConsultsList.addColumn("ORG");
        tblModelConsultsList.addColumn("PART");
        tblModelConsultsList.addColumn("QTY");
        tblModelConsultsList.addColumn("Activity");
        tblModelConsultsList.addColumn("G-OH");
        tblModelConsultsList.addColumn("G-Ex");
        tblModelConsultsList.addColumn("Task");
        tblModelConsultsList.addColumn("Current Date");
        tblModelConsultsList.addColumn("Last Req Date");
        tblModelConsultsList.addColumn("Prev Cons");
        jtblConsults.setModel(tblModelConsultsList);
        //Allows the user to sort the items ina PartsListColumn
        jtblConsults.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblConsults.getTableHeader();
        header.setBackground(Color.black);
        header.setForeground(Color.yellow);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblConsults.setRowHeight(22);
        jtblConsults.getColumnModel().getColumn(0).setPreferredWidth(70);
        jtblConsults.getColumnModel().getColumn(0).setResizable(false);
        jtblConsults.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(1).setPreferredWidth(90);
        jtblConsults.getColumnModel().getColumn(1).setResizable(false);
        jtblConsults.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(2).setPreferredWidth(170);
        jtblConsults.getColumnModel().getColumn(2).setResizable(false);
        jtblConsults.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(3).setPreferredWidth(65);
        jtblConsults.getColumnModel().getColumn(3).setResizable(false);
        jtblConsults.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);        
        jtblConsults.getColumnModel().getColumn(4).setPreferredWidth(130);
        jtblConsults.getColumnModel().getColumn(4).setResizable(false);
        jtblConsults.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(5).setPreferredWidth(65);
        jtblConsults.getColumnModel().getColumn(5).setResizable(false);
        jtblConsults.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(6).setPreferredWidth(130);
        jtblConsults.getColumnModel().getColumn(6).setResizable(false);
        jtblConsults.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(7).setPreferredWidth(65);
        jtblConsults.getColumnModel().getColumn(7).setResizable(false);
        jtblConsults.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(8).setPreferredWidth(65);
        jtblConsults.getColumnModel().getColumn(8).setResizable(false);
        jtblConsults.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(9).setPreferredWidth(130);
        jtblConsults.getColumnModel().getColumn(9).setResizable(false);
        jtblConsults.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(10).setPreferredWidth(120);
        jtblConsults.getColumnModel().getColumn(10).setResizable(false);
        jtblConsults.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(11).setPreferredWidth(120);
        jtblConsults.getColumnModel().getColumn(11).setResizable(false);
        jtblConsults.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
        jtblConsults.getColumnModel().getColumn(12).setPreferredWidth(100);
        jtblConsults.getColumnModel().getColumn(12).setResizable(false);
        jtblConsults.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
        
    }
    //</editor-fold>
    
    //Prepares the JTable columns in order to receive the history of consults
    private void configConsultsDBTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        tblModelDataBase.addColumn("TIER");
        tblModelDataBase.addColumn("REGION");
        tblModelDataBase.addColumn("COUNTRY");
        tblModelDataBase.addColumn("ORG");
        tblModelDataBase.addColumn("PART");
        tblModelDataBase.addColumn("QTY");
        tblModelDataBase.addColumn("Activity");
        tblModelDataBase.addColumn("G-OH");
        tblModelDataBase.addColumn("G-Ex");
        tblModelDataBase.addColumn("Consult Date");
        tblModelDataBase.addColumn("DOM");
        tblModelDataBase.addColumn("Moved");
        tblModelDataBase.addColumn("Task");
        tblModelDataBase.addColumn("Tracking");
        jtblDataBase.setModel(tblModelDataBase);
        //Allows the user to sort the items ina PartsListColumn
        jtblDataBase.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblDataBase.getTableHeader();
        header.setBackground(Color.darkGray);
        header.setForeground(Color.white);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblDataBase.setRowHeight(22);
        jtblDataBase.getColumnModel().getColumn(0).setPreferredWidth(65);
        jtblDataBase.getColumnModel().getColumn(0).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(1).setPreferredWidth(100);
        jtblDataBase.getColumnModel().getColumn(1).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(2).setPreferredWidth(180);
        jtblDataBase.getColumnModel().getColumn(2).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(3).setPreferredWidth(65);
        jtblDataBase.getColumnModel().getColumn(3).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);        
        jtblDataBase.getColumnModel().getColumn(4).setPreferredWidth(150);
        jtblDataBase.getColumnModel().getColumn(4).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(5).setPreferredWidth(65);
        jtblDataBase.getColumnModel().getColumn(5).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(6).setPreferredWidth(150);
        jtblDataBase.getColumnModel().getColumn(6).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(7).setPreferredWidth(65);
        jtblDataBase.getColumnModel().getColumn(7).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(8).setPreferredWidth(65);
        jtblDataBase.getColumnModel().getColumn(8).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(9).setPreferredWidth(140);
        jtblDataBase.getColumnModel().getColumn(9).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(10).setPreferredWidth(85);
        jtblDataBase.getColumnModel().getColumn(10).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(11).setPreferredWidth(85);
        jtblDataBase.getColumnModel().getColumn(11).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(12).setPreferredWidth(120);
        jtblDataBase.getColumnModel().getColumn(12).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
        jtblDataBase.getColumnModel().getColumn(13).setPreferredWidth(160);
        jtblDataBase.getColumnModel().getColumn(13).setResizable(false);
        jtblDataBase.getColumnModel().getColumn(13).setCellRenderer(centerRenderer);
        
        //Adding dropdown lists to columns
        TableColumn colDOM = jtblDataBase.getColumnModel().getColumn(10);
        JComboBox droplistDOM = new JComboBox();
        droplistDOM.addItem("YES");
        droplistDOM.addItem("NO");
        droplistDOM.addItem("NA");
        colDOM.setCellEditor(new DefaultCellEditor(droplistDOM));
        TableColumn colMOV = jtblDataBase.getColumnModel().getColumn(11);
        JComboBox droplistMOV = new JComboBox();
        droplistMOV.addItem("YES");
        droplistMOV.addItem("NO");
        droplistMOV.addItem("NA");
        colMOV.setCellEditor(new DefaultCellEditor(droplistMOV));
        
    }
    //</editor-fold>
    
    //Prepares the JTable columns in order to receive the list of Backorders from the Excel file
    private void configBackordersTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        tblModelBackorders.addColumn("BO status");
        tblModelBackorders.addColumn("BO Req Date");
        tblModelBackorders.addColumn("Service Req");
        tblModelBackorders.addColumn("Task number");
        tblModelBackorders.addColumn("Order Number");
        tblModelBackorders.addColumn("PN#");
        tblModelBackorders.addColumn("QTY");
        tblModelBackorders.addColumn("Description");
        tblModelBackorders.addColumn("Task status");
        tblModelBackorders.addColumn("PLC");
        tblModelBackorders.addColumn("Part Criticality");
        tblModelBackorders.addColumn("Part Condition");
        tblModelBackorders.addColumn("Good New Search Assumption");
        tblModelBackorders.addColumn("Alternatives");
        tblModelBackorders.addColumn("Comments");
        tblModelBackorders.addColumn("ISO 1");
        tblModelBackorders.addColumn("AWB 1");
        tblModelBackorders.addColumn("ISO 2");
        tblModelBackorders.addColumn("AWB 2");
        tblModelBackorders.addColumn("ISO 3");
        tblModelBackorders.addColumn("AWB 3");
        tblModelBackorders.addColumn("ISO (MI2 > BUE)");
        tblModelBackorders.addColumn("AWB (MI2 > BUE)");
        tblModelBackorders.addColumn("SIMI (DJAI)");
        tblModelBackorders.addColumn("GSI Task Notes");
        tblModelBackorders.addColumn("Back Order E-mail Title");
        tblModelBackorders.addColumn("Tracking #");
        jtblBackorders.setModel(tblModelBackorders);
        //Allows the user to sort the items ina PartsListColumn
        jtblBackorders.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblBackorders.getTableHeader();
        header.setBackground(Color.black);
        header.setForeground(Color.orange);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblBackorders.setAutoResizeMode(jtblBackorders.AUTO_RESIZE_OFF);
        jtblBackorders.setRowHeight(22);
        jtblBackorders.getColumnModel().getColumn(0).setPreferredWidth(160);
        jtblBackorders.getColumnModel().getColumn(0).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(1).setPreferredWidth(90);
        jtblBackorders.getColumnModel().getColumn(1).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(2).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(2).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(3).setPreferredWidth(100);
        jtblBackorders.getColumnModel().getColumn(3).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(4).setPreferredWidth(100);
        jtblBackorders.getColumnModel().getColumn(4).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);        
        jtblBackorders.getColumnModel().getColumn(5).setPreferredWidth(100);
        jtblBackorders.getColumnModel().getColumn(5).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(6).setPreferredWidth(60);
        jtblBackorders.getColumnModel().getColumn(6).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(7).setPreferredWidth(210);
        jtblBackorders.getColumnModel().getColumn(7).setResizable(false);
        //jtblBackorders.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(8).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(8).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(9).setPreferredWidth(60);
        jtblBackorders.getColumnModel().getColumn(9).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(10).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(10).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(11).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(11).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(12).setPreferredWidth(250);
        jtblBackorders.getColumnModel().getColumn(12).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(13).setPreferredWidth(100);
        jtblBackorders.getColumnModel().getColumn(13).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(13).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(14).setPreferredWidth(200);
        jtblBackorders.getColumnModel().getColumn(14).setResizable(false);
        //jtblBackorders.getColumnModel().getColumn(14).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(15).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(15).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(15).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(16).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(16).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(16).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(17).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(17).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(17).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(18).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(18).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(18).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(19).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(19).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(19).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(20).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(20).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(20).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(21).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(21).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(21).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(22).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(22).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(22).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(23).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(23).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(23).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(24).setPreferredWidth(300);
        jtblBackorders.getColumnModel().getColumn(24).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(24).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(25).setPreferredWidth(300);
        jtblBackorders.getColumnModel().getColumn(25).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(25).setCellRenderer(centerRenderer);
        jtblBackorders.getColumnModel().getColumn(26).setPreferredWidth(120);
        jtblBackorders.getColumnModel().getColumn(26).setResizable(false);
        jtblBackorders.getColumnModel().getColumn(26).setCellRenderer(centerRenderer);
        
        //Adding dropdown lists to columns
        TableColumn colBOS = jtblBackorders.getColumnModel().getColumn(0);
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
    
    //Prepares the JTable columns in order to receive the list of WebADI orders from the Excel file
    private void configWebADITable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        tblModelWebADI.addColumn("Creation Date");
        tblModelWebADI.addColumn("Item");
        tblModelWebADI.addColumn("QTY");
        tblModelWebADI.addColumn("From");
        tblModelWebADI.addColumn("Dest");
        tblModelWebADI.addColumn("Shipping Method");
        tblModelWebADI.addColumn("Ref");
        tblModelWebADI.addColumn("Order #");
        tblModelWebADI.addColumn("Airwaybill");
        tblModelWebADI.addColumn("Status");
        tblModelWebADI.addColumn("Activity");
        tblModelWebADI.addColumn("Task #");
        tblModelWebADI.addColumn("SIMI");
        tblModelWebADI.addColumn("CIBU");
        tblModelWebADI.addColumn("Comments");
        jtblWebADI.setModel(tblModelWebADI);
        //Allows the user to sort the items ina PartsListColumn
        jtblWebADI.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblWebADI.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.cyan);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblWebADI.setAutoResizeMode(jtblWebADI.AUTO_RESIZE_OFF);
        jtblWebADI.setRowHeight(22);
        jtblWebADI.getColumnModel().getColumn(0).setPreferredWidth(100);
        jtblWebADI.getColumnModel().getColumn(0).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(1).setPreferredWidth(130);
        jtblWebADI.getColumnModel().getColumn(1).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(2).setPreferredWidth(70);
        jtblWebADI.getColumnModel().getColumn(2).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(3).setPreferredWidth(75);
        jtblWebADI.getColumnModel().getColumn(3).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);        
        jtblWebADI.getColumnModel().getColumn(4).setPreferredWidth(75);
        jtblWebADI.getColumnModel().getColumn(4).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(5).setPreferredWidth(140);
        jtblWebADI.getColumnModel().getColumn(5).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(6).setPreferredWidth(100);
        jtblWebADI.getColumnModel().getColumn(6).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(7).setPreferredWidth(120);
        jtblWebADI.getColumnModel().getColumn(7).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(8).setPreferredWidth(120);
        jtblWebADI.getColumnModel().getColumn(8).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(9).setPreferredWidth(150);
        jtblWebADI.getColumnModel().getColumn(9).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(10).setPreferredWidth(120);
        jtblWebADI.getColumnModel().getColumn(10).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(11).setPreferredWidth(120);
        jtblWebADI.getColumnModel().getColumn(11).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(12).setPreferredWidth(120);
        jtblWebADI.getColumnModel().getColumn(12).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(13).setPreferredWidth(120);
        jtblWebADI.getColumnModel().getColumn(13).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(13).setCellRenderer(centerRenderer);
        jtblWebADI.getColumnModel().getColumn(14).setPreferredWidth(200);
        jtblWebADI.getColumnModel().getColumn(14).setResizable(false);
        jtblWebADI.getColumnModel().getColumn(14).setCellRenderer(centerRenderer);
    }
    //</editor-fold>
    

    //CLEANING TABLES

    //Cleans the parts JTable
    private void cleanPartsListTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = tblModelPartsList.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelPartsList.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the Table \n" + e, "AGENTINA PLANNING", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Cleans the Consults JTable
    private void cleanNewConsultsTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = this.tblModelConsultsList.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelConsultsList.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the Consults Table \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>

    //Cleans the Data Base JTable
    private void cleanConsultsDBTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = this.tblModelDataBase.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelDataBase.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the Data Base Table \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Cleans the Backorders JTable
    private void cleanBackordersTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = this.tblModelBackorders.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelBackorders.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the Backorders Table \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Cleans the WebADI JTable
    private void cleanWebADITable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = tblModelWebADI.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelWebADI.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the Table \n"
                + "Method: CleanWebADITable()\n" + e, "GN RIGHTHAND", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    

    
    
    /*  *****DATA MANAGEMENT RELATED METHODS***** */
    
    
    //Loads the information from the 2d-Matrix (Excel file) into de parts Parts/Orgs JTable
    private void loadPartsListTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        System.out.println("Loading data from String Matrix into screen JTable");
        locateColumns();
        int r;
        for ( r=1; r<xlsDataMatrix.length; r++ )
        {
            PartsListColumn[0] = xlsDataMatrix[r][iReg];
            PartsListColumn[1] = xlsDataMatrix[r][iCountry];
            PartsListColumn[2] = xlsDataMatrix[r][iOrgName];
            PartsListColumn[3] = xlsDataMatrix[r][iOrgCode];
            PartsListColumn[4] = xlsDataMatrix[r][iTier];
            PartsListColumn[5] = xlsDataMatrix[r][iPN];
            PartsListColumn[6] = xlsDataMatrix[r][iOHTot];
            PartsListColumn[7] = xlsDataMatrix[r][iEXTot];
            tblModelPartsList.addRow(PartsListColumn);
            jtblParts.setModel(tblModelPartsList);
        }
        System.out.println("Matrix loaded in the screen's JTable");
        jlblLineQTY.setText(String.valueOf(r));
    }
    //</editor-fold>
    
    //Loads the information stored in the imported 2D-Matrix (Excel file) into the WebADI Data Base ArrayList 
    //At the same time, it updates the Backorders DB with the ISO# on each ACTIVE Task line
    private void loadWebADIDBfromXLS(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        System.out.println("Updating data in WebADI ArrayList");
        cls_Date_Manager tmpDM = new cls_Date_Manager();
        String sDate="", sActivity = "", sTask = "", sSIMI="";
        //Starts loading the info from the eighth row 
        for ( int r=7; r<xlsWebADIMatrix.length; r++ ){
            System.out.println("Adding Object " + (r-6));
            //Checks the Shipping Method in order to load the Activity string value
            if ( xlsWebADIMatrix[r][iShipMeth].equals("FS Deferred Non Critical") ) {sActivity = "Replenishment";} else {sActivity = "Backorders";}
            //Checks if the file comes with a Task number. If it does, it loads it; otherwise, it puts "NA"
            if ( xlsWebADIMatrix[r][iADITsk].equals("") ){sTask = "NA";} else {sTask = xlsWebADIMatrix[r][iADITsk];}
            //Checks if the file comes with a SIMI number. If it does, it loads it; otherwise, it puts "NA"
            if ( xlsWebADIMatrix[r][iSIMI].equals("") ){sSIMI = "NA";} else {sSIMI = xlsWebADIMatrix[r][iSIMI];}
            //Reads the date and loads it in fomart "yyyy-mm-dd" (this is for Data Base sorting purposes)
            //sDate = xlsWebADIMatrix[r][iCreaDate];
            //JOptionPane.showMessageDialog(this, "XLS Date: " + sDate);
            sDate = tmpDM.reformatDatetoYYYYMMDD(xlsWebADIMatrix[r][iCreaDate],tmpDM.identifyCountryFormat(xlsWebADIMatrix[r][iCreaDate]));
            //JOptionPane.showMessageDialog(this, "Country: " + tmpDM.identifyCountryFormat(xlsWebADIMatrix[r][iCreaDate]));
            //JOptionPane.showMessageDialog(this, "Converted date: " + sDate);
            //Creates a new object in the WebADI Data Base arraylist 
            alWebadiDB.add(new cls_WebADI_Data(sDate, 
                    xlsWebADIMatrix[r][iItem],
                    xlsWebADIMatrix[r][iQTY], 
                    xlsWebADIMatrix[r][iSrc], 
                    xlsWebADIMatrix[r][iDes],
                    xlsWebADIMatrix[r][iShipMeth],
                    xlsWebADIMatrix[r][iSR],
                    xlsWebADIMatrix[r][iISO],
                    "NA" /*Airwaybill*/, 
                    "Picked" /*Initial Status*/,
                    sActivity,
                    sTask,
                    sSIMI,
                    "NA" /*CIBU*/,
                    "NA" /*Comments*/,
                    "NA" /*Postition*/,
                    "NA" /*Var XX1*/,
                    "NA" /*Var XX2*/));
            if ( !sTask.equals("NA") ){
                //UPDATES THE CORRESPONDING BACKORDER LINE DB ARRAYLIST WITH ISO AND SIMI
                setISOtoBackorderLine(xlsWebADIMatrix[r][iISO], sTask, sSIMI, xlsWebADIMatrix[r][iSrc], xlsWebADIMatrix[r][iDes]);
            }
        }
        
        System.out.println("WebADI Data Base updated");        
    }
    //</editor-fold>
    
    //Updates the Backorders DB with the ISO# and SIMI# from the WebADI DB loaded file
    private void setISOtoBackorderLine(String sISO, String sTsk, String sSIMI, String sSou, String sDes){
    //<editor-fold defaultstate="Collapsed" desc="Method Source Code">    
        sISO_SIMI_Report = sISO_SIMI_Report + "Task: " + sTsk + ". ";
        boolean bFlag = false;
        for ( cls_BO_Data tmp : alBckordDB ){
            if ( tmp.getTask().equals(sTsk) && !tmp.getBSta().equals("Closed") ){//If it finds the Task Line and it is not closed, then...
                bFlag = true;
                //FILLING THE ISO
                sISO_SIMI_Report = sISO_SIMI_Report + "Found.\n";
                if ( sSou.equals("MI2") && sDes.equals("BUE") ){//If this is the last hop, then...
                    sISO_SIMI_Report = sISO_SIMI_Report + "LST HOP. ";
                    if ( tmp.getIsMB().equals("NA") ){//If the last hop ISO is empty, then...
                        tmp.setIsMB(sISO);  //This would be the last hop ISO
                        sISO_SIMI_Report = sISO_SIMI_Report + "ISO: " + sISO + "\n";
                    }
                    else{//If last hop ISO it is not empty...
                        sISO_SIMI_Report = sISO_SIMI_Report + "Already filled. Please check." + "\n";
                    }
                }//If this is not the last hop...
                else{//Fills the ISO# in the first available blank between ISO1, ISO2 and ISO3
                    sISO_SIMI_Report = sISO_SIMI_Report + "STD HOP. ";
                    if ( tmp.getISO1().equals("NA") ){
                        tmp.setISO1(sISO);
                        sISO_SIMI_Report = sISO_SIMI_Report + "ISO1: " + sISO + "\n";
                    }
                    else{
                        if ( tmp.getISO2().equals("NA") ){
                            tmp.setISO2(sISO);
                            sISO_SIMI_Report = sISO_SIMI_Report + "ISO2: " + sISO + "\n";
                        }
                        else{
                            if ( tmp.getISO3().equals("NA") ){
                                tmp.setISO3(sISO);
                                sISO_SIMI_Report = sISO_SIMI_Report + "ISO3: " + sISO + "\n";
                            }
                            else{
                                sISO_SIMI_Report = sISO_SIMI_Report + "All ISOs already filled. Please check." + "\n";
                            }
                        }
                    }
                }
                //FILLING THE SIMI
                if ( tmp.getSIMI().equals("NA") ){//If the SIMI blank is empty, then...
                    tmp.setSIMI(sSIMI);
                    sISO_SIMI_Report = sISO_SIMI_Report + "SIMI: " + sSIMI + "\n";
                }
                else{
                    sISO_SIMI_Report = sISO_SIMI_Report + "SIMI: Already filled. Please check." + "\n";
                }
            }            
        }
        if ( bFlag == false ){sISO_SIMI_Report = sISO_SIMI_Report + "Not found or closed\n";}
        sISO_SIMI_Report = sISO_SIMI_Report + "\n";
    }
    //</editor-fold>
    
    
    //LOADING DB ARRAYLISTS FROM LOCAL .TXT FILES
    
    //Loads the Consults Data Base from a local .txt file into the Main data Base ArrayList
    private void loadConsulDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        this.alCosulDB.clear();
        this.iCoQTY = 0;
        this.iMaQTY = 0;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sDOM="", sPrtMvd="", sTsk="", sTracking="";
        
        try
        {
            fDataBase = new File(sLocCoDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("CREATED CONSULTS") )
            {
                String [] position = chain.split("\t");
                sTir = position[0];
                sReg = position[1];
                sCnt = position[2];
                sOrg = position[3];        
                sPrt = position[4];
                sQty = position[5];
                sAct = position[6];
                sGOH = position[7];
                sGXS = position[8];
                sDat = position[9];
                sDOM = position[10];
                sPrtMvd = position[11];
                sTsk = position[12];
                sTracking = position[13];
                alCosulDB.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sPrtMvd, sTsk, sTracking, "NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iCoQTY = Integer.valueOf(chain);
            chain = br.readLine();
            chain = br.readLine();
            iMaQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
            alCosulDB = updateArrayListNames(alCosulDB);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n"
                    + "Method: loadConsultDB()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Loads the Backorders Data Base from a local .txt file into the Backorders data base ArrayList
    private void loadBckordDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        this.alBckordDB.clear();
        this.iBoQTY = 0;
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
            fDataBase = new File(sLocBoDBPath);
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
                alBckordDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA","NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iBoQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders local Data Base\n"
                    + "Method: loadBackordersDB()\n" + e, "DB RIGHTHAND", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
        
    //Loads the Ticket Data Base from a local .txt file into the WebADI data base ArrayList
    private void loadWebadiDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        this.alWebadiDB.clear();
        this.iWaQTY = 0;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sDat="", sItm="", sQty="", sFrm="", sDst="", sShpMet="", sRef="", sISO="", sAwb="", sSta="", sAct="", sTsk="", sSMI="", sCIB="", sCom="";
        try
        {
            fDataBase = new File(sLocWaDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("WEBADI LINES") )
            {
                String [] position = chain.split("\t");
                sDat = position[0];
                sItm = position[1];
                sQty = position[2];
                sFrm = position[3];        
                sDst = position[4];
                sShpMet = position[5];
                sRef = position[6];
                sISO = position[7];
                sAwb = position[8];
                sSta = position[9];
                sAct = position[10];
                sTsk = position[11];
                sSMI = position[12];
                sCIB = position[13];
                sCom = position[14];
                alWebadiDB.add(new cls_WebADI_Data(sDat, sItm, sQty, sFrm, sDst, sShpMet, sRef, sISO, sAwb, sSta, sAct, sTsk, sSMI, sCIB, sCom, "NA", "NA", "NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iWaQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the WebADI local Data Base\n"
                    + "Method: loadWebADIDB()\n" + e, "DB RIGHTHAND", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Loads the Consults Data Base from a local .txt file into a temporary ArrayList
    private ArrayList<cls_PartDataReq> loadTMPlocConsultsDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_PartDataReq> tmpLocCons = new ArrayList<>();
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sDOM="", sPrtMvd="", sTsk="", sTracking="";
        try
        {
            fDataBase = new File(sLocCoDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("CREATED CONSULTS") )
            {
                String [] position = chain.split("\t");
                sTir = position[0];
                sReg = position[1];
                sCnt = position[2];
                sOrg = position[3];        
                sPrt = position[4];
                sQty = position[5];
                sAct = position[6];
                sGOH = position[7];
                sGXS = position[8];
                sDat = position[9];
                sDOM = position[10];
                sPrtMvd = position[11];
                sTsk = position[12];
                sTracking = position[13];
                tmpLocCons.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sPrtMvd, sTsk, sTracking, "NA"));
                chain = br.readLine();
            }
            br.close();
            fr.close();
            tmpLocCons = updateArrayListNames(tmpLocCons);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n"
                    + "Method: loadTMPlocConsultsDB\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
        return tmpLocCons;
    }
    //</editor-fold>
    
    //Loads the Backorders Data Base from a local .txt file into a temporary ArrayList
    private ArrayList<cls_BO_Data> loadTMPlocBackordersDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_BO_Data> tmpLocBOs = new ArrayList<>();
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
            fDataBase = new File(sLocBoDBPath);
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
                tmpLocBOs.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA","NA"));
                chain = br.readLine();
            }
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders local Data Base\n"
                    + "Method: loadTMPlocBackordersDB()\n" + e, "DB RIGHTHAND", JOptionPane.ERROR_MESSAGE );
        }
        return tmpLocBOs;
    }
    //</editor-fold>
    
    //Loads the Ticket Data Base from a local .txt file into a temporary ArrayList
    private ArrayList<cls_WebADI_Data> loadTMPlocWebADIDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_WebADI_Data> tmpLocWAs = new ArrayList<>();
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sDat="", sItm="", sQty="", sFrm="", sDst="", sShpMet="", sRef="", sISO="", sAwb="", sSta="", sAct="", sTsk="", sSMI="", sCIB="", sCom="";
        try
        {
            fDataBase = new File(sLocWaDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("WEBADI LINES") )
            {
                String [] position = chain.split("\t");
                sDat = position[0];
                sItm = position[1];
                sQty = position[2];
                sFrm = position[3];        
                sDst = position[4];
                sShpMet = position[5];
                sRef = position[6];
                sISO = position[7];
                sAwb = position[8];
                sSta = position[9];
                sAct = position[10];
                sTsk = position[11];
                sSMI = position[12];
                sCIB = position[13];
                sCom = position[14];
                tmpLocWAs.add(new cls_WebADI_Data(sDat, sItm, sQty, sFrm, sDst, sShpMet, sRef, sISO, sAwb, sSta, sAct, sTsk, sSMI, sCIB, sCom, "NA", "NA", "NA"));
                chain = br.readLine();
            }
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the WebADI local Data Base\n"
                    + "Method: loadTMPlocWebADIDB()\n" + e, "DB RIGHTHAND", JOptionPane.ERROR_MESSAGE );
        }
        return tmpLocWAs;
    }
    //</editor-fold>
    
    
    //LOADING & UPLOADS DB FROM REMOTE .TXT FILES
    
    //Loads the Consults Data Base from the Beehive .txt Backup file into the active ArrayList 
    private void loadRemConsulDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Clears the current ArrayList for the Data Base
        alCosulDB.clear();
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain, sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sDOM="", sPrtMvd="", sTsk="", sTracking="";
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemCoDBPath);
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
                System.out.println("Remote connection established to Consults Data Base.\nProceeding to download.");
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("CREATED CONSULTS") ){
                    String [] position = chain.split("\t");
                    sTir = position[0];
                    sReg = position[1];
                    sCnt = position[2];
                    sOrg = position[3];        
                    sPrt = position[4];
                    sQty = position[5];
                    sAct = position[6];
                    sGOH = position[7];
                    sGXS = position[8];
                    sDat = position[9];
                    sDOM = position[10];
                    sPrtMvd = position[11];
                    sTsk = position[12];
                    sTracking = position[13];
                    alCosulDB.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sPrtMvd, sTsk, sTracking, "NA"));
                    chain = br.readLine();
                }
                chain = br.readLine();
                iCoQTY = Integer.valueOf(chain);
                chain = br.readLine();
                chain = br.readLine();
                iMaQTY = Integer.valueOf(chain);
                System.out.println("Remote Consults Data Base downloaded.\nClosing threads.");
            }
            br.close();
            isr.close();
            alCosulDB = updateArrayListNames(alCosulDB);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Consults Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads the Backorders Data Base from the Beehive .txt Backup file into the active ArrayList 
    private void loadRemBckordDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Clears the current ArrayList for the Data Base
        alBckordDB.clear();
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
            URL url = new URL(sRemBoDBPath);
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
                System.out.println("Remote connection established to Backorders Data Base.\nProceeding to download.");
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
                    alBckordDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA","NA"));
                    chain = br.readLine();
                }
                chain = br.readLine();
                iBoQTY = Integer.valueOf(chain);
                System.out.println("Remote Backorders Data Base downloaded.\nClosing threads.");
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR - loadRemBackordersDB()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads the WebADI Data Base from the Beehive .txt Backup file into the active ArrayList 
    private void loadRemWebadiDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Clears the current ArrayList for the Data Base
        alWebadiDB.clear();
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain, sDat="", sItm="", sQty="", sFrm="", sDst="", sShpMet="", sRef="", sISO="", sAwb="", sSta="", sAct="", sTsk="", sSMI="", sCIB="", sCom="";
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemWaDBPath);
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
                System.out.println("Remote connection established to WebADI Data Base.\nProceeding to download.");
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("WEBADI LINES") ){
                    String [] position = chain.split("\t");
                    sDat = position[0];
                    sItm = position[1];
                    sQty = position[2];
                    sFrm = position[3];        
                    sDst = position[4];
                    sShpMet = position[5];
                    sRef = position[6];
                    sISO = position[7];
                    sAwb = position[8];
                    sSta = position[9];
                    sAct = position[10];
                    sTsk = position[11];
                    sSMI = position[12];
                    sCIB = position[13];
                    sCom = position[14];
                    alWebadiDB.add(new cls_WebADI_Data(sDat, sItm, sQty, sFrm, sDst, sShpMet, sRef, sISO, sAwb, sSta, sAct, sTsk, sSMI, sCIB, sCom, "NA", "NA", "NA"));
                    chain = br.readLine();
                }
                chain = br.readLine();
                iWaQTY = Integer.valueOf(chain);
                System.out.println("Remote WebADI Data Base downloaded.\nClosing threads.");
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote WebADI Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads the Consults Data Base from the Beehive .txt Backup file into a temporary ArrayList 
    private ArrayList<cls_PartDataReq> loadTMPRemConsDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Clears the current ArrayList for the Data Base
        ArrayList<cls_PartDataReq> tmpRemCons = new ArrayList<>();
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain, sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sDOM="", sPrtMvd="", sTsk="", sTracking="";
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemCoDBPath);
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
                System.out.println("Remote connection established to Consults Data Base.\nProceeding to download.");
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("CREATED CONSULTS") ){
                    String [] position = chain.split("\t");
                    sTir = position[0];
                    sReg = position[1];
                    sCnt = position[2];
                    sOrg = position[3];        
                    sPrt = position[4];
                    sQty = position[5];
                    sAct = position[6];
                    sGOH = position[7];
                    sGXS = position[8];
                    sDat = position[9];
                    sDOM = position[10];
                    sPrtMvd = position[11];
                    sTsk = position[12];
                    sTracking = position[13];
                    tmpRemCons.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sPrtMvd, sTsk, sTracking, "NA"));
                    chain = br.readLine();
                }
            }
            br.close();
            isr.close();
            tmpRemCons = updateArrayListNames(tmpRemCons);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Consults Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team\n" +
                    "Method: loadTMPRemConsDB()","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return tmpRemCons;
    }
    //</editor-fold>
    
    //Loads the Backorders Data Base from the Beehive .txt Backup file into a temporary ArrayList 
    private ArrayList<cls_BO_Data> loadTMPRemBackordersDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_BO_Data> tmpRemBOs = new ArrayList<>();
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
            URL url = new URL(sRemBoDBPath);
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
                System.out.println("Remote connection established to Backorders Data Base.\nProceeding to download.");
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
                    tmpRemBOs.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA","NA"));
                    chain = br.readLine();
                }
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR - loadTMPRemBackordersDB()",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return tmpRemBOs;
    }
    //</editor-fold>
    
    //Loads the WebADI Data Base from the Beehive .txt Backup file into a temporary ArrayList 
    private ArrayList<cls_WebADI_Data> loadTMPRemWebADIDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_WebADI_Data> tmpRemWAs = new ArrayList<>();
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain, sDat="", sItm="", sQty="", sFrm="", sDst="", sShpMet="", sRef="", sISO="", sAwb="", sSta="", sAct="", sTsk="", sSMI="", sCIB="", sCom="";
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemWaDBPath);
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
                System.out.println("Remote connection established to WebADI Data Base.\nProceeding to download.");
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the ArrayLIst with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("WEBADI LINES") ){
                    String [] position = chain.split("\t");
                    sDat = position[0];
                    sItm = position[1];
                    sQty = position[2];
                    sFrm = position[3];        
                    sDst = position[4];
                    sShpMet = position[5];
                    sRef = position[6];
                    sISO = position[7];
                    sAwb = position[8];
                    sSta = position[9];
                    sAct = position[10];
                    sTsk = position[11];
                    sSMI = position[12];
                    sCIB = position[13];
                    sCom = position[14];
                    tmpRemWAs.add(new cls_WebADI_Data(sDat, sItm, sQty, sFrm, sDst, sShpMet, sRef, sISO, sAwb, sSta, sAct, sTsk, sSMI, sCIB, sCom, "NA", "NA", "NA"));
                    chain = br.readLine();
                }
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote WebADI Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" + 
                    "Method: loadTMPRemWebADIDB()","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return tmpRemWAs;
    }
    //</editor-fold>
    
    
    
    //Saves the Consults Data Base from the active ArrayList into the Beehive .txt remote file
    private void uploadRemConsDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        URLConnection urlConn = null;
        OutputStreamWriter osw = null;
        try
        {
            System.out.println("Opening URL connection to Consults Data Base");
            //Opens the URL connection
            URL url = new URL(sRemCoDBPath);
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
                System.out.println("The URL connection to the Consults Data Base is up");
                System.out.println("The output stream buffer is available");
                System.out.println("Starting to upload Consults lines");
                osw = new OutputStreamWriter(urlConn.getOutputStream(),Charset.defaultCharset());
                for ( cls_PartDataReq tmp : alCosulDB ){
                    osw.write(tmp.getTier() + "\t"
                            + tmp.getRegion() + "\t"
                            + tmp.getCountryName() + "\t"
                            + tmp.getOrgCode() + "\t"
                            + tmp.getPartNumber() + "\t"
                            + tmp.getQTY() + "\t"
                            + tmp.getActivity() + "\t"
                            + tmp.getTotalOH() + "\t"
                            + tmp.getTotalXS() + "\t"
                            + tmp.getCurrentDate() + "\t"
                            + tmp.getDOM() + "\t"
                            + tmp.getPartMoved() + "\t"
                            + tmp.getTask() + "\t"
                            + tmp.getTracking() + "\n");
                }
                osw.write("CREATED CONSULTS\n");
                osw.write(String.valueOf(alCosulDB.size()) + "\n");
                osw.write("CREATED MAILS\n");
                osw.write(String.valueOf(iMaQTY) + "\n");
                osw.write(this.sUser + "\n");//It writes the e-mail of the last User who logged into the DB
            }
            osw.flush();
            urlConn.getContentLengthLong();
            System.out.println("The remote Consults Data Base has been updated.\nClosing output stream.");
        }
        catch (Exception e)
        {
//            JOptionPane.showMessageDialog(this,"Exception while writing into the remote Data Base\n" +
//                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
//                    "If the issue persists please contact the CR Spares Planning Team\n" + e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Saves the Backorders Data Base from the active ArrayList into the Beehive .txt remote file
    private void uploadRemBackordersDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        URLConnection urlConn = null;
        OutputStreamWriter osw = null;
        try
        {
            System.out.println("Opening URL connection to the Backorders Data Base");
            //Opens the URL connection
            URL url = new URL(sRemBoDBPath);
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
                for(cls_BO_Data tmp: this.alBckordDB){
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
                iBoQTY = alBckordDB.size();
                osw.write("BO LINES\n");
                osw.write(String.valueOf(iBoQTY) + "\n");
                jlblBODBsize.setText("<html>Data Base size:<br>" + iBoQTY + " lines</html>");
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
    
    //Saves the WebADI Data Base from the active ArrayList into the Beehive .txt remote file
    private void uploadRemWebADIDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        URLConnection urlConn = null;
        OutputStreamWriter osw = null;
        try
        {
            System.out.println("Opening URL connection to the WebADI Data Base");
            //Opens the URL connection
            URL url = new URL(sRemWaDBPath);
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
                System.out.println("The URL connection to the WebADI Data Base is up");
                System.out.println("The output stream buffer is available");
                System.out.println("Starting to upload WebADI lines");
                osw = new OutputStreamWriter(urlConn.getOutputStream(),Charset.defaultCharset());
                for(cls_WebADI_Data tmp: this.alWebadiDB){
                    osw.write(tmp.getDat() + "\t" 
                        + tmp.getItm() + "\t" 
                        + tmp.getQTY() + "\t" 
                        + tmp.getFrm() + "\t" 
                        + tmp.getDst() + "\t" 
                        + tmp.getShpMet() + "\t" 
                        + tmp.getRef() + "\t" 
                        + tmp.getISO() + "\t" 
                        + tmp.getAwb() + "\t" 
                        + tmp.getSta() + "\t" 
                        + tmp.getAct() + "\t" 
                        + tmp.getTsk() + "\t"
                        + tmp.getSMI() + "\t"
                        + tmp.getCIB() + "\t"
                        + tmp.getCom() + "\n");
                }
                iWaQTY = alWebadiDB.size();
                osw.write("WEBADI LINES\n");
                osw.write(String.valueOf(iWaQTY) + "\n");
                jlblWADBsize.setText("<html>Data Base size:<br>" + iWaQTY + " lines</html>");
            }
            osw.flush();
            urlConn.getContentLengthLong();
            System.out.println("The remote WebADI Data Base has been updated.\nClosing output stream.");
        }
        catch (Exception e)
        {
//            JOptionPane.showMessageDialog(this,"Exception while writing into the remote WebADI Data Base\n" +
//                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
//                    "If the issue persists please contact the CR Spares Planning Team\n" + e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    
    //LOADING JTABLES FROM ARRAYLISTS
    
    //Loads the information from the New Consults ArrayList of current consults into de Consults JTable
    private void loadNewConsultsTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Loading data from ArrayList into screen Consults JTable");
        for ( cls_PartDataReq tmp: alGNSearchList ){
            ConsultsColumn[0] = tmp.getTier();
            ConsultsColumn[1] = tmp.getRegion();
            ConsultsColumn[2] = tmp.getCountryName();
            ConsultsColumn[3] = tmp.getOrgCode();
            ConsultsColumn[4] = tmp.getPartNumber();
            ConsultsColumn[5] = tmp.getQTY();
            ConsultsColumn[6] = tmp.getActivity();
            ConsultsColumn[7] = tmp.getTotalOH();
            ConsultsColumn[8] = tmp.getTotalXS();
            ConsultsColumn[9] = tmp.getTask();
            ConsultsColumn[10] = tmp.getCurrentDate();
            int iPos = findOldConsultPos(new cls_PartDataReq(tmp.getTier(), tmp.getRegion(), tmp.getCountryName(), tmp.getOrgCode(), 
                    tmp.getPartNumber(), tmp.getQTY(), tmp.getActivity(), tmp.getTotalOH(), tmp.getTotalXS(), tmp.getCurrentDate(), 
                    tmp.getDOM(), tmp.getPartMoved(), tmp.getTracking(), tmp.getTask(), tmp.getPosition()));
            if ( iPos  == -1 ) {
                ConsultsColumn[11] = "NA";
            }
            else {
                ConsultsColumn[11] = alCosulDB.get(iPos).getCurrentDate();
            }
            ConsultsColumn[12] = countPreviousConsults(new cls_PartDataReq(tmp.getTier(), tmp.getRegion(), tmp.getCountryName(), tmp.getOrgCode(), 
                    tmp.getPartNumber(), tmp.getQTY(), tmp.getActivity(), tmp.getTotalOH(), tmp.getTotalXS(), tmp.getCurrentDate(), 
                    tmp.getDOM(), tmp.getPartMoved(), tmp.getTracking(), tmp.getTask(), tmp.getPosition()));
            tblModelConsultsList.addRow(ConsultsColumn);
            jtblConsults.setModel(this.tblModelConsultsList);
        }
        System.out.println("ArrayList loaded in the Consults's JTable");
    }
    //</editor-fold>
    
    //Loads the information from the Consults DB ArrayList of current consults into de Consults DB JTable
    private void loadConsultsDBTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Loading data from Consults ArrayList into screen Data Base JTable");
        String sMode = "";
        if ( this.bONLINE == true ){sMode = "REMOTE";}
        else{sMode = "LOCAL";}
        for ( cls_PartDataReq tmp: this.alCosulDB ){
            DataBaseColumn[0] = tmp.getTier();
            DataBaseColumn[1] = tmp.getRegion();
            DataBaseColumn[2] = tmp.getCountryName();
            DataBaseColumn[3] = tmp.getOrgCode();
            DataBaseColumn[4] = tmp.getPartNumber();
            DataBaseColumn[5] = tmp.getQTY();
            DataBaseColumn[6] = tmp.getActivity();
            DataBaseColumn[7] = tmp.getTotalOH();
            DataBaseColumn[8] = tmp.getTotalXS();
            DataBaseColumn[9] = tmp.getCurrentDate();
            DataBaseColumn[10] = tmp.getDOM();
            DataBaseColumn[11] = tmp.getPartMoved();
            DataBaseColumn[12] = tmp.getTask();
            DataBaseColumn[13] = tmp.getTracking();
            tblModelDataBase.addRow(DataBaseColumn);
            jtblDataBase.setModel(this.tblModelDataBase);
        }
        this.bDBFLAG = true;
        this.jlblDBFlag.setText("<html><font color='green'>" + sMode + " DATA BASE TABLE</font></html>");
        System.out.println("ArrayList loaded in the Consults Data Base's JTable");
    }
    //</editor-fold>
    
    //Loads the information from the Backorders Data Base ArrayList into the corresponding JTable
    private void loadBackordersTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Loading data from Backorders ArralyList into screen JTable");
        String sMode = "";
        if ( this.bONLINE == true ){sMode = "Rem.";}
        else{sMode = "Loc.";}
        for ( cls_BO_Data tmp: this.alBckordDB )
        {
            BOColumn[0] = tmp.getBSta();
            BOColumn[1] = tmp.getDate();
            BOColumn[2] = tmp.getSvRq();
            BOColumn[3] = tmp.getTask();
            BOColumn[4] = tmp.getISO();
            BOColumn[5] = tmp.getItem();
            BOColumn[6] = tmp.getQty();
            BOColumn[7] = tmp.getDesc();
            BOColumn[8] = tmp.getTkSt();
            BOColumn[9] = tmp.getPLC();
            BOColumn[10] = tmp.getCrit();
            BOColumn[11] = tmp.getCond();
            BOColumn[12] = tmp.getSrAs();
            BOColumn[13] = tmp.getAlts();
            BOColumn[14] = tmp.getComm();
            BOColumn[15] = tmp.getISO1();
            BOColumn[16] = tmp.getAwb1();
            BOColumn[17] = tmp.getISO2();
            BOColumn[18] = tmp.getAwb2();
            BOColumn[19] = tmp.getISO3();
            BOColumn[20] = tmp.getAwb3();
            BOColumn[21] = tmp.getIsMB();
            BOColumn[22] = tmp.getAwMB();
            BOColumn[23] = tmp.getSIMI();
            BOColumn[24] = tmp.getTkNt();
            BOColumn[25] = tmp.getBOMT();
            BOColumn[26] = tmp.getTrak();
            tblModelBackorders.addRow(BOColumn);
            jtblBackorders.setModel(tblModelBackorders);
        }
        this.bBOFLAG = true;
        this.jlblBOFlag.setText("<html>Showing: <font color='green'>BO "+ sMode + " Data Base</font></html>");
        System.out.println("Backorders ArrayList loaded in the Backorders Data Base's JTable");
    }
    //</editor-fold>
    
    //Loads the information from the WebADI Data Base ArrayList into the corresponding JTable
    private void loadWebADITable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Loading data from WebADI ArralyList into screen JTable");
        String sMode = "";
        if ( this.bONLINE == true ){sMode = "Rem.";}
        else{sMode = "Loc.";}
        for ( cls_WebADI_Data tmp: this.alWebadiDB )
        {
            WebADIColumn[0] = tmp.getDat();
            WebADIColumn[1] = tmp.getItm();
            WebADIColumn[2] = tmp.getQTY();
            WebADIColumn[3] = tmp.getFrm();
            WebADIColumn[4] = tmp.getDst();
            WebADIColumn[5] = tmp.getShpMet();
            WebADIColumn[6] = tmp.getRef();
            WebADIColumn[7] = tmp.getISO();
            WebADIColumn[8] = tmp.getAwb();
            WebADIColumn[9] = tmp.getSta();
            WebADIColumn[10] = tmp.getAct();
            WebADIColumn[11] = tmp.getTsk();
            WebADIColumn[12] = tmp.getSMI();
            WebADIColumn[13] = tmp.getCIB();
            WebADIColumn[14] = tmp.getCom();
            tblModelWebADI.addRow(WebADIColumn);
            jtblWebADI.setModel(tblModelWebADI);
        }
        this.bWAFLAG = true;
        this.jlblWAFlag.setText("<html>Showing: <font color='green'>WebADI " + sMode + " Data Base</font></html>");
        System.out.println("WebADI ArrayList loaded in the WebADI Data Base's JTable");
    }
    //</editor-fold>
    
    
    
    //LOADING SCREEN COUNTERS
    
    //Updates the counters in the main screen after loading the Excel file
    private void updateMainCounters(String[][] dataMatrix){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        cls_Data_Manager tmpDM = new cls_Data_Manager();
        jlblRegsQTY.setText(String.valueOf(tmpDM.countDifferentsBD(tmpDM.getDifferentsBD(dataMatrix, this.iReg))));
        jlblCntrsQTY.setText(String.valueOf(tmpDM.countDifferentsBD(tmpDM.getDifferentsBD(dataMatrix, iCountry))));
        jlblOrgsQTY.setText(String.valueOf(tmpDM.countDifferentsBD(tmpDM.getDifferentsBD(dataMatrix, iOrgCode))));
        jlblPrtsQTY.setText(String.valueOf(tmpDM.countDifferentsBD(tmpDM.getDifferentsBD(dataMatrix, iPN))));
    }
    //</editor-fold>
    
    //LOADING OFFLINE COUNTERS
    
    //Loads and shows the consults and notifications historical QTYs in the Data Base main screen from the .TXT Data Base
    private void loadConsultsQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sConsQTY, sMailsQTY;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocCoDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("CREATED CONSULTS") )
            {
                chain = br.readLine();
            }
            sConsQTY = br.readLine();
            br.readLine();
            sMailsQTY = br.readLine();
            br.close();
            fr.close();
            jlblTickets.setText(sConsQTY);
            jlblMails.setText(sMailsQTY);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Loads and shows the Backorders history in the Data Base main screen from the .TXT Data Base
    private void loadBackordersQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sBOQTY;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocBoDBPath);
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
            this.jlblBODBsize.setText("<html>Data Base size:<br>" + sBOQTY + " lines</html>");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders local Data Base \n"
                    + "Method: loadBOQTYHist()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    //Loads and shows the consults and notifications history in the Data Base main screen from the .TXT Data Base
    private void loadWebADIQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sWAQTY;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocWaDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("WEBADI LINES") )
            {
                chain = br.readLine();
            }
            sWAQTY = br.readLine();
            br.close();
            fr.close();
            this.jlblWADBsize.setText("<html>Data Base size:<br>" + sWAQTY + " lines</html>");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the WebADI local Data Base \n"
                    + "Method: loadWebADIQTYHist()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    
    //Loads the consults QTYs from the .TXT Data Base
    private String getConsultsQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sConsQTY="", sMailsQTY;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocCoDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("CREATED CONSULTS") )
            {
                chain = br.readLine();
            }
            sConsQTY = br.readLine();
            br.readLine();
            sMailsQTY = br.readLine();
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
        return sConsQTY;
    }
    //</editor-fold>
    
    //Loads the Backorders history from the .TXT Data Base
    private String getBackordersQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sBOQTY="";
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocBoDBPath);
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
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Backorders local Data Base \n"
                    + "Method: loadBOQTYHist()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
        return sBOQTY;
    }
    //</editor-fold>
    
    //Loads WebADI history from the .TXT Data Base
    private String getWebADIQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sWAQTY="";
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocWaDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("WEBADI LINES") )
            {
                chain = br.readLine();
            }
            sWAQTY = br.readLine();
            br.close();
            fr.close();
            this.jlblWADBsize.setText("<html>Data Base size:<br>" + sWAQTY + " lines</html>");        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the WebADI local Data Base \n"
                    + "Method: loadWebADIQTYHist()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
        return sWAQTY;
    }
    //</editor-fold>
    
    
    
    
    //LOADING ONLINE COUNTERS
    
    //Loads and shows the consults and notifications historical QTYs in the Data Base main screen from the Beehive remote .TXT Consults Data Base 
    private void loadRemConsQTYHist(){
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
            URL url = new URL(sRemCoDBPath);
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
                while( !chain.equals("CREATED CONSULTS") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                iCoQTY = Integer.valueOf(chain);
                chain = br.readLine();
                chain = br.readLine();
                iMaQTY = Integer.valueOf(chain);
            }
            br.close();
            isr.close();
            jlblTickets.setText(String.valueOf(iCoQTY));
            jlblMails.setText(String.valueOf(iMaQTY));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Consults Data Base QTYs\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads and shows the historical QTYs in the Data Base main screen from the Beehive remote .TXT Backorders Data Base
    private void loadRemBackordersQTYHist(){
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
            URL url = new URL(sRemBoDBPath);
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
                iBoQTY = Integer.valueOf(chain);
            }
            br.close();
            isr.close();
            jlblBODBsize.setText("<html>Data Base size:<br>" + iBoQTY + " lines</html>");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Backorders Data Base QTY\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads and shows the hisgtorical QTYs in the Data Base main screen from the Beehive remote .TXT WebADI Data Base
    private void loadRemWebADIQTYHist(){
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
            URL url = new URL(sRemWaDBPath);
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
                while( !chain.equals("WEBADI LINES") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                iWaQTY = Integer.valueOf(chain);
            }
            br.close();
            isr.close();
            jlblWADBsize.setText("<html>Data Base size:<br>" + iWaQTY + " lines</html>");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote WebADI Data Base QTY\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>
    
    //Loads the consults and notifications historical QTYsfrom the Beehive remote .TXT Consults Data Base 
    private String getRemConsQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String sConsQTY="";
        //Prepares the necessary variables to fill the ArrayList
        String chain;
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemCoDBPath);
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
                while( !chain.equals("CREATED CONSULTS") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                sConsQTY = chain;
                chain = br.readLine();
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Consults Data Base QTYs\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return sConsQTY;
    }
    //</editor-fold>
    
    //Loads the Backorders QTYs from the Beehive remote .TXT Backorders Data Base
    private String getRemBackordersQTYHist(){
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
            URL url = new URL(sRemBoDBPath);
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
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return sBOQTY;
    }
    //</editor-fold>
    
    //Loads the WebADI QTYs from the Beehive remote .TXT WebADI Data Base
    private String getRemWebADIQTYHist(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        String sWAQTY="";
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain;
        try
        {
            //Opens the URL connection
            URL url = new URL(sRemWaDBPath);
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
                while( !chain.equals("WEBADI LINES") ){
                    chain = br.readLine();
                }
                chain = br.readLine();
                sWAQTY = chain;
            }
            br.close();
            isr.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote WebADI Data Base QTY\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return sWAQTY;
    }
    //</editor-fold>
    
    
    //UPDATING DB ARRAYLISTS ACCORDING WITH THE CHANGES ON THE CORRESPONDING JTABLES
    
    //Updates the local Consults Data Base ArrayList according with the changes on the screen Jtable
    private void updateConsultsALDataBase() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        if ( bDBFLAG == true ) {
            alCosulDB.clear();
            String sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sTsk="", sDOM="", sMov="", sTrk="";
            for ( int i=0; i < this.jtblDataBase.getRowCount(); i++ )
            {
                sTir = jtblDataBase.getValueAt(i, 0).toString();
                sReg = jtblDataBase.getValueAt(i, 1).toString();
                sCnt = jtblDataBase.getValueAt(i, 2).toString();
                sOrg = jtblDataBase.getValueAt(i, 3).toString();
                sPrt = jtblDataBase.getValueAt(i, 4).toString();
                sQty = jtblDataBase.getValueAt(i, 5).toString();
                sAct = jtblDataBase.getValueAt(i, 6).toString();
                sGOH = jtblDataBase.getValueAt(i, 7).toString();
                sGXS = jtblDataBase.getValueAt(i, 8).toString();
                sDat = jtblDataBase.getValueAt(i, 9).toString();
                sDOM = jtblDataBase.getValueAt(i, 10).toString();
                sMov = jtblDataBase.getValueAt(i, 11).toString();
                sTsk = jtblDataBase.getValueAt(i, 12).toString();
                sTrk = jtblDataBase.getValueAt(i, 13).toString();
                alCosulDB.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sMov, sTsk, sTrk, "NA"));
            }
        }
        else {
            //Updates the search results ArrayList with the results on the screen
            updateConsultsSearchResults();
            
            for ( cls_PartDataReq tmp: this.alConsulSearchResults ) {
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setTier(tmp.getTier());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setRegion(tmp.getRegion());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setCountryName(tmp.getCountryName());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setOrgCode(tmp.getOrgCode());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setPartNumber(tmp.getPartNumber());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setQTY(tmp.getQTY());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setActivity(tmp.getActivity());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setTotalOH(tmp.getTotalOH());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setTotalXS(tmp.getTotalXS());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setCurrentDate(tmp.getCurrentDate());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setDOM(tmp.getDOM());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setPartMoved(tmp.getPartMoved());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setTask(tmp.getTask());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setTracking(tmp.getTracking());
                alCosulDB.get(Integer.valueOf(tmp.getPosition())).setPosition("NA");
            }
        }
        JOptionPane.showMessageDialog(this, "The Data Base has been updated");
        cleanConsultsDBTable();
        loadConsultsDBTable();
    }
    //</editor-fold>
    
    //Updates the local Backorders Data Base ArrayList according with the changes on the screen Jtable
    private void updateBackordersALDataBase() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Flag value:  "+ this.bBOFLAG);
        // If true: Whole DB; False: Search DB
        if ( bBOFLAG == true ) {
            //Cleans the DB ArrayList
            alBckordDB.clear();
            String sBSta="", sDate="", sSvRq="", sTask="", sISO="",
                sItem="", sQty="", sDesc="", sTkSt="", sPLC="", sCrit="",
                sCond="", sSrAs="", sAlts="", sComm="", sISO1="", sAwb1="", 
                sISO2="", sAwb2="", sISO3="", sAwb3="", sIsMB="", sAwMB="", 
                sSIMI="", sTkNt="", sBOMT="", sTrak=""; 
            
            for ( int i=0; i < this.jtblBackorders.getRowCount(); i++ )
            {
                sBSta = jtblBackorders.getValueAt(i, 0).toString();
                sDate = jtblBackorders.getValueAt(i, 1).toString();
                sSvRq = jtblBackorders.getValueAt(i, 2).toString();
                sTask = jtblBackorders.getValueAt(i, 3).toString();
                sISO = jtblBackorders.getValueAt(i, 4).toString();
                sItem = jtblBackorders.getValueAt(i, 5).toString();
                sQty = jtblBackorders.getValueAt(i, 6).toString();
                sDesc = jtblBackorders.getValueAt(i, 7).toString();
                sTkSt = jtblBackorders.getValueAt(i, 8).toString();
                sPLC = jtblBackorders.getValueAt(i, 9).toString();
                sCrit = jtblBackorders.getValueAt(i, 10).toString();
                sCond = jtblBackorders.getValueAt(i, 11).toString();
                sSrAs = jtblBackorders.getValueAt(i, 12).toString();
                sAlts = jtblBackorders.getValueAt(i, 13).toString();
                sComm = jtblBackorders.getValueAt(i, 14).toString();
                sISO1 = jtblBackorders.getValueAt(i, 15).toString();
                sAwb1 = jtblBackorders.getValueAt(i, 16).toString();
                sISO2 = jtblBackorders.getValueAt(i, 17).toString();
                sAwb2 = jtblBackorders.getValueAt(i, 18).toString();
                sISO3 = jtblBackorders.getValueAt(i, 19).toString();
                sAwb3 = jtblBackorders.getValueAt(i, 20).toString();
                sIsMB = jtblBackorders.getValueAt(i, 21).toString();
                sAwMB = jtblBackorders.getValueAt(i, 22).toString(); 
                sSIMI = jtblBackorders.getValueAt(i, 23).toString();
                sTkNt = jtblBackorders.getValueAt(i, 24).toString();
                sBOMT = jtblBackorders.getValueAt(i, 25).toString();
                sTrak = jtblBackorders.getValueAt(i, 26).toString();
                alBckordDB.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                        sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA","NA"));
            }
        }
        else {
            //Updates the search results ArrayList with the results on the screen
            updateBackordersSearchResults();
            
            for ( cls_BO_Data tmp: this.alBckordSearchResults ) {
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setBSta(tmp.getBSta());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setDate(tmp.getDate());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setSvRq(tmp.getSvRq());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setTask(tmp.getTask());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setISO(tmp.getISO());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setItem(tmp.getItem());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setQty(tmp.getQty());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setDesc(tmp.getDesc());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setTkSt(tmp.getTkSt());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setPLC(tmp.getPLC());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setCrit(tmp.getCrit());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setCond(tmp.getCond());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setSrAs(tmp.getSrAs());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setAlts(tmp.getAlts());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setComm(tmp.getComm());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setISO1(tmp.getISO1());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setAwb1(tmp.getAwb1());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setISO2(tmp.getISO2());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setAwb2(tmp.getAwb2());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setISO3(tmp.getISO3());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setAwb3(tmp.getAwb3());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setIsMB(tmp.getIsMB());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setAwMB(tmp.getAwMB());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setSIMI(tmp.getSIMI());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setTkNt(tmp.getTkNt());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setBOMT(tmp.getBOMT());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setTrak(tmp.getTrak());
                alBckordDB.get(Integer.valueOf(tmp.getPosi())).setPosi("NA");
            }
        }
        JOptionPane.showMessageDialog(this, "The Data Base has been updated");
        cleanBackordersTable();
        loadBackordersTable();
    }
    //</editor-fold>
    
    //Updates the local WebADI Data Base ArrayList according with the changes on the screen Jtable
    private void updateWebADIALDataBase() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Flag value:  "+ this.bWAFLAG);
        // If true: Whole DB; False: Search DB
        if ( bWAFLAG == true ) {
            alWebadiDB.clear();
            String sDat="", sItm="", sQty="", sFrm="", sDst="", sShpMet="", sRef="", sISO="", sAwb="", sSta="", sAct="", sTsk="", sSMI="", sCIB="", sCom="";
            for ( int i=0; i < this.jtblWebADI.getRowCount(); i++ )
            {
                System.out.println("Adding Table line: " + i);
                sDat = jtblWebADI.getValueAt(i, 0).toString();
                sItm = jtblWebADI.getValueAt(i, 1).toString();
                sQty = jtblWebADI.getValueAt(i, 2).toString();
                sFrm = jtblWebADI.getValueAt(i, 3).toString();
                sDst = jtblWebADI.getValueAt(i, 4).toString();
                sShpMet = jtblWebADI.getValueAt(i, 5).toString();
                sRef = jtblWebADI.getValueAt(i, 6).toString();
                sISO = jtblWebADI.getValueAt(i, 7).toString();
                sAwb = jtblWebADI.getValueAt(i, 8).toString();
                sSta = jtblWebADI.getValueAt(i, 9).toString();
                sAct = jtblWebADI.getValueAt(i, 10).toString();
                sTsk = jtblWebADI.getValueAt(i, 11).toString();
                sSMI = jtblWebADI.getValueAt(i, 12).toString();
                sCIB = jtblWebADI.getValueAt(i, 13).toString();
                sCom = jtblWebADI.getValueAt(i, 14).toString();
                alWebadiDB.add(new cls_WebADI_Data(sDat, sItm, sQty, sFrm, sDst, sShpMet, sRef, sISO, sAwb, sSta, sAct, sTsk, sSMI, sCIB, sCom, "NA", "NA", "NA"));
            }
        }
        else {
            //Updates the search results ArrayList with the results on the screen
            updateWebADISearchResults();
            
            for ( cls_WebADI_Data tmp: this.alWebadiSearchResults ) {
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setDat(tmp.getDat());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setItm(tmp.getItm());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setQTY(tmp.getQTY());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setFrm(tmp.getFrm());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setDst(tmp.getDst());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setShpMet(tmp.getShpMet());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setRef(tmp.getRef());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setISO(tmp.getISO());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setAwb(tmp.getAwb());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setSta(tmp.getSta());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setAct(tmp.getAct());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setTsk(tmp.getTsk());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setSMI(tmp.getSMI());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setCIB(tmp.getCIB());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setCom(tmp.getCom());
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setPos("NA");
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setXX1("NA");
                alWebadiDB.get(Integer.valueOf(tmp.getPos())).setXX2("NA");
                
            }
        }
        JOptionPane.showMessageDialog(this, "The Data Base has been updated");
        cleanWebADITable();
        loadWebADITable();
    }
    //</editor-fold>
    
    //Creates a temporary Consults ArrayList with all the info on the corresponding screen Jtable
    private ArrayList<cls_PartDataReq> loadTMPscreenConsultsDB() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_PartDataReq> tmpConsDB = new ArrayList<>();
        String sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sTsk="", sDOM="", sMov="", sTrk="";
        for ( int i=0; i < this.jtblDataBase.getRowCount(); i++ ){
            sTir = jtblDataBase.getValueAt(i, 0).toString();
            sReg = jtblDataBase.getValueAt(i, 1).toString();
            sCnt = jtblDataBase.getValueAt(i, 2).toString();
            sOrg = jtblDataBase.getValueAt(i, 3).toString();
            sPrt = jtblDataBase.getValueAt(i, 4).toString();
            sQty = jtblDataBase.getValueAt(i, 5).toString();
            sAct = jtblDataBase.getValueAt(i, 6).toString();
            sGOH = jtblDataBase.getValueAt(i, 7).toString();
            sGXS = jtblDataBase.getValueAt(i, 8).toString();
            sDat = jtblDataBase.getValueAt(i, 9).toString();
            sDOM = jtblDataBase.getValueAt(i, 10).toString();
            sMov = jtblDataBase.getValueAt(i, 11).toString();
            sTsk = jtblDataBase.getValueAt(i, 12).toString();
            sTrk = jtblDataBase.getValueAt(i, 13).toString();
            tmpConsDB.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sMov, sTsk, sTrk, "NA"));
        }
        return tmpConsDB;
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
        for ( int i=0; i < this.jtblBackorders.getRowCount(); i++ )
        {
            sBSta = jtblBackorders.getValueAt(i, 0).toString();
            sDate = jtblBackorders.getValueAt(i, 1).toString();
            sSvRq = jtblBackorders.getValueAt(i, 2).toString();
            sTask = jtblBackorders.getValueAt(i, 3).toString();
            sISO = jtblBackorders.getValueAt(i, 4).toString();
            sItem = jtblBackorders.getValueAt(i, 5).toString();
            sQty = jtblBackorders.getValueAt(i, 6).toString();
            sDesc = jtblBackorders.getValueAt(i, 7).toString();
            sTkSt = jtblBackorders.getValueAt(i, 8).toString();
            sPLC = jtblBackorders.getValueAt(i, 9).toString();
            sCrit = jtblBackorders.getValueAt(i, 10).toString();
            sCond = jtblBackorders.getValueAt(i, 11).toString();
            sSrAs = jtblBackorders.getValueAt(i, 12).toString();
            sAlts = jtblBackorders.getValueAt(i, 13).toString();
            sComm = jtblBackorders.getValueAt(i, 14).toString();
            sISO1 = jtblBackorders.getValueAt(i, 15).toString();
            sAwb1 = jtblBackorders.getValueAt(i, 16).toString();
            sISO2 = jtblBackorders.getValueAt(i, 17).toString();
            sAwb2 = jtblBackorders.getValueAt(i, 18).toString();
            sISO3 = jtblBackorders.getValueAt(i, 19).toString();
            sAwb3 = jtblBackorders.getValueAt(i, 20).toString();
            sIsMB = jtblBackorders.getValueAt(i, 21).toString();
            sAwMB = jtblBackorders.getValueAt(i, 22).toString(); 
            sSIMI = jtblBackorders.getValueAt(i, 23).toString();
            sTkNt = jtblBackorders.getValueAt(i, 24).toString();
            sBOMT = jtblBackorders.getValueAt(i, 25).toString();
            sTrak = jtblBackorders.getValueAt(i, 26).toString();
            tmpBOs.add(new cls_BO_Data(sBSta, sDate, sSvRq, sTask, sISO, sItem, sQty, sDesc, sTkSt, 
                    sPLC, sCrit, sCond, sSrAs, sAlts, sComm, sISO1, sAwb1, sISO2, sAwb2, sISO3, sAwb3,
                    sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA","NA"));
        }
        return tmpBOs;
    }
    //</editor-fold>
    
    //Updates a temporary WebADI ArrayList with all the info on the corresponding screen Jtable
    private ArrayList<cls_WebADI_Data> loadTMPscreenWebADIDB() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_WebADI_Data> tmpWAs = new ArrayList<>();
        String sDat="", sItm="", sQty="", sFrm="", sDst="", sShpMet="", sRef="", sISO="", sAwb="", sSta="", sAct="", sTsk="", sSMI="", sCIB="", sCom="";
        for ( int i=0; i < this.jtblWebADI.getRowCount(); i++ )
        {
            sDat = jtblWebADI.getValueAt(i, 0).toString();
            sItm = jtblWebADI.getValueAt(i, 1).toString();
            sQty = jtblWebADI.getValueAt(i, 2).toString();
            sFrm = jtblWebADI.getValueAt(i, 3).toString();
            sDst = jtblWebADI.getValueAt(i, 4).toString();
            sShpMet = jtblWebADI.getValueAt(i, 5).toString();
            sRef = jtblWebADI.getValueAt(i, 6).toString();
            sISO = jtblWebADI.getValueAt(i, 7).toString();
            sAwb = jtblWebADI.getValueAt(i, 8).toString();
            sSta = jtblWebADI.getValueAt(i, 9).toString();
            sAct = jtblWebADI.getValueAt(i, 10).toString();
            sTsk = jtblWebADI.getValueAt(i, 11).toString();
            sSMI = jtblWebADI.getValueAt(i, 12).toString();
            sCIB = jtblWebADI.getValueAt(i, 13).toString();
            sCom = jtblWebADI.getValueAt(i, 14).toString();
            tmpWAs.add(new cls_WebADI_Data(sDat, sItm, sQty, sFrm, sDst, sShpMet, sRef, sISO, sAwb, sSta, sAct, sTsk, sSMI, sCIB, sCom, "NA", "NA", "NA"));
        }
        return tmpWAs;
    }
    //</editor-fold>
    
    
    //UPDATING .TXT DATA BASES FROM ARRAYLISTS
    
    //Updates the local .TXT Consults Data Base file directly from the Consults Data Base ArrayList
    public void updateConsultsTXTDataBase()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        try
        {
            File fDataBase;
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter wr = null;
            
            fDataBase = new File (sLocCoDBPath); //points to the local .txt data base file
            fw = new FileWriter(fDataBase);
            bw = new BufferedWriter(fw);
            wr = new PrintWriter(bw);
            
            //Reads, line by line, all the consults that are currently in the Data Base Array List
            for(cls_PartDataReq tmp: this.alCosulDB)
            {
                wr.println( tmp.getTier() + "\t" 
                        + tmp.getRegion() + "\t" 
                        + tmp.getCountryName() + "\t" 
                        + tmp.getOrgCode() + "\t" 
                        + tmp.getPartNumber() + "\t" 
                        + tmp.getQTY() + "\t" 
                        + tmp.getActivity() + "\t" 
                        + tmp.getTotalOH() + "\t" 
                        + tmp.getTotalXS() + "\t" 
                        + tmp.getCurrentDate() + "\t" 
                        + tmp.getDOM() + "\t" 
                        + tmp.getPartMoved() + "\t"
                        + tmp.getTask() + "\t"
                        + tmp.getTracking() );
            }
            iCoQTY = alCosulDB.size();
            wr.println("CREATED CONSULTS");
            wr.println(String.valueOf(iCoQTY));
            wr.println("CREATED MAILS");
            wr.println(String.valueOf(iMaQTY));
            wr.println(this.sUser);//It writes the e-mail of the last User who logged into the DB
            wr.close();
            bw.close();
            fw.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(this,"There was an error while updating the local .TXT Data Base \n"
                + "Method: updateConsultsTXTDataBase()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Updates the local .TXT WebADI Data Base file directly from the Backorders Data Base ArrayList
    public void updateBackordersTXTDataBase()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        try
        {
            File fDataBase;
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter wr = null;
            
            fDataBase = new File (this.sLocBoDBPath); //points to the local .txt Backorders data base file
            fw = new FileWriter(fDataBase);
            bw = new BufferedWriter(fw);
            wr = new PrintWriter(bw);
            
            //Reads, line by line, all the consults that are currently in the Data Base Array List
            for(cls_BO_Data tmp: this.alBckordDB)
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
            iBoQTY = alBckordDB.size();
            wr.println("BO LINES");
            wr.println(String.valueOf(iBoQTY));
            this.jlblBODBsize.setText("<html>Data Base size:<br>" + iBoQTY + " lines</html>");
            wr.close();
            bw.close();
            fw.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(this,"There was an error while updating the local Backorders .TXT Data Base \n"
                + "Method: updateBackordersTXTDataBase()\n" + e, "GN RIGHTHAND", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Updates the local .TXT WebADI Data Base file directly from the WebADI Data Base ArrayList
    public void updateWebADITXTDataBase()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        try
        {
            File fDataBase;
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter wr = null;
            
            fDataBase = new File (this.sLocWaDBPath); //points to the local .txt WebADI data base file
            fw = new FileWriter(fDataBase);
            bw = new BufferedWriter(fw);
            wr = new PrintWriter(bw);
            
            //Reads, line by line, all the consults that are currently in the Data Base Array List
            for(cls_WebADI_Data tmp: this.alWebadiDB)
            {
                wr.println( tmp.getDat() + "\t" 
                        + tmp.getItm() + "\t" 
                        + tmp.getQTY() + "\t" 
                        + tmp.getFrm() + "\t" 
                        + tmp.getDst() + "\t" 
                        + tmp.getShpMet() + "\t" 
                        + tmp.getRef() + "\t" 
                        + tmp.getISO() + "\t" 
                        + tmp.getAwb() + "\t" 
                        + tmp.getSta() + "\t" 
                        + tmp.getAct() + "\t" 
                        + tmp.getTsk() + "\t"
                        + tmp.getSMI() + "\t"
                        + tmp.getCIB() + "\t"
                        + tmp.getCom());
            }
            iWaQTY = alWebadiDB.size();
            wr.println("WEBADI LINES");
            wr.println(String.valueOf(iWaQTY));
            this.jlblWADBsize.setText("<html>Data Base size:<br>" + iWaQTY + " lines</html>");
            wr.close();
            bw.close();
            fw.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(this,"There was an error while updating the local WebADI .TXT Data Base \n"
                + "Method: updateWebADITXTDataBase()\n" + e, "GN RIGHTHAND", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    
    
    //SEARCHING INTO THE DATA BASES
    
    //Searches for a given text into the Consults DataBase ArrayList
    //Creates an ArrayList with the results and saves the original positions in the main Consults DB ArrayList
    //Shows the results in the DB table screen
    private void searchTextConsultsDB(String sText) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        alConsulSearchResults.clear();
        for ( int i = 0; i < this.alCosulDB.size(); i++ ) {
            //Looks for the text chain into the PN or Tracking
            if ( (alCosulDB.get(i).getTier().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getRegion().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getCountryName().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getOrgCode().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getPartNumber().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getActivity().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getCurrentDate().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getDOM().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getPartMoved().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getTask().toUpperCase().indexOf(sText) != -1) ||
                    (alCosulDB.get(i).getTracking().toUpperCase().indexOf(sText) != -1) ) {
                alConsulSearchResults.add(new cls_PartDataReq(alCosulDB.get(i).getTier(),
                        alCosulDB.get(i).getRegion(), 
                        alCosulDB.get(i).getCountryName(),
                        alCosulDB.get(i).getOrgCode(),
                        alCosulDB.get(i).getPartNumber(),
                        alCosulDB.get(i).getQTY(),
                        alCosulDB.get(i).getActivity(),
                        alCosulDB.get(i).getTotalOH(),
                        alCosulDB.get(i).getTotalXS(),
                        alCosulDB.get(i).getCurrentDate(),
                        alCosulDB.get(i).getDOM(),
                        alCosulDB.get(i).getPartMoved(),
                        alCosulDB.get(i).getTask(),
                        alCosulDB.get(i).getTracking(),
                        String.valueOf(i)));
            }
        }
        //Checks if the process detected results or not
        if ( alConsulSearchResults.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
        }
        else {//If the process got results, it showes them in the screen
            JOptionPane.showMessageDialog(this, alConsulSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            this.cleanConsultsDBTable();
            for (cls_PartDataReq tmp : alConsulSearchResults) {
                try {
                    DataBaseColumn[0] = tmp.getTier();
                    DataBaseColumn[1] = tmp.getRegion();
                    DataBaseColumn[2] = tmp.getCountryName();
                    DataBaseColumn[3] = tmp.getOrgCode();
                    DataBaseColumn[4] = tmp.getPartNumber();
                    DataBaseColumn[5] = tmp.getQTY();
                    DataBaseColumn[6] = tmp.getActivity();
                    DataBaseColumn[7] = tmp.getTotalOH();
                    DataBaseColumn[8] = tmp.getTotalXS();
                    DataBaseColumn[9] = tmp.getCurrentDate();
                    DataBaseColumn[10] = tmp.getDOM();
                    DataBaseColumn[11] = tmp.getPartMoved();
                    DataBaseColumn[12] = tmp.getTask();
                    DataBaseColumn[13] = tmp.getTracking();
                    tblModelDataBase.addRow(DataBaseColumn);
                    jtblDataBase.setModel(this.tblModelDataBase);
                }
                catch(Exception e){JOptionPane.showMessageDialog(this, "There was an error while loading the results\n"
                        + "Method: consultsTextSearch()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE);}  
            }
            //Warns the System that the DB table screen is now showing the results list
            this.bDBFLAG = false;
            this.jlblDBFlag.setText("<html><font color='orange'>SEARCH RESULTS TABLE</font></html>");
        }
    }
    //</editor-fold>
    
    //Searches for a given text into the Backorders DataBase ArrayList
    //Creates an ArrayList with the results and saves the original positions in the main Backorders DB ArrayList
    //Shows the results in the DB table screen
    public void searchTextBackordersDB(String sText) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        alBckordSearchResults.clear();
        for ( int i = 0; i < this.alBckordDB.size(); i++ ) {
            //Looks for the text chain into the different columns (except comments, email tittle and task notes columns)
            if ( (alBckordDB.get(i).getBSta().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getDate().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getSvRq().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getTask().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getISO().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getItem().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getQty().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getDesc().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getTkSt().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getPLC().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getCrit().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getCond().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getSrAs().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getAlts().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getISO1().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getAwb1().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getISO2().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getAwb2().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getISO3().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getAwb3().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getIsMB().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getAwMB().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getSIMI().toUpperCase().indexOf(sText) != -1) ||
                    (alBckordDB.get(i).getTrak().toUpperCase().indexOf(sText) != -1)) {
                alBckordSearchResults.add(new cls_BO_Data(alBckordDB.get(i).getBSta(),
                        alBckordDB.get(i).getDate(), 
                        alBckordDB.get(i).getSvRq(),
                        alBckordDB.get(i).getTask(),
                        alBckordDB.get(i).getISO(),
                        alBckordDB.get(i).getItem(),
                        alBckordDB.get(i).getQty(),
                        alBckordDB.get(i).getDesc(),
                        alBckordDB.get(i).getTkSt(),
                        alBckordDB.get(i).getPLC(),
                        alBckordDB.get(i).getCrit(),
                        alBckordDB.get(i).getCond(),
                        alBckordDB.get(i).getSrAs(),
                        alBckordDB.get(i).getAlts(),
                        alBckordDB.get(i).getComm(),
                        alBckordDB.get(i).getISO1(),
                        alBckordDB.get(i).getAwb1(),
                        alBckordDB.get(i).getISO2(),
                        alBckordDB.get(i).getAwb2(),
                        alBckordDB.get(i).getISO3(),
                        alBckordDB.get(i).getAwb3(),
                        alBckordDB.get(i).getIsMB(),
                        alBckordDB.get(i).getAwMB(),
                        alBckordDB.get(i).getSIMI(),
                        alBckordDB.get(i).getTkNt(),
                        alBckordDB.get(i).getBOMT(),
                        alBckordDB.get(i).getTrak(),
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
        if ( alBckordSearchResults.isEmpty() ) {
            //JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
        }
        else {
            //JOptionPane.showMessageDialog(this, alBOSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            //Shows the results in the screen
            this.cleanBackordersTable();
            for (cls_BO_Data tmp : alBckordSearchResults) {
                try {
                    BOColumn[0] = tmp.getBSta();
                    BOColumn[1] = tmp.getDate();
                    BOColumn[2] = tmp.getSvRq();
                    BOColumn[3] = tmp.getTask();
                    BOColumn[4] = tmp.getISO();
                    BOColumn[5] = tmp.getItem();
                    BOColumn[6] = tmp.getQty();
                    BOColumn[7] = tmp.getDesc();
                    BOColumn[8] = tmp.getTkSt();
                    BOColumn[9] = tmp.getPLC();
                    BOColumn[10] = tmp.getCrit();
                    BOColumn[11] = tmp.getCond();
                    BOColumn[12] = tmp.getSrAs();
                    BOColumn[13] = tmp.getAlts();
                    BOColumn[14] = tmp.getComm();
                    BOColumn[15] = tmp.getISO1();
                    BOColumn[16] = tmp.getAwb1();
                    BOColumn[17] = tmp.getISO2();
                    BOColumn[18] = tmp.getAwb2();
                    BOColumn[19] = tmp.getISO3();
                    BOColumn[20] = tmp.getAwb3();
                    BOColumn[21] = tmp.getIsMB();
                    BOColumn[22] = tmp.getAwMB();
                    BOColumn[23] = tmp.getSIMI();
                    BOColumn[24] = tmp.getTkNt();
                    BOColumn[25] = tmp.getBOMT();
                    BOColumn[26] = tmp.getTrak();
                    tblModelBackorders.addRow(BOColumn);
                    jtblBackorders.setModel(this.tblModelBackorders);
                }
                catch(Exception e){JOptionPane.showMessageDialog(this, "There was an error while loading the Backorders search results\n"
                        + "Method: backordersTextSearch()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE);}  
            }
            //Warns the System that the DB table screen is now showing the WebADI search results list
            this.bBOFLAG = false;
            this.jlblBOFlag.setText("<html>Now showing: <font color='orange'>Search results</font></html>");
        }
    }
    //</editor-fold>
    
    //Searches for a given text into the WebADI DataBase ArrayList
    //Creates an ArrayList with the results and saves the original positions in the main WebADI DB ArrayList
    //Shows the results in the DB table screen
    private void searchTextWebADIDB(String sText) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        alWebadiSearchResults.clear();
        for ( int i = 0; i < this.alWebadiDB.size(); i++ ) {
            //Looks for the text chain into the different columns
            if ( (alWebadiDB.get(i).getDat().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getItm().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getFrm().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getDst().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getShpMet().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getRef().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getISO().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getAwb().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getSta().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getAct().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getTsk().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getSMI().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getCIB().toUpperCase().indexOf(sText) != -1) ||
                    (alWebadiDB.get(i).getCom().toUpperCase().indexOf(sText) != -1)) {
                alWebadiSearchResults.add(new cls_WebADI_Data(alWebadiDB.get(i).getDat(),
                        alWebadiDB.get(i).getItm(), 
                        alWebadiDB.get(i).getQTY(),
                        alWebadiDB.get(i).getFrm(),
                        alWebadiDB.get(i).getDst(),
                        alWebadiDB.get(i).getShpMet(),
                        alWebadiDB.get(i).getRef(),
                        alWebadiDB.get(i).getISO(),
                        alWebadiDB.get(i).getAwb(),
                        alWebadiDB.get(i).getSta(),
                        alWebadiDB.get(i).getAct(),
                        alWebadiDB.get(i).getTsk(),
                        alWebadiDB.get(i).getSMI(),
                        alWebadiDB.get(i).getCIB(),
                        alWebadiDB.get(i).getCom(),
                        String.valueOf(i),//Identifies the position were the value was found
                        "NA",
                        "NA"));
            }
        }
        //Checks if the process detected results or not
        if ( alWebadiSearchResults.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
        }
        else {
            JOptionPane.showMessageDialog(this, alWebadiSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            //Shows the results in the screen
            this.cleanWebADITable();
            for (cls_WebADI_Data tmp : alWebadiSearchResults) {
                try {
                    WebADIColumn[0] = tmp.getDat();
                    WebADIColumn[1] = tmp.getItm();
                    WebADIColumn[2] = tmp.getQTY();
                    WebADIColumn[3] = tmp.getFrm();
                    WebADIColumn[4] = tmp.getDst();
                    WebADIColumn[5] = tmp.getShpMet();
                    WebADIColumn[6] = tmp.getRef();
                    WebADIColumn[7] = tmp.getISO();
                    WebADIColumn[8] = tmp.getAwb();
                    WebADIColumn[9] = tmp.getSta();
                    WebADIColumn[10] = tmp.getAct();
                    WebADIColumn[11] = tmp.getTsk();
                    WebADIColumn[12] = tmp.getSMI();
                    WebADIColumn[13] = tmp.getCIB();
                    WebADIColumn[14] = tmp.getCom();
                    tblModelWebADI.addRow(WebADIColumn);
                    jtblWebADI.setModel(this.tblModelWebADI);
                }
                catch(Exception e){JOptionPane.showMessageDialog(this, "There was an error while loading the WebADI search results\n"
                        + "Method: webADITextSearch()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE);}  
            }
            //Warns the System that the DB table screen is now showing the WebADI search results list
            this.bWAFLAG = false;
            this.jlblWAFlag.setText("<html>Now showing: <font color='orange'>Search results</font></html>");
        }
    }
    //</editor-fold>
    
    
    
    //ADDING NEW DATA INTO THE BACKORDERS DATA BASE
    
    //Searches for ODS Backorders lines into the existing data base
    //Returns the Arraylist position where the line is
    private int findBODBLine(String sSvRq, String sTask, String sItem, String sOrNu){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i = 0; i < alBckordDB.size(); i++ ){
            if ( alBckordDB.get(i).getSvRq().equals(sSvRq) &&
                    alBckordDB.get(i).getTask().equals(sTask) &&
                    alBckordDB.get(i).getItem().equals(sItem) &&
                    alBckordDB.get(i).getISO().equals(sOrNu) ) {
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
    
    //Looks for previous lines in the exisiting Backorders DB ArrayList
    //If the line doesn't exit, it adds it to the Backorders data base ArrayList and fulfills the necessary fields
    private void createNewBackorderLines(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Loads the PPSE data sheet from the local PPSE.xls file
        loadPPSEfromXLS();
        cls_Date_Manager tmpDM = new cls_Date_Manager();
        String sDate = "NA", sCrit = "NA", sDisp = "NA", sSrch = "NA";
        iNEW = 0;
        //It starts reading from the 2nd line of the BO XLS File (2D Matrix)
        for ( int i=1; i<xlsODSBOMatrix.length; i++ ){
            //Checks if the line on the XLS file (2D Matrix) already exists in the Backorders DB (ArrayList)
            if ( findBODBLine(xlsODSBOMatrix[i][iSvRq_odsbo], xlsODSBOMatrix[i][iTask_odsbo], xlsODSBOMatrix[i][iItem_odsbo], xlsODSBOMatrix[i][iISO_odsbo]) == -1 ){
                //If the Line is not found, it creates a new one
                //Gets the date from the original xls file and reformats it as yyyy-mm-dd for the DB
                sDate = tmpDM.formatDate_yyyyMMdd(tmpDM.convertMMDDYY_toDate(xlsODSBOMatrix[i][iDate_odsbo]));
                sCrit = getCriticality(xlsODSBOMatrix[i][iItem_odsbo]);
                sDisp = getDisposition(xlsODSBOMatrix[i][iItem_odsbo]);
                //Determines the Good New Search Assumption depending on the PLC and Disp
                if( xlsODSBOMatrix[i][iPLC_odsbo].equals("P") || xlsODSBOMatrix[i][iPLC_odsbo].equals("N") || xlsODSBOMatrix[i][iPLC_odsbo].equals("S") 
                    && sDisp.equals("Consumable") ){
                    sSrch = "Assume Good New";
                }
                else{
                    if( xlsODSBOMatrix[i][iPLC_odsbo].equals("P") || xlsODSBOMatrix[i][iPLC_odsbo].equals("N") && sDisp.equals("Repairable") ){
                        sSrch = "Assume Good New";
                    }
                    else{
                        sSrch = "DOM Inspection";
                    }
                }
                //Creates the new Lines in the BO Data Base ArrayList
                alBckordDB.add(new cls_BO_Data("Good New Search",
                        sDate, //Reformated Date from xls to yyyy-mm-dd
                        xlsODSBOMatrix[i][iSvRq_odsbo],
                        xlsODSBOMatrix[i][iTask_odsbo],
                        xlsODSBOMatrix[i][iISO_odsbo],
                        xlsODSBOMatrix[i][iItem_odsbo],
                        xlsODSBOMatrix[i][iQty_odsbo],
                        xlsODSBOMatrix[i][iDesc_odsbo],
                        xlsODSBOMatrix[i][iTkSt_odsbo],
                        xlsODSBOMatrix[i][iPLC_odsbo],
                        sCrit,//Criticality
                        sDisp,//Condition
                        sSrch,//Search Assumption
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
                        "Planning Team Argentina Part: " + xlsODSBOMatrix[i][iItem_odsbo] + " | Quantity: " + xlsODSBOMatrix[i][iQty_odsbo],
                        "AR / SR " + xlsODSBOMatrix[i][iSvRq_odsbo] + " / Task " + xlsODSBOMatrix[i][iTask_odsbo] + " / BO " + xlsODSBOMatrix[i][iISO_odsbo] + " / PN " + xlsODSBOMatrix[i][iItem_odsbo],
                        "NA",//BO mail title 
                        "NA",//Tracking number 
                        "NA",//Position 
                        "NA",//Planner 
                        "NA",//Last review date 
                        "NA",//Revised ETA 
                        "NA",//Path 
                        "NA",//Improved ETA
                        "NA",//XX1
                        "NA",//XXX2
                        "NA"));//XX3
                iNEW = iNEW + 1;
            }
        }
        //Cleans the PPSE 2D Matrix
        xlsPPSEMatrix = null;
        //System.gc();
    }
    //</editor-fold>
        
    //Cheks the lines on the Backorders DB ArrayList that are no longer reported in the ODS file
    //Sends a warning to the User in order to check for those that are not closed yet
    private void checkClosedLines(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        iCHK = 0;
        for ( int r=0; r<alBckordDB.size(); r++ ){
            if ( findBOXLLine(alBckordDB.get(r).getSvRq(), alBckordDB.get(r).getTask(), alBckordDB.get(r).getItem(), alBckordDB.get(r).getISO()) == -1 ){
                if ( alBckordDB.get(r).getBSta().equals("Closed") ){
                    alBckordDB.get(r).setTkSt("Closed");
                }
                else{
                    alBckordDB.get(r).setTkSt("CHECK");
                    iCHK = iCHK + 1;
                }
            }
        }
    }
    //</editor-fold>
    
    
    
    
    //UPDATING SEARCH RESULTS
    
    //Updates the Consults search results ArrayList with the results on the Jtable screen 
    private void updateConsultsSearchResults() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int i = 0;
        for ( cls_PartDataReq tmp : alConsulSearchResults ) {
            tmp.setTier(jtblDataBase.getValueAt(i, 0).toString());
            tmp.setRegion(jtblDataBase.getValueAt(i, 1).toString());
            tmp.setCountryName(jtblDataBase.getValueAt(i, 2).toString());
            tmp.setOrgCode(jtblDataBase.getValueAt(i, 3).toString());
            tmp.setPartNumber(jtblDataBase.getValueAt(i, 4).toString());
            tmp.setQTY(jtblDataBase.getValueAt(i, 5).toString());
            tmp.setActivity(jtblDataBase.getValueAt(i, 6).toString());
            tmp.setTotalOH(jtblDataBase.getValueAt(i, 7).toString());
            tmp.setTotalXS(jtblDataBase.getValueAt(i, 8).toString());
            tmp.setCurrentDate(jtblDataBase.getValueAt(i, 9).toString());
            tmp.setDOM(jtblDataBase.getValueAt(i, 10).toString());
            tmp.setPartMoved(jtblDataBase.getValueAt(i, 11).toString());
            tmp.setTask(jtblDataBase.getValueAt(i, 12).toString());
            tmp.setTracking(jtblDataBase.getValueAt(i, 13).toString());
            i++;
        }
    }
    //</editor-fold>
    
    //Updates the Backorders search results ArrayList with the results on the Jtable screen 
    private void updateBackordersSearchResults() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int i = 0;
        for ( cls_BO_Data tmp : alBckordSearchResults ) {
            tmp.setBSta(jtblBackorders.getValueAt(i, 0).toString());
            tmp.setDate(jtblBackorders.getValueAt(i, 1).toString());
            tmp.setSvRq(jtblBackorders.getValueAt(i, 2).toString());
            tmp.setTask(jtblBackorders.getValueAt(i, 3).toString());
            tmp.setISO(jtblBackorders.getValueAt(i, 4).toString());
            tmp.setItem(jtblBackorders.getValueAt(i, 5).toString());
            tmp.setQty(jtblBackorders.getValueAt(i, 6).toString());
            tmp.setDesc(jtblBackorders.getValueAt(i, 7).toString());
            tmp.setTkSt(jtblBackorders.getValueAt(i, 8).toString());
            tmp.setPLC(jtblBackorders.getValueAt(i, 9).toString());
            tmp.setCrit(jtblBackorders.getValueAt(i, 10).toString());
            tmp.setCond(jtblBackorders.getValueAt(i, 11).toString());
            tmp.setSrAs(jtblBackorders.getValueAt(i, 12).toString());
            tmp.setAlts(jtblBackorders.getValueAt(i, 13).toString());
            tmp.setComm(jtblBackorders.getValueAt(i, 14).toString());
            tmp.setISO1(jtblBackorders.getValueAt(i, 15).toString());
            tmp.setAwb1(jtblBackorders.getValueAt(i, 16).toString());
            tmp.setISO2(jtblBackorders.getValueAt(i, 17).toString());
            tmp.setAwb2(jtblBackorders.getValueAt(i, 18).toString());
            tmp.setISO3(jtblBackorders.getValueAt(i, 19).toString());
            tmp.setAwb3(jtblBackorders.getValueAt(i, 20).toString());
            tmp.setIsMB(jtblBackorders.getValueAt(i, 21).toString());
            tmp.setAwMB(jtblBackorders.getValueAt(i, 22).toString());
            tmp.setSIMI(jtblBackorders.getValueAt(i, 23).toString());
            tmp.setTkNt(jtblBackorders.getValueAt(i, 24).toString());
            tmp.setBOMT(jtblBackorders.getValueAt(i, 25).toString());
            tmp.setTrak(jtblBackorders.getValueAt(i, 26).toString());
            i++;
        }
    }
    //</editor-fold>
    
    //Updates the WebADI search results ArrayList with the results on the Jtable screen 
    private void updateWebADISearchResults() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int i = 0;
        for ( cls_WebADI_Data tmp : alWebadiSearchResults ) {
            tmp.setDat(jtblWebADI.getValueAt(i, 0).toString());
            tmp.setItm(jtblWebADI.getValueAt(i, 1).toString());
            tmp.setQTY(jtblWebADI.getValueAt(i, 2).toString());
            tmp.setFrm(jtblWebADI.getValueAt(i, 3).toString());
            tmp.setDst(jtblWebADI.getValueAt(i, 4).toString());
            tmp.setShpMet(jtblWebADI.getValueAt(i, 5).toString());
            tmp.setRef(jtblWebADI.getValueAt(i, 6).toString());
            tmp.setISO(jtblWebADI.getValueAt(i, 7).toString());
            tmp.setAwb(jtblWebADI.getValueAt(i, 8).toString());
            tmp.setSta(jtblWebADI.getValueAt(i, 9).toString());
            tmp.setAct(jtblWebADI.getValueAt(i, 10).toString());
            tmp.setTsk(jtblWebADI.getValueAt(i, 11).toString());
            tmp.setSMI(jtblWebADI.getValueAt(i, 12).toString());
            tmp.setCIB(jtblWebADI.getValueAt(i, 13).toString());
            tmp.setCom(jtblWebADI.getValueAt(i, 14).toString());
            i++;
        }
    }
    //</editor-fold>
    
    
    
    //ADDING LINES

    //Adds new Lines at the end of the Consults DB screen table
    private void addConsultsNewLine(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        alCosulDB.add(new cls_PartDataReq("NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"));
        cleanConsultsDBTable();
        //Loads the information from the Consults DB ArrayList of current consults into de Consults DB JTable
        loadConsultsDBTable();
        Rectangle cellBounds = this.jtblDataBase.getCellRect(jtblDataBase.getRowCount() - 1, 0, true);
        jtblDataBase.scrollRectToVisible(cellBounds);
    }
    //</editor-fold>
    
    //Adds new Lines at the end of the WebADI screen table
    private void addBONewLine(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        alBckordDB.add(new cls_BO_Data("NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA", "NA", "NA", 
                "NA", "NA", "NA", "NA", "NA"));
        cleanBackordersTable();
        loadBackordersTable();
        Rectangle cellBounds = this.jtblBackorders.getCellRect(jtblBackorders.getRowCount() - 1, 0, true);
        jtblBackorders.scrollRectToVisible(cellBounds);
    }
    //</editor-fold>  
        
    //Adds new Lines at the end of the WebADI screen table
    private void addWebADINewLine(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        alWebadiDB.add(new cls_WebADI_Data("NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"));
        cleanWebADITable();
        loadWebADITable();
        Rectangle cellBounds = this.jtblWebADI.getCellRect(jtblWebADI.getRowCount() - 1, 0, true);
        jtblWebADI.scrollRectToVisible(cellBounds);
    }
    //</editor-fold>    
    
    
    //Compares the Data Bases and indicates the differences found
    //Options: "Consults", "Backorders", "WebADI"
    private boolean checkForUnsavedChanges(String sData){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        boolean bDIFF = false; //false: there is no difference
        switch ( sData ){
            case "Consults" :{
                //Creates a temporary Consults ArrayList with all the info on the corresponding screen Jtable
                ArrayList<cls_PartDataReq> tmpCons = loadTMPscreenConsultsDB();
                //Compares two versions of the Consults Data Base. Return TRUE if differences are found.
                bDIFF = compareConsultsDBs(tmpCons, alCosulDB);
                break;
            }
            case "Backorders" :{
                //Creates a temporary Backorders ArrayList with all the info on the corresponding screen Jtable
                ArrayList<cls_BO_Data> tmpBOs = loadTMPscreenBackordersDB();
                //Compares two versions of the Backorders Data Base. Return TRUE if differences are found.
                bDIFF = compareBackordersDBs(tmpBOs, alBckordDB);
                break;
            }
            case "WebADI" :{
                //Creates a temporary WebADI ArrayList with all the info on the corresponding screen Jtable
                ArrayList<cls_WebADI_Data> tmpWAs = loadTMPscreenWebADIDB();
                //Compares two versions of the WebADI Data Base. Return TRUE if differences are found.
                bDIFF = compareWebADIDBs(tmpWAs, alWebadiDB);
                break;
            }
        }
        return bDIFF;
    }
    //</editor-fold>
        
    //Compares the Data Bases and indicates the differences found
    //Options: "Consults", "Backorders", ""WebADI"
    private boolean compareDataBases(String sData){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        boolean bDIFF = false; //false: there is no difference
        switch ( sData ){
            case "Consults" :{
                if ( bONLINE == true ){
                    //Reloads the remote .txt DB in a temporary Array (this will be the last saved version)
                    ArrayList<cls_PartDataReq> tmpRemCons = loadTMPRemConsDB();
                    //Compares the "last saved version" with the active ArrayList
                    bDIFF = compareConsultsDBs(tmpRemCons, alCosulDB);
                }
                else{
                    //Reloads the local .txt DB in a temporary Array (this will be the last saved version)
                    ArrayList<cls_PartDataReq> tmpLocCons = loadTMPlocConsultsDB();
                    //Compares the "last saved version" with the active ArrayList
                    bDIFF = compareConsultsDBs(tmpLocCons, alCosulDB);
                }
                break;
            }
            case "Backorders" :{
                if ( bONLINE == true ){
                    //Reloads the remote .txt DB in a temporary Array (this will be the last saved version)
                    ArrayList<cls_BO_Data> tmpRemBOs = loadTMPRemBackordersDB();
                    //Compares the "last saved version" with the active ArrayList
                    bDIFF = compareBackordersDBs(tmpRemBOs, alBckordDB);
                }
                else{
                    //Reloads the local .txt DB in a temporary Array (this will be the last saved version)
                    ArrayList<cls_BO_Data> tmpLocBOs = loadTMPlocBackordersDB();
                    //Compares the "last saved version" with the active ArrayList
                    bDIFF = compareBackordersDBs(tmpLocBOs, alBckordDB);
                }
                break;
            }
            case "WebADI" :{
                if ( bONLINE == true ){
                    //Reloads the remote .txt DB in a temporary Array (this will be the last saved version)
                    ArrayList<cls_WebADI_Data> tmpRemWAs = loadTMPRemWebADIDB();
                    //Compares the "last saved version" with the active ArrayList
                    bDIFF = compareWebADIDBs(tmpRemWAs, alWebadiDB);
                }
                else{
                    //Reloads the local .txt DB in a temporary Array (this will be the last saved version)
                    ArrayList<cls_WebADI_Data> tmpLocWAs = loadTMPlocWebADIDB();
                    //Compares the "last saved version" with the active ArrayList
                    bDIFF = compareWebADIDBs(tmpLocWAs, alWebadiDB);
                }
                break;
            }
        }
        return bDIFF;
    }
    //</editor-fold>
    
    
    //Compares two versions of the Consults Data Base. Return TRUE if differences are found.
    private boolean compareConsultsDBs(ArrayList<cls_PartDataReq> A1, ArrayList<cls_PartDataReq> A2){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bDIFF = false;
        if ( A1.size() != A2.size() ){//Checks the size
            bDIFF = true;
        }
        else{//if Size is the same, it checks contents
            for ( int i=0; i<A1.size(); i++ ){
                if ( !A1.get(i).getTier().equals(A2.get(i).getTier())
                        || !A1.get(i).getRegion().equals(A2.get(i).getRegion())
                        || !A1.get(i).getCountryName().equals(A2.get(i).getCountryName())
                        || !A1.get(i).getOrgCode().equals(A2.get(i).getOrgCode())
                        || !A1.get(i).getPartNumber().equals(A2.get(i).getPartNumber())
                        || !A1.get(i).getQTY().equals(A2.get(i).getQTY())
                        || !A1.get(i).getActivity().equals(A2.get(i).getActivity())
                        || !A1.get(i).getTotalOH().equals(A2.get(i).getTotalOH())
                        || !A1.get(i).getTotalXS().equals(A2.get(i).getTotalXS())
                        || !A1.get(i).getCurrentDate().equals(A2.get(i).getCurrentDate())
                        || !A1.get(i).getPartMoved().equals(A2.get(i).getPartMoved())
                        || !A1.get(i).getDOM().equals(A2.get(i).getDOM())
                        || !A1.get(i).getTask().equals(A2.get(i).getTask())
                        || !A1.get(i).getTracking().equals(A2.get(i).getTracking())){
                    bDIFF = true;
                    break;
                }
            }
        }
        return bDIFF;
    }
    //</editor-fold>
    
    //Compares two versions of the Backorders Data Base. Return TRUE if differences are found.
    private boolean compareBackordersDBs(ArrayList<cls_BO_Data> A1, ArrayList<cls_BO_Data> A2){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bDIFF = false;
        if ( A1.size() != A2.size() ){//Checks the size
            bDIFF = true;
        }
        else{//if Size is the same, it checks contents
            for ( int i=0; i<A1.size(); i++ ){
                if ( !A1.get(i).getBSta().equals(A2.get(i).getBSta())
                        || !A1.get(i).getDate().equals(A2.get(i).getDate())
                        || !A1.get(i).getSvRq().equals(A2.get(i).getSvRq())
                        || !A1.get(i).getTask().equals(A2.get(i).getTask())
                        || !A1.get(i).getISO().equals(A2.get(i).getISO())
                        || !A1.get(i).getItem().equals(A2.get(i).getItem())
                        || !A1.get(i).getQty().equals(A2.get(i).getQty())
                        || !A1.get(i).getDesc().equals(A2.get(i).getDesc())
                        || !A1.get(i).getTkSt().equals(A2.get(i).getTkSt())
                        || !A1.get(i).getPLC().equals(A2.get(i).getPLC())
                        || !A1.get(i).getCrit().equals(A2.get(i).getCrit())
                        || !A1.get(i).getCond().equals(A2.get(i).getCond())
                        || !A1.get(i).getSrAs().equals(A2.get(i).getSrAs())
                        || !A1.get(i).getAlts().equals(A2.get(i).getAlts())
                        || !A1.get(i).getComm().equals(A2.get(i).getComm())
                        || !A1.get(i).getISO1().equals(A2.get(i).getISO1())
                        || !A1.get(i).getAwb1().equals(A2.get(i).getAwb1())
                        || !A1.get(i).getISO2().equals(A2.get(i).getISO2())
                        || !A1.get(i).getAwb2().equals(A2.get(i).getAwb2())
                        || !A1.get(i).getISO3().equals(A2.get(i).getISO3())
                        || !A1.get(i).getAwb3().equals(A2.get(i).getAwb3())
                        || !A1.get(i).getIsMB().equals(A2.get(i).getIsMB())
                        || !A1.get(i).getAwMB().equals(A2.get(i).getAwMB())
                        || !A1.get(i).getSIMI().equals(A2.get(i).getSIMI())
                        || !A1.get(i).getTkNt().equals(A2.get(i).getTkNt())
                        || !A1.get(i).getBOMT().equals(A2.get(i).getBOMT())
                        || !A1.get(i).getTrak().equals(A2.get(i).getTrak())){
                    bDIFF = true;
                    break;
                }
            }
        }
        return bDIFF;
    }
    //</editor-fold>
    
    //Compares two versions of the WebADI Data Base. Return TRUE if differences are found.
    private boolean compareWebADIDBs(ArrayList<cls_WebADI_Data> A1, ArrayList<cls_WebADI_Data> A2){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bDIFF = false;
        if ( A1.size() != A2.size() ){//Checks the size
            bDIFF = true;
        }
        else{//if Size is the same, it checks contents
            for ( int i=0; i<A1.size(); i++ ){
                if ( !A1.get(i).getDat().equals(A2.get(i).getDat())
                        || !A1.get(i).getItm().equals(A2.get(i).getItm())
                        || !A1.get(i).getQTY().equals(A2.get(i).getQTY())
                        || !A1.get(i).getFrm().equals(A2.get(i).getFrm())
                        || !A1.get(i).getDst().equals(A2.get(i).getDst())
                        || !A1.get(i).getShpMet().equals(A2.get(i).getShpMet())
                        || !A1.get(i).getRef().equals(A2.get(i).getRef())
                        || !A1.get(i).getISO().equals(A2.get(i).getISO())
                        || !A1.get(i).getAwb().equals(A2.get(i).getAwb())
                        || !A1.get(i).getSta().equals(A2.get(i).getSta())
                        || !A1.get(i).getAct().equals(A2.get(i).getAct())
                        || !A1.get(i).getTsk().equals(A2.get(i).getTsk())
                        || !A1.get(i).getSMI().equals(A2.get(i).getSMI())
                        || !A1.get(i).getCIB().equals(A2.get(i).getCIB())
                        || !A1.get(i).getCom().equals(A2.get(i).getCom())){
                    bDIFF = true;
                    break;
                }
            }
        }
        return bDIFF;
    }
    //</editor-fold>


    //Changes the name of some countries into a given ArrayList data base
    private ArrayList<cls_PartDataReq> updateArrayListNames(ArrayList<cls_PartDataReq> alData){
        for ( cls_PartDataReq tmp : alData ) {
            if ( tmp.getCountryName().equals("KOREA, REPUBLIC OF") ) {tmp.setCountryName("KOREA");}
            
        }
        return alData;
    }
    
    //Changes the name of some countries into a given 2d Array data base
    private String[][] updateArrayNames(String[][] bdData){
        for ( int i=0; i<bdData.length; i++ ) {
            for ( int j=0; j<bdData[i].length; j++ ) {
                if ( bdData[i][j].equals("KOREA, REPUBLIC OF") ) {bdData[i][j] = "KOREA";}
            }
        }
        return bdData;
    }

    
    
    
    /*  *****MAIL MANAGEMENT RELATED METHODS***** */
    
    //Determines the MailTo value depending on the Contry and Organization
    private String createConsultMailTo(String sCountry, String sOrg) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Special cases that depend on the Org as well
        if( sCountry.equals("GERMANY") ){sCountry = sCountry + sOrg;}
        if( sCountry.equals("SWITZERLAND") ){sCountry = sCountry + sOrg;}
        if( sCountry.equals("UNITED KINGDOM") ){sCountry = sCountry + sOrg;}
        if (sOrg.equals("MIA") ){sCountry = sCountry + sOrg;}
        if (sOrg.equals("LOC") ){sCountry = sCountry + sOrg;}
        //Initializing result variable
        String sMailTo = "NA";
                switch (sCountry) {
            case "AUSTRALIA" : sMailTo = "DHL Australia SYD (nsw_dsc_oracle@dhl.com)"; break;
            case "AUSTRIA" : sMailTo = "DHL Austria VIE (ORACLE-AT@DHL.COM)"; break;
            case "BELGIUM" : sMailTo = "DHL Belgium BRU (Jurgen.HERMANS@dhl.com)"; break;
            case "BRAZIL" : sMailTo = "DHL Brazil SAB (oracle.brazil@dhl.com)"; break;
            case "BULGARIA" : sMailTo = "DHL Bulgaria SOI(ORACLE-BG@DHL.COM)"; break;
            case "CANADA" : sMailTo = "DHL Canada(Toronto_Inventory@appleexpress.com)"; break;
            case "CHILE" : sMailTo = "DHL Chile SAG(oracle.chile@dhl.com)"; break;
            case "CHINA" : sMailTo = "DHL China SHA(Tracy.Liu@dhl.com)"; break;
            case "COLOMBIA" : sMailTo = "DHL Colombia BOG(oracle.colombia@dhl.com)"; break;
            case "COSTA RICA" : sMailTo = "DHL Costa Rica SAS(oracle.costarica@dhl.com)"; break;
            case "CROATIA" : sMailTo = "DHL Croatia ZAG(ORACLE-HR@DHL.COM)"; break;
            case "CZECH REPUBLIC" : sMailTo = "DHL Czech Republic(ORACLE-CZ@DHL.COM)"; break;
            case "DENMARK" : sMailTo = "DHL Denmark COP(ORACLE-DK@DHL.COM)"; break;
            case "FINLAND" : sMailTo = "DHL Finland HEL(fidscspc@dhl.com)"; break;
            case "FRANCE" : sMailTo = "DHL France PAR(oracle-fr@dhl.com)"; break;
            case "GERMANYFRA" : sMailTo = "DHL Germany FRA(SPC-Frankfurt.oracle@dhl.com)"; break;
            case "GERMANYLEZ" : sMailTo = "DHL Germany LEZ(de.dsc.leipzig-cs-oracle@dhl.com); DHL Germany Inbound(de.dsc.Leipzig-Inbound-Oracle@dhl.com)"; break;
            case "GERMANYDUE" : sMailTo = "DHL Germany DUE(SPC.Duesseldorf@dhl.com)"; break;
            case "GERMANYHAM" : sMailTo = "DHL Germany HAM(Hamburg.SPC@dh.com)"; break;
            case "GERMANYMUN" : sMailTo = "Messenger Germany MUN(SPC.Muenchen@messenger.de)"; break;
            case "GRECE" : sMailTo = "DHL Greece ATH(ORACLE-GR@DHL.COM)"; break;
            case "HONG KONG" : sMailTo = "DHL Hong Kong HNG(sunhkasl@dhl.com)"; break;
            case "INDONESIA" : sMailTo = "DHL Indonesia JAK(Oracle.ADMSPL@dhl.com)"; break;
            case "IRELAND" : sMailTo = "DHL Ireland DUL(SPCDUBLIN.IE01@dhl.com)"; break;
            case "ITALY" : sMailTo = "DHL Italy MII(OracleSplitalia@dhl.com)"; break;
            case "JAPAN" : sMailTo = "DHL Japan YAS(edgjp-desc-orc-tyo@dhl.com)"; break;
            case "KOREA" : sMailTo = "DHL South Korea SEO(selasldhl.com@dhl.com)"; break;
            case "LUXEMBOURG" : sMailTo = "DHL Luxembourg LUX(ORACLE-LU@DHL.COM)"; break;
            case "MALAYSIA" : sMailTo = "DHL Malaysia KUA(SPCops.kul@dhl.com)"; break;
            case "MELBOURNE" : sMailTo = "DHL Melbourne MEL(vic_dsc_oracle@dhl.com)"; break;
            case "MEXICO" : sMailTo = "DHL Mexico MEX(oracle.mexico@dhl.com)"; break;
            case "NETHERLANDS" : sMailTo = "DHL Netherlands AMS(oracle-nl@dhl.com)"; break;
            case "NEW ZEALAND" : sMailTo = "DHL New Zealand AUC(aklspc@dhl.com)"; break;
            case "NORWAY" : sMailTo = "DHL Norway OSL(ORACLE-NO@DHL.COM)"; break;
            case "PERU" : sMailTo = "DHL Peru (oracle.peru@dhl.com)"; break;
            case "PHILIPPINES" : sMailTo = "DHL Philippines PAN (eddie.cauman@dhl.com)"; break;
            case "POLAND" : sMailTo = "DHL Poland WAR(ORACLE-PL@DHL.COM)"; break;
            case "PORTUGAL" : sMailTo = "DHL Portugal LIS(ORACLE-PT@dhl.com)"; break;
            case "PUERTO RICO" : sMailTo = "DHL Puerto Rico CAA(oracle.puertorico@dhl.com)"; break;
            case "ROMANIA" : sMailTo = "DHL Romania OTP(ORACLE-RO@DHL.COM)"; break;
            case "RUSSIAN FEDERATION" : sMailTo = "DHL Russia MOS(ORACLE-RU@DHL.COM)"; break;
            case "SINGAPORE" : sMailTo = "DHL Singapore SIN-SIG(GCS.SG.SUNCS@DHL.COM)"; break;
            case "SLOVAKIA" : sMailTo = "DHL Slovakia BRT(ORACLE-SK@DHL.COM)"; break;
            case "SLOVENIA" : sMailTo = "DHL Slovenia LJU(ORACLE-SI@DHL.COM)"; break;
            case "SOUTH AFRICA" : sMailTo = "DHL South Africa JOH(ORACLE-ZA@DHL.COM)"; break;
            case "SPAIN" : sMailTo = "DHL Spain MAD(mspc.es@dhl.com)"; break;
            case "SWEDEN" : sMailTo = "DHL Sweden STC(Daniel.Andersson@dhl.com)"; break;
            case "SWITZERLANDBEN" : sMailTo = "VOGT.Cargo Switzerland BEN(Info@vogtcargo.ch)"; break;
            case "SWITZERLANDGEN" : sMailTo = "Bluewin Switzerland GEN(GVA2SPC.CH@BLUEWIN.CH)"; break;
            case "SWITZERLANDZUR" : sMailTo = "Panatlantic Switzerland ZUR(SUNZRH@panatlantic.ch)"; break;
            case "TAIWAN" : sMailTo = "DHL Taiwan TAI(Tiffany.Lu@dhl.com)"; break;
            case "THAILAND" : sMailTo = "DHL Thailand BAN(th.spl@dhl.com)"; break;
            case "UNITED KINGDOMBIR" : sMailTo = "DHL United Kingdom BIR(BIRMINGHAM.WAREHOUSE@DHL.COM)"; break;
            case "UNITED KINGDOMBRI" : sMailTo = "DHL United Kingdom BRI(BRISTOL.WAREHOUSE@DHL.COM)"; break;
            case "UNITED KINGDOMHOU" : sMailTo = "DHL United Kingdom HOU(HEATHROW.WAREHOUSE@DHL.COM)"; break;
            case "UNITED KINGDOMLEE" : sMailTo = "DHL United Kingdom LEE(LEEDS.WAREHOUSE@DHL.COM)"; break;
            case "UNITED KINGDOMLIV" : sMailTo = "DHL United Kingdom LIV(EDINBURGH.WAREHOUSE@DHL.COM)"; break;
            case "UNITED KINGDOMLON" : sMailTo = "DHL United Kingdom LON(LONDONCITYSAMEDAY.WAREHOUSE@DHL.COM)"; break;
            case "UNITED KINGDOMMIL" : sMailTo = "DHL United Kingdom MIL(ORACLE-UK@dhl.com)"; break;
            case "UNITED STATES" : sMailTo = "DHL United States (DHLCommandCenter@dhl.com)"; break;
            case "UNITED STATESMIA" : sMailTo = "DHL United States MIA(OracleAlerts@AppleExpress.com)"; break;
            case "UNITED STATESLOC" : sMailTo = "DHL United States LOC (mwo_sun_inv@dhl.com)"; break;
            default :  sMailTo = "Mail not found in data base"; break;
        }
        return sMailTo;
    }
    //</editor-fold>
    
    //Creates a tracking number for the e-mail
    private String createTracking() throws IOException {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sTracking = "";
        String sDateCode = generateDateCode(); //MMDDAAAA
        String sCountCode = "";
        String sTmpHexPos = Integer.toHexString(Integer.valueOf(getLastMailConsult()) + 1);
        int iCharCount = sTmpHexPos.length();
        
        
        switch (iCharCount) {
            case 1 : {
                sCountCode = "000" + sTmpHexPos;
                break;
            }
            case 2 : {
                sCountCode = "00" + sTmpHexPos;
                break;
            }
            case 3 : {
                sCountCode = "0" + sTmpHexPos;
                break;
            }
            case 4 : {
                sCountCode = sTmpHexPos;
                break;
            }
            default : {
                break;
            }
        }
        sTracking = sDateCode.substring(0, 4) + "." + sCountCode + "ARP" + sDateCode.substring(6);
        setLastMailConsult();
        return sTracking;
    }
    //</editor-fold>

    //Creates the mail subject line
    //Requests the System to create a Tracking and returns the whole mail subject
    private String createMailSubject(String sOrg, String sCountry){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sMailSub = "";
        if ( sCountry.equals("ARGENTINA") ||
                sCountry.equals("URUGUAY") ||
                sCountry.equals("PARAGAY") ||
                sCountry.equals("BOLIVIA") ||
                sCountry.equals("CHILE") ||
                sCountry.equals("PERU") ||
                sCountry.equals("ECUADOR") ||
                sCountry.equals("COLOMBIA") ||
                sCountry.equals("VENEZUELA") ||
                sCountry.equals("PANAMA") ||
                sCountry.equals("COSTA RICA") ||
                sCountry.equals("NICARAGUA") ||
                sCountry.equals("HONDURAS") ||
                sCountry.equals("EL SALVADOR") ||
                sCountry.equals("GUATEMALA") ||
                sCountry.equals("BELICE") ||
                sCountry.equals("MEXICO") ||
                sCountry.equals("CUBA") ||
                sCountry.equals("PUERTO RICO") ||
                sCountry.equals("SPAIN")) {
            sMailSub = "Búsqueda de partes GOOD NEW para Argentina. PN: ";
            if ( iConsultedPartsQTY > 1 ) {
                sMailSub = sMailSub + "Partes Múltiples";
            }
            else {
                for ( int i = 0; i<alGNSearchList.size(); i++ ) {
                    if ( alGNSearchList.get(i).getOrgCode().equals(sOrg) ) {
                        sMailSub = sMailSub + alGNSearchList.get(i).getPartNumber(); 
                    }
                }
            }
        }
        else {
            sMailSub = "Good New Search for Argentina. PN: ";
            if ( iConsultedPartsQTY > 1 ) {
                sMailSub = sMailSub + "Multiple Parts";
            }
            else {
                for ( int i = 0; i<alGNSearchList.size(); i++ ) {
                    if ( alGNSearchList.get(i).getOrgCode().equals(sOrg) ) {
                        sMailSub = sMailSub + alGNSearchList.get(i).getPartNumber(); 
                    }
                }
            }
        }
        sMailSub = sMailSub + ", Tracking No - ";
        return sMailSub;        
    }
    //</editor-fold>
    
    //Creates a String with the list of consulted parts depending on the Org
    private String getConsultedParts(String sCountry, String sOrg, String sTrack, boolean bFlag) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sParts = "";
        iConsultedPartsQTY = 0;
        for ( int i=0; i<alGNSearchList.size(); i++ ) {
            if ( alGNSearchList.get(i).getOrgCode().equals(sOrg) ) {
                alGNSearchList.get(i).setTracking(sTrack);
                if ( bFlag == false ){
                    sParts = sParts 
                        + "COUNTRY: " 
                        + alGNSearchList.get(i).getCountryName() 
                        + ", ORG: " 
                        + alGNSearchList.get(i).getOrgCode() 
                        + " (" 
                        + alGNSearchList.get(i).getTier() 
                        + ")  / PN: " 
                        + alGNSearchList.get(i).getPartNumber() 
                        + ", QTY: " 
                        + alGNSearchList.get(i).getQTY() 
                        + "\n";
                }
                else{
                    sUSAParts = sUSAParts 
                        + "COUNTRY: " 
                        + alGNSearchList.get(i).getCountryName() 
                        + ", ORG: " 
                        + alGNSearchList.get(i).getOrgCode() 
                        + " (" 
                        + alGNSearchList.get(i).getTier() 
                        + ")  / PN: " 
                        + alGNSearchList.get(i).getPartNumber() 
                        + ", QTY: " 
                        + alGNSearchList.get(i).getQTY() 
                        + "\n";
                }
                iConsultedPartsQTY = iConsultedPartsQTY + 1;
                
            }
        }
        return sParts;
    }
    //</editor-fold>
    
    //Dtermines the corresponding Country for the given Organization
    private String getCountry(String sOrg) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sCountry = "";
        for ( int i=0; i<alGNSearchList.size(); i++ ) {
            if ( alGNSearchList.get(i).getOrgCode().equals(sOrg) ) {
                sCountry = alGNSearchList.get(i).getCountryName();
            }
        }
        return sCountry;
    }
    //</editor-fold>
    
    //Dtermines the corresponding Org for the given Country
    private String getOrganization(String sCtry) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sOrg = "";
        for ( int i=0; i<alGNSearchList.size(); i++ ) {
            if ( alGNSearchList.get(i).getCountryName().equals(sCtry) ) {
                sOrg = alGNSearchList.get(i).getOrgCode();
            }
        }
        return sOrg;
    }
    //</editor-fold>
    
    //Creates the mail's body
    private String createConsultBodyMail(String sParts, String sCountry, String sMailTo) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        
        String sGreeting = "";
        String sMailBody = "";
        if ( sCountry.equals("ARGENTINA") ||
                sCountry.equals("URUGUAY") ||
                sCountry.equals("PARAGAY") ||
                sCountry.equals("BOLIVIA") ||
                sCountry.equals("CHILE") ||
                sCountry.equals("PERU") ||
                sCountry.equals("ECUADOR") ||
                sCountry.equals("COLOMBIA") ||
                sCountry.equals("VENEZUELA") ||
                sCountry.equals("PANAMA") ||
                sCountry.equals("COSTA RICA") ||
                sCountry.equals("NICARAGUA") ||
                sCountry.equals("HONDURAS") ||
                sCountry.equals("EL SALVADOR") ||
                sCountry.equals("GUATEMALA") ||
                sCountry.equals("BELICE") ||
                sCountry.equals("MEXICO") ||
                sCountry.equals("CUBA") ||
                sCountry.equals("PUERTO RICO") ||
                sCountry.equals("SPAIN")) {
            sGreeting = "Hola equipo DHL.\n\n";
            sMailBody = sGreeting
                + "Podrían por favor revisar en su bodega si las siguientes partes están marcadas solamente con fecha DOM?\n\n"
                + sParts + "\n"
                + "La etiqueta no debe contener ninguno de los siguientes:\n\n"
                + "\t• RDM Label\n"
                + "\t• SVD Label\n"
                + "\t• RE-LABELED label\n"
                + "\t• Certified Reutilized Parts\n"
                + "\t• SC number as 1418 or 2212\n"
                + "\t• RB and/or RC within the serial number\n\n"
                + "Ejemplos de partes  reacondicionadas o reparadas (no sirve):\n\n"
                + " \n\n"
                + "Ejemplo de una parte nueva cuya etiqueta muestra solamente la fecha DOM (sí sirve):\n\n"
                + " \n\n"
                + "Gracias;\n\n"
                + sName + "\n"
                + "Inventory Control Analyst\n"
                + "Oracle Spares Planning\n"
                + "Oracle Centroamerica. Parque Empresarial Forum 2, Santa Ana, Costa Rica.\n\n";
        }
        else {
            if ( sMailTo.contains("@BLUEWIN.CH") ||
                    sMailTo.contains("@vogtcargo.ch") ||
                    sMailTo.contains("@messenger.de") ||
                    sMailTo.contains("@panatlantic.ch") ){
                sGreeting = "Hello Team.\n\n";
            }
            else {
                sGreeting = "Hello DHL Team.\n\n";
            }
            sMailBody = sGreeting
                + "Could you please start a Good New Search (DOM label only) at your location for the following part numbers?\n\n"
                + sParts + "\n"
                + "Please confirm the label does not contain any of the following:\n\n"
                + "\t• RDM Label\n"
                + "\t• SVD Label\n"
                + "\t• RE-LABELED label\n"
                + "\t• Certified Reutilized Parts\n"
                + "\t• SC number as 1418 or 2212\n"
                + "\t• RB and/or RC within the serial number\n\n"
                + "Examples of refurbished/repaired parts:\n\n"
                + " \n\n"
                + "Example of New (DOM label only) parts:\n\n"
                + " \n\n"
                + "Regards;\n\n"
                + sName + "\n"
                + "Inventory Control Analyst\n"
                + "Oracle Spares Planning\n"
                + "Oracle Centroamerica. Parque Empresarial Forum 2, Santa Ana, Costa Rica.\n\n";
        }
        return sMailBody;
    }
    //</editor-fold>
    
    //Returns an unidimentional String Array with the list of different ORGs in the Consults Chart    
    private String[] getDifferentOrgs() {
        //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Creates a temporary array of 30 positions in order to store Orgs
        String[] saOrgs = new String[30];
        int iPos = 0;
        saOrgs[iPos] = alGNSearchList.get(0).getOrgCode();
        for ( int i=1; i<alGNSearchList.size(); i++  ) {
            if ( !alGNSearchList.get(i).getOrgCode().equals(saOrgs[iPos]) ) {
                iPos++;
                saOrgs[iPos] = alGNSearchList.get(i).getOrgCode();
            }
        }
        iPos++;
        saOrgs[iPos] = "END";
        int i = 0;
        do {
            System.out.println(saOrgs[i]);
            i++;
        } while ( !saOrgs[i].equals("END") );
        return saOrgs;
    }
    //</editor-fold>
    
    //Returns an unidimentional String Array with the list of different ORGs in the Consults Chart    
    private String[] getDifferentCountries() {
        //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Creates a temporary array of 30 positions in order to store Orgs
        String[] saCtrs = new String[30];
        int iPos = 0;
        saCtrs[iPos] = alGNSearchList.get(0).getCountryName();
        for ( int i=1; i<alGNSearchList.size(); i++  ) {
            if ( !alGNSearchList.get(i).getCountryName().equals(saCtrs[iPos]) ) {
                iPos++;
                saCtrs[iPos] = alGNSearchList.get(i).getCountryName();
            }
        }
        iPos++;
        saCtrs[iPos] = "END";
        int i = 0;
        do {
            System.out.println(saCtrs[i]);
            i++;
        } while ( !saCtrs[i].equals("END") );
        return saCtrs;
    }
    //</editor-fold>
    
    private void sendMail(String smailTo, String smailCC, String smailSub, String smailBody, String sCountry, String sOrg) throws IOException, URISyntaxException {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        cls_Mail_Manager tmpMail = new cls_Mail_Manager(smailTo, "NA", smailSub, smailBody, "Parts", sOrg);
        tmpMail.sendMail();
        
    }
    //</editor-fold>
    
    
    /*  *****JLISTS CONTROL RELATED METHODS***** */
        
    //Checks for the different Tiers in the provided data base and loads them into a selectable list
    private void loadTiersList(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        if ( iTier == -1 ) {
            JOptionPane.showMessageDialog(this, "The current data is invalid. Please import a valid excel file", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else {
            System.out.println("Loading data into Tiers list");
            jlstTiers.removeAll();
            cls_Data_Manager tmpDM = new cls_Data_Manager();
            sarTiers = tmpDM.getDifferentsBD(xlsDataMatrix, iTier);
            jlstTiers.add("N/A");
            int i=0;
            while ( !sarTiers[i].equals("n/a") ) {
                jlstTiers.add(sarTiers[i]);
                i++;            
            }
            System.out.println("Tiers list loaded");
        }
    }
    //</editor-fold>
    
    private void loadRegionsList(String sTier){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code" >    
        jlstRegions.removeAll();
        if ( sTier != null ){
            int j=0;
            int iRow = xlsDataMatrix.length;
            String tmpList[] = new String[iRow];
            for ( int i=0; i<iRow; i++ ){
                if ( xlsDataMatrix[i][iTier].equals(sTier) ){
                    tmpList[j] = xlsDataMatrix[i][iReg];
                    j++;
                }
            }
            //Removing duplicates from the tmpArray
            cls_Data_Manager tmpDM = new cls_Data_Manager();
            String aRegions[] = tmpDM.getDifferentsUD(tmpList);
            jlstRegions.add("N/A");
            int i=0;
            while ( !aRegions[i].equals("n/a") ) {
                jlstRegions.add(aRegions[i]);
                i++;            
            }
            System.out.println("Regions list completely loaded");
        }
    }
    //</editor-fold>
    
    private void loadCountriesList(String sTier, String sRegion){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code" >    
        jlstCountries.removeAll();
        if ( sRegion != null ){
            int j=0;
            int iRow = xlsDataMatrix.length;
            String tmpList[] = new String[iRow];
            for ( int i=0; i<iRow; i++ ){
                if ( xlsDataMatrix[i][iTier].equals(sTier) && xlsDataMatrix[i][iReg].equals(sRegion) ){
                    tmpList[j] = xlsDataMatrix[i][iCountry];
                    j++;
                }
            }
            //Removing duplicates from the tmpArray
            cls_Data_Manager tmpDM = new cls_Data_Manager();
            String aCountries[] = tmpDM.getDifferentsUD(tmpList);
            jlstCountries.add("N/A");
            int i=0;
            while ( !aCountries[i].equals("n/a") ) {
                jlstCountries.add(aCountries[i]);
                i++;            
            }
            System.out.println("Countries list completely loaded");
        }
    }
    //</editor-fold>
    
    private void loadPartsList(String sTier, String sRegion, String sCountry){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code" >    
        jlstParts.removeAll();
        if ( sCountry != null ){
            int j=0;
            int iRow = xlsDataMatrix.length;
            String tmpList[] = new String[iRow];
            for ( int i=0; i<iRow; i++ ){
                if ( xlsDataMatrix[i][iTier].equals(sTier) && xlsDataMatrix[i][iReg].equals(sRegion) && xlsDataMatrix[i][iCountry].equals(sCountry) ){
                    tmpList[j] = xlsDataMatrix[i][iPN];
                    j++;
                }
            }
            //Removing duplicates from the tmpArray
            cls_Data_Manager tmpDM = new cls_Data_Manager();
            String aParts[] = tmpDM.getDifferentsUD(tmpList);
            jlstParts.add("N/A");
            int i=0;
            while ( !aParts[i].equals("n/a") ) {
                jlstParts.add(aParts[i]);
                i++;            
            }
            System.out.println("Countries list completely loaded");
        }
    }
    //</editor-fold>
    
    private void loadOrgsList(String sTier, String sRegion, String sCountry, String sPart){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code" >    
        jlstOrgsTots.removeAll();
        if ( sPart != null ){
            int j=0;
            int iRow = xlsDataMatrix.length;
            String tmpOHList[] = new String[iRow];
            String tmpEXList[] = new String[iRow];
            for ( int i=0; i<iRow; i++ ){
                if ( xlsDataMatrix[i][iTier].equals(sTier) && xlsDataMatrix[i][iReg].equals(sRegion) && xlsDataMatrix[i][iCountry].equals(sCountry) && xlsDataMatrix[i][iPN].equals(sPart) ){
                    tmpOHList[j] = xlsDataMatrix[i][iOrgCode] + " - " + xlsDataMatrix[i][iOHTot];
                    tmpEXList[j] = xlsDataMatrix[i][iOrgCode] + " - " + xlsDataMatrix[i][iEXTot];
                    j++;
                }
            }
            //Removing duplicates from the tmpArrays
            cls_Data_Manager tmpDM = new cls_Data_Manager();
            String aOrgsOH[] = tmpDM.getDifferentsUD(tmpOHList);
            String aOrgsEX[] = tmpDM.getDifferentsUD(tmpEXList);
            int i=0;
            //Checks if the QTY to load are XS or OH
            if ( checkActivity() == 1 ){
                while ( !aOrgsEX[i].equals("n/a") ) {
                    jlstOrgsTots.add(aOrgsEX[i]);
                    i++;    
                }
            }
            else{
                while ( !aOrgsOH[i].equals("n/a") ) {
                    jlstOrgsTots.add(aOrgsOH[i]);
                    i++;    
                }
            }
            System.out.println("Orgs list completely loaded");
        }
    }
    //</editor-fold>
    
    //Cleans the dropdown lists in the selection tab
    private void CleanLists() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        jlstTiers.removeAll();
        jlstRegions.removeAll();
        jlstCountries.removeAll();
        jlstParts.removeAll();
        jlstOrgsTots.removeAll();
        jlstTasks.removeAll();
    }
    //</editor-fold>
    
    //Loads the list of opened tasks related with the highlighted PN#
    private void loadTasks(String sItem){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        jlstTasks.removeAll();
        for ( cls_BO_Data tmp: this.alBckordDB ){
            //Cheks if any part, on the "Part" or the "Alts" colum, has an active Good New Search
            //If it is so, it shows the related Task number on the screen
            if ( (tmp.getItem().equals(sItem) || tmp.getAlts().contains(sItem) ) && 
                    tmp.getBSta().equals("Good New Search") ){
                jlstTasks.add(tmp.getTask());
            }
        }
    }
    //</editor-fold>
    
    
    
    /*  *****VALIDATION RELATED METHODS***** */
    
    //Recognizes if the loaded Parts List xls file has the correct format to work
    private boolean validateXLSFile(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bFlag = true;
        if ( iReg == -1 || iCountry == -1 || iOrgName == -1 || iOrgCode == -1
                || iTier == -1 || iPN == -1 || iOHTot == -1 ){
            bFlag = false;
        }
        if ( bFlag == false ) {
            System.out.println("EXCEL FILE VALITATION FAILED: One or more columns were not found");
        }
        else {
            System.out.println("EXCEL FILE VALITATION PASSED");
        }
        return bFlag;        
    }
    //</editor-fold>
    
    //Recognizes if the loaded WebADI xls file has the correct format to work
    private boolean validateWebADIXLSFile(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bFlag = true;
        if ( iISO == -1 || 
                iItem == -1 || 
                iQTY == -1 || 
                iShipMeth == -1 || 
                iCreaDate == -1 || 
                iSrc == -1 || 
                iDes == -1 || 
                iSR == -1 ){
            bFlag = false;
        }
        if ( bFlag == false ) {
            System.out.println("EXCEL WEBADI FILE VALITATION FAILED: One or more columns were not found");
        }
        else {
            System.out.println("EXCEL WEBADI FILE VALITATION PASSED");
        }
        return bFlag;        
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
                iPLC_odsbo == -1 ){
            bFlag = false;
        }
        if ( bFlag == false ) {
            System.out.println("EXCEL ODS BACKORDERS FILE VALITATION FAILED: One or more columns were not found");
        }
        else {
            System.out.println("EXCEL ODS BACKORDERS FILE VALITATION PASSED");
        }
        return bFlag;        
    }
    //</editor-fold>
    
    //Recognizes if the loaded WebADI xls file has the correct format to work
    private boolean validatePPSEXLSFile(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bFlag = true;
        if ( iItem_ppse == -1 || 
                iPLC_ppse == -1 || 
                iDisp_ppse == -1 || 
                iCrit_ppse == -1 ){
            bFlag = false;
        }
        if ( bFlag == false ) {
            System.out.println("EXCEL PPSE FILE VALITATION FAILED: One or more columns were not found");
        }
        else {
            System.out.println("EXCEL PPSE FILE VALITATION PASSED");
        }
        return bFlag;        
    }
    //</editor-fold>
    
    
    
    /*  *****TIME MANAGEMENT RELATED METHODS***** */
    
  
    
    //Generates a date code of the current day in the format MMDDAAAA
    private String generateDateCode() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sDateCode = "MMDDAAA";
        Calendar cal = new GregorianCalendar();
        //Creating time variables for the date. Obtaining values form the System's clock
        String sDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String sMon = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String sYea = String.valueOf(cal.get(Calendar.YEAR));
        if ( sDay.length() == 1 ) { sDay = "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));}
        if ( sMon.length() == 1 ) { sMon = "0" + String.valueOf(cal.get(Calendar.MONTH) + 1);}
        sDateCode = sMon + sDay + sYea;
        return sDateCode;
    }
    //</editor-fold>
    
    
    private String getLastMailConsult() {
        String sLstCons = "";
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sLocCoDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("CREATED MAILS") )
            {
                chain = br.readLine();
            }
            sLstCons = br.readLine();
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
        return sLstCons;
    }
    
    private void setLastMailConsult() throws IOException {
        int iCount = Integer.valueOf(getLastMailConsult());
        File fDataBase;
        fDataBase = new File (sLocCoDBPath); //points to the local .txt data base file
        //Writing vars
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter wr = null;
        fw = new FileWriter(fDataBase);
        bw = new BufferedWriter(fw);
        wr = new PrintWriter(bw);
        try
        {
           
            //Reads, line by line, all the consults that are currently in the Data Base Array List
            for(cls_PartDataReq tmp: this.alCosulDB)
            {
                wr.println( tmp.getTier() + "\t" 
                        + tmp.getRegion() + "\t" 
                        + tmp.getCountryName() + "\t" 
                        + tmp.getOrgCode() + "\t" 
                        + tmp.getPartNumber() + "\t" 
                        + tmp.getQTY() + "\t" 
                        + tmp.getActivity() + "\t" 
                        + tmp.getTotalOH() + "\t" 
                        + tmp.getTotalXS() + "\t" 
                        + tmp.getCurrentDate() + "\t" 
                        + tmp.getDOM() + "\t" 
                        + tmp.getPartMoved() + "\t"
                        + tmp.getTask() + "\t"
                        + tmp.getTracking() );
            }
            iCoQTY = alCosulDB.size();
            wr.println("CREATED CONSULTS");
            wr.println(String.valueOf(iCoQTY));
            wr.println("CREATED MAILS");
            wr.println(iCount + 1);
            wr.println(this.sUser);//It writes the e-mail of the last User who logged into the DB
            iMaQTY = iCount + 1;
            this.jlblTickets.setText(String.valueOf(iCoQTY));
            wr.close();
            bw.close();
            fw.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    
    
    //Saves the all data from the Consults JTable into the Consults ArrayList
    //Updates the local .txt Data Base file with the new consults
    public void SaveConsultsData()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        String sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sTsk="", sDat="";
        int i = 0;
        for ( i = 0; i < jtblConsults.getRowCount(); i++ )
        {
            sTir = jtblConsults.getValueAt(i, 0).toString();
            sReg = jtblConsults.getValueAt(i, 1).toString();
            sCnt = jtblConsults.getValueAt(i, 2).toString();
            sOrg = jtblConsults.getValueAt(i, 3).toString();
            sPrt = jtblConsults.getValueAt(i, 4).toString();
            sQty = jtblConsults.getValueAt(i, 5).toString();
            sAct = jtblConsults.getValueAt(i, 6).toString();
            sGOH = jtblConsults.getValueAt(i, 7).toString();
            sGXS = jtblConsults.getValueAt(i, 8).toString();
            sTsk = jtblConsults.getValueAt(i, 9).toString();
            sDat = jtblConsults.getValueAt(i, 10).toString();
            alCosulDB.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, "NA", "NA", sTsk, "NA", "NA"));
        }
        updateConsultsTXTDataBase();
    }
    //</editor-fold>
    
    //Gets the information from each consult and stores it into a temporary Array List
    private ArrayList<cls_PartDataReq> captureConsult(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        ArrayList<cls_PartDataReq> alConsultCaptured = new ArrayList<>();
        alConsultCaptured.clear();
        cls_PartDataReq tmpConsul;
        String sTr, sRg, sCt, sOr,sPt, sQy, sAc, sOH = "NA", sXS = "NA", sTk;
        cls_Date_Manager tmpDM = new cls_Date_Manager();
        String sDt = tmpDM.getCurrentDate_yyyymmdd();
        String OrgsSelection[] = jlstOrgsTots.getSelectedItems();
        for ( int i=0; i<OrgsSelection.length; i++  ){
            sTr = this.jlstTiers.getSelectedItem();
            sRg = this.jlstRegions.getSelectedItem();
            sCt = this.jlstCountries.getSelectedItem();
            sOr = OrgsSelection[i].substring(0,3);
            sPt = this.jlstParts.getSelectedItem();
            sQy = OrgsSelection[i].substring(6);
            if ( checkActivity() == 1 ) {sAc = "Replenishment";} else {sAc = "Backorders";}
            for ( int j=0; j<xlsDataMatrix.length; j++ ){
                if ( xlsDataMatrix[j][iTier].equals(sTr) && xlsDataMatrix[j][iReg].equals(sRg) && xlsDataMatrix[j][iCountry].equals(sCt) 
                        && xlsDataMatrix[j][iPN].equals(sPt) && xlsDataMatrix[j][iOrgCode].equals(sOr) ){
                    System.out.println("Match found");
                    sOH = xlsDataMatrix[j][iOHTot];
                    sXS = xlsDataMatrix[j][iEXTot];
                }
            }
            if ( jlstTasks.getSelectedIndex() == -1 ){sTk = "NA";} else {sTk = this.jlstTasks.getSelectedItem();}
            tmpConsul = new cls_PartDataReq(sTr, sRg, sCt, sOr, sPt, sQy, sAc, sOH, sXS, sDt, "NA", "NA", sTk, "NA", "NA");
            //Checks if the consult is already created as part of the current group of new consults
            if ( findConsult(tmpConsul) == false ){
                alConsultCaptured.add(tmpConsul);
            }
            else{
                JOptionPane.showMessageDialog(this, "The consult for PN: " + tmpConsul.getPartNumber() + " at "  + tmpConsul.getOrgCode() + "-" + tmpConsul.getCountryName() + 
                        " will not be added as it is already listed.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        return alConsultCaptured;
    }
    //</editor-fold>
    
    //Updates the new consults ArrayList with the entry
    //Refreshes the consult list chart with the values on the ArrayList
    private void updateConsultList(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        ArrayList<cls_PartDataReq> alCurrentConsult = captureConsult();
        for ( cls_PartDataReq tmp: alCurrentConsult ){
            alGNSearchList.add(tmp);
        }
        for ( cls_PartDataReq tmp: alGNSearchList ){
            System.out.println("Tier: " + tmp.getTier() + " Region: " + tmp.getRegion() + " Country: " + tmp.getCountryName() + " Part: " + tmp.getPartNumber()
                + " Org: " + tmp.getOrgCode() + " QTY: " + tmp.getQTY() + " Activity: " + tmp.getActivity() + " On-Hand: " + tmp.getTotalOH() + " Excess: " + tmp.getTotalXS() 
                    + " Task: " + tmp.getTask() + " Date: " + tmp.getCurrentDate());
        }
        jlblConsCount.setText(String.valueOf(alGNSearchList.size()));
    }
    //</editor-fold>     
    
    //Sends the list of new consults to the existing Consults Data base and updates the corresponding .TXT file
    private void sendConsultsToDB (){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sTsk="";
        for ( cls_PartDataReq tmp : alGNSearchList ){
            alCosulDB.add(new cls_PartDataReq(tmp.getTier(),
                    tmp.getRegion(),
                    tmp.getCountryName(),
                    tmp.getOrgCode(),
                    tmp.getPartNumber(),
                    tmp.getQTY(),
                    tmp.getActivity(),
                    tmp.getTotalOH(),
                    tmp.getTotalXS(),
                    tmp.getCurrentDate(),
                    "NA",
                    "NA",
                    tmp.getTask(),
                    tmp.getTracking(),
                    "NA"));
            //Updates the Tracking number in the Backorders ArrayList Data Base
            updateBackordersTracking(tmp.getTask(), tmp.getPartNumber(), tmp.getTracking());
        }
        if ( bONLINE == true ){
            uploadRemConsDB();
            uploadRemBackordersDB();
        }
        else{
            updateConsultsTXTDataBase();
            updateBackordersTXTDataBase();
        }
    }
    //</editor-fold>
    
    //Updates the Tracking number into the corresponding Task Line on the Backorders Data base
    //If the line has any previous tracking number, it adds the new after a ">" char
    private void updateBackordersTracking(String sTask, String sPart, String sTrack){
        for ( int i=0; i<alBckordDB.size(); i++ ){
            if ( alBckordDB.get(i).getTask().equals(sTask) ){
                if ( alBckordDB.get(i).getItem().equals(sPart) || alBckordDB.get(i).getAlts().contains(sPart) ){
                    if ( alBckordDB.get(i).getTrak().equals("NA") ){
                    alBckordDB.get(i).setTrak(sTrack);
                    }
                    else{
                        alBckordDB.get(i).setTrak(alBckordDB.get(i).getTrak() + ">" + sTrack);
                    }
                }
            }
        }
    }
    
    
    
    
    //Looks for a consult into the exisiting ArrayList of new consults and indicates if it is already there or not
    private boolean findConsult(cls_PartDataReq tmpCons){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean consultFound = false;
        for ( cls_PartDataReq tmp: alGNSearchList ){
            if( tmpCons.getTier().equals(tmp.getTier()) &&
                    tmpCons.getRegion().equals(tmp.getRegion()) &&
                    tmpCons.getCountryName().equals(tmp.getCountryName()) &&
                    tmpCons.getOrgCode().equals(tmp.getOrgCode()) &&
                    tmpCons.getPartNumber().equals(tmp.getPartNumber()) &&
                    tmpCons.getQTY().equals(tmp.getQTY()) &&
                    tmpCons.getActivity().equals(tmp.getActivity()) &&
                    tmpCons.getTotalOH().equals(tmp.getTotalOH()) &&
                    tmpCons.getTotalXS().equals(tmp.getTotalXS()) &&
                    tmpCons.getTask().equals(tmp.getTask()) &&
                    tmpCons.getCurrentDate().equals(tmp.getCurrentDate()) ){
                consultFound = true;
            }
        }
        return consultFound;
    }
    //</editor-fold>
    
    //Looks for a consult object into the exisiting ArrayList of new consults and return its position
    private int findConsultPos(cls_PartDataReq tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i=0; i<this.alGNSearchList.size(); i++ ){
            if ( tmpCons.getTier().equals(this.alGNSearchList.get(i).getTier()) && 
                    tmpCons.getRegion().equals(this.alGNSearchList.get(i).getRegion()) &&
                    tmpCons.getCountryName().equals(this.alGNSearchList.get(i).getCountryName()) &&
                    tmpCons.getOrgCode().equals(this.alGNSearchList.get(i).getOrgCode()) &&
                    tmpCons.getPartNumber().equals(this.alGNSearchList.get(i).getPartNumber()) &&
                    tmpCons.getQTY().equals(this.alGNSearchList.get(i).getQTY()) &&
                    tmpCons.getActivity().equals(this.alGNSearchList.get(i).getActivity()) &&
                    tmpCons.getTotalOH().equals(this.alGNSearchList.get(i).getTotalOH()) &&
                    tmpCons.getTotalXS().equals(this.alGNSearchList.get(i).getTotalXS()) &&
                    tmpCons.getTask().equals(this.alGNSearchList.get(i).getTask())){
                iPos = i;
            }
        }
        return iPos;
    }
    //</editor-fold>
    
    //Looks for a consult object into the exisiting Consults Data Base (this is the historical DB) and return its position
    private int findConsultDBPos(cls_PartDataReq tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i=0; i<this.alCosulDB.size(); i++ ){
            if ( tmpCons.getTier().equals(this.alCosulDB.get(i).getTier()) && 
                    tmpCons.getRegion().equals(this.alCosulDB.get(i).getRegion()) &&
                    tmpCons.getCountryName().equals(this.alCosulDB.get(i).getCountryName()) &&
                    tmpCons.getOrgCode().equals(this.alCosulDB.get(i).getOrgCode()) &&
                    tmpCons.getPartNumber().equals(this.alCosulDB.get(i).getPartNumber()) &&
                    tmpCons.getQTY().equals(this.alCosulDB.get(i).getQTY()) &&
                    tmpCons.getActivity().equals(this.alCosulDB.get(i).getActivity()) &&
                    tmpCons.getTotalOH().equals(this.alCosulDB.get(i).getTotalOH()) &&
                    tmpCons.getTotalXS().equals(this.alCosulDB.get(i).getTotalXS()) &&
                    tmpCons.getCurrentDate().equals(this.alCosulDB.get(i).getCurrentDate()) &&
                    tmpCons.getDOM().equals(this.alCosulDB.get(i).getDOM()) &&
                    tmpCons.getPartMoved().equals(this.alCosulDB.get(i).getPartMoved()) &&
                    tmpCons.getTask().equals(this.alCosulDB.get(i).getTask()) &&
                    tmpCons.getTracking().equals(this.alCosulDB.get(i).getTracking()) ){
                iPos = i;
            }
        }
        return iPos;
    }
    //</editor-fold>
    
    //Looks for an object into the exisiting Backorders ArrayList DB and return its position
    private int findBODBPos(cls_BO_Data tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i=0; i<this.alBckordDB.size(); i++ ){
            if ( tmpCons.getBSta().equals(this.alBckordDB.get(i).getBSta()) && 
                    tmpCons.getDate().equals(this.alBckordDB.get(i).getDate()) &&
                    tmpCons.getSvRq().equals(this.alBckordDB.get(i).getSvRq()) &&
                    tmpCons.getTask().equals(this.alBckordDB.get(i).getTask()) &&
                    tmpCons.getISO().equals(this.alBckordDB.get(i).getISO()) &&
                    tmpCons.getItem().equals(this.alBckordDB.get(i).getItem()) &&
                    tmpCons.getQty().equals(this.alBckordDB.get(i).getQty()) &&
                    tmpCons.getDesc().equals(this.alBckordDB.get(i).getDesc()) &&
                    tmpCons.getTkSt().equals(this.alBckordDB.get(i).getTkSt()) &&
                    tmpCons.getPLC().equals(this.alBckordDB.get(i).getPLC()) &&
                    tmpCons.getCrit().equals(this.alBckordDB.get(i).getCrit()) &&
                    tmpCons.getCond().equals(this.alBckordDB.get(i).getCond()) &&
                    tmpCons.getSrAs().equals(this.alBckordDB.get(i).getSrAs()) &&
                    tmpCons.getAlts().equals(this.alBckordDB.get(i).getAlts()) &&
                    tmpCons.getComm().equals(this.alBckordDB.get(i).getComm()) &&
                    tmpCons.getISO1().equals(this.alBckordDB.get(i).getISO1()) &&
                    tmpCons.getAwb1().equals(this.alBckordDB.get(i).getAwb1()) &&
                    tmpCons.getISO2().equals(this.alBckordDB.get(i).getISO2()) &&
                    tmpCons.getAwb2().equals(this.alBckordDB.get(i).getAwb2()) &&
                    tmpCons.getISO3().equals(this.alBckordDB.get(i).getISO3()) &&
                    tmpCons.getAwb3().equals(this.alBckordDB.get(i).getAwb3()) &&
                    tmpCons.getIsMB().equals(this.alBckordDB.get(i).getIsMB()) &&
                    tmpCons.getAwMB().equals(this.alBckordDB.get(i).getAwMB()) &&
                    tmpCons.getSIMI().equals(this.alBckordDB.get(i).getSIMI()) &&
                    tmpCons.getTkNt().equals(this.alBckordDB.get(i).getTkNt()) &&
                    tmpCons.getBOMT().equals(this.alBckordDB.get(i).getBOMT()) &&
                    tmpCons.getTrak().equals(this.alBckordDB.get(i).getTrak()) ){
                iPos = i;
            }
        }
        return iPos;
    }
    //</editor-fold>
    
    //Looks for an object into the exisiting WebADI ArrayList DB and return its position
    private int findWebADIDBPos(cls_WebADI_Data tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i=0; i<this.alWebadiDB.size(); i++ ){
            if ( tmpCons.getDat().equals(this.alWebadiDB.get(i).getDat()) && 
                    tmpCons.getItm().equals(this.alWebadiDB.get(i).getItm()) &&
                    tmpCons.getQTY().equals(this.alWebadiDB.get(i).getQTY()) &&
                    tmpCons.getFrm().equals(this.alWebadiDB.get(i).getFrm()) &&
                    tmpCons.getDst().equals(this.alWebadiDB.get(i).getDst()) &&
                    tmpCons.getShpMet().equals(this.alWebadiDB.get(i).getShpMet()) &&
                    tmpCons.getRef().equals(this.alWebadiDB.get(i).getRef()) &&
                    tmpCons.getISO().equals(this.alWebadiDB.get(i).getISO()) &&
                    tmpCons.getAwb().equals(this.alWebadiDB.get(i).getAwb()) &&
                    tmpCons.getSta().equals(this.alWebadiDB.get(i).getSta()) &&
                    tmpCons.getAct().equals(this.alWebadiDB.get(i).getAct()) &&
                    tmpCons.getTsk().equals(this.alWebadiDB.get(i).getTsk()) &&
                    tmpCons.getSMI().equals(this.alWebadiDB.get(i).getSMI()) &&
                    tmpCons.getCom().equals(this.alWebadiDB.get(i).getCom()) ){
                iPos = i;
            }
        }
        return iPos;
    }
    //</editor-fold>
    
    
    //Looks for a current Consult into the historical list of consults. 
    //Returns the most recent consult position.
    private int findOldConsultPos(cls_PartDataReq tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iPos = -1;
        for ( int i=alCosulDB.size()-1; i>-1; i-- ){
            if ( tmpCons.getTier().equals(this.alCosulDB.get(i).getTier()) && 
                    tmpCons.getRegion().equals(this.alCosulDB.get(i).getRegion()) &&
                    tmpCons.getCountryName().equals(this.alCosulDB.get(i).getCountryName()) &&
                    tmpCons.getOrgCode().equals(this.alCosulDB.get(i).getOrgCode()) &&
                    tmpCons.getPartNumber().equals(this.alCosulDB.get(i).getPartNumber()) &&
                    tmpCons.getActivity().equals(this.alCosulDB.get(i).getActivity())){
                System.out.println("ENTRY FOUND! POS: " + i);
                iPos = i;
                break;
            }
        }
        return iPos;
    }
    //</editor-fold>
    
    //Captures the data in the highlighted line on the new Consults screen and returns an Object of data type
    private cls_PartDataReq captureConsultLine(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        int iLine = this.jtblConsults.getSelectedRow();
        String sTr = this.jtblConsults.getValueAt(iLine, 0).toString();
        String sRg = this.jtblConsults.getValueAt(iLine, 1).toString();
        String sCn = this.jtblConsults.getValueAt(iLine, 2).toString();
        String sOr = this.jtblConsults.getValueAt(iLine, 3).toString();
        String sPn = this.jtblConsults.getValueAt(iLine, 4).toString();
        String sQy = this.jtblConsults.getValueAt(iLine, 5).toString();
        String sAc = this.jtblConsults.getValueAt(iLine, 6).toString();
        String sOH = this.jtblConsults.getValueAt(iLine, 7).toString();
        String sXS = this.jtblConsults.getValueAt(iLine, 8).toString();
        String sTk = this.jtblConsults.getValueAt(iLine, 9).toString();
        String sDt = this.jtblConsults.getValueAt(iLine, 10).toString();
        cls_PartDataReq tmpCons = new cls_PartDataReq(sTr, sRg, sCn, sOr, sPn, sQy, sAc, sOH, sXS, sDt, "NA", "NA", sTk, "NA", "NA");
        return tmpCons;
    }
    //</editor-fold>
    
    //Captures the data in the highlighted line on the DB screen and returns an Object of data type
    private cls_PartDataReq captureDataBaseLine(int iRow) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        cls_PartDataReq ptrTMP = new cls_PartDataReq();
        ptrTMP.setTier(this.jtblDataBase.getValueAt(iRow, 0).toString());
        ptrTMP.setRegion(this.jtblDataBase.getValueAt(iRow, 1).toString());
        ptrTMP.setCountryName(this.jtblDataBase.getValueAt(iRow, 2).toString());
        ptrTMP.setOrgCode(this.jtblDataBase.getValueAt(iRow, 3).toString());
        ptrTMP.setPartNumber(this.jtblDataBase.getValueAt(iRow, 4).toString());
        ptrTMP.setQTY(this.jtblDataBase.getValueAt(iRow, 5).toString());
        ptrTMP.setActivity(this.jtblDataBase.getValueAt(iRow, 6).toString());
        ptrTMP.setTotalOH(this.jtblDataBase.getValueAt(iRow, 7).toString());
        ptrTMP.setTotalXS(this.jtblDataBase.getValueAt(iRow, 8).toString());
        ptrTMP.setCurrentDate(this.jtblDataBase.getValueAt(iRow, 9).toString());
        ptrTMP.setDOM(this.jtblDataBase.getValueAt(iRow, 10).toString());
        ptrTMP.setPartMoved(this.jtblDataBase.getValueAt(this.jtblDataBase.getSelectedRow(), 11).toString());
        ptrTMP.setTask(this.jtblDataBase.getValueAt(iRow, 12).toString());
        ptrTMP.setTracking(this.jtblDataBase.getValueAt(iRow, 13).toString());
        ptrTMP.setPosition("NA");
        return ptrTMP;
    }
    //</editor-fold>
    
    //Captures the data in the highlighted line on the Backorders screen and returns an Object of data type
    private cls_BO_Data captureBOLine(int iRow) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        cls_BO_Data ptrTMP = new cls_BO_Data();
        ptrTMP.setBSta(this.jtblBackorders.getValueAt(iRow, 0).toString());
        ptrTMP.setDate(this.jtblBackorders.getValueAt(iRow, 1).toString());
        ptrTMP.setSvRq(this.jtblBackorders.getValueAt(iRow, 2).toString());
        ptrTMP.setTask(this.jtblBackorders.getValueAt(iRow, 3).toString());
        ptrTMP.setISO(this.jtblBackorders.getValueAt(iRow, 4).toString());
        ptrTMP.setItem(this.jtblBackorders.getValueAt(iRow, 5).toString());
        ptrTMP.setQty(this.jtblBackorders.getValueAt(iRow, 6).toString());
        ptrTMP.setDesc(this.jtblBackorders.getValueAt(iRow, 7).toString());
        ptrTMP.setTkSt(this.jtblBackorders.getValueAt(iRow, 8).toString());
        ptrTMP.setPLC(this.jtblBackorders.getValueAt(iRow, 9).toString());
        ptrTMP.setCrit(this.jtblBackorders.getValueAt(iRow, 10).toString());
        ptrTMP.setCond(this.jtblBackorders.getValueAt(iRow, 11).toString());
        ptrTMP.setSrAs(this.jtblBackorders.getValueAt(iRow, 12).toString());
        ptrTMP.setAlts(this.jtblBackorders.getValueAt(iRow, 13).toString());
        ptrTMP.setComm(this.jtblBackorders.getValueAt(iRow, 14).toString());
        ptrTMP.setISO1(this.jtblBackorders.getValueAt(iRow, 15).toString());
        ptrTMP.setAwb1(this.jtblBackorders.getValueAt(iRow, 16).toString());
        ptrTMP.setISO2(this.jtblBackorders.getValueAt(iRow, 17).toString());
        ptrTMP.setAwb2(this.jtblBackorders.getValueAt(iRow, 18).toString());
        ptrTMP.setISO3(this.jtblBackorders.getValueAt(iRow, 19).toString());
        ptrTMP.setAwb3(this.jtblBackorders.getValueAt(iRow, 20).toString());
        ptrTMP.setIsMB(this.jtblBackorders.getValueAt(iRow, 21).toString());
        ptrTMP.setAwMB(this.jtblBackorders.getValueAt(iRow, 22).toString());
        ptrTMP.setSIMI(this.jtblBackorders.getValueAt(iRow, 23).toString());
        ptrTMP.setTkNt(this.jtblBackorders.getValueAt(iRow, 24).toString());
        ptrTMP.setBOMT(this.jtblBackorders.getValueAt(iRow, 25).toString());
        ptrTMP.setTrak(this.jtblBackorders.getValueAt(iRow, 26).toString());
        ptrTMP.setPosi("NA");
        ptrTMP.setZone("NA");
        ptrTMP.setCtry("NA");
        return ptrTMP;
    }
    //</editor-fold>
    
    //Captures the data in the highlighted line on the WebADI DB screen and returns an Object of data type
    private cls_WebADI_Data captureWebADILine(int iRow) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        cls_WebADI_Data ptrTMP = new cls_WebADI_Data();
        ptrTMP.setDat(this.jtblWebADI.getValueAt(iRow, 0).toString());
        ptrTMP.setItm(this.jtblWebADI.getValueAt(iRow, 1).toString());
        ptrTMP.setQTY(this.jtblWebADI.getValueAt(iRow, 2).toString());
        ptrTMP.setFrm(this.jtblWebADI.getValueAt(iRow, 3).toString());
        ptrTMP.setDst(this.jtblWebADI.getValueAt(iRow, 4).toString());
        ptrTMP.setShpMet(this.jtblWebADI.getValueAt(iRow, 5).toString());
        ptrTMP.setRef(this.jtblWebADI.getValueAt(iRow, 6).toString());
        ptrTMP.setISO(this.jtblWebADI.getValueAt(iRow, 7).toString());
        ptrTMP.setAwb(this.jtblWebADI.getValueAt(iRow, 8).toString());
        ptrTMP.setSta(this.jtblWebADI.getValueAt(iRow, 9).toString());
        ptrTMP.setAct(this.jtblWebADI.getValueAt(iRow, 10).toString());
        ptrTMP.setTsk(this.jtblWebADI.getValueAt(iRow, 11).toString());
        ptrTMP.setSMI(this.jtblWebADI.getValueAt(iRow, 12).toString());
        ptrTMP.setCom(this.jtblWebADI.getValueAt(iRow, 13).toString());
        ptrTMP.setCom(this.jtblWebADI.getValueAt(iRow, 14).toString());
        ptrTMP.setPos("NA");
        ptrTMP.setXX1("NA");
        ptrTMP.setXX2("NA");
        return ptrTMP;
    }
    //</editor-fold>
    
    //Counts the number of times a consult may exist into the historical Data Base
    private int countPreviousConsults(cls_PartDataReq tmpCons) {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iCount = 0;
        for ( cls_PartDataReq tmp : alCosulDB ) {
            if ( tmp.getTier().equals(tmpCons.getTier()) &&
                    tmp.getRegion().equals(tmpCons.getRegion()) &&
                    tmp.getCountryName().equals(tmpCons.getCountryName()) &&
                    tmp.getOrgCode().equals(tmpCons.getOrgCode()) &&
                    tmp.getPartNumber().equals(tmpCons.getPartNumber()) &&
                    tmp.getActivity().equals(tmpCons.getActivity())) {
                iCount = iCount + 1;
            }
        }
        return iCount;
    }
    //</editor-fold>
    
    
    //Gets the complete list of mail tracking numbers for any specific line on the Backorders Data Base
    private void getMailTrackings(){
    //<editor-fold defaultstate="collpased" desc="Method Source Code">    
        String sTrackings = "";
        int iRow = jtblBackorders.getSelectedRow();
        sTrackings = jtblBackorders.getValueAt(iRow, 26).toString().replaceAll(">","\n");
        System.out.println(sTrackings);
        gui_InfoNotes tmpIN = new gui_InfoNotes("LIST OF RELATED MAIL TRACKINGS: \n\n" + sTrackings);
        tmpIN.setLocationRelativeTo(this);
        tmpIN.setTitle("MAIL TRACKING NUMBERS");
        tmpIN.setVisible(true);
    }
    //</editor-fold>
    
    
    
    
    
    //OBSOLETE METHODS    
    //<editor-fold defaultstate="collapsed" desc="Unused methods">
    
    //ReplaceS "|" with "/" on the BO DB mail title
    private void replaceSlash(){
        for ( cls_BO_Data tmp: this.alBckordDB ){
            tmp.setBOMT(tmp.getBOMT().replace('|','/'));
        }
        this.cleanBackordersTable();
        this.loadBackordersTable();
    }
    
    
    //Replaced "Replishment" with "Replishment on the WebADI DB
    private void cleanWrongvalue(){
        for ( cls_WebADI_Data tmp: this.alWebadiDB ){
            if ( tmp.getAct().equals("Replishement") ){
                tmp.setAct("Replenishment");
            }
        }
        this.cleanWebADITable();
        this.loadWebADITable();
    }
    
    
    
    private void updateTracking(String sOrg, String sTrack){
        int iConsQTY = alGNSearchList.size();
        //Checks the Historical DB Array from the very last entry until the first consult created today
        for ( int i=alCosulDB.size()-1; i>alCosulDB.size()-iConsQTY-1; i-- ){
            if ( alCosulDB.get(i).getOrgCode().equals(sOrg) ) {
                alCosulDB.get(i).setTracking(sTrack);
            }
        }
        this.updateConsultsTXTDataBase();
        this.loadConsultsDBTable();
    }
    
    
    //Cleans blank spaces on a given XLS Matrix and writes "NA" instead
    private String[][] cleanMatrixBlanks(String[][] tmpMatrix){
        for ( int r = 0; r<tmpMatrix.length; r++ ){
            for ( int c = 0; c<tmpMatrix[0].length; c++ ){
                if ( tmpMatrix[r][c].equals("") ){
                    tmpMatrix[r][c] = "NA";
                }
            }
        }
        return tmpMatrix;
    }
    
    
    //Gets the current date from the System's clock with format "dd-MMM-yy"
    private String getCurrentDate(){
        String sCurrentTime;
        Calendar cal = new GregorianCalendar();
        //Creating time variables for the date. Obtaining values form the System's clock
        String sDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String sMon = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String sYea = String.valueOf(cal.get(Calendar.YEAR));
        //Defines the Month name depending on the Month number
        switch (sMon){
            case "1": sMon = "JAN"; break;
            case "2": sMon = "FEB"; break;
            case "3": sMon = "MAR"; break;
            case "4": sMon = "APR"; break;
            case "5": sMon = "MAY"; break;
            case "6": sMon = "JUN"; break;
            case "7": sMon = "JUL"; break;
            case "8": sMon = "AUG"; break;
            case "9": sMon = "SEP"; break;
            case "10": sMon = "OCT"; break;
            case "11": sMon = "NOV"; break;
            case "12": sMon = "DEC"; break;
        }
        //Prepares the text to be displayed in the clock label on the main screen
        sCurrentTime = sDay + "-" + sMon + "-" + sYea /*+ " " + sHrs + ":" + sMns*/;
        return sCurrentTime;
    }
    
    private void tmpWebADIDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        this.alWebadiDB.clear();
        this.iWaQTY = 0;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sDat="", sItm="", sQty="", sFrm="", sDst="", sShpMet="", sRef="", sISO="", sAwb="", sSta="", sAct="", sTsk="", sSMI="", sCIB="", sCom="";
        try
        {
            fDataBase = new File(sLocWaDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("WEBADI LINES") )
            {
                String [] position = chain.split("\t");
                sDat = position[0];
                sItm = position[1];
                sQty = position[2];
                sFrm = position[3];        
                sDst = position[4];
                sShpMet = position[5];
                sRef = position[6];
                sISO = position[7];
                sAwb = position[8];
                sSta = position[9];
                sAct = position[10];
                sTsk = position[11];
                sSMI = position[12];
                sCom = position[13];
                alWebadiDB.add(new cls_WebADI_Data(sDat, sItm, sQty, sFrm, sDst, sShpMet, sRef, sISO, sAwb, sSta, sAct, sTsk, sSMI, "NA", sCom, "NA", "NA", "NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iWaQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the WebADI local Data Base\n"
                    + "Method: loadWebADIDB()\n" + e, "DB RIGHTHAND", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    private void tmpConsultDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        this.alCosulDB.clear();
        this.iCoQTY = 0;
        this.iMaQTY = 0;
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain, sTir="", sReg="", sCnt="", sOrg="", sPrt="", sQty="", sAct="", sGOH="", sGXS="", sDat="", sDOM="", sPrtMvd="", sTsk="", sTracking="";
        
        try
        {
            fDataBase = new File(sLocCoDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("CREATED CONSULTS") )
            {
                String [] position = chain.split("\t");
                sTir = position[0];
                sReg = position[1];
                sCnt = position[2];
                sOrg = position[3];        
                sPrt = position[4];
                sQty = position[5];
                sAct = position[6];
                sGOH = position[7];
                sGXS = position[8];
                sDat = position[9];
                sDOM = position[10];
                sPrtMvd = position[11];
                sTracking = position[12];
                alCosulDB.add(new cls_PartDataReq(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sPrtMvd, "NA", sTracking, "NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iCoQTY = Integer.valueOf(chain);
            chain = br.readLine();
            chain = br.readLine();
            iMaQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
            alCosulDB = updateArrayListNames(alCosulDB);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n"
                    + "Method: loadConsultDB()\n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
        }
    }
    //</editor-fold>
    
    private void reformatBackorders(){
        for ( cls_BO_Data tmp : alBckordDB ){
            tmp.setAlts(tmp.getAlts().replace('|','>'));
            //tmp.setAlts(tmp.getAlts().replace('>',';'));
            tmp.setAlts(tmp.getAlts().replaceAll(" ",""));
            tmp.setComm(tmp.getComm().replace('\n','>'));
            tmp.setTrak(tmp.getTrak().replace('\n','>'));
        }
        this.cleanBackordersTable();
        this.loadBackordersTable();
    }
    
    private void formatDateConsults(){
        cls_Date_Manager tmpDM = new cls_Date_Manager();
        for ( int i=0; i<this.alCosulDB.size(); i++ ){
            if (  !alCosulDB.get(i).getCurrentDate().contains("2017-") ){
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replace('-', '/'));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("JAN", "01"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("FEB", "02"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("MAR", "03"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("APR", "04"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("MAY", "05"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("JUN", "06"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("JUL", "07"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("AUG", "08"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("SEP", "09"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("OCT", "10"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("NOV", "11"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("DEC", "12"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("/13", "/2013"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("/14", "/2014"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("/15", "/2015"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("/16", "/2016"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i).getCurrentDate().replaceAll("/17", "/2017"));
                alCosulDB.get(i).setCurrentDate(alCosulDB.get(i) + "  1:00:00");
                alCosulDB.get(i).setCurrentDate(tmpDM.formatDate_yyyyMMdd(tmpDM.convertDDMMYYYY_toDate(alCosulDB.get(i).getCurrentDate())));
            }
        }
        this.cleanConsultsDBTable();
        this.loadConsultsDBTable();
    }
    
    private void formatDateBODB(){
        cls_Date_Manager tmpDM = new cls_Date_Manager();
        for ( int i=0; i<this.alBckordDB.size(); i++ ){
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replace('-', '/'));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Jan", "01"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Feb", "02"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Mar", "03"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Apr", "04"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("May", "05"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Jun", "06"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Jul", "07"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Aug", "08"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Sep", "09"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Oct", "10"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Nov", "11"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("Dec", "12"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("/13", "/2013"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("/14", "/2014"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("/15", "/2015"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("/16", "/2016"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate().replaceAll("/17", "/2017"));
            alBckordDB.get(i).setDate(alBckordDB.get(i).getDate() + "  1:00:00");
            alBckordDB.get(i).setDate(tmpDM.formatDate_yyyyMMdd(tmpDM.convertDDMMYYYY_toDate(alBckordDB.get(i).getDate())));
        }
        this.cleanBackordersTable();
        this.loadBackordersTable();
    }
    
    private void formatDateWebADI(){
        cls_Date_Manager tmpDM = new cls_Date_Manager();
        
        for ( int i=0; i<this.alWebadiDB.size(); i++ ){
            /*alWebADI_DB.get(i).setDat(alWebADI_DB.get(i).getDat().replaceAll("Jan", "Ene"));
            alWebADI_DB.get(i).setDat(alWebADI_DB.get(i).getDat().replaceAll("Apr", "Abr"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Jan", "01"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Feb", "02"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Mar", "03"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Apr", "04"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("May", "05"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Jun", "06"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Jul", "07"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Aug", "08"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Sep", "09"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Oct", "10"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Nov", "11"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("Dec", "12"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("/13", "/2013"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("/14", "/2014"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("/15", "/2015"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("/16", "/2016"));
            alBO_DB.get(i).setDate(alBO_DB.get(i).getDate().replaceAll("/17", "/2017"));*/
            alWebadiDB.get(i).setDat(tmpDM.formatDate_yyyyMMdd(tmpDM.convertMMMDDYYY_toDate(alWebadiDB.get(i).getDat())));
        }
        this.cleanWebADITable();
        this.loadWebADITable();
        
        
        
    }
    
    /*
    //Creating variables to locate columns in the Backorders imported file
    private int iBSta = -1, iDate = -1, iSvRq = -1, iTask = -1, iISO_bo = -1, iItem_bo = -1, 
            iQty_bo = -1, iDesc = -1, iTkSt = -1, iPLC = -1, iCrit = -1, iCond = -1, iSrAs = -1, 
            iAlts = -1, iComm = -1, iIsMB = -1, iAwMB = -1, iSIMI = -1, iTkNt = -1, iBOMT = -1, iTrak = -1;
    
    //Identifies the column numbers on the Backorders imported Excel file
    private void locateBOColumns(){
        //Reset column values
        iBSta = -1; iDate = -1; iSvRq = -1; iTask = -1; iISO_bo = -1; iItem_bo = -1; 
        iQty_bo = -1; iDesc = -1; iTkSt = -1; iPLC = -1; iCrit = -1; iCond = -1; iSrAs = -1; 
        iAlts = -1; iComm = -1; iIsMB = -1; iAwMB = -1; iSIMI = -1; iTkNt = -1; iBOMT = -1; iTrak = -1;
        
        //FOR Cycle in order to identify the coumn number depending on the PartsListColumn name
        System.out.println("Detecting Backorders Matrix dimmentions.");
        System.out.println("Columns: " + xlsBOMatrix[0].length + " / Rows: " + xlsBOMatrix.length);
        System.out.println("Identifying columns");
        for ( int c=0; c<xlsBOMatrix[0].length; c++ )
        {
            if ( xlsBOMatrix[0][c].equals("Backorder Status (BOT)") ){iBSta = c;}
            if ( xlsBOMatrix[0][c].equals("BO Request Date") ){iDate = c;}
            if ( xlsBOMatrix[0][c].equals("Service Request") ){iSvRq = c;}
            if ( xlsBOMatrix[0][c].equals("Task Number") ){iTask = c;}
            if ( xlsBOMatrix[0][c].equals("Order Number") ){iISO_bo = c;}
            if ( xlsBOMatrix[0][c].equals("Part Number") ){iItem_bo = c;}
            if ( xlsBOMatrix[0][c].equals("Quantity") ){iQty_bo = c;}
            if ( xlsBOMatrix[0][c].equals("Description") ){iDesc = c;}
            if ( xlsBOMatrix[0][c].equals("Task Status") ){iTkSt = c;}
            if ( xlsBOMatrix[0][c].equals("PLC") ){iPLC = c;} //TBD by formula from PPSE
            if ( xlsBOMatrix[0][c].equals("Part Criticality") ){iCrit = c;} //TBD by formula from PPSE
            if ( xlsBOMatrix[0][c].equals("Part Condition") ){iCond = c;} //TBD by formula
            if ( xlsBOMatrix[0][c].equals("Good New Search Assumption") ){iSrAs = c;} //TBD by formula
            if ( xlsBOMatrix[0][c].equals("Alternatives") ){iAlts = c;} //TBD by formula
            if ( xlsBOMatrix[0][c].equals("Comments") ){iComm = c;}
            if ( xlsBOMatrix[0][c].equals("ISO (MI2 > BUE)") ){iIsMB = c;}
            if ( xlsBOMatrix[0][c].equals("AWB (MI2 > BUE)") ){iAwMB = c;}
            if ( xlsBOMatrix[0][c].equals("SIMI (DJAI)") ){iSIMI = c;}
            if ( xlsBOMatrix[0][c].equals("GSI Task Notes") ){iTkNt = c;}
            if ( xlsBOMatrix[0][c].equals("Back Order E-mail Title") ){iBOMT = c;}
            if ( xlsBOMatrix[0][c].equals("Email Tracking") ){iTrak = c;}
        }
    }
        
    //Recognizes if the loaded Backorders xls file has the correct format to work
    private boolean validateBOXLSFile(){
        boolean bFlag = true;
        if ( iBSta == -1 || 
                iDate == -1 || 
                iSvRq == -1 || 
                iTask == -1 || 
                iISO_bo == -1 || 
                iItem_bo == -1 || 
                iQty_bo == -1 ||
                iDesc == -1 ||
                iTkSt == -1 ||
                iPLC == -1 ||
                iCrit == -1 || 
                iCond == -1 || 
                iSrAs == -1 || 
                iAlts == -1 || 
                iComm == -1 || 
                iIsMB == -1 ||
                iAwMB == -1 ||
                iSIMI == -1 ||
                iTkNt == -1 ||
                iBOMT == -1 ||
                iTrak == -1 ){
            bFlag = false;
        }
        if ( bFlag == false ) {
            System.out.println("EXCEL BACKORDERS FILE VALITATION FAILED: One or more columns were not found");
        }
        else {
            System.out.println("EXCEL BACKORDERS FILE VALITATION PASSED");
        }
        return bFlag;        
    }
    
    //Loads the information stored in the imported 2D-Matrix (Excel file) into the Backorders Data Base ArrayList  
    private void loadBODBfromXLS(){
        System.out.println("Updating data in backorders ArrayList");
        
        for ( int r=1; r<xlsBOMatrix.length; r++ ){
            System.out.println("Adding Object " + (r));
            //Creates a new object in the WebADI Data Base arraylist 
            alBO_DB.add(new cls_BO_Data(xlsBOMatrix[r][this.iBSta], 
                    xlsBOMatrix[r][iDate],
                    xlsBOMatrix[r][iSvRq],
                    xlsBOMatrix[r][iTask],
                    xlsBOMatrix[r][iISO_bo],
                    xlsBOMatrix[r][iItem_bo],
                    xlsBOMatrix[r][iQty_bo],
                    xlsBOMatrix[r][iDesc],
                    xlsBOMatrix[r][iTkSt],
                    xlsBOMatrix[r][iPLC],
                    xlsBOMatrix[r][iCrit],
                    xlsBOMatrix[r][iCond],
                    xlsBOMatrix[r][iSrAs],
                    xlsBOMatrix[r][iAlts],
                    xlsBOMatrix[r][iComm],
                    "NA", //ISO1
                    "NA", //AWB1
                    "NA", //ISO2
                    "NA", //AWB2
                    "NA", //ISO3
                    "NA", //AWB3
                    xlsBOMatrix[r][iIsMB],
                    xlsBOMatrix[r][iAwMB],
                    xlsBOMatrix[r][iSIMI],
                    xlsBOMatrix[r][iTkNt],
                    xlsBOMatrix[r][iBOMT],
                    xlsBOMatrix[r][iTrak],
                    "NA", //Position
                    "NA", //XXX Value 1
                    "NA"  //XXX Value 2
            ));
        }
        System.out.println("Backorders Data Base loaded");        
    }
    
    
    //IMPORT THE WHOLE EXCEL BACKORDERS DATA BASE. THIS IS THE BUTTON METHOD
    
        xlsBOMatrix = null;
        System.out.println("Preparing to import Excel Backorders data sheet");
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
            cleanBackordersTable();
            //Gets the first Sheet of the File -if it exists-
            Sheet sh = xlsManager.createExcelSheet(fl);
            //Loads a Bidimentional Array with that Sheet
            xlsBOMatrix = xlsManager.loadXLSsheet_toArray(sh);
            //Identifyies the different columns on the BD-Array
            locateBOColumns();
            if ( validateBOXLSFile() == true ){
                JOptionPane.showMessageDialog(this,"The File was successfully imported");
                //Updates Historical data base ArrayList with the entries from the Excel file
                loadBODBfromXLS();
                loadBackordersTable();
            }
            else {
                JOptionPane.showMessageDialog(this, "The provided Excel file does not contain the necessary columns. Please double check", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    */
    
    
    
    
    //</editor-fold>
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btgrActivity = new javax.swing.ButtonGroup();
        jpnlMiddle = new javax.swing.JPanel();
        jtbpMain = new javax.swing.JTabbedPane();
        jpnlMain = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblParts = new javax.swing.JTable();
        jlblLines = new javax.swing.JLabel();
        jlblLineQTY = new javax.swing.JLabel();
        jbtnImport = new javax.swing.JButton();
        jlblRegs = new javax.swing.JLabel();
        jlblRegsQTY = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jlblCntrs = new javax.swing.JLabel();
        jlblCntrsQTY = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jlblOrgs = new javax.swing.JLabel();
        jlblOrgsQTY = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jlblPrts = new javax.swing.JLabel();
        jlblPrtsQTY = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jpnlTools = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jbtnLoad = new javax.swing.JButton();
        jbtnReset = new javax.swing.JButton();
        jbtnAdd = new javax.swing.JButton();
        rbtnReplen = new javax.swing.JRadioButton();
        rbtnBack = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jlblConsults = new javax.swing.JLabel();
        jlblConsCount = new javax.swing.JLabel();
        jlblCons = new javax.swing.JLabel();
        jpnlSelection = new javax.swing.JPanel();
        jlblTiers = new javax.swing.JLabel();
        jlstTiers = new java.awt.List();
        jlstRegions = new java.awt.List();
        jLabel1 = new javax.swing.JLabel();
        jlstCountries = new java.awt.List();
        jLabel2 = new javax.swing.JLabel();
        jlstParts = new java.awt.List();
        jlblParts = new javax.swing.JLabel();
        jlstOrgsTots = new java.awt.List();
        jlblOrgTot = new javax.swing.JLabel();
        jpnlTasks = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jlstTasks = new java.awt.List();
        jbtnViewTask = new javax.swing.JButton();
        jlblTaskInfo = new javax.swing.JLabel();
        jpnlConsults = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblConsults = new javax.swing.JTable();
        jbtnCreateMails = new javax.swing.JButton();
        jbtnRemove = new javax.swing.JButton();
        jbtnClearList = new javax.swing.JButton();
        jpnlDataBase = new javax.swing.JPanel();
        lblTickHist = new javax.swing.JLabel();
        jlblTickets = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtblDataBase = new javax.swing.JTable();
        jtxtDBSearch = new javax.swing.JTextField();
        jbtnSave = new javax.swing.JButton();
        jbtnReload = new javax.swing.JButton();
        jbtnAdd1 = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jbtnDBSearch = new javax.swing.JButton();
        jlblDBFlag = new javax.swing.JLabel();
        jlblMails = new javax.swing.JLabel();
        lblMailHist = new javax.swing.JLabel();
        jbtnExpDB = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        jpnlBackorders = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtblBackorders = new javax.swing.JTable();
        jbtnBOImp = new javax.swing.JButton();
        jlblBOFlag = new javax.swing.JLabel();
        jbtnBOExp = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jbtnBORefresh = new javax.swing.JButton();
        jbtnBOSearch = new javax.swing.JButton();
        jtxtBOSearch = new javax.swing.JTextField();
        jlblBODBsize = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jbtnBOSave = new javax.swing.JButton();
        jbtnBODel = new javax.swing.JButton();
        jbtnBOAdd = new javax.swing.JButton();
        jbtnBOMail = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JSeparator();
        jbtnCom = new javax.swing.JButton();
        jbtnTrk = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        jpnlWebADI = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtblWebADI = new javax.swing.JTable();
        jbtnWImp = new javax.swing.JButton();
        jbtnWSearch = new javax.swing.JButton();
        jtxtWASearch = new javax.swing.JTextField();
        jbtnWExport = new javax.swing.JButton();
        jbtnWRefresh = new javax.swing.JButton();
        jbtnWSave = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jbtnWDel = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jbtnWAdd = new javax.swing.JButton();
        jlblWADBsize = new javax.swing.JLabel();
        jlblWAFlag = new javax.swing.JLabel();
        jpnlTop = new javax.swing.JPanel();
        jlblTop = new javax.swing.JLabel();
        jlblSta = new javax.swing.JLabel();
        jpnlBottom = new javax.swing.JPanel();
        jbtnExit = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jbtnSwitch = new javax.swing.JButton();
        jlblUser = new javax.swing.JLabel();
        jbtnLogout = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
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
        jbtnRST = new javax.swing.JButton();
        jmbrTopMenu = new javax.swing.JMenuBar();
        jmenFile = new javax.swing.JMenu();
        jmeitImport = new javax.swing.JMenuItem();
        jmeitExport = new javax.swing.JMenuItem();
        jmeitExit = new javax.swing.JMenuItem();
        jmenEdit = new javax.swing.JMenu();
        jmenTools = new javax.swing.JMenu();
        jmeiQuickTracks = new javax.swing.JMenuItem();
        jmeitUserPriv = new javax.swing.JMenuItem();
        jmenDBbackup = new javax.swing.JMenuItem();
        jmeitOpt = new javax.swing.JMenuItem();
        jmeitTempTools = new javax.swing.JMenu();
        jmenAbout = new javax.swing.JMenu();
        jmiAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpnlMiddle.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jpnlMiddle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtbpMain.setToolTipText("");

        jpnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtblParts.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jtblParts);

        jpnlMain.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 950, 310));

        jlblLines.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblLines.setText("LINES LOADED: ");
        jpnlMain.add(jlblLines, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 340, 105, -1));

        jlblLineQTY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblLineQTY.setText("QTY");
        jpnlMain.add(jlblLineQTY, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 340, 60, -1));

        jbtnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/import_medium.png"))); // NOI18N
        jbtnImport.setToolTipText("Import");
        jbtnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnImportActionPerformed(evt);
            }
        });
        jpnlMain.add(jbtnImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 327, 50, -1));

        jlblRegs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblRegs.setText("Regions:  ");
        jpnlMain.add(jlblRegs, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 70, -1));

        jlblRegsQTY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblRegsQTY.setText("00");
        jpnlMain.add(jlblRegsQTY, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 53, -1));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlMain.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(222, 330, -1, 30));

        jlblCntrs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblCntrs.setText("Countries:  ");
        jpnlMain.add(jlblCntrs, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, 80, -1));

        jlblCntrsQTY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblCntrsQTY.setText("00");
        jpnlMain.add(jlblCntrsQTY, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 340, 60, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlMain.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 330, -1, 30));

        jlblOrgs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblOrgs.setText("Organizations:  ");
        jpnlMain.add(jlblOrgs, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 340, 102, -1));

        jlblOrgsQTY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblOrgsQTY.setText("00");
        jpnlMain.add(jlblOrgsQTY, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 340, 60, -1));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlMain.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(602, 330, 30, 30));

        jlblPrts.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblPrts.setText("Parts:  ");
        jpnlMain.add(jlblPrts, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 340, 68, -1));

        jlblPrtsQTY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblPrtsQTY.setText("00");
        jpnlMain.add(jlblPrtsQTY, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 340, 60, -1));

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlMain.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 330, -1, 30));

        jtbpMain.addTab("New Good Search", jpnlMain);

        jpnlTools.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList1);

        jpnlTools.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 0, 0));

        jbtnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/DataList_medium.png"))); // NOI18N
        jbtnLoad.setText(" LOAD");
        jbtnLoad.setToolTipText("Load lists from List of Parts data ");
        jbtnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLoadActionPerformed(evt);
            }
        });
        jpnlTools.add(jbtnLoad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 100, 40));

        jbtnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clear_2_medium.png"))); // NOI18N
        jbtnReset.setText(" CLEAR");
        jbtnReset.setToolTipText("Cleans the Lists and Consults count");
        jbtnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnResetActionPerformed(evt);
            }
        });
        jpnlTools.add(jbtnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, 110, 40));

        jbtnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/arrow-add_medium.png"))); // NOI18N
        jbtnAdd.setText("ADD");
        jbtnAdd.setToolTipText("Adds the selection to the list of consults");
        jbtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddActionPerformed(evt);
            }
        });
        jpnlTools.add(jbtnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 280, 130, -1));

        btgrActivity.add(rbtnReplen);
        rbtnReplen.setText(" Replenishment");
        rbtnReplen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtnReplenItemStateChanged(evt);
            }
        });
        rbtnReplen.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnReplenStateChanged(evt);
            }
        });
        rbtnReplen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnReplenActionPerformed(evt);
            }
        });
        jpnlTools.add(rbtnReplen, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 310, -1, 30));

        btgrActivity.add(rbtnBack);
        rbtnBack.setText(" Backorders");
        rbtnBack.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnBackStateChanged(evt);
            }
        });
        rbtnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnBackActionPerformed(evt);
            }
        });
        jpnlTools.add(rbtnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 310, -1, 30));

        jLabel4.setText("(Good Excess)");
        jpnlTools.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 340, 90, 20));

        jLabel5.setText("(Good On Hand)");
        jpnlTools.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 340, 100, 20));

        jlblConsults.setText("CONSULTS: ");
        jpnlTools.add(jlblConsults, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 280, 70, 30));

        jlblConsCount.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jlblConsCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblConsCount.setText("0");
        jpnlTools.add(jlblConsCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 280, 30, 30));

        jlblCons.setFont(new java.awt.Font("Engravers MT", 0, 18)); // NOI18N
        jlblCons.setText("<html><font color='blue'>FILTER, SELECT & CREATE CONSULTS</font></html>");
        jlblCons.setToolTipText("");
        jpnlTools.add(jlblCons, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 600, -1));

        jpnlSelection.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), "FILTERS"));

        jlblTiers.setText("TIERS");

        jlstTiers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlstTiersMouseClicked(evt);
            }
        });

        jlstRegions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlstRegionsMouseClicked(evt);
            }
        });

        jLabel1.setText("REGIONS");

        jlstCountries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlstCountriesMouseClicked(evt);
            }
        });

        jLabel2.setText("COUNTRIES");

        jlstParts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlstPartsMouseClicked(evt);
            }
        });

        jlblParts.setText("PARTS");

        jlstOrgsTots.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlstOrgsTotsMouseClicked(evt);
            }
        });

        jlblOrgTot.setText("ORGS - TOT");

        javax.swing.GroupLayout jpnlSelectionLayout = new javax.swing.GroupLayout(jpnlSelection);
        jpnlSelection.setLayout(jpnlSelectionLayout);
        jpnlSelectionLayout.setHorizontalGroup(
            jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblTiers)
                    .addComponent(jlstTiers, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlstRegions, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlstCountries, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlstParts, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblParts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblOrgTot)
                    .addComponent(jlstOrgsTots, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnlSelectionLayout.setVerticalGroup(
            jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlblTiers)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jlblParts)
                    .addComponent(jlblOrgTot))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnlSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlstRegions, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addComponent(jlstCountries, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlstParts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlstOrgsTots, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlstTiers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnlTools.add(jpnlSelection, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 590, 270));

        jpnlTasks.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), "TASKS"));

        jLabel3.setText(" TASK NUMBER");

        jlstTasks.setMultipleMode(true);
        jlstTasks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlstTasksMouseClicked(evt);
            }
        });

        jbtnViewTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/view_medium.png"))); // NOI18N
        jbtnViewTask.setText("View Task Data");
        jbtnViewTask.setToolTipText("Looks for the highlighted Task on the BO data base");
        jbtnViewTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnViewTaskActionPerformed(evt);
            }
        });

        jlblTaskInfo.setText("Task info");

        javax.swing.GroupLayout jpnlTasksLayout = new javax.swing.GroupLayout(jpnlTasks);
        jpnlTasks.setLayout(jpnlTasksLayout);
        jpnlTasksLayout.setHorizontalGroup(
            jpnlTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTasksLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlTasksLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpnlTasksLayout.createSequentialGroup()
                        .addComponent(jlstTasks, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpnlTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtnViewTask, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .addComponent(jlblTaskInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jpnlTasksLayout.setVerticalGroup(
            jpnlTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTasksLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnlTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlstTasks, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                    .addGroup(jpnlTasksLayout.createSequentialGroup()
                        .addComponent(jbtnViewTask)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblTaskInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jpnlTools.add(jpnlTasks, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 300, 230));

        jtbpMain.addTab("Selection Tool", jpnlTools);

        jpnlConsults.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtblConsults.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jtblConsults);

        jpnlConsults.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 940, 300));

        jbtnCreateMails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sendmail_medium.png"))); // NOI18N
        jbtnCreateMails.setText("  Create Mail(s)");
        jbtnCreateMails.setToolTipText("Create mail(s)");
        jbtnCreateMails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCreateMailsActionPerformed(evt);
            }
        });
        jpnlConsults.add(jbtnCreateMails, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 320, 150, 40));

        jbtnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove_medium.png"))); // NOI18N
        jbtnRemove.setToolTipText("Remove highlighted line");
        jbtnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRemoveActionPerformed(evt);
            }
        });
        jpnlConsults.add(jbtnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 50, 40));

        jbtnClearList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clear_2_medium.png"))); // NOI18N
        jbtnClearList.setToolTipText("Clear Table list");
        jbtnClearList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnClearListActionPerformed(evt);
            }
        });
        jpnlConsults.add(jbtnClearList, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 50, 40));

        jtbpMain.addTab("Consults List", jpnlConsults);

        lblTickHist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ticket_small.png"))); // NOI18N

        jlblTickets.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlblTickets.setText("Ticket qty");

        jtblDataBase.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jtblDataBase);

        jtxtDBSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtDBSearchKeyPressed(evt);
            }
        });

        jbtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/save_medium.png"))); // NOI18N
        jbtnSave.setToolTipText("Save");
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        jbtnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh_2_medium.png"))); // NOI18N
        jbtnReload.setToolTipText("Refresh screen");
        jbtnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnReloadActionPerformed(evt);
            }
        });

        jbtnAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_medium.png"))); // NOI18N
        jbtnAdd1.setToolTipText("Add line");
        jbtnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAdd1ActionPerformed(evt);
            }
        });

        jbtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove_medium.png"))); // NOI18N
        jbtnDelete.setToolTipText("Delete selected line");
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });

        jbtnDBSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_find_medium.png"))); // NOI18N
        jbtnDBSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDBSearchActionPerformed(evt);
            }
        });

        jlblDBFlag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblDBFlag.setText("DATA BASE FLAG");

        jlblMails.setText("Mails qty");

        lblMailHist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/mail_small.png"))); // NOI18N

        jbtnExpDB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/csvexport2_medium.png"))); // NOI18N
        jbtnExpDB.setToolTipText("Export screen data to.csv");
        jbtnExpDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExpDBActionPerformed(evt);
            }
        });

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jpnlDataBaseLayout = new javax.swing.GroupLayout(jpnlDataBase);
        jpnlDataBase.setLayout(jpnlDataBaseLayout);
        jpnlDataBaseLayout.setHorizontalGroup(
            jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlDataBaseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jpnlDataBaseLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jbtnExpDB)
                        .addGap(75, 75, 75)
                        .addComponent(lblTickHist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlblTickets, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMailHist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlblMails)
                        .addGap(150, 150, 150)
                        .addComponent(jbtnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtDBSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblDBFlag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnDBSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnReload, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );
        jpnlDataBaseLayout.setVerticalGroup(
            jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlDataBaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlDataBaseLayout.createSequentialGroup()
                        .addGroup(jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jbtnReload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtnExpDB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtnDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator12, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtnSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtnAdd1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtnDBSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jpnlDataBaseLayout.createSequentialGroup()
                        .addGroup(jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtDBSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jlblTickets)
                                .addComponent(lblTickHist)
                                .addGroup(jpnlDataBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpnlDataBaseLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jlblMails))
                                    .addComponent(lblMailHist))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblDBFlag)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jtbpMain.addTab("Consults DB", jpnlDataBase);

        jpnlBackorders.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtblBackorders.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(jtblBackorders);

        jpnlBackorders.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 958, 299));

        jbtnBOImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/import_medium.png"))); // NOI18N
        jbtnBOImp.setToolTipText("Import ODS Backorders Data");
        jbtnBOImp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBOImpActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBOImp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 321, -1, 40));

        jlblBOFlag.setText("Current data base");
        jpnlBackorders.add(jlblBOFlag, new org.netbeans.lib.awtextra.AbsoluteConstraints(672, 350, 200, -1));

        jbtnBOExp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/csvexport2_medium.png"))); // NOI18N
        jbtnBOExp.setToolTipText("Export screen data to .csv file");
        jbtnBOExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBOExpActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBOExp, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 321, -1, 40));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlBackorders.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 321, -1, 33));

        jbtnBORefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh_2_medium.png"))); // NOI18N
        jbtnBORefresh.setToolTipText("Refresh screen");
        jbtnBORefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBORefreshActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBORefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(928, 321, 40, 40));

        jbtnBOSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_find_medium.png"))); // NOI18N
        jbtnBOSearch.setToolTipText("Search");
        jbtnBOSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBOSearchActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBOSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(881, 320, 40, 40));

        jtxtBOSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtBOSearchKeyPressed(evt);
            }
        });
        jpnlBackorders.add(jtxtBOSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(672, 321, 200, 23));

        jlblBODBsize.setText("Data Base size: ");
        jpnlBackorders.add(jlblBODBsize, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 325, -1, -1));

        jSeparator11.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlBackorders.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 320, 10, 40));

        jbtnBOSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/save_medium.png"))); // NOI18N
        jbtnBOSave.setToolTipText("Save");
        jbtnBOSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBOSaveActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBOSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 320, 40, 40));

        jbtnBODel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove_medium.png"))); // NOI18N
        jbtnBODel.setToolTipText("Delete selected line");
        jbtnBODel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBODelActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBODel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 320, 40, 40));

        jbtnBOAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_medium.png"))); // NOI18N
        jbtnBOAdd.setToolTipText("Add line");
        jbtnBOAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBOAddActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBOAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 320, 40, 40));

        jbtnBOMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sendmail_medium.png"))); // NOI18N
        jbtnBOMail.setToolTipText("Prepare backorder e-mail");
        jbtnBOMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBOMailActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnBOMail, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 320, 40, 40));

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlBackorders.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 320, 10, 40));

        jbtnCom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Comments_medium.png"))); // NOI18N
        jbtnCom.setToolTipText("View and edit comments");
        jbtnCom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnComActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 320, 40, 40));

        jbtnTrk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/numberlist_medium.png"))); // NOI18N
        jbtnTrk.setToolTipText("View the list of related Mail Tracking numbers");
        jbtnTrk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTrkActionPerformed(evt);
            }
        });
        jpnlBackorders.add(jbtnTrk, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 320, 40, 40));

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlBackorders.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 320, 10, 40));

        jtbpMain.addTab("Backorders DB", jpnlBackorders);

        jpnlWebADI.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtblWebADI.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(jtblWebADI);

        jpnlWebADI.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 960, 301));

        jbtnWImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/import_medium.png"))); // NOI18N
        jbtnWImp.setToolTipText("Import WebADI data base");
        jbtnWImp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWImpActionPerformed(evt);
            }
        });
        jpnlWebADI.add(jbtnWImp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 323, 40, 40));

        jbtnWSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_find_medium.png"))); // NOI18N
        jbtnWSearch.setToolTipText("Search");
        jbtnWSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWSearchActionPerformed(evt);
            }
        });
        jpnlWebADI.add(jbtnWSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 320, 40, 40));

        jtxtWASearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtWASearchKeyPressed(evt);
            }
        });
        jpnlWebADI.add(jtxtWASearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 320, 210, 23));

        jbtnWExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/csvexport2_medium.png"))); // NOI18N
        jbtnWExport.setToolTipText("Export screen data to.csv");
        jbtnWExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWExportActionPerformed(evt);
            }
        });
        jpnlWebADI.add(jbtnWExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 323, 40, 40));

        jbtnWRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh_2_medium.png"))); // NOI18N
        jbtnWRefresh.setToolTipText("Refresh screen");
        jbtnWRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWRefreshActionPerformed(evt);
            }
        });
        jpnlWebADI.add(jbtnWRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(929, 320, 40, 40));

        jbtnWSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/save_medium.png"))); // NOI18N
        jbtnWSave.setToolTipText("Save");
        jbtnWSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWSaveActionPerformed(evt);
            }
        });
        jpnlWebADI.add(jbtnWSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 320, 40, 40));

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlWebADI.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 323, -1, 40));

        jbtnWDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove_medium.png"))); // NOI18N
        jbtnWDel.setToolTipText("Delete selected line");
        jbtnWDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWDelActionPerformed(evt);
            }
        });
        jpnlWebADI.add(jbtnWDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 320, 40, 40));

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlWebADI.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(642, 320, -1, 40));

        jbtnWAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_medium.png"))); // NOI18N
        jbtnWAdd.setToolTipText("Add line");
        jbtnWAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnWAddActionPerformed(evt);
            }
        });
        jpnlWebADI.add(jbtnWAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 320, 40, 40));

        jlblWADBsize.setText("Data Base size: ");
        jpnlWebADI.add(jlblWADBsize, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 323, 106, -1));

        jlblWAFlag.setText("Current Data Base");
        jpnlWebADI.add(jlblWAFlag, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 350, 210, -1));

        jtbpMain.addTab("WebADI DB", jpnlWebADI);

        jpnlMiddle.add(jtbpMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 3, 983, 400));

        getContentPane().add(jpnlMiddle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 57, 990, 410));

        jpnlTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlTop.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlblTop.setFont(new java.awt.Font("Vacation Postcard NF", 1, 34)); // NOI18N
        jlblTop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblTop.setText("GN-righthand x.xx");
        jlblTop.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlblTop.setIconTextGap(8);
        jpnlTop.add(jlblTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 870, 40));

        jlblSta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblSta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jlblSta.setOpaque(true);
        jpnlTop.add(jlblSta, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 10, 60, 20));

        getContentPane().add(jpnlTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 1050, -1));

        jpnlBottom.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlBottom.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exit_medium.png"))); // NOI18N
        jbtnExit.setText("Exit");
        jbtnExit.setToolTipText("Exit the Application");
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExitActionPerformed(evt);
            }
        });
        jpnlBottom.add(jbtnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 90, 30));

        jLabel6.setText("CR Spares Planning Team. Argentina Planning, 2017.");
        jpnlBottom.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 64, -1, 20));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/RightHand.fw2.fw.Small.fw.png"))); // NOI18N
        jpnlBottom.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 60));

        jbtnSwitch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/switch_small.png"))); // NOI18N
        jbtnSwitch.setText("Swtich");
        jbtnSwitch.setToolTipText("Switch between Consulting Tool and Data Bases");
        jbtnSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSwitchActionPerformed(evt);
            }
        });
        jpnlBottom.add(jbtnSwitch, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 40, 170, 30));

        jlblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblUser.setText("User");
        jpnlBottom.add(jlblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(444, 10, 520, -1));

        jbtnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout_lil.png"))); // NOI18N
        jbtnLogout.setText("Logout");
        jbtnLogout.setToolTipText("Go back to the Login Screen");
        jbtnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLogoutActionPerformed(evt);
            }
        });
        jpnlBottom.add(jbtnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 40, 110, 30));

        jButton1.setText("Test");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jpnlBottom.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, -1, -1));

        getContentPane().add(jpnlBottom, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 990, 90));

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
        jSeparator9.setSeparatorSize(new java.awt.Dimension(0, 35));
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

        jSeparator15.setPreferredSize(new java.awt.Dimension(0, 75));
        jSeparator15.setSeparatorSize(new java.awt.Dimension(0, 75));
        jSeparator15.setVerifyInputWhenFocusTarget(false);
        jtbarMain.add(jSeparator15);

        jbtnRST.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Reset1_MED.png"))); // NOI18N
        jbtnRST.setToolTipText("Resets the DB on the active Tab and reloads its most recent saved version");
        jbtnRST.setFocusable(false);
        jbtnRST.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnRST.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnRST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRSTActionPerformed(evt);
            }
        });
        jtbarMain.add(jbtnRST);

        getContentPane().add(jtbarMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 60, 50, 510));

        jmenFile.setText("File");

        jmeitImport.setText("Import");
        jmeitImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmeitImportActionPerformed(evt);
            }
        });
        jmenFile.add(jmeitImport);

        jmeitExport.setText("Export");
        jmeitExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmeitExportActionPerformed(evt);
            }
        });
        jmenFile.add(jmeitExport);

        jmeitExit.setText("Exit");
        jmeitExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmeitExitActionPerformed(evt);
            }
        });
        jmenFile.add(jmeitExit);

        jmbrTopMenu.add(jmenFile);

        jmenEdit.setText("Edit");
        jmbrTopMenu.add(jmenEdit);

        jmenTools.setText("Tools");

        jmeiQuickTracks.setText("Quick Tracks");
        jmeiQuickTracks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmeiQuickTracksActionPerformed(evt);
            }
        });
        jmenTools.add(jmeiQuickTracks);

        jmeitUserPriv.setText("Manage User Privileges");
        jmeitUserPriv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmeitUserPrivActionPerformed(evt);
            }
        });
        jmenTools.add(jmeitUserPriv);

        jmenDBbackup.setText("Data Base Backup Control");
        jmenDBbackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenDBbackupActionPerformed(evt);
            }
        });
        jmenTools.add(jmenDBbackup);

        jmeitOpt.setText("Options");
        jmenTools.add(jmeitOpt);

        jmeitTempTools.setText("Temporary Tools");
        jmenTools.add(jmeitTempTools);

        jmbrTopMenu.add(jmenTools);

        jmenAbout.setText("About");

        jmiAbout.setText("About this Tool");
        jmiAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAboutActionPerformed(evt);
            }
        });
        jmenAbout.add(jmiAbout);

        jmbrTopMenu.add(jmenAbout);

        setJMenuBar(jmbrTopMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExitActionPerformed
        int opc;
        String sUnsavedDB = "";
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if ( checkForUnsavedChanges("Consults") == true ){
            sUnsavedDB = sUnsavedDB + " CONSULTS";
        }
        if ( checkForUnsavedChanges("Backorders") == true ){
            if ( sUnsavedDB.equals("") ){
                sUnsavedDB = sUnsavedDB + " BACKORDERS";
            }
            else{
                sUnsavedDB = sUnsavedDB + ", BACKORDERS";
            }
        }
        if ( checkForUnsavedChanges("WebADI") == true ){
            if ( sUnsavedDB.equals("") ){
                sUnsavedDB = sUnsavedDB + " WEBADI.";
            }
            else{
                sUnsavedDB = sUnsavedDB + ", WEBADI.";
            }
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if ( !sUnsavedDB.equals("") ){
            opc = JOptionPane.showConfirmDialog(this, "We have detected that the following Data Bases contain unsaved changes:" + sUnsavedDB + "\n"
                    + "Click on YES if you want to exit discarding these changes or click NO and go back to save the data");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(this,"No changes are being saved. Bye!");
                //Backsups the data from the active ArrayList into the local HDD
                updateConsultsTXTDataBase();
                updateBackordersTXTDataBase();
                updateWebADITXTDataBase();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                System.exit(0);
            }
        }
        else{
            opc = JOptionPane.showConfirmDialog(this,"Do you want to exit the Application?\n");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Backsups the data from the active ArrayList into the local HDD
                updateConsultsTXTDataBase();
                updateBackordersTXTDataBase();
                updateWebADITXTDataBase();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                System.exit(0);
            }
        }
    }//GEN-LAST:event_jbtnExitActionPerformed

    private void jbtnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnImportActionPerformed
        CleanLists();
        xlsDataMatrix = null;
        System.out.println("Preparing to import Excel data sheet");
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
            cleanPartsListTable();
            //Gets the first Sheet of the File -if it exists-
            Sheet sh = xlsManager.createExcelSheet(fl);
            //Creates a Bidimentional Array with that Sheet
            xlsDataMatrix = xlsManager.loadXLSsheet_toArray(sh);
            xlsDataMatrix = this.updateArrayNames(xlsDataMatrix);
            //Identifyies the different columns on the BD-Array
            locateColumns();
            if ( validateXLSFile() == true ){
                JOptionPane.showMessageDialog(this,"The File was successfully imported");
                loadPartsListTable();
                this.updateMainCounters(xlsDataMatrix);
                CleanLists();
            }
            else {
                JOptionPane.showMessageDialog(this, "The provided Excel file does not contain the necessary columns. Please double check", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
            //loadDropLists();
        }
    }//GEN-LAST:event_jbtnImportActionPerformed

    private void jbtnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLoadActionPerformed
        CleanLists();
        loadTiersList();
    }//GEN-LAST:event_jbtnLoadActionPerformed

    private void jlstTiersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlstTiersMouseClicked
        jlstRegions.removeAll();
        jlstCountries.removeAll();
        jlstParts.removeAll();
        jlstOrgsTots.removeAll();
        jlstTasks.removeAll();
        jlblTaskInfo.setText("");
        String sItem = jlstTiers.getSelectedItem();
        if ( sItem.equals("N/A") ) {
            jlstRegions.removeAll();
            jlstCountries.removeAll();
        }
        else {
            this.loadRegionsList(sItem);
        }
    }//GEN-LAST:event_jlstTiersMouseClicked

    private void jlstRegionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlstRegionsMouseClicked
        jlstCountries.removeAll();
        jlstParts.removeAll();
        jlstOrgsTots.removeAll();
        jlstTasks.removeAll();
        jlblTaskInfo.setText("");
        String sTier = jlstTiers.getSelectedItem();
        String sReg = jlstRegions.getSelectedItem();
        if ( sReg.equals("N/A") ) {
            jlstCountries.removeAll();
        }
        else {
            this.loadCountriesList(sTier, sReg);
        }
    }//GEN-LAST:event_jlstRegionsMouseClicked

    private void jbtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnResetActionPerformed
        CleanLists();
        jlblConsCount.setText("0");
        jlblTaskInfo.setText("");        
    }//GEN-LAST:event_jbtnResetActionPerformed

    private void jlstCountriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlstCountriesMouseClicked
        jlstParts.removeAll();
        jlstOrgsTots.removeAll();
        jlstTasks.removeAll();
        jlblTaskInfo.setText("");
        String sTier = jlstTiers.getSelectedItem();
        String sReg = jlstRegions.getSelectedItem();
        String sCntr = jlstCountries.getSelectedItem();
        if ( sCntr.equals("N/A") ) {
            jlstParts.removeAll();
        }
        else {
            this.loadPartsList(sTier, sReg, sCntr);
        }
    }//GEN-LAST:event_jlstCountriesMouseClicked

    private void jlstOrgsTotsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlstOrgsTotsMouseClicked
        
    }//GEN-LAST:event_jlstOrgsTotsMouseClicked

    private void jlstPartsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlstPartsMouseClicked
        jlstOrgsTots.removeAll();
        jlstTasks.removeAll();
        jlblTaskInfo.setText("");
        String sTier = jlstTiers.getSelectedItem();
        String sReg = jlstRegions.getSelectedItem();
        String sCntr = jlstCountries.getSelectedItem();
        String sPart = jlstParts.getSelectedItem();
        if ( sPart.equals("N/A") ) {
            jlstOrgsTots.removeAll();
        }
        else {
            this.loadOrgsList(sTier, sReg, sCntr, sPart);
            loadTasks(this.jlstParts.getSelectedItem());
        }
    }//GEN-LAST:event_jlstPartsMouseClicked

    private void rbtnReplenStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnReplenStateChanged
    }//GEN-LAST:event_rbtnReplenStateChanged

    private void rbtnBackStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnBackStateChanged

    }//GEN-LAST:event_rbtnBackStateChanged

    private void rbtnReplenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtnReplenItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnReplenItemStateChanged

    private void rbtnReplenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnReplenActionPerformed
        try {
            jlstOrgsTots.removeAll();
            loadOrgsList(jlstTiers.getSelectedItem(), jlstRegions.getSelectedItem(), jlstCountries.getSelectedItem(),  jlstParts.getSelectedItem());
        }
        catch ( Exception e){
            System.out.println("Exception error occured. Not enough values selected");
        }        
    }//GEN-LAST:event_rbtnReplenActionPerformed

    private void rbtnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBackActionPerformed
        try {
            jlstOrgsTots.removeAll();
            loadOrgsList(jlstTiers.getSelectedItem(), jlstRegions.getSelectedItem(), jlstCountries.getSelectedItem(),  jlstParts.getSelectedItem());
        }
        catch ( Exception e){
            System.out.println("Exception error occured. Not enough values selected");
        }
    }//GEN-LAST:event_rbtnBackActionPerformed

    private void jbtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddActionPerformed
        //Updates the consults ArrayList with the entry and refreshes the consult list chart with the values on the ArrayList
        updateConsultList();
        cleanNewConsultsTable();
        loadNewConsultsTable();
    }//GEN-LAST:event_jbtnAddActionPerformed

    private void jbtnClearListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnClearListActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you really want to clear the ALL the list of consults?");
        if ( opc == 0 ){
            cleanNewConsultsTable();
            alGNSearchList.clear();
            jlblConsCount.setText("0");
        }
    }//GEN-LAST:event_jbtnClearListActionPerformed

    private void jbtnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRemoveActionPerformed
        int ArrayLine = findConsultPos(captureConsultLine());
        System.out.println("Line at Array position: " + ArrayLine);
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to remove the selected Line?");
        if ( opc == 0 ){
            this.alGNSearchList.remove(ArrayLine);
            jlblConsCount.setText(String.valueOf(alGNSearchList.size()));
            this.cleanNewConsultsTable(); 
            this.loadNewConsultsTable();
            JOptionPane.showMessageDialog(this, "The Line has been removed");
        }        
    }//GEN-LAST:event_jbtnRemoveActionPerformed

    private void jbtnCreateMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCreateMailsActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String sCountry = "";
        String sTXT = "";
        //Prepares the mail subject and tracking variables
        String sMailSub = ""; String sTrack = ""; String sMailTo = ""; String sOrg = "";
        //Cleaning global variables
        sTrackings = "";
        sUSAParts = "";
        sUSATracking = "";
        //Checks if the User is Online or Offline
        if( bONLINE == true ){sTXT = "REMOTE";}
        else{sTXT = "LOCAL";}
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        int opc = JOptionPane.showConfirmDialog(this,"PLEASE CONFIRM THAT YOU WANT TO CREATE MAILS FOR THE ALL THE CONSULTS.\n"
                + "THIS ACTION WILL AUTOMATICALLY UPDATE YOUR " + sTXT + " DATA BASE.");
        if ( opc == 0 ){
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int i = 0;
            //Creates an unidimentional array with the list of different Orgs
            String[] saOrgs = getDifferentOrgs();
            //Checks the Orgs one by one
            do {
                sOrg = saOrgs[i].toUpperCase();
                sCountry = getCountry(sOrg);
                if ( !sCountry.equals("UNITED STATES") || sOrg.equals("MIA") || sOrg.equals("LOC") ) {
                    //Generates a tracking number
                    try {sTrack = createTracking().toUpperCase();}
                    catch (IOException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
                    sTrackings = sTrackings + sTrack + "\n";
                    //Identifies the receiver's Mail depending on the Country and Org 
                    sMailTo = createConsultMailTo(sCountry, sOrg);
                    //Determines the list of consulted parts depending on the Org and updates each corresponding tracking
                    String sParts = getConsultedParts(sCountry, sOrg, sTrack, false);
                    //Creates the mail subject
                    sMailSub = createMailSubject(sOrg, sCountry) + sTrack;
                    //Creates the mail's body
                    String sBody = createConsultBodyMail(sParts, sCountry, sMailTo);
                    //OPENS AN OUTLOOK WINDOW WITH THE MAIL READY TO BE SENT
                    try {this.sendMail(sMailTo, "na", sMailSub, sBody, sCountry, sOrg);}
                    catch (IOException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
                    catch (URISyntaxException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
                }
                else { //Generates a single tracking and catches all the USA's T3s consults
                    //Checks if there is a tracking number for USA'S T3s already
                    if ( sUSATracking.equals("") ){
                        try {sUSATracking = createTracking().toUpperCase();}
                        catch (IOException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);} 
                        sTrackings = sTrackings + sUSATracking + "\n";
                    }
                    //Determines the list of consulted parts depending on the Org and updates each corresponding tracking
                    getConsultedParts(sCountry, sOrg, sUSATracking, true);
                    System.out.println("Parts so far: " + sUSAParts);
                }
                i++;
            } while ( !saOrgs[i].equals("END") );
            if ( !sUSATracking.equals("") ) {
                //Identifies the receiver's Mail depending on the Country and Org 
                sMailTo = createConsultMailTo("UNITED STATES", "NA");
                //Creates the mail subject
                sMailSub = createMailSubject(sOrg, sCountry) + sUSATracking;
                //Creates the mail's body
                System.out.println("Parts sent to create body: " + sUSAParts);
                String sBody = createConsultBodyMail(sUSAParts, sCountry, sMailTo);
                //OPENS AN OUTLOOK WINDOW WITH THE MAIL READY TO BE SENT
                try {this.sendMail(sMailTo, "na", sMailSub, sBody, sCountry, sOrg);}
                catch (IOException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
                catch (URISyntaxException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
            }
            //Updates the local or remote .txt Data bases: Consults and Backorders (with the tracking number)
            sendConsultsToDB ();
            //Reloads the Consults QTY history
            if ( bONLINE == true ){
                loadRemConsQTYHist();
            }
            else{
                loadConsultsQTYHist();
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(this, "The " + sTXT.toLowerCase() + " Consults Data Base has been updated");
            //gui_InfoNotes tmpIN = new gui_InfoNotes("GENERATED TRACKINGS: \n" + sTrackings);
            //tmpIN.setLocationRelativeTo(this);
            //tmpIN.setTitle("TRACKINGS LIST");
            //tmpIN.setVisible(true);
            sTrackings = "";
            sUSAParts = "";
            sUSATracking = "";
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        cleanNewConsultsTable();
        loadNewConsultsTable();
        
        cleanConsultsDBTable();
        loadConsultsDBTable();
        
        cleanBackordersTable();
        loadBackordersTable();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jbtnCreateMailsActionPerformed

    private void jbtnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnReloadActionPerformed
        this.jtblDataBase.getRowSorter().setSortKeys(null);
        cleanConsultsDBTable();
        jtxtDBSearch.setText("");
        //Loads the information from the Consults DB ArrayList of current consults into de Consults DB JTable
        loadConsultsDBTable();
        if ( bONLINE == true ){
            loadRemConsQTYHist();
        }
        else{
            loadConsultsQTYHist();
        }
        JOptionPane.showMessageDialog(this,"The Data has been reloaded");
    }//GEN-LAST:event_jbtnReloadActionPerformed

    private void jbtnDBSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDBSearchActionPerformed
        if ( jtxtDBSearch.getText().equals("") ) {
            this.cleanConsultsDBTable();
            this.loadConsultsDBTable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else {
            searchTextConsultsDB(jtxtDBSearch.getText().toUpperCase());
        }
    }//GEN-LAST:event_jbtnDBSearchActionPerformed

    private void jtxtDBSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtDBSearchKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {
            if ( jtxtDBSearch.getText().equals("") ) {
            this.cleanConsultsDBTable();
            this.loadConsultsDBTable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else {
                searchTextConsultsDB(jtxtDBSearch.getText().toUpperCase());
            }
        }
    }//GEN-LAST:event_jtxtDBSearchKeyPressed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        if ( bONLINE == true ){
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite the remote Consults Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Updates the ArrayList with the info from the JTable
                updateConsultsALDataBase();
                //Updates the .txt DB file with the info from the ArrayList 
                uploadRemConsDB();
                //Updates screen counters
                loadRemConsQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
        else{
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite your local Consults Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Updates the ArrayList with the info from the JTable
                updateConsultsALDataBase();
                //Updates the .txt DB file with the info from the ArrayList 
                updateConsultsTXTDataBase();
                //Updates screen counters
                loadConsultsQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_jbtnSaveActionPerformed

    private void jmiAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAboutActionPerformed
        About tmp = new About(sVer);
        tmp.setLocationRelativeTo(this);
        tmp.setVisible(true);
    }//GEN-LAST:event_jmiAboutActionPerformed

    private void jmeitImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmeitImportActionPerformed
        CleanLists();
        xlsDataMatrix = null;
        System.out.println("Preparing to import Excel data sheet");
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
            cleanPartsListTable();
            //Gets the first Sheet of the File -if it exists-
            Sheet sh = xlsManager.createExcelSheet(fl);
            //Creates a Bidimentional Array with that Sheet
            xlsDataMatrix = xlsManager.loadXLSsheet_toArray(sh);
            //Identifyies the different columns on the BD-Array
            locateColumns();
            if ( validateXLSFile() == true ){
                JOptionPane.showMessageDialog(this,"The File was successfully imported");
                loadPartsListTable();
                CleanLists();
            }
            else {
                JOptionPane.showMessageDialog(this, "The provided Excel file does not contain the necessary columns. Please double check", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
            //loadDropLists();
        }
    }//GEN-LAST:event_jmeitImportActionPerformed

    private void jmeitExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmeitExitActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to exit the Application?");
        if ( opc == 0 ){
            updateConsultsTXTDataBase();
            System.exit(0);
        }
    }//GEN-LAST:event_jmeitExitActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed
        //Captures the selected Lines
        int[] selLines = jtblDataBase.getSelectedRows();
        if ( selLines.length > 0 ){
            int opc = JOptionPane.showConfirmDialog(this,"THE SELECTED LINE(s) WILL BE DELETED.\n Do you want to proceed.");
            if ( opc == 0 ){
                //Checks if the current data in the screen is the actual DB or just searching results
                if ( bDBFLAG == true ) {
                    for ( int i=0; i<selLines.length; i++ ){
                        //Determines the position of the highlighted lines in the main DB Array List
                        int iDelPos = findConsultDBPos(captureDataBaseLine(selLines[i]));
                        //Captures the data in the highlighted line; finds its position into the BD Array List and deletes it
                        alCosulDB.remove(iDelPos);
                        }
                    }
                    else {
                        int iCnt = 0;
                        for ( int i=0; i<selLines.length; i++ ){
                            //The position to be deleted will vary at the moment at a line is deleted
                            alCosulDB.remove(Integer.valueOf(alConsulSearchResults.get(selLines[i]).getPosition()) - iCnt);
                            iCnt++;
                        }
                    }
                JOptionPane.showMessageDialog(this,"The selected line(s) were deleted from the Consults Data Base.\nCHANGES WILL NOT AFFECT THE DATA BASE UNTIL YOU SAVE.");
            }
            else {
                JOptionPane.showMessageDialog(this,"No changes applied to the Data Base.");
            }
            this.jtxtDBSearch.setText("");
            //Resets the data base chart sort
            jtblDataBase.getRowSorter().setSortKeys(null);
            //Reloads the data base in the screen and updates the QTYs
            cleanConsultsDBTable();
            //Loads the information from the Consults DB ArrayList of current consults into de Consults DB JTable
            loadConsultsDBTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting one line at least");
        }
    }//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAdd1ActionPerformed
        addConsultsNewLine();
    }//GEN-LAST:event_jbtnAdd1ActionPerformed

    private void jlstTasksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlstTasksMouseClicked
        String sTask = this.jlstTasks.getSelectedItem();
        String sInfo = getTaskInfo(sTask);
        jlblTaskInfo.setText(sInfo);
    }//GEN-LAST:event_jlstTasksMouseClicked

    private void jbtnExpDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExpDBActionPerformed
        cls_Excel_Manager tmpXLS = new cls_Excel_Manager();
        try {
            if ( bDBFLAG ==false ){
                tmpXLS.exportConsultsDBtoCSVFile(this.alConsulSearchResults);
            }
            else {
                tmpXLS.exportConsultsDBtoCSVFile(this.alCosulDB);
            }
        }
        catch (WriteException ex) {
            Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtnExpDBActionPerformed

    private void jbtnBOImpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBOImpActionPerformed
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
            cleanBackordersTable();
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
                cleanBackordersTable();
                //loads the BO table from the info in the recently updated ArrayList
                loadBackordersTable();
                //Shows the final summary message
                if ( iNEW > 0 && iCHK > 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                            + "New Lines added: " + iNEW + "\n"
                            + "Lines to be checked: " + iCHK + "\n" 
                            + "IMPORTANT: CHANGES WILL NOT BE REFLECTED UNTIL YOU SAVE THE DATA BASE.");
                }
                if ( iNEW > 0 && iCHK == 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                            + "New Lines added: " + iNEW + "\n"
                            + "No lines to be checked.\n" 
                            + "IMPORTANT: CHANGES WILL NOT BE REFLECTED UNTIL YOU SAVE THE DATA BASE.");
                }
                if ( iNEW == 0 && iCHK > 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                            + "No new lines added.\n"
                            + "Lines to be checked: " + iCHK + "\n" 
                            + "IMPORTANT: CHANGES WILL NOT BE REFLECTED UNTIL YOU SAVE THE DATA BASE.");
                }
                if ( iNEW == 0 && iCHK == 0 ){
                    JOptionPane.showMessageDialog(this, "The Backorders Data Base has been updated:\n"
                            + "No new lines added.\n"
                            + "No lines to be checked.\n");
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "The provided Excel file does not contain the necessary columns. Please double check", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jbtnBOImpActionPerformed

    private void jbtnWImpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWImpActionPerformed
        CleanLists();
        xlsWebADIMatrix = null;
        System.out.println("Preparing to import Excel WebADI data sheet");
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
            cleanWebADITable();
            //Gets the first Sheet of the File -if it exists-
            Sheet sh = xlsManager.createExcelSheet(fl);
            //Loads a Bidimentional Array with that Sheet
            xlsWebADIMatrix = xlsManager.loadXLSsheet_toArray(sh);
            //Identifyies the different columns on the BD-Array
            locateWebADIColumns();
            if ( validateWebADIXLSFile() == true ){//If the Excel file is valid, then...
                JOptionPane.showMessageDialog(this,"The File was successfully imported\n\n"
                        + "IMPORTANT: CHANGES IN ANY OF THE DATA BASES\nWILL NOT BE REFLECTED UNTIL YOU SAVE THEM");
                sISO_SIMI_Report = "";
                //Updates Historical data base ArrayList with the entries from the Excel file
                loadWebADIDBfromXLS();
                loadWebADITable();
                //Resets and reloads the Backorders Table with the updates that came after loading the WebADI
                cleanBackordersTable();
                loadBackordersTable();
                //SHOW POUP WINDOW WITH SUMMARY
                gui_InfoNotes tmpIN = new gui_InfoNotes("BACKORDERS-DB UPDATE SUMMARY: \n\n" + sISO_SIMI_Report);
                tmpIN.setLocationRelativeTo(this);
                tmpIN.setTitle("BACKORDERS UPDATE");
                tmpIN.setVisible(true);
                //Resets the Report and prepares it for the next import process
                sISO_SIMI_Report = "";
            }
            else {
                JOptionPane.showMessageDialog(this, "The provided Excel file does not contain the necessary columns. Please double check", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jbtnWImpActionPerformed

    private void jbtnWSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWSaveActionPerformed
        if ( bONLINE == true ){
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite the remote WebADI Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));                
                //Updates the ArrayList with the info from the JTable
                updateWebADIALDataBase();
                //Updates the remote .txt DB file with the info from the ArrayList 
                uploadRemWebADIDB();
                //Updates screen counters
                loadRemWebADIQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
        else{
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite your local WebADI Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Updates the ArrayList with the info from the JTable
                updateWebADIALDataBase();
                //Updates the .txt DB file with the info from the ArrayList 
                updateWebADITXTDataBase();
                //Updates screen counters
                loadWebADIQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }        
    }//GEN-LAST:event_jbtnWSaveActionPerformed

    private void jbtnWSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWSearchActionPerformed
        if ( jtxtWASearch.getText().equals("") ) {
            this.cleanWebADITable();
            this.loadWebADITable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else {
            searchTextWebADIDB(jtxtWASearch.getText().toUpperCase());
        }
    }//GEN-LAST:event_jbtnWSearchActionPerformed

    private void jtxtWASearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWASearchKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            if ( jtxtWASearch.getText().equals("") ) {
            this.cleanWebADITable();
            this.loadWebADITable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else {
                searchTextWebADIDB(jtxtWASearch.getText().toUpperCase());
            }
        }
    }//GEN-LAST:event_jtxtWASearchKeyPressed

    private void jbtnWRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWRefreshActionPerformed
        this.jtblWebADI.getRowSorter().setSortKeys(null);
        cleanWebADITable();
        jtxtWASearch.setText("");
        //Loads the information from the WebADI Data Base ArrayList into the corresponding JTable
        loadWebADITable();
        loadConsultsDBTable();
        if ( bONLINE == true ){
            loadRemWebADIQTYHist();;
        }
        else{
            loadWebADIQTYHist();;
        }
        JOptionPane.showMessageDialog(this,"The Data has been refreshed");
    }//GEN-LAST:event_jbtnWRefreshActionPerformed

    private void jbtnWDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWDelActionPerformed
        //Captures the selected Lines
        int[] selLines = this.jtblWebADI.getSelectedRows();
        for ( int i=0; i<selLines.length; i++ ){
            System.out.println("Line selected: " + selLines[i]);
        }
        if ( selLines.length > 0 ){
            int opc = JOptionPane.showConfirmDialog(this,"THE SELECTED LINE(s) WILL BE DELETED.\n Do you want to proceed.");
            if ( opc == 0 ){
                //Checks if the current data in the screen is the actual DB or just searching results
                if ( bWAFLAG == true ) {
                    System.out.println("Flag: " + bWAFLAG);
                    for ( int i=0; i<selLines.length; i++ ){
                        //Determines the position of the highlighted lines in the main WebADI Array List
                        int iDelPos = findWebADIDBPos(captureWebADILine(selLines[i]));
                        System.out.println("LIne to delete: " + iDelPos);
                        //Captures the data in the highlighted line; finds its position into the BD Array List and deletes it
                        alWebadiDB.remove(iDelPos);
                        }
                    }
                    else {
                        System.out.println("Flag: " + bWAFLAG);
                        int iCnt = 0;
                        for ( int i=0; i<selLines.length; i++ ){
                            //The position to be deleted will vary at the moment at a line is deleted
                            this.alWebadiDB.remove(Integer.valueOf(alWebadiSearchResults.get(selLines[i]).getPos()) - iCnt);
                            iCnt++;
                        }
                    }
                JOptionPane.showMessageDialog(this,"The selected line(s) were deleted from your local WebADI Data Base.\nCHANGES WILL NOT AFFECT THE DATA BASE UNTIL YOU SAVE.");
            }
            else {
                JOptionPane.showMessageDialog(this,"No changes applied to the Data Base.");
            }
            jtxtWASearch.setText("");
            //Resets the data base chart sort
            jtblWebADI.getRowSorter().setSortKeys(null);
            //Reloads the data base in the screen and updates the QTYs
            cleanWebADITable();
            loadWebADITable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting one line at least");
        }
    }//GEN-LAST:event_jbtnWDelActionPerformed

    private void jbtnWAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWAddActionPerformed
        addWebADINewLine();
    }//GEN-LAST:event_jbtnWAddActionPerformed

    private void jbtnWExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWExportActionPerformed
        cls_Excel_Manager tmpXLS = new cls_Excel_Manager();
        try {
            if ( bWAFLAG ==false ){
                tmpXLS.exportWebADIDBtoCSVFile(this.alWebadiSearchResults);
            }
            else {
                tmpXLS.exportWebADIDBtoCSVFile(this.alWebadiDB);
            }
        }
        catch (WriteException ex) {
            Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtnWExportActionPerformed

    private void jbtnPlnDskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPlnDskActionPerformed
        openLink("https://global-ebusiness.oraclecorp.com:443/OA_HTML/RF.jsp?function_id=1021126&resp_id=1702662&resp_appl_id=523&security_group_id=0&lang_code=US&oas=yKpOwWHKT0rC5MTKcUQtyQ..");
    }//GEN-LAST:event_jbtnPlnDskActionPerformed

    private void jbtnWebADIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnWebADIActionPerformed
        openLink("https://global-ebusiness.oraclecorp.com/OA_HTML/RF.jsp?function_id=1705633&resp_id=1702662&resp_appl_id=523&security_group_id=0&lang_code=US&oas=a7o9de913PKpCuRYY_Q4bA..");
    }//GEN-LAST:event_jbtnWebADIActionPerformed

    private void jbtn2ndHopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn2ndHopActionPerformed
        openLink("https://global-vcp.oraclecorp.com:443/OA_HTML/RF.jsp?function_id=40814&resp_id=50706&resp_appl_id=724&security_group_id=0&lang_code=US&oas=VVEux5NBpWUy36upRChIrw..");
    }//GEN-LAST:event_jbtn2ndHopActionPerformed

    private void jbtnEndecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEndecaActionPerformed
        openLink("https://eidap.oraclecorp.com/endeca/web/home/index");
    }//GEN-LAST:event_jbtnEndecaActionPerformed

    private void jbtnODSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnODSActionPerformed
        openLink("https://gcwap-ods.oraclecorp.com/analytics/saw.dll?bieehome");
    }//GEN-LAST:event_jbtnODSActionPerformed

    private void jbtnTracksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTracksActionPerformed
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
    }//GEN-LAST:event_jbtnTracksActionPerformed

    private void jbtnPurFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPurFSActionPerformed
        openLink("https://global-ebusiness.oraclecorp.com:443/OA_HTML/RF.jsp?function_id=1644&resp_id=1682660&resp_appl_id=30027&security_group_id=0&lang_code=US&oas=z-icV0wf8-T7cignfiThLA..");
    }//GEN-LAST:event_jbtnPurFSActionPerformed

    private void jbtnBOExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBOExpActionPerformed
        cls_Excel_Manager tmpXLS = new cls_Excel_Manager();
        try {
            if ( bBOFLAG ==false ){
                tmpXLS.exportBackordersDBtoCSVFile(this.alBckordSearchResults);
            }
            else {
                tmpXLS.exportBackordersDBtoCSVFile(this.alBckordDB);
            }
        }
        catch (WriteException ex) {
            Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtnBOExpActionPerformed

    private void jbtnBORefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBORefreshActionPerformed
        this.jtblBackorders.getRowSorter().setSortKeys(null);
        cleanBackordersTable();
        jtxtBOSearch.setText("");
        //Loads the information from the Backorders Data Base ArrayList into the corresponding JTable
        loadBackordersTable();
        if ( bONLINE == true ){
            loadRemBackordersQTYHist();
        }
        else{
            loadBackordersQTYHist();
        }
        JOptionPane.showMessageDialog(this,"The Data has been refreshed");
    }//GEN-LAST:event_jbtnBORefreshActionPerformed

    private void jbtnBOSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBOSearchActionPerformed
        if ( jtxtBOSearch.getText().equals("") ) {
            this.cleanBackordersTable();
            this.loadBackordersTable();
            JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else {
            searchTextBackordersDB(jtxtBOSearch.getText().toUpperCase());
            //Checks if the process detected results or not
            if ( alBckordSearchResults.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "VALUE NOT FOUND");
            }
            else {
                JOptionPane.showMessageDialog(this, alBckordSearchResults.size() + " ENTRIES FOUND IN THE DATA BASE");
            }
        }
    }//GEN-LAST:event_jbtnBOSearchActionPerformed

    private void jtxtBOSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBOSearchKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            if ( jtxtBOSearch.getText().equals("") ) {
                this.cleanBackordersTable();
                this.loadBackordersTable();
                JOptionPane.showMessageDialog(this, "Please make sure of typing a valid text in the Search field.","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else {
                searchTextBackordersDB(jtxtBOSearch.getText().toUpperCase());
            }
        }
    }//GEN-LAST:event_jtxtBOSearchKeyPressed

    private void jbtnViewTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnViewTaskActionPerformed
        jtbpMain.add("Backorders DB", this.jpnlBackorders);
        jtbpMain.removeTabAt(0);
        jtbpMain.removeTabAt(0);
        jtbpMain.removeTabAt(0);
        jbtnSwitch.setText("New Consults");
        jbtnRST.setEnabled(true);
        jbtnCom.setEnabled(false);//Disables the comments button
        iMODE = 3;//This value indicates the System that this comes from a Task quick view
        jtxtBOSearch.setText(jlstTasks.getSelectedItem());
        searchTextBackordersDB(jlstTasks.getSelectedItem());
        
    }//GEN-LAST:event_jbtnViewTaskActionPerformed

    private void jbtnBOSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBOSaveActionPerformed
        if ( bONLINE == true ){
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite the remote Backorders Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Updates the ArrayList with the info from the JTable
                updateBackordersALDataBase();
                //Updates the remote .txt DB file with the info from the ArrayList 
                uploadRemBackordersDB();
                //Updates screen counters
                loadRemBackordersQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
        else{
            int opc = JOptionPane.showConfirmDialog(this,"WARNING: This action will overwrite your local Backorders Data Base?\n Do you want to continue?");
            if ( opc == 0 ){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Updates the ArrayList with the info from the JTable
                updateBackordersALDataBase();
                //Updates the .txt DB file with the info from the ArrayList 
                updateBackordersTXTDataBase();
                //Updates screen counters
                loadBackordersQTYHist();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_jbtnBOSaveActionPerformed

    private void jbtnBODelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBODelActionPerformed
        //Captures the selected Lines
        int[] selLines = jtblBackorders.getSelectedRows();
        if ( selLines.length > 0 ){
            int opc = JOptionPane.showConfirmDialog(this,"THE SELECTED LINE(s) WILL BE DELETED.\n Do you want to proceed.");
            if ( opc == 0 ){
                //Checks if the current data in the screen is the actual DB or just searching results
                if ( bBOFLAG == true ) {
                    for ( int i=0; i<selLines.length; i++ ){
                        //Determines the position of the highlighted lines in the main DB Array List
                        int iDelPos = findBODBPos(captureBOLine(selLines[i]));
                        //Captures the data in the highlighted line; finds its position into the BD Array List and deletes it
                        alBckordDB.remove(iDelPos);
                    }
                }
                else {
                    int iCnt = 0;
                    for ( int i=0; i<selLines.length; i++ ){
                        //The position to be deleted will vary at the moment at a line is deleted
                        alBckordDB.remove(Integer.valueOf(alBckordSearchResults.get(selLines[i]).getPosi()) - iCnt);
                        iCnt++;
                    }
                }
                JOptionPane.showMessageDialog(this,"The selected line(s) were deleted from your local Backorders Data Base.\nCHANGES WILL NOT AFFECT THE DATA BASE UNTIL YOU SAVE.");
            }
            else {
                JOptionPane.showMessageDialog(this,"No changes applied to the Data Base.");
            }
            this.jtxtBOSearch.setText("");
            //Resets the data base chart sort
            jtblBackorders.getRowSorter().setSortKeys(null);
            //Reloads the data base in the screen and updates the QTYs
            cleanBackordersTable();
            loadBackordersTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting one line at least");
        }
        
        /*OLD BUTTON METHOD
        if ( this.jtblBackorders.getSelectedRow() > -1 ){
            int opc = JOptionPane.showConfirmDialog(this,"THE SELECTED LINE WILL BE DELETED.\n Do you want to proceed.");
            if ( opc == 0 ){
                //Checks if the current data in the screen is the actual DB or just searching results
                if ( bBOFLAG == true ) {
                    //Determines the position of the highlighted line in the main DB Array List
                    int iDelPos = findBODBPos(captureBOLine(this.jtblBackorders.getSelectedRow()));
                    //Captures the data in the highlighted line; finds its position into the BD Array List and deletes it
                    alBO_DB.remove(iDelPos);
                }
                else {
                    //Determines the position of the highlighted line in the main DB Array List
                    int iDelPos = Integer.valueOf(alBOSearchResults.get(jtblBackorders.getSelectedRow()).getPosi());
                    alBO_DB.remove(iDelPos);
                }
                JOptionPane.showMessageDialog(this,"The selected line was deleted from your local Backorders Data Base.\nCHANGES WILL NOT AFFECT THE DATA BASE UNTIL YOU SAVE.");
            }
            else {
                JOptionPane.showMessageDialog(this,"No changes applied to the Data Base.");
            }
            this.jtxtBOSearch.setText("");
            //Resets the data base chart sort
            jtblBackorders.getRowSorter().setSortKeys(null);
            //Reloads the data base in the screen and updates the QTYs
            cleanBackordersTable();
            loadBackordersTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting a line");
        }
        */
    }//GEN-LAST:event_jbtnBODelActionPerformed

    private void jbtnBOAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBOAddActionPerformed
        addBONewLine();
    }//GEN-LAST:event_jbtnBOAddActionPerformed

    private void jbtnSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSwitchActionPerformed
        switch (iMODE){//1. Working on Consults 2. Working on Data Bases
            case 1: {
                jtbpMain.add("Consults DB", this.jpnlDataBase);
                jtbpMain.add("Backorders DB", this.jpnlBackorders);
                jtbpMain.add("WebADI DB", this.jpnlWebADI);
                jtbpMain.removeTabAt(0);
                jtbpMain.removeTabAt(0);
                jtbpMain.removeTabAt(0);
                jbtnSwitch.setText("New Consults");
                jbtnRST.setEnabled(true);//Enables de Reset button
                jbtnCom.setEnabled(true);//Enables the comments button
                iMODE = 2;//Tells the System that the User is currently working on the Data Bases
                break;
            }
            case 2: {
                jtbpMain.add("Good New Search", this.jpnlMain);
                jtbpMain.add("Selection Tool", this.jpnlTools);
                jtbpMain.add("Consults List", this.jpnlConsults);
                jtbpMain.removeTabAt(0);
                jtbpMain.removeTabAt(0);
                jtbpMain.removeTabAt(0);
                jbtnSwitch.setText("Data Bases");
                jbtnRST.setEnabled(false);//Disables de Reset button
                jbtnCom.setEnabled(true);//Enables the comments button
                iMODE = 1;//Tells the System that the User is currently working on New Consults
                break;
            }
            case 3: {//This value indicates the System that this comes from a Task quick view
                jtbpMain.add("Good New Search", this.jpnlMain);
                jtbpMain.add("Selection Tool", this.jpnlTools);
                jtbpMain.add("Consults List", this.jpnlConsults);
                jtbpMain.setSelectedIndex(2);
                jtbpMain.removeTabAt(0);
                jbtnSwitch.setText("Data Bases");
                jbtnRST.setEnabled(false);//Disables de Reset button
                jbtnCom.setEnabled(true);//Enables the comments button
                iMODE = 1;//Tells the System that the User is currently working on New Consults
                break;
            }
        }
    }//GEN-LAST:event_jbtnSwitchActionPerformed

    private void jbtnBOMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBOMailActionPerformed
        int iRow = jtblBackorders.getSelectedRow();
        if ( iRow > -1 ){
            try {sendMail("", "", jtblBackorders.getValueAt(jtblBackorders.getSelectedRow(), 25).toString(), "", "na", "na");}
            catch (IOException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);} 
            catch (URISyntaxException ex) {Logger.getLogger(gui_MainScreen.class.getName()).log(Level.SEVERE, null, ex);}
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting a line");
        }
    }//GEN-LAST:event_jbtnBOMailActionPerformed

    private void jbtnComActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnComActionPerformed
        int iPos; //Variable: Line position in the Backorders Data Base arraylist
        //Checks if there is a selected line or not
        if ( this.jtblBackorders.getSelectedRow() > -1 ){//If therre is a selected line
            //Checks if the info in the screen is the whole DB or just searching results
            //Then identifies the position of the object into the main BO DB ArrayList
            if ( bBOFLAG == true ){//If the screen is showing the whole DB
                cls_BO_Data bodLine = captureBOLine(this.jtblBackorders.getSelectedRow());
                iPos = findBODBPos(bodLine);
            }
            else{//If the screen is showing searching results
                iPos = Integer.valueOf(alBckordSearchResults.get(jtblBackorders.getSelectedRow()).getPosi());
            }
            System.out.println("BO selected line: " + jtblBackorders.getSelectedRow());
            System.out.println("BODB object position: " + iPos);
            //Calls the Commnent Edition screen and prepares it with the necessary data
            gui_Edition_Window tmpEW = new gui_Edition_Window(alBckordDB, iPos, jtblBackorders.getSelectedRow(), bBOFLAG, jtxtBOSearch.getText(), sLocBoDBPath, 
                    bONLINE, sRemBoDBPath, sUser, sPass, sVer, sName, sPriv);
            tmpEW.setLocationRelativeTo(this);
            tmpEW.setVisible(true);
            //Temporary closes the DB main screen
            dispose();
        }
        else{//If no line is selected
            JOptionPane.showMessageDialog(this, "Please make sure of selecting a line");
        }
    }//GEN-LAST:event_jbtnComActionPerformed

    private void jbtnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLogoutActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to go back to the Login Screen?\n");
        if ( opc == 0 ){
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String sUnsavedDB = "";
            if ( checkForUnsavedChanges("Consults") == true ){
                sUnsavedDB = sUnsavedDB + " CONSULTS";
            }
            if ( checkForUnsavedChanges("Backorders") == true ){
                if ( sUnsavedDB.equals("") ){
                    sUnsavedDB = sUnsavedDB + " BACKORDERS";
                }
                else{
                    sUnsavedDB = sUnsavedDB + ", BACKORDERS";
                }
            }
            if ( checkForUnsavedChanges("WebADI") == true ){
                if ( sUnsavedDB.equals("") ){
                    sUnsavedDB = sUnsavedDB + " WEBADI.";
                }
                else{
                    sUnsavedDB = sUnsavedDB + ", WEBADI.";
                }
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if ( !sUnsavedDB.equals("") ){
                opc = JOptionPane.showConfirmDialog(this, "We have detected that the following Data Bases contain unsaved changes:" + sUnsavedDB + "\n"
                        + "Click on YES if you still want to logout discarding these changes or click NO and go back to save the data");
                if ( opc == 0 ){
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    JOptionPane.showMessageDialog(this,"No changes are being saved.");
                    //Backsups the data from the active ArrayList into the local HDD
                    updateConsultsTXTDataBase();
                    updateBackordersTXTDataBase();
                    updateWebADITXTDataBase();
                    gui_LoginScreen tmpLS = new gui_LoginScreen();
                    tmpLS.setLocationRelativeTo(this);
                    tmpLS.setVisible(true);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    dispose();
                }
            }
            else{
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Backsups the data from the active ArrayList into the local HDD
                updateConsultsTXTDataBase();
                updateBackordersTXTDataBase();
                updateWebADITXTDataBase();
                gui_LoginScreen tmpLS = new gui_LoginScreen();
                tmpLS.setLocationRelativeTo(this);
                tmpLS.setVisible(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                dispose();
            }
        }
    }//GEN-LAST:event_jbtnLogoutActionPerformed

    private void jbtnTrkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTrkActionPerformed
        if ( this.jtblBackorders.getSelectedRow() > -1 ){
            getMailTrackings();
        }
        else{//If no line is selected
            JOptionPane.showMessageDialog(this, "Please make sure of selecting a line");
        }
    }//GEN-LAST:event_jbtnTrkActionPerformed

    private void jmeiQuickTracksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmeiQuickTracksActionPerformed
        QuickTracks tmpQT = new QuickTracks("");
        tmpQT.txtTracNumb.setText("");
        tmpQT.setLocationRelativeTo(this);
        tmpQT.setResizable(false);
        tmpQT.setVisible(true);
    }//GEN-LAST:event_jmeiQuickTracksActionPerformed

    private void jbtnRSTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRSTActionPerformed
        switch (jtbpMain.getSelectedIndex()){
            case 0 :{ //This is the Consults DB tab selected
                int opc = JOptionPane.showConfirmDialog(this,"Do you want to reset the Consults Data to its most recent saved version?\n");
                if ( opc == 0 ){
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    this.jtblDataBase.getRowSorter().setSortKeys(null);
                    if ( bONLINE == true ){
                        //Loads the Consults Data Base from the Beehive remot .txt file into the Consults data base ArrayList
                        loadRemConsulDB();
                        loadRemConsQTYHist();
                    }
                    else{
                        //Loads the Consults Data Base from the HDD's local .txt file into the Consults data base ArrayList
                        loadConsulDB();
                        loadConsultsQTYHist();
                    }
                    cleanConsultsDBTable();
                    jtxtDBSearch.setText("");
                    //Loads the information from the Consults Data Base ArrayList into the corresponding JTable
                    loadConsultsDBTable();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    JOptionPane.showMessageDialog(this,"The Consults Data has been reseted and reloaded");
                }
                else{
                    JOptionPane.showMessageDialog(this,"No changes implemented in the Consults Data Base so far");
                }
                break;  
            }
            case 1 :{ //This is the Backorders DB tab selected
                int opc = JOptionPane.showConfirmDialog(this,"Do you want to reset the Backorders Data to its most recent saved version?\n");
                if ( opc == 0 ){
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    this.jtblBackorders.getRowSorter().setSortKeys(null);
                    //Loads the Backorders Data Base from the HDD's local .txt file into the Backorders data base ArrayList
                    loadBckordDB();
                    cleanBackordersTable();
                    jtxtBOSearch.setText("");
                    //Loads the information from the Backorders Data Base ArrayList into the corresponding JTable
                    loadBackordersTable();
                    loadBackordersQTYHist();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    JOptionPane.showMessageDialog(this,"The Backorders Data has been reseted and reloaded");
                }
                else{
                    JOptionPane.showMessageDialog(this,"No changes implemented in the Backorders Data Base so far");
                }
                break;
            }
            case 2 :{ //This is the WebADI DB tab selected
                int opc = JOptionPane.showConfirmDialog(this,"Do you want to reset the WebADI Data to its most recent saved version?\n");
                if ( opc == 0 ){
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    this.jtblWebADI.getRowSorter().setSortKeys(null);
                    //Loads the WebADI Data Base from the HDD's local .txt file into the WebADI data base ArrayList
                    loadWebadiDB();
                    cleanWebADITable();
                    jtxtWASearch.setText("");
                    //Loads the information from the WebADI Data Base ArrayList into the corresponding JTable
                    loadWebADITable();
                    loadWebADIQTYHist();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    JOptionPane.showMessageDialog(this,"The WebADI Data has been reseted and reloaded");
                }
                else{
                    JOptionPane.showMessageDialog(this,"No changes implemented in the WebADI Data Base so far");
                }
                break;
            }
        }
    }//GEN-LAST:event_jbtnRSTActionPerformed

    private void jmeitUserPrivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmeitUserPrivActionPerformed
        gui_PrivAccessManager tmpAM = new gui_PrivAccessManager(sUser, sPass);
        tmpAM.setLocationRelativeTo(this);
        tmpAM.setVisible(true);
    }//GEN-LAST:event_jmeitUserPrivActionPerformed

    private void jmenDBbackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenDBbackupActionPerformed
        if ( !sUser.equals("juan.carlos.suarez@oracle.com") || !sUser.equals("javier.f.alvarado@oracle.com") ){
            gui_WorkInProgress WIPtmpGUI = new gui_WorkInProgress();
            WIPtmpGUI.setLocationRelativeTo(this);
            WIPtmpGUI.setVisible(true);
        }
        else{
            gui_DataBase_Manager newTMP = new gui_DataBase_Manager(sUser, sPass, sLocCoDBPath, sRemCoDBPath, sLocBoDBPath, sRemBoDBPath, sLocWaDBPath, sRemWaDBPath);
            newTMP.setVisible(true);
        }
        
        
        
        
        
      
    }//GEN-LAST:event_jmenDBbackupActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        gui_DataBase_Manager tmpDBM = new gui_DataBase_Manager(sUser, sPass, sLocCoDBPath, sRemCoDBPath, sLocBoDBPath, sRemBoDBPath, sLocWaDBPath, sRemWaDBPath);
        tmpDBM.jtxtCOloc.setText(getConsultsQTYHist());
        tmpDBM.jtxtCOrem.setText(getRemConsQTYHist());
        tmpDBM.jtxtBOloc.setText(getBackordersQTYHist());
        tmpDBM.jtxtBOrem.setText(getRemBackordersQTYHist());
        tmpDBM.jtxtWAloc.setText(getWebADIQTYHist());
        tmpDBM.jtxtWArem.setText(getRemWebADIQTYHist());
        tmpDBM.setLocationRelativeTo(this);
        tmpDBM.setTitle("DATA BASE MANAGER");
        tmpDBM.setVisible(true);
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jmeitExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmeitExportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmeitExportActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(gui_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui_MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui_MainScreen().setVisible(true);
            }
        });
    }

    //<editor-fold defaultstate="collapsed" desc="Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgrActivity;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JToolBar.Separator jSeparator15;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JButton jbtn2ndHop;
    private javax.swing.JButton jbtnAdd;
    private javax.swing.JButton jbtnAdd1;
    private javax.swing.JButton jbtnBOAdd;
    private javax.swing.JButton jbtnBODel;
    private javax.swing.JButton jbtnBOExp;
    private javax.swing.JButton jbtnBOImp;
    private javax.swing.JButton jbtnBOMail;
    private javax.swing.JButton jbtnBORefresh;
    private javax.swing.JButton jbtnBOSave;
    public javax.swing.JButton jbtnBOSearch;
    private javax.swing.JButton jbtnClearList;
    private javax.swing.JButton jbtnCom;
    private javax.swing.JButton jbtnCreateMails;
    private javax.swing.JButton jbtnDBSearch;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnEndeca;
    private javax.swing.JButton jbtnExit;
    private javax.swing.JButton jbtnExpDB;
    private javax.swing.JButton jbtnImport;
    private javax.swing.JButton jbtnLoad;
    private javax.swing.JButton jbtnLogout;
    private javax.swing.JButton jbtnODS;
    private javax.swing.JButton jbtnPlnDsk;
    private javax.swing.JButton jbtnPurFS;
    private javax.swing.JButton jbtnRST;
    private javax.swing.JButton jbtnReload;
    private javax.swing.JButton jbtnRemove;
    private javax.swing.JButton jbtnReset;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JButton jbtnSwitch;
    private javax.swing.JButton jbtnTracks;
    private javax.swing.JButton jbtnTrk;
    private javax.swing.JButton jbtnViewTask;
    private javax.swing.JButton jbtnWAdd;
    private javax.swing.JButton jbtnWDel;
    private javax.swing.JButton jbtnWExport;
    private javax.swing.JButton jbtnWImp;
    private javax.swing.JButton jbtnWRefresh;
    private javax.swing.JButton jbtnWSave;
    private javax.swing.JButton jbtnWSearch;
    private javax.swing.JButton jbtnWebADI;
    private javax.swing.JLabel jlblBODBsize;
    private javax.swing.JLabel jlblBOFlag;
    private javax.swing.JLabel jlblCntrs;
    private javax.swing.JLabel jlblCntrsQTY;
    private javax.swing.JLabel jlblCons;
    private javax.swing.JLabel jlblConsCount;
    private javax.swing.JLabel jlblConsults;
    private javax.swing.JLabel jlblDBFlag;
    private javax.swing.JLabel jlblLineQTY;
    private javax.swing.JLabel jlblLines;
    private javax.swing.JLabel jlblMails;
    private javax.swing.JLabel jlblOrgTot;
    private javax.swing.JLabel jlblOrgs;
    private javax.swing.JLabel jlblOrgsQTY;
    private javax.swing.JLabel jlblParts;
    private javax.swing.JLabel jlblPrts;
    private javax.swing.JLabel jlblPrtsQTY;
    private javax.swing.JLabel jlblRegs;
    private javax.swing.JLabel jlblRegsQTY;
    public javax.swing.JLabel jlblSta;
    private javax.swing.JLabel jlblTaskInfo;
    private javax.swing.JLabel jlblTickets;
    private javax.swing.JLabel jlblTiers;
    private javax.swing.JLabel jlblTop;
    private javax.swing.JLabel jlblUser;
    private javax.swing.JLabel jlblWADBsize;
    private javax.swing.JLabel jlblWAFlag;
    private java.awt.List jlstCountries;
    private java.awt.List jlstOrgsTots;
    private java.awt.List jlstParts;
    private java.awt.List jlstRegions;
    private java.awt.List jlstTasks;
    private java.awt.List jlstTiers;
    private javax.swing.JMenuBar jmbrTopMenu;
    private javax.swing.JMenuItem jmeiQuickTracks;
    private javax.swing.JMenuItem jmeitExit;
    private javax.swing.JMenuItem jmeitExport;
    private javax.swing.JMenuItem jmeitImport;
    private javax.swing.JMenuItem jmeitOpt;
    private javax.swing.JMenu jmeitTempTools;
    private javax.swing.JMenuItem jmeitUserPriv;
    private javax.swing.JMenu jmenAbout;
    private javax.swing.JMenuItem jmenDBbackup;
    private javax.swing.JMenu jmenEdit;
    private javax.swing.JMenu jmenFile;
    private javax.swing.JMenu jmenTools;
    private javax.swing.JMenuItem jmiAbout;
    private javax.swing.JPanel jpnlBackorders;
    private javax.swing.JPanel jpnlBottom;
    private javax.swing.JPanel jpnlConsults;
    private javax.swing.JPanel jpnlDataBase;
    private javax.swing.JPanel jpnlMain;
    private javax.swing.JPanel jpnlMiddle;
    private javax.swing.JPanel jpnlSelection;
    private javax.swing.JPanel jpnlTasks;
    private javax.swing.JPanel jpnlTools;
    private javax.swing.JPanel jpnlTop;
    private javax.swing.JPanel jpnlWebADI;
    private javax.swing.JToolBar jtbarMain;
    public javax.swing.JTable jtblBackorders;
    private javax.swing.JTable jtblConsults;
    private javax.swing.JTable jtblDataBase;
    private javax.swing.JTable jtblParts;
    private javax.swing.JTable jtblWebADI;
    public javax.swing.JTabbedPane jtbpMain;
    public javax.swing.JTextField jtxtBOSearch;
    private javax.swing.JTextField jtxtDBSearch;
    private javax.swing.JTextField jtxtWASearch;
    private javax.swing.JLabel lblMailHist;
    private javax.swing.JLabel lblTickHist;
    private javax.swing.JRadioButton rbtnBack;
    private javax.swing.JRadioButton rbtnReplen;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}

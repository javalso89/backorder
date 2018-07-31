
package gn_righthand;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
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
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import sun.misc.BASE64Encoder;


public class gui_DataBase_Manager extends javax.swing.JFrame {
    
    //User credentials
    private String sUser;
    private String sPass;
    
    //Data Base locations
    private String sLocCoDBPath;
    private String sRemCoDBPath;
    private String sLocBoDBPath;
    private String sRemBoDBPath;
    private String sLocWaDBPath;
    private String sRemWaDBPath;
    
    //ArrayList that will store the complete data base of consults
    private ArrayList<cls_GNSearchLine> alCosulDB = new ArrayList<>();
    //ArrayList that will store the complete data base of WebADI entries
    private ArrayList<cls_WebADI_Data> alWebadiDB = new ArrayList<>();
    //ArrayList that will store the complete data base of Backorders entries
    private ArrayList<cls_BO_Data> alBckordDB = new ArrayList<>();
    
    private int iCoQTY = 0;
    private int iWaQTY = 0;
    private int iBoQTY = 0;
    private int iMaQTY = 0;
    
    
    
    public gui_DataBase_Manager(String sUser, String sPass, String sLocCoDBPath, String sRemCoDBPath,
            String sLocBoDBPath, String sRemBoDBPath,
            String sLocWaDBPath, String sRemWaDBPath) {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        
        this.sUser = sUser;
        this.sPass = sPass;
        
        
        //Loads the data bases locations
        this.sLocCoDBPath = sLocCoDBPath;
        this.sRemCoDBPath = sRemCoDBPath;
        this.sLocBoDBPath = sLocBoDBPath;
        this.sRemBoDBPath = sRemBoDBPath;
        this.sLocWaDBPath = sLocWaDBPath;
        this.sRemWaDBPath = sRemWaDBPath; 
        
        
        
        

        
        
    }

    private gui_DataBase_Manager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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
                //alCosulDB.add(new cls_GNSearchLine(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sPrtMvd, sTsk, sTracking, "NA"));
                chain = br.readLine();
            }
            chain = br.readLine();
            iCoQTY = Integer.valueOf(chain);
            chain = br.readLine();
            chain = br.readLine();
            iMaQTY = Integer.valueOf(chain);
            br.close();
            fr.close();
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
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"));
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
    
    
    //LOADING THE DB ARRAYLISTS FROM REMOTE .TXT FILES
        
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
                    //alCosulDB.add(new cls_GNSearchLine(sTir, sReg, sCnt, sOrg, sPrt, sQty, sAct, sGOH, sGXS, sDat, sDOM, sPrtMvd, sTsk, sTracking, "NA"));
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
                        sIsMB, sAwMB, sSIMI, sTkNt, sBOMT, sTrak, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"));
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
        
    }
    //</editor-fold>
    
        
    //UPLOADING THE ARRAYLISTS TO THE .TXT DATA BASES
    
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
                for ( cls_GNSearchLine tmp : alCosulDB ){
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
            for(cls_GNSearchLine tmp: this.alCosulDB)
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
            wr.close();
            bw.close();
            fw.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(this,"There was an error while updating the local WebADI .TXT Data Base \n"
                + "Method: updateWebADITXTDataBase()\n" + e, "GN RIGHTHAND", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    
    /*
    public void setMaximized(boolean maximized){
    if(maximized){
        DisplayMode mode = this.getGraphicsConfiguration().getDevice().getDisplayMode();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
        this.setMaximizedBounds(new Rectangle(
                mode.getWidth() - insets.right - insets.left, 
                mode.getHeight() - insets.top - insets.bottom
        ));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }else{
            this.setExtendedState(JFrame.NORMAL);
        }
    }
    */

    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlTop = new javax.swing.JPanel();
        jlblTittle = new javax.swing.JLabel();
        jpnlCO = new javax.swing.JPanel();
        jlblCOloc = new javax.swing.JLabel();
        jlblCOrem = new javax.swing.JLabel();
        jtxtCOloc = new javax.swing.JTextField();
        jtxtCOrem = new javax.swing.JTextField();
        jsepCO = new javax.swing.JSeparator();
        jlblCOsrc = new javax.swing.JLabel();
        jlblCOdes = new javax.swing.JLabel();
        jcboxCOloc = new javax.swing.JComboBox<>();
        jcboxCOrem = new javax.swing.JComboBox<>();
        jbtnCObu = new java.awt.Button();
        jpnlBO = new javax.swing.JPanel();
        jlblBOloc = new javax.swing.JLabel();
        jtxtBOloc = new javax.swing.JTextField();
        jlblBOrem = new javax.swing.JLabel();
        jtxtBOrem = new javax.swing.JTextField();
        jsepBO = new javax.swing.JSeparator();
        jlblBOsrc = new javax.swing.JLabel();
        jlblBOdes = new javax.swing.JLabel();
        jcboxBOrem = new javax.swing.JComboBox<>();
        jcboxBOloc = new javax.swing.JComboBox<>();
        jbtnBObu = new java.awt.Button();
        jpnlWA = new javax.swing.JPanel();
        jlblWAloc = new javax.swing.JLabel();
        jtxtWAloc = new javax.swing.JTextField();
        jlblWArem = new javax.swing.JLabel();
        jtxtWArem = new javax.swing.JTextField();
        jsepWA = new javax.swing.JSeparator();
        jlblWAsrc = new javax.swing.JLabel();
        jlblWAdes = new javax.swing.JLabel();
        jcboxWArem = new javax.swing.JComboBox<>();
        jcboxWAloc = new javax.swing.JComboBox<>();
        jbtnWAbu = new java.awt.Button();
        jpnlBottom = new javax.swing.JPanel();
        jbtnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpnlTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlblTittle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jlblTittle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblTittle.setText("DATA BASE BACKUP CONTROL");

        javax.swing.GroupLayout jpnlTopLayout = new javax.swing.GroupLayout(jpnlTop);
        jpnlTop.setLayout(jpnlTopLayout);
        jpnlTopLayout.setHorizontalGroup(
            jpnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblTittle, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlTopLayout.setVerticalGroup(
            jpnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblTittle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jpnlTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 540, -1));

        jpnlCO.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Consuls Data Base"));
        jpnlCO.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlblCOloc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblCOloc.setText("LOCAL LINES");
        jpnlCO.add(jlblCOloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 27, 95, -1));

        jlblCOrem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblCOrem.setText("REMOTE LINES");
        jpnlCO.add(jlblCOrem, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 27, 95, -1));

        jtxtCOloc.setEditable(false);
        jtxtCOloc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpnlCO.add(jtxtCOloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 52, 95, -1));

        jtxtCOrem.setEditable(false);
        jtxtCOrem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpnlCO.add(jtxtCOrem, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 52, 95, -1));

        jsepCO.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlCO.add(jsepCO, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 16, -1, 50));

        jlblCOsrc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblCOsrc.setText("SRC: ");
        jpnlCO.add(jlblCOsrc, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 30, 40, -1));

        jlblCOdes.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblCOdes.setText("DES: ");
        jpnlCO.add(jlblCOdes, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 65, 40, -1));

        jcboxCOloc.addItem("Select an option");
        jcboxCOloc.addItem("LOCAL HDD");
        jcboxCOloc.addItem("REMOTE DATA");
        jpnlCO.add(jcboxCOloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 27, 140, -1));

        jcboxCOrem.addItem("Select an option");
        jcboxCOrem.addItem("LOCAL HDD");
        jcboxCOrem.addItem("REMOTE DATA");
        jpnlCO.add(jcboxCOrem, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 62, 140, -1));

        jbtnCObu.setLabel("Start backup");
        jbtnCObu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCObuActionPerformed(evt);
            }
        });
        jpnlCO.add(jbtnCObu, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 80, 60));

        getContentPane().add(jpnlCO, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 65, 540, -1));

        jpnlBO.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Backorders Data Base"));
        jpnlBO.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlblBOloc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblBOloc.setText("LOCAL LINES");
        jpnlBO.add(jlblBOloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 27, 95, -1));

        jtxtBOloc.setEditable(false);
        jtxtBOloc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpnlBO.add(jtxtBOloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 52, 95, -1));

        jlblBOrem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblBOrem.setText("REMOTE LINES");
        jpnlBO.add(jlblBOrem, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 27, 95, -1));

        jtxtBOrem.setEditable(false);
        jtxtBOrem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpnlBO.add(jtxtBOrem, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 52, 95, -1));

        jsepBO.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlBO.add(jsepBO, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 16, -1, 50));

        jlblBOsrc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblBOsrc.setText("SRC: ");
        jpnlBO.add(jlblBOsrc, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 25, 40, -1));

        jlblBOdes.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblBOdes.setText("DES: ");
        jpnlBO.add(jlblBOdes, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 60, 40, -1));

        jcboxBOrem.addItem("Select an option");
        jcboxBOrem.addItem("LOCAL HDD");
        jcboxBOrem.addItem("REMOTE DATA");
        jpnlBO.add(jcboxBOrem, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 57, 140, -1));

        jcboxBOloc.addItem("Select an option");
        jcboxBOloc.addItem("LOCAL HDD");
        jcboxBOloc.addItem("REMOTE DATA");
        jpnlBO.add(jcboxBOloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 22, 140, -1));

        jbtnBObu.setLabel("Start backup");
        jpnlBO.add(jbtnBObu, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 80, 55));

        getContentPane().add(jpnlBO, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 169, 540, -1));

        jpnlWA.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED), "WebADI Data Base"));
        jpnlWA.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlblWAloc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblWAloc.setText("LOCAL LINES");
        jpnlWA.add(jlblWAloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 27, 95, -1));

        jtxtWAloc.setEditable(false);
        jtxtWAloc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpnlWA.add(jtxtWAloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 52, 95, -1));

        jlblWArem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblWArem.setText("REMOTE LINES");
        jpnlWA.add(jlblWArem, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 27, 95, -1));

        jtxtWArem.setEditable(false);
        jtxtWArem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpnlWA.add(jtxtWArem, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 52, 95, -1));

        jsepWA.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpnlWA.add(jsepWA, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 16, -1, 50));

        jlblWAsrc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblWAsrc.setText("SRC: ");
        jpnlWA.add(jlblWAsrc, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 25, 40, -1));

        jlblWAdes.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblWAdes.setText("DES: ");
        jpnlWA.add(jlblWAdes, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 60, 40, -1));

        jcboxWArem.addItem("Select an option");
        jcboxWArem.addItem("LOCAL HDD");
        jcboxWArem.addItem("REMOTE DATA");
        jpnlWA.add(jcboxWArem, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 57, 140, -1));

        jcboxWAloc.addItem("Select an option");
        jcboxWAloc.addItem("LOCAL HDD");
        jcboxWAloc.addItem("REMOTE DATA");
        jpnlWA.add(jcboxWAloc, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 22, 140, -1));

        jbtnWAbu.setLabel("Start backup");
        jpnlWA.add(jbtnWAbu, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 80, 55));

        getContentPane().add(jpnlWA, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 263, 540, -1));

        jpnlBottom.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbtnExit.setText("Exit");
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnlBottomLayout = new javax.swing.GroupLayout(jpnlBottom);
        jpnlBottom.setLayout(jpnlBottomLayout);
        jpnlBottomLayout.setHorizontalGroup(
            jpnlBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlBottomLayout.createSequentialGroup()
                .addContainerGap(426, Short.MAX_VALUE)
                .addComponent(jbtnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnlBottomLayout.setVerticalGroup(
            jpnlBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnExit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jpnlBottom, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 357, 540, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExitActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to go back to Main Screen?");
        if ( opc == 0 ){
            dispose();
        }
    }//GEN-LAST:event_jbtnExitActionPerformed

    private void jbtnCObuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCObuActionPerformed
        String sSCR = this.jcboxCOloc.getSelectedItem().toString();
        String sDes = this.jcboxCOrem.getSelectedItem().toString();
        int opc;
        if ( sSCR.equals("Select an option") || sDes.equals("Select an option") || sSCR.equals(sDes) ){
            JOptionPane.showMessageDialog(this, "Please make sure of selecting a valid SCR and DES location", "SPARES PLANNING", JOptionPane.ERROR_MESSAGE);
        }
        else{
            alCosulDB.clear();
            if ( sSCR.equals("LOCAL HDD") ){
                opc = JOptionPane.showConfirmDialog(this, "Please confirm that you want to overwrite the REMOTE data with the information in the local data base.");
                if ( opc == 0 ){
                    this.loadConsulDB();
                    this.uploadRemConsDB();
                    JOptionPane.showMessageDialog(this, "The REMOTE data base has been overwritten with the local data.");
                }
                else{
                    JOptionPane.showMessageDialog(this, "No changes were implemented in the data bases");
                }
            }
            else{
                opc = JOptionPane.showConfirmDialog(this, "Please confirm that you want to overwrite the LOCAL data with the information in the remote data base.");
                if ( opc == 0 ){
                    this.loadRemConsulDB();
                    this.updateConsultsTXTDataBase();
                    JOptionPane.showMessageDialog(this, "The LOCAL data base has been overwritten with the remote data.");
                }
                else{
                    JOptionPane.showMessageDialog(this, "No changes were implemented in the data bases");
                }
            }
            
        }
        
        
        
    }//GEN-LAST:event_jbtnCObuActionPerformed

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
            java.util.logging.Logger.getLogger(gui_DataBase_Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui_DataBase_Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui_DataBase_Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui_DataBase_Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui_DataBase_Manager().setVisible(true);
            }
        });
    }

    //<editor-fold defaultstate="collapsed" desc="Variablers Declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button jbtnBObu;
    private java.awt.Button jbtnCObu;
    private javax.swing.JButton jbtnExit;
    private java.awt.Button jbtnWAbu;
    private javax.swing.JComboBox<String> jcboxBOloc;
    private javax.swing.JComboBox<String> jcboxBOrem;
    private javax.swing.JComboBox<String> jcboxCOloc;
    private javax.swing.JComboBox<String> jcboxCOrem;
    private javax.swing.JComboBox<String> jcboxWAloc;
    private javax.swing.JComboBox<String> jcboxWArem;
    private javax.swing.JLabel jlblBOdes;
    private javax.swing.JLabel jlblBOloc;
    private javax.swing.JLabel jlblBOrem;
    private javax.swing.JLabel jlblBOsrc;
    private javax.swing.JLabel jlblCOdes;
    private javax.swing.JLabel jlblCOloc;
    private javax.swing.JLabel jlblCOrem;
    private javax.swing.JLabel jlblCOsrc;
    private javax.swing.JLabel jlblTittle;
    private javax.swing.JLabel jlblWAdes;
    private javax.swing.JLabel jlblWAloc;
    private javax.swing.JLabel jlblWArem;
    private javax.swing.JLabel jlblWAsrc;
    private javax.swing.JPanel jpnlBO;
    private javax.swing.JPanel jpnlBottom;
    private javax.swing.JPanel jpnlCO;
    private javax.swing.JPanel jpnlTop;
    private javax.swing.JPanel jpnlWA;
    private javax.swing.JSeparator jsepBO;
    private javax.swing.JSeparator jsepCO;
    private javax.swing.JSeparator jsepWA;
    public javax.swing.JTextField jtxtBOloc;
    public javax.swing.JTextField jtxtBOrem;
    public javax.swing.JTextField jtxtCOloc;
    public javax.swing.JTextField jtxtCOrem;
    public javax.swing.JTextField jtxtWAloc;
    public javax.swing.JTextField jtxtWArem;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}

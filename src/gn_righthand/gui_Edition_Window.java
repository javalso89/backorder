package gn_righthand;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import sun.misc.BASE64Encoder;


public class gui_Edition_Window extends javax.swing.JFrame {
    
    //VARIABLES DECLARATION//
    
    private String sText;
    private ArrayList<cls_BO_Data> alBOtmp;
    private int iPos;
    private int iSel;
    private boolean bBOFLAG;
    private String sTxtSearch;
    private String sBackordersDBPath;
    private int iBOQTY = 0;

    private String sUser = "";
    private String sPass = "";    
    private String sVER= "";
    private boolean bONLINE = false;
    private String sName = "";
    private String sPriv = "";
    
    private String backURL = "";
    
    
    
    
    //CONSTRUCTORS//
    
    public gui_Edition_Window(ArrayList<cls_BO_Data> alBOtmp, int iPos, int iSel, boolean bBOFLAG, String sTxtSearch, String sBackordersDBPath,
            boolean bONLINE, String backURL, String sUser, String sPass, String sVer, String sName, String sPriv) {
        initComponents();
        setResizable(false);
        //Receiving data from the DB Main screen
        this.alBOtmp = alBOtmp;
        this.iPos = iPos;
        this.iSel = iSel;
        this.bBOFLAG = bBOFLAG;
        this.sTxtSearch = sTxtSearch;
        this.sBackordersDBPath = sBackordersDBPath;
        //Preparing the Text panel for edition
        this.jtxtareEdiPan.setEditable(true);
        this.jtxtareEdiPan.setText(sText);
        
        
        this.sUser = sUser;
        this.sPass = sPass;
        this.bONLINE = bONLINE;
        this.backURL = backURL;
        this.sVER = sVer;
        this.sName = sName;
        this.sPriv = sPriv;
        
        getCommnents();
        getTaskInfo();
        setaccessLevel();
        
    }

    private gui_Edition_Window() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    //MAIN METHODS//
    
    
    //Gets the comments text from the Backorders-DB line indicated by the object position received
    //Shows those comments in the edition field
    private void getCommnents(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sComm = alBOtmp.get(iPos).getComm();
        //Checks if the object at the selected position contains comments or not
        if ( !sComm.equals("NA") ){//If the comments are not empty
            //Substitutes the char '>' with text end of lines '\n'
            sComm = sComm.replaceAll(">", "\n");
        }
        else{//If the comments are empty
            sComm = "";
        }
        jtxtareEdiPan.setText(sComm);
    }
    //</editor-fold>
    
    //Gets the info of the Backorder line
    private void getTaskInfo(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sTaskInfo = "<html><font color='blue'>Request Date: </font>" + alBOtmp.get(iPos).getDate() + " / <font color='blue'>Task: </font>" 
                + alBOtmp.get(iPos).getTask() + " / <font color='blue'>Order number: </font>" + alBOtmp.get(iPos).getISO() + " / <font color='blue'>Part: </font>" 
                + alBOtmp.get(iPos).getItem() + ", QTY: " + alBOtmp.get(iPos).getQty() + ", PLC-" + alBOtmp.get(iPos).getPLC() 
                + ", " + alBOtmp.get(iPos).getCrit() + ", Cond: " + alBOtmp.get(iPos).getCond() + "</html>";
        this.jlblInfo.setText(sTaskInfo);
    }
    //</editor-fold>
        
    //Saves the text -and changes- in the screen into the tmp BO Data Base Arraylist
    private void saveText(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sText = jtxtareEdiPan.getText();
        if ( sText.equals("") ){
            sText = "NA";
        }
        else{
            //Reformats the text to its original form to be saved into the BO data base
            sText= sText.replaceAll("\n", ">");
        }
        //Saves the text into the temp BO Data Base ArrayList
        alBOtmp.get(iPos).setComm(sText);
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
            
            fDataBase = new File (this.sBackordersDBPath); //points to the local .txt Backorders data base file
            fw = new FileWriter(fDataBase);
            bw = new BufferedWriter(fw);
            wr = new PrintWriter(bw);
            
            //Reads, line by line, all the consults that are currently in the Data Base Array List
            for(cls_BO_Data tmp: this.alBOtmp)
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
            iBOQTY = alBOtmp.size();
            wr.println("BO LINES");
            wr.println(String.valueOf(iBOQTY));
            //this.jlblBODBsize.setText("<html>Data Base size:<br>" + iBOQTY + " lines</html>");
            wr.close();
            bw.close();
            fw.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(this,"There was an error while updating the local Backorders .TXT Data Base \n"
                + "Method: updateBackordersTXTDataBase()\n" + e, "GN RIGHTHAND", JOptionPane.ERROR_MESSAGE);}
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
            URL url = new URL(backURL);
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
                for(cls_BO_Data tmp: this.alBOtmp){//It uses the temporary Backorders DB ArrayList
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
                iBOQTY = alBOtmp.size();
                osw.write("BO LINES\n");
                osw.write(String.valueOf(iBOQTY) + "\n");
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
    
    
    
    
    
    
    //Restrics access to different options depending on the User level privilege
    private void setaccessLevel(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        switch ( sPriv ){
            case "Reader" :{
                this.jbtnSav.setEnabled(false);
                this.jtxtareEdiPan.setEditable(false);
                break;
            }
            case "Offline" :{
                this.jbtnSav.setEnabled(false);
                this.jtxtareEdiPan.setEditable(false);
                break;
            }
        }
    }
    //</editor-fold>
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlTop = new javax.swing.JPanel();
        jlblTop = new javax.swing.JLabel();
        jpnlMid = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtareEdiPan = new javax.swing.JTextArea();
        jpnlBot = new javax.swing.JPanel();
        jlblInfo = new javax.swing.JLabel();
        jpnlMidRig = new javax.swing.JPanel();
        jbtnExi = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jbtnSav = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jpnlInfo = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jpnlTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlblTop.setFont(new java.awt.Font("Engravers MT", 0, 18)); // NOI18N
        jlblTop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblTop.setText("gn-righthand edition pane");

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
            .addGroup(jpnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblTop)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnlMid.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jtxtareEdiPan.setColumns(20);
        jtxtareEdiPan.setLineWrap(true);
        jtxtareEdiPan.setRows(5);
        jtxtareEdiPan.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jtxtareEdiPan);

        javax.swing.GroupLayout jpnlMidLayout = new javax.swing.GroupLayout(jpnlMid);
        jpnlMid.setLayout(jpnlMidLayout);
        jpnlMidLayout.setHorizontalGroup(
            jpnlMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlMidLayout.setVerticalGroup(
            jpnlMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jpnlBot.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlblInfo.setText("Info Label");

        javax.swing.GroupLayout jpnlBotLayout = new javax.swing.GroupLayout(jpnlBot);
        jpnlBot.setLayout(jpnlBotLayout);
        jpnlBotLayout.setHorizontalGroup(
            jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlBotLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlBotLayout.setVerticalGroup(
            jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
        );

        jpnlMidRig.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbtnExi.setText("Exit");
        jbtnExi.setToolTipText("Go back to the Data Base Manager ");
        jbtnExi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExiActionPerformed(evt);
            }
        });

        jButton2.setText("Select all");
        jButton2.setEnabled(false);

        jButton3.setText("Delete");
        jButton3.setEnabled(false);

        jbtnSav.setText("Save");
        jbtnSav.setToolTipText("Saves de comments field according with the screen's info");
        jbtnSav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSavActionPerformed(evt);
            }
        });

        jButton5.setText("Undo");
        jButton5.setEnabled(false);

        jButton6.setText("Next");
        jButton6.setEnabled(false);

        jButton7.setText("Previous");
        jButton7.setEnabled(false);

        javax.swing.GroupLayout jpnlMidRigLayout = new javax.swing.GroupLayout(jpnlMidRig);
        jpnlMidRig.setLayout(jpnlMidRigLayout);
        jpnlMidRigLayout.setHorizontalGroup(
            jpnlMidRigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidRigLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlMidRigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnExi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnSav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnlMidRigLayout.setVerticalGroup(
            jpnlMidRigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlMidRigLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(11, 11, 11)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtnSav)
                .addGap(65, 65, 65)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(jbtnExi)
                .addContainerGap())
        );

        jpnlInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jpnlInfoLayout = new javax.swing.GroupLayout(jpnlInfo);
        jpnlInfo.setLayout(jpnlInfoLayout);
        jpnlInfoLayout.setHorizontalGroup(
            jpnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpnlInfoLayout.setVerticalGroup(
            jpnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnlBot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpnlMid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jpnlMidRig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpnlMidRig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnlMid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnlBot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnSavActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSavActionPerformed
        int opc = JOptionPane.showConfirmDialog(this, "Do you want to overwrite the note with the new changes?");
        if ( opc == 0 ){
            //Updates the temp BO ArayList with the comments text
            saveText();
            //Checks if the User is Online or Offline
            //Then, updates the corresponding DB from the temm BO ArrayList
            if ( this.bONLINE == true ){
                uploadRemBackordersDB();
            }
            else{
                updateBackordersTXTDataBase();
            }
            JOptionPane.showMessageDialog(this, "The comments were updated.");
        }
    }//GEN-LAST:event_jbtnSavActionPerformed

    private void jbtnExiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExiActionPerformed
        int opc = JOptionPane.showConfirmDialog(this, "Please make sure of saving changes before going back to the Data Base Manager\nDo you want to exit?");
        if ( opc == 0 ){
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            gui_MainScreen guiMS = new gui_MainScreen(2, bONLINE, sUser, sPass, sVER, sName, sPriv);
            guiMS.setLocationRelativeTo(this);
            guiMS.jtbpMain.setSelectedIndex(1);
            if ( bONLINE == true ){
                guiMS.jlblSta.setForeground(Color.BLACK);
                guiMS.jlblSta.setBackground(new Color(125,245,10));
                guiMS.jlblSta.setText("ONLINE");
                
            }
            else{
                guiMS.jlblSta.setForeground(Color.WHITE);
                guiMS.jlblSta.setBackground(new Color(255,50,0));
                guiMS.jlblSta.setText("OFFLINE");
            }
            
            if ( bBOFLAG == false ){//If the original screen was showing searching results
                guiMS.jtxtBOSearch.setText(sTxtSearch);
                guiMS.searchTextBackordersDB(sTxtSearch);
            }
            guiMS.jtblBackorders.setRowSelectionInterval(iSel, iSel);
            guiMS.jtblBackorders.scrollRectToVisible(guiMS.jtblBackorders.getCellRect(iPos,0,true));
            guiMS.setVisible(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            dispose();
        }
    }//GEN-LAST:event_jbtnExiActionPerformed

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
            java.util.logging.Logger.getLogger(gui_Edition_Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui_Edition_Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui_Edition_Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui_Edition_Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui_Edition_Window().setVisible(true);
            }
        });
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc="Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnExi;
    private javax.swing.JButton jbtnSav;
    private javax.swing.JLabel jlblInfo;
    private javax.swing.JLabel jlblTop;
    private javax.swing.JPanel jpnlBot;
    private javax.swing.JPanel jpnlInfo;
    private javax.swing.JPanel jpnlMid;
    private javax.swing.JPanel jpnlMidRig;
    private javax.swing.JPanel jpnlTop;
    private javax.swing.JTextArea jtxtareEdiPan;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
    
}

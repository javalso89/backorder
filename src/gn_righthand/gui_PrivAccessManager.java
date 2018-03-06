package gn_righthand;

import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import sun.misc.BASE64Encoder;


public class gui_PrivAccessManager extends javax.swing.JFrame {
    
    private String sUser = "";
    private String sPass = "";
    private String accsURL = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB/Access_Cred.txt";
    private String[][] sAccsPriv = new String[20][2];
    //Preparing the main screen table model variables
    javax.swing.table.DefaultTableModel tblModelUserList = new javax.swing.table.DefaultTableModel();
    Object[] UserListColumn = new Object [2];
    
    
    public gui_PrivAccessManager(String sUser, String sPass) {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        
        this.sUser = sUser;
        this.sPass = sPass;
        
        //Loads the current list of Users
        loadAccsPrivDB();
        configUserListTable();
        loadUserListTable();
        
    }

    private gui_PrivAccessManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //Cleans the 2D-Array with the list of privileges by adding "N/A" to each position
    private void cleanPrivList(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        for ( int i=0; i<sAccsPriv.length; i++ ){
            for ( int j=0; j<sAccsPriv[0].length; j++ ){
                this.sAccsPriv[i][j] = "N/A";
            }
        }
    }
    //</editor-fold>
    
    //Loads the access privileges Data Base from the Beehive .txt file into 2D String Matrix 
    private boolean loadAccsPrivDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        boolean bAccsGranted = false;
        System.out.println("Getting the Access Privileges List");
        //Clears the current 2D-Array for access privileges Data Base
        cleanPrivList();
        //Prepares the necessary variables to read the .txt file from the given URL
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        //Prepares the necessary variables to fill the ArrayList
        String chain, sUser="", sPriv="";
        try
        {
            //Opens the URL connection
            System.out.println("Accessing Beehive given URL");
            URL url = new URL(accsURL);
            urlConn = (HttpURLConnection)url.openConnection();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Prepares and provides the necessary credentials
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = this.sUser + ":" + sPass;
            System.out.println("Obtaining authorization");
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            urlConn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            //Sets a timeout for the reading activity
            if (urlConn != null){urlConn.setReadTimeout(60 * 1000);}
            //If the .txt file is readable then it creates an input stream
            if (urlConn != null && urlConn.getInputStream() != null){
                System.out.println("Connection established.\nDownloading data.");
                int i = 0;
                isr = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
                br = new BufferedReader(isr);
                //Fills the 2D-Array with the information found on the remote .txt data base
                chain = br.readLine();
                while( !chain.equals("END") ){
                    String [] position = chain.split("\t");
                    sUser = position[0];
                    sPriv = position[1];
                    sAccsPriv[i][0] = sUser;
                    sAccsPriv[i][1] = sPriv;
                    System.out.println("Added: " + sAccsPriv[i][0] + " as " + sAccsPriv[i][1]);
                    i++;
                    chain = br.readLine();
                }
            }
            br.close();
            isr.close();
            bAccsGranted = true;
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Exception while accesing the remote Privileges Data Base\n" +
                    "The Data Base may not be available at the moment or the Username and/or Password are incorrect\n" +
                    "If the issue persists please contact the CR Spares Planning Team","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
        return bAccsGranted;
    }
    //</editor-fold>
    
    //Prepares the JTable columns in order to receive the list of Users from the remote .txt file
    private void configUserListTable(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        tblModelUserList.addColumn("User(e-mail)");
        tblModelUserList.addColumn("Access Level");
        jtblUserList.setModel(tblModelUserList);
        //Allows the user to sort the items in a UserListColumn
        jtblUserList.setAutoCreateRowSorter(true);        
        //Prepares the Table to aling values to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Preparing the header line
        JTableHeader header = jtblUserList.getTableHeader();
        header.setBackground(Color.BLUE);
        header.setForeground(Color.YELLOW);
        header.setReorderingAllowed(false); //will not allow the user to reorder the columns position
        //Configure rows and columns
        jtblUserList.setRowHeight(22);
        jtblUserList.getColumnModel().getColumn(0).setPreferredWidth(120);
        jtblUserList.getColumnModel().getColumn(0).setResizable(false);
        jtblUserList.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtblUserList.getColumnModel().getColumn(1).setPreferredWidth(100);
        jtblUserList.getColumnModel().getColumn(1).setResizable(false);
        jtblUserList.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        //Adding dropdown lists to columns
        TableColumn colBOS = jtblUserList.getColumnModel().getColumn(1);
        JComboBox droplistBOS = new JComboBox();
        droplistBOS.addItem("N/A");
        droplistBOS.addItem("Admin");
        droplistBOS.addItem("Backorder Planner");
        droplistBOS.addItem("Reader");
        colBOS.setCellEditor(new DefaultCellEditor(droplistBOS));
        
    }
    //</editor-fold>
    
    //Cleans the User List JTable
    private void cleanUserListTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        int a = this.tblModelUserList.getRowCount()-1;
        try
        {
            for ( int i=a; i >= 0; i--){tblModelUserList.removeRow(i);}
            
        }
        catch (Exception e){JOptionPane.showMessageDialog(this, "There was an error while cleaning the User List Table \n" + e, "ACCESS MANAGER MSG", JOptionPane.ERROR_MESSAGE);}
    }
    //</editor-fold>
    
    //Loads the information from the 2d-Matrix into de parts Users JTable
    private void loadUserListTable()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        System.out.println("Loading data from String Matrix into screen JTable");
        int r;
        for ( r=2; r<sAccsPriv.length; r++ )
        {
            UserListColumn[0] = sAccsPriv[r][0];
            UserListColumn[1] = sAccsPriv[r][1];
            tblModelUserList.addRow(UserListColumn);
            jtblUserList.setModel(tblModelUserList);
        }
        System.out.println("Matrix loaded in the screen's JTable");
    }
    //</editor-fold>
    
    //Updates the local User List Data Base 2-D Array according with the changes on the screen Jtable
    private void updateUserListArray() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        cleanPrivList();
        sAccsPriv[0][0] = "juan.carlos.suarez@oracle.com";
        sAccsPriv[0][1] = "Development";
        sAccsPriv[1][0] = "javier.f.alvarado@oracle.com";
        sAccsPriv[1][1] = "Development";
        int j = 0;
       // System.out.println("Lines: "+ jtblUserList.getRowCount());
        for ( int i=0; i<jtblUserList.getRowCount(); i++ ){
            j = i+2;
            sAccsPriv[j][0] = jtblUserList.getValueAt(i, 0).toString();
            sAccsPriv[j][1] = jtblUserList.getValueAt(i, 1).toString();
        }
        JOptionPane.showMessageDialog(this, "The Users Data Base has been updated");
    }
    //</editor-fold>
    
    //Saves the Remote User List Data Base from the active 2-D Array into the Beehive .txt Backup file
    private void updateRemoteUserListDB(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares the necessary variables to read the .txt file from the given URL
        URLConnection urlConn = null;
        OutputStreamWriter osw = null;
        try
        {
            System.out.println("Opening URL connection");
            //Opens the URL connection
            URL url = new URL(accsURL);
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
            System.out.println("Staring to load lines");
            
            //Checks if the URL connection is opened and if there is an output stream available
            if (urlConn != null && urlConn.getOutputStream() != null){
                System.out.println("The URL connection is up");
                System.out.println("The output stream buffer is available");
                osw = new OutputStreamWriter(urlConn.getOutputStream(),Charset.defaultCharset());
                for ( int i=0; i<sAccsPriv.length; i++ ){
                    osw.write(sAccsPriv[i][0] + "\t" + sAccsPriv[i][1] + "\n");
                }
                osw.write("END");
            }
            osw.flush();
            urlConn.getContentLengthLong();
            System.out.println("Closing output stream");
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

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlTop = new javax.swing.JPanel();
        jlblTop = new javax.swing.JLabel();
        jpnlMid = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblUserList = new javax.swing.JTable();
        jpnlBot = new javax.swing.JPanel();
        jbtnExit = new javax.swing.JButton();
        jbtnSave = new javax.swing.JButton();
        jbtnRem = new javax.swing.JButton();
        jbtnAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpnlTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlblTop.setFont(new java.awt.Font("Castellar", 1, 16)); // NOI18N
        jlblTop.setForeground(new java.awt.Color(0, 102, 255));
        jlblTop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblTop.setText("ACCESS MANAGER");

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
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jpnlMid.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jtblUserList.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jtblUserList);

        javax.swing.GroupLayout jpnlMidLayout = new javax.swing.GroupLayout(jpnlMid);
        jpnlMid.setLayout(jpnlMidLayout);
        jpnlMidLayout.setHorizontalGroup(
            jpnlMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlMidLayout.setVerticalGroup(
            jpnlMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnlBot.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbtnExit.setText("Exit");
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExitActionPerformed(evt);
            }
        });

        jbtnSave.setText("Save");
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        jbtnRem.setText("Remove");
        jbtnRem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRemActionPerformed(evt);
            }
        });

        jbtnAdd.setText("Add");
        jbtnAdd.setEnabled(false);

        javax.swing.GroupLayout jpnlBotLayout = new javax.swing.GroupLayout(jpnlBot);
        jpnlBot.setLayout(jpnlBotLayout);
        jpnlBotLayout.setHorizontalGroup(
            jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlBotLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtnRem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnlBotLayout.setVerticalGroup(
            jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlBotLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlBotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnExit)
                    .addComponent(jbtnSave)
                    .addComponent(jbtnRem)
                    .addComponent(jbtnAdd))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnlMid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnlTop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnlBot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlMid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpnlBot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExitActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to exit the Access Manager?");
        if ( opc == 0 ){
            dispose();
        }
    }//GEN-LAST:event_jbtnExitActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to update the Access List?");
        if ( opc == 0 ){
            updateUserListArray();
            updateRemoteUserListDB();
            cleanUserListTable();
            loadUserListTable();
        }
    }//GEN-LAST:event_jbtnSaveActionPerformed

    private void jbtnRemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRemActionPerformed
        //Captures the selected Lines
        int[] selLines = this.jtblUserList.getSelectedRows();
        for ( int i=0; i<selLines.length; i++ ){
            System.out.println("Line selected: " + selLines[i]);
        }
        if ( selLines.length > 0 ){
            int opc = JOptionPane.showConfirmDialog(this,"THE SELECTED LINE(s) WILL BE DELETED.\n Do you want to proceed.");
            if ( opc == 0 ){
                for ( int i=0; i<selLines.length; i++ ){
                    sAccsPriv[selLines[i]+1][0]="N/A";
                    sAccsPriv[selLines[i]+1][1]="N/A";
                }
                JOptionPane.showMessageDialog(this,"The selected line(s) were deleted from the Users List.\n"
                            + "The changes will be reflected until you save.");
            }
            else {
                JOptionPane.showMessageDialog(this,"No changes applied to the Users List.");
            }
            cleanUserListTable();
            loadUserListTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure of selecting one line at least");
        }
    }//GEN-LAST:event_jbtnRemActionPerformed

    
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
            java.util.logging.Logger.getLogger(gui_PrivAccessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui_PrivAccessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui_PrivAccessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui_PrivAccessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui_PrivAccessManager().setVisible(true);
            }
        });
    }

    
    //<editor-fold defaultstate="Collapsed" desc="Variables Declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnAdd;
    private javax.swing.JButton jbtnExit;
    private javax.swing.JButton jbtnRem;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JLabel jlblTop;
    private javax.swing.JPanel jpnlBot;
    private javax.swing.JPanel jpnlMid;
    private javax.swing.JPanel jpnlTop;
    private javax.swing.JTable jtblUserList;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}

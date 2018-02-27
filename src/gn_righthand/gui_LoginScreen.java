package gn_righthand;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import sun.misc.BASE64Encoder;


public class gui_LoginScreen extends javax.swing.JFrame{
    
    //Prepares the Application version
    String sVER = "3.21.13.b";
    
    String sConsultDBPath = "F:\\Oracle Projects\\DB Argentina Consults\\Consults_DB.txt"; //DEVELOPMENT PHASE PATH
    //String sConsultDBPath = "C:\\Program Files (x86)\\Oracle Spares Planning\\GN Righthand\\Data Files\\Consults_DB.txt"; //PRODUCTION PHASE PATH
    
    //URL for the access privileges
    private String accsURL = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB/Access_Cred.txt";
    private String[][] sAccsPriv = new String[20][2];


    
    //Prepares the main path for the data bases
    private String myURL = "https://stbeehive.oracle.com/content/dav/st/Juan%20K/Documents/GN_Righthand_DB";
    //Prepares the variables for the User's credentials
    private String sUser = "";
    private String sPass = "";
    private ArrayList<String> alFullName = new ArrayList<>();
    //Variable to check online or offline status
    boolean bONLINE = false; //True: ONLINE, False: OFFLINE
    
    //Checks for the last mail that accessed the App
    private String slastMail = "";
    //Prepares the variables for the progress bar
    private Timer tTime;
    int cont;
    public final static int TWO_SECOND=10;
    
    //CONSTRUCTORS SECTION
    
    public gui_LoginScreen()
    {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("SPARES PLANNING TEAM");
        this.jlblVersion.setText("Version " + sVER);
        jtxtUserMail.setText(getLastUserMail());
        psstxtPass.requestFocusInWindow();
        if ( !sUser.equals("") )
        {
            System.out.println("User name detected: " + sUser);
            jtxtUserMail.setText(sUser);
            //txtUser.set
            psstxtPass.requestFocusInWindow();
        }
        
        chkboxOffline.setEnabled(true);
        
        
        
        
    }
    
    //Checks if the User wants to work offline or online
    private void statusCheck()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        if ( chkboxOffline.isSelected() )
        {
            System.out.println("Working OFFLINE");
            psstxtPass.setEnabled(false);
            bONLINE = false;
        }
        else
        {
            System.out.println("Working ONLINE");
            jtxtUserMail.setEnabled(true);
            psstxtPass.setEnabled(true);
            bONLINE = true;
        }
    }
    //</editor-fold>
    
    //Validates the User field in order to make sure that it is an Oracle E-mail
    private boolean validateEmail()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        System.out.println("Validating e-mail format");
        boolean flag = false;
        String tmpDom = jtxtUserMail.getText();
        int j = 0;
        if ( jtxtUserMail.getText().contains("@") )
        {
            for ( int i=0; i<tmpDom.length(); i++ )
            {
                if ( tmpDom.charAt(i) == '@' ){j = i;}
            }
            if ( jtxtUserMail.getText().substring(j).toLowerCase().equals("@oracle.com"))
            {
                flag = true;
                System.out.println("E-mail format: Correct");
            }
            else
            {
                System.out.println("E-mail format: Incorrect");
            }
        }
        return flag;
    }
    //</editor-fold>
    
    //Separates the first, middle and last name from the provided e-mail address
    //Stores those Strings into an ArrayList in separate positions
    //Adds a final position to the same ArrayList for the whole name
    private void obtainName(String eMail){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Clear the ArrayList that will store the User's name
        alFullName.clear();
        String tmpName="", tmpFullName="";
        int pos=0, i=0;
        do{
            do{
                tmpName = tmpName + eMail.charAt(pos);
                pos++;
            }
            while ( eMail.charAt(pos) != '.' && eMail.charAt(pos) != '@'); //Stops at "." or at "@"
            //Changes the 1st char to Uppercase
            tmpName = Character.toUpperCase(tmpName.charAt(0)) + tmpName.substring(1);
            alFullName.add(tmpName);
            tmpFullName = tmpFullName + tmpName + " ";
            //Resets the temp name var
            tmpName = "";
            i++;
            pos++;
        }
        while (eMail.charAt(pos-1)!='@');
        alFullName.add(tmpFullName);
    }
    //</editor-fold>
    
    //From the local Consults Data Base file, obtains the e-mail of the last person who logged into the Tool
    private String getLastUserMail() {
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        String sLstMail = "";
        File fDataBase;
        FileReader fr;
        BufferedReader br;
        String chain;
        try
        {
            fDataBase = new File(sConsultDBPath);
            fr = new FileReader(fDataBase);
            br = new BufferedReader(fr);
            //Loading the list of Consults from the .txt file into the ArrayList
            chain = br.readLine();
            while( !chain.equals("CREATED MAILS") )
            {
                chain = br.readLine();
            }
            chain = br.readLine();
            sLstMail = br.readLine();
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(this,"The System has generated an error while reading from the Consults local Data Base \n" + e, "CONSULTING TOOL MSG", JOptionPane.ERROR_MESSAGE );
            sLstMail = "";
        }
        return sLstMail;
    }
    //</editor-fold>
    
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
        //Prepares the necessary variables to fill the Array
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
            getCredentials();
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
    
    //Identifies the corresponding access privilege for the current User
    private String getAccsPriv(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        String sPriv = "None";
        for ( int i=0; i<sAccsPriv.length; i++ ){
            if ( sAccsPriv[i][0].equals(sUser) ){
                sPriv = sAccsPriv[i][1];
            }
        } 
        System.out.println("Access Priv: " + sPriv);
        return sPriv;
    }
    //</editor-fold>
    
    //Obtains the User's credentials from the Login screen fields
    private void getCredentials()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        sUser = jtxtUserMail.getText();
        System.out.println("Obtaining User credentials for: " + sUser);
        sPass = "";
        char[] pass = psstxtPass.getPassword();
        if ( pass.length == 0  )
        {
            System.out.println("Password has no characters");
        }
        else
        {
            for (int i=0; i<pass.length; i++){sPass = sPass + pass[i];}
            System.out.println("Password detected with " + pass.length + " chars");
        }
    }
    //</editor-fold>
    
    //Creates a login screen that gathers the user's mail and pass
    private void Login(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        if ( validateEmail() == false ){
            JOptionPane.showMessageDialog(this, "Your Username is not a valid Oracle e-Mail. Please try again.","LOGIN ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else{
            //If the mail has a valid Oracle format, then it saves it and also gets the whole User name
            sUser = jtxtUserMail.getText();
            obtainName(sUser);
            //Checks if the User is working online or not
            statusCheck();
            if ( bONLINE == false ){
                System.out.println("WORKING OFFLINE");
                JOptionPane.showMessageDialog(this, "Please be aware that you are offline.\n"
                        + "You will not have access to the remote Data Bases.\n"
                        + "Some functions will not be avilable","WARNING",JOptionPane.OK_OPTION);
                //Working offline means that the User's password will be ignored
                sPass = "n/a";
                callSelectionScreen();
            }
            else{
                System.out.println("WORKING ONLINE");
                System.out.println("Downloading list of Users from Oracle Workspace");
                
                
                callSelectionScreen();
            }
        }
    }
    //</editor-fold>
    
    //Launches the Selection Screen indicating Offline or Online working mode
    //Validates the list of privileges under Online Mode
    private void callSelectionScreen(){
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        System.out.println("Preparing to call the Selection screen");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if ( bONLINE == false ){//Working Offline
            System.out.println("Launching Selection Screen. Offline");
            String sPriv = "Offline";
            gui_SelectionScreen SStmpGUI = new gui_SelectionScreen(bONLINE, sUser, sPass, sVER, alFullName.get(alFullName.size()-1), sPriv);
            SStmpGUI.jlblSta.setForeground(Color.WHITE);
            SStmpGUI.jlblSta.setBackground(new Color(255,50,0));
            SStmpGUI.jlblSta.setText("OFFLINE");
            SStmpGUI.setLocationRelativeTo(this);
            SStmpGUI.setVisible(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            dispose(); 
        }
        else{//Working Online
            System.out.println("Launching Selection Screen. Online.");
            boolean bAccsGranted = loadAccsPrivDB();
            String sPriv = getAccsPriv();
            //Checks if the remote access was performed correctly
            if ( bAccsGranted == true ){
                switch ( sPriv ){
                    case "Backorder Planner":{
                        gui_BO_MainScreen guiBOMS = new gui_BO_MainScreen(bONLINE, sUser, sPass);
                        guiBOMS.jlblSta.setForeground(Color.BLACK);
                        guiBOMS.jlblSta.setBackground(new Color(125,245,10));
                        guiBOMS.jlblSta.setText("ONLINE");
                        guiBOMS.jlblUser.setText("<html><font color='blue'>User: </font>" + alFullName.get(alFullName.size()-1) 
                                        + " / <font color='blue'>Access Level: </font>" + sPriv + "</html>");
                        guiBOMS.setLocationRelativeTo(this);
                        guiBOMS.setVisible(true);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        break;
                    }
                    case "Development":{
                        String sOpt="";
                        boolean bCond=false;
                        do{
                            sOpt = JOptionPane.showInputDialog(this, "Welcome " + alFullName.get(alFullName.size()-1).substring(0,this.alFullName.get(alFullName.size()-1).indexOf(' ')) + "\n"
                                + "1. Argentina Planning Tool\n"
                                + "2. Backorders Module");
                            if ( sOpt.equals("1") || sOpt.equals("2") ){bCond = true;}
                        }while(bCond == false);
                        switch (sOpt){
                            case "1" :{
                                gui_SelectionScreen SStmpGUI = new gui_SelectionScreen(bONLINE, sUser, sPass, sVER, alFullName.get(alFullName.size()-1), sPriv);
                                SStmpGUI.jlblSta.setForeground(Color.BLACK);
                                SStmpGUI.jlblSta.setBackground(new Color(125,245,10));
                                SStmpGUI.jlblSta.setText("ONLINE");
                                SStmpGUI.setLocationRelativeTo(this);
                                SStmpGUI.setVisible(true);
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                break;
                            }
                            case "2" :{
                                gui_BO_MainScreen guiBOMS = new gui_BO_MainScreen(bONLINE, sUser, sPass);
                                guiBOMS.jlblSta.setForeground(Color.BLACK);
                                guiBOMS.jlblSta.setBackground(new Color(125,245,10));
                                guiBOMS.jlblSta.setText("ONLINE");
                                guiBOMS.jlblUser.setText("<html><font color='blue'>User: </font>" + alFullName.get(alFullName.size()-1) 
                                        + " / <font color='blue'>Access Level: </font>" + sPriv + "</html>");
                                guiBOMS.setLocationRelativeTo(this);
                                guiBOMS.setVisible(true);
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                break;
                            }
                        }
                        break;
                    }
                    default:{
                        gui_SelectionScreen SStmpGUI = new gui_SelectionScreen(bONLINE, sUser, sPass, sVER, alFullName.get(alFullName.size()-1), sPriv);
                        SStmpGUI.jlblSta.setForeground(Color.BLACK);
                        SStmpGUI.jlblSta.setBackground(new Color(125,245,10));
                        SStmpGUI.jlblSta.setText("ONLINE");
                        SStmpGUI.setLocationRelativeTo(this);
                        SStmpGUI.setVisible(true);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        break;
                    }
                }
                dispose();
            }
        }
    }
    //</editor-fold>
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        pnlLogin = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        lblPass = new javax.swing.JLabel();
        jtxtUserMail = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        psstxtPass = new javax.swing.JPasswordField();
        btnExit = new javax.swing.JButton();
        chkboxOffline = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        ProgBar = new javax.swing.JProgressBar();
        jlblVersion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/RightHand.fw2.fw.Small.fw.png"))); // NOI18N

        pnlLogin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        lblUser.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("Username: ");

        lblPass.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPass.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPass.setText("Password: ");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLoginKeyPressed(evt);
            }
        });

        psstxtPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                psstxtPassKeyPressed(evt);
            }
        });

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        chkboxOffline.setText("  Work offline");
        chkboxOffline.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkboxOfflineStateChanged(evt);
            }
        });
        chkboxOffline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkboxOfflineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);
        pnlLoginLayout.setHorizontalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtUserMail))
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addComponent(lblPass, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(psstxtPass))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                        .addComponent(chkboxOffline)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlLoginLayout.setVerticalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtUserMail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUser))
                .addGap(26, 26, 26)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(psstxtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnExit)
                    .addComponent(chkboxOffline))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GOOD-NEW CONSULTING TOOL");

        jlblVersion.setText("Version");

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ProgBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jlblVersion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                        .addComponent(jlblVersion)
                        .addGap(36, 36, 36)))
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ProgBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        int opc = JOptionPane.showConfirmDialog(this,"Do you want to exit the Program?");
        if ( opc == 0 ){System.exit(0);}
    }//GEN-LAST:event_btnExitActionPerformed

    private void chkboxOfflineStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkboxOfflineStateChanged
        statusCheck();        
    }//GEN-LAST:event_chkboxOfflineStateChanged

    private void btnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginKeyPressed
        
    }//GEN-LAST:event_btnLoginKeyPressed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        Login();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void psstxtPassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_psstxtPassKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {
            Login();
        }
    }//GEN-LAST:event_psstxtPassKeyPressed

    private void chkboxOfflineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkboxOfflineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkboxOfflineActionPerformed

    /**
     * @param args the command line arguments
     */
    
    class TimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            cont++;
            ProgBar.setValue(cont);
            if ( cont == 100 ){
                tTime.stop();
                //hide();

                
            }
        }
    }
    
//    public void hide(){this.setVisible(false);}
    public void activate(){tTime.start();}
            
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
            java.util.logging.Logger.getLogger(gui_LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui_LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui_LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui_LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui_LoginScreen().setVisible(true);
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar ProgBar;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JCheckBox chkboxOffline;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jlblVersion;
    public javax.swing.JTextField jtxtUserMail;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPasswordField psstxtPass;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}

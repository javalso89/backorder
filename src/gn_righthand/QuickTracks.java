package gn_righthand;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class QuickTracks extends javax.swing.JFrame
{
    private String sAirwaybill;
    
    public QuickTracks(String sAirwaybill)
    {
        this.sAirwaybill = sAirwaybill;
        //this.txtTracNumb.setText(sAirwaybill);
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("QUICK TRACKS FOR RIGHTHAND");
        ((JPanel)getContentPane()).setOpaque(false);
        ImageIcon uno = new ImageIcon(this.getClass().getResource("/Images/parcel 1.png"));
        JLabel fondo = new JLabel();
        fondo.setIcon(uno);
        getLayeredPane().add(fondo,JLayeredPane.FRAME_CONTENT_LAYER);
        fondo.setBounds(70, 60, uno.getIconWidth(), uno.getIconHeight());
        btnDHL.setSelected(true);
    }

    private QuickTracks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //Cleans all the blanks spaces from a provided String
    public String cleanBlanks(String txtField)
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">>
    {
        String tmp = txtField.replaceAll("\\s+", "");
        return tmp;
    }
    //</editor-fold>
    
    //Opens an Internet URL
    public void openLink(String sLink)
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        try
        {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(sLink));

        }
        catch(Exception e){JOptionPane.showMessageDialog(this, e.getMessage());}
    }
    //</editor-fold>
    
    private void gotoURL()
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
    {
        if ( btnDHL.isSelected() )
            {
                String sQLink = "http://www.dhl.com/en/express/tracking.html?AWB="
                        + cleanBlanks(txtTracNumb.getText()) + "&brand=DHL";
                openLink(sQLink);
            }
            if ( btnUPS.isSelected() )
            {
                String sQLink = "https://wwwapps.ups.com/WebTracking/track?track=yes&trackNums="
                        + cleanBlanks(txtTracNumb.getText()) + "&loc=es_us";
                openLink(sQLink);
            }
            if ( btnFEDEX.isSelected() )
            {
                String sQLink = "https://www.fedex.com/apps/fedextrack/?action=track&trackingnumber="
                        + cleanBlanks(txtTracNumb.getText()) + "&cntry_code=us";
                openLink(sQLink);
            }
            if ( btnUSPS.isSelected() )
            {
                String sQLink = "https://tools.usps.com/go/TrackConfirmAction_input?qtc_tLabels1="
                        + cleanBlanks(txtTracNumb.getText());
                openLink(sQLink);
            }
            if ( this.btnAppleExpress.isSelected() )
            {
                String sQLink = "http://www.appleexpress.com/";
                openLink(sQLink);
            }
            if ( btnDHLGF.isSelected() )
            {
                String sQLink = "https://dhli.dhl.com/dhli-client/publicTracking?searchType=HBN&searchValue="
                        + cleanBlanks(txtTracNumb.getText()) + "&commit=Track!";
                openLink(sQLink);
            }
            if ( btnLOOMIS.isSelected() )
            {
                String sQLink = "http://www.loomisexpress.com/ca/wfTrackingStatus.aspx?PieceNumber="
                        + cleanBlanks(txtTracNumb.getText());
                openLink(sQLink);
            }
            if ( btnEXPEDITORS.isSelected() )
            {
                String sQLink = "http://expo.expeditors.com/expo/SQGuest?tcode=E11855354&ucode=ORACLE&SearchType=consignmentSearch&reference="
                        + cleanBlanks(txtTracNumb.getText());
                openLink(sQLink);
            }
            if ( btnPOWERNET.isSelected() )
            {
                String sQLink = "http://www.nnrpowernet.com/";
                openLink(sQLink);
            }
    }
    //</editor-fold>
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btnEXPEDITORS1 = new javax.swing.JRadioButton();
        pnlMainScreen = new javax.swing.JPanel();
        btnQreq = new javax.swing.JButton();
        txtTracNumb = new javax.swing.JTextField();
        labTracNumb = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnDHL = new javax.swing.JRadioButton();
        jlogoDHL = new javax.swing.JLabel();
        btnUPS = new javax.swing.JRadioButton();
        jlogoUPS = new javax.swing.JLabel();
        btnFEDEX = new javax.swing.JRadioButton();
        jlogoFEDEX = new javax.swing.JLabel();
        btnUSPS = new javax.swing.JRadioButton();
        jlogoUSPS = new javax.swing.JLabel();
        lblCreationDate = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        btnDHLGF = new javax.swing.JRadioButton();
        jlogoDHLGF = new javax.swing.JLabel();
        btnLOOMIS = new javax.swing.JRadioButton();
        jlogoLOOMIS = new javax.swing.JLabel();
        btnEXPEDITORS = new javax.swing.JRadioButton();
        jlogoEXPEDITORS = new javax.swing.JLabel();
        btnPOWERNET = new javax.swing.JRadioButton();
        jlogoPOWERNET = new javax.swing.JLabel();
        btnAppleExpress = new javax.swing.JRadioButton();
        jlogoAppleExpress = new javax.swing.JLabel();
        menMenuBar = new javax.swing.JMenuBar();

        buttonGroup1.add(btnEXPEDITORS1);
        btnEXPEDITORS1.setOpaque(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlMainScreen.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quick Tracks", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        pnlMainScreen.setOpaque(false);
        pnlMainScreen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnQreq.setText("Check");
        btnQreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQreqActionPerformed(evt);
            }
        });
        pnlMainScreen.add(btnQreq, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 91, -1));

        txtTracNumb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTracNumbActionPerformed(evt);
            }
        });
        pnlMainScreen.add(txtTracNumb, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 180, -1));

        labTracNumb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labTracNumb.setText("Tracking # ");
        pnlMainScreen.add(labTracNumb, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        pnlMainScreen.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 300, 100, -1));

        buttonGroup1.add(btnDHL);
        btnDHL.setToolTipText("");
        pnlMainScreen.add(btnDHL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jlogoDHL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil Dhl_logo.png"))); // NOI18N
        jlogoDHL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoDHLMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoDHL, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 140, -1));

        buttonGroup1.add(btnUPS);
        pnlMainScreen.add(btnUPS, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jlogoUPS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil UPS_logo_2003.png"))); // NOI18N
        jlogoUPS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoUPSMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoUPS, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 130, -1));

        buttonGroup1.add(btnFEDEX);
        pnlMainScreen.add(btnFEDEX, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jlogoFEDEX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil FedEx-Logo-PNG-Transparent.png"))); // NOI18N
        jlogoFEDEX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoFEDEXMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoFEDEX, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 140, -1));

        buttonGroup1.add(btnUSPS);
        pnlMainScreen.add(btnUSPS, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jlogoUSPS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil United_States_Postal_Service_Logo.png"))); // NOI18N
        jlogoUSPS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoUSPSMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoUSPS, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 160, -1));

        lblCreationDate.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblCreationDate.setText("CR Planning Team. 11.2016.");
        pnlMainScreen.add(lblCreationDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(697, 518, -1, -1));

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        pnlMainScreen.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 96, -1));

        buttonGroup1.add(btnDHLGF);
        btnDHLGF.setOpaque(false);
        pnlMainScreen.add(btnDHLGF, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, -1, -1));

        jlogoDHLGF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil dhl_logo_gf02.png"))); // NOI18N
        jlogoDHLGF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoDHLGFMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoDHLGF, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 130, -1));

        buttonGroup1.add(btnLOOMIS);
        btnLOOMIS.setOpaque(false);
        pnlMainScreen.add(btnLOOMIS, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, -1, -1));

        jlogoLOOMIS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil LoomisExpress.png"))); // NOI18N
        jlogoLOOMIS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoLOOMISMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoLOOMIS, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 130, -1));

        buttonGroup1.add(btnEXPEDITORS);
        btnEXPEDITORS.setOpaque(false);
        pnlMainScreen.add(btnEXPEDITORS, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 170, -1, -1));

        jlogoEXPEDITORS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil Expeditors LOGO.jpg"))); // NOI18N
        jlogoEXPEDITORS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoEXPEDITORSMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoEXPEDITORS, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, 90, -1));

        buttonGroup1.add(btnPOWERNET);
        btnPOWERNET.setOpaque(false);
        pnlMainScreen.add(btnPOWERNET, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 210, -1, -1));

        jlogoPOWERNET.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil NNRPowernet.jpg"))); // NOI18N
        jlogoPOWERNET.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoPOWERNETMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoPOWERNET, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 110, -1));

        buttonGroup1.add(btnAppleExpress);
        btnAppleExpress.setText("        ");
        btnAppleExpress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAppleExpressActionPerformed(evt);
            }
        });
        pnlMainScreen.add(btnAppleExpress, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 21, -1));

        jlogoAppleExpress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QuickTracks/Lil AppleExpress.JPG"))); // NOI18N
        jlogoAppleExpress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlogoAppleExpressMouseClicked(evt);
            }
        });
        pnlMainScreen.add(jlogoAppleExpress, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 169, 29));

        menMenuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setJMenuBar(menMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMainScreen, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMainScreen, javax.swing.GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQreqActionPerformed
        if ( btnPOWERNET.isSelected() || btnAppleExpress.isSelected() )
        {
            gotoURL();
        }
        else
        {
            if ( txtTracNumb.getText().equals("") )
            {
                JOptionPane.showMessageDialog(this, "Please provide a Tracking Number","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                gotoURL();
            }
        }
    }//GEN-LAST:event_btnQreqActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtTracNumb.setText("");
        btnDHL.setSelected(true);
    }//GEN-LAST:event_btnClearActionPerformed

    private void jlogoDHLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoDHLMouseClicked
        this.btnDHL.setSelected(true);
    }//GEN-LAST:event_jlogoDHLMouseClicked

    private void jlogoUPSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoUPSMouseClicked
        this.btnUPS.setSelected(true);
    }//GEN-LAST:event_jlogoUPSMouseClicked

    private void jlogoFEDEXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoFEDEXMouseClicked
        this.btnFEDEX.setSelected(true);
    }//GEN-LAST:event_jlogoFEDEXMouseClicked

    private void jlogoUSPSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoUSPSMouseClicked
        this.btnUSPS.setSelected(true);
    }//GEN-LAST:event_jlogoUSPSMouseClicked

    private void jlogoDHLGFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoDHLGFMouseClicked
        this.btnDHLGF.setSelected(true);
    }//GEN-LAST:event_jlogoDHLGFMouseClicked

    private void jlogoLOOMISMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoLOOMISMouseClicked
        this.btnLOOMIS.setSelected(true);
    }//GEN-LAST:event_jlogoLOOMISMouseClicked

    private void jlogoEXPEDITORSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoEXPEDITORSMouseClicked
        this.btnEXPEDITORS.setSelected(true);
    }//GEN-LAST:event_jlogoEXPEDITORSMouseClicked

    private void jlogoPOWERNETMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoPOWERNETMouseClicked
        this.btnPOWERNET.setSelected(true);
    }//GEN-LAST:event_jlogoPOWERNETMouseClicked

    private void btnAppleExpressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppleExpressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAppleExpressActionPerformed

    private void jlogoAppleExpressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlogoAppleExpressMouseClicked
        this.btnAppleExpress.setSelected(true);
    }//GEN-LAST:event_jlogoAppleExpressMouseClicked

    private void txtTracNumbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTracNumbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTracNumbActionPerformed

    
    public static void main(String args[])
    {
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
            java.util.logging.Logger.getLogger(QuickTracks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuickTracks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuickTracks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuickTracks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuickTracks().setVisible(true);
            }
        });
    }

    //<editor-fold defaultstate="collapsed" desc="Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton btnAppleExpress;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose;
    private javax.swing.JRadioButton btnDHL;
    private javax.swing.JRadioButton btnDHLGF;
    private javax.swing.JRadioButton btnEXPEDITORS;
    private javax.swing.JRadioButton btnEXPEDITORS1;
    private javax.swing.JRadioButton btnFEDEX;
    private javax.swing.JRadioButton btnLOOMIS;
    private javax.swing.JRadioButton btnPOWERNET;
    private javax.swing.JButton btnQreq;
    private javax.swing.JRadioButton btnUPS;
    private javax.swing.JRadioButton btnUSPS;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jlogoAppleExpress;
    private javax.swing.JLabel jlogoDHL;
    private javax.swing.JLabel jlogoDHLGF;
    private javax.swing.JLabel jlogoEXPEDITORS;
    private javax.swing.JLabel jlogoFEDEX;
    private javax.swing.JLabel jlogoLOOMIS;
    private javax.swing.JLabel jlogoPOWERNET;
    private javax.swing.JLabel jlogoUPS;
    private javax.swing.JLabel jlogoUSPS;
    private javax.swing.JLabel labTracNumb;
    private javax.swing.JLabel lblCreationDate;
    private javax.swing.JMenuBar menMenuBar;
    private javax.swing.JPanel pnlMainScreen;
    public javax.swing.JTextField txtTracNumb;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}

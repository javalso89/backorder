
package gn_righthand;

//Library import section

public class cls_WebADI_Data {
    
    //VARIABLES DECLARATION
    private String sDat;
    private String sItm;
    private String sQTY;
    private String sFrm;
    private String sDst;
    private String sShpMet;
    private String sRef;
    private String sISO;
    private String sAwb;
    private String sSta;
    private String sAct;
    private String sTsk;
    private String sSMI;
    private String sCIB;
    private String sCom;
    private String sPos;
    private String sXX1; //Addtional var 1
    private String sXX2; //Addtional var 2
    
    //CONSTRUCTORS
    public cls_WebADI_Data() {
    }
    
    public cls_WebADI_Data(String sDat, String sItm, String sQty, 
            String sFrm, String sDst, String sShpMet,
            String sRef, String sISO, String sAwb,
            String sSta, String sAct, String sTsk,
            String sSMI, String sCIB, String sCom,
            String sPos, String sXX1, String sXX2) {
        this.sDat = sDat;
        this.sItm = sItm;
        this.sQTY = sQty;
        this.sFrm = sFrm;
        this.sDst = sDst;
        this.sShpMet = sShpMet;
        this.sRef = sRef;
        this.sISO = sISO;
        this.sAwb = sAwb;
        this.sSta = sSta;
        this.sAct = sAct;
        this.sTsk = sTsk;
        this.sSMI = sSMI;
        this.sCIB = sCIB;
        this.sCom = sCom;
        this.sPos = sPos;
        this.sXX1 = sXX1;
        this.sXX2 = sXX2;
    }
    
    //SETS $ GETS
    public void setDat(String sDat) {this.sDat = sDat;}
    public void setItm(String sItm) {this.sItm = sItm;}
    public void setQTY(String sQTY) {this.sQTY = sQTY;}
    public void setFrm(String sFrm) {this.sFrm = sFrm;}
    public void setDst(String sDst) {this.sDst = sDst;}
    public void setShpMet(String sShpMet) {this.sShpMet = sShpMet;}
    public void setRef(String sRef) {this.sRef = sRef;}
    public void setISO(String sISO) {this.sISO = sISO;}
    public void setAwb(String sAwb) {this.sAwb = sAwb;}
    public void setSta(String sSta) {this.sSta = sSta;}
    public void setAct(String sAct) {this.sAct = sAct;}
    public void setTsk(String sTsk) {this.sTsk = sTsk;}
    public void setSMI(String sSMI) {this.sSMI = sSMI;}
    public void setCIB(String sCIB) {this.sCIB = sCIB;}
    public void setCom(String sCom) {this.sCom = sCom;}
    public void setPos(String sPos) {this.sPos = sPos;}
    public void setXX1(String sXX1) {this.sXX1 = sXX1;}
    public void setXX2(String sXX2) {this.sXX2 = sXX2;}
       
    
    public String getDat() {return sDat;}
    public String getItm() {return sItm;}
    public String getQTY() {return sQTY;}
    public String getFrm() {return sFrm;}
    public String getDst() {return sDst;}
    public String getShpMet() {return sShpMet;}
    public String getRef() {return sRef;}
    public String getISO() {return sISO;}
    public String getAwb() {return sAwb;}
    public String getSta() {return sSta;}
    public String getAct() {return sAct;}
    public String getTsk() {return sTsk;}
    public String getSMI() {return sSMI;}
    public String getCIB() {return sCIB;}
    public String getCom() {return sCom;}
    public String getPos() {return sPos;}
    public String getXX1() {return sXX1;}
    public String getXX2() {return sXX2;}
    
    //MAIN METHODS
    
    
}

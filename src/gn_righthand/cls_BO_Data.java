
package gn_righthand;

//Library import section

public class cls_BO_Data {
        
    //VARIABLES DECLARATION
    private String sBSta; //BO status
    private String sDate; //BO request date
    private String sSvRq; //Service Request number
    private String sTask; //Task number
    private String sISO; //Order number
    private String sItem; //Part number
    private String sQty; //Quantity
    private String sDesc; //Description
    private String sTkSt; //Task status
    private String sPLC; //Part PLC
    private String sCrit; //Part criticallity
    private String sCond; //Part condition
    private String sSrAs; //GN Search assumption
    private String sAlts; //Alternatives
    private String sComm; //Comments
    private String sISO1; //ISO 1
    private String sAwb1; //Airwaybill 1
    private String sISO2; //ISO 2
    private String sAwb2; //Airwaybill 2
    private String sISO3; //ISO 3
    private String sAwb3; //Airwaybill 3
    private String sIsMB; //ISO MI2 to BUE
    private String sAwMB; //Airwaybill MI2 to BUE
    private String sSIMI; //SIMI number
    private String sTkNt; //GSI Task notes
    private String sBOMT; //BO mail title
    private String sTrak; //Mail tracking
    private String sPosi; //Entry ArrayList position
    //Additional Planning variables
    private String sPlan; //Planner Name
    private String sRvDt; //Last review date
    private String sReta; //Revised ETA
    private String sPath; //Path
    private String sIeta; //Improved ETA
    private String sRoot; //Root cause
    //Additional variables
    private String sZone;
    private String sCtry;
    private String sXXX1;
    
    //CONSTRUCTORS
    
    public cls_BO_Data() {
    }
    
    public cls_BO_Data(String sBSta, String sDate, String sSvRq, String sTask, 
            String sISO, String sItem, String sQty, String sDesc, String sTkSt, 
            String sPLC, String sCrit, String sCond, String sSrAs, String sAlts, 
            String sComm, String sISO1, String sAwb1, String sISO2, String sAwb2, 
            String sISO3, String sAwb3, String sIsMB, String sAwMB, String sSIMI, 
            String sTkNt, String sBOMT, String sTrak, String sPosi, String sPlan, 
            String sRvDt, String sReta, String sPath, String sIeta, String sRoot,
            String sZone, String sCtry, String sXXX1){
        this.sBSta = sBSta;
        this.sDate = sDate;
        this.sSvRq = sSvRq;
        this.sTask = sTask;
        this.sISO = sISO;
        this.sItem = sItem;
        this.sQty = sQty;
        this.sDesc = sDesc;
        this.sTkSt = sTkSt;
        this.sPLC = sPLC;
        this.sCrit = sCrit;
        this.sCond = sCond;
        this.sSrAs = sSrAs;
        this.sAlts = sAlts;
        this.sComm = sComm;
        this.sISO1 = sISO1;
        this.sAwb1 = sAwb1;
        this.sISO2 = sISO2;
        this.sAwb2 = sAwb2;
        this.sISO3 = sISO3;
        this.sAwb3 = sAwb3;
        this.sIsMB = sIsMB;
        this.sAwMB = sAwMB;
        this.sSIMI = sSIMI;
        this.sTkNt = sTkNt;
        this.sBOMT = sBOMT;
        this.sTrak = sTrak;
        this.sPosi = sPosi;
        this.sPlan = sPlan;
        this.sRvDt = sRvDt;
        this.sReta = sReta;
        this.sPath = sPath;
        this.sIeta = sIeta;
        this.sRoot = sRoot;
        this.sZone = sZone;
        this.sCtry = sCtry;
        this.sXXX1 = sXXX1;
    }
    
    //SETS $ GETS
            
    public void setBSta(String sBSta) {this.sBSta = sBSta;}
    public void setDate(String sDate) {this.sDate = sDate;}
    public void setSvRq(String sSvRq) {this.sSvRq = sSvRq;}
    public void setTask(String sTask) {this.sTask = sTask;}
    public void setISO(String sISO) {this.sISO = sISO;}
    public void setItem(String sItem) {this.sItem = sItem;}
    public void setQty(String sQty) {this.sQty = sQty;}
    public void setDesc(String sDesc) {this.sDesc = sDesc;}
    public void setTkSt(String sTkSt) {this.sTkSt = sTkSt;}
    public void setPLC(String sPLC) {this.sPLC = sPLC;}
    public void setCrit(String sCrit) {this.sCrit = sCrit;}
    public void setCond(String sCond) {this.sCond = sCond;}
    public void setSrAs(String sSrAs) {this.sSrAs = sSrAs;}
    public void setAlts(String sAlts) {this.sAlts = sAlts;}
    public void setComm(String sComm) {this.sComm = sComm;}
    public void setISO1(String sISO1) {this.sISO1 = sISO1;}
    public void setAwb1(String sAwb1) {this.sAwb1 = sAwb1;}
    public void setISO2(String sISO2) {this.sISO2 = sISO2;}
    public void setAwb2(String sAwb2) {this.sAwb2 = sAwb2;}
    public void setISO3(String sISO3) {this.sISO3 = sISO3;}
    public void setAwb3(String sAwb3) {this.sAwb3 = sAwb3;}
    public void setIsMB(String sIsMB) {this.sIsMB = sIsMB;}
    public void setAwMB(String sAwMB) {this.sAwMB = sAwMB;}
    public void setSIMI(String sSIMI) {this.sSIMI = sSIMI;}
    public void setTkNt(String sTkNt) {this.sTkNt = sTkNt;}
    public void setBOMT(String sBOMT) {this.sBOMT = sBOMT;}
    public void setTrak(String sTrak) {this.sTrak = sTrak;}
    public void setPosi(String sPosi) {this.sPosi = sPosi;}
    public void setPlan(String sPlan) {this.sPlan = sPlan;}
    public void setRvDt(String sRvDt) {this.sRvDt = sRvDt;}
    public void setReta(String sReta) {this.sReta = sReta;}
    public void setPath(String sPath) {this.sPath = sPath;}
    public void setIeta(String sIeta) {this.sIeta = sIeta;}
    public void setRoot(String sRoot) {this.sRoot = sRoot;}
    public void setZone(String sZone) {this.sZone = sZone;}
    public void setCtry(String sCtry) {this.sCtry = sCtry;}
    public void setXXX1(String sXXX1) {this.sXXX1 = sXXX1;}
    
    
    public String getBSta() {return sBSta;}
    public String getDate() {return sDate;}
    public String getSvRq() {return sSvRq;}
    public String getTask() {return sTask;}
    public String getISO() {return sISO;}
    public String getItem() {return sItem;}
    public String getQty() {return sQty;}
    public String getDesc() {return sDesc;}
    public String getTkSt() {return sTkSt;}
    public String getPLC() {return sPLC;}
    public String getCrit() {return sCrit;}
    public String getCond() {return sCond;}
    public String getSrAs() {return sSrAs;}
    public String getAlts() {return sAlts;}
    public String getComm() {return sComm;}
    public String getISO1() {return sISO1;}
    public String getAwb1() {return sAwb1;}
    public String getISO2() {return sISO2;}
    public String getAwb2() {return sAwb2;}
    public String getISO3() {return sISO3;}
    public String getAwb3() {return sAwb3;}
    public String getIsMB() {return sIsMB;}
    public String getAwMB() {return sAwMB;}
    public String getSIMI() {return sSIMI;}
    public String getTkNt() {return sTkNt;}
    public String getBOMT() {return sBOMT;}
    public String getTrak() {return sTrak;}
    public String getPosi() {return sPosi;}
    public String getPlan() {return sPlan;}
    public String getRvDt() {return sRvDt;}
    public String getReta() {return sReta;}
    public String getPath() {return sPath;}
    public String getIeta() {return sIeta;}
    public String getRoot() {return sRoot;}
    public String getZone() {return sZone;}
    public String getXXX1() {return sCtry;}
    public String getXXX3() {return sXXX1;}
    
    //MAIN METHODS

    
    
    
}


package gn_righthand;

//Library import section

public class cls_GNSearchLine {

    //VARIABLES DECLARATION
    private String sTier;
    private String sRegion;
    private String sCountryName;
    private String sOrgCode;
    private String sPartNumber;
    private String sQTY; //This is the QTY checked with the Org from XS or OH
    private String sActivity;
    private String sTotalOH;
    private String sTotalXS;
    private String sTotalNd;
    private String sCurrentDate;
    private String sDOM;
    private String sPartMoved;
    private String sTask;
    private String sTracking;
    private String sConcCode;
    private String sPosition;
    
    //CONSTRUCTORS
    public cls_GNSearchLine() {
    }
    
    public cls_GNSearchLine(String sTier, String sRegion, String sCountryName, String sOrgCode,  
            String sPartNumber, String sQTY, String sActivity, String sTotalOH, String sTotalXS, String sTotalNd,
            String sCurrentDate, String sDOM, String sPartMoved, String sTask, String sTracking, String sConcCode, String sPosition) {
        this.sTier = sTier;
        this.sRegion = sRegion;
        this.sCountryName = sCountryName;
        this.sOrgCode = sOrgCode;
        this.sPartNumber = sPartNumber;
        this.sQTY = sQTY;
        this.sActivity = sActivity;
        this.sTotalOH = sTotalOH;
        this.sTotalXS = sTotalXS;
        this.sTotalNd = sTotalNd;
        this.sCurrentDate = sCurrentDate;
        this.sDOM = sDOM;
        this.sPartMoved = sPartMoved;
        this.sTask = sTask;
        this.sTracking = sTracking;
        this.sConcCode = sConcCode;
        this.sPosition = sPosition;
   }
    
    //SETS $ GETS
    public void setTier(String sTier) {this.sTier = sTier;}
    public void setRegion(String sRegion) {this.sRegion = sRegion;}
    public void setCountryName(String sCountryName) {this.sCountryName = sCountryName;}
    public void setOrgCode(String sOrgCode) {this.sOrgCode = sOrgCode;}
    public void setPartNumber(String sPartNumber) {this.sPartNumber = sPartNumber;}
    public void setQTY(String sQTY) {this.sQTY = sQTY;}
    public void setActivity(String sActivity) {this.sActivity = sActivity;}
    public void setTotalOH(String sTotalOH) {this.sTotalOH = sTotalOH;}
    public void setTotalXS(String sTotalXS) {this.sTotalXS = sTotalXS;}
    public void setTotalNd(String sTotalNd) {this.sTotalNd = sTotalNd;}
    public void setCurrentDate(String sCurrentDate) {this.sCurrentDate = sCurrentDate;}
    public void setDOM(String sDOM) {this.sDOM = sDOM;}
    public void setPartMoved(String sPartMoved) {this.sPartMoved = sPartMoved;}
    public void setTask(String sTask) {this.sTask = sTask;}
    public void setTracking(String sTracking) {this.sTracking = sTracking;}
    public void setConcCode(String sConcCode) {this.sConcCode = sConcCode;}
    public void setPosition(String sPosition) {this.sPosition = sPosition;}
        
    public String getTier() {return sTier;}
    public String getRegion() {return sRegion;}
    public String getCountryName() {return sCountryName;}
    public String getOrgCode() {return sOrgCode;}
    public String getPartNumber() {return sPartNumber;}
    public String getQTY() {return sQTY;}
    public String getActivity() {return sActivity;}
    public String getTotalOH() {return sTotalOH;}
    public String getTotalXS() {return sTotalXS;}
    public String getTotalNd() {return sTotalNd;}
    public String getCurrentDate() {return sCurrentDate;}
    public String getDOM() {return sDOM;}
    public String getPartMoved() {return sPartMoved;}
    public String getTask() {return sTask;}
    public String getTracking() {return sTracking;}
    public String getConcCode() {return sConcCode;}
    public String getPosition() {return sPosition;}   
    
    //MAIN METHODS
    
    
}

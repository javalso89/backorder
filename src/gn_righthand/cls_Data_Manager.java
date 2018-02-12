package gn_righthand;

public class cls_Data_Manager {
    
    //VARIABLES DECLARATION*********************************************

    
    
    //CONSTRUCTORS SECTION**********************************************
    public cls_Data_Manager() {
    }
    
    

    
    //METHODS SECTION***************************************************
    
    
    
    public String[] getDifferentsBD(String[][] dataMatrix, int iCol){
    /* Receives a bidimentional array and the number of one of its columns
     * It returns an unidimentional array with all the different values that if finds in that column
    */    
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares an Unidimentional array that will store all the different values found in the specific column of the bidimentional array. Fulfills that array with n/a values
        String[] arrDifferents = new String[200]; 
        for ( int i=0; i<arrDifferents.length; i++ ){arrDifferents[i]="n/a";}
        int i=0;
        //Saves the first value of the column into the unidimentional array
        arrDifferents[i] = dataMatrix[1][iCol];
        for ( int r=2; r<dataMatrix.length; r++ ){
            if ( checkFor( dataMatrix[r][iCol], arrDifferents ) == -1 ){
                i++;
                arrDifferents[i] = dataMatrix[r][iCol];
            }
        }
        return arrDifferents;
    }
    //</editor-fold>    
    

    public String[] getDifferentsUD(String[] sDataArray){
    /* Receives a unidimentional array
     * It returns an unidimentional array with all the different values that if finds in that column
    */    
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        //Prepares an Unidimentional array that will store all the different values found in the specific column of the bidimentional array. Fulfills that array with n/a values
        String[] arrDifferents = new String[sDataArray.length]; 
        for ( int i=0; i<arrDifferents.length; i++ ){arrDifferents[i]="n/a";}
        int i=0;
        //Saves the first value of the column into the unidimentional array
        arrDifferents[i] = sDataArray[0];
        for ( int j=1; j<sDataArray.length; j++ ){
            if ( sDataArray[j] != null ){
                    if ( checkFor( sDataArray[j], arrDifferents ) == -1 ){
                    i++;
                    arrDifferents[i] = sDataArray[j];
                }
            }
        }
        return arrDifferents;
    }
    //</editor-fold>

    public int countDifferentsBD(String[] dataArray) {
    /* Receives a unidimentional array
     * It returns the number of item that it contains until reaching the 1st "n/a" value
    */    
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">    
        int iCount = 0;
        int i = 0;
        do {
            if ( !dataArray[i].equals("n/a") ){
                iCount = iCount + 1;
                i++;
            }
            else{break;}
        }
        while ( !dataArray[i].equals("n/a") );
        return iCount;
    }
    //</editor-fold> 
    
    private int checkFor(String sValue, String[] sArray ){
    //Looks for a value into an unidimentional Array and returns the first location number where it is (-1 means that the value is not present)        
    //<editor-fold defaultstate="collapsed" desc="Method Source Code">
        int iFlag = -1;
        for ( int i=0; i<sArray.length; i++ )
        {
            if ( sArray[i].equals(sValue) )
            {
                iFlag = i;
                break;
            }
        }
        return iFlag;
    }
    //</editor-fold>
    




    
}

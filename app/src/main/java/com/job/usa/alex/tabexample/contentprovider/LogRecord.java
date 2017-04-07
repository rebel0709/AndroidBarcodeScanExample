package com.job.usa.alex.tabexample.contentprovider;

/**
 * Created by SuperMaster on 4/4/2017.
 */

public class LogRecord {
    String timestamp;
    String code_type;
    String code_value;

    public LogRecord(String timestamp, String code_type, String code_value){
        this.timestamp=timestamp;
        this.code_type=code_type;
        this.code_value=code_value;
    }
    public String getTimeStamp(){return this.timestamp;}
    public String getCodeType(){return this.code_type;}
    public String getCodeValue(){return this.code_value;}
}

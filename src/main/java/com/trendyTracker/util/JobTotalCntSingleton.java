package com.trendyTracker.util;

public class JobTotalCntSingleton {
    private static JobTotalCntSingleton instance;
    private long totalJobCnt;

    public static JobTotalCntSingleton getInstance(){
        if( instance == null)
            instance = new JobTotalCntSingleton();

        return instance;
    }

    public void setTotalCnt(long cnt){
        this.totalJobCnt =cnt;
    }

    public long getTotalCnt(){
        return totalJobCnt;
    }

    public void increaseCnt(){
        totalJobCnt = totalJobCnt +1;
    }

    public void decreaseCnt(){
        totalJobCnt = totalJobCnt-1;
    }
}

package com.trendyTracker.Util;

public class JobTotalCntSingleton {
    // TODO 분산 시스템을 고려하면 Redis 로 변환 필요
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

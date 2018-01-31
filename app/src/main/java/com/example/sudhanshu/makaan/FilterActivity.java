package com.example.sudhanshu.makaan;

import android.content.Context;

/**
 * Created by sudhanshu on 1/31/18.
 */

class FilterActivity {
    private static FilterActivity filterActivity;
    public Context mContext;
    public boolean filterArray[]=new boolean[5];
    public long filterCount;

    public static FilterActivity getInstance(Context context){
        if(filterActivity==null)
            filterActivity=new FilterActivity(context);
        return filterActivity;
    }

    private FilterActivity(Context context) {
        mContext=context;
    }

    public StringBuffer getBedroomFilter(){
        filterCount=0;
        long bedroomCount;
        StringBuffer bedroomUrl=new StringBuffer();
        boolean filter=false;
        int i,count=0;
        for(i=0;i<5;i++){
            if(filterArray[i]){
                count++;
                if(filter){
                    bedroomUrl.append(","+(i+1));
                }
                else{
                    bedroomUrl.append(""+(i+1));
                    filter=true;
                }
            }
        }
        if(filter){
            bedroomCount=1;
            if(count>1){
                bedroomUrl.replace(0,bedroomUrl.length(),",{%22equal%22:{%22bedrooms%22:["+bedroomUrl+"]}}");
            }
            else
                bedroomUrl.replace(0,bedroomUrl.length(),",{%22equal%22:{%22bedrooms%22:"+bedroomUrl+"}}");
        }

        else{
            bedroomUrl.replace(0,bedroomUrl.length(),"");
            bedroomCount=0;

        }
        filterCount=filterCount+bedroomCount;
        return bedroomUrl;
    }



    public StringBuffer getRangeFilter(int minRange,int maxRange){
        long rangeCount;
        StringBuffer rangeUrl=new StringBuffer();
        if(minRange==0 && maxRange==100000000){
            rangeUrl.replace(0,rangeUrl.length(),"");
            rangeCount=0;
        }
        else{
            rangeUrl.replace(0,rangeUrl.length(),",{\"range\":{\"price\":{\"from\":"+minRange+",\"to\":"+maxRange+"}}}");
            rangeCount=1;
        }
        filterCount=filterCount+rangeCount;
        return rangeUrl;
    }

}

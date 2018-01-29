package com.example.sudhanshu.makaan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sudhanshu on 1/24/18.
 */

class MakaanDataAdapter extends RecyclerView.Adapter<MakaanDataAdapter.ViewHolder> {
    private MakaanListing makaanListing;
    private Context mContext;
    private ImageLoader imageLoader;
    private ArrayList<Apartment> arrayList;

    // arraylist is used for storing data

    /**
     * constructor declared
     * */
    public MakaanDataAdapter(Context context, MakaanListing makaan, ImageLoader image) {
        makaanListing = makaan;
        mContext=context;
        imageLoader=image;
        arrayList=new ArrayList<>(Arrays.asList(makaanListing.data[0].facetedResponse.items));
    }

    /**
     * viewholder subclass declared used for holding data
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price;
        public TextView rupees;
        public TextView bedroom;
        public TextView city;
        public TextView possession;
        public NetworkImageView networkImageView;
        public TextView currency;
        ViewHolder(View view) {
            super(view);
            price=view.findViewById(R.id.price);

            rupees=view.findViewById(R.id.rupees);


            bedroom=view.findViewById(R.id.bedroom);


            city=view.findViewById(R.id.city);

            possession=view.findViewById(R.id.possession);
            currency=view.findViewById(R.id.currency);
            networkImageView=view.findViewById(R.id.imageView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.card_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        long price=arrayList.get(position).listing.currentListingPrice.price;
        double cost=price;

        StringBuffer value=new StringBuffer(""+price);
        if(value.length()==4 || value.length()==5){
            holder.currency.setText("k");
            cost=cost/1000;
            value.replace(0,value.length(),""+cost);
            if(value.length()>4)
                value.replace(0,value.length(),value.substring(0,4));
        }

        else if(value.length()==6 || value.length()==7){
            holder.currency.setText("L");
            cost=cost/100000;
            value.replace(0,value.length(),""+cost);
            if(value.length()>4)
                value.replace(0,value.length(),value.substring(0,4));
        }

        else{
            holder.currency.setText("Cr");
            cost=cost/10000000;
            value.replace(0,value.length(),""+cost);
            if(value.length()>4)
                value.replace(0,value.length(),value.substring(0,4));
        }

        holder.price.setText(value);

        StringBuffer measure=new StringBuffer();
        if(arrayList.get(position).listing.property.measure!=null)
            measure.replace(0,measure.length(),arrayList.get(position).listing.property.measure);
        else
            measure.replace(0,measure.length(),"sq ft");


        holder.bedroom.setText(""+arrayList.get(position).listing.property.bedrooms+" BHK Apartment   "+arrayList.get(position).listing.property.size+" "+measure);
        StringBuffer name=new StringBuffer();
        if(arrayList.get(position).listing.property.unitType!=null){
            if(arrayList.get(position).listing.property.unitType.length()==9){
                if(arrayList.get(position).listing.property.project.name!=null)
                    name.replace(0,name.length(),arrayList.get(position).listing.property.project.name);
            }
            else
                name.replace(0,name.length(),arrayList.get(position).listing.property.unitType);


        }
        else
            name.replace(0,name.length(),"BuilderFloor");
        StringBuffer locality=new StringBuffer();
        StringBuffer suburb=new StringBuffer();
        if(arrayList.get(position).listing.property.project.locality.label!=null)
            locality.replace(0,locality.length(),arrayList.get(position).listing.property.project.locality.label);
        if(arrayList.get(position).listing.property.project.locality.suburb.label!=null)
            suburb.replace(0,suburb.length(),arrayList.get(position).listing.property.project.locality.suburb.label);
        holder.city.setText(""+name+"  |  "+locality+" "+suburb);

        /**
         * for setting suffix value for floor count
         */

        StringBuffer postText=new StringBuffer();
        long floor=arrayList.get(position).listing.floor;
        if(floor==0)
            postText.replace(0,postText.length()," ");
        else if(floor==1)
            postText.replace(0,postText.length(),"st");
        else if(floor==2)
            postText.replace(0,postText.length(),"nd");
        else if(floor==3)
            postText.replace(0,postText.length(),"rd");
        else if(floor>3)
            postText.replace(0,postText.length(),"th");


        /**
         * Below algorithm is used for converting epoch time to month and year
         */
        StringBuffer formatted=new StringBuffer();
        StringBuffer month=new StringBuffer();
        StringBuffer year=new StringBuffer();

        if(arrayList.get(position).listing.property.project.possessionDate!=null){
            long date_value=arrayList.get(position).listing.property.project.possessionDate;
            formatted.replace(0,formatted.length(),setDate(date_value));
            month.replace(0,month.length(), String.valueOf(getMonth(formatted.substring(5,7))));
            year.replace(0,year.length(), String.valueOf(formatted.substring(0,4)));
            holder.possession.setText("Possession by "+month+" "+year+"  |  "+floor+postText+" of "+arrayList.get(position).listing.totalFloors+" floor");
        }
        else
            holder.possession.setText("Possession by January 2018  |  "+floor+postText +" of "+arrayList.get(position).listing.totalFloors+" floor");

        holder.networkImageView.setImageUrl(arrayList.get(position).listing.mainImageURL,imageLoader);

    }

    /**
     * method used for extracting month
     */
    private StringBuffer getMonth(String month) {
        StringBuffer month_value=new StringBuffer();

        if(month.charAt(0)=='0' && month.charAt(1)=='1')
            month_value=month_value.replace(0,month_value.length(),"January");
        else if(month.charAt(0)=='1'&& month.charAt(1)=='2')
            month_value=month_value.replace(0,month_value.length(),"February");
        else if(month.charAt(1)=='3')
            month_value=month_value.replace(0,month_value.length(),"March");
        if(month.charAt(1)=='4')
            month_value=month_value.replace(0,month_value.length(),"April");
        if(month.charAt(1)=='5')
            month_value=month_value.replace(0,month_value.length(),"May");
        if(month.charAt(1)=='6')
            month_value=month_value.replace(0,month_value.length(),"June");
        if(month.charAt(1)=='7')
            month_value=month_value.replace(0,month_value.length(),"July");
        if(month.charAt(1)=='8')
            month_value=month_value.replace(0,month_value.length(),"August");
        if(month.charAt(1)=='9')
            month_value=month_value.replace(0,month_value.length(),"September");
        if(month.charAt(0)=='1'&& month.charAt(1)=='0')
            month_value=month_value.replace(0,month_value.length(),"October");
        if(month.charAt(0)=='1'&& month.charAt(1)=='1')
            month_value=month_value.replace(0,month_value.length(),"November");
        if(month.charAt(0)=='1' && month.charAt(1)=='2')
            month_value=month_value.replace(0,month_value.length(),"December");


        return month_value;
    }

    private String setDate(long date_value) {
        Date date = new Date(date_value);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        return formatted;
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /**
     * this method is used when new data is concatenated to the previous one
     *
     */
    protected void updateData(MakaanListing makaan){

        makaanListing=makaan;
        Collections.addAll(arrayList,makaanListing.data[0].facetedResponse.items);

    }

    /**
     * this method is used when filters are applied and hence arraylist will be cleared and
     * new data will be assigned to it
     */
    protected void setNewData(MakaanListing makaan){
        makaanListing=makaan;
        arrayList.clear();
        Collections.addAll(arrayList,makaanListing.data[0].facetedResponse.items);
    }


}

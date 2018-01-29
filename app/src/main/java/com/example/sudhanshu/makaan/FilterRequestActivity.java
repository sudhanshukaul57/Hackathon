package com.example.sudhanshu.makaan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

public class FilterRequestActivity extends AppCompatActivity {
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private Button button;
    private RangeseekBar<Integer> rangeSeekBar;
    private StringBuffer rangeUrl=new StringBuffer();
    private StringBuffer equalUrl=new StringBuffer();
    private StringBuffer mainUrl=new StringBuffer();
    private int min,max;
    private long count;
    private boolean mutex;
    private TextView seekBar1;
    private TextView seekBar2;
    private TextView seekBar3;
    private TextView seekBar4;
    private TextView bhk1;
    private TextView bhk2;
    private TextView bhk3;
    private TextView bhk4;
    private TextView bhk5;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_request);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /**
         * on pressing back button in action bar this method will be called
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        rangeSeekBar=findViewById(R.id.seekBar);
        checkBox1=findViewById(R.id.one);
        checkBox2=findViewById(R.id.two);
        checkBox3=findViewById(R.id.three);
        checkBox4=findViewById(R.id.four);
        checkBox5=findViewById(R.id.five);
        button=findViewById(R.id.button);
        loadPreferences();


        rangeSeekBar.setNotifyWhileDragging(true);
        /**
         * this method will be called when seekbar widget is used
         */
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeseekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeseekBar bar, Object minValue, Object maxValue) {
                min= (Integer) minValue;
                max= (Integer) maxValue;
                setRange(min,max);
            }
        });

        /**
         * This button is defined for filters.Pressing this button will pass return intent to the main activity
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setUrl();
                returnIntent.putExtra("result", (CharSequence) equalUrl);
                returnIntent.putExtra("count",count);
                setResult(Activity.RESULT_OK,returnIntent);
                savePreferences();
                Log.e("onClickListener ","minimum value is "+min+" maximum value is "+max);
                finish();
            }
        });


    }

    /**
     *This method is called for displaying range values
     */
    private void setRange(int minPrice,int maxPrice) {
        seekBar1=findViewById(R.id.first);
        seekBar2=findViewById(R.id.second);
        seekBar3=findViewById(R.id.third);
        seekBar4=findViewById(R.id.fourth);
        double min_range,max_range;

        StringBuffer value1=new StringBuffer(""+minPrice);
        StringBuffer value2=new StringBuffer(""+maxPrice);
        if(value1.length()==4 || value1.length()==5){
            seekBar2.setText("k");
            minPrice=minPrice/1000;
            value1.replace(0,value1.length(),""+minPrice);
        }

        else if(value1.length()==6 || value1.length()==7){
            seekBar2.setText("L - ");
            minPrice=minPrice/100000;
            value1.replace(0,value1.length(),""+minPrice);
        }

        else{
            seekBar2.setText("Cr - ");
            min_range=minPrice;
            min_range=min_range/10000000;
            value1.replace(0,value1.length(),""+min_range);
            value1.replace(0,value1.length(),""+value1.substring(0,3));
        }

        if(value2.length()==4 || value2.length()==5){
            seekBar4.setText("k");
            maxPrice=maxPrice/1000;
            value2.replace(0,value1.length(),""+maxPrice);
        }

        else if(value2.length()==6 || value2.length()==7){
            seekBar4.setText("L");
            maxPrice=maxPrice/100000;
            value2.replace(0,value2.length(),""+maxPrice);
        }

        else{
            seekBar4.setText("Cr");
            max_range=maxPrice;
            max_range=max_range/10000000;
            value2.replace(0,value2.length(),""+max_range);
            value2.replace(0,value2.length(),""+value2.substring(0,3));
        }

        seekBar1.setText(value1);
        seekBar3.setText(value2);
    }


    /**
     *Checkboxes are used as filters for bedrooms. This method will genrate subpart of url and will pass it to
     * main activity
     */
    public void onCheckBoxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();


        switch(view.getId()) {
            case R.id.one:
                bhk1=findViewById(R.id.one_bhk);
                if (checked){
                    bhk1.setTextColor(Color.WHITE);
                    count++;
                    if(mainUrl.length()==0)
                        mainUrl.append("1");
                    else
                        mainUrl.append(",1");

                }

                else{
                    bhk1.setTextColor(Color.BLACK);
                    count--;
                    if(mainUrl.charAt(0)=='1'){
                        if(mainUrl.length()==1){
                            mainUrl.deleteCharAt(0);
                            break;
                        }
                        else
                            mainUrl.deleteCharAt(0);
                            mainUrl.deleteCharAt(0);
                    }
                    else if(mainUrl.charAt(mainUrl.length()-1)=='1'){
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                    }
                    else{
                        int index =mainUrl.indexOf("1");
                        mainUrl.deleteCharAt(index-1);
                        mainUrl.deleteCharAt(index-1);
                        }

                }
                break;

            case R.id.two:
                bhk2=findViewById(R.id.two_bhk);
                if (checked){
                    bhk2.setTextColor(Color.WHITE);

                    count++;
                    if(mainUrl.length()==0)
                        mainUrl.append("2");
                    else
                        mainUrl.append(",2");

                }

                else{
                    bhk2.setTextColor(Color.BLACK);
                    count--;
                    if(mainUrl.charAt(0)=='2'){
                        if(mainUrl.length()==1){
                            mainUrl.deleteCharAt(0);
                            break;
                        }
                        else
                            mainUrl.deleteCharAt(0);
                            mainUrl.deleteCharAt(0);
                    }
                    else if(mainUrl.charAt(mainUrl.length()-1)=='2'){
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                    }
                    else{
                        int index =mainUrl.indexOf("2");
                        mainUrl.deleteCharAt(index-1);
                        mainUrl.deleteCharAt(index-1);
                        }

                }
                break;

            case R.id.three:
                bhk3=findViewById(R.id.three_bhk);
                if (checked){
                    bhk3.setTextColor(Color.WHITE);
                    count++;
                    if(mainUrl.length()==0)
                        mainUrl.append("3");
                    else
                        mainUrl.append(",3");

                }

                else{
                    bhk3.setTextColor(Color.BLACK);
                    count--;
                    if(mainUrl.charAt(0)=='3'){
                        if(mainUrl.length()==1){
                            mainUrl.deleteCharAt(0);
                            break;
                        }
                        else
                            mainUrl.deleteCharAt(0);
                            mainUrl.deleteCharAt(0);
                    }
                    else if(mainUrl.charAt(mainUrl.length()-1)=='3'){
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                    }
                    else{
                        int index =mainUrl.indexOf("3");
                        mainUrl.deleteCharAt(index-1);
                        mainUrl.deleteCharAt(index-1);
                        }

                }
                break;

            case R.id.four:
                bhk4=findViewById(R.id.four_bhk);
                if (checked){
                    bhk4.setTextColor(Color.WHITE);
                    count++;
                    if(mainUrl.length()==0)
                        mainUrl.append("4");
                    else
                        mainUrl.append(",4");

                }

                else{
                    bhk4.setTextColor(Color.BLACK);
                    count--;
                    if(mainUrl.charAt(0)=='4'){
                        if(mainUrl.length()==1){
                            mainUrl.deleteCharAt(0);
                            break;
                        }
                        else
                            mainUrl.deleteCharAt(0);
                            mainUrl.deleteCharAt(0);
                    }
                    else if(mainUrl.charAt(mainUrl.length()-1)=='4'){
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                    }
                    else{
                        int index =mainUrl.indexOf("4");
                        mainUrl.deleteCharAt(index-1);
                        mainUrl.deleteCharAt(index-1);
                        }

                }
                break;

            case R.id.five:
                bhk5=findViewById(R.id.five_bhk);
                if (checked){
                    bhk5.setTextColor(Color.WHITE);
                    count++;
                    if(mainUrl.length()==0)
                        mainUrl.append("5");
                    else
                        mainUrl.append(",5");

                }

                else{
                    bhk5.setTextColor(Color.BLACK);
                    count--;
                    if(mainUrl.charAt(0)=='5'){
                        if(mainUrl.length()==1){
                            mainUrl.deleteCharAt(0);
                            break;
                        }
                        else
                            mainUrl.deleteCharAt(0);
                            mainUrl.deleteCharAt(0);
                    }
                    else if(mainUrl.charAt(mainUrl.length()-1)=='5'){
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                        mainUrl.deleteCharAt(mainUrl.length()-1);
                    }
                    else{
                        int index =mainUrl.indexOf("5");
                        mainUrl.deleteCharAt(index-1);
                        mainUrl.deleteCharAt(index-1);
                        }
                }

        }

    }

    /**
     * this method is used for generating url which contains filters for both budget and bedrooms
     */
    protected void setUrl(){
        if(mainUrl.length()!=0){
            if(mainUrl.length()==1)
                equalUrl.replace(0,equalUrl.length(),",{%22equal%22:{%22bedrooms%22:"+mainUrl+"}}");
            else
                equalUrl.replace(0,equalUrl.length(),",{%22equal%22:{%22bedrooms%22:["+mainUrl+"]}}");
        }
        else
            equalUrl.replace(0,equalUrl.length(),"");

        if(min==0 && max==100000000){
            rangeUrl.replace(0,rangeUrl.length(),"");
        }
        else{
            rangeUrl.replace(0,rangeUrl.length(),",{\"range\":{\"price\":{\"from\":"+min+",\"to\":"+max+"}}}");
        }
        if((min!=0 ||max!=100000000)&&mutex){
            count++;
            mutex=false;
        }
        if(min==0 && max==100000000&&!mutex){
            count--;
            mutex=true;
        }

        equalUrl.replace(0,equalUrl.length(),""+equalUrl+rangeUrl);
    }

    /**
     * Shared Preferences are used here for saving the state of filters data
     */
    protected void savePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("box1",checkBox1.isChecked());
        editor.putBoolean("box2",checkBox2.isChecked());
        editor.putBoolean("box3",checkBox3.isChecked());
        editor.putBoolean("box4",checkBox4.isChecked());
        editor.putBoolean("box5",checkBox5.isChecked());
        editor.putString("url",""+mainUrl);
        editor.putInt("min",min);
        editor.putInt("max",max);
        editor.putLong("count_filter",count);
        editor.putBoolean("mutex",mutex);
        editor.commit();
    }


    /**
     * this method will be called in onCreate and hence all previous data will be restored
     */
    protected void loadPreferences(){
        rangeSeekBar.setRangeValues(0,100000000);
        SharedPreferences sharedPreferences = getSharedPreferences("preferences",MODE_PRIVATE);
        checkBox1.setChecked(sharedPreferences.getBoolean("box1",false));
        checkBox2.setChecked(sharedPreferences.getBoolean("box2",false));
        checkBox3.setChecked(sharedPreferences.getBoolean("box3",false));
        checkBox4.setChecked(sharedPreferences.getBoolean("box4",false));
        checkBox5.setChecked(sharedPreferences.getBoolean("box5",false));
        mainUrl.replace(0,mainUrl.length(),sharedPreferences.getString("url",""));
        rangeSeekBar.setSelectedMinValue(sharedPreferences.getInt("min",0));
        rangeSeekBar.setSelectedMaxValue( sharedPreferences.getInt("max",100000000));
        min=  sharedPreferences.getInt("min",0);
        max=  sharedPreferences.getInt("max",100000000);
        count=sharedPreferences.getLong("count_filter",0);
        mutex=sharedPreferences.getBoolean("mutex",true);
        setRange(min,max);
        }

    /**
     * here onDestroy() will pass return o
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Filter"," onDestroy");
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


}

package com.example.admin.chamaapp.admin.Presenter;

import android.graphics.Color;
import android.util.Log;

import com.example.admin.chamaapp.Model.Contribution;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;


public class MyDetailsPresenter
{
    public View view;

    public MyDetailsPresenter(View view)
    {
        this.view = view;
    }

    public int months(Contribution contribution)
    {
        int count = 0;
        if(contribution.getJan() != 0)
        {
            count= count + 1;
        }
        if(contribution.getFeb() != 0)
        {
            count= count + 1;
        }
        if(contribution.getMarch() != 0)
        {
            count= count + 1;
        }
        if(contribution.getApril() != 0)
        {
            count= count + 1;
        }
        if(contribution.getMayy() != 0)
        {
            count= count + 1;
        }
        if(contribution.getJune() != 0)
        {
            count= count + 1;
        }
        if(contribution.getJuly() != 0)
        {
            count= count + 1;
        }
        if(contribution.getaugust() != 0)
        {
            count= count + 1;
        }
        if(contribution.getSeptemeber() != 0)
        {
            count= count + 1;
        }
        if(contribution.getOctober() != 0)
        {
            count= count + 1;
        }
        if(contribution.getNovember() != 0)
        {
            count= count + 1;
        }
        if(contribution.getDecember() != 0)
        {
            count= count + 1;
        }


        view.displayMonths(count);
        return count;
    }

    public String getDateInWords()
    {
        String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December" };
        Calendar cal = Calendar.getInstance();
        String month = monthName[cal.get(Calendar.MONTH)];
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        Log.d("Year","This is the year " + year);
        String date = day +" " + month + " " + year;
        System.out.println("Date name: " + date);

        return date;
    }

    public void drawAgraph(Contribution contribution)
    {

        String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
                "Oct", "Nov", "Dec"};

        int[] yAxisData = {contribution.getJan(),
                contribution.getFeb(),
                contribution.getMarch(),
                contribution.getApril(),
                contribution.getMayy(),
                contribution.getJune(),
                contribution.getJuly(),
                contribution.getaugust(),
                contribution.getSeptemeber(),
                contribution.getOctober(),
                contribution.getNovember(),
                contribution.getDecember()};

        int[] yAxisDataReference = {500, 200, 150, 300, 200, 600, 150, 400, 450, 100, 900, 180};


        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();
        List yAxisValuesReference = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#00EEEE"));
        Line lineReference = new Line(yAxisValuesReference).setColor(Color.parseColor("#00EEEE"));

        for(int i = 0; i < axisData.length; i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++){
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        for (int i = 0; i < yAxisDataReference.length; i++){
            yAxisValuesReference.add(new PointValue(i, yAxisDataReference[i]));
        }


        List lines = new ArrayList();
        lines.add(line);
        lines.add(lineReference);

        LineChartData data = new LineChartData();
        data.setLines(lines);


        view.setLineChartData(data);

        Axis axis = new Axis();
        axis.setTextColor(Color.parseColor("#FF4081"));
        axis.setTextSize(16);
        axis.setValues(axisValues);
        axis.setName("Contributions in Ksh");
        data.setAxisXBottom(axis);


        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#FF4081"));
        yAxis.setTextSize(16);
        yAxis.setName("Months of the year");
        data.setAxisYLeft(yAxis);

        view.setLineChartData(data);

       int highest =  getMax(yAxisDataReference);
        view.setLinecChartDataViewPort(highest);



    }

    // Method for getting the maximum value
    public static int getMax(int[] inputArray){
        int maxValue = inputArray[0];
        for(int i=1;i < inputArray.length;i++){
            if(inputArray[i] > maxValue){
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

    public interface View
    {
        void displayMonths(int count);
        void setLineChartData(LineChartData data);
        void setLinecChartDataViewPort(int viewPort);
    }
}

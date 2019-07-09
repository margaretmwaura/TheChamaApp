package com.example.admin.chamaapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Contribution implements Parcelable
{

    public int january;
    public int february;
    public int march;
    public int april;
    public int may;
    public int june;
    public int july;
    public int august;
    public int september;
    public int october;
    public int november;
    public int december;


    public Contribution(int a,int b,int c, int d ,int e ,int f ,int g ,int i , int j, int k , int l , int m)
    {
        this.january = a;
        this.february = b;
        this.march = c;
        this.april = d;
        this.may = e;
        this.june = f;
        this.july = g;
        this.august= i;
        this.september  = j;
        this.october = k;
        this.november = l;
        this.december= m;
    }

    public Contribution()
    {

    }

    public void setJan(int a)
    {
        this.january = a;
    }
    public int getJan()
    {
        return january;
    }
    public void setFeb(int b)
    {
        this.february = b;
    }
    public int getFeb()
    {
        return february;
    }
    public int getMarch()
    {
        return march;
    }
    public void setMarch(int m)
    {
        this.march = m;
    }
    public void setApril(int a)
    {
        this.april = a;
    }
    public int getApril()
    {
        return this.april;
    }
    public void setMayy(int m)
    {
        this.may = m;
    }
    public int getMayy()
    {
        return this.may;
    }
    public int getJune()
    {
        return this.june;
    }
    public void setJune(int j)
    {
        this.june = j;
    }
    public int getJuly()
    {
        return this.july;
    }
    public void setJuly(int j)
    {
        this.july = j;
    }
    public int getaugust()
    {
        return this.august;
    }
    public void setAugust(int a)
    {
        this.august = a;
    }
    public int getSeptemeber()
    {
        return this.september;
    }
    public void setSeptemeber(int s)
    {
        this.september = s;
    }
    public int getOctober()
    {
        return this.october;
    }
    public void setOctober(int o)
    {
        this.october = o;
    }
    public void setNovember(int n)
    {
        this.november = n;
    }
    public int getNovember()
    {
        return this.november;
    }
    public int getDecember()
    {
        return this.december;
    }
    public void setDecember(int d)
    {
        this.december = d;
    }
    protected Contribution(Parcel in) {
        january = in.readInt();
        february = in.readInt();
        march = in.readInt();
        april = in.readInt();
        may = in.readInt();
        june = in.readInt();
        july = in.readInt();
        august = in.readInt();
        september = in.readInt();
        october = in.readInt();
        november = in.readInt();
        december = in.readInt();
    }

    public static final Creator<Contribution> CREATOR = new Creator<Contribution>() {
        @Override
        public Contribution createFromParcel(Parcel in) {
            return new Contribution(in);
        }

        @Override
        public Contribution[] newArray(int size) {
            return new Contribution[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(january);
        dest.writeInt(february);
        dest.writeInt(march);
        dest.writeInt(april);
        dest.writeInt(may);
        dest.writeInt(june);
        dest.writeInt(july);
        dest.writeInt(august);
        dest.writeInt(september);
        dest.writeInt(october);
        dest.writeInt(november);
        dest.writeInt(december);
    }
}

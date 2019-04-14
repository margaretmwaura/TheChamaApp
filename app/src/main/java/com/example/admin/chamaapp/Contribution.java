package com.example.admin.chamaapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Contribution implements Parcelable
{
    public int jan;
    public int feb;
    public int march;
    public int april;
    public int mayy;
    public int june;
    public int july;
    public int august;
    public int septemeber;
    public int october;
    public int november;
    public int december;
    public String phonenumber;


    public Contribution(int a,int b,int c, int d ,int e ,int f ,int g ,int i , int j, int k , int l , int m,String phonenumber)
    {
        this.jan = a;
        this.feb = b;
        this.march = c;
        this.april = d;
        this.mayy = e;
        this.june = f;
        this.july = g;
        this.august= i;
        this.septemeber  = j;
        this.october = k;
        this.november = l;
        this.december = m;
        this.phonenumber = phonenumber;
    }

    public Contribution()
    {

    }

    public void setJan(int a)
    {
        this.jan = a;
    }
    public int getJan()
    {
        return jan;
    }
    public void setFeb(int b)
    {
        this.feb = b;
    }
    public int getFeb()
    {
        return feb;
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
        this.mayy = m;
    }
    public int getMayy()
    {
        return this.mayy;
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
        return this.septemeber;
    }
    public void setSeptemeber(int s)
    {
        this.septemeber = s;
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
    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }
    public String getPhonenumber()
    {
        return this.phonenumber;
    }
    protected Contribution(Parcel in) {
        jan = in.readInt();
        feb = in.readInt();
        march = in.readInt();
        mayy = in.readInt();
        june = in.readInt();
        july = in.readInt();
        august = in.readInt();
        septemeber = in.readInt();
        october = in.readInt();
        november = in.readInt();
        december = in.readInt();
        phonenumber = in.readString();
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
        dest.writeInt(jan);
        dest.writeInt(feb);
        dest.writeInt(march);
        dest.writeInt(mayy);
        dest.writeInt(june);
        dest.writeInt(july);
        dest.writeInt(august);
        dest.writeInt(septemeber);
        dest.writeInt(october);
        dest.writeInt(november);
        dest.writeInt(december);
        dest.writeString(phonenumber);
    }
}

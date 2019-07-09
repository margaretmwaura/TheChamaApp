package com.example.admin.chamaapp.admin.Presenter;

import android.util.Log;
import android.view.View;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;


import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import androidx.annotation.NonNull;

public class NavigationDrawerPresenter
{
    public View view;

    public NavigationDrawerPresenter(View view)
    {
        this.view = view;
    }
    public String[] getUserDetails ()
    {

        String[] details = new String[2];
        String detail;
        File directory = view.getTheActivityFile();
//      File directory = this.getFilesDir();
        File file = new File(directory,"UserDetails" );

        try {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(in);

            details = new String[2];
            details = (String[]) s.readObject();

            for(int i = 0; i<details.length;i++)
            {
                detail = details[i];
                Log.d("UserDetails","This are the user details " + detail);
            }
            return details;
        }
        catch (Exception e)
        {
            Log.d("ErrorFileReading","Error encountered while reading the file " + e.getMessage());

            return details;

        }

    }

    public Daraja createDaraja()
    {
        Daraja daraja = Daraja.with("08MUHjEBN7qJ5RhfYR008fVFbw0R1i4N", "UOYiGP1PyFkvQL8U", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.d("DarajaCreation","The daraja class has beeen created");
                Log.i("AccessToken", accessToken.getAccess_token());
            }

            @Override
            public void onError(String error) {
                Log.d("DarajaCreation","The daraja class has not been created,an error has been encountered" + error);
                Log.e("AccessToken", error);
            }
        });

        return daraja;
    }
    public void requestMpesa(String mpesaPhoneNumber , Daraja daraja)
    {
        LNMExpress lnmExpress = new LNMExpress(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                TransactionType.CustomerPayBillOnline,
                "100",
                "254798436887",
                "174379",
                mpesaPhoneNumber,
                "http://mycallbackurl.com/checkout.php",
                "001ABC",
                "Goods Payment"
        );

       daraja.requestMPESAExpress(lnmExpress,
               new DarajaListener<LNMResult>() {
                   @Override
                   public void onResult(@NonNull LNMResult lnmResult) {
                       Log.d("SendingMoney","Money has been sent");
                   }

                   @Override
                   public void onError(String error) {
                       Log.d("SendingMoney","Money has not been sent " + error);
                   }
               });
    }
    public interface View
    {
        File getTheActivityFile();
    }
}

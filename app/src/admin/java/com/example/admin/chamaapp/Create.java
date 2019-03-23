package com.example.admin.chamaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Create extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);

        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.


//        Without this line of code the navigation bar is not transparent
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        this.getWindow().setFlags(uiOptions,0);

//blurring the background image
        LinearLayout newContainer= (LinearLayout) findViewById(R.id.root);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        newContainer.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

    }
    public void openSign(View view)
    {
        final Intent s = new Intent( this, Sign.class);
        startActivity(s);
    }
    public void openLogin(View view)
    {
        final Intent s = new Intent( this, LoginInsteadOfCreating.class);
        startActivity(s);
    }

}

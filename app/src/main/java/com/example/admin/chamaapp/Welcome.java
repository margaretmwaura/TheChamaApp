package com.example.admin.chamaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Welcome extends AppCompatActivity
{
    private TextView tv;
    private TextView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        this.getWindow().setFlags(uiOptions,0);

//blurring the background image
        RelativeLayout mContainerView = (RelativeLayout) findViewById(R.id.big);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.newback);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//End of code of blurring the background image


        iv= (TextView) findViewById(R.id.logo);
        tv = (TextView) findViewById(R.id.welcomeText);

//        tv= (TextView) findViewById(R.id.tv);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        final Animation textanim = AnimationUtils.loadAnimation(this,R.anim.textviewanimation);

        Animation.AnimationListener animationInListener
                = new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                tv.setVisibility(View.VISIBLE);
                tv.startAnimation(textanim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }};

        myanim.setAnimationListener(animationInListener);
        iv.startAnimation(myanim);

        /**code for the halting of the other activity **/
        final Intent i = new Intent(this,Create.class);
        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(20000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();


    }

}

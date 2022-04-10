package com.example.cookieclickergame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    ImageView mainTesla;
    TextView teslaValuationText;
    static AtomicInteger valuation;
    int passiveIncome;
    TextView incrementIndicator;
    ConstraintLayout constraintLayout;
    TextView storeText;
    ImageView upgradeImage;
    ImageView bigSuperCharger;
    TextView passiveIncomeText;
    boolean chargerVisible = false;
    int numSuperChargers;
    Switch levelSwitch;
    boolean isEasyLevel;
    final ScaleAnimation upgradeAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    final ScaleAnimation bigUpgradeAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTesla = findViewById(R.id.id_mainTesla);
        teslaValuationText = findViewById(R.id.id_teslaValuationText);
        passiveIncomeText = findViewById(R.id.id_passiveIncomeTest);
        valuation = new AtomicInteger(0);
        passiveIncome = 0;
        numSuperChargers = 0;
        constraintLayout = findViewById(R.id.layout);
        storeText = findViewById(R.id.id_storeText);
        levelSwitch = findViewById(R.id.id_levelSwitch);
        bigSuperCharger = findViewById(R.id.id_bigSuperCharger);
        bigSuperCharger.setVisibility(View.INVISIBLE);

        SuperCharger superChargerThread = new SuperCharger();
        superChargerThread.start();
        PassiveIncome passiveIncomeThread = new PassiveIncome();
        passiveIncomeThread.start();
        HardLevel hardLevelThread = new HardLevel();
        hardLevelThread.start();

        upgradeAnimation.setDuration(250);
        bigUpgradeAnimation.setDuration(500);


        isEasyLevel = true;
        levelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isEasyLevel = b;
                if(b){
                    levelSwitch.setText("Easy");
                }
                else{
                    levelSwitch.setText("Hard");
                }
            }
        });


        final ScaleAnimation mainTeslaAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mainTeslaAnimation.setDuration(250);

        mainTesla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(mainTeslaAnimation);
                valuation.addAndGet(100);
                teslaValuationText.setText("Rocketship Valuation: $ " + valuation);



                incrementIndicator = new TextView(MainActivity.this);
                incrementIndicator.setId(View.generateViewId());
                incrementIndicator.setText("+$100");
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                incrementIndicator.setLayoutParams(params);

                constraintLayout.addView(incrementIndicator);


                ConstraintSet incrementConstraintSet = new ConstraintSet();
                incrementConstraintSet.clone(constraintLayout);
                incrementConstraintSet.connect(incrementIndicator.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                incrementConstraintSet.connect(incrementIndicator.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                incrementConstraintSet.connect(incrementIndicator.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                incrementConstraintSet.connect(incrementIndicator.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                float randHorizontalBias = (float) (Math.random() * 0.5 + 0.25f);
                incrementConstraintSet.setHorizontalBias(incrementIndicator.getId(), randHorizontalBias);
                incrementConstraintSet.setVerticalBias(incrementIndicator.getId(), 0.5f);

                incrementConstraintSet.applyTo(constraintLayout);


                TranslateAnimation incrementAnimation = new TranslateAnimation(randHorizontalBias, randHorizontalBias, 0, -300);
                incrementAnimation.setDuration(1000);

                incrementIndicator.startAnimation(incrementAnimation);
                incrementIndicator.setVisibility(View.INVISIBLE);


            }
        });
    }

    public class PassiveIncome extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){}
                valuation.getAndAdd(passiveIncome);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teslaValuationText.setText("Rocketship Valuation: $ " + valuation);
                        passiveIncomeText.setText("Passive Income: $" + passiveIncome + "/sec");
                        storeText.setText(numSuperChargers + " SuperChargers");
                    }
                });

            }
        }
    }

    public class HardLevel extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(5000);
                }catch(Exception e){}
                if(isEasyLevel == false){
                    final int randomDecrease = (int)(Math.random() * 1000);
                    valuation.getAndAdd(-randomDecrease);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            teslaValuationText.setText("Rocketship Valuation: $ " + valuation);
                            Toast randomDecreaseToast = Toast.makeText(MainActivity.this, "You Just Lost $" + randomDecrease + "!", Toast.LENGTH_SHORT);
                            randomDecreaseToast.show();
                        }
                    });
                }


            }
        }
    }

    public class SuperCharger extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(1);
                }
                catch(Exception e){ }
                if(valuation.get() >= 1000 && (chargerVisible == false)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            upgradeImage = new ImageView(MainActivity.this);
                            upgradeImage.setImageResource(R.drawable.supercharger);
                            upgradeImage.setId(View.generateViewId());
                            upgradeImage.setVisibility(View.VISIBLE);
                            bigSuperCharger.setVisibility(View.VISIBLE);
                            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                            upgradeImage.setLayoutParams(params);
                            constraintLayout.addView(upgradeImage);

                            ConstraintSet upgradeConstraintSet = new ConstraintSet();
                            upgradeConstraintSet.clone(constraintLayout);
                            upgradeConstraintSet.connect(upgradeImage.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                            upgradeConstraintSet.connect(upgradeImage.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                            upgradeConstraintSet.connect(upgradeImage.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                            upgradeConstraintSet.connect(upgradeImage.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                            upgradeConstraintSet.setHorizontalBias(upgradeImage.getId(), 0);
                            upgradeConstraintSet.setVerticalBias(upgradeImage.getId(), 0.25f);

                            upgradeConstraintSet.applyTo(constraintLayout);

                            chargerVisible = true;
                            upgradeImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    view.startAnimation(upgradeAnimation);
                                    if(valuation.get() >= 1000){
                                        passiveIncome+=50;
                                        numSuperChargers+=1;
                                        valuation.addAndGet(-1000);

                                        TranslateAnimation upgradeTranslateAnimation = new TranslateAnimation(0f, 250, 0, 700);
                                        upgradeTranslateAnimation.setDuration(500);
                                        view.startAnimation(upgradeTranslateAnimation);
                                        bigSuperCharger.startAnimation(bigUpgradeAnimation);
                                    }
                                    else{
                                        Toast insufficientToast = Toast.makeText(MainActivity.this, "You Cannot Afford a SuperCharger!", Toast.LENGTH_SHORT);
                                        insufficientToast.show();
                                    }



                                }
                            });
                        }
                    });
                }

            }
        }

    }
}
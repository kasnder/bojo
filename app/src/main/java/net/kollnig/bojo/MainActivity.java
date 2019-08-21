package net.kollnig.bojo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mPlayer;
    TextView txtDebts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Calculate debts
        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH, 23);
        thatDay.set(Calendar.MONTH, 5);
        thatDay.set(Calendar.YEAR, 2016);
        Calendar today = Calendar.getInstance();
        long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();
        double d = 24. * 60. * 60. * 1000.;
        final Double days = diff / d;
        final String debts = "Owes the NHS: £" + round(days * (350./7.) / 1000., 2) + "bn";

        // Show debts
        txtDebts = findViewById(R.id.txtDebts);
        txtDebts.setVisibility(View.INVISIBLE);
        txtDebts.setText(debts);

        // Listen to BoJo
        mPlayer = MediaPlayer.create(MainActivity.this, R.raw.bojo);

        View bojo = findViewById(R.id.imgBojo);
        bojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                mPlayer.seekTo(0);
                mPlayer.start();
                txtDebts.setVisibility(View.VISIBLE);

                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(200); // manage blinking time
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                txtDebts.startAnimation(anim);
            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void onDestroy() {
        mPlayer.release();
        mPlayer = null;
        super.onDestroy();
    }
}

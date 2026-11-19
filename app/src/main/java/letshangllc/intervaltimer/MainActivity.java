package letshangllc.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /* TextViews */
    private TextView tvIntervals, tvMinuteInterval, tvSecondInterval,
            tvMinuteRest, tvSecondRest;

    /* Number of Intervals */
    private int numIntervals;

    private AdsHelper adsHelper;
    private Timer adRefreshTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyWindowInsets();
        this.findViews();
        this.setupViews();

        this.startAds();
    }

    private void applyWindowInsets() {
        final View content = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(content, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    public void findViews() {
        tvIntervals = findViewById(R.id.tv_intervals);

        tvMinuteInterval = findViewById(R.id.tv_minuteInterval);
        tvSecondInterval = findViewById(R.id.tv_secondInterval);

        tvMinuteRest = findViewById(R.id.tv_minuteRest);
        tvSecondRest = findViewById(R.id.tv_secondRest);
    }

    public void setupViews() {
        final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);

        numIntervals = settings.getInt(getString(R.string.prefs_num_intervals), 0);
        int minutesInterval = settings.getInt(getString(R.string.prefs_minutes_interval), 0);
        int secondsInterval = settings.getInt(getString(R.string.prefs_seconds_interval), 0);

        Log.i(TAG, "Min Int: " + minutesInterval);
        Log.i(TAG, "Sec Int: " + secondsInterval);

        tvMinuteInterval.setText(String.format(Locale.US, "%02d", minutesInterval));
        tvSecondInterval.setText(String.format(Locale.US, "%02d", secondsInterval));

        int minutesRest = settings.getInt(getString(R.string.prefs_minutes_rest), 0);
        int secondsRest = settings.getInt(getString(R.string.prefs_seconds_rest), 0);

        Log.i(TAG, "Min Rest: " + minutesRest);
        Log.i(TAG, "Sec Rest: " + secondsRest);

        tvMinuteRest.setText(String.format(Locale.US, "%02d", minutesRest));
        tvSecondRest.setText(String.format(Locale.US, "%02d", secondsRest));

        tvMinuteInterval.setOnClickListener(new onClickInterval());
        tvSecondInterval.setOnClickListener(new onClickInterval());

        tvMinuteRest.setOnClickListener(new onClickRest());
        tvSecondRest.setOnClickListener(new onClickRest());

        tvIntervals.setText(String.format(Locale.US, "%01d", numIntervals));
    }


    private class onClickInterval implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);

            int minutes = settings.getInt(getString(R.string.prefs_minutes_interval), 0);
            int seconds = settings.getInt(getString(R.string.prefs_seconds_interval), 0);


            TimePickerDialog timePickerDialog = new TimePickerDialog();

            timePickerDialog.setMinutes(minutes);
            timePickerDialog.setSeconds(seconds);

            timePickerDialog.setCallback((minutes1, seconds1) -> {
                tvMinuteInterval.setText(String.format(Locale.US, "%02d", minutes1));
                tvSecondInterval.setText(String.format(Locale.US, "%02d", seconds1));

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(getString(R.string.prefs_minutes_interval), minutes1);
                editor.putInt(getString(R.string.prefs_seconds_interval), seconds1);
                editor.apply();
            });
            timePickerDialog.show(getSupportFragmentManager(), TAG);
        }
    }

    private class onClickRest implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);
            int minutes = settings.getInt(getString(R.string.prefs_minutes_rest), 0);
            int seconds = settings.getInt(getString(R.string.prefs_seconds_rest), 0);


            TimePickerDialog timePickerDialog = new TimePickerDialog();

            timePickerDialog.setMinutes(minutes);
            timePickerDialog.setSeconds(seconds);

            timePickerDialog.setCallback((minutes1, seconds1) -> {
                tvMinuteRest.setText(String.format(Locale.US, "%02d", minutes1));
                tvSecondRest.setText(String.format(Locale.US, "%02d", seconds1));

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(getString(R.string.prefs_minutes_rest), minutes1);
                editor.putInt(getString(R.string.prefs_seconds_rest), seconds1);
                editor.apply();
            });
            timePickerDialog.show(getSupportFragmentManager(), TAG);
        }
    }

    public void subOneInterval(View view) {
        numIntervals = Integer.parseInt(tvIntervals.getText().toString());

        if (numIntervals > 0) {
            numIntervals--;
        }
        tvIntervals.setText(String.format(Locale.US, "%d", numIntervals));

        this.saveNumIntervals();
    }

    public void addOneInterval(View view) {
        numIntervals = Integer.parseInt(tvIntervals.getText().toString());
        numIntervals++;
        tvIntervals.setText(String.format(Locale.US, "%d", numIntervals));
        this.saveNumIntervals();
    }

    public void startRoutineOnClick(View view) {
        this.saveNumIntervals();
        if (numIntervals == 0) {
            Toast.makeText(this, "Please enter a number of intervals", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, IntervalActivity.class);
            startActivity(intent);
        }
    }

    private void saveNumIntervals() {
        final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(getString(R.string.prefs_num_intervals), numIntervals);
        editor.apply();
    }

    private void startAds() {
        adsHelper = new AdsHelper(getWindow().getDecorView(),
                getResources().getString(R.string.admob_id_main), this);

        adsHelper.setUpAds();
        int delay = 1000;
        int period = getResources().getInteger(R.integer.ad_refresh_rate);
        adRefreshTimer = new Timer();
        adRefreshTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                adsHelper.refreshAd();
            }
        }, delay, period);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adsHelper != null) {
            adsHelper.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adsHelper != null) {
            adsHelper.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (adRefreshTimer != null) {
            adRefreshTimer.cancel();
            adRefreshTimer = null;
        }
        if (adsHelper != null) {
            adsHelper.onDestroy();
        }
        super.onDestroy();
    }
}

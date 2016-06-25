package letshangllc.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /* TextViews */
    private TextView tvIntervals, tvMinuteInterval,tvSecondInterval ,
            tvMinuteRest,tvSecondRest;

    /* Number of Intervals */
    private int numIntervals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViews();
        this.setupViews();

        this.startAds();
    }

    public void findViews(){
        tvIntervals = (TextView) findViewById(R.id.tv_intervals);

        tvMinuteInterval = (TextView) findViewById(R.id.tv_minuteInterval);
        tvSecondInterval = (TextView) findViewById(R.id.tv_secondInterval);

        tvMinuteRest = (TextView) findViewById(R.id.tv_minuteRest);
        tvSecondRest = (TextView) findViewById(R.id.tv_secondRest);
    }

    public void setupViews(){
        final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);

        numIntervals = settings.getInt(getString(R.string.prefs_num_intervals), 0);
        int minutesInterval = settings.getInt(getString(R.string.prefs_minutes_interval),0);
        int secondsInterval = settings.getInt(getString(R.string.prefs_seconds_interval),0);

        Log.i(TAG, "Min Int: " +minutesInterval);
        Log.i(TAG, "Sec Int: " + secondsInterval);

        tvMinuteInterval.setText(String.format(Locale.US, "%02d", minutesInterval));
        tvSecondInterval.setText(String.format(Locale.US, "%02d", secondsInterval));

        int minutesRest = settings.getInt(getString(R.string.prefs_minutes_rest),0);
        int secondsRest = settings.getInt(getString(R.string.prefs_seconds_rest),0);

        Log.i(TAG, "Min Rest: " +minutesRest);
        Log.i(TAG, "Sec Rest: " + secondsRest);

        tvMinuteRest.setText(String.format(Locale.US, "%02d", minutesRest));
        tvSecondRest.setText(String.format(Locale.US, "%02d", secondsRest));

        tvMinuteInterval.setOnClickListener(new onClickInterval());
        tvSecondInterval.setOnClickListener(new onClickInterval());

        tvMinuteRest.setOnClickListener(new onClickRest());
        tvSecondRest.setOnClickListener(new onClickRest());

        tvIntervals.setText(String.format(Locale.US, "%01d", numIntervals));


    }


    private class onClickInterval implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);

            int minutes = settings.getInt(getString(R.string.prefs_minutes_interval),0);
            int seconds = settings.getInt(getString(R.string.prefs_seconds_interval),0);


            TimePickerDialog timePickerDialog = new TimePickerDialog();

            timePickerDialog.setMinutes(minutes);
            timePickerDialog.setSeconds(seconds);

            timePickerDialog.setCallback(new TimePickerDialog.Listener() {
                @Override
                public void postitiveClick(int minutes, int seconds) {
                    tvMinuteInterval.setText(String.format(Locale.US, "%02d", minutes));
                    tvSecondInterval.setText(String.format(Locale.US, "%02d", seconds));

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(getString(R.string.prefs_minutes_interval),minutes);
                    editor.putInt(getString(R.string.prefs_seconds_interval),seconds);
                    editor.commit();
                }
            });
            timePickerDialog.show(getSupportFragmentManager(), TAG);
        }
    }
    private class onClickRest implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);
            int minutes = settings.getInt(getString(R.string.prefs_minutes_rest),0);
            int seconds = settings.getInt(getString(R.string.prefs_seconds_rest),0);


            TimePickerDialog timePickerDialog = new TimePickerDialog();

            timePickerDialog.setMinutes(minutes);
            timePickerDialog.setSeconds(seconds);

            timePickerDialog.setCallback(new TimePickerDialog.Listener() {
                @Override
                public void postitiveClick(int minutes, int seconds) {
                    tvMinuteRest.setText(String.format(Locale.US, "%02d", minutes));
                    tvSecondRest.setText(String.format(Locale.US, "%02d", seconds));

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(getString(R.string.prefs_minutes_rest),minutes);
                    editor.putInt(getString(R.string.prefs_seconds_rest),seconds);
                    editor.commit();
                }
            });
            timePickerDialog.show(getSupportFragmentManager(), TAG);
        }
    }
    public void subOneInterval(View view){
        numIntervals = Integer.parseInt(tvIntervals.getText().toString());

        if(numIntervals >0){
            numIntervals--;
        }
        tvIntervals.setText(String.format(Locale.US, "%d", numIntervals));

        this.saveNumIntervals();
    }

    public void addOneInterval(View view){
        numIntervals = Integer.parseInt(tvIntervals.getText().toString());
        numIntervals++;
        tvIntervals.setText(String.format(Locale.US, "%d", numIntervals));
        this.saveNumIntervals();
    }

    public void startRoutineOnClick(View view){
        this.saveNumIntervals();
        if(numIntervals == 0){
            Toast.makeText(this, "Please enter a number of intervals", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, IntervalActivity.class);
            startActivity(intent);
        }
    }

    private void saveNumIntervals(){
        final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(getString(R.string.prefs_num_intervals), numIntervals);
        editor.apply();
    }

    private AdsHelper adsHelper;
    private void startAds(){
        adsHelper =  new AdsHelper(getWindow().getDecorView(), getResources().getString(R.string.admob_id_main), this);

        adsHelper.setUpAds();
        int delay = 1000; // delay for 1 sec.
        int period = getResources().getInteger(R.integer.ad_refresh_rate);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                adsHelper.refreshAd();  // display the data
            }
        }, delay, period);
    }

}

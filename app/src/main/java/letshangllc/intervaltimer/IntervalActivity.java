package letshangllc.intervaltimer;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class IntervalActivity extends AppCompatActivity {

    /* Private textviews */
    private TextView tvMinute, tvSecond, tvIntervalRestLabel, tvIntervalCount;

    /* Rest times */
    private int restMinutes, restSeconds, restTime;

    /* Interval Time */
    private int intervalMinutes, intervalSeconds, intervalTime;

    /* Ringtone */
    private Ringtone r;

    /* IntervalCount and total*/
    private int currentInterval;
    private int totalIntervals;

    /* Countdown Timers */
    private CountDownTimer countDownTimerInterval;
    private CountDownTimer countDownTimerRest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);

        currentInterval = 1;

        this.findViews();
        this.getData();

        this.startTest();

        this.startAds();
    }

    private void findViews(){
        tvMinute = (TextView) findViewById(R.id.tv_minute);
        tvSecond = (TextView) findViewById(R.id.tv_second);
        tvIntervalCount = (TextView) findViewById(R.id.tvIntervalCount);
        tvIntervalRestLabel = (TextView) findViewById(R.id.tvIntervalRestLabel);
    }

    private void getData(){
        final SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_name), 0);
        intervalMinutes = settings.getInt(getString(R.string.prefs_minutes_interval),0);
        intervalSeconds = settings.getInt(getString(R.string.prefs_seconds_interval),0);

        intervalTime = (intervalMinutes*60+intervalSeconds)*1000;

        restMinutes = settings.getInt(getString(R.string.prefs_minutes_rest),0);
        restSeconds = settings.getInt(getString(R.string.prefs_seconds_rest),0);

        restTime = (restMinutes*60 + restSeconds) * 1000;

        totalIntervals = settings.getInt(getString(R.string.prefs_num_intervals),0);
        setIntervalTimes();
    }

    private void setIntervalTimes(){
        tvMinute.setText(String.format(Locale.US, "%02d", intervalMinutes));
        tvSecond.setText(String.format(Locale.US, "%02d", intervalSeconds));
        tvIntervalRestLabel.setText("Interval: ");
        tvIntervalCount.setText(String.format(Locale.US, "%1d", currentInterval));
    }
    private void setRestTimes(){
        tvMinute.setText(String.format(Locale.US, "%02d", restMinutes));
        tvSecond.setText(String.format(Locale.US, "%02d", restSeconds));
    }

    private void startTest(){
        if(currentInterval > totalIntervals || totalIntervals ==0){
            finish();
            return;
        }
        setIntervalTimes();
        startIntervalTimer();
    }

    private void startIntervalTimer(){
        countDownTimerInterval = new CountDownTimer(intervalTime, 100) {

            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000)  / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                tvMinute.setText(String.format(Locale.US, "%02d", minutes));
                tvSecond.setText(String.format(Locale.US, "%02d", seconds));
            }

            public void onFinish() {
                tvMinute.setText(String.format(Locale.US, "%02d", 0));
                tvSecond.setText(String.format(Locale.US, "%02d", 0));
                playAlarm();


                setRestTimes();
                startRestTimer();
                this.cancel();
            }
        }.start();
    }

    private void startRestTimer(){
        tvIntervalRestLabel.setText("Rest: ");
        countDownTimerRest = new CountDownTimer(restTime, 100) {

            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000)  / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                tvMinute.setText(String.format(Locale.US, "%02d", minutes));
                tvSecond.setText(String.format(Locale.US, "%02d", seconds));
            }

            public void onFinish() {
                playAlarm();
                currentInterval++;
                startTest();
                this.cancel();
            }
        }.start();
    }

    private void playAlarm(){
        /* Get the alarm tone */
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(alert == null){
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // If notication is null the use ringtone
            if(alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        try {
            /* Play the alarm */
            r = RingtoneManager.getRingtone(this, alert);
            r.setStreamType(AudioManager.STREAM_ALARM);
            r.play();
        }catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    r.stop();
                } catch (Exception e) {
                    r.stop();
                }

            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countDownTimerInterval != null){
            countDownTimerInterval.cancel();
        }
        if(countDownTimerRest != null){
            countDownTimerRest.cancel();
        }
    }

    private AdsHelper adsHelper;
    private void startAds(){
        adsHelper =  new AdsHelper(getWindow().getDecorView(), getResources().getString(R.string.admob_id_interval), this);

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

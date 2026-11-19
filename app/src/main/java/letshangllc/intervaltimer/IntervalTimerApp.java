package letshangllc.intervaltimer;

import android.app.Application;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Collections;

/**
 * Application entry point. Configures AdMob for Play Families / child-directed treatment
 * before any ad requests are made.
 */
public class IntervalTimerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Families policy / child-directed ads:
        // - Treat requests as child-directed (TFCD)
        // - Cap creatives at G (suitable for all audiences)
        // - Tag for under age of consent
        // Combined with AD_ID permission removal in the manifest so Advertising ID is not used.
        RequestConfiguration.Builder builder = new RequestConfiguration.Builder()
                .setTagForChildDirectedTreatment(
                        RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                .setTagForUnderAgeOfConsent(
                        RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE);

        if (BuildConfig.DEBUG) {
            builder.setTestDeviceIds(Collections.singletonList(AdRequest.DEVICE_ID_EMULATOR));
        }

        MobileAds.setRequestConfiguration(builder.build());
    }
}

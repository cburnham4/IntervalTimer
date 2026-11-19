# Google Play Data Safety Responses

**App:** Interval Timer (`letshangllc.intervaltimer`)  
**Version:** 3.0.0 (versionCode 100)  
**Last Updated:** November 2026  
**Target SDK:** 36

This document provides responses for the Google Play Console Data Safety section and Families / ads-related declarations.

---

## Overview

Interval Timer is a local workout interval timer. It does **not** create accounts, sync user workouts to a backend, or include analytics/crash SDKs. Ads are served via **Google AdMob** only (Amazon Mobile Ads removed). The app is configured for **Play Families / child-directed** ad treatment so it can be listed with children in the target audience without transmitting Advertising ID from non-certified SDKs.

---

## Data Collection Summary

| Data Type | Collected | Shared | Purpose |
|-----------|-----------|--------|---------|
| App interactions | Yes (AdMob) | Yes (AdMob) | Advertising |
| Device or other IDs (Advertising ID) | **No** (AD_ID permission removed; TFCD) | No | N/A — child-directed ads |
| Personal info (name, email, etc.) | No | No | — |
| Location | No | No | — |
| Crash logs / diagnostics | No | No | — |

Timer preferences (interval counts/times) are stored **only on-device** in `SharedPreferences` and are not collected by the developer or shared with third parties.

---

## Third-Party SDKs

### Google AdMob (`play-services-ads`)
- **Purpose:** Display banner ads
- **Configuration:**
  - `tagForChildDirectedTreatment = true`
  - `maxAdContentRating = G`
  - `tagForUnderAgeOfConsent = true`
  - `com.google.android.gms.permission.AD_ID` removed from the merged manifest
- **Privacy Policy:** https://policies.google.com/privacy
- **Families:** AdMob is a Google Play certified ads SDK for Families

### Google User Messaging Platform (UMP)
- **Purpose:** Consent / messaging where required
- **Note:** With under-age / child-directed tags, personalized-ad consent is not used for AAID-based personalization
- **Privacy Policy:** https://policies.google.com/privacy

### Removed
- **Amazon Mobile Ads** — removed entirely (was implicated in prior Families / Advertising ID policy removal)

---

## Permissions

| Permission | Why | Play declaration notes |
|------------|-----|------------------------|
| `INTERNET` | Load ads | Standard |
| `ACCESS_NETWORK_STATE` | AdMob connectivity checks | Standard |
| `ACCESS_WIFI_STATE` | Legacy / AdMob network info | Standard |
| `AD_ID` | **Not used** — explicitly removed | Declare that the app does **not** use Advertising ID when answering Families / Data safety |

No `POST_NOTIFICATIONS`, precise location, or microphone permissions are requested.

---

## Play Console Form Responses

### Does your app collect or share any of the required user data types?
**Yes** (AdMob may process app activity / device info for serving ads; Advertising ID is **not** used under the Families-safe configuration)

### Is all of the user data collected by your app encrypted in transit?
**Yes** (HTTPS for AdMob / UMP)

### Do you provide a way for users to request that their data is deleted?
**Yes** — on-device preferences can be cleared by clearing app storage / uninstalling. No cloud account data is stored by the developer.

### Advertising ID
- **Collected / shared:** No (permission removed; child-directed treatment)
- Align Data safety answers with “Advertising ID not collected” when using this build’s Families-safe ads setup

### Data types that may still apply via AdMob (non-AAID)
- **App interactions** — shared with Google for advertising (subject to Families / child-directed rules)
- Confirm against the current [AdMob / Google Play Data safety guidance](https://support.google.com/admob/) when filing

---

## Families policy & target audience (Play Console)

This build is intended to be **safe to list with children in the target audience**:

1. In **Target audience and content**, select the age groups that match real users.
2. If **any** age group under 13 (or mixed audience including children) is selected:
   - Complete the **Families policy** questionnaire.
   - Use only certified ad SDKs (this app: AdMob only).
   - Keep the in-app Families-safe ad configuration (already in code).
3. If the app is **adults only (18+)**:
   - Select 18+ only; Families requirements for child-directed ads may not apply.
   - You may later relax TFCD / AD_ID removal for higher ad fill — **do not** do that while children remain in the audience.

**Recommendation given the prior Play removal for Families / Advertising ID:** treat the listing as including children (or mixed), keep the Families-safe ad settings in this PR, and choose target age groups accordingly rather than 18+ only unless you intentionally re-position the app for adults.

---

## Contact

For privacy inquiries, contact the developer via the email address listed on the Google Play Store listing.

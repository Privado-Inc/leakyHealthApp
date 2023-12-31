package io.privado.privadohealthapp.viewmodels;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.amplitude.android.Amplitude;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.privado.privadohealthapp.models.PersonalyIndentifiableInformation;
import io.privado.privadohealthapp.utils.HashUtils;
import io.privado.privadohealthapp.utils.PIIUtils;

public class SignupViewModel extends ViewModel {

    public PersonalyIndentifiableInformation getPii(Context context){

        String wlanMac = PIIUtils.getMACAddress("wlan0");
        String eth0Mac = PIIUtils.getMACAddress("eth0");
        String ipv4 = PIIUtils.getIPAddress(true); // IPv4
        String ipv6 = PIIUtils.getIPAddress(false); // IPv6
        String imei = PIIUtils.getDeviceIMEI(context);
        String adId = PIIUtils.getAdId(context);

        return new PersonalyIndentifiableInformation(wlanMac, eth0Mac, ipv4, ipv6, imei, adId);
    }
    public void logPIIFacebook(Context context, String email, PersonalyIndentifiableInformation pii){

        Bundle parameters = new Bundle();
        parameters.putString("device_id", pii.getImei());
        parameters.putString("wlan_mac", pii.getWlanMac());
        parameters.putString("eth0_mac", pii.getEthernetMac());
        parameters.putString("ipv4", pii.getIpAddressv4());
        parameters.putString("ipv6", pii.getIpAddressv6());
        parameters.putString("advertising_id", pii.getAdId());
        parameters.putString("user_email", HashUtils.getHash(email));
        parameters.putString("is_signed_up", "yes");

        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logEvent("signup", parameters);
    }
    public void logPIIGoogleAnalyitcs(String email, PersonalyIndentifiableInformation pii){

        Bundle parameters = new Bundle();
        parameters.putString("device_id", pii.getImei());
        parameters.putString("wlan_mac", pii.getWlanMac());
        parameters.putString("eth0_mac", pii.getEthernetMac());
        parameters.putString("ipv4", pii.getIpAddressv4());
        parameters.putString("ipv6", pii.getIpAddressv6());
        parameters.putString("advertising_id", pii.getAdId());
        parameters.putString("user_email", HashUtils.getHash(email));
        parameters.putString("is_signed_up", "yes");

        FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("signup", parameters);
    }
}

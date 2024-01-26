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
import io.privado.privadohealthapp.models.PharmacyInformation
import io.privado.privadohealthapp.utils.HashUtils;
import io.privado.privadohealthapp.utils.PIIUtils;

public class LoginViewModel extends ViewModel {

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

        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logEvent("health_data", parameters);
        logger.logEvent("login", parameters);
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

        FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("login", parameters);
    }
    public void logPIIAmplitude(Amplitude amplitude, String email, PersonalyIndentifiableInformation pii){

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("user_email", email);
        hashMap.put("device_id", pii.getImei());
        hashMap.put("wlan_mac", pii.getWlanMac());
        hashMap.put("eth0_mac", pii.getEthernetMac());
        hashMap.put("ipv4", pii.getIpAddressv4());
        hashMap.put("ipv6", pii.getIpAddressv6());
        hashMap.put("advertising_id", pii.getAdId());

        amplitude.track("health_data", hashMap);
    }
    public void logPIIMixpanel(MixpanelAPI mixpanel, String email, PersonalyIndentifiableInformation pii, pharmacyInformation phi){

        JSONObject props = new JSONObject();
        try {
            props.put("user_email", email);
            props.put("device_id", pii.getImei());
            props.put("wlan_mac", pii.getWlanMac());
            props.put("eth0_mac", pii.getEthernetMac());
            props.put("ipv4", pii.getIpAddressv4());
            props.put("ipv6", pii.getIpAddressv6());
            props.put("advertising_id", pii.getAdId());
            props.put("drug", phi.getDrug());
            props.put("form", phi.getForm());
            props.put("pharmacy_location", phi.getPharmacyLocation());
            props.put("purchase_coupon_taken", phi.getIsCouponTaken());
            mixpanel.track("health_data", props);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

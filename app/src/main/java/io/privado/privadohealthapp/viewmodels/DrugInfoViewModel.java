package io.privado.privadohealthapp.viewmodels;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import io.privado.privadohealthapp.models.DrugInformation;
import io.privado.privadohealthapp.models.PersonalInformation;
import io.privado.privadohealthapp.models.PersonalyIndentifiableInformation;
import io.privado.privadohealthapp.utils.PIIUtils;

public class DrugInfoViewModel extends ViewModel {

    public PersonalyIndentifiableInformation getPii(Context context){

        String wlanMac = PIIUtils.getMACAddress("wlan0");
        String eth0Mac = PIIUtils.getMACAddress("eth0");
        String ipv4 = PIIUtils.getIPAddress(true); // IPv4
        String ipv6 = PIIUtils.getIPAddress(false); // IPv6
        String imei = PIIUtils.getDeviceIMEI(context);
        String adId = PIIUtils.getAdId(context);

        return new PersonalyIndentifiableInformation(wlanMac, eth0Mac, ipv4, ipv6, imei, adId);
    }
    public void logPIIFacebook(Context context, PersonalyIndentifiableInformation pii, DrugInformation drugInformation) {

        Bundle parameters = new Bundle();
        parameters.putString("device_id", pii.getImei());
        parameters.putString("wlan_mac", pii.getWlanMac());
        parameters.putString("eth0_mac", pii.getEthernetMac());
        parameters.putString("ipv4", pii.getIpAddressv4());
        parameters.putString("ipv6", pii.getIpAddressv6());
        parameters.putString("advertising_id", pii.getAdId());
        parameters.putString("drug_name", drugInformation.getDrugName());
        parameters.putString("drug_category", drugInformation.getDrugCategory());
        parameters.putString("drug_quantity", drugInformation.getDrugQuantity());
        parameters.putString("purchase_coupon", drugInformation.getPurchaseCoupon());
        parameters.putString("pharmacy_id", drugInformation.getPharmacyId());
        parameters.putString("health_condition", drugInformation.getHealthCondition());

        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logEvent("drug_info", parameters);
    }
    public void logPIIGoogleAnalyitcs(PersonalyIndentifiableInformation pii, DrugInformation drugInformation) {

        Bundle parameters = new Bundle();
        parameters.putString("device_id", pii.getImei());
        parameters.putString("wlan_mac", pii.getWlanMac());
        parameters.putString("eth0_mac", pii.getEthernetMac());
        parameters.putString("ipv4", pii.getIpAddressv4());
        parameters.putString("ipv6", pii.getIpAddressv6());
        parameters.putString("advertising_id", pii.getAdId());
        parameters.putString("drug_category", drugInformation.getDrugCategory());
        parameters.putString("drug_quantity", drugInformation.getDrugQuantity());
        parameters.putString("purchase_coupon", drugInformation.getPurchaseCoupon());
        parameters.putString("pharmacy_id", drugInformation.getPharmacyId());
        parameters.putString("health_condition", drugInformation.getHealthCondition());

        FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("drug_info", parameters);
//        MixpanelAPI.track("drug_info", parameters);

    }
}

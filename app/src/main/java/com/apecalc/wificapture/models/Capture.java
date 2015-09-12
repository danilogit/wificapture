package com.apecalc.wificapture.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "captures")
public class Capture {

    // String field separator
    public static final String SFS = ",";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String BSSID;
    @DatabaseField
    private String SSID;
    @DatabaseField
    private String lat;
    @DatabaseField
    private String lon;
    @DatabaseField
    private long timestamp;
    @DatabaseField
    private boolean wifi_protected = false;
    @DatabaseField
    private String wifi_security_type;
    @DatabaseField
    private int frequency;
    @DatabaseField
    private int level;
    @DatabaseField
    private String friendly_name;
    @DatabaseField
    private String venue_name;
    @DatabaseField
    private boolean sent = false;
    @DatabaseField
    private float accurancy;
    @DatabaseField
    private String provider;


    public Capture(){}

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isWifiProtected() {
        return wifi_protected;
    }

    public void setWifiProtected(boolean wifi_protected) {
        this.wifi_protected = wifi_protected;
    }


    public void setWifiSecurityType(String wifi_security_type) {
        this.wifi_security_type = wifi_security_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public String getFriendlyName() {
        return friendly_name;
    }

    public void setFriendlyName(String friendly_name) {
        this.friendly_name = friendly_name;
    }

    public String getVenueName() {
        return venue_name;
    }

    public void setVenueName(String venue_name) {
        this.venue_name = venue_name;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getWifiSecurityType() {
        return wifi_security_type;
    }

    public float getAccurancy() {
        return accurancy;
    }

    public void setAccurancy(float accurancy) {
        this.accurancy = accurancy;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString(){
        return getId() + SFS +  getBSSID() + SFS + getSSID() +
                SFS + getLat() + SFS + getLon() + SFS +
                getTimestamp() + SFS + isWifiProtected() + SFS + getWifiSecurityType() + SFS +
                getFrequency() + SFS + getLevel() + SFS + getFriendlyName() + SFS +
                getVenueName() + SFS + isSent() + SFS + getAccurancy() + SFS + getProvider();

    }
}

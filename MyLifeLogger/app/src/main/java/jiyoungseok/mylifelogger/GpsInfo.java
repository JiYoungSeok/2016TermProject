package jiyoungseok.mylifelogger;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GpsInfo implements LocationListener{
    static Double latitude ;
    static Double longitude ;

    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}

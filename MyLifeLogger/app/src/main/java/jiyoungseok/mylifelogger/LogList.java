package jiyoungseok.mylifelogger;

public class LogList {
    private int date, time;
    private Double latitude, longitude;
    private String event, memo;

    public void setDate(int date) { this.date = date; }
    public void setTime(int time) { this.time = time; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setEvent(String event) { this.event = event; }
    public void setMemo(String memo) { this.memo = memo; }

    public int getDate() { return date; }
    public int getTime() { return time; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getEvent() { return event; }
    public String getMemo() { return memo; }
}

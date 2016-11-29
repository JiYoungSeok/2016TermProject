package jiyoungseok.mylifelogger;

public class LogList {
    private int todayDate, currentTime;
    private Double latitude, longitude;
    private String event, memo;

    public void setTodayDate(int todayDate) { this.todayDate = todayDate; }
    public void setCurrentTime(int currentTime) { this.currentTime = currentTime; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setEvent(String event) { this.event = event; }
    public void setMemo(String memo) { this.memo = memo; }

    public int getTodayDate() { return todayDate; }
    public int getCurrentTime() { return currentTime; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getEvent() { return event; }
    public String getMemo() { return memo; }
}

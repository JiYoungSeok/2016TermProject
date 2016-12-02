package jiyoungseok.mylifelogger;

public class GoalList {
    private int startDate, endDate, time;
    private String category, upOrDown;

    public void setStartDate(int startDate) { this.startDate = startDate; }
    public void setEndDate(int endDate) { this.endDate = endDate; }
    public void setTime(int time) { this.time = time; }
    public void setCategory(String category) { this.category = category; }
    public void setUpOrDown(String upOrDown) { this.upOrDown = upOrDown; }

    public int getStartDate() { return startDate; }
    public int getEndDate() { return endDate; }
    public int getTime() { return time; }
    public String getCategory() { return category; }
    public String getUpOrDown() { return upOrDown; }
}

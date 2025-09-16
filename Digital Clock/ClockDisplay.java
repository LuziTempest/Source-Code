import java.util.Calendar;
import java.text.DecimalFormat;

public class ClockDisplay {
    private NumberDisplay hours, minutes, seconds, day, month, year;
    private double temperature;
    private DecimalFormat tempFormat;
    private String displayString;

    public ClockDisplay() {
        Calendar cal = Calendar.getInstance();
        
        hours = new NumberDisplay(24);
        hours.setValue(cal.get(Calendar.HOUR_OF_DAY));
        minutes = new NumberDisplay(60);
        minutes.setValue(cal.get(Calendar.MINUTE));
        seconds = new NumberDisplay(60);
        seconds.setValue(cal.get(Calendar.SECOND));
        
        month = new NumberDisplay(13);
        month.setValue(cal.get(Calendar.MONTH) + 1);
        if(month.getValue() == 0) month.setValue(1);

        year = new NumberDisplay(2101);
        year.setValue(cal.get(Calendar.YEAR));
        if(year.getValue() == 0) year.setValue(2024);

        day = new NumberDisplay(getDaysInMonth(year.getValue(), month.getValue()) + 1);
        day.setValue(cal.get(Calendar.DAY_OF_MONTH));
        if(day.getValue() == 0) day.setValue(1);
        
        temperature = 29.5;
        tempFormat = new DecimalFormat("#0.0");

        updateDisplay();
    }

    public void timeTick() {
        seconds.increment();
        if (seconds.getValue() == 0) {
            minutes.increment();
            if (minutes.getValue() == 0) {
                hours.increment();
                if (hours.getValue() == 0) {
                    day.increment();
                    if (day.getValue() == 1) {
                        month.increment();
                        if (month.getValue() == 1) {
                            year.increment();
                        }
                        day.setLimit(getDaysInMonth(year.getValue(), month.getValue()) + 1);
                    }
                }
            }
        }
        updateTemperature();
        updateDisplay();
    }

    public String getDisplayString() {
        return displayString;
    }
    
    private void updateDisplay() {
        String datePart = day.getDisplayValue() + "/" + month.getDisplayValue() + "/" + year.getValue();
        String timePart = hours.getDisplayValue() + ":" + minutes.getDisplayValue() + ":" + seconds.getDisplayValue();
        String tempPart = tempFormat.format(temperature) + " °C";

        displayString = "<html><center>" + 
                        timePart + "<br>" + 
                        datePart + "<br>" + 
                        tempPart + 
                        "</center></html>";
    }
    
    private void updateTemperature() {
        double change = (Math.random() * 0.2) - 0.1;
        this.temperature += change;
        if (this.temperature < 26.0) this.temperature = 26.0;
        if (this.temperature > 34.0) this.temperature = 34.0;
    }
    
    private int getDaysInMonth(int year, int month) {
        if (month == 2) {
            boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            return isLeap ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }
    
    // Pastikan 3 metode ini ada di dalam kelas ClockDisplay.java

    public String getTimeString() {
        return hours.getDisplayValue() + ":" + minutes.getDisplayValue() + ":" + seconds.getDisplayValue();
    }
    
    public String getDateString() {
        return day.getDisplayValue() + "/" + month.getDisplayValue() + "/" + year.getValue();
    }
    
    public String getTemperatureString() {
        return tempFormat.format(temperature) + " °C";
    }
}
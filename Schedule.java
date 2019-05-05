package interview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Schedule {
    private List<Date> byList = new ArrayList<Date>();

    private boolean everyDay;

    private boolean everyHour;

    private List<String> everyList = new ArrayList<String>();

    private String scheduleString;

    public Schedule(String scheduleString) {
        this.scheduleString = scheduleString;
        this.everyDay = this.scheduleString.toLowerCase().contains("every day") || this.scheduleString.toLowerCase().contains("sun-sat");
        this.everyHour = this.scheduleString.toLowerCase().contains("every hour");
    }

    public boolean hasBy() {
        return scheduleString.trim().toLowerCase().startsWith("by ");
    }

    public List<Date> getByList() {
        byList.clear();
        if(this.scheduleString.toLowerCase().contains("by")) {
            String time = this.scheduleString.substring(this.scheduleString.toLowerCase().indexOf("by") + 2, this.scheduleString.toLowerCase().lastIndexOf("est") + 3);
            String [] timeArray = time.split(",");
            for(int i = 0; i < timeArray.length; i++) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a z");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
                    byList.add(dateFormat.parse(timeArray[i].trim()));
                } catch (ParseException ex) {
                    Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return this.byList;
    }

    public boolean isEveryDay() {
        return this.everyDay;
    }

    public boolean isEveryHour() {
        return this.everyHour;
    }

    public List<String> getEveryList() {
        everyList.clear();
        String []allWeekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String allDays = "1st,2nd,3rd,4th,5th,6th,7th,8th,9th,10th,11th,12th,13th,14th,15th,16th,17th,18th,19th,20th,21st,22nd,23rd,24th,25th,26th,27th,28th,29th,30th,31st";
        String []allDaysArray = allDays.split(",");
        if(!this.everyDay) {
            if(StringUtils.indexOfAny(this.scheduleString, allWeekdays) > -1 || 
               this.scheduleString.substring(this.scheduleString.indexOf("every") + 6).contains("hour")|| 
               this.scheduleString.substring(this.scheduleString.indexOf("every") + 6).contains("minute")|| 
               this.scheduleString.substring(this.scheduleString.indexOf("every") + 6).contains("seconds")) {
                String daysOrHours = this.scheduleString.substring(this.scheduleString.indexOf("every") + 6);
                if(daysOrHours.contains("-")) {
                   String start = daysOrHours.split("-")[0].trim();
                   String end = daysOrHours.split("-")[1].trim();
                   int i = 0;
                   while(!allWeekdays[i].toLowerCase().equals(start.toLowerCase()) && i < 7) {
                       i++;
                   }
                   while(!allWeekdays[i].toLowerCase().equals(end.toLowerCase()) && i < 7) {
                       everyList.add(allWeekdays[i++]);
                   } 
                   everyList.add(end);
                } else if (daysOrHours.replace(" ", "").contains(",")) {
                    everyList =  Arrays.asList(daysOrHours.split(", "));
                } else if(!this.everyHour) {
                    everyList.add(daysOrHours);
                }
            }
            if(StringUtils.indexOfAny(this.scheduleString, allDaysArray) > -1) {
                String numberedDate = this.scheduleString.substring(this.scheduleString.indexOf("every") + 6);
                if(numberedDate.contains("-")) {           
                   String start = numberedDate.split("-")[0].trim();
                   String end = numberedDate.split("-")[1].trim();
                   int i = 0;
                   while(!allDaysArray[i].toLowerCase().equals(start.toLowerCase())) {
                       i = (i + 1) % allDaysArray.length;
                   }
                   while(!allDaysArray[i].toLowerCase().equals(end.toLowerCase())) {
                       everyList.add(allDaysArray[i]);
                       i = (i + 1) % allDaysArray.length;
                   } 
                   everyList.add(end);
                } else if (numberedDate.replace(" ", "").contains(",")) {
                    everyList =  Arrays.asList(numberedDate.split(", "));
                } else {
                    everyList.add(numberedDate.trim());
                }
            }
        }
          
        return everyList;
    }
}

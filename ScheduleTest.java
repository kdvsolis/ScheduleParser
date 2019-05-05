package interview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

public class ScheduleTest {
    @Test
    public void testEveryDay() {
        Schedule schedule = new Schedule("every day");
        assertSchedule(schedule, false, true, false, null, null);
    }

    @Test
    public void testEveryMonToFri() {
        Schedule schedule = new Schedule("every Mon-Fri");
        assertSchedule(schedule, false, false, false, null, "Mon,Tue,Wed,Thu,Fri");
    }

    @Test
    public void testEveryMonWedFri() {
        Schedule schedule = new Schedule("every Mon, Wed, Fri");
        assertSchedule(schedule, false, false, false, null, "Mon,Wed,Fri");
    }

    @Test
    public void testEveryHour() {
        Schedule schedule = new Schedule("every hour");
        assertSchedule(schedule, false, false, true, null, null);
    }

    @Test
    public void testEvery6Hours() {
        Schedule schedule = new Schedule("every 6 hours");
        assertSchedule(schedule, false, false, false, null, "6 hours");
    }

    @Test
    public void testEvery30Minutes() {
        Schedule schedule = new Schedule("every 30 minutes");
        assertSchedule(schedule, false, false, false, null, "30 minutes");
    }

    @Test
    public void testBy1AmEveryDay() {
        Schedule schedule = new Schedule("by 1:00 AM EST every day");
        assertSchedule(schedule, true, true, false, "1:00 AM EST", null);

    }

    @Test
    public void testBy1AmEveryFri() {
        Schedule schedule = new Schedule("by 1:00 AM EST every Fri");
        assertSchedule(schedule, true, false, false, "1:00 AM EST", "Fri");
    }

    @Test
    public void testBy1AmEveryMonToFri() {
        Schedule schedule = new Schedule("by 1:00 AM EST every Mon-Fri");
        assertSchedule(schedule, true, false, false, "1:00 AM EST", "Mon,Tue,Wed,Thu,Fri");
    }

    @Test
    public void testBy1AmEveryMonWedFri() {
        Schedule schedule = new Schedule("by 1:00 AM EST every Mon, Wed, Fri");
        assertSchedule(schedule, true, false, false, "1:00 AM EST", "Mon,Wed,Fri");
    }

    @Test
    public void testBy1AmEvery1st() {
        Schedule schedule = new Schedule("by 1:00 AM EST every 1st");
        assertSchedule(schedule, true, false, false, "1:00 AM EST", "1st");
    }

    @Test
    public void testBy1AmEvery15th30th() {
        Schedule schedule = new Schedule("by 1:00 AM EST every 15th, 30th");
        assertSchedule(schedule, true, false, false, "1:00 AM EST", "15th,30th");
    }

    @Test
    public void testBy1AmEvery10thTo12th() {
        Schedule schedule = new Schedule("by 1:00 AM EST every 10th-12th");
        assertSchedule(schedule, true, false, false, "1:00 AM EST", "10th,11th,12th");
    }

    @Test
    public void testBy1AmEvery31stTo3rd() {
        Schedule schedule = new Schedule("by 1:00 AM EST every 31st-3rd");
        assertSchedule(schedule, true, false, false, "1:00 AM EST", "31st,1st,2nd,3rd");
    }

    @Test
    public void testBy1AmEvery2ndTo22nd() {
        Schedule schedule = new Schedule("by 1:00 AM EST every 2nd-22nd");
        assertSchedule(schedule, true, false, false, "1:00 AM EST",
                "2nd,3rd,4th,5th,6th,7th,8th,9th,10th,11th,12th,13th,14th,15th,16th,17th,18th,19th,20th,21st,22nd");
    }

    @Test
    public void testBy9Am5PmEverySunToFri() {
        Schedule schedule = new Schedule("by 9:00 AM EST, 5:00 PM EST every Sun-Fri");
        assertSchedule(schedule, true, false, false, "9:00 AM EST,5:00 PM EST", "Sun,Mon,Tue,Wed,Thu,Fri");
    }

    @Test
    public void testBy9Am5PmEveryMonToFri() {
        Schedule schedule = new Schedule("by 9:00 AM EST, 5:00 PM EST every Mon-Fri");
        assertSchedule(schedule, true, false, false, "9:00 AM EST,5:00 PM EST", "Mon,Tue,Wed,Thu,Fri");
    }

    private void assertSchedule(Schedule schedule, boolean hasBy, boolean isEveryDay, boolean isEveryHour,
            String expectedByString, String expectedEveryString) {
        assertEquals(hasBy, schedule.hasBy());
        assertEquals(isEveryDay, schedule.isEveryDay());
        assertEquals(isEveryHour, schedule.isEveryHour());
        assertDateList(schedule.getByList(), expectedByString);
        assertList(schedule.getEveryList(), expectedEveryString);
    }

    private void assertList(List<String> list, String string) {
        String[] split = new String[0];
        if (string != null) {
            split = string.split(",");
        }

        String[] everyArray = list.toArray(new String[0]);
        assertTrue(ArrayUtils.isEquals(split, everyArray));
    }

    private void assertDateList(List<Date> dates, String string) {
        List<String> list = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        for (Date date : dates) {
            list.add(dateFormat.format(date));
        }

        assertList(list, string);
    }
}

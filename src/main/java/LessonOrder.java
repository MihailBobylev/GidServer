import java.util.Calendar;
import java.util.Date;

public class LessonOrder {
    private int week;
    private int day;
    private int order;

    private int[][] time = {{380,599},{600,699},{700,799},{800,929},{930,1029},{1030,1129}, {1130,1230}};

    public LessonOrder() {
        int minute, cDay, even;

        Date date = new Date();

        //текущее вермя
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(date);

        //1 сентября
        Calendar oldCalendar = Calendar.getInstance();
        if(nowCalendar.get(Calendar.MONTH)>0 && nowCalendar.get(Calendar.MONTH)<6)
            oldCalendar.set(nowCalendar.get(Calendar.YEAR)-1, Calendar.SEPTEMBER, 1);
        else oldCalendar.set(nowCalendar.get(Calendar.YEAR), Calendar.SEPTEMBER, 1);

        //31 декабря
        Calendar calendar = Calendar.getInstance();
        if(nowCalendar.get(Calendar.MONTH)>0 && nowCalendar.get(Calendar.MONTH)<6)
            calendar.set(nowCalendar.get(Calendar.YEAR)-1,Calendar.DECEMBER,31);
        else calendar.set(nowCalendar.get(Calendar.YEAR),Calendar.DECEMBER,31);

        if(oldCalendar.get(Calendar.DAY_OF_WEEK)==1)
            week=2;
        else week = 1;

        if(nowCalendar.get(Calendar.DAY_OF_YEAR)-oldCalendar.get(Calendar.DAY_OF_YEAR)>0)
            cDay = nowCalendar.get(Calendar.DAY_OF_YEAR) - oldCalendar.get(Calendar.DAY_OF_YEAR);
        else cDay = calendar.get(Calendar.DAY_OF_YEAR) - oldCalendar.get(Calendar.DAY_OF_YEAR) + nowCalendar.get(Calendar.DAY_OF_YEAR);

        even = cDay/7;

        if(even%2==0)
            if (week == 2)
                week = 1;
            else week = 2;



        if(nowCalendar.get(Calendar.DAY_OF_WEEK)==1){
            day = 7;
        }
        else if(nowCalendar.get(Calendar.DAY_OF_WEEK)==7){
            day = 6;
        }
        else day = nowCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        minute  = nowCalendar.get(Calendar.HOUR_OF_DAY)*60+nowCalendar.get(Calendar.MINUTE);

        for (int i = 0; i < time.length; i++){
            if(minute>time[i][0] && minute<time[i][1]) {
                order = i + 1;
                break;
            }
        }
    }

    public int getWeek() {
        return week;
    }

    public int getDayWeek() {
        return day;
    }

    public int getOrder() {
        return order;
    }
}

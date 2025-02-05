package Utils;

import java.util.HashMap;
import java.util.Map;
//класс для хранения и обработки праздничных дней
public class WorkCalendar {
private final Map<String,int[]> specialCalendar = new HashMap<>();
private final int defaultWorkStart = 9;
private final int defaultWorkEnd = 17;

public WorkCalendar(){
    specialCalendar.put("04.05.2025", new int[]{12,16});
    specialCalendar.put("10.05.2025", new int[]{11,17});
}
public int getStartWorkHours(String date){
    if(specialCalendar.containsKey(date)){
        return specialCalendar.get(date)[0];
    }else {
        return defaultWorkStart;
    }
}
public int getEndWorkHours(String date){
        if(specialCalendar.containsKey(date)){
            return specialCalendar.get(date)[1];
        }else {
            return defaultWorkEnd;
        }
    }
}

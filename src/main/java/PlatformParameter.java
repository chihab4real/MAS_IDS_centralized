import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlatformParameter {
    public static Runtime runtime = Runtime.instance();
    public static Profile profile = new ProfileImpl();

    public  static ContainerController containerController;

    public static ArrayList<Boolean> alive= new ArrayList<>();
    public static String startTime;
    public static String startTime2;

    public static String methode(){

        LocalDateTime localDateTime = LocalDateTime.now();
        if(String.valueOf(localDateTime.getMinute()).length()==1){
            startTime2=localDateTime.getHour()+":0"+localDateTime.getMinute()+":"+localDateTime.getSecond();
            return localDateTime.getYear()+"_"+localDateTime.getMonthValue()+"_"+localDateTime.getDayOfMonth()+
                    "_"+localDateTime.getHour()+"_"+localDateTime.getMinute()+"_"+localDateTime.getSecond();
        }

        startTime2=localDateTime.getHour()+":"+localDateTime.getMinute()+":"+localDateTime.getSecond();
        return localDateTime.getYear()+"_"+localDateTime.getMonthValue()+"_"+localDateTime.getDayOfMonth()+
                "_"+localDateTime.getHour()+"_"+localDateTime.getMinute()+"_"+localDateTime.getSecond();
    }
}

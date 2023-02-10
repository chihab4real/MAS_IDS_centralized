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

    public static String startTime;



}

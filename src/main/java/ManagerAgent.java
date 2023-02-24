import jade.core.Agent;
import weka.core.Instances;

import java.util.ArrayList;

public class ManagerAgent extends Agent {

    public static ArrayList<PacketSniffer> packetsDetected=new ArrayList<>();

    public static ArrayList<PacketDetected> packetsClassified=new ArrayList<>();

    public static ArrayList<PacketSniffer> allPackets=new ArrayList<>();

    public static int numberOfContainers =1;
    public static ArrayList<Container> containers = new ArrayList<>();


    public static ArrayList<Attack> attacks=new ArrayList<>();
    public static int treating_time =1000;

    public static Clasificator DT,SVM,NN;

    public static boolean stop = false;
    public static Instances test = null;
    @Override
    protected void setup() {

        addBehaviour(new BehTrain());

        addBehaviour(new CreateContainersBehav());

        addBehaviour(new BehClassif());
    }
}

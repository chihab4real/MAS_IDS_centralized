import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.ArrayList;
import java.util.Random;

public class ManagerAgent extends Agent {

    public static ArrayList<PacketSniffer> packetsDetected=new ArrayList<>();
    public static ArrayList<PacketDetected> packetsClassified=new ArrayList<>();
    public static ArrayList<PacketSniffer> all=new ArrayList<>();
    public static int numberOfContainers =3;
    public static ArrayList<Container> containers = new ArrayList<>();

    public static ArrayList<Attack> attacks=new ArrayList<>();
    public static int number=1000;

    public static Clsi DT,SVM,NN;

    public static boolean stop = false;
    public static Instances test = null;
    @Override
    protected void setup() {

        addBehaviour(new BehTrain());

        addBehaviour(new CreateContainersBehav());

        addBehaviour(new BehClassif());
    }
}

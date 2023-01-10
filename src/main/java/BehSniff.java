import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.*;

import java.util.Random;


public class BehSniff extends TickerBehaviour {
    public BehSniff(Agent a) {
        super(a, ManagerAgent.number);
    }

    @Override
    protected void onTick() {

        System.out.println("Tick");

        Instances test=null;

        try {
            test = new DataSource("KDDTest.arff").getDataSet();
            test.setClassIndex(test.numAttributes()-1);


        }catch (Exception e){
            e.printStackTrace();
        }

        PacketSniffer packetSniffer = new PacketSniffer(test.get(new Random().nextInt(test.size()-1)),false);

        ManagerAgent.packetsDetected.add(packetSniffer);
        ManagerAgent.all.add(packetSniffer);


        //System.out.println("Random was: "+ManagerAgent.number+"\n"+packetSniffer.getInstance());
        ManagerAgent.number=new Random().nextInt(1000);

        //ManagerAgent.number=4000;
        reset(ManagerAgent.number);


    }
}

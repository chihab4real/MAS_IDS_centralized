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

        String myId = String.valueOf(this.getAgent().getAID().getLocalName().replace("SnifferAgent_Container",""));


        Instances test=null;

        try {
            test = new DataSource("KDDTest.arff").getDataSet();
            test.setClassIndex(test.numAttributes()-1);


        }catch (Exception e){
            e.printStackTrace();
        }

        PacketSniffer packetSniffer = new PacketSniffer(test.get(new Random().nextInt(test.size()-1)),false,myId);

        ManagerAgent.packetsDetected.add(packetSniffer);
        ManagerAgent.all.add(packetSniffer);


        ManagerAgent.containers.get(Integer.parseInt(myId)-1).getPacketsDetected().add(packetSniffer);
        ManagerAgent.containers.get(Integer.parseInt(myId)-1).getAll().add(packetSniffer);





    }
}

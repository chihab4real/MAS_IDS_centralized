import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.Random;


public class BehSniff extends TickerBehaviour {
    public BehSniff(Agent a) {
        super(a, ManagerAgent.treating_time);
    }

    @Override
    protected void onTick() {

        if(!ManagerAgent.stop){
            String myId = String.valueOf(this.getAgent().getAID().getLocalName().replace("SnifferAgent_Container",""));


            //Instances test=null;

            try {



            }catch (Exception e){
                e.printStackTrace();
            }

            PacketSniffer packetSniffer = new PacketSniffer(ManagerAgent.test.get(new Random().nextInt(ManagerAgent.test.size()-1)),false,myId);

            ManagerAgent.packetsDetected.add(packetSniffer);

            ManagerAgent.allPackets.add(packetSniffer);


            ManagerAgent.containers.get(Integer.parseInt(myId)-1).getPacketsDetected().add(packetSniffer);
            ManagerAgent.containers.get(Integer.parseInt(myId)-1).getAll().add(packetSniffer);

            int rand = new Random().nextInt((900 - 100) + 1) + 100;
            reset(rand);
        }






    }
}

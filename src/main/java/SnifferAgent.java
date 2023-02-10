import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class SnifferAgent extends Agent {



    @Override
    protected void setup() {







        Instances TestData = null;
        try {
            TestData = new ConverterUtils.DataSource("KDDTest.arff").getDataSet();

        } catch (Exception e) {
            e.printStackTrace();
        }


        String containerID = getMyID(getAID().getLocalName());





        addBehaviour(new BehSniff(this));



    }

    public String getMyID(String AID){
        return AID.replace("SnifferAgent_Container","");
    }








}



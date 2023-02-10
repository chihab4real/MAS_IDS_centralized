import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.SimpleBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class CreateContainersBehav extends SimpleBehaviour {
    Boolean condition = false;
    int n=1;

    @Override
    public void action() {



        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "Container"+n);
        profile.setParameter(Profile.MAIN_HOST, "localhost");



        ContainerController containerController =  runtime.createAgentContainer(profile);
        AgentController agentController = null;
        try {
            ManagerAgent.containers.add(new Container());
            agentController = containerController.createNewAgent("SnifferAgent_Container"+n,"SnifferAgent",null);
            agentController.start();


        } catch (StaleProxyException e) {
            e.printStackTrace();
        }


        n++;

        if(n==ManagerAgent.numberOfContainers+1){

            condition=true;
        }


    }

    @Override
    public boolean done() {
        return condition;
    }
}

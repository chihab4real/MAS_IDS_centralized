import jade.core.Profile;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    static int index1=0;
    public static void main(String[] args) {

        int time_ex=60;

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(index1==time_ex){
                    try {
                        PlatformPara.containerController.kill();
                    } catch (StaleProxyException e) {
                        e.printStackTrace();
                    }

                    System.out.println("\n\n\nArchitecture Centraliee\nTemps d'exuction: "+time_ex+" seconcds");
                    System.out.println("Packets Detecters: "+ManagerAgent.all.size());
                    System.out.println("Packets Classifiers: "+ManagerAgent.packetsClassified.size());
                    ArrayList<Integer> arrayList = howMuchNormal(ManagerAgent.packetsClassified);

                    System.out.println("Normal: "+arrayList.get(0)+"\t("+(arrayList.get(0)*100/ManagerAgent.packetsClassified.size())+")%");
                    System.out.println("Anomaly: "+arrayList.get(1)+"\t("+(arrayList.get(1)*100/ManagerAgent.packetsClassified.size())+")%");

                    System.exit(0);
                }

                index1++;

            }
        },58000,1000);


        PlatformPara.profile.setParameter(Profile.MAIN_HOST, "localhost");
        PlatformPara.profile.setParameter(Profile.GUI,"true");
        PlatformPara.containerController = PlatformPara.runtime.createMainContainer(PlatformPara.profile);
        PlatformPara.startTime=PlatformPara.methode();

        AgentController agentController=null;

        try {
            agentController = PlatformPara.containerController.createNewAgent("ManagerAgent","ManagerAgent",null);
            agentController.start();
        }catch (StaleProxyException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<Integer> howMuchNormal(ArrayList<PacketDetected> arrayList){
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        arrayList1.add(0);
        arrayList1.add(0);
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getCategory().equals("Normal")){
                arrayList1.set(0,arrayList1.get(0)+1);
            }else{
                arrayList1.set(1,arrayList1.get(1)+1);
            }
        }


        return arrayList1;
    }
}

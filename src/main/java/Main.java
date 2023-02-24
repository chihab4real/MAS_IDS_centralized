import com.mongodb.*;
import jade.core.Profile;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {


    static int index1=0;
    static String code = "_"+ManagerAgent.treating_time +"_"+ManagerAgent.numberOfContainers;
    public static void main(String[] args) {


        int time_ex=300;

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(index1>=time_ex){
                    try {
                        PlatformParameter.containerController.kill();
                    } catch (StaleProxyException e) {
                        e.printStackTrace();
                    }


                    System.out.println(ManagerAgent.allPackets.size());
                    System.out.println(ManagerAgent.packetsClassified.size());
                    try {
                        sendPackettoDB(ManagerAgent.packetsClassified);
                        System.exit(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.exit(-1);

                    ManagerAgent.stop = true;
                }


                index1+=1;

            }
        },58000,1000);


        PlatformParameter.profile.setParameter(Profile.MAIN_HOST, "localhost");
        PlatformParameter.profile.setParameter(Profile.GUI,"true");
        PlatformParameter.containerController = PlatformParameter.runtime.createMainContainer(PlatformParameter.profile);
        PlatformParameter.startTime=PlatformParameter.methode();

        AgentController agentController=null;

        try {
            agentController = PlatformParameter.containerController.createNewAgent("ManagerAgent","ManagerAgent",null);
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

    public static void getSummary() throws Exception{

        String fileName="C:\\Users\\pc\\Desktop\\3IDS_TEST\\C\\STATES"+PlatformParameter.startTime+".txt";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        String time_work="Started: "+PlatformParameter.startTime+"\nEnded: "+dtf.format(LocalDateTime.now());
        String container="Number of containers: "+ManagerAgent.numberOfContainers;
        String details_of_containers="";

        for(int i=0;i<ManagerAgent.containers.size();i++){
            String de="Container "+i+": ";
            ArrayList<Integer> arrayList = howMuchNormal2(ManagerAgent.containers.get(i));
            int x= arrayList.get(0)+arrayList.get(1);
            de+="\nTotale packets: "+(x);
            de+="\nNormal: "+arrayList.get(0)+" ("+(arrayList.get(0)*100/x)+"%)";
            de+="\nAnomalie: "+arrayList.get(1)+" ("+(arrayList.get(1)*100/x)+"%)";

            details_of_containers+="\n"+de+"\n--------------------------------------";

        }

        String str = "\n"+time_work+"\n"+container+"\n"+details_of_containers;



        try {

            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                //System.out.println("File created: " + myObj.getName());
            } else {
                //System.out.println("File already exists.");
            }

            FileWriter fileWritter = new FileWriter(fileName,true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            bw.write("\n"+str);
            bw.close();

            //System.out.println("Successfully wrote to the file.");


        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }




    }
    public static ArrayList<Integer> howMuchNormal2(Container container){


        ArrayList<Integer> arrayList1 = new ArrayList<>();
        arrayList1.add(0);
        arrayList1.add(0);

        for(int i=0;i<container.getPacketClassified().size();i++){
            if(container.getPacketClassified().get(i).getCategory().equals("Normal")){
                arrayList1.set(0,arrayList1.get(0)+1);
            }else{
                arrayList1.set(1,arrayList1.get(1)+1);
            }
        }


        return arrayList1;
    }


    //to be removed

    public static void sendPackettoDB(ArrayList<PacketDetected> packetDetected)throws Exception{
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("PacketsCentra");

        DBCollection collection = database.getCollection("PacketsDetected"+code);


        for(PacketDetected p : packetDetected){
            DBObject dbObject = p.toDBObject();
            collection.insert(dbObject);
        }









    }

}

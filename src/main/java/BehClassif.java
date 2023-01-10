import com.mongodb.*;
import jade.core.behaviours.CyclicBehaviour;
import weka.core.Instance;

import java.util.ArrayList;

public class BehClassif extends CyclicBehaviour {


    @Override
    public void action() {

        if(!ManagerAgent.packetsDetected.isEmpty()){
            PacketSniffer packetSniffer = ManagerAgent.packetsDetected.get(0);


            try {
                Solve(ManagerAgent.attacks,packetSniffer,ManagerAgent.DT,ManagerAgent.SVM,ManagerAgent.NN);


                ManagerAgent.packetsDetected.remove(packetSniffer);



            } catch (Exception e) {
                e.printStackTrace();
            }





        }
    }


    public void Solve(ArrayList<Attack> attacks, PacketSniffer packetTest, Clsi DT, Clsi SVM, Clsi NN) throws Exception{

        //String containerID = getMyID(getAID().getLocalName());

        double resultj48 = DT.getClassifier().classifyInstance(packetTest.getInstance());
        double resultjsvm = SVM.getClassifier().classifyInstance(packetTest.getInstance());
        double resultjnn = NN.getClassifier().classifyInstance(packetTest.getInstance());
        Attack attackj48 = attacks.get((int)resultj48);
        Attack attacksvm = attacks.get((int)resultjsvm);
        Attack attacknn = attacks.get((int)resultjnn);


        System.out.println("\n\nPacket : "+"--:\t"+packetTest.getInstance()+"\nDetected as :\n----------------");

        System.out.println("Decesion tree: "+attacks.get((int) resultj48).getName()+"  ["+attacks.get((int)resultj48).getCategory()+"]");

        System.out.println("SVM: "+attacks.get((int) resultjsvm).getName()+"  ["+attacks.get((int)resultjsvm).getCategory()+"]");
        System.out.println("NN: "+attacks.get((int) resultjnn).getName()+"  ["+attacks.get((int)resultjnn).getCategory()+"]\n\n");

        double finall= getFinaleClass(DT,SVM,NN,attackj48,attacksvm,attacknn);

        System.out.println("Final decision : "+attacks.get((int)finall).getName());

        Instance instance = packetTest.getInstance();
        instance.setValue(26,finall);
        PacketDetected packetDetected = new PacketDetected(instance);
        packetDetected.setCategory(attacks.get((int)finall).getCategory());
        System.out.println("\n\nCATE:"+packetDetected.getCategory());
        packetDetected.setBywho(getByWho(attacks.get((int)finall).getName(),attackj48,attacksvm,attacknn));
        ManagerAgent.packetsClassified.add(packetDetected);

        sendPackettoDB(packetDetected);

        /*

        System.out.println("\n----------------------------------------------\n\n"+getAID().getLocalName()+": \n");

        System.out.println(packetTest.getInstance());
        System.out.println("DT:\t"+ClassifAgent.j48.classifyInstance(packetTest.getInstance()));
        System.out.println("NN:\t"+ClassifAgent.multilayerPerceptron.classifyInstance(packetTest.getInstance()));
        System.out.println("SVM:\t"+ClassifAgent.smOreg.classifyInstance(packetTest.getInstance()));
        String containerID = getMyID(getAID().getLocalName());
        ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
        ManagerAgent.packetSolveds.add(new PacketSolved(packetTest.getInstance(),packetTest.getInstance(),true));
        ManagerAgent.containers.get(Integer.parseInt(containerID)-1).getPacketsDetected().remove(0);

        msg.setContent("PCT"+"_OK");
        AID dest = null;
        dest = new AID("SnifferAgent_Container"+containerID,AID.ISLOCALNAME);
        msg.addReceiver(dest);
        send(msg);

        Message messageListe;
        messageListe = new Message(msg.getSender().getLocalName(),"SnifferAgent_Container"+containerID,msg.getContent());
        ManagerAgent.addMessage(messageListe);

        //System.out.println("FINAAAAAAAAAL");*/

    }

    public  double getFinaleClass(Clsi DT, Clsi SVM, Clsi NN, Attack j48, Attack svm, Attack nn){



        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(j48);
        attacks.add(svm);
        attacks.add(nn);

        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Double> scores = new ArrayList<>();
        ArrayList<Clsi> clsis = new ArrayList<>();
        clsis.add(DT);
        clsis.add(SVM);
        clsis.add(NN);


        for(int i=0;i<attacks.size();i++){
            if(!strings.isEmpty() && strings.contains(attacks.get(i).getID())){
                double x = scores.get(strings.indexOf(attacks.get(i).getID()));

                scores.set(strings.indexOf(attacks.get(i).getID()),x+clsis.get(i).getEvaluation().fMeasure(1));
                x=0;

            }else{
                strings.add(attacks.get(i).getID());

                scores.add(clsis.get(i).getEvaluation().fMeasure(1));

            }
        }

        double max=-1;
        double index=-1;
        for(int i=0;i<scores.size();i++){
            if(scores.get(i)>max){
                max=scores.get(i);
                index=i;
            }
        }

        return Double.parseDouble(strings.get((int)index));
    }


    public String getByWho(String result, Attack j48, Attack svm, Attack nn){
        String x="";
        if(result.equals(j48.getName())){
            x+="DT,";
        }

        if(result.equals(svm.getName())){
            x+="SVM,";
        }

        if(result.equals(nn.getName())){
            x+="NN,";
        }

        System.out.println("\n\nBYWHO:"+x);
        return x;
    }


    public static void sendPackettoDB(PacketDetected packetDetected)throws Exception{
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("Test_Centralized");

        DBCollection collection = database.getCollection("PacketsDetected");


        DBObject dbObject = packetDetected.toDBObject();

        /*List<Integer> books = Arrays.asList(27464, 747854);

        DBObject person = new BasicDBObject("_id", "jo")
                .append("name", "Jo Bloggs")
                .append("address", new BasicDBObject("street", "123 Fake St")
                        .append("city", "Faketon")
                        .append("state", "MA")
                        .append("zip", 12345))
                .append("books", books);*/

        collection.insert(dbObject);
        System.out.println("Packet Detectd and saved as  "+dbObject.get("class"));


    }


}

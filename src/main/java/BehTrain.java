import com.mongodb.*;
import jade.core.behaviours.OneShotBehaviour;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BehTrain extends OneShotBehaviour {

    public static ArrayList<Attack> attacks = new ArrayList<>();

    Clasificator DT;
    Clasificator SVM;
    Clasificator NN;

    private boolean DT_TrainingDone;
    private boolean SVM_TrainingDone;
    private boolean NN_TrainingDone;

    @Override
    public void action() {
        try {

            ManagerAgent.test = new ConverterUtils.DataSource("KDDTest.arff").getDataSet();

            ManagerAgent.test.setClassIndex(ManagerAgent.test.numAttributes()-1);

            System.out.println("start-training");

            Instances TrainDataDT = getTrainDatasetDT("PacketsTrainDT");
            Instances TrainDataSVM = getTrainDatasetDT("PacketsTrainSVMNN");
            attacks = getAttacks();

            DT_TrainingDone=false;
            SVM_TrainingDone=false;
            NN_TrainingDone=false;
            CallDT(TrainDataDT);

            CallSVM(TrainDataSVM);
            CallNN(TrainDataSVM);

            ManagerAgent.DT=DT;
            ManagerAgent.SVM=SVM;
            ManagerAgent.NN=NN;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Attack> getAttacks() throws Exception {



        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("Test");

        DBCollection collection = database.getCollection("Attacks");
        DBCursor cursor = collection.find();
        ArrayList<DBObject> arrayList = (ArrayList<DBObject>) cursor.toArray();


        ArrayList<Attack> attacks = new ArrayList<>();
        for(int i = 0; i < arrayList.size(); i++) {
            attacks.add(new Attack(String.valueOf(arrayList.get(i).get("id")), String.valueOf(arrayList.get(i).get("name")), String.valueOf(arrayList.get(i).get("category"))));
        }

        ManagerAgent.attacks=attacks;
        return attacks;
    }

    public static Instances getTrainDatasetDT(String CollectionName) throws Exception {

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("Test");

        DBCollection collection = database.getCollection(CollectionName);
        DBCursor cursor = collection.find();
        ArrayList<DBObject> arrayList = (ArrayList<DBObject>) cursor.toArray();

        Instances instances = new ConverterUtils.DataSource("trainsmpl.arff").getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
        instances.clear();

        for (int i = 0; i < arrayList.size(); i++) {
            PacketDT packetDT = new PacketDT(arrayList.get(i));
            instances.add(packetDT.toInstance(instances));
        }
        return instances;
    }

    public void CallDT(Instances TrainDataDT) throws Exception {


        J48 j48 = new J48();
        j48.buildClassifier(TrainDataDT);

        DT = new Clasificator("DT", j48, TrainDataDT);


        System.out.println("DT DONE-----");
        System.out.println("F-Measure :" + DT.getEvaluation().fMeasure(1));
        /*System.out.println("Precision : "+DT.getEvaluation().precision(1));
        System.out.println("Recall: "+DT.getEvaluation().recall(1));
        System.out.println("Error rate: "+DT.getEvaluation().errorRate());
        System.out.println("% Correct: "+DT.getEvaluation().pctCorrect());
        System.out.println("% Incorrect:  "+DT.getEvaluation().pctIncorrect());*/
        System.out.println("----------------------\n");

        /*sendMSG("DTOK,"+TrainDataDT.size()+","+
                DT.getEvaluation().fMeasure(1)+","+
                DT.getEvaluation().precision(1)+","+
                DT.getEvaluation().falsePositiveRate(1)+","+
                DT.getEvaluation().kappa()+",");*/

        //sendMSG("DT,TR"+TrainDataDT.size()+",FM"+DT.getEvaluation().fMeasure(1)+",PR"+DT.getEvaluation().precision(1));
        DT_TrainingDone = true;


    }

    void CallNN(Instances TrainDataSVM) throws Exception {


        MultilayerPerceptron multilayerPerceptron = new MultilayerPerceptron();

        multilayerPerceptron.setLearningRate(0.1);
        multilayerPerceptron.setMomentum(0.2);
        multilayerPerceptron.setTrainingTime(2000);
        multilayerPerceptron.setHiddenLayers("3");
        multilayerPerceptron.buildClassifier(TrainDataSVM);
        NN = new Clasificator("NN", multilayerPerceptron, TrainDataSVM);
        System.out.println("NN done");
        System.out.println("F-Measure :" + NN.getEvaluation().fMeasure(1));
        /*System.out.println("Precision : "+DT.getEvaluation().precision(1));
        System.out.println("Recall: "+DT.getEvaluation().recall(1));
        System.out.println("Error rate: "+DT.getEvaluation().errorRate());
        System.out.println("% Correct: "+DT.getEvaluation().pctCorrect());
        System.out.println("% Incorrect:  "+DT.getEvaluation().pctIncorrect());*/
        System.out.println("----------------------\n");
        /*sendMSG("NNOK,"+TrainDataSVM.size()+","+
                NN.getEvaluation().fMeasure(1)+","+
                NN.getEvaluation().precision(1)+","+
                NN.getEvaluation().falsePositiveRate(1)+","+
                NN.getEvaluation().kappa()+",");*/
        //sendMSG("NN,TR"+TrainDataSVM.size()+",FM"+NN.getEvaluation().fMeasure(1)+",PR"+NN.getEvaluation().precision(1));

        NN_TrainingDone=true;

    }

    void CallSVM(Instances TrainDataSVM) throws Exception {


        SMO smo = new SMO();
        smo.buildClassifier(TrainDataSVM);
        SVM = new Clasificator("SVM", smo, TrainDataSVM);
        System.out.println("SVM done");
        System.out.println("F-Measure :" + SVM.getEvaluation().fMeasure(1));
        /*System.out.println("Precision : "+DT.getEvaluation().precision(1));
        System.out.println("Recall: "+DT.getEvaluation().recall(1));
        System.out.println("Error rate: "+DT.getEvaluation().errorRate());
        System.out.println("% Correct: "+DT.getEvaluation().pctCorrect());
        System.out.println("% Incorrect:  "+DT.getEvaluation().pctIncorrect());*/
        System.out.println("----------------------\n");
        /*sendMSG("SVMOK,"+TrainDataSVM.size()+","+
                SVM.getEvaluation().fMeasure(1)+","+
                SVM.getEvaluation().precision(1)+","+
                SVM.getEvaluation().falsePositiveRate(1)+","+
                SVM.getEvaluation().kappa()+",");*/
        //sendMSG("SVM,TR"+TrainDataSVM.size()+",FM"+SVM.getEvaluation().fMeasure(1)+",PR"+SVM.getEvaluation().precision(1));


        SVM_TrainingDone = true;


    }

    void SaveToLocal(Clasificator clsi) throws IOException {


        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\pc\\Desktop\\folderfortest\\dt.dat");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(clsi);

        objectOutputStream.close();
    }
}

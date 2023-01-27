import java.util.ArrayList;

public class Container {
    public static int count=1;
    private String ContainerID;
    private ArrayList<PacketSniffer> packetsDetected;

    private ArrayList<PacketSniffer> all;
    private ArrayList<PacketDetected> packetClassified;


    Container(){
        this.ContainerID = ""+count;
        count+=1;
        this.all = new ArrayList<>();
        this.packetsDetected= new ArrayList<>();
        this.packetClassified = new ArrayList<>();
    }
    public String getContainerID() {
        return ContainerID;
    }

    public void setContainerID(String containerID) {
        ContainerID = containerID;
    }

    public ArrayList<PacketSniffer> getPacketsDetected() {
        return packetsDetected;
    }

    public void setPacketsDetected(ArrayList<PacketSniffer> packetsDetected) {
        this.packetsDetected = packetsDetected;
    }

    public ArrayList<PacketSniffer> getAll() {
        return all;
    }

    public void setAll(ArrayList<PacketSniffer> all) {
        this.all = all;
    }

    public ArrayList<PacketDetected> getPacketClassified() {
        return packetClassified;
    }

    public void setPacketClassified(ArrayList<PacketDetected> packetClassified) {
        this.packetClassified = packetClassified;
    }
}

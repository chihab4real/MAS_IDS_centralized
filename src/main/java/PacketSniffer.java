import weka.core.Instance;

public class PacketSniffer {
    private Instance instance;

    private boolean solved;
    private String byWho;

    PacketSniffer(){

    }

    PacketSniffer(Instance instance, boolean solved,String byWho){
        this.instance=instance;
        this.solved = solved;
        this.byWho = byWho;

    }


    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instanceSVMNN) {
        this.instance = instanceSVMNN;
    }


    public String getByWho() {
        return byWho;
    }

    public void setByWho(String byWho) {
        this.byWho = byWho;
    }
}

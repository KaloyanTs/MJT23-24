package bg.sofia.uni.fmi.mjt.passvault.command;

public class VaultCommandBuilder {

    public enum COMMAND_TYPE {SAVE, ...}

    ;

    private COMMAND_TYPE type;

    public VaultCommandBuilder type(COMMAND_TYPE commandType) {
        type = commandType;
        return this;
    }

    public VaultCommand build() {
        switch (type) {
            case SAVE -> {
            }
        }
    }
}

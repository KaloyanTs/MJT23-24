package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class IoTDeviceBase implements IoTDevice {
    static int uniqueNumberDevice = 0;
    String name;
    double powerConsumption;
    LocalDateTime installationDateTime;
    LocalDateTime registration;

    public IoTDeviceBase(String name, double powerConsumption, LocalDateTime installationDateTime) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
    }

    @Override
    public abstract String getId();

    //	public void setDeviceID(int uniqueNumber)
    //	{
    //		deviceID = deviceID + uniqueNumber;
    //	}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return installationDateTime;
    }

    @Override
    public abstract DeviceType getType();

    @Override
    public LocalDateTime getRegistration() {
        return registration;
    }

    @Override
    public void setRegistration(LocalDateTime registration) {
        this.registration = registration;
    }

    @Override
    public long getPowerConsumptionKWh() {
        long duration = Duration.between(getInstallationDateTime(), LocalDateTime.now()).toHours();
        return (long) (duration * powerConsumption);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this.getClass().getTypeName().equals(other.getClass().getTypeName()) && hashCode() == other.hashCode();
    }

}

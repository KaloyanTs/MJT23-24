package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.EnumMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {

    private final Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = taxRates;
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException();
        }
        return taxRates.get(utilityType) * switch (utilityType) {
            case NATURAL_GAS -> billable.getNaturalGasConsumption();
            case ELECTRICITY -> billable.getElectricityConsumption();
            case WATER -> billable.getWaterConsumption();
        };
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException();
        }
        double tax = 0;
        for (UtilityType type : UtilityType.values()) {
            tax += getUtilityCosts(type, billable);
        }
        return tax;
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException();
        }
        Map<UtilityType, Double> res = new EnumMap<>(UtilityType.class);
        for (UtilityType type : UtilityType.values()) {
            res.put(type, Math.abs(getUtilityCosts(type, firstBillable) - getUtilityCosts(type,
                secondBillable)));
        }
        return Map.copyOf(res);
    }
}

package congestion.calculator;

import congestion.calculator.vehicle.Car;
import congestion.calculator.vehicle.Diplomat;
import congestion.calculator.vehicle.Emergency;
import congestion.calculator.vehicle.Foreign;
import congestion.calculator.vehicle.Military;
import congestion.calculator.vehicle.Motorcycle;
import congestion.calculator.vehicle.Tractor;
import congestion.calculator.vehicle.Vehicle;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CongestionTaxCalculator {

    public static final Map<String, Integer> tollFreeVehicles = new HashMap<>();

    static {
        tollFreeVehicles.put("Motorcycle", 0);
        tollFreeVehicles.put("Tractor", 1);
        tollFreeVehicles.put("Emergency", 2);
        tollFreeVehicles.put("Diplomat", 3);
        tollFreeVehicles.put("Foreign", 4);
        tollFreeVehicles.put("Military", 5);
    }
    
    public int getTax(Vehicle vehicle, List<LocalDateTime> dates)
    {
        if(isTollFreeVehicle(vehicle))
            return 0;
        if(dates == null || dates.isEmpty())
            return 0;

        Collections.sort(dates);

        LocalDateTime intervalStart = dates.get(0);
        int tempFee = getTollFee(intervalStart, vehicle);
        int totalFee = tempFee;

        for (int i = 1; i < dates.size() ; i++) {
            LocalDateTime date = dates.get(i);
            int nextFee = getTollFee(date, vehicle);
            if (nextFee == 0)
                continue;

            long diffInMillies = localDateTimeToEpochMillis(date) - localDateTimeToEpochMillis(intervalStart);
            long minutes = diffInMillies/1000/60;

            if (minutes <= 60 && nextFee > 0)
            {
                if (totalFee > 0) totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            }
            else
            {
                intervalStart = date;
                tempFee = nextFee;
                totalFee += nextFee;
            }
        }                
      
        if (totalFee > 60) totalFee = 60;
        return totalFee;
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        String vehicleType = vehicle.getVehicleType();
        return tollFreeVehicles.containsKey(vehicleType);
    }

    public int getTollFee(LocalDateTime date, Vehicle vehicle)
    {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) return 0;


        int hour = date.getHour();
        int minute = date.getMinute();

        if (hour == 6 && minute <= 29) return 8;
        else if (hour == 6) return 13;
        else if (hour == 7) return 18;
        else if (hour == 8 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14) return 8;
        else if (hour == 15 && minute <= 29) return 13;
        else if (hour >= 15 && hour <= 16) return 18;
        else if (hour == 17) return 13;
        else if (hour == 18 && minute <= 29) return 8;
        else return 0;
    }

    private boolean isTollFreeDate(LocalDateTime date)
    {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfWeek().getValue();
        int dayOfMonth = date.getDayOfMonth();

        if (day == DayOfWeek.SATURDAY.getValue() || day == DayOfWeek.SUNDAY.getValue()) return true;

        if (year == 2013)
        {
            return (month == 1 && dayOfMonth == 1) ||
                (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                (month == 7) ||
                (month == 11 && dayOfMonth == 1) ||
                (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26|| dayOfMonth == 31));
        }
        return false;
    }

    public Long localDateTimeToEpochMillis(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli();
    }

    public LocalDateTime epochMillisToLocalDateTime(long epochMillis) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMillis),
            ZoneId.systemDefault());
    }

    public Vehicle getVehicleByName(String vehicleName){
        Integer vehicleValue = tollFreeVehicles.get(vehicleName);
        if(vehicleValue == null){
            if ("Car".equals(vehicleName)){
                return new Car();
            }
            return null;
        }
        return switch (vehicleValue) {
            case 0 -> new Motorcycle();
            case 1 -> new Tractor();
            case 2 -> new Emergency();
            case 3 -> new Diplomat();
            case 4 -> new Foreign();
            case 5 -> new Military();
            default -> null;
        };
    }
}

package congestion.calculator;

import static org.junit.jupiter.api.Assertions.*;

import congestion.calculator.vehicle.Car;
import congestion.calculator.vehicle.Diplomat;
import congestion.calculator.vehicle.Emergency;
import congestion.calculator.vehicle.Foreign;
import congestion.calculator.vehicle.Military;
import congestion.calculator.vehicle.Motorcycle;
import congestion.calculator.vehicle.Tractor;
import congestion.calculator.vehicle.Vehicle;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CongestionTaxCalculatorTest {

  private List<LocalDateTime> getScribbledDates(){
    List<LocalDateTime> dateList = new ArrayList<>();
    dateList.add(LocalDateTime.parse("2013-01-14T21:00:00"));
    dateList.add(LocalDateTime.parse("2013-01-15T21:00:00"));
    dateList.add(LocalDateTime.parse("2013-02-07T06:23:27"));
    dateList.add(LocalDateTime.parse("2013-02-07T15:27:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T06:27:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T06:20:27"));
    dateList.add(LocalDateTime.parse("2013-02-08T14:35:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T15:29:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T15:47:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T16:01:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T16:48:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T17:49:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T18:29:00"));
    dateList.add(LocalDateTime.parse("2013-02-08T18:35:00"));
    dateList.add(LocalDateTime.parse("2013-03-26T14:25:00"));
    dateList.add(LocalDateTime.parse("2013-03-28T14:07:27"));
    return dateList;
  }

  private List<Integer> getTollFeesForScribbledDates(){
    List<Integer> feesList = new ArrayList<>();
    feesList.add(0);
    feesList.add(0);
    feesList.add(8);
    feesList.add(13);
    feesList.add(8);
    feesList.add(8);
    feesList.add(8);
    feesList.add(13);
    feesList.add(18);
    feesList.add(18);
    feesList.add(18);
    feesList.add(13);
    feesList.add(8);
    feesList.add(0);
    feesList.add(8);
    feesList.add(0);
    return feesList;
  }

  @Test
  void getTotalTax() {
    CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();
    List<LocalDateTime> dateList = getScribbledDates();


    int tax = congestionTaxCalculator.getTax(new Car(),dateList);
    assertEquals(23,tax);
  }

  @Test
  void getTollFee() {
    List<LocalDateTime> dateList = getScribbledDates();
    List<Integer> tollFeeList = getTollFeesForScribbledDates();
    CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();

    for (int i = 0 ; i < dateList.size() ; i++){
      assertEquals(tollFeeList.get(i), congestionTaxCalculator.getTollFee(dateList.get(i), new Car()));
    }

    for (LocalDateTime ldt : dateList){
      int tollFee = congestionTaxCalculator.getTollFee(ldt, new Car());
      System.out.println("Date: " + ldt + " DayOfWeek: " + ldt.getDayOfWeek() + " Tax: " + tollFee);
    }
  }

  @Test
  void getTollFeeForTollFreeVehicles(){
    List<LocalDateTime> dateList = getScribbledDates();
    List<Vehicle> tollFreeVehicles = new ArrayList<>();
    tollFreeVehicles.add(new Motorcycle());
    tollFreeVehicles.add(new Tractor());
    tollFreeVehicles.add(new Emergency());
    tollFreeVehicles.add(new Diplomat());
    tollFreeVehicles.add(new Foreign());
    tollFreeVehicles.add(new Military());

    CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();

    for (Vehicle vehicle : tollFreeVehicles) {
      for (LocalDateTime date : dateList) {
        assertEquals(0, congestionTaxCalculator.getTollFee(date, vehicle));
      }
    }
  }
}
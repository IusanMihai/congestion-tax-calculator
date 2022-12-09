package congestion.calculator.api;

import congestion.calculator.CongestionTaxCalculator;
import congestion.calculator.model.CongestionTaxDto;
import congestion.calculator.model.VehicleEntryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/congestiontax")
@RestController
public class CongestionTaxController {

  private final CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator();

  @PostMapping(value = "/get")
  public ResponseEntity<CongestionTaxDto> getCongestionTax(@RequestBody @Validated VehicleEntryDto vehicleEntry){
    Integer tax = congestionTaxCalculator.getTax(congestionTaxCalculator.getVehicleByName(vehicleEntry.getVehicleType()),vehicleEntry.getEntryDates());
    return new ResponseEntity<>(CongestionTaxDto.builder().congestionTax(tax).build(), HttpStatus.OK);
  }
}

package hei.school.demo.endpoint.rest.controller;

import hei.school.demo.endpoint.rest.controller.dto.ArrivalRequest;
import hei.school.demo.endpoint.rest.controller.dto.ArrivalsResponse;
import hei.school.demo.entity.Arrival;
import hei.school.demo.service.ArrivalService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ArrivalController {

  private final ArrivalService arrivalService;

  @GetMapping("/arrivals")
  public ResponseEntity<?> getArrivals(
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "perPage", defaultValue = "10") int perPage) {

    List<Arrival> arrivals = arrivalService.getArrivals(page, perPage);
    long total = arrivalService.countArrivals();

    ArrivalsResponse response =
        new ArrivalsResponse(arrivals, new ArrivalsResponse.Meta(total, page, perPage));

    return ResponseEntity.ok(response);
  }

  @PostMapping("/arrivals")
  public ResponseEntity<?> createArrival(@RequestBody ArrivalRequest request) {
    Arrival created = arrivalService.createArrival(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/arrivals/{id}")
  public ResponseEntity<?> getArrivalById(@PathVariable UUID id) {
    Arrival arrival = arrivalService.getArrivalById(id);
    return ResponseEntity.ok(arrival);
  }

  @PutMapping("/arrivals/{id}")
  public ResponseEntity<?> updateArrival(
      @PathVariable UUID id, @RequestBody ArrivalRequest request) {
    Arrival updated = arrivalService.updateArrival(id, request);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/arrivals/{id}")
  public ResponseEntity<?> deleteArrival(@PathVariable UUID id) {
    arrivalService.deleteArrival(id);
    return ResponseEntity.noContent().build();
  }
}

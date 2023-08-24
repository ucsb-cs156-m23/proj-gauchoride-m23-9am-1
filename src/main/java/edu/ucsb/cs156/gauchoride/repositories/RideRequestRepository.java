package edu.ucsb.cs156.gauchoride.repositories;

import edu.ucsb.cs156.gauchoride.entities.RideRequest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepository extends CrudRepository<RideRequest, Long> {
  Iterable<RideRequest> findAllByRiderId(long riderId);
}

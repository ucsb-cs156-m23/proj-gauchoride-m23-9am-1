package edu.ucsb.cs156.gauchoride.repositories;

import edu.ucsb.cs156.gauchoride.entities.RiderApplication;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RiderApplicationRepository extends CrudRepository<RiderApplication, Long> {
  Optional<RiderApplication> findById(long id);
  Iterable<RiderApplication> findByUserId(long userId);
}
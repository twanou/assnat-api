package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Roster;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RosterRepository extends MongoRepository<Roster, String> {

    Optional<Roster> findByEndDate(LocalDate endDate);
}

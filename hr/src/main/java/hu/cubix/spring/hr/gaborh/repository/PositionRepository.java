package hu.cubix.spring.hr.gaborh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.cubix.spring.hr.gaborh.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

	Position findBynameOfPosition(String nameOfPosition);
}

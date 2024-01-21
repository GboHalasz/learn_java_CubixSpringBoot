package hu.cubix.spring.hr.gaborh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.spring.hr.gaborh.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
	
	@Query("SELECT DISTINCT p FROM Position p LEFT JOIN FETCH p.employees")
	public List<Position> findAll();
	
	@Query("SELECT p FROM Position p LEFT JOIN FETCH p.employees WHERE p.id = :id")
	public Position findById(long id);

	Position findBynameOfPosition(String nameOfPosition);
}

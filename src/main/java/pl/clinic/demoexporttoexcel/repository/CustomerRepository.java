package pl.clinic.demoexporttoexcel.repository;

import pl.clinic.demoexporttoexcel.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}

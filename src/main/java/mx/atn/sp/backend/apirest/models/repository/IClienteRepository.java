package mx.atn.sp.backend.apirest.models.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.atn.sp.backend.apirest.models.entity.Cliente;

@Repository
public interface IClienteRepository extends PagingAndSortingRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE c.id = :id")
    Page<Cliente> findById(@Param("id") Long id, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.variable_nombre = :nombre")
    Page<Cliente> findByNombre(@Param("nombre") String nombre, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.id = :id AND c.variable_nombre = :nombre")
    Page<Cliente> findByIdAndNombre(@Param("id") Long id, @Param("nombre") String nombre, Pageable pageable);
}


//@Repository
//public interface IClienteRepository extends PagingAndSortingRepository<Cliente, Long>{
//
//	Page<Cliente> findById(Long id, Pageable pageable);
//    
//    Page<Cliente> findByNombre(String nombre, Pageable pageable);
//    
//    Page<Cliente> findByIdAndNombre(Long id, String nombre, Pageable pageable);
//}

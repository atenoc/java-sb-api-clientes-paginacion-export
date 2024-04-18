package mx.atn.sp.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.atn.sp.backend.apirest.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();
	public Cliente findById(Long id);
	byte[] exportarClientesToExcel() throws Exception;
	
	Page<Cliente> obtenerClientes(Pageable pageable);
    
    Page<Cliente> obtenerClientesPorId(Long id, Pageable pageable);
    
    Page<Cliente> obtenerClientesPorNombre(String nombre, Pageable pageable);
    
    Page<Cliente> obtenerClientesPorIdYNombre(Long id, String nombre, Pageable pageable);
}

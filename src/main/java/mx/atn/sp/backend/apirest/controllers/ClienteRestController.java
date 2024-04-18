package mx.atn.sp.backend.apirest.controllers;

import java.util.List;

import org.hibernate.boot.model.relational.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mx.atn.sp.backend.apirest.export.ExcelExporter;
import mx.atn.sp.backend.apirest.models.dto.ClienteDTO;
import mx.atn.sp.backend.apirest.models.entity.Cliente;
import mx.atn.sp.backend.apirest.models.services.IClienteService;

@CrossOrigin (origins = {"http://localhost:4200"})    //origen del dominio que puede enviar y recibir datos (Angular)
@RestController
@RequestMapping("/api")
@Api(tags = "Cliente API", description = "API para operaciones relacionadas con clientes")
public class ClienteRestController {
	
	@Autowired
	IClienteService clienteService;
	
	@Autowired
    ExcelExporter excelExporter;
	
	
	@GetMapping("/all-clientes")
	@ApiOperation(value = "Obtiene todos los clientes", notes = "Devuelve una lista de todos los clientes registrados")
	public List<Cliente> clientes(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/{id}")
	public Cliente cliente(@PathVariable Long id) {
		return clienteService.findById(id);
	}
	
	/* Old - Sin Sort
	@GetMapping("/clientes")
	public ResponseEntity<Page<Cliente>> obtenerClientes(@RequestParam("page") int page,
	                                                     @RequestParam("size") int size) {
		System.out.println("Page: "+page);
		System.out.println("Size: "+size);
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Cliente> clientes = clienteService.obtenerClientes(pageable);
	    return new ResponseEntity<>(clientes, HttpStatus.OK);
	} */
	
	
	@GetMapping("/clientes")
	public ResponseEntity<Page<ClienteDTO>> obtenerClientes(@RequestParam(value = "page", defaultValue = "0") int page,
	                                                        @RequestParam(value = "size", defaultValue = "30") int size,
	                                                        @RequestParam(value = "sort", required = false) String sort,
	                                                        @RequestParam(value = "id", required = false) Long id,
	                                                        @RequestParam(value = "nombre", required = false) String nombre) {
		
		System.out.print("Clientes consultados.");
		
	    try {
	        if (size < 1) {
	            size = 10;
	        }

	        Pageable pageable;
	        if (sort != null) {
	            String[] sortParams = sort.split(",");
	            String sortField = sortParams[0];
	            Sort.Direction sortDirection = Sort.Direction.ASC;
	            if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
	                sortDirection = Sort.Direction.DESC;
	            }
	            pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
	        } else {
	            pageable = PageRequest.of(page, size);
	        }

	        Page<Cliente> clientes;
	        if (id != null && nombre != null) {
	            clientes = clienteService.obtenerClientesPorIdYNombre(id, nombre, pageable);
	        } else if (id != null) {
	            clientes = clienteService.obtenerClientesPorId(id, pageable);
	        } else if (nombre != null) {
	            clientes = clienteService.obtenerClientesPorNombre(nombre, pageable);
	        } else {
	            clientes = clienteService.obtenerClientes(pageable);
	        }

	        Page<ClienteDTO> clientesDTO = clientes
	        		.map(cliente -> new ClienteDTO(
	        				cliente.getId(), 
	        				cliente.getNombre(), 
	        				cliente.getApellido()
	        		));
	        
	        return new ResponseEntity<>(clientesDTO, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}




	/* Genera y retorna como respuesta de la petición HTTP el archivo .xlsx genereado (convertido Bytes) */
	@GetMapping("/clientes/excel")
    public ResponseEntity<ByteArrayResource> generarDevolverExcel() {
        try {
            byte[] bytes = clienteService.exportarClientesToExcel();

            ByteArrayResource resource = new ByteArrayResource(bytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Clientes.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(bytes.length)
                    .body(resource);
        } catch (Exception e) {
            // Manejo del error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	/* Genera y Exporta el archivo Excel en alguna ruta en el equipo local */
	@GetMapping("/clientes/exportar-excel")
    public ResponseEntity<?> exportarExcel() {
        List<Cliente> clientes = clienteService.findAll();
        try {
        	System.out.println("exportar...");
            String filePath = "C://Users/TuUsuario/Downloads/clientes.xlsx";
            System.out.println("exportandoooooooo...");
            excelExporter.exportToExcel(clientes, filePath);
            return ResponseEntity.ok("Archivo Excel exportado exitosamente");
        } catch (Exception e) {
        	System.out.println("Error al exportar: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al exportar el archivo Excel");
        }
    }

}

package mx.atn.sp.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;

import mx.atn.sp.backend.apirest.export.ExcelExporter;
import mx.atn.sp.backend.apirest.models.entity.Cliente;
import mx.atn.sp.backend.apirest.models.repository.IClienteRepository;

@Service
public class ClienteServiceImpl implements IClienteService{

	@Autowired
	private IClienteRepository clienteRepository;
	
    @Autowired
    private ExcelExporter excelExporter;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
    public Page<Cliente> obtenerClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

	@Override
	public Page<Cliente> obtenerClientesPorId(Long id, Pageable pageable) {
	    return clienteRepository.findById(id, pageable);
	}

	@Override
	public Page<Cliente> obtenerClientesPorNombre(String nombre, Pageable pageable) {
	    return clienteRepository.findByNombre(nombre, pageable);
	}

	@Override
	public Page<Cliente> obtenerClientesPorIdYNombre(Long id, String nombre, Pageable pageable) {
	    return clienteRepository.findByIdAndNombre(id, nombre, pageable);
	}

	
	@Override
    public byte[] exportarClientesToExcel() throws Exception {
        // Obtener la lista de clientes desde la base de datos
        List<Cliente> clientes = findAll();

        // Generar el archivo Excel utilizando el ExcelExporter inyectado
        Workbook workbook = excelExporter.generarExcel(clientes);

        // Convierte el Workbook a un array de bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] bytes = outputStream.toByteArray();
        outputStream.close();

        return bytes;
    }
	
}

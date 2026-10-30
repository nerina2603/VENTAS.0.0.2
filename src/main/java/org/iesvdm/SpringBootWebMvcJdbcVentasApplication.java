package org.iesvdm;

import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringBootWebMvcJdbcVentasApplication implements CommandLineRunner {

	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private ComercialDAO comercialDAO;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebMvcJdbcVentasApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		log.info("*******************************");
		log.info("*Prueba de arranque ClienteDAO*");
		log.info("*******************************");

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

		int id = 1;
		Optional<Cliente> cliente = clienteDAO.find(id);

		if (cliente.isPresent()) {
			log.info("Cliente {}: {}", id, cliente.get());

			String nombreOld = cliente.get().getNombre();

			cliente.get().setNombre("Jose M");

			clienteDAO.update(cliente.get());

			cliente = clienteDAO.find(id);

			log.info("Cliente {}: {}", id, cliente.get());

			cliente.get().setNombre(nombreOld);
			clienteDAO.update(cliente.get());

		}

		Cliente clienteNew = new Cliente(0, "Jose M", "Martín", null, "Málaga", 100);

		clienteDAO.create(clienteNew);

		log.info("Cliente nuevo con id = {}", clienteNew.getId());

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

		clienteDAO.delete(clienteNew.getId());

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

		log.info("************************************");
		log.info("*FIN: Prueba de arranque ClienteDAO*");
		log.info("************************************");

		log.info("***********************************");
		log.info("*Prueba de arranque ComercialDAO*");
		log.info("***********************************");

		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		Comercial comercialNuevo = new Comercial(0, "Prueba", "DAO", "Comercial", 0.10f);
		comercialDAO.create(comercialNuevo);
		log.info("Comercial nuevo con id = {}", comercialNuevo.getId());

		try {
			Optional<Comercial> comercial = comercialDAO.find(comercialNuevo.getId());
			if (comercial.isPresent()) {
				log.info("Comercial {}: {}", comercialNuevo.getId(), comercial.get());

				comercial.get().setNombre("Prueba actualizada");
				comercial.get().setComision(0.12f);
				comercialDAO.update(comercial.get());

				comercial = comercialDAO.find(comercialNuevo.getId());
				log.info("Comercial actualizado {}: {}", comercialNuevo.getId(), comercial.orElse(null));
			}
		} finally {
			comercialDAO.delete(comercialNuevo.getId());
		}

		log.info("Comercial tras el borrado: {}", comercialDAO.find(comercialNuevo.getId()));
		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		log.info("**************************************");
		log.info("*FIN: Prueba de arranque ComercialDAO*");
		log.info("**************************************");

	}

}

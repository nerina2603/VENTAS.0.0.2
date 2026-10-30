package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Comercial;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository

@AllArgsConstructor
public class ComercialDAOImpl implements ComercialDAO {

	private JdbcTemplate jdbcTemplate;

	@Override
	public void create(Comercial cliente) {
		String sqlInsert = """
				INSERT INTO comercial (nombre, apellido1, apellido2, comisión)
				VALUES (?, ?, ?, ?)
				""";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			ps.setString(1, cliente.getNombre());
			ps.setString(2, cliente.getApellido1());
			ps.setString(3, cliente.getApellido2());
			ps.setFloat(4, cliente.getComision());
			return ps;
		}, keyHolder);

		cliente.setId(keyHolder.getKey().intValue());
		log.info("Insertados {} registros.", rows);

	}

	@Override
	public List<Comercial> getAll() {

		List<Comercial> listComercial = jdbcTemplate.query(
				"SELECT * FROM comercial",
				(rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión"))

		);

		log.info("Devueltos {} registros.", listComercial.size());

		return listComercial;
	}

	@Override
	public Optional<Comercial> find(int id) {
		List<Comercial> comerciales = jdbcTemplate.query(
				"SELECT * FROM comercial WHERE id = ?",
				(rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión")),
				id);

		if (comerciales.isEmpty()) {
			log.info("Comercial no encontrado.");
			return Optional.empty();
		}
		return Optional.of(comerciales.get(0));
	}

	@Override
	public void update(Comercial cliente) {
		int rows = jdbcTemplate.update("""
				UPDATE comercial SET
					nombre = ?,
					apellido1 = ?,
					apellido2 = ?,
					comisión = ?
				WHERE id = ?
				""", cliente.getNombre(), cliente.getApellido1(), cliente.getApellido2(),
				cliente.getComision(), cliente.getId());
		log.info("Update de Comercial con {} registros actualizados.", rows);

	}

	@Override
	public void delete(long id) {
		int rows = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);
		log.info("Delete de Comercial con {} registros eliminados.", rows);

	}

}

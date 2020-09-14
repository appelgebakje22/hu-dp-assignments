package nl.appelgebakje22.dp.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import nl.appelgebakje22.dp.dao.IDAO;
import nl.appelgebakje22.dp.lib.SQLColumn;
import nl.appelgebakje22.dp.lib.SQLIgnore;
import nl.appelgebakje22.dp.lib.SQLTable;

abstract class AbstractDAOImpl<T> implements IDAO<T> {

	private static final List<Class<?>> ALLOWED_TYPES = Arrays.asList(Integer.class, int.class, Float.class, float.class, Date.class, String.class);
	private final String tableName;
	protected final Connection conn;
	protected final String[] columns;
	protected final Class<?>[] columnTypes;

	protected AbstractDAOImpl(Connection conn, Class<?> entityClass) {
		this.conn = conn;
		SQLTable tableInfo = entityClass.getAnnotation(SQLTable.class);
		if (tableInfo == null) {
			throw new NullPointerException("Missing " + SQLTable.class.getName() + " annotation!");
		}
		this.tableName = tableInfo.value();
		Field[] fields = Arrays.stream(entityClass.getDeclaredFields())
				.filter(field -> !field.isAnnotationPresent(SQLIgnore.class))
				.toArray(Field[]::new);
		this.columns = new String[fields.length];
		this.columnTypes = new Class[fields.length];
		for (int i = 0; i < fields.length; ++i) {
			Field field = fields[i];
			String name = field.isAnnotationPresent(SQLColumn.class) ? field.getAnnotation(SQLColumn.class).value() : field.getName();
			Class<?> type = field.getType();
			if (i == 0 && type.isPrimitive()) {
				throw new RuntimeException("Field %s must not be a primitive");
			}
			if (!AbstractDAOImpl.ALLOWED_TYPES.contains(field.getType())) {
				throw new RuntimeException(String.format("Field %s has invalid type %s", name, type.getName()));
			}
			this.columns[i] = name;
			this.columnTypes[i] = type;
		}
	}

	protected String createInsertQuery() {
		return "INSERT INTO " + this.tableName + " VALUES (" + String.join(",", IntStream.range(0, this.columns.length).mapToObj(ignored -> "?").toArray(String[]::new)) + ")";
	}

	protected String createUpdateQuery() {
		StringBuilder builder = new StringBuilder("UPDATE ").append(this.tableName).append(" SET ");
		for (int i = 1; i < this.columns.length; ++i) {
			builder.append(this.columns[i]).append(" = ?");
			if (i < this.columns.length - 1) {
				builder.append(", ");
			}
		}
		builder.append(" WHERE ").append(this.columns[0]).append(" = ?");
		return builder.toString();
	}

	protected void mapData(PreparedStatement stmt, Object[] data) throws SQLException {
		if (data.length != this.columns.length) {
			throw new RuntimeException("Data doesn't match column definition!");
		}
		for (int i = 0; i < this.columns.length; ++i) {
			final int sqlIndex = i + 1;
			Class<?> type = this.columnTypes[i];
			if (type == Integer.class || type == int.class) {
				stmt.setInt(sqlIndex, (int) data[i]);
			} else if (type == Float.class || type == float.class) {
				stmt.setFloat(sqlIndex, (float) data[i]);
			} else if (type == Date.class) {
				stmt.setDate(sqlIndex, (Date) data[i]);
			} else if (type == String.class) {
				stmt.setString(sqlIndex, (String) data[i]);
			} else {
				throw new RuntimeException("Cannot map column type: " + type);
			}
		}
	}

	@Override
	public T findById(int id) {
		try (PreparedStatement stmt = this.conn.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", this.tableName, this.columns[0]))) {
			stmt.setInt(1, id);
			ResultSet set = stmt.executeQuery();
			return set.next() ? this.mapEntity(set) : null;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return null;
		}
	}

	public List<T> findAll() {
		try (Statement stmt = this.conn.createStatement()) {
			ResultSet set = stmt.executeQuery("SELECT * FROM " + this.tableName);
			ArrayList<T> result = new ArrayList<>();
			while (set.next()) {
				result.add(this.mapEntity(set));
			}
			return result;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return Collections.emptyList();
		}
	}

	protected abstract T mapEntity(ResultSet set) throws SQLException;
}
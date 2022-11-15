package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import server.TableSchema.Column;


public class TableData {

	DbAccess db;
	
	public TableData(DbAccess db) {
		this.db=db;
	}

	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
		List<Example> list = new LinkedList<Example>();
		String str = "SELECT DISTINCT * FROM " + table;
		Statement statement = db.getConnection().createStatement();
		ResultSet result = statement.executeQuery(str);
		TableSchema schema = new TableSchema(db, table);
		while(result.next()) {
			Example ex = new Example();
			for(int i = 0; i < schema.getNumberOfAttributes(); i++) {
				if(schema.getColumn(i).isNumber()) {
					ex.add(result.getFloat(i+1));
				}else {
					ex.add(result.getString(i+1));
				}
			}
			list.add(ex);
		}
		return list;
	}

	
	public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException{
		String query = new String("SELECT DISTINCT " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName() + " ASC");
		Statement statement = db.getConnection().createStatement();
		ResultSet res = statement.executeQuery(query);
		Set<Object> set = new TreeSet<Object>();
		while(res.next()) {
			set.add(res.getObject(1));
		}
		return set;
	}

	public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException,NoValueException{
		String query = new String("SELECT " + aggregate.toString() + "(" + column.getColumnName() + ") FROM " + table);
		Statement statement = db.getConnection().createStatement();
		ResultSet res = statement.executeQuery(query);
		if(res.next()) {
			return res.getObject(1);
		}else {
			return null;
		}
	}
}

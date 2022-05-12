package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> idMapAll) 
	{

		String sql = "SELECT ccode, StateNme FROM country ORDER BY StateNme";
		
		try 
		{
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) 
			{
				if(!idMapAll.containsKey(rs.getInt("ccode")))
				{
					Country c = new Country(rs.getInt("ccode"), rs.getString("StateNme"));
					
					idMapAll.put(c.getId(), c);
				}
				
			}
			
			conn.close();

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country> idMapAll) 
	{
		String sql = "SELECT state1no AS id1, state2no AS id2 "
				+ "FROM contiguity "
				+ "WHERE year <= ? "
				+ "AND conttype = 1 "
				+ "AND state1no > state2no";
		
		List<Border> confini = new ArrayList<Border>();
		
		try 
		{
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) 
			{
				int id1 = rs.getInt("id1");
				int id2 = rs.getInt("id2");
				Country c1 = idMapAll.get(id1);
				Country c2 = idMapAll.get(id2);
				
				Border b = new Border(c1, c2);
				
				confini.add(b);
			}
			
			conn.close();
			return confini;

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}

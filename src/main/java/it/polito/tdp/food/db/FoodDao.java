package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public void listAllFoods(Map <Integer, Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f=new Food(res.getInt("food_code"),
							res.getString("display_name")
							);
					idMap.put(f.getFood_code(), f);
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List <Food> getVertici(Map <Integer, Food> idMap, int porzioni){
		String sql = "SELECT DISTINCT f.food_code as id, COUNT(DISTINCT p.portion_id) AS pp "
				+ "FROM food f, `portion` p  "
				+ "WHERE f.food_code=p.food_code "
				+ "GROUP BY f.food_code "
				+ "HAVING pp = ? " 
				+ " ORDER BY f.display_name ASC" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food f= idMap.get(res.getInt("id"));
				if(f!=null) {
					list.add(f);
				}
			}
			
			conn.close();
			return list;
			

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
			
		}

	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer, Food> idMap){
		String sql = "SELECT f1.food_code as f1, f2.food_code as f2, AVG(c1.condiment_calories) AS peso "
				+ "FROM food_condiment f1, food_condiment f2, condiment c1, condiment c2 "
				+ "WHERE f1.condiment_code=c1.condiment_code AND f2.condiment_code=c2.condiment_code "
				+ "AND f1.food_code> f2.food_code AND f1.condiment_code=f2.condiment_code "
				+ "GROUP BY f1.food_code, f2.food_code "
				+ "ORDER BY peso DESC";
		try {
 			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food f1=idMap.get(res.getInt("f1"));
				Food f2= idMap.get(res.getInt("f2"));
				double peso=res.getDouble("peso");
				if(f1!=null &&f2!=null) {
					Adiacenza a= new Adiacenza(f1,f2,peso);
					list.add(a);
				}
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
}

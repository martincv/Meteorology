package core;

import model.SearchQuery;
import dao.SearchQueryDAO;

public class DatabaseTester {

	public static void main(String[] args) {
		try{  
			SearchQueryDAO search = new SearchQueryDAO();
			SearchQuery query = new SearchQuery();
			query.setTimeFrom(1000L);
			query.setTimeTo(2000L);
			search.insertNewQuery(query);
				
			}catch(Exception e){e.printStackTrace();}  

	}

}

package com.cruds.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
public class RunBookManagement {
    String sql="";
    Connection conn = null;
    Statement stmt = null;
    
    	static
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
    	RunBookManagement ()
	{
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/book_management","USERNAME","PASSWORD");// please enter your credentials
		} catch (SQLException e) {
			e.printStackTrace();
		}
                try {
                    stmt = conn.createStatement();
                } catch (SQLException e) {
                        e.printStackTrace();
	}
	}
    
    public DefaultTableModel ListBooks()
    {
    			sql="select * from book;";
                        Vector <String> colNames=new Vector<>();
                        colNames.add("isbn");
                        colNames.add("title");
                        colNames.add("category");
                        colNames.add("total");
                        Vector<Vector<String>> data= new Vector<>();
			try {
				ResultSet result=stmt.executeQuery(sql);
				//System.out.println("isbn--title--category--total\n");
				while (result.next())
				{
                                    Vector<String> row1=new Vector<>();
                                    row1.add(result.getString("isbn"));
                                    row1.add(result.getString("title"));
                                    row1.add(result.getString("category"));
                                    row1.add(result.getString("total"));
                                    data.add(row1);
					/*long is=result.getLong("isbn");
					System.out.print (is+"--");
					String ti=result.getString("title");
					System.out.print(ti+"--");
					String ca=result.getString("category");
					System.out.print(ca+"--");
					int tot=result.getInt("total");
					System.out.println(tot);*/
				}
				result.close();
				//System.out.println();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
                        return  new DefaultTableModel(data,colNames);
    }
    
    public boolean AddBook(String title, long ISBN, String category, String authName, long authNo)
    {
		sql="INSERT INTO BOOK VALUES("+ISBN+",'"+title+"','"+category+"',"+1+") on duplicate key update total=total+1;";//where total is the total number of same book
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		sql="insert into author values('"+authName+"',"+authNo+","+ISBN+") on duplicate key update authname=authname";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
    }
    public DefaultTableModel SearchBookTitle(String titleSearch)
    {
    	Vector<Vector<String>> data= new Vector<>();
    	Vector <String> colNames=new Vector<>();
		sql="select * from book where locate('"+titleSearch+"',title)!=0";
		try {
			ResultSet result=stmt.executeQuery(sql);
			//System.out.println("isbn--title--category--total\n");
			
            colNames.add("isbn");
            colNames.add("title");
            colNames.add("category");
            colNames.add("total");
            
			while (result.next())
			{
				/*long is=result.getLong("isbn");
				System.out.print (is+"--");
				String ti=result.getString("title");
				System.out.print(ti+"--");
				String ca=result.getString("category");
				System.out.print(ca+"--");
				int tot=result.getInt("total");
				System.out.println(tot);*/
				 Vector<String> row1=new Vector<>();
                 row1.add(result.getString("isbn"));
                 row1.add(result.getString("title"));
                 row1.add(result.getString("category"));
                 row1.add(result.getString("total"));
                 data.add(row1);
			}
			result.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	    return  new DefaultTableModel(data,colNames);
    }
    
public DefaultTableModel SearchCategory(String categorySearch)
{
	Vector<Vector<String>> data= new Vector<>();
	Vector <String> colNames=new Vector<>();
	sql="select * from book where locate('"+categorySearch+"',category)!=0";
	try {
		ResultSet result=stmt.executeQuery(sql);
		//System.out.println("isbn--title--category--total\n");
		
        colNames.add("isbn");
        colNames.add("title");
        colNames.add("category");
        colNames.add("total");
        
		while (result.next())
		{
			/*long is=result.getLong("isbn");
			System.out.print (is+"--");
			String ti=result.getString("title");
			System.out.print(ti+"--");
			String ca=result.getString("category");
			System.out.print(ca+"--");
			int tot=result.getInt("total");
			System.out.println(tot);*/
			 Vector<String> row1=new Vector<>();
             row1.add(result.getString("isbn"));
             row1.add(result.getString("title"));
             row1.add(result.getString("category"));
             row1.add(result.getString("total"));
             data.add(row1);
		}
		result.close();
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
    return  new DefaultTableModel(data,colNames);
}

public int IssueBook(String name, String usn, long is, String date)
{
	// Issue book to student
	int flag=0;
	try {
		sql="select total from book where isbn="+is+";";
		ResultSet result=stmt.executeQuery(sql);
		result.next();
		int tot=result.getInt("total");
		if (tot==0)
		{
			return 2;
		}
		else
		{
		tot--;
		sql="update book set total="+tot+" where isbn="+is+";";
		stmt.executeUpdate(sql);
		Date d= new SimpleDateFormat("dd/MM/yyyy").parse(date);
		java.sql.Date sqlDate = new java.sql.Date(d.getTime());//issue date
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		//System.out.println(d);
		c.add(Calendar.DAY_OF_MONTH, 7);
		String returnDate = (new SimpleDateFormat("yyyy-MM-dd")).format(c.getTime());//return date
		//System.out.println(returnDate);
		sql="insert ignore into student(usn,name) values('"+usn+"','"+name+"');";
		stmt.executeUpdate(sql);
		sql="insert into issue(usn,issuedate,returndate,isbn) values('"+usn+"','"+sqlDate+"','"+returnDate+"',"+is+");";
		stmt.executeUpdate(sql);
		return 1;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return 0;
}


public DefaultTableModel IssuedBooks(String usnSearch)
{
	//System.out.println("Enter the usn to search for");
	sql="select * from issue where usn='"+usnSearch+"';";
	Vector<Vector<String>> data= new Vector<>();
	Vector <String> colNames=new Vector<>();
    colNames.add("issue id");
    colNames.add("usn");
    colNames.add("issue date");
    colNames.add("return date");
    colNames.add("isbn");
	try {
		ResultSet result=stmt.executeQuery(sql);
		//System.out.println("id--usn--isuuedate--returndate--isbn\n");
		while (result.next())
		{
			Vector<String> row1=new Vector<>();
			row1.add(result.getString("issueid"));
			row1.add(result.getString("usn"));
			java.sql.Date issueDate = result.getDate("issuedate");
			row1.add(issueDate.toString());
			java.sql.Date returnDate = result.getDate("returndate");
			row1.add(returnDate.toString());
			row1.add(result.getString("isbn"));
			data.add(row1);
		}
		result.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
                    return  new DefaultTableModel(data,colNames);
}

public DefaultTableModel curr()
{
	DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date Date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(Date.getTime());
    sql="select * from issue where returndate='"+sqlDate+"';";
	Vector<Vector<String>> data= new Vector<>();
	Vector <String> colNames=new Vector<>();
    colNames.add("issue id");
    colNames.add("usn");
    colNames.add("issue date");
    colNames.add("return date");
    colNames.add("isbn");
	try {
		ResultSet result=stmt.executeQuery(sql);
		//System.out.println("id--usn--isuuedate--returndate--isbn\n");
		while (result.next())
		{
			Vector<String> row1=new Vector<>();
			row1.add(result.getString("issueid"));
			row1.add(result.getString("usn"));
			java.sql.Date issueDate = result.getDate("issuedate");
			row1.add(issueDate.toString());
			java.sql.Date returnDate = result.getDate("returndate");
			row1.add(returnDate.toString());
			row1.add(result.getString("isbn"));
			data.add(row1);
		}
		//System.out.println();
		result.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
    return  new DefaultTableModel(data,colNames);
}

public DefaultTableModel SearchAuthor(String authSearch)
{
	sql="select * from author as a , book as b where locate('"+authSearch+"',authname)!=0 and a.isbn=b.isbn";
	Vector<Vector<String>> data= new Vector<>();
	Vector <String> colNames=new Vector<>();
	colNames.add("isbn");
	colNames.add("title");
	colNames.add("category");
	colNames.add("total");
	colNames.add("authname");
	colNames.add("authnumber");
	try {
		ResultSet result=stmt.executeQuery(sql);
		//System.out.println("isbn--title--category--total--author_name--author_phone\n");
		while (result.next())
		{
			Vector<String> row1=new Vector<>();
			row1.add(result.getString("isbn"));
			row1.add(result.getString("title"));
			row1.add(result.getString("category"));
			row1.add(result.getString("total"));
			row1.add(result.getString("authname"));
			row1.add(result.getString("authnumber"));
			data.add(row1);
		}
		result.close();
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
    return  new DefaultTableModel(data,colNames);
}
}

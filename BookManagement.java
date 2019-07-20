package com.cruds.BookManagement;
import java.sql.*;
import java.util.Scanner;
public class BookManagement {
	static
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection()
	{
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/book_management","YOUR USERNAME","YOUR PASSWORD");//mention your username and password
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void main (String args[])
	{   Connection conn = null;
	    Statement stmt = null;
	    String title="",category,authName,v,sql;
	    long ISBN,authNo;
		conn=getConnection();
		if (getConnection()!=null)
		{
			System.out.println("Connection Established!");
		}
	Scanner sc=new Scanner(System.in);
	try {
		stmt = conn.createStatement();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	while (true)
	{
		System.out.println("1.Add a book 2.Search Book Title 3.Search Book Category 4.Search Book Author 5.List all books 6. Issue book to student 7.List books issued to student based on USN number 8.List books which are to be returned for current date 9.Exit");
		int choice=sc.nextInt();
		v=sc.nextLine();
		switch(choice)
		{
		case 1:
			//Add record of books
			System.out.println("Enter book title, "
					+ " ISBN ( Book Number), category and Author information ( Author Name and Phone Number) ");
			title=sc.nextLine();
			ISBN=sc.nextLong();
			v=sc.nextLine();
			category=sc.nextLine();
			authName=sc.nextLine();
			authNo=sc.nextLong();
			v=sc.nextLine();
			sql="INSERT INTO BOOK VALUES("+ISBN+",'"+title+"','"+category+"',"+1+") on duplicate key update total=total+1;";//where total is the total number of same book
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql="insert into author values('"+authName+"',"+authNo+","+ISBN+");";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			//Search Book Title 
			System.out.println("Enter book title to search for");
			String titleSearch=sc.nextLine();
			sql="select * from book where locate('"+titleSearch+"',title)!=0";
			try {
				ResultSet result=stmt.executeQuery(sql);
				System.out.println("isbn--title--category--total\n");
				while (result.next())
				{
					long is=result.getLong("isbn");
					System.out.print (is+"--");
					String ti=result.getString("title");
					System.out.print(ti+"--");
					String ca=result.getString("category");
					System.out.print(ca+"--");
					int tot=result.getInt("total");
					System.out.println(tot);
				}
				result.close();
				System.out.println();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;
		case 3:
			//Search book category
			System.out.println("Enter book category to search for");
			String categorySearch=sc.nextLine();
			sql="select * from book where locate('"+categorySearch+"',category)!=0";
			try {
				ResultSet result=stmt.executeQuery(sql);
				System.out.println("isbn--title--category--total\n");
				while (result.next())
				{
					long is=result.getLong("isbn");
					System.out.print (is+"--");
					String ti=result.getString("title");
					System.out.print(ti+"--");
					String ca=result.getString("category");
					System.out.print(ca+"--");
					int tot=result.getInt("total");
					System.out.println(tot);
				}
				result.close();
				System.out.println();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;
		case 4:
			//Search book Author
			System.out.println("Enter book author name to search for");
			String authSearch=sc.nextLine();
			sql="select * from author as a , book as b where locate('"+authSearch+"',authname)!=0 and a.isbn=b.isbn";
			try {
				ResultSet result=stmt.executeQuery(sql);
				System.out.println("isbn--title--category--total--author_name--author_phone\n");
				while (result.next())
				{
					long is=result.getLong("isbn");
					System.out.print (is+"--");
					String ti=result.getString("title");
					System.out.print(ti+"--");
					String ca=result.getString("category");
					System.out.print(ca+"--");
					int tot=result.getInt("total");
					System.out.print(tot+"--");
					String aname=result.getString("authname");
					System.out.print(aname+"--");
					long ano=result.getLong("authnumber");
					System.out.println(ano);
				}
				result.close();
				System.out.println();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;
		case 5:
			sql="select * from book;";
			try {
				ResultSet result=stmt.executeQuery(sql);
				System.out.println("isbn--title--category--total\n");
				while (result.next())
				{
					long is=result.getLong("isbn");
					System.out.print (is+"--");
					String ti=result.getString("title");
					System.out.print(ti+"--");
					String ca=result.getString("category");
					System.out.print(ca+"--");
					int tot=result.getInt("total");
					System.out.println(tot);
				}
				result.close();
				System.out.println();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 6:
			// Issue book to student
			break;
		case 7:
			//List books issued to student based on USN number
			break;
		case 8:
			//List books which are to be returned for current date
			break;
		case 9:
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice!");
		}
	}
	}
}

/*
 * LibraryModel.java
 * Author: sarilroya 300492683
 * Created on: Began: 19/05/2021
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.*;

public class LibraryModel {
    // For use in creating dialogs and making them modal
    private JFrame dialogParent;
    Connection connectn;
    Statement statemnt;
    ResultSet resultSet;


    public LibraryModel(JFrame parent, String userid, String password) {
	dialogParent = parent;

    try{
        Class.forName("org.postgresql.Driver");

    String url = "jdbc:postgresql://db.ecs.vuw.ac.nz/" + userid + "_jdbc";
    connectn = DriverManager.getConnection(url, userid, password);

    } catch (SQLException e){
        e.printStackTrace();

    } catch (ClassNotFoundException e){
        e.printStackTrace();
    }
    }

    /**
    * bookLookup method prints out the information of a certain book 
    * by searching its isbn. 
    * Goes through the library of books by using a query, and if there is a match 
    * with the isbn entered then it will be added into the output and print. 
    * @param isbn
    * @return "Book Lookup:
    *          ISBN: title
    *          Edition: 1 - Number of Copies: 10 - Copies Remaining: 10
    *          Authors: Surnames"
    */
    public String bookLookup(int isbn) {
	    String edition = "";
        String copiesN = "";
        String copiesLeft = "";
        String Author = "";
        String title = "No such ISBN";
        
        try {
            String query = "SELECT * FROM Book NATURAL JOIN Book_Author NATURAL JOIN AUTHOR " + 
            			   "WHERE (isbn = " + isbn + ")" + 
            			   "ORDER BY AuthorSeqNo ASC;";

            Statement statemnt = connectn.createStatement();
            ResultSet result = statemnt.executeQuery(query);

            //add book information into result. 
            while(result.next()){ 
                edition = result.getString("edition_no");
                copiesN =result.getString("numofcop");
                copiesLeft = result.getString("numleft");
                Author += result.getString("surname") + ",";
                title = result.getString("Title");
            }
            statemnt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    return "Book Lookup: \n 	" + isbn + ": " + title + "\n 	Edition: " + edition + 
    		" - Number of copies: " + copiesN + " - Copies Left: " + copiesLeft + "\n 	Authors: " + Author.replaceAll("\\s+"," ");
    }


    /**
    * Returns all books that are looked up in the catalogue. 
    * showCatalogue uses the previous method (bookLookup) to search the books
    * and output the library. 
    * @return "Show Catalogue:
    * 		   Book Lookup:
    * 		   ISBN: title
    *          Edition: 1 - Number of Copies: 10 - Copies Remaining: 10
    *          Authors: Surnames"
    */
    public String showCatalogue() {
       String retrn = "Show Catalogue: ";
	   String output = "";

        try {
            String query = "SELECT isbn FROM Book ORDER BY isbn ASC;";
            Statement statemnt = connectn.createStatement();
            ResultSet result = statemnt.executeQuery(query);

            while(result.next()){
                int isbn = result.getInt("isbn");

                //search the book with its isbn using method bookLookup.
                output += "\n \n " + bookLookup(isbn); 
            }

        } catch (SQLException e) {
            return "ERROR accessing catalogue.";
        }

    return retrn + output;
    }


    /**
    * Prints loaned books. 
    * If any will print the book, if none will print no loaned. 
    *  @return "Loaned Books:
    *  			(No Loaned Books)"
    */
    public String showLoanedBooks() {
	   String output = "Show Loaned Books: \n 	";

        try {
            int isbn = 0;
            //if no books are loaned, the boolean will be returned false
            boolean loanedB = false;

            String query = "SELECT * FROM Book WHERE (numofcop > numLeft) ORDER BY isbn ASC;";
            Statement statemnt = connectn.createStatement();
            ResultSet result = statemnt.executeQuery(query);
            
            //the book has been succesfully loaned (if true)
            while(result.next()){
                loanedB = true;
                isbn = result.getInt("isbn");
                output += bookLookup(isbn) + "\n \n ";
            }
            statemnt.close();

            if(loanedB == false) return output +  "(No Loaned Books)"; 

        } catch (SQLException e) {
            return "ERROR accessing books.";
        }

        return output;
    }


    /**
    * Returns and shows the Author of the book.
    * @param authorID
    * @return "Show Author:
    * 		   authorID - Author Name
    * 		   Book Written: 
    * 		   ISBN - Title"
    */
    public String showAuthor(int authorID) {
	    String title = "";
        String output = "" ;
        String theBook = "";
        int ID = 0;

        try {
            String query = "SELECT * FROM Book NATURAL JOIN Book_Author NATURAL JOIN AUTHOR " + 
            		       "WHERE (AuthorId = " + authorID + ")" + 
            			   "ORDER BY AuthorSeqNo ASC;";

            Statement statemnt = connectn.createStatement();
            ResultSet result = statemnt.executeQuery(query);

            while(result.next()){
            	//iterates +1 book
                ID++; 
                output = "	" + authorID + " - " + result.getString("name").replaceAll("\\s+","") + " " + result.getString("surname").replaceAll("\\s+","") + "\n";
                theBook += "\n	" + result.getInt("isbn") + " - " + result.getString("title");
            }

            //Book does not exist/have been written.
            if(ID == 0){ title = "(no books written)"; }
            else { title = "	Book written:"; }
            
            statemnt.close();

        } catch (SQLException e) {
            return "No such Author ID: ";
        }

        return "Show Author: \n" + output + title + theBook + " \n";
    }


    /**
     * Returns and shows all Authors.
     * @return "Showing Authors: 
     * 			authorID - Author Name"
     */
    public String showAllAuthors() {
	   String authors = "Showing Authors: \n";

        try {
            String select = "SELECT * FROM author;";
            Statement statemnt = connectn.createStatement();
            ResultSet result = statemnt.executeQuery(select);
            
            while(result.next()){
                authors += " 	" + result.getInt("AuthorId") + " - " + result.getString("name").replaceAll("\\s+","") + ", " + result.getString("surname") + "\n";
            }

        } catch (SQLException e) {
            return "ERROR Showing Authors";
        }

    if(authors == null) { return "No Authors"; }

    return authors;
    }


    /**
     * Returns all of the customer's information, 
     * and the number of books borrowed.
     * @param customerID
     * @return "Show Customer:
     * 			customerID: lname, fname - city
     * 			Books Borrowed: "
     */
    public String showCustomer(int customerID) {
    	String cust = "Show Customer: \n" ;
        String book = "";
        String borrowed = "";
        
        try{
            int customer = 0;
            Statement statemnt = connectn.createStatement();
            String select = "SELECT * FROM  Customer WHERE (customerId = " + customerID + ");";
            ResultSet result = statemnt.executeQuery(select);
            
            while(result.next()){ customer++; }
            if(customer == 0){ return "No such Customer ID"; }
            
            try {
                select = "SELECT * FROM Customer WHERE (customerId = " + customerID + ");";
                result = statemnt.executeQuery(select);
                
                while(result.next()){
                    cust += " 	" + result.getInt("customerid") + ": "+ result.getString("l_name").replaceAll("\\s+","") + ", " + result.getString("f_name").replaceAll("\\s+ ","") 
                            + " - " + result.getString("city") + "\n";
                }
                
                select = "SELECT * FROM Cust_book NATURAL JOIN book WHERE (customerId = " + customerID +");";
                result = statemnt.executeQuery(select);
                
                while(result.next()){
                    book += " 	\n " + result.getInt("isbn") + " - " + result.getString("title");
                    customer++;             
                    }

                if(customer == 0){ borrowed = "\n (No books borrowed)"; }
                else{ borrowed = " 	Books Borrowed: " + customer; }

                statemnt.close();

            } catch (SQLException e) {
                return "ERROR cannot find books";
            }
        } catch (SQLException e) {
            return "ERROR no such Customer ID";
        }

        return cust + borrowed + book + " \n";
    }


    /**
     * Returns and prints all customers. 
     * @return "Show all customers: 
     * 			customerID: lname, fname - city"
     */
    public String showAllCustomers() {
	String allCustomers = "Show all customers: \n";
	
        try {
            String select = "SELECT * FROM customer;";
            Statement statemnt = connectn.createStatement();
            ResultSet result = statemnt.executeQuery(select);

            while(result.next()){
                allCustomers += "  	 " + result.getInt("customerid") + ": "+ result.getString("l_name").replaceAll("\\s+","") + ", " + result.getString("f_name").replaceAll("\\s+ ","") 
                + " - " + result.getString("city") + " \n";
            }

        } catch (SQLException e) {
            return "ERROR getting all customers";
        }
        
        return allCustomers;
    }

    
    /**
    * Allows the customer to borrow a book. 
    * @param isbn, customerID, day, month, year
    * @return "Borrow Book:
    * 		   Loaned to: customerID fname lname
    * 	       Due Date: "
    */
    public String borrowBook(int isbn, int customerID, int day, int month, int year) {
    	String borrow = "Borrow Book: \n";
		String message = " 	Customer unable to borrow book.";
		String finalResult = "";

		try {
			Statement statemnt1 = connectn.createStatement();
			Statement statemnt2 = connectn.createStatement();
			Statement statemnt3 = connectn.createStatement();
			
			String select1 = "SELECT numLeft FROM book WHERE (isbn = " + isbn + ") AND (numLeft > 0);";
			ResultSet result1  = statemnt1.executeQuery(select1);
			
			if(result1.next() != false) {
				String select2 = "SELECT customerid FROM cust_book WHERE (isbn = " + isbn + ") AND (customerid = " + customerID + ");";
				ResultSet result2  = statemnt2.executeQuery(select2);
				
				//checks if the result from the query of the isbn entered
				if(result2.next() != false ) 
					message = " 	Customer already borrowed this book: " + isbn;
				else { 
					message = " 	Borrow book successful.";
					updateBook(isbn, customerID, day, month, year);
				}
			}
			else message = " 	There are no copies left of this book: " + isbn;

			String select3 = "SELECT customerid FROM cust_book WHERE (isbn = " + isbn + ") AND (customerid = " + customerID + ");";
			ResultSet result3  = statemnt3.executeQuery(select3);
			result3 = statemnt3.executeQuery("SELECT * FROM cust_book WHERE (customerid = " + customerID + ");");
			
			while (result3.next()) {
			finalResult += "\n " + result3.getInt("isbn") + " - " + result3.getString("title") + "\nLoaned to: " + result3.getInt("customerid") + ": " + 
			result3.getString("l_name").replaceAll("\\s+","") + ", " + result3.getString("f_name").replaceAll("\\s+ ","") + "\nDue Date: " + result3.getDate(2);
			}
		}
		catch (SQLException sqlex){
			System.out.println("ERROR borrowing book");
		}

		return borrow + message + "\n 	Book: " + isbn + finalResult + "\n 	(borrow book information not shown for some reason)";
	}
    
    
    /**
     * Helper method, updatebook for borrow book. 
     * Updates the values from the queries and returns it.
     * @param isbn, customerID, day, month, year
     */
	public void updateBook(int isbn, int customerID, int day, int month, int year) {
		try {
			LocalDate date = LocalDate.of(year, month, day);

			Statement s = connectn.createStatement();
			Statement s2 = connectn.createStatement();
			String select1 = "INSERT INTO cust_book VALUES('" + isbn + "','" + date + "','" + customerID + "');";
			String select2 = "UPDATE book SET numleft = numleft-1 WHERE (isbn = " + isbn + ");";

			int result1 = s.executeUpdate(select1);
			int result2 = s2.executeUpdate(select2);

		}
		catch (SQLException sqlex){
			sqlex.printStackTrace();
		}
	}
	
	
    /**
    * Method allows the customer to return a book.
    * @param isbn, customerID
    * @return "Return Book: "
    */
    public String returnBook(int isbn, int customerID) {
    	String retrn = "Return Book: \n";
    	String output = "";
    	ResultSet result = null;

    	try {
    		Statement statemnt = connectn.createStatement();
    		String select1 = "SELECT * FROM customer WHERE (customerid = " + customerID + ");";
    		result = statemnt.executeQuery(select1);
    		
    		if(result.next() != false) {
    			if(result.next() != false) output = " 	isbn does not exist.";

    			else {
    				String select2 = "SELECT * FROM book WHERE (isbn = " + isbn + ");";
    				result = statemnt.executeQuery(select2);
    				
    				String select3 = "DELETE FROM cust_book WHERE (customerid = " + customerID + ");";
    				statemnt.executeUpdate(select3);
    				
    				String select4 = "UPDATE book SET numleft = numleft+1 WHERE (isbn = " + isbn + ");";
    				statemnt.executeUpdate(select4);
    		}
    	}
    		else output = " 	No more remaining copies of the book.";

    		String select5 = "SELECT * FROM cust_book WHERE (customerid = " + customerID + ");";
    		result = statemnt.executeQuery(select5);
    		
    		connectn.commit();
    	}
    	catch (SQLException sqlex){
    		System.out.println("ERROR occured");
    	}

    	return retrn + output + " \n" + " 	Book has been returned." ;
    }

    
    
    /**
    * Disconnects the database connection. 
    */
    public void closeDBConnection() {
        try {
            connectn.close();
        }
        catch (SQLException e) {
            System.out.println("Connection cannot be closed.");
        }
    }

    
    /**
    * Deletes the record of the customer.
    * @param customerID
    */
    public String deleteCus(int customerID) {
    	String retrn = "Delete Customer: \n \n";
    	String message = "";

		try {
			Statement statemnt1 = connectn.createStatement();
			String select1 = "SELECT * FROM cust_book WHERE (customerid = " + customerID + ");";
			ResultSet result = statemnt1.executeQuery(select1);

			if(result.next() != false) {
				message  = " 	The customer does not exist within this library.";
			}
			
			else {
				Statement statemnt2 = connectn.createStatement();
				String select2 = "DELETE  FROM customer WHERE (customerid = " + customerID + ");";
				int result2 = statemnt2.executeUpdate(select2);
				
				message = " 	The Customer has been removed from the database.";
			}
		}
		catch (SQLException sqlex){
			sqlex.printStackTrace();
		}
    	return retrn + message;
    }
    
    
    /**
     * Deletes the record of the Author.
     * @param authorID
     */
    public String deleteAuthor(int authorID) {
    	String retrn = "Delete Author: \n \n";
    	String message = "";
    	
		try {
			Statement statemnt1 = connectn.createStatement();
			String select1 = "SELECT * FROM author WHERE (authorid = " + authorID + ");";
			ResultSet result1 = statemnt1.executeQuery(select1);

			if(result1.next() == false) {
				message  = " 	The author does not exist within this library.";
			}

			else {
				Statement statemnt2 = connectn.createStatement();
				String select2 = "DELETE FROM author WHERE (authorid = " + authorID + ");";
				int result2 = statemnt2.executeUpdate(select2);

				message = " 	The Author has been removed from this library.";
			}
		}
		catch (SQLException sqlex){
			sqlex.printStackTrace();
		}

		return retrn + message;
    }

    
    /**
     * Delete the entire book and all its copies from the library.
     * @param isbn
     */
    public String deleteBook(int isbn) {
    	String retrn = "Delete Book: \n \n";
    	
    	return retrn;
    }
}
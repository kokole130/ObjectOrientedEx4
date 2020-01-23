package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Server.game_service;

/**
 * This class represents the player analysis of the results in the automatic game.
 * @author Yogev
 *
 */
public class DialogSQL{

	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	public int moves[];//array for know the allowance of moves possible in each scenario.
	
	/**
	 * Default constructor that initializing the moves limit each scenario.
	 */
	public DialogSQL() {
		moves=new int[24];
		moves[0]=290;
		moves[1]=580;
		moves[3]=580;
		moves[5]=500;
		moves[9]=580;
		moves[11]=580;
		moves[13]=580;
		moves[16]=290;
		moves[19]=580;
		moves[20]=290;
		moves[23]=1140;

	}
	
	/**
	 * Function that create a dialog with the user, and returns the number of game played with this ID.
	 */
	public void AmountOfPlay() {
		JFrame frame=new JFrame();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID="+205966781;//select our ID from the table

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int ind =0;
			while(resultSet.next()) {
				ind++;//counting the number of games.
			}
			resultSet.close();
			statement.close();		
			connection.close();	

			JOptionPane.showMessageDialog(frame, "played "+ind+" games");
		}


		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function that create a dialog with the user, and return the current level of this ID.
	 */
	public void currentLevel() {
		JFrame frame=new JFrame();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID="+205966781;//select my ID from the table

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int level =0;
			while(resultSet.next()) {
				level=resultSet.getInt("levelID");//takes the current level till the end of my ID. (this is the highest level).
			}

			resultSet.close();
			statement.close();		
			connection.close();	

			JOptionPane.showMessageDialog(frame, "the current level is "+level);
		}


		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function that create a dialog with the user, ask him for choose a scenario, and accordingly
	 * return the best score of my ID in the specific scenario.
	 * @return
	 */
	public int bestScore() {
		JFrame frame=new JFrame();
		int max=0;

		try {

			String choose;
			String []scenario= {"0","1","3","5","9","11","13"
					,"16","19","20","23"};				
			choose = (String) JOptionPane.showInputDialog(frame,"Choose a map",
					"Scenario",JOptionPane.QUESTION_MESSAGE,null,scenario,scenario[0]);

			int x=Integer.parseInt(choose);

			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID="+205966781+" AND levelID="+x;
			/*select the userID column of my ID and the specific levelID column */

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int bestScore =0;
			int move=0;
			while(resultSet.next()) {
				bestScore=resultSet.getInt("score");
				move=resultSet.getInt("moves");
				if(bestScore>max&&move<=moves[x]) {/*take the maximum score each iteration, in move limit condition
					of the specific scenario*/
					max=bestScore;
				}
			}

			resultSet.close();
			statement.close();		
			connection.close();	

			JOptionPane.showMessageDialog(frame, "The best score in level "+x+" is "+max);
		}


		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return max;

	}

	/**
	 * Function that create a dialog with the user, ask him for choose a scenario, and accordingly
	 * return the correct position of my ID in the specific scenario out of all the class.
	 */
	public void PosInClass() {
		JFrame frame2=new JFrame();
		try {

			String choose;
			String []scenario= {"0","1","3","5","9","11","13"
					,"16","19","20","23"};				
			choose = (String) JOptionPane.showInputDialog(frame2,"Choose a map now",
					"Scenario",JOptionPane.QUESTION_MESSAGE,null,scenario,scenario[0]);

			int x=Integer.parseInt(choose);

			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where levelID="+x +" ORDER BY UserID,score";
			/*select all users in scenario x and sort them first by userID and then by score
			 * for to designate each ID*/

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int index=0;
			int myIndex=0;
			int id=0;
			int lastid=0;
			int move=0;
			int score=0;
			int bestscore=this.bestScoreForMe(x);
			while(resultSet.next()) {
				id=resultSet.getInt("userID");
				if(id!=lastid) {
					index++;//just when the lastid different from id , add 1 to the count.
				}
				if(id==205966781) {/*when this is my id - check if the moves is lower than the limit
									and if the current score is lower than the new score we take*/
					move=resultSet.getInt("moves");
					score=resultSet.getInt("score");
					if(move<=moves[x]&&score==bestscore) {
						myIndex=index;
					}
				}
				lastid=id;
			}
			int pos=index-myIndex;//the subtract is our position out of the class

			resultSet.close();
			statement.close();		
			connection.close();	

			JOptionPane.showMessageDialog(frame2, "My Position At Level "+x+" is "+pos+" out of "+index);
		}


		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Side function that return our best score in specific scenario.
	 * @param map - scenario number
	 * @return
	 */
	private int bestScoreForMe(int map) {
		int max=0;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID="+205966781+" AND levelID="+map;

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int bestScore =0;
			int move=0;
			while(resultSet.next()) {
				bestScore=resultSet.getInt("score");
				move=resultSet.getInt("moves");
				if(bestScore>max&&move<=moves[map]) {
					max=bestScore;
				}
			}

			resultSet.close();
			statement.close();		
			connection.close();	

		}


		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return max;
	}
}



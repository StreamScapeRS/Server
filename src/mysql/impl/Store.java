//package mysql.impl;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//import com.StreamScape.world.World;
//import com.StreamScape.world.entity.impl.player.Player;
//
///**
// * Using this class: To call this class, it's best to make a new thread. You can
// * do it below like so: new Thread(new Donation(player)).start();
// */
//public class Store implements Runnable {
//
//	public static final String HOST = "StreamScape.com"; // website ip address
//	public static final String USER = "Streamsc_store";
//	public static final String PASS = "Q@!fB5*M8Qk1";
//	public static final String DATABASE = "Streamsc_store";
//
//	private Player player;
//	private Connection conn;
//	private Statement stmt;
//
//	/**
//	 * The constructor
//	 *
//	 * @param player
//	 */
//	public Store(Player player) {
//		this.player = player;
//	}
//
//	@Override
//	public void run() {
//
//		try {
//
//			HttpResponse<String> response = Unirest.get("http://StreamScape.com/store/claim.php")
//					.queryString("username", player.getUsername()).asString();
//
//			String string = response.getBody();
//			int amount = Integer.parseInt(string);
//
//			if (amount > 0) {
//				int bonus = 0;
//				if (amount >= 20 && amount < 50) {
//					bonus = (int) (amount * 0.1);
//				} else if (amount >= 50 && amount < 100) {
//					bonus = (int) (amount * 0.15);
//				} else if (amount >= 100) {
//					bonus = (int) (amount * 0.2);
//				} else {
//					bonus = 0;
//				}
//				if (bonus > 0) {
//					player.getPointsManager().setWithIncrease("donation", (int) (amount + (float) Math.round(bonus)));
//				} else {
//					player.getPointsManager().setWithIncrease("donation", amount);
//				}
//
//				World.sendMessage(
//						"@bla@" + player.getUsername() + " @red@has claimed @bla@" + amount + " @red@donation points!");
//			} else {
//				player.getPacketSender().sendMessage("@red@There are no donations to claim at the moment.");
//			}
//
//		} catch (UnirestException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 *
//	 * @param host
//	 *            the host ip address or url
//	 * @param database
//	 *            the name of the database
//	 * @param user
//	 *            the user attached to the database
//	 * @param pass
//	 *            the users password
//	 * @return true if connected
//	 */
//	public boolean connect(String host, String database, String user, String pass) {
//		try {
//			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
//			return true;
//		} catch (SQLException e) {
//			System.out.println("Failing connecting to database!");
//			return false;
//		}
//	}
//
//	/**
//	 * Disconnects from the MySQL server and destroy the connection and statement
//	 * instances
//	 */
//	public void destroy() {
//		try {
//			conn.close();
//			conn = null;
//			if (stmt != null) {
//				stmt.close();
//				stmt = null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Executes an update query on the database
//	 *
//	 * @param query
//	 * @see {@link Statement#executeUpdate}
//	 */
//	public int executeUpdate(String query) {
//		try {
//			this.stmt = this.conn.createStatement(1005, 1008);
//			int results = stmt.executeUpdate(query);
//			return results;
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//		}
//		return -1;
//	}
//
//	/**
//	 * Executres a query on the database
//	 *
//	 * @param query
//	 * @see {@link Statement#executeQuery(String)}
//	 * @return the results, never null
//	 */
//	public ResultSet executeQuery(String query) {
//		try {
//			this.stmt = this.conn.createStatement(1005, 1008);
//			ResultSet results = stmt.executeQuery(query);
//			return results;
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//		}
//		return null;
//	}
//}
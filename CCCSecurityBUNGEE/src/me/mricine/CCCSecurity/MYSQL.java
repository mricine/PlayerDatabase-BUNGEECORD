package me.mricine.CCCSecurity;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MYSQL {
	

	private String HOST = "";
	  
	  private String DATABASE = "";
	  
	  private String USER = "";
	  
	  private String PASSWORD = "";
	  
	  private java.sql.Connection con;
	  
	  public MYSQL(String host, String database, String user, String password) {
	    this.HOST = host;
	    this.DATABASE = database;
	    this.USER = user;
	    this.PASSWORD = password;
	    connect();
	  }
	  
	  public void connect() {
	    try {
	      this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", 
	          this.USER, this.PASSWORD);
	      System.out.println("§aMYSQL: §cConnesso con successo!");
	    } catch (SQLException e) {
	    	System.out.println("§cMYSQL ERRORE: c'è stato un problema:");
	      System.out.println(e.getMessage());
	    } 
	  }
	  
	  public void close() {
	    try {
	      if (this.con != null) {
	        this.con.close();
	        System.out.println("§aMYSQL: §fChiuso con successo!");
	      } 
	    } catch (SQLException e) {
	    	System.out.println("§cMYSQL ERRORE: c'è stato un problema:");
		      System.out.println(e.getMessage());
	    } 
	  }
	  
	  public void update(String qry) {
	    try {
	      java.sql.Statement st = this.con.createStatement();
	      st.executeUpdate(qry);
	      st.close();
	    } catch (SQLException e) {
	      connect();
	      System.err.println(e);
	    } 
	  }
	  
	  public ResultSet query(String qry) {
	    ResultSet rs = null;
	    try {
	      java.sql.Statement st = this.con.createStatement();
	      rs = st.executeQuery(qry);
	    } catch (SQLException e) {
	      connect();
	      System.err.println(e);
	    } 
	    return rs;
	  }//PLAYER varchar(64), UUID varchar(64), IP varchar(15), IPCHANGE boolean
	  public static boolean playerExist(String p) {
	    try {
	      ResultSet rs = Princ.mysql.query("SELECT * FROM CCCDatabase WHERE PLAYER= '" + p + "'");
	      if (rs.next())
	        return (rs.getString("PLAYER") != null); 
	      return false;
	    } catch (SQLException e) {
	      e.printStackTrace();
	      return false;
	    } 
	  }
	  public static void createPlayer(ProxiedPlayer p) {
		  if(!playerExist(p.getName())) {
		    	Princ.mysql.update("INSERT INTO CCCDatabase (PLAYER, UUID, IP, IPCHANGE, NEWIP) VALUES ('" + p.getName() + 
		          "', '"+p.getUniqueId().toString()+"', '"+p.getAddress().getAddress().getHostAddress().toString()+"', "+false+", '');");
		  }else {
			  setIpChanged(p);
		  }
		  }
	  public static String getUUID(String p) {
		  String i = "";
	      try {
	        ResultSet rs = Princ.mysql.query("SELECT * FROM CCCDatabase WHERE PLAYER= '" + p + "'");
	        if (!rs.next() || String.valueOf(rs.getString("UUID")) == null);
	        i = String.valueOf(rs.getString("UUID"));
	      } catch (SQLException e) {
	        e.printStackTrace();
	      } 
	    return i;
	  }
	  public static String getIP(String p) {
		  String i = "";
	      try {
	        ResultSet rs = Princ.mysql.query("SELECT * FROM CCCDatabase WHERE PLAYER= '" + p + "'");
	        if (!rs.next() || String.valueOf(rs.getString("IP")) == null);
	        i = String.valueOf(rs.getString("IP"));
	      } catch (SQLException e) {
	        e.printStackTrace();
	      } 
	    return i;
	  }//p.getAddress().getAddress().getHostAddress().toString()     NEWIP
	  public static void setIP(String p, String address) {
		    	Princ.mysql.update("UPDATE CCCDatabase SET IP= '" +address+  "' WHERE UUID= '" + p + "';");
	  }
	  public static void setIpChanged(ProxiedPlayer p) {
		  if(p.getAddress().getAddress().getHostAddress().toString().equalsIgnoreCase(getIP(p.getName()))) {
	    	Princ.mysql.update("UPDATE CCCDatabase SET IPCHANGE= '" +0+  "' WHERE PLAYER= '" + p.getName() + "';");
		  }else {
			  Princ.mysql.update("UPDATE CCCDatabase SET IPCHANGE= '" +1+  "' WHERE PLAYER= '" + p.getName() + "';");
			  setNEWIP(p);
		  }
	  }
	  public static void setNEWIP(ProxiedPlayer p) {
			  Princ.mysql.update("UPDATE CCCDatabase SET NEWIP= '" +p.getAddress().getAddress().getHostAddress().toString()+  "' WHERE PLAYER= '" + p.getName() + "';");
	  }
}

package me.mricine.CCCSecurity;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Princ extends Plugin implements Listener{
	
	public static MYSQL mysql;
	
	public void onEnable() {
		this.getProxy().getPluginManager().registerListener(this, this);
		ConnectMySQL();
	}
	
	
	@SuppressWarnings("deprecation")
	public void ConnectMySQL() {
	    String ip = "192.168.1.77";
	    String database = "cccnetwork";
	    String name = "root";
	    String password = "ma122296";
	    try {
	      mysql = new MYSQL(ip, database, name, password);
	      mysql.update("CREATE TABLE IF NOT EXISTS CCCDatabase(PLAYER varchar(64), UUID varchar(64), IP varchar(15), IPCHANGE boolean, NEWIP varchar(15));");
	      getProxy().getConsole().sendMessage("activated");
	    } catch (Exception error) {
	    	 getProxy().getConsole().sendMessage("§cMYSQL ERROR: §fImpossibile connettersi");
	    	 this.onDisable();
	    } 
	  }
	@EventHandler
	public void onJoin(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();
		MYSQL.createPlayer(p);
	}
	
}
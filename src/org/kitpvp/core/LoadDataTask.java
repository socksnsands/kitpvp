package org.kitpvp.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitpvp.loadout.Loadout;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;


public class LoadDataTask extends BukkitRunnable {

	private String uuid;
	
	public LoadDataTask(String uuid){
		this.uuid = uuid;
	}
	
	public void run(){
		try{
			PreparedStatement statement = Core.getInstance().getConnection().prepareStatement(
					"SELECT * FROM `users` WHERE `uuid` = ?"
			);
			statement.setString(1, uuid);
			ResultSet resultSet = statement.executeQuery();
			boolean isLoaded = resultSet.next();
			if(isLoaded){
				User user = Core.getInstance().getUserManager().getUser(uuid);
				if(user == null){
					user = new User(uuid);
					Core.getInstance().getUserManager().addUser(user);
				}
				String rank = resultSet.getString("rank");
				String loadouts = resultSet.getString("kit");
				int money = resultSet.getInt("money");
				boolean g = false;
				for(Rank r : Rank.values()){
					if(r.toString().toUpperCase().equals(rank)){
						g = true;
					}
				}
				if(g)
					user.setRank(Rank.valueOf(rank.toUpperCase()));

				String abilities = resultSet.getString("abilities");
				user.readAndSaveAbilitiesString(abilities);
				String series = resultSet.getString("series");
				user.readAndSaveSeriesString(series);
				user.setMoney(money);
				
				
				//must load loadouts last (checks if user has abilities)
				if(loadouts != null && !loadouts.equals("")){
					for(Loadout loadout : user.readAllLoadoutStrings(loadouts)){
						user.addLoadout(loadout);
					}
				}
				
			}else{
				PreparedStatement ps = Core.getInstance().getConnection().prepareStatement(
						"INSERT INTO `users` (`uuid`, `rank`, `kit`, `abilities`, `series`, `money`) VALUES (?, ?, ?, ?, ?, ?)"
				);
				ps.setString(1, uuid);
				ps.setString(2, "DEFAULT");
				ps.setString(3, "");
				ps.setString(4, "");
				ps.setString(5, "");
				ps.setInt(6, 0);
				ps.executeUpdate();
				ps.close();
			}
			resultSet.close();
			statement.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}

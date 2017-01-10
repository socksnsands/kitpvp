package org.kitpvp.core;

import java.sql.PreparedStatement;

import org.bukkit.scheduler.BukkitRunnable;
import org.kitpvp.loadout.Loadout;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

public class PushDataTask extends BukkitRunnable {

	private String uuid;

	public PushDataTask(String uuid) {
		this.uuid = uuid;
	}

	public void run() {
		try {
			User user = Core.getInstance().getUserManager().getUser(uuid);
			
			PreparedStatement s = Core.getInstance().getConnection().prepareStatement(
					"UPDATE `users` SET `rank` = ?, `kit` = ?, `abilities` = ?, `series` = ?, `money` = ?, `specialty` = ? WHERE `uuid` = ?");
			s.setString(7, uuid);
			if(user.getRank() != null)
				s.setString(1, user.getRank().toString());
			else
				s.setString(1, Rank.DEFAULT.toString());
			String loadoutsString = "";
			for (Loadout loadout : user.getLoadouts()) {
				loadoutsString += loadout.toDBString();
				if (!user.getLoadouts().get(user.getLoadouts().size() - 1).equals(loadout)) {
					loadoutsString += "_";
				}
			}
			s.setString(2, loadoutsString);
			s.setString(3, user.encodeAllAbilities());
			s.setString(4, user.encodeAllSeries());
			s.setInt(5, user.getBalance());
			s.setString(6, user.getSpecialty().toString());
			s.executeUpdate();
			s.close();
			
			Core.getInstance().getUserManager().removeUser(user);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

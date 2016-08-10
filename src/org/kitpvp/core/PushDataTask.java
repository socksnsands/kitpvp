package org.kitpvp.core;

import java.sql.PreparedStatement;

import org.bukkit.entity.Player;
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
			PreparedStatement s = Core.getInstance().getConnection().prepareStatement(
					"UPDATE `users` SET `uuid` = ?, `rank` = ?, `kit` = ?, `abilities` = ?, `series` = ?, `money` = ?");
			User user = Core.getInstance().getUserManager().getUser(uuid);
			s.setString(1, uuid);
			if(user.getRank() != null)
				s.setString(2, user.getRank().toString());
			else
				s.setString(2, Rank.DEFAULT.toString());
			String loadoutsString = "";
			for (Loadout loadout : user.getLoadouts()) {
				loadoutsString += loadout.toDBString();
				if (!user.getLoadouts().get(user.getLoadouts().size() - 1).equals(loadout)) {
					loadoutsString += "_";
				}
			}
			s.setString(3, loadoutsString);
			// TODO push series and abilities
			s.setString(4, user.encodeAllAbilities());
			s.setString(5, user.encodeAllSeries());
			s.setInt(6, user.getBalance());
			s.executeUpdate();
			s.close();

			Core.getInstance().getUserManager().removeUser(user);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

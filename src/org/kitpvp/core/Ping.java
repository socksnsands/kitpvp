package org.kitpvp.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.entity.Player;

public class Ping {
	
	
	public static int getPing(Player p) {
		int ping = new Random(50L).nextInt();
		try {
			Method getHandleMethod = p.getClass().getDeclaredMethod("getHandle", new Class[0]);
			getHandleMethod.setAccessible(true);
			Object nmsplayer = getHandleMethod.invoke(p, new Object[0]);
			Field pingField = nmsplayer.getClass().getDeclaredField("ping");
			pingField.setAccessible(true);
			ping = pingField.getInt(nmsplayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ping;
	}

}

package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

public class EntityList {
	private static Map stringToClassMapping = new HashMap();
	private static Map classToStringMapping = new HashMap();
	private static Map IDtoClassMapping = new HashMap();
	private static Map classToIDMapping = new HashMap();

	private static void addMapping(Class var0, EntitySupplier var1, String var2, int var3) {
		stringToClassMapping.put(var2, var1);
		classToStringMapping.put(var0, var2);
		IDtoClassMapping.put(Integer.valueOf(var3), var1);
		classToIDMapping.put(var0, Integer.valueOf(var3));
	}

	public static Entity createEntityInWorld(String var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)stringToClassMapping.get(var0);
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		return var2;
	}

	public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)stringToClassMapping.get(var0.getString("id"));
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if(var2 != null) {
			var2.readFromNBT(var0);
		} else {
			System.out.println("Skipping Entity with id " + var0.getString("id"));
		}

		return var2;
	}

	public static Entity createEntity(int var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)IDtoClassMapping.get(Integer.valueOf(var0));
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if(var2 == null) {
			System.out.println("Skipping Entity with id " + var0);
		}

		return var2;
	}

	public static int getEntityID(Entity var0) {
		return ((Integer)classToIDMapping.get(var0.getClass())).intValue();
	}

	public static String getEntityString(Entity var0) {
		return (String)classToStringMapping.get(var0.getClass());
	}

	static {
		addMapping(EntityItem.class, EntityItem::new, "Item", 1);
		addMapping(EntityXPOrb.class, EntityXPOrb::new, "XPOrb", 2);
		addMapping(EntityArrow.class, EntityArrow::new, "Arrow", 10);
		addMapping(EntitySnowball.class, EntitySnowball::new, "Snowball", 11);
		addMapping(EntityPainting.class, EntityPainting::new, "Painting", 9);
		addMapping(EntityLiving.class, EntityLiving::new, "Mob", 48);
		addMapping(EntityMob.class, EntityMob::new, "Monster", 49);
		addMapping(EntityCreeper.class, EntityCreeper::new, "Creeper", 50);
		addMapping(EntitySkeleton.class, EntitySkeleton::new, "Skeleton", 51);
		addMapping(EntitySpider.class, EntitySpider::new, "Spider", 52);
		addMapping(EntityGiantZombie.class, EntityGiantZombie::new, "Giant", 53);
		addMapping(EntityZombie.class, EntityZombie::new, "Zombie", 54);
		addMapping(EntitySlime.class, EntitySlime::new, "Slime", 55);
		addMapping(EntityGhast.class, EntityGhast::new, "Ghast", 56);
		addMapping(EntityPigZombie.class, EntityPigZombie::new, "PigZombie", 57);
		addMapping(EntityEnderman.class, EntityEnderman::new, "Enderman", 58);
		addMapping(EntityCaveSpider.class, EntityCaveSpider::new, "CaveSpider", 59);
		addMapping(EntitySilverfish.class, EntitySilverfish::new, "Silverfish", 60);
		addMapping(EntityPig.class, EntityPig::new, "Pig", 90);
		addMapping(EntitySheep.class, EntitySheep::new, "Sheep", 91);
		addMapping(EntityCow.class, EntityCow::new, "Cow", 92);
		addMapping(EntityChicken.class, EntityChicken::new, "Chicken", 93);
		addMapping(EntitySquid.class, EntitySquid::new, "Squid", 94);
		addMapping(EntityWolf.class, EntityWolf::new, "Wolf", 95);
		addMapping(EntityTNTPrimed.class, EntityTNTPrimed::new, "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, EntityFallingSand::new, "FallingSand", 21);
		addMapping(EntityMinecart.class, EntityMinecart::new, "Minecart", 40);
		addMapping(EntityBoat.class, EntityBoat::new,"Boat", 41);
	}
}

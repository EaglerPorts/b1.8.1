package net.minecraft.src;

public class BiomeGenHell extends BiomeGenBase {
	public BiomeGenHell(int var1) {
		super(var1);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast::new, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie::new, 10, 4, 4));
	}
}

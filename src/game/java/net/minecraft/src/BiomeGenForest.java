package net.minecraft.src;

import net.lax1dude.eaglercraft.Random;

public class BiomeGenForest extends BiomeGenBase {
	public BiomeGenForest(int var1) {
		super(var1);
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf::new, 5, 4, 4));
		this.field_35488_u.field_35911_r = 10;
		this.field_35488_u.field_35909_t = 2;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random var1) {
		return (WorldGenerator)(var1.nextInt(5) == 0 ? this.field_35481_B : (var1.nextInt(10) == 0 ? this.field_35480_A : this.field_35493_z));
	}
}

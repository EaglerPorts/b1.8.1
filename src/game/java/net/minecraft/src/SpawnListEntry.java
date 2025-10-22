package net.minecraft.src;

import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

public class SpawnListEntry extends WeightedRandomChoice {
	public EntitySupplier entityClass;
	public int field_35591_b;
	public int field_35592_c;

	public SpawnListEntry(EntitySupplier var1, int var2, int var3, int var4) {
		super(var2);
		this.entityClass = var1;
		this.field_35591_b = var3;
		this.field_35592_c = var4;
	}
}

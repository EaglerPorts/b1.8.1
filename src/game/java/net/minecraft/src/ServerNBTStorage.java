package net.minecraft.src;

public class ServerNBTStorage {
	public String field_35795_a;
	public String field_35793_b;
	public String field_35794_c;
	public String field_35791_d;
	public long field_35792_e;
	public boolean field_35790_f = false;
	public boolean isDefault = false;
	public boolean hideAddress = false;

	public ServerNBTStorage(String var1, String var2, boolean var3) {
		this.field_35795_a = var1;
		this.field_35793_b = var2;
		this.hideAddress = var3;
	}

	public ServerNBTStorage(String var1, String var2) {
		this(var1, var2, false);
	}

	public NBTTagCompound func_35789_a() {
		NBTTagCompound var1 = new NBTTagCompound();
		var1.setString("name", this.field_35795_a);
		var1.setString("ip", this.field_35793_b);
		var1.setBoolean("hideAddr", this.hideAddress);
		return var1;
	}

	public static ServerNBTStorage func_35788_a(NBTTagCompound var0) {
		return new ServerNBTStorage(var0.getString("name"), var0.getString("ip"), var0.getBoolean("hideAddr"));
	}
}

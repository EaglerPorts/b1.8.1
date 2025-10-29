package net.minecraft.src;

import java.math.BigInteger;

import cbare.md5.MD5;

public class MD5String {
  private static final MD5 md5 = new MD5();

	private String field_27370_a;

	public MD5String(String var1) {
		this.field_27370_a = var1;
	}

	public String func_27369_a(String var1) {
    md5.reset();
    String var2 = this.field_27370_a + var1;
    md5.update(var2.getBytes(), 0, var2.length());
    return (new BigInteger(1, md5.doFinal())).toString(16);
	}
}
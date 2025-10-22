package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpider var1) {
		return 180.0F;
	}

	protected boolean setSpiderEyeBrightness(EntitySpider var1, int var2, float var3) {
		if(var2 != 0) {
			return false;
		} else if(var2 != 0) {
			return false;
		} else {
			this.loadTexture("/mob/spider_eyes.png");
			float var4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return true;
		}
	}

	protected void func_35446_a(EntitySpider var1, float var2) {
		float var3 = var1.func_35188_k_();
		GL11.glScalef(var3, var3, var3);
	}

	protected void preRenderCallback(EntityLiving var1, float var2) {
		this.func_35446_a((EntitySpider)var1, var2);
	}

	protected float getDeathMaxRotation(EntityLiving var1) {
		return this.setSpiderDeathMaxRotation((EntitySpider)var1);
	}

	protected boolean shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.setSpiderEyeBrightness((EntitySpider)var1, var2, var3);
	}
}

package com.fuzs.swordblockingmechanics.util;

import com.fuzs.swordblockingmechanics.SwordBlockingMechanics;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.AttackIndicatorStatus;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.function.BiConsumer;

@SuppressWarnings("deprecation")
public class AttackIndicatorHelper {

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(SwordBlockingMechanics.MODID, "textures/gui/icons.png");
    
    private static AttackIndicatorStatus attackIndicator;

    public static AttackIndicatorStatus getActiveIndicator(RenderGameOverlayEvent.ElementType elementType, AttackIndicatorStatus indicatorSetting) {

        if (elementType == RenderGameOverlayEvent.ElementType.CROSSHAIRS && indicatorSetting == AttackIndicatorStatus.CROSSHAIR) {

            return AttackIndicatorStatus.CROSSHAIR;
        }

        if (elementType == RenderGameOverlayEvent.ElementType.HOTBAR && indicatorSetting == AttackIndicatorStatus.HOTBAR) {

            return AttackIndicatorStatus.HOTBAR;
        }

        return AttackIndicatorStatus.OFF;
    }

    public static void renderCrosshairIndicator(boolean isPreRendering, BiConsumer<Integer, Integer> drawIcon) {

        renderShieldIndicator(isPreRendering, () -> renderCrosshairIcon(drawIcon));
    }

    public static void renderHotbarIndicator(boolean isPreRendering, BiConsumer<Integer, Integer> drawIcon) {

        renderShieldIndicator(isPreRendering, () -> renderHotbarIcon(drawIcon));
    }

    private static void renderShieldIndicator(boolean isPreRendering, Runnable renderIcon) {

        if (isPreRendering) {

            disableAttackIndicator();
        } else {

            resetAttackIndicator();
            renderIcon.run();
        }
    }

    private static void renderCrosshairIcon(BiConsumer<Integer, Integer> drawIcon) {

        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        int width = mc.getMainWindow().getScaledWidth() / 2 - 8;
        int height = mc.getMainWindow().getScaledHeight() / 2 - 7 + 16;
        drawIcon.accept(width, height);
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }

    @SuppressWarnings("ConstantConditions")
    private static void renderHotbarIcon(BiConsumer<Integer, Integer> drawIcon) {

        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        int width = mc.getMainWindow().getScaledWidth() / 2;
        int height = mc.getMainWindow().getScaledHeight() - 20;
        width = mc.player.getPrimaryHand().opposite() == HandSide.RIGHT ? width - 91 - 22 : width + 91 + 6;
        drawIcon.accept(width, height);
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableBlend();
    }

    public static void disableAttackIndicator() {

        Minecraft mc = Minecraft.getInstance();
        if (mc.gameSettings.attackIndicator != AttackIndicatorStatus.OFF && attackIndicator == null) {

            attackIndicator = mc.gameSettings.attackIndicator;
            mc.gameSettings.attackIndicator = AttackIndicatorStatus.OFF;
        }
    }

    public static void resetAttackIndicator() {

        if (attackIndicator != null) {

            Minecraft.getInstance().gameSettings.attackIndicator = attackIndicator;
            attackIndicator = null;
        }
    }
    
}

/*
 * This file ("GuiFurnaceDouble.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.inventory.gui;

import de.ellpeck.actuallyadditions.mod.inventory.ContainerFurnaceDouble;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityBase;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityFurnaceDouble;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiFurnaceDouble extends GuiContainer{

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("guiFurnaceDouble");
    private final TileEntityFurnaceDouble tileFurnace;

    public GuiFurnaceDouble(InventoryPlayer inventory, TileEntityBase tile){
        super(new ContainerFurnaceDouble(inventory, tile));
        this.tileFurnace = (TileEntityFurnaceDouble)tile;
        this.xSize = 176;
        this.ySize = 93+86;
    }

    @Override
    public void drawScreen(int x, int y, float f){
        super.drawScreen(x, y, f);
        String text = this.tileFurnace.storage.getEnergyStored()+"/"+this.tileFurnace.storage.getMaxEnergyStored()+" RF";
        if(x >= this.guiLeft+28 && y >= this.guiTop+6 && x <= this.guiLeft+43 && y <= this.guiTop+88){
            this.drawHoveringText(Collections.singletonList(text), x, y);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y){
        AssetUtil.displayNameString(this.fontRendererObj, this.xSize, -10, this.tileFurnace.getName());
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int x, int y){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(AssetUtil.GUI_INVENTORY_LOCATION);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop+93, 0, 0, 176, 86);

        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);

        if(this.tileFurnace.storage.getEnergyStored() > 0){
            int i = this.tileFurnace.getEnergyScaled(83);
            this.drawTexturedModalRect(this.guiLeft+28, this.guiTop+89-i, 176, 44, 16, i);
        }
        if(this.tileFurnace.firstSmeltTime > 0){
            int i = this.tileFurnace.getFirstTimeToScale(23);
            this.drawTexturedModalRect(this.guiLeft+51, this.guiTop+40, 176, 0, 24, i);
        }
        if(this.tileFurnace.secondSmeltTime > 0){
            int i = this.tileFurnace.getSecondTimeToScale(23);
            this.drawTexturedModalRect(this.guiLeft+101, this.guiTop+40, 176, 22, 24, i);
        }
    }
}
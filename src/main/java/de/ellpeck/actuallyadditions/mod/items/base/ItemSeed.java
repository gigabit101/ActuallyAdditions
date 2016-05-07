/*
 * This file ("ItemSeed.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items.base;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.blocks.base.BlockPlant;
import de.ellpeck.actuallyadditions.mod.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ItemSeed extends ItemSeeds{

    public Block plant;
    public String name;
    public String oredictName;

    public ItemSeed(String name, String oredictName, Block plant, Item returnItem, int returnMeta){
        super(plant, Blocks.FARMLAND);
        this.name = name;
        this.oredictName = oredictName;
        this.plant = plant;

        if(plant instanceof BlockPlant){
            ((BlockPlant)this.plant).seedItem = this;
            ((BlockPlant)this.plant).returnItem = returnItem;
            ((BlockPlant)this.plant).returnMeta = returnMeta;
        }

        this.register();
    }

    private void register(){
        ItemUtil.registerItem(this, this.getBaseName(), this.shouldAddCreative());

        this.registerRendering();
    }

    protected String getBaseName(){
        return this.name;
    }

    public boolean shouldAddCreative(){
        return true;
    }

    protected void registerRendering(){
        ActuallyAdditions.proxy.addRenderRegister(new ItemStack(this), new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos){
        return this.plant.getDefaultState();
    }
}
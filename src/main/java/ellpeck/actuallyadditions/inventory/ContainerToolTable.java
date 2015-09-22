/*
 * This file ("ContainerToolTable.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://github.com/Ellpeck/ActuallyAdditions/blob/master/README.md
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * � 2015 Ellpeck
 */

package ellpeck.actuallyadditions.inventory;

import ellpeck.actuallyadditions.inventory.slot.SlotOutput;
import ellpeck.actuallyadditions.recipe.ToolTableHandler;
import ellpeck.actuallyadditions.tile.TileEntityBase;
import ellpeck.actuallyadditions.tile.TileEntityToolTable;
import ellpeck.actuallyadditions.util.ItemUtil;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@InventoryContainer
public class ContainerToolTable extends Container{

    private TileEntityToolTable table;

    public ContainerToolTable(InventoryPlayer inventory, TileEntityBase tile){
        this.table = (TileEntityToolTable)tile;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 2; j++){
                this.addSlotToContainer(new Slot(this.table, j+i*2, 35+j*18, 7+i*18){
                    @Override
                    public void onSlotChanged(){
                        if(this.inventory instanceof TileEntityToolTable){
                            TileEntityToolTable table = (TileEntityToolTable)this.inventory;
                            ItemStack stack = ToolTableHandler.getResultFromSlots(table.slots);
                            table.slots[TileEntityToolTable.SLOT_OUTPUT] = stack == null ? null : stack.copy();
                        }
                        super.onSlotChanged();
                    }
                });
            }
        }

        this.addSlotToContainer(new SlotOutput(this.table, TileEntityToolTable.SLOT_OUTPUT, 115, 25){
            @Override
            public void onPickupFromSlot(EntityPlayer player, ItemStack stack){
                if(this.inventory instanceof TileEntityToolTable){
                    TileEntityToolTable table = (TileEntityToolTable)this.inventory;
                    ToolTableHandler.Recipe recipe = ToolTableHandler.getRecipeFromSlots(table.slots);
                    for(int i = 0; i < TileEntityToolTable.INPUT_SLOT_AMOUNT; i++){
                        if(ItemUtil.contains(recipe.itemsNeeded, table.getStackInSlot(i))){
                            table.decrStackSize(i, 1);
                        }
                    }

                    ItemStack newOutput = ToolTableHandler.getResultFromSlots(table.slots);
                    table.slots[TileEntityToolTable.SLOT_OUTPUT] = newOutput == null ? null : newOutput.copy();
                }
                super.onPickupFromSlot(player, stack);
            }
        });

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                this.addSlotToContainer(new Slot(inventory, j+i*9+9, 8+j*18, 69+i*18));
            }
        }
        for(int i = 0; i < 9; i++){
            this.addSlotToContainer(new Slot(inventory, i, 8+i*18, 127));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player){
        return this.table.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        final int inventoryStart = 7;
        final int inventoryEnd = inventoryStart+26;
        final int hotbarStart = inventoryEnd+1;
        final int hotbarEnd = hotbarStart+8;

        Slot theSlot = (Slot)this.inventorySlots.get(slot);

        if (theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            //Slots in Inventory to shift from
            if(slot == TileEntityToolTable.SLOT_OUTPUT){
                if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, true)) return null;
                theSlot.onSlotChange(newStack, currentStack);
            }
            //Other Slots in Inventory excluded
            else if(slot >= inventoryStart){
                //Shift from Inventory
                if(ToolTableHandler.isIngredient(newStack)){
                    if(!this.mergeItemStack(newStack, 0, TileEntityToolTable.INPUT_SLOT_AMOUNT, false)) return null;
                }
                //

                else if(slot >= inventoryStart && slot <= inventoryEnd){
                    if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)) return null;
                }
                else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)) return null;
            }
            else if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, false)) return null;

            if (newStack.stackSize == 0) theSlot.putStack(null);
            else theSlot.onSlotChanged();

            if (newStack.stackSize == currentStack.stackSize) return null;
            theSlot.onPickupFromSlot(player, newStack);

            return currentStack;
        }
        return null;
    }
}
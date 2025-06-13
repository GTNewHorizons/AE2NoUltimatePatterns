import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;

import cpw.mods.fml.common.registry.GameRegistry;

public class PosteaTransformers implements Runnable {

    final byte FLUID_BYTE = 2;

    @Override
    public void run() {
        // <appliedenergistics2:item.ItemEncodedUltimatePattern>.withTag({in:[0:{Damage:0s,Req:0L,Cnt:1L,Craft:0b,id:325s,StackType:1b,Count:0b},1:{Req:0L,FluidName:"water",Cnt:1000L,Craft:0b,StackType:2b,Count:0b}],out:[0:{Req:0L,FluidName:"water",Cnt:1000L,Craft:0b,StackType:2b,Count:0b}],author:"Developer",substitute:0b,beSubstitute:0b})
        // <appliedenergistics2:item.ItemEncodedUltimatePattern>.withTag({in:[0:{},1:{},2:{},3:{},4:{Req:0L,Damage:0s,Cnt:1L,Craft:0b,StackType:1b,id:326s,Count:0b},5:{},6:{},7:{},8:{}],out:[0:{Req:0L,Damage:0s,Cnt:1L,Craft:0b,StackType:1b,id:2s,Count:0b}],substitute:0b,author:"Developer",beSubstitute:0b})

        // <ae2fc:fluid_encoded_pattern>.withTag({combine:0,in:[0:{Damage:0s,Req:0L,Cnt:1000L,Craft:0b,id:4491s,tag:{Fluid:"potion.vodka"},Count:0b},1:{Damage:0s,Req:0L,Cnt:1L,Craft:0b,id:326s,Count:0b},2:{},3:{},4:{},5:{},6:{},7:{},8:{}],Outputs:[0:{Damage:0s,Req:0L,Cnt:400L,Craft:0b,id:4491s,tag:{Fluid:"bioethanol"},Count:0b},1:{Damage:0s,Req:0L,Cnt:600L,Craft:0b,id:4491s,tag:{Fluid:"water"},Count:0b},2:{Damage:0s,Req:0L,Cnt:1L,Craft:0b,id:326s,Count:0b}],out:[0:{Damage:0s,Req:0L,Cnt:400L,Craft:0b,id:4491s,tag:{Fluid:"bioethanol"},Count:0b},1:{Damage:0s,Req:0L,Cnt:600L,Craft:0b,id:4491s,tag:{Fluid:"water"},Count:0b},2:{Damage:0s,Req:0L,Cnt:1L,Craft:0b,id:326s,Count:0b}],Inputs:[0:{Damage:0s,Req:0L,Cnt:1000L,Craft:0b,id:4491s,tag:{Fluid:"potion.vodka"},Count:0b},1:{Damage:0s,Req:0L,Cnt:1L,Craft:0b,id:326s,Count:0b},2:{},3:{},4:{},5:{},6:{},7:{},8:{}],author:"HeadOfMetal",beSubstitute:0b})
        // <appliedenergistics2:item.ItemEncodedPattern>.withTag({in:[0:{},1:{},2:{},3:{},4:{},5:{Damage:0s,id:326s,Count:1},6:{},7:{},8:{}],out:[0:{Damage:1s,id:4198s,Count:1}],author:"HeadOfMetal",substitute:0b,crafting:0b,beSubstitute:0b})

        ItemStackReplacementManager
            .addItemReplacement("ae2noultimatepatterns:item.ItemEncodedUltimatePattern", (tag) -> {
                final int dropID = Item.getIdFromItem(getFluidDropItem());

                var inList = tag.getCompoundTag("tag")
                    .getTagList("in", 10);
                var outList = tag.getCompoundTag("tag")
                    .getTagList("out", 10);
                boolean isFluidPattern = isFluidPattern(inList, outList);

                for (int i = 0; i < inList.tagCount(); i++) {
                    var _tag = inList.getCompoundTagAt(i);
                    if (_tag.hasNoTags()) continue;
                    byte stackType = _tag.getByte("StackType");
                    _tag.removeTag("StackType");
                    if (isFluidPattern) {
                        if (stackType == FLUID_BYTE) {
                            var fluidTag = new NBTTagCompound();
                            fluidTag.setString("Fluid", _tag.getString("FluidName"));
                            _tag.setTag("tag", fluidTag);
                            _tag.removeTag("FluidName");
                            _tag.setTag("id", new NBTTagShort((short) dropID));
                        }
                    } else {
                        _tag.setTag("Count", new NBTTagInt((int) _tag.getLong("Cnt")));
                        _tag.removeTag("Cnt");
                    }
                }

                for (int i = 0; i < outList.tagCount(); i++) {
                    var _tag = outList.getCompoundTagAt(i);
                    if (_tag.hasNoTags()) continue;
                    byte stackType = _tag.getByte("StackType");
                    _tag.removeTag("StackType");
                    if (isFluidPattern) {
                        if (stackType == FLUID_BYTE) {
                            var fluidTag = new NBTTagCompound();
                            fluidTag.setString("Fluid", _tag.getString("FluidName"));
                            _tag.setTag("tag", fluidTag);
                            _tag.removeTag("FluidName");
                            _tag.setTag("id", new NBTTagShort((short) dropID));
                        }
                    } else {
                        _tag.setTag("Count", new NBTTagInt((int) _tag.getLong("Cnt")));
                        _tag.removeTag("Cnt");
                    }
                }

                Item pattern;
                if (isFluidPattern) {
                    pattern = GameRegistry.findItem("ae2fc", "fluid_encoded_pattern");
                    tag.getCompoundTag("tag")
                        .setTag(
                            "Outputs",
                            tag.getCompoundTag("tag")
                                .getTagList("out", 10));
                } else {
                    pattern = GameRegistry.findItem("appliedenergistics2", "item.ItemEncodedPattern");
                }

                int itemId = Item.getIdFromItem(pattern);
                tag.setInteger("id", itemId);

                return tag;
            });
    }

    private boolean isFluidPattern(NBTTagList inList, NBTTagList outList) {
        for (int i = 0; i < inList.tagCount(); i++) {
            var _tag = inList.getCompoundTagAt(i);
            if (_tag.hasNoTags()) continue;
            byte stackType = _tag.getByte("StackType");
            // _tag.removeTag("StackType");
            if (stackType == FLUID_BYTE) {
                return true;
            }
        }

        for (int i = 0; i < outList.tagCount(); i++) {
            var _tag = outList.getCompoundTagAt(i);
            if (_tag.hasNoTags()) continue;
            byte stackType = _tag.getByte("StackType");
            // _tag.removeTag("StackType");
            if (stackType == FLUID_BYTE) {
                return true;
            }
        }

        return false;
    }

    private static Item FLUID_DROP_ITEM;
    private static boolean checkedCache;

    private static Item getFluidDropItem() {
        // Use a checked cache variable instead of relying on the item lookup for cache since
        // we don't want to recheck every time if AE2FC is not installed
        if (!checkedCache) {
            FLUID_DROP_ITEM = GameRegistry.findItem("ae2fc", "fluid_drop");
            checkedCache = true;
        }
        return FLUID_DROP_ITEM;
    }
}

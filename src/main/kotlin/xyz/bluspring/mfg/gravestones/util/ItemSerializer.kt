package xyz.bluspring.mfg.gravestones.util

//import net.minecraft.nbt.NBTCompressedStreamTools
//import net.minecraft.nbt.NBTTagCompound
//import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
//import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools
//import net.minecraft.server.v1_16_R3.NBTTagCompound
//import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import org.bukkit.Bukkit
//import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import xyz.bluspring.mfg.gravestones.Init
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ItemSerializer {
    private val NMS_VERSION = Bukkit.getServer().javaClass.getPackage().name.substring(23)

    val craftItemStack = Class.forName("org.bukkit.craftbukkit.$NMS_VERSION.inventory.CraftItemStack")

    fun serialize(item: ItemStack): ByteArray? {
        var baos: ByteArrayOutputStream? = null

        try {
            baos = ByteArrayOutputStream()
            var tag = CompoundTag()
            //tag = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack::class.java).invoke(this, item)

            tag = (craftItemStack.getMethod("asNMSCopy", ItemStack::class.java)
                .invoke(null, item) as net.minecraft.world.item.ItemStack)
                .save(tag)
            //tag = CraftItemStack.asNMSCopy(item).save(tag)

            NbtIo.writeCompressed(tag, baos)

            return baos.toByteArray()
        } catch (e: Exception) {
            Init.plugin.logger.severe("An error occurred whilst serializing item meta!")
            e.printStackTrace()
        } finally {
            if (baos != null) {
                try {
                    baos.close()
                } catch (e: Exception) {
                    Init.plugin.logger.severe("An error occurred whilst closing the ByteArrayOutputStream!")
                    e.printStackTrace()
                }
            }
        }

        return null
    }

    fun deserialize(data: ByteArray): ItemStack? {
        var bais: ByteArrayInputStream? = null

        try {
            bais = ByteArrayInputStream(data)

            val tag = NbtIo.readCompressed(bais)

            return craftItemStack
                .getMethod("asBukkitCopy", net.minecraft.world.item.ItemStack::class.java)
                .invoke(null, net.minecraft.world.item.ItemStack.of(tag)) as ItemStack?

            //return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of(tag))
        } catch (e: Exception) {
            Init.plugin.logger.severe("An error occurred whilst deserializing item meta!")
            e.printStackTrace()
        } finally {
            if (bais != null) {
                try {
                    bais.close()
                } catch (e: Exception) {
                    Init.plugin.logger.severe("An error occurred whilst closing the ByteArrayInputStream!")
                    e.printStackTrace()
                }
            }
        }
        return null
    }
}
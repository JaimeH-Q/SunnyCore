package jaime.uno.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static jaime.uno.utils.MessageUtils.colorize;

public class ItemUtils {
    public static void replaceName(ItemStack item, String rawName){
        ItemMeta itemMeta = item.getItemMeta();
        TextComponent textComponent = LegacyComponentSerializer.legacySection()
                .deserialize(colorize(rawName))
                .decoration(TextDecoration.ITALIC, false);
        itemMeta.displayName(textComponent);
        item.setItemMeta(itemMeta);

    }

    public static String getDisplayName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return item.getType().name();
        }
        return LegacyComponentSerializer.legacySection().serialize(Objects.requireNonNull(meta.displayName()));
    }

    public static void recolorName(ItemStack item, String colorCode) {
        ItemMeta meta = item.getItemMeta();
        // Obtiene el texto plano del nombre actual
        Component nameComponent = meta.displayName();

        // Alternativa más precisa para extraer texto sin formato:
        if (nameComponent instanceof TextComponent textComponent) {
            replaceName(item, colorCode + textComponent.content());
        } else {
            assert nameComponent != null;
            throw new IllegalArgumentException("The item displayName() is invalid. " + nameComponent.examinableName());
        }

    }

    public static String itemStackArrayToBase64(ItemStack[] items) {
        if (items == null) return null;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.flush();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize ItemStack[]", e);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) {
        if (data == null) return null;

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            int length = dataInput.readInt();
            ItemStack[] items = new ItemStack[length];

            for (int i = 0; i < length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            return items;

        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to deserialize ItemStack[]", e);
        }
    }







}

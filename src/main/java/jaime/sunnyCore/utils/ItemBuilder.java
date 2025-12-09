    package jaime.sunnyCore.utils;

    import com.destroystokyo.paper.profile.PlayerProfile;
    import net.kyori.adventure.text.Component;
    import net.kyori.adventure.text.TextComponent;
    import net.kyori.adventure.text.format.TextDecoration;
    import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
    import org.apache.commons.lang3.EnumUtils;
    import org.bukkit.Material;
    import org.bukkit.configuration.ConfigurationSection;
    import org.bukkit.inventory.ItemStack;
    import org.bukkit.inventory.meta.ItemMeta;
    import org.bukkit.inventory.meta.SkullMeta;
    import org.bukkit.profile.PlayerTextures;
    import org.jetbrains.annotations.Nullable;

    import java.net.URI;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.Base64;
    import java.util.List;
    import java.util.UUID;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;


    public class ItemBuilder {
        private final ItemStack itemStack;
        private final ItemMeta meta;

        private ItemBuilder(Material material) {
            this.itemStack = new ItemStack(material);
            this.meta = itemStack.getItemMeta();
        }

        public static ItemBuilder of(Material material) {
            return new ItemBuilder(material);
        }

        public static ItemBuilder head(){
            return new ItemBuilder(Material.PLAYER_HEAD);
        }

        public static ItemBuilder fromConfigurationSection(@Nullable ConfigurationSection section, @Nullable PlaceholderReplacer placeholderReplacer){
            if(section == null){
                WarnLogger.debug("%p% &ctried to build invalid item, was replaced by a stone");
                return of(Material.STONE).name("&cINVALID ITEM");
            }
            if(placeholderReplacer == null) placeholderReplacer = new EmptyItemPlaceholderReplacer();


            Material material = EnumUtils.getEnum(Material.class, section.getString("material"));
            if(material == null){
                WarnLogger.debug("%p% &cinvalid material " + section.getString("material") + " temporary replaced to stone.");
                material = Material.STONE;
            }

            String rawName = section.getString("name");
            if(rawName == null){
                rawName = section.getString("display_name");
            }
            String name = placeholderReplacer.replaceString(rawName);
            int customModelData = section.getInt("model_data");
            List<String> rawLore = section.getStringList("lore");

            ItemBuilder builder = of(material)
                    .name(name)
                    .customModelData(customModelData)
                    .addLore(placeholderReplacer.replaceList(rawLore));

            if(material == Material.PLAYER_HEAD){
                String skullValue = section.getString("skull_value");
                if(skullValue != null){
                    builder.setCustomHead(skullValue);
                }
            }

            return builder;
        }


        public ItemBuilder setCustomHead(String base64){
            if(itemStack.getType() != Material.PLAYER_HEAD){
                throw new IllegalStateException("Builder item is not PLAYER_HEAD material");
            }

            URL skinUrl = extractSkinUrl(base64);

            if (skinUrl == null) {
                WarnLogger.debug("%p% &7skull value is invalid: &c\"" + base64 + " \"&e. No texture was applied.");
                return this;
            }

            if (!(meta instanceof SkullMeta skullMeta)) {
                throw new IllegalStateException("ItemMeta is not instance of SkullMeta");
            }

            PlayerProfile profile = UNO.getInstance().getServer().createProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(skinUrl);
            profile.setTextures(textures);
            skullMeta.setPlayerProfile(profile);

            return this;
        }

        public ItemBuilder name(Component name) {
            meta.displayName(name);
            return this;
        }

        /**
          * @param name Non-colorized name
         **/
        public ItemBuilder name(String name) {
            String content = colorize(name);
            TextComponent textComponent = LegacyComponentSerializer.legacySection()
                    .deserialize(content)
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(textComponent);
            return this;
        }


        public ItemBuilder customModelData(int data) {
            meta.setCustomModelData(data);
            return this;
        }

        public ItemStack build() {
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        public List<Component> getLore(){
            return meta.lore();
        }

        public ItemBuilder addLine(String line){
            String content = colorize(line);
            TextComponent textComponent = LegacyComponentSerializer.legacySection()
                    .deserialize(content)
                    .decoration(TextDecoration.ITALIC, false);

            List<Component> lore = meta.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.add(textComponent);
            meta.lore(lore);

            return this;
        }


        public ItemBuilder removeLastLine(){
            List<Component> lore = meta.lore();
            if (lore == null || lore.isEmpty()) {
                return this;
            }
            lore.removeLast();
            meta.lore(lore);

            return this;
        }

        public ItemBuilder addLore(List<String> rawLore){
            for(String s : rawLore){
                addLine(s);
            }
            return this;
        }


        public static ItemStack getCustomSkull(String base64Texture) {
            URL skinUrl = extractSkinUrl(base64Texture);
            if (skinUrl == null) return new ItemStack(Material.PLAYER_HEAD); // fallback

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            PlayerProfile profile = UNO.getInstance().getServer().createProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(skinUrl);
            profile.setTextures(textures);

            meta.setPlayerProfile(profile);
            head.setItemMeta(meta);

            return head;
        }

        private static URL extractSkinUrl(String base64Texture) {
            try {
                String decoded = new String(Base64.getDecoder().decode(base64Texture));
                Pattern pattern = Pattern.compile("\"url\"\\s*:\\s*\"(http://textures\\.minecraft\\.net/texture/[a-zA-Z0-9]+)\"");
                Matcher matcher = pattern.matcher(decoded);
                if (matcher.find()) {
                    return new URI(matcher.group(1)).toURL();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

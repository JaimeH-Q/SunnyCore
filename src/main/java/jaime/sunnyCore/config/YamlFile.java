package jaime.sunnyCore.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;

public class YamlFile {

    private final JavaPlugin plugin;
    private final String fileName;
    private final String folderName;

    private FileConfiguration fileConfiguration = null;
    private File file = null;

    public YamlFile(String fileName, String folderName, JavaPlugin plugin) {
        this.fileName = fileName;
        this.folderName = folderName;
        this.plugin = plugin;
    }

    public String getPath() {
        return this.fileName;
    }

    public void registerConfig() {

        // Ubicación final del archivo
        if (folderName != null) {
            file = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
        } else {
            file = new File(plugin.getDataFolder(), fileName);
        }

        // Crear carpeta si no existe
        if (folderName != null) {
            File folder = new File(plugin.getDataFolder(), folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }

        // Si el archivo no existe → copiar del jar
        if (!file.exists()) {
            if (folderName != null) {
                plugin.saveResource(folderName + File.separator + fileName, false);
            } else {
                plugin.saveResource(fileName, false);
            }
        }

        // Cargar YAML
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() throws YAMLException {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            throw new YAMLException(e);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    public boolean reloadConfig() {
        if (fileConfiguration == null) {
            if (folderName != null) {
                file = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
            } else {
                file = new File(plugin.getDataFolder(), fileName);
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        // Defaults (no es estrictamente necesario pero está bien)
        if (file != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.setDefaults(defConfig);
        }

        return true;
    }
}

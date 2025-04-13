package com.iruka.VelocityHEXMsg;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageManager {
    
    private final VelocityHEXMsg plugin;
    private final Path configDir;
    private final Map<String, String> customMessages = new HashMap<>();
    
    // Шаблон для поиска HEX цветов в формате &#RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9A-Fa-f]{6})");
    
    public MessageManager(VelocityHEXMsg plugin, Path configDir) {
        this.plugin = plugin;
        this.configDir = configDir;
    }
    
    /**
     * Загружает сообщения из файла конфигурации
     */
    public void loadMessages() {
        Path messagesFile = configDir.resolve("messages.properties");
        
        if (!Files.exists(messagesFile)) {
            plugin.getLogger().warn("Файл сообщений не найден, используются стандартные сообщения Velocity");
            return;
        }
        
        Properties properties = new Properties();
        
        try {
            properties.load(Files.newBufferedReader(messagesFile));
            plugin.getLogger().info("Загружены пользовательские сообщения");
            
            // Загружаем сообщения в карту
            for (String key : properties.stringPropertyNames()) {
                customMessages.put(key, properties.getProperty(key));
            }
            
        } catch (IOException e) {
            plugin.getLogger().error("Ошибка при загрузке файла сообщений", e);
        }
    }
    
    /**
     * Получает сообщение по ключу из конфигурации (с поддержкой HEX цветов)
     */
    public String getMessage(String key) {
        return customMessages.getOrDefault(key, key);
    }
    
    /**
     * Преобразует сообщение с HEX цветами в компонент Adventure
     */
    public Component formatMessage(String message) {
        if (message == null) return Component.empty();

        // Преобразуем HEX цвета в формат MiniMessage <#RRGGBB>
        String miniMessageStr = convertHexToMiniMessage(message);
        
        // Заменяем стандартные цветовые коды (например, &c) на MiniMessage формат
        // Важно: не используем метод convertLegacyToMiniMessage, 
        // он создает двойное преобразование и портит форматирование
        miniMessageStr = miniMessageStr.replace("&", "\\&");
        
        // Преобразуем в компонент Adventure напрямую через MiniMessage
        return plugin.getMiniMessage().deserialize(miniMessageStr);
    }
    
    /**
     * Преобразует строку с HEX цветами в формате &#RRGGBB в формат MiniMessage <#RRGGBB>
     */
    private String convertHexToMiniMessage(String message) {
        if (message == null) return "";
        
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder buffer = new StringBuilder();
        
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "<#$1>");
        }
        
        matcher.appendTail(buffer);
        
        // Заменяем стандартные цветовые коды на MiniMessage формат
        return LegacyComponentSerializer.legacyAmpersand().serialize(
               LegacyComponentSerializer.legacyAmpersand().deserialize(buffer.toString())
        );
    }
    
    /**
     * Преобразует ключ сообщения в компонент Adventure с подстановкой параметров
     */
    public Component getComponent(String key, Object... args) {
        String message = getMessage(key);
        
        // Подставляем параметры, если они есть
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(args[i]));
            }
        }
        
        return formatMessage(message);
    }
} 
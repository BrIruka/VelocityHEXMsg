package com.iruka.VelocityHEXMsg;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "velocityhexmsg",
        name = "VelocityHEXMsg",
        version = "1.0.0",
        description = "Добавляет поддержку HEX/RGB цветов в сообщения Velocity",
        authors = {"Iruka"}
)
public class VelocityHEXMsg {

    private final ProxyServer server;
    private final Logger logger;
    private final MiniMessage miniMessage;
    private final Path dataDirectory;
    private MessageManager messageManager;

    @Inject
    public VelocityHEXMsg(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.miniMessage = MiniMessage.miniMessage();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Инициализация плагина VelocityHEXMsg");
        
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            logger.error("Не удалось создать директорию конфигурации", e);
            return;
        }
        
        // Инициализация менеджера сообщений
        messageManager = new MessageManager(this, dataDirectory);
        
        // Копирование файла сообщений по умолчанию, если его нет
        createDefaultMessagesFile();
        
        // Загрузка сообщений
        messageManager.loadMessages();
        
        // Регистрация обработчика сообщений
        server.getEventManager().register(this, new MessageListener(this));
        
        logger.info("Плагин VelocityHEXMsg успешно инициализирован!");
    }
    
    private void createDefaultMessagesFile() {
        Path messagesFile = dataDirectory.resolve("messages.properties");
        
        if (Files.exists(messagesFile)) {
            return;
        }
        
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("default-messages.properties")) {
            if (in != null) {
                Files.copy(in, messagesFile);
                logger.info("Создан файл сообщений по умолчанию");
            } else {
                logger.warn("Не удалось найти файл сообщений по умолчанию в ресурсах");
            }
        } catch (IOException e) {
            logger.error("Не удалось создать файл сообщений по умолчанию", e);
        }
    }
    
    public ProxyServer getServer() {
        return server;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public MiniMessage getMiniMessage() {
        return miniMessage;
    }
    
    public MessageManager getMessageManager() {
        return messageManager;
    }
} 
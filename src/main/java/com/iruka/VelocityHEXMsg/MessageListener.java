package com.iruka.VelocityHEXMsg;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Optional;

public class MessageListener {
    
    private final VelocityHEXMsg plugin;
    
    public MessageListener(VelocityHEXMsg plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Перехватывает событие отключения от сервера и форматирует сообщение
     */
    @Subscribe
    public void onKickedFromServer(KickedFromServerEvent event) {
        // Получаем причину кика
        Component reason = event.getServerKickReason().orElse(Component.text("Неизвестная причина"));
        
        // Форматируем сообщение с HEX цветами
        if (event.kickedDuringServerConnect()) {
            String serverName = event.getServer().getServerInfo().getName();
            String reasonStr = PlainTextComponentSerializer.plainText().serialize(reason);
            
            // Берем сообщение из ключа velocity.error.connecting-server-error
            Component customKickReason = plugin.getMessageManager().getComponent("velocity.error.connecting-server-error");
            
            // Если хотим заменить сообщение при подключении к серверу
            event.setResult(KickedFromServerEvent.RedirectPlayer.create(null, customKickReason));
        } else {
            String serverName = event.getServer().getServerInfo().getName();
            String reasonStr = PlainTextComponentSerializer.plainText().serialize(reason);
            
            // Берем сообщение из ключа velocity.error.connected-server-error
            Component customKickReason = plugin.getMessageManager().getComponent("velocity.error.connected-server-error", serverName);
            
            // Если хотим заменить сообщение при отключении от сервера
            event.setResult(KickedFromServerEvent.DisconnectPlayer.create(customKickReason));
        }
    }
    
    /**
     * Перехватывает событие перед подключением к серверу и форматирует сообщение ошибки
     */
    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        // Если игрок уже подключается к серверу
        if (event.getPlayer().getCurrentServer().isPresent() && 
            event.getPlayer().getCurrentServer().get().getServerInfo().getName().equals(
                event.getOriginalServer().getServerInfo().getName())) {
            
            // Получаем сообщение об ошибке уже подключенного игрока с поддержкой HEX-цветов
            Component alreadyConnected = plugin.getMessageManager().getComponent("velocity.error.already-connected");
            
            // Устанавливаем результат - отказ в подключении с указанным сообщением
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
            
            // Отправляем сообщение игроку отдельно
            event.getPlayer().sendMessage(alreadyConnected);
        }
    }
    
    /**
     * Перехватывает событие подключения к серверу
     */
    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        // Здесь можно добавить кастомные сообщения при подключении к серверу
    }
    
    /**
     * Перехватывает событие отключения от прокси и форматирует сообщение
     */
    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        // Здесь можно добавить кастомные сообщения при отключении от прокси
    }
    
    /**
     * Перехватывает событие входа на прокси и форматирует сообщение
     */
    @Subscribe
    public void onLogin(LoginEvent event) {
        // Здесь можно добавить кастомные сообщения при входе на прокси
    }
    
    /**
     * Перехватывает событие чата и форматирует сообщение с HEX цветами
     */
    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        // Внимание: в новых версиях Velocity обработка чата может быть изменена
        // Этот метод может не сработать в последних версиях
        
        // Здесь можно добавить поддержку HEX цветов в чате, если нужно
    }
} 
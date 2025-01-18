**Attention:** Please download the V3 version. The first folder you see is the `src` directory.

# OLA OMC Addon for Global and Private Chat

## Description

The **OLA OMC Addon** is a powerful plugin designed for Minecraft servers, enabling enhanced control over **global** and **private** chat functionalities. With this plugin, server administrators can manage chat access, toggle chat options, and customize messages for global and private communications. 

## Features

- **Global Chat Management:** Allows players to send messages to all users on the server.
- **Private Chat Management:** Lets players send direct messages to specific users.
- **Cooldowns for Chat:** Configurable cooldowns for both global and private messages.
- **Chat Toggle Command:** Players can enable or disable global/private chat through a simple command.
- **Customizable Messages:** Modify message formats and chat behavior via the config file.
- **Permission Support:** Control who can bypass chat restrictions using permissions.

## Installation

1. Download the latest release of OLA OMC Addon.
2. Place the plugin jar file in your server’s `plugins` directory.
3. Restart the server to generate the necessary configuration files.
4. Customize the settings in `config.yml` as needed.

## Configuration

The plugin configuration is stored in the `config.yml` file. Below are the key sections you can customize:

### Prefixes

Define custom prefixes for both global and private chat.

```yaml
prefixes:
  global: "&8[&6Global&8]"
  private: "&8[&dPrivate&8]"
```

### Colors

Set the colors for private, global, and player messages.

```yaml
colors:
  private: "&d"
  global: "&6"
  player: "&e"
```

### Messages

Customize the messages that appear for different actions, including private and global messages.

```yaml
messages:
  private-message-sender: "&8[&dPrivate&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  private-message-receiver: "&8[&dPrivate&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  only-players: "&cThis command can only be used by players."
  cooldown: "&6You need to wait before sending another message. Time left: &c{time}s"
  global-usage: "&cCorrect usage: &6/globalmessage &e<message>"
  private-usage: "&cCorrect usage: &6/pm &e<player_name> <message>"
  no-permission: "&cYou don't have permission to use this command."
  error-invalid-cooldown-time: "&cInvalid cooldown time entered!"
  global-message-sent: "&aYour message was successfully sent."
  private-disabled: "&c{player} &6is not accepting private messages."
  player-not-found: "&cThis player is offline."
  private-message-sent: "&aYour private message has been sent to &e{target}."
```

### Cooldowns

Configure the cooldowns for both global and private messages.

```yaml
cooldowns:
  global-time: 30  # Default global cooldown time in seconds
  time: 10  # Private message cooldown time in seconds
  enabled: true  # Enable/Disable cooldown system
```

### Permissions

Manage permissions for players to bypass chat restrictions.

```yaml
permissions:
  chat-bypass: "chatcontrol.bypass"  # Permission needed to bypass chat restrictions
```

### Settings

Control the status of chat functionalities like whether the chat is disabled.

```yaml
settings:
  chatstatus:
    enabled: false  # Disable or enable chat globally
```

## Commands

- `/global <message>` – Send a global message to all players.
- `/pm <player_name> <message>` – Send a private message to a specific player.
- `/allchat <global/private> <on/off>` – Enable or disable global/private chat for the user.
- `/private-block <player-name>` – Block user from private-message (pm).
- `/private-unblock <player-name>` – UnBlock user from private-message (pm).
- `/globalreload` – Realod plugin configration file (config.yml).
- `/lobal <msg> ` – Send your message in Global Chat.


### Example:

```bash
/allchat global on  # Enables global chat
/allchat private off  # Disables private chat
```

## Dependencies

- **[OMC Base Core](https://github.com/ozaiithejava/OMC-)** (Spigot 1.8.8)
  

## License

This plugin is licensed under the MIT License. See the LICENSE file for details.

## Config For EN-Eng

```yml

prefixes:
  global: "&8[&6Global&8]"
  private: "&8[&dPrivate&8]"

colors:
  private: "&d"
  global: "&6"
  player: "&e"

messages:
  private-message-sender: "&8[&dPrivate&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  private-message-receiver: "&8[&dPrivate&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  only-players: "&cThis command can only be used by &6players."
  cooldown: "&6You must wait before chatting again. &cTime left: &e{time}s"
  global-usage: "&cCorrect usage: &6/globalmessage &e<message>"
  private-usage: "&cCorrect usage: &6/pm &e<player_name> <message>"
  no-permission: "&cYou do not have the &6required permission &cto use this command."
  error-invalid-cooldown-time: "&cInvalid cooldown time &6entered!"
  global-message-sent: "&aYour message has been &6successfully sent."
  private-disabled: "&c{player} &6has disabled private messages."
  player-not-found: "&cThe player is &6offline."
  private-message-sent: "&aYour private message has been sent to &e{target}."
  open-global-message: "&cYou must enable &6global messages &cto use the global chat."
  chat-disabled: "&cChat is currently &6disabled. &cYou are not allowed to send messages."
  usage: "&cCorrect usage: &6/pm &e<player_name> <message>"
  self-message: "&cYou cannot send a message to yourself."
  player-blocked: "&c{player} &6has blocked you, and your message cannot be sent."
  pcooldown: "&cYou must wait before sending another private message. Time left: &e{time}s"
cooldowns:
  global-time: 30 # Default cooldown time (seconds)
  time: 10 # Cooldown time for private messages (seconds)
  enabled: true # Enable the cooldown system

database:
  globalMessageTable: "global_message_settings"
  privateMessageTable: "private_message_settings"
  blockTable: "blocked_player_table"

commands:
  chat-toggle:
    usage: "&cCorrect usage: &6/chattoggle &e<global/private> <on/off>"
    invalid-type: "&cInvalid chat type. &6Valid types: &eglobal&7, &eprivate"
    private:
      enabled: "&aPrivate messages have been &6enabled."
      disabled: "&cPrivate messages have been &6disabled."
    global:
      enabled: "&aGlobal messages have been &6enabled."
      disabled: "&cGlobal messages have been &6disabled."

settings:
  chatstatus:
    enabled : false # Enable or disable chat usage

permissions:
  chat-bypass: "chatcontrol.bypass" # Permission required to send chat messages

```

## Config For TR-Turkhis

```yml

prefixes:
  global: "&8[&6Global&8]"
  private: "&8[&dÖzel&8]"

colors:
  private: "&d"
  global: "&6"
  player: "&e"

messages:
  private-message-sender: "&8[&dÖzel&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  private-message-receiver: "&8[&dÖzel&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  only-players: "&cBu komut yalnızca &6oyuncular &ctarafından kullanılabilir."
  cooldown: "&6Sohbet için beklemeniz gerekiyor. &cKalan süre: &e{time}s"
  global-usage: "&cDoğru kullanım: &6/globalmessage &e<mesaj>"
  private-usage: "&cDoğru kullanım: &6/pm &e<oyuncu_adi> <mesaj>"
  no-permission: "&cBu komutu kullanmak için &6gerekli izne &csahip değilsiniz."
  error-invalid-cooldown-time: "&cGeçersiz bekleme süresi &6girildi!"
  global-message-sent: "&aMesajınız &6başarıyla gönderildi."
  private-disabled: "&c{player} &6adlı oyuncu özel mesajları kabul etmiyor."
  player-not-found: "&cBu oyuncu &6çevrimdışı."
  private-message-sent: "&aÖzel mesajınız &e{target} &6adlı oyuncuya gönderildi."
  open-global-message: "&cGlobal sohbeti kullanabilmek için &6global mesajları açmalısınız."
  chat-disabled: "&cSohbet şu anda &6devre dışı. &cMesaj göndermeye izniniz yok."
  usage: "&cDoğru kullanım: &6/pm &e<oyuncu_adi> <mesaj>"
  self-message: "&cKendinize mesaj gönderemezsiniz."
  player-blocked: "&c{player} &6adlı oyuncu sizi engellediği için mesaj gönderilemez."
  pcooldown: "&cÖzel mesaj göndermeden önce beklemeniz gerekiyor. Kalan süre: &e{time}s"
cooldowns:
  global-time: 30 # Varsayılan bekleme süresi (saniye)
  time: 10 # Özel mesajlar için bekleme süresi (saniye)
  enabled: true # Bekleme sistemi etkinleştirilsin mi?

database:
  globalMessageTable: "global_message_settings"
  privateMessageTable: "private_message_settings"
  blockTable: "blocked_player_table"

commands:
  chat-toggle:
    usage: "&cDoğru kullanım: &6/chattoggle &e<global/private> <on/off>"
    invalid-type: "&cGeçersiz sohbet türü. &6Geçerli türler: &eglobal&7, &eprivate"
    private:
      enabled: "&aÖzel mesajlar &6açıldı."
      disabled: "&cÖzel mesajlar &6kapatıldı."
    global:
      enabled: "&aGlobal mesajlar &6açıldı."
      disabled: "&cGlobal mesajlar &6kapatıldı."

settings:
  chatstatus:
    enabled : false # Sohbet kullanımını etkinleştir

permissions:
  chat-bypass: "chatcontrol.bypass" # Sohbet göndermek için gerekli izin


```

### Dependencies
``` Maven
 - OMC
 - PLACEHOLDER API
```

### Placeholders
```YML
%OLA_globalMessageStatus%
%OLA_privateMessageStatus%
%OLA_chatStatus%
%OLA_blockedPlayers%

```
If you have any issues or need help, please contact the plugin author or create an issue in the GitHub repository.

---

Enjoy using OLA OMC Addon for Global and Private Chat!

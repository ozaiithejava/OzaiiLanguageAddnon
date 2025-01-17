
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

### Example:

```bash
/allchat global on  # Enables global chat
/allchat private off  # Disables private chat
```

## Dependencies

- **[OMC Base Core](https://github.com/ozaiithejava/OMC-)** (Spigot 1.8.8)
  

## License

This plugin is licensed under the MIT License. See the LICENSE file for details.

## Config For TR-Turkhis

```yml

prefixes:
  global: "&8[&6Global&8]"
  private: "&8[&dOzel&8]"

colors:
  private: "&d"
  global: "&6"
  player: "&e"

messages:
  private-message-sender: "&8[&dOzel&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  private-message-receiver: "&8[&dOzel&8] &e{sender} &7-> &e{receiver}&7: &f{message}"
  only-players: "&cBu komut yalnizca oyuncular tarafindan &6kullanilabilir."
  cooldown: "&6Sohbet icin beklemeniz gerekiyor. &cKalan sure: &e{time}s"
  global-usage: "&cDogru kullanim: &6/globalmessage &e<mesaj>"
  private-usage: "&cDogru kullanim: &6/pm &e<oyuncu_adi> <mesaj>"
  no-permission: "&cBu komutu kullanmak icin &6gerekli izne sahip degilsiniz."
  error-invalid-cooldown-time: "&cGecersiz cooldown suresi &6girildi!"
  global-message-sent: "&aMesajiniz &6basariyla gonderildi."
  private-disabled: "&c{player} &6adli oyuncu ozel mesajlari kabul etmiyor."
  player-not-found: "&cBu oyuncu &6cevrimdisi."
  private-message-sent: "&aOzel mesajiniz &e{target} &6adli oyuncuya gonderildi."
  open-global-message: "&cGlobal sohbeti kullanabilmek icin &6global mesajlari acmalisiniz."
  chat-disabled: "&cSohbet su anda &6devre disi. &cMesaj gondermek icin izniniz yok."

cooldowns:
  global-time: 30 # Varsayilan cooldown suresi (saniye)
  time: 10 # Ozel mesajlar icin cooldown suresi (saniye)
  enabled: true # Cooldown sistemini etkinlestir

database:
  globalMessageTable: "global_message_settings"
  privateMessageTable: "private_message_settings"

commands:
  chat-toggle:
    usage: "&cDogru kullanim: &6/chattoggle &e<global/private> <on/off>"
    invalid-type: "&cGecersiz sohbet turu. &6Gecerli turler: &eglobal&7, &eprivate"
    private:
      enabled: "&aOzel mesajlar &6acildi."
      disabled: "&cOzel mesajlar &6kapatildi."
    global:
      enabled: "&aGlobal mesajlar &6acildi."
      disabled: "&cGlobal mesajlar &6kapatildi."

settings:
  chatstatus:
    enabled : false # sohbeti kullanım

permissions:
  chat-bypass: "chatcontrol.bypass" # Sohbet göndermek için gerekli izin
```
### Dependencies

##### - OMC
##### - PLACEHOLDER API


If you have any issues or need help, please contact the plugin author or create an issue in the GitHub repository.

---

Enjoy using OLA OMC Addon for Global and Private Chat!

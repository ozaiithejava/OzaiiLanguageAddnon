# **Attention**
Please download the V3 version. The first folder you see is the `src` directory.

---

# OLA OMC Addon for Global and Private Chat

## Description

The **OLA OMC Addon** is a powerful plugin designed for Minecraft servers, enabling enhanced control over **global** and **private** chat functionalities. With this plugin, server administrators can manage chat access, toggle chat options, and customize messages for global and private communications. 

---

## Features

- **Global Chat Management:** Allows players to send messages to all users on the server.
- **Private Chat Management:** Lets players send direct messages to specific users.
- **Cooldowns for Chat:** Configurable cooldowns for both global and private messages.
- **Chat Toggle Command:** Players can enable or disable global/private chat through a simple command.
- **Private Message Blocking:** Players can block or unblock private messages from others.
- **Customizable Messages:** Modify message formats and chat behavior via the config file.
- **Permission Support:** Control who can bypass chat restrictions using permissions.
- **Reload Command:** Quickly reload the plugin configuration.

---

## Installation

1. Download the latest release of OLA OMC Addon.
2. Place the plugin jar file in your server’s `plugins` directory.
3. Restart the server to generate the necessary configuration files.
4. Customize the settings in `config.yml` as needed.

---

## Commands

- `/global <message>` – Send a global message to all players.
- `/pm <player_name> <message>` – Send a private message to a specific player.
- `/private-block <player_name>` – Block a player from sending private messages to you.
- `/private-unblock <player_name>` – Unblock a player for private messages.
- `/chattoggle <global/private> <on/off>` – Enable or disable global/private chat for the user.
- `/globalreload` – Reload the plugin configuration file (`config.yml`).

### Example Usage:

```bash
/chattoggle global on  # Enables global chat
/chattoggle private off  # Disables private chat
```

---

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
  global-message-sent: "&aYour message was successfully sent."
  private-disabled: "&c{player} &6is not accepting private messages."
  player-not-found: "&cThis player is offline."
  private-message-sent: "&aYour private message has been sent to &e{target}."
  player-blocked: "&c{player} &6has blocked you. Message not sent."
  chat-disabled: "&cChat is currently &6disabled. &cYou are not allowed to send messages."
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

---

## Dependencies

- **[OMC Base Core](https://github.com/ozaiithejava/OMC-)**
- PlaceholderAPI

---

## Placeholders

```yaml
%OLA_globalMessageStatus%
%OLA_privateMessageStatus%
%OLA_chatStatus%
%OLA_blockedPlayers%
```

---

## Recent Updates (V3)

### Added:
- PlaceholderAPI support.
- Block/unblock functionality for private messages.
- Reload command for quick config updates.

### Fixed:
- Resolved various bugs for smoother performance.

---

## License

This plugin is licensed under the MIT License. See the LICENSE file for details.

---



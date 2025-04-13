# VelocityHEXMsg

A Velocity plugin that adds support for HEX/RGB colors in Velocity messages.

## Features

- Full HEX/RGB color support for all Velocity messages
- Compatible with Velocity 3.4.0 and newer
- Uses modern Adventure API for text formatting
- Custom kick messages with HEX colors
- Format server connection, disconnection, and error messages
- Easy configuration through property files
- MinimessageFormat support

## Installation

1. Download the latest release
2. Place the JAR file in your Velocity server's `plugins` folder
3. Restart your Velocity server
4. Edit the configuration file at `plugins/velocityhexmsg/messages.properties` if needed

## Usage

### HEX Color Format

Use the format `&#RRGGBB` for HEX colors in your messages:

```
&#FF5555This text will be red
&#1E93CBThis text will be blue
7FF55This text will be green
```

Standard Minecraft color codes (`&c`, `&a`, etc.) are also supported.

### Configuration

The plugin creates a `messages.properties` file in the `plugins/velocityhexmsg` directory. This file contains all customizable messages.

Example of the configuration file:

```properties
velocity.error.already-connected=&#FF5555You are already connected to this server!
velocity.error.already-connected-proxy=&#FF5555You are already connected to this proxy!
velocity.error.connecting-server-error=&#FF5555Connection interrupted! \n\n&#1E93CBPlease try again in a few minutes.
```

You can customize all messages by editing this file and restarting your Velocity server.

## Requirements

- Velocity 3.4.0 or higher
- Java 17 or higher


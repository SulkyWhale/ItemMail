# ItemMail

A GUI-based item mail Minecraft plugin for Paper servers to send items to other players.

## Commands

- `/itemmail` - Open the mail GUI.
- `/itemmail send {player name}` - Sends the item that the player is holding to the specified player.
- `/itemmail {player name}` - Admin command for players with the permission `itemmail.admin` to open a GUI to inspect the mail of the specified player.

Players can collect mail by doing `/itemmail` and clicking on the player head for the player they wish to receive mail from.

## Permissions

- `itemmail.mail.send` - Allows players to send mail.
- `itemmail.mail.gui` - Allows players to view mail GUI.
- `itemmail.admin` - Allows admins to view contents of players' mail.

## Installation

1. Download the latest JAR from [Releases](https://github.com/SulkyWhale/ItemMail/releases) and place it in your plugins folder.
2. Restart your server.

## Building

If you wish to build the plugin yourself, follow the instructions below.

1. Clone the repository:
    ```shell
    git clone https://github.com/SulkyWhale/ItemMail.git
    ```
2. Change into the working directory:
    ```shell
    cd ItemMail
    ```
3. Build the JAR with Maven:
    ```shell
    mvn clean package
    ```

## Issues

If you encounter any bugs or issues, please open an [issue](https://github.com/SulkyWhale/ItemMail/issues/new). Be sure to check existing issues first to avoid duplicates.

## Contributing

Contributions are welcome. If you have any bug fixes, improvements, or new features you would like to add to this project, feel free to open a [pull request](https://github.com/SulkyWhale/ItemMail/pulls).


## License

ItemMail is licensed under the GNU GPL v3. Please see the [license](/LICENSE) for more information.

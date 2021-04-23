# Forca
Forca is a game, based in hangman game, developed in a bootcamp challenge where our group should build a multiplayer game during the weekend.
The game consist in trying to find out the word in the blank spots guessing the letters. Each mistake build another body part in the hang. If the body in completelly build and the word wasn't discovered yet, the game is over.

### Server 
Using serverSocket API our server could host the game for multiplayer clients. Our server allow multiplatform (and crossplatform) game due to compatibility with a our console client, our graphic client and NetCat.
The client connected can start a game by its own, choose if is solo player or multiplayer, and choose between 2 themes of words, or the client can wait in the lobby to join a game started by another player.

### Clients
Clients connected to the server will receive instructions to proceed to the game or wait in the lobby. All the interaction are made throught keyboard.
The graphic client would receive instructions via terminal and the graphics are initialized when the game starts.

#### Tehcnology
- Java

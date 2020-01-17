package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResult;
import cz.zcu.kiv.krysl.bsclient.net.types.*;

/**
 * A Battleships game client.
 * Represents a session on the server.
 * When the network connection is lost, the client session can be restored by calling
 * the restore method.
 *
 */
public interface BattleshipsClient {

    /**
     * Join a game.
     *
     * @return Nickname of the opponent if already in the game or null if must wait for the opponent to join.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws InvalidStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    Nickname joinGame() throws DisconnectedException, InvalidStateException;

    /**
     * Choose ships layout.
     *
     * @param layout The layout to choose.
     * @return True if the layout is accepted, false otherwise.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws InvalidStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    boolean chooseLayout(Layout layout) throws DisconnectedException, InvalidStateException;

    /**
     * Shoot.
     *
     * @param position The target position.
     * @return Result of shooting.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws InvalidStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    ShootResult shoot(Position position) throws DisconnectedException, InvalidStateException;

    /**
     * Leave the game.
     *
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws InvalidStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    void leaveGame() throws DisconnectedException, InvalidStateException;

    /**
     * Properly disconnect from the server.
     * The session is terminated.
     *
     * @throws DisconnectedException If the client is disconnected before or during the call.
     */
    void disconnect() throws DisconnectedException;

    /**
     * Shutdown the client workers and underlying connection.
     */
    void close();
}

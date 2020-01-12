package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.results.RestoreResult;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResult;
import cz.zcu.kiv.krysl.bsclient.net.types.*;

import java.net.InetSocketAddress;

public interface BattleshipsClient {
    /**
     * Connect to the server on the address using the nickname.
     *
     * @return Session key if successfully connected, null if the server is full.
     * @throws AlreadyConnectedException If the client is already connected to the server.
     * @throws ConnectException    If there was an error while connecting to the server.
     */
    SessionKey connect(InetSocketAddress serverAddress, Nickname nickname) throws AlreadyConnectedException, ConnectException, ClosedException;

    /**
     * Restore client session on the server on the address using the session key.
     *
     * @return Session restoration result.
     * @throws AlreadyConnectedException If the client is already connected to the server.
     * @throws ConnectException If there was an error while connecting to the server.
     */
    RestoreResult restore(InetSocketAddress serverAddress, SessionKey sessionKey) throws AlreadyConnectedException, ConnectException, ClosedException;

    /**
     * Join a game.
     *
     * @return False if waiting for the opponent, true if the opponent is already in the game.
     * @throws DisconnectedException If the server disconnects before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client session may be restored back.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    boolean joinGame() throws DisconnectedException, OfflineException, IllegalStateException, ClosedException;

    /**
     * Choose ships layout.
     *
     * @param layout The layout to choose.
     * @return True if the layout is accepted, false otherwise.
     * @throws DisconnectedException If the server disconnects before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client session may be restored back.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    boolean chooseLayout(Layout layout) throws DisconnectedException, OfflineException, IllegalStateException, ClosedException;

    /**
     * Shoot.
     *
     * @param position The target position.
     * @return Result of shooting.
     * @throws DisconnectedException If the server disconnects before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client session may be restored back.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    ShootResult shoot(Position position) throws DisconnectedException, OfflineException, IllegalStateException, ClosedException;

    /**
     * Leave the game.
     *
     * @throws DisconnectedException If the server disconnects before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client session may be restored back.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    void leaveGame() throws DisconnectedException, OfflineException, IllegalStateException, ClosedException;

    /**
     * Properly disconnect from the server.
     * The session is terminated.
     *
     * @throws DisconnectedException If the server disconnects before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client session may be restored back.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    void disconnect() throws DisconnectedException, OfflineException, IllegalStateException, ClosedException;

    /**
     * Close the client.
     * It can't be reused again.
     * All future calls to this client will throw ClosedException.
     */
    void close();
}

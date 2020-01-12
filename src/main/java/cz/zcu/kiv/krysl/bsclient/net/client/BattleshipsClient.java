package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResult;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;

public interface BattleshipsClient {
    /**
     * Connect to the server.
     *
     * @return True if successfully connected, false if the server is full.
     * @throws AlreadyConnectedException If the client is already connected to the server.
     * @throws InterruptedException      If the call is interrupted.
     * @throws ClientConnectException    If there was an error while connecting to the server.
     */
    boolean connect() throws AlreadyConnectedException, InterruptedException, ClientConnectException;

    /**
     * Join a game.
     *
     * @return False if waiting for the opponent, true if the opponent is already in the game.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client may be restored back to online state.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    boolean joinGame() throws DisconnectedException, OfflineException, IllegalStateException;

    /**
     * Choose ships layout.
     *
     * @param layout The layout to choose.
     * @return True if the layout is accepted, false otherwise.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client may be restored back to online state.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    boolean chooseLayout(Layout layout) throws DisconnectedException, OfflineException, IllegalStateException;

    /**
     * Shoot.
     *
     * @param position The target position.
     * @return Result of shooting.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client may be restored back to online state.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    ShootResult shoot(Position position) throws DisconnectedException, OfflineException, IllegalStateException;

    /**
     * Shoot.
     *
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client may be restored back to online state.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    void leaveGame() throws DisconnectedException, OfflineException, IllegalStateException;

    /**
     * Properly disconnect from the server.
     * Session on the server is terminated.
     *
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call. Client may be restored back to online state.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    void disconnect() throws DisconnectedException, OfflineException, IllegalStateException;

    /**
     * Restore connection to online state from the offline state.
     *
     * @throws AlreadyOnlineException If the connection is already online.
     * @throws DisconnectedException  If the client is disconnected before or during the call.
     */
    boolean restore() throws AlreadyOnlineException, DisconnectedException;

    /**
     * Force client to close immediately.
     */
    void close();
}

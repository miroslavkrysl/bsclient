package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.results.ChooseLayoutResult;
import cz.zcu.kiv.krysl.bsclient.net.results.ConnectResult;
import cz.zcu.kiv.krysl.bsclient.net.results.JoinGameResult;
import cz.zcu.kiv.krysl.bsclient.net.results.ShootResult;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;

import java.io.IOException;

public interface BattleshipsClient {
    /**
     * Connect to the server.
     *
     * @return A result of connecting. May be success or failure.
     * @throws IOException               When an error occurs while creating connection to the server.
     * @throws AlreadyConnectedException If the client is already connected to the server.
     */
    ConnectResult connect() throws IOException, AlreadyConnectedException;

    /**
     * Join a game.
     *
     * @return Result of joining the game.
     * @throws DisconnectedException   If the connection is not connected.
     * @throws OfflineException        If the underlying connection is lost during the call. May be restored back to online state.
     * @throws IllegalRequestException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    JoinGameResult joinGame() throws DisconnectedException, OfflineException, IllegalRequestException;

    /**
     * Choose ships layout.
     *
     * @param layout The layout to choose.
     * @return Result of choosing the layout.
     * @throws DisconnectedException   If the connection is not connected.
     * @throws OfflineException        If the underlying connection is lost during the call. May be restored back to online state.
     * @throws IllegalRequestException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    ChooseLayoutResult chooseLayout(Layout layout) throws DisconnectedException, OfflineException, IllegalRequestException;

    /**
     * Shoot.
     *
     * @param position The target position.
     * @return Result of shooting.
     * @throws DisconnectedException   If the connection is not connected.
     * @throws OfflineException        If the underlying connection is lost during the call. May be restored back to online state.
     * @throws IllegalRequestException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    ShootResult shoot(Position position) throws DisconnectedException, OfflineException, IllegalRequestException;

    /**
     * Shoot.
     *
     * @throws DisconnectedException   If the connection is not connected.
     * @throws OfflineException        If the underlying connection is lost during the call. May be restored back to online state.
     * @throws IllegalRequestException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    void leaveGame() throws DisconnectedException, OfflineException, IllegalRequestException;

    /**
     * Properly disconnect from the server.
     * Session on the server is terminated.
     *
     * @throws DisconnectedException   If the connection is not connected.
     * @throws OfflineException        If the underlying connection is lost during the call. May be restored back to online state.
     * @throws IllegalRequestException If the request is illegal in the current connection state, or the state was changed during the call.
     */
    void disconnect() throws DisconnectedException, OfflineException, IllegalRequestException;

    /**
     * Restore connection to online state from the offline state.
     *
     * @throws AlreadyOnlineException If the connection is already online.
     * @throws DisconnectedException  If the connection is not connected.
     */
    void restore() throws AlreadyOnlineException, DisconnectedException;

    /**
     * Force client to close immediately.
     */
    void close();
}
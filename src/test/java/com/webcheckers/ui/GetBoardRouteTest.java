package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
class GetBoardRouteTest {

    /**
     * The component-under-test
     */
    private GetBoardRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private Gson gson;

    private GameList gameListTest;
    private PlayerLobby playerLobbyTest;
    private Player red;
    private Player white;
    private BoardModel modelTest;
    private Space mockSpace;


    private Space whiteSpace;
    private Space redSpace;
    private Piece whitePiece;
    private Piece redPiece;

    /**
     * Run before each test to set up the mock objects
     */
    @BeforeEach
    void testSetup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gson = new Gson();
        gameListTest = mock(GameList.class);
        playerLobbyTest = mock(PlayerLobby.class);

        modelTest = mock(BoardModel.class);
        red = mock(Player.class);
        white = mock(Player.class);

        when(white.isInGame()).thenReturn(true);
        when(modelTest.getActivePlayer()).thenReturn(white);
        when(modelTest.getRedPlayer()).thenReturn(red);

        when(session.attribute("id")).thenReturn("whiteName");
        when(gameListTest.getBoardModel(white)).thenReturn(modelTest);
        when(playerLobbyTest.getPlayer("whiteName")).thenReturn(white);

    }

    /**
     * Ensures that the TeamHasMove call in handle returns true and allows the program to continue
     */
    private void setUpTeamHasMoveTrue() {
        mockSpace = mock(Space.class);
        when(mockSpace.isValid()).thenReturn(true);

        redPiece = new Piece(Piece.color.RED, Piece.pieceType.KING);
        redSpace = mock(Space.class);
        when(redSpace.getPiece()).thenReturn(redPiece);
        when(modelTest.getSpace(new Position(4, 3))).thenReturn(redSpace);

        whitePiece = new Piece(Piece.color.WHITE, Piece.pieceType.KING);
        whiteSpace = mock(Space.class);
        when(whiteSpace.getPiece()).thenReturn(whitePiece);
        when(modelTest.getSpace(new Position(3, 2))).thenReturn(whiteSpace);

        when(modelTest.getSpace(any())).thenReturn(mockSpace);
    }

     /**
      * Ensures that the TeamHasMove call in handle returns false and disallows the program to continue
      */
     private void setUpTeamHasMoveFalse() {
        mockSpace = mock(Space.class);
        when(mockSpace.isValid()).thenReturn(true);

        redPiece = new Piece(Piece.color.RED, Piece.pieceType.KING);
        redSpace = mock(Space.class);
        when(redSpace.getPiece()).thenReturn(redPiece);
        when(modelTest.getSpace(new Position(7, 4))).thenReturn(redSpace);
        when(modelTest.getSpace(new Position(7, 2))).thenReturn(redSpace);


        whitePiece = new Piece(Piece.color.WHITE, Piece.pieceType.SINGLE);
        whiteSpace = mock(Space.class);
        when(whiteSpace.getPiece()).thenReturn(whitePiece);
        when(modelTest.getSpace(new Position(6, 3))).thenReturn(whiteSpace);

        when(modelTest.getSpace(any())).thenReturn(mockSpace);
    }

    /**
     * Test that the board will fail with a null game list
     */
    @Test
    void nullGameListTest(){
        assertThrows(NullPointerException.class, () -> {
            new GetBoardRoute(null, playerLobbyTest, engine, gson);}, "GetBoardRoute allowed null GameList.");
    }

    /**
     * Test that the board will fail with a null player lobby
     */
    @Test
    void nullPlayerLobbyTest(){
        assertThrows(NullPointerException.class, () -> {
            new GetBoardRoute(gameListTest, null, engine, gson);}, "GetBoardRoute allowed null PlayerLobby.");
    }

    /**
     * Test that the board will fail with a null engine
     */
    @Test
    void nullEngineTest(){
        assertThrows(NullPointerException.class, () -> {
            new GetBoardRoute(gameListTest, playerLobbyTest, null, gson);}, "GetBoardRoute allowed null engine.");
    }

    /**
     * Test that the board works with all arguments valid
     */
    @Test
    void workingRouteTest(){
        CuT = new GetBoardRoute(gameListTest, playerLobbyTest, engine, gson);
        setUpTeamHasMoveTrue();

        BoardView viewTest = mock(BoardView.class);
        when(gameListTest.getBoardView(white)).thenReturn(viewTest);


        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

    }

    /**
     * Tests that the route redirects to home if the player is not in a game
     */
    @Test
    void currentPlayerNotInGameTest() {
        // If player is not in a game, then this should redirect to home
        CuT = new GetBoardRoute(gameListTest, playerLobbyTest, engine, gson);
        setUpTeamHasMoveTrue();
        BoardView viewTest = mock(BoardView.class);
        when(gameListTest.getBoardView(white)).thenReturn(viewTest);
        when(white.isInGame()).thenReturn(false);
        CuT.handle(request, response);
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Tests that the losing message is displayed if team has move == false
     */
    @Test
    void teamHasMoveFalseTest() {
        CuT = new GetBoardRoute(gameListTest, playerLobbyTest, engine, gson);
        setUpTeamHasMoveFalse();
        when(modelTest.getWinner()).thenReturn(red);
        when(red.getName()).thenReturn("redPlayer");
        BoardView viewTest = mock(BoardView.class);
        when(gameListTest.getBoardView(white)).thenReturn(viewTest);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("message", new Message(Message.TYPE.error,
                "Game Over. " + modelTest.getWinner().getName() + " has won."));

    }


}
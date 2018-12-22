package com.webcheckers.ui;

import static spark.Spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;

import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.AIPlayer;
import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class WebServer {
  private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

  //
  // Constants
  //

  /**
   * @todo fix the /board /game swap ...or not? (>o.O)>♣ good luck.
   * The URL pattern to request the Home page.
   */

  static final String HOME_URL = "/";
  static final String SIGNIN_URL = "/signin";
  static final String SIGNOUT_URL = "/signout";
  static final String GAME_URL = "/board";
  static final String MESSAGE_URL = "/message";
  static final String BOARD_URL = "/game";
  static final String VALIDATE_URL = "/validateMove";
  static final String CHECK_URL = "/checkTurn";
  static final String SUBMIT_URL = "/submitTurn";
  static final String RESIGN_URL = "/resignGame";
  static final String BACKUP_URL = "/backupMove";
  static final String REPLAY_GAME = "/replay/game";
  static final String REPLAY_STOP = "/replay/stopWatching";
  static final String REPLAY_NEXT = "/replay/nextTurn";
  static final String REPLAY_PREVIOUS = "/replay/previousTurn";

  //
  // Attributes
  //

  private final PlayerLobby playerLobby;
  private final TemplateEngine templateEngine;
  private final Gson gson;
  private final GameList gameList;
  private final SavedGameList savedGames;

  //
  // Constructor
  //

  /**
   * The constructor for the Web Server.
   *
   * @param playerLobby
   *    The site wide {@link PlayerLobby} object
   * @param templateEngine
   *    The default {@link TemplateEngine} to render page-level HTML views.
   * @param gson
   *    The Google JSON parser object used to render Ajax responses.
   *
   * @throws NullPointerException
   *    If any of the parameters are {@code null}.
   */
  public WebServer(final GameList gameList, final PlayerLobby playerLobby, final TemplateEngine templateEngine, final SavedGameList savedGames, final Gson gson) {
    // validation
    Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gson, "gson must not be null");
    //
    this.gameList = gameList;
    this.playerLobby = playerLobby;
    this.templateEngine = templateEngine;
    this.gson = gson;
    this.savedGames = savedGames;
  }

  //
  // Public methods
  //

  /**
   * Initialize all of the HTTP routes that make up this web application.
   *
   * <p>
   * Initialization of the web server includes defining the location for static
   * files, and defining all routes for processing client requests. The method
   * returns after the web server finishes its initialization.
   * </p>
   */
  public void initialize() {

    // Configuration to serve static files
    staticFileLocation("/public");

    //// Setting any route (or filter) in Spark triggers initialization of the
    //// embedded Jetty web server.

    //// A route is set for a request verb by specifying the path for the
    //// request, and the function callback (request, response) -> {} to
    //// process the request. The order that the routes are defined is
    //// important. The first route (request-path combination) that matches
    //// is the one which is invoked. Additional documentation is at
    //// http://sparkjava.com/documentation.html and in Spark tutorials.

    //// Each route (processing function) will check if the request is valid
    //// from the client that made the request. If it is valid, the route
    //// will extract the relevant data from the request and pass it to the
    //// application object delegated with executing the request. When the
    //// delegate completes execution of the request, the route will create
    //// the parameter map that the response template needs. The data will
    //// either be in the value the delegate returns to the route after
    //// executing the request, or the route will query other application
    //// objects for the data needed.

    //// FreeMarker defines the HTML response using templates. Additional
    //// documentation is at
    //// http://freemarker.org/docs/dgui_quickstart_template.html.
    //// The Spark FreeMarkerEngine lets you pass variable values to the
    //// template via a map. Additional information is in online
    //// tutorials such as
    //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

    //// These route definitions are examples. You will define the routes
    //// that are appropriate for the HTTP client interface that you define.
    //// Create separate Route classes to handle each route; this keeps your
    //// code clean; using small classes.

    // Shows the Checkers game Home page.
    get(HOME_URL, new GetHomeRoute(playerLobby, templateEngine, savedGames));

    // Shows the Checkers sign in page
    get(SIGNIN_URL, new GetSignInRoute(templateEngine));

    // POST to the sign in page
    post(SIGNIN_URL, new PostSignInRoute(playerLobby, templateEngine));

    // GET sign out route
    get(SIGNOUT_URL, new GetSignOutRoute(playerLobby, templateEngine));

    // GET game route
    get(GAME_URL, new GetGameRoute(gameList, playerLobby, savedGames, templateEngine));

    // GET board route
    get(BOARD_URL, new GetBoardRoute(gameList, playerLobby, templateEngine, gson));

    //GET Replay game route
    get(REPLAY_GAME, new GetReplayGameRoute(savedGames, gson, templateEngine));

    //GET Replay stop route
    get(REPLAY_STOP, new GetReplayStopRoute(savedGames, templateEngine));

    // Post validate move route
    post(VALIDATE_URL, new PostValidateMoveRoute(playerLobby, gameList, gson));

    // POST check turn route
    post(CHECK_URL, new PostCheckTurnRoute(gameList, playerLobby, gson));

    // POST submit turn
    post(SUBMIT_URL, new PostSubmitTurnRoute(gameList, playerLobby, gson));

    // POST resign route
    post(RESIGN_URL, new PostResignGameRoute(gameList, savedGames, gson));

    // POST back up turn route; backs up last non-submitted move
    post(BACKUP_URL, new PostBackUpMoveRoute(gameList, playerLobby, gson));

    //POST next turn replay
    post(REPLAY_NEXT, new PostReplayForwardRoute(savedGames, gson));

    //POST previous turn replay
    post(REPLAY_PREVIOUS, new PostReplayBackwardRoute(savedGames, gson));

    initializeAIPlayers();
    LOG.config("WebServer is initialized.");
  }

  private void initializeAIPlayers() {
    playerLobby.addAIPlayer("AI Player", -1);
  }
}
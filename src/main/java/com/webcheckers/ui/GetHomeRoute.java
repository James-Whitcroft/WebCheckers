package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.Message;
import com.webcheckers.Model.Player;
import spark.*;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
  static final String SIGNED_IN = "signedIn";
  static final String PLAYER_NAME = "playerName";
  static final String ACTIVE_PLAYERS = "activePlayers";
  static final String ACTIVE_PLAYER_COUNT = "activePlayerCount";
  static final String MESSAGE = "message";
  static final String REPLAY_GAME_LIST = "replayGameList";


  // player stats
  static final String GAMES_PLAYED_COUNT = "gamesPlayedCount";
  static final String GAMES_WON_COUNT = "gamesWonCount";

  static final String VIEW_NAME = "home.ftl";

  private final PlayerLobby playerLobby;
  private final TemplateEngine templateEngine;
  private final SavedGameList savedGameList;


  /**
   * Create the Spark Route (UI controller) for the
   * {@code GET /} HTTP request.
   *
   * @param playerLobby
   *  the site wide PlayerLobby
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(PlayerLobby playerLobby, final TemplateEngine templateEngine, SavedGameList savedGameList) {
    // validation
    Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    //
    this.playerLobby = playerLobby;
    this.templateEngine = templateEngine;
    this.savedGameList = savedGameList;
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");
    // retrieve the HTTP session
    final Session httpSession = request.session();
    String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);
    // if the player has an id set
    if(playerName != null) {
      vm.put(SIGNED_IN, true);
      vm.put(PLAYER_NAME, playerName);
      // put the list of OTHER active players, players excluding myself

      Player newPlayer = playerLobby.getPlayer(playerName);

      vm.put(ACTIVE_PLAYERS, playerLobby.getOtherActivePlayers(newPlayer));
      vm.put(GAMES_PLAYED_COUNT, newPlayer.getGamesPlayed());
      vm.put(GAMES_WON_COUNT, newPlayer.getGamesWon());
      vm.put(REPLAY_GAME_LIST, savedGameList.getAllSavedGames());


      if(httpSession.attributes().contains(MESSAGE)){
        vm.put(MESSAGE, httpSession.attribute(MESSAGE));
        httpSession.removeAttribute(MESSAGE);
      }
      if (playerLobby.getPlayer(playerName).isInGame()) {
        response.redirect(WebServer.BOARD_URL);
        halt();
        return null;
      }
    } else {
      vm.put(SIGNED_IN, false);
    }
    // display the total number of players to an un-signed in player
    vm.put(ACTIVE_PLAYER_COUNT, playerLobby.getActivePlayers().size());
    vm.put(REPLAY_GAME_LIST, savedGameList.getAllSavedGames());
    return templateEngine.render(new ModelAndView(vm , GetHomeRoute.VIEW_NAME));
  }

}
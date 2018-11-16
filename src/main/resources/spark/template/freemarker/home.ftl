<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <script>

        function getBack() {
            if (window.sessionStorage.hasOwnProperty("back")) {
                swapBack(window.sessionStorage.back);
            }
        }

        function swapBack(img) {
            document.getElementsByTagName("body")[0].style.background = "url('" + img + "')";
            document.getElementsByTagName("body")[0].style.backgroundSize = "cover";
            window.sessionStorage.setItem("back", img);
        }
    </script>
</head>
<body onload="getBack()">
  <div class="page">
  
    <h1>Web Checkers</h1>
    
    <div class="navigation">
      <a href="/">my home</a>
      <#if signedIn><div class="fake_link">Welcome, ${playerName}</div>
      <a href="/signout">sign out</a>
      <#else><a href="/signin">sign in</a>
      </#if>
      <div class="back_selector fake_link">Backgrounds
        <div class="selector">
            <div onclick="swapBack('/img/bunny_1.jpg')">Back 1</div>
            <div onclick="swapBack('/img/kittens_1.jpg')">Back 2</div>
            <div onclick="swapBack('/img/cosmo.jpg')">Back 3</div>
            <div onclick="swapBack('/img/bun_chicks.jpg')">Back 4</div>
            <div onclick="swapBack('/img/cool_cat.jpg')">Back 5</div>
            <div onclick="swapBack('/img/elephant.jpg')">Back 6</div>
        </div>
       </div>
    </div>
    
    <div class="body column">
        <div class="large_title centered">
            Welcome to the world of online Checkers.
        </div>

        <div class="row">
            <div class="one_third_row">
                <#if signedIn>
                  <div class="lobby column">
                    <div class="small_title">
                        Other Active Players - ${activePlayerCount - 1}
                    </div>
                    <#list activePlayers as player>
                        <div>
                            <form action="/board" method="GET">
                                <button class="lobby_entry" type="submit" name="white" value="${player}">${player}</button>
                            </form>
                        </div>
                    </#list>
                  </div>
                <#else>
                    <div>
                        Sign in and join the other ${activePlayerCount} player<#if activePlayerCount gt 1>s</#if>!
                    </div>
                </#if>
             </div>
             <div class="one_third_row">
                <#if message??><div>${message}</div></#if>
             </div>
             <#if signedIn>
                <div class="one_third_row">
                    <div class="lobby column">
                        <div class="small_title">
                            Your Stats
                        </div>
                        <div class="lobby_entry"> Games Played: ${gamesPlayedCount} </div>
                        <div class="lobby_entry"> Games Won: ${gamesWonCount} </div>
                     </div>
                </div>
             </#if>
         </div> <!--end row-->
    </div> <!--end body-->
  </div>
</body>
</html>

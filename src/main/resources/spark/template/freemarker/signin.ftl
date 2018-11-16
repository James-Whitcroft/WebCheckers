<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
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
    </div>
    <div class="column centered">
        <div class="title">Sign in to play!</div>
        <#if nameTaken??><div>Sorry, please try another name.</div></#if>
        <form action="/signin" method="POST">
            <input class="pretty_input" name="signIn" autofocus/>
            <button class="fake_link" type="submit">Sign in</button>
        </form>
    </div>
  </div>
</body>
</html>

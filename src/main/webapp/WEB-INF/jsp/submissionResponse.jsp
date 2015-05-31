<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>

</head>
<body>
<div th:include="header"/>
<div class="container">
<h1 th:text="${msg}">Hello</h1>
<h1 th:if="${param.containsKey('msg')}" th:text="${param.msg[0]}">Hello</h1>

<h2 th:if="${param.containsKey('url')}"><a th:href="${param.url[0]}">Here</a></h2>

</div>
</body>
</html>
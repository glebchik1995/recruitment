<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Уведомление о новом сообщении</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Message</h1>
        <p>Привет, ${name}!</p>
        <p>Вам поступило сообщение.</p>
        <p>С наилучшими пожеланиями, Команда Gmail!</p>
        <p>Ссылка на просмотр сообщения: <a id="a" href="http://localhost:8080/api/v1/emails">Просмотреть сообщение</a>
        </p>
    </div>
</div>
</body>
</html>
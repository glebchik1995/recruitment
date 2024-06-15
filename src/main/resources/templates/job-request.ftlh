<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Уведомление о новой заявки на найм</title>
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

        #button {
            background-color: #607FF2;
            border-radius: 10px;
            padding: 5px;
            color: white;
            text-decoration: none;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Job Request</h1>
        <p>Привет, ${name}!</p>
        <p>Направляю в ваш адрес заявку на найм на вакансию ${title} с позицией ${position}.</p>
        <p>С наилучшими пожеланиями, Команда Gmail!</p>
        <p>Ссылка на просмотр заявки: <a id="a" href="http://localhost:8080/api/v1/emails">Просмотреть заявку</a></p>
    </div>
</div>
</body>
</html>
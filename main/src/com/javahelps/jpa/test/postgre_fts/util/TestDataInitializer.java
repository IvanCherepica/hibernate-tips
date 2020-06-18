package com.javahelps.jpa.test.postgre_fts.util;

import com.javahelps.jpa.test.postgre_fts.entity.Article;
import com.javahelps.jpa.test.postgre_fts.service.ArticleService;

import javax.persistence.EntityManager;

public class TestDataInitializer {

    public static void initArticle(ArticleService articleService) {

        String title1 = "Java Core. Практическая задача 7.2.13";
        String content1 = "<div class=\"lesson-question\">\n" +
                "                        <div class=\"lesson-description\" id=\"lesson-description\"><p>В этой задаче вам предстоит самостоятельно написать набор классов таким образом, чтобы данный код успешно компилировался и выполнялся.</p>\n" +
                "\n" +
                "<p>Вам предстоит использовать новые знания про generics, коллекции и Stream API.</p>\n" +
                "\n" +
                "<p>В приведенном коде используется оператор;<b>assert</b>. Этот оператор используется для того, чтобы проверять определенные инварианты в коде. С помощью него возможно писать небольшие тесты и следить за корректностью своей программы (в обычной ситуации предпочтительно для этих целей использовать библиотеки для модульного тестирования, которые выходят за рамки базового курса).</p>\n" +
                "\n" +
                "<p>Оператор выглядит следующим образом:<br></br>\n" +
                "<b>assert</b><i>предикат</i><i>сообщение</i>;<br></br>\n" +
                "Предикат – выражение с типом boolean. Если выражение является ложным, то в программе возникает исключение AssertionError с соответствующим сообщением.</p>\n" +
                "\n" +
                "<p>По-умолчанию данная функциональность отключена. Чтобы ее включить, необходимо передать специальный ключ<b>-ea</b>в параметры виртуальной машины. Сделать это можно прямо при запуске в консоли с помощью программы java, либо указав этот ключ в настройках запуска программы в вашей IDE. В случае IntellijIDEA, например, эта опция указывается в поле Run -&gt; Edit Configurations... -&gt; конфигурация запуска вашей программы -&gt; VM Options.</p>\n" +
                "\n" +
                "<p>Код, который необходимо заставить успешно работать:</p>\n" +
                "\n" +
                "<pre><code class=\"language-java hljs\"><span class=\"hljs-comment\">// Random variables</span>\n" +
                "String randomFrom = <span class=\"hljs-string\">\"...\"</span>; <span class=\"hljs-comment\">// Некоторая случайная строка. Можете выбрать ее самостоятельно. </span>\n" +
                "String randomTo = <span class=\"hljs-string\">\"...\"</span>;  <span class=\"hljs-comment\">// Некоторая случайная строка. Можете выбрать ее самостоятельно.</span>\n" +
                "<span class=\"hljs-keyword\">int</span> randomSalary = <span class=\"hljs-number\">100</span>;  <span class=\"hljs-comment\">// Некоторое случайное целое положительное число. Можете выбрать его самостоятельно.</span>\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Создание списка из трех почтовых сообщений.</span>\n" +
                "MailMessage firstMessage = <span class=\"hljs-keyword\">new</span> MailMessage(\n" +
                "        <span class=\"hljs-string\">\"Robert Howard\"</span>,\n" +
                "        <span class=\"hljs-string\">\"H.P. Lovecraft\"</span>,\n" +
                "        <span class=\"hljs-string\">\"This \\\"The Shadow over Innsmouth\\\" story is real masterpiece, Howard!\"</span>\n" +
                ");\n" +
                "\n" +
                "<span class=\"hljs-keyword\">assert</span> firstMessage.getFrom().equals(<span class=\"hljs-string\">\"Robert Howard\"</span>): <span class=\"hljs-string\">\"Wrong firstMessage from address\"</span>;\n" +
                "<span class=\"hljs-keyword\">assert</span> firstMessage.getTo().equals(<span class=\"hljs-string\">\"H.P. Lovecraft\"</span>): <span class=\"hljs-string\">\"Wrong firstMessage to address\"</span>;\n" +
                "<span class=\"hljs-keyword\">assert</span> firstMessage.getContent().endsWith(<span class=\"hljs-string\">\"Howard!\"</span>): <span class=\"hljs-string\">\"Wrong firstMessage content ending\"</span>;\n" +
                "\n" +
                "MailMessage secondMessage = <span class=\"hljs-keyword\">new</span> MailMessage(\n" +
                "        <span class=\"hljs-string\">\"Jonathan Nolan\"</span>,\n" +
                "        <span class=\"hljs-string\">\"Christopher Nolan\"</span>,\n" +
                "        <span class=\"hljs-string\">\"Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!\"</span>\n" +
                ");\n" +
                "\n" +
                "MailMessage thirdMessage = <span class=\"hljs-keyword\">new</span> MailMessage(\n" +
                "        <span class=\"hljs-string\">\"Stephen Hawking\"</span>,\n" +
                "        <span class=\"hljs-string\">\"Christopher Nolan\"</span>,\n" +
                "        <span class=\"hljs-string\">\"Я так и не понял Интерстеллар.\"</span>\n" +
                ");\n" +
                "\n" +
                "List&lt;MailMessage&gt; messages = Arrays.asList(\n" +
                "        firstMessage, secondMessage, thirdMessage\n" +
                ");\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Создание почтового сервиса.</span>\n" +
                "MailService&lt;String&gt; mailService = <span class=\"hljs-keyword\">new</span> MailService&lt;&gt;();\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Обработка списка писем почтовым сервисом</span>\n" +
                "messages.stream().forEachOrdered(mailService);\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Получение и проверка словаря \"почтового ящика\",</span>\n" +
                "<span class=\"hljs-comment\">//   где по получателю можно получить список сообщений, которые были ему отправлены</span>\n" +
                "Map&lt;String, List&lt;String&gt;&gt; mailBox = mailService.getMailBox();\n" +
                "\n" +
                "<span class=\"hljs-keyword\">assert</span> mailBox.get(<span class=\"hljs-string\">\"H.P. Lovecraft\"</span>).equals(\n" +
                "        Arrays.asList(\n" +
                "                <span class=\"hljs-string\">\"This \\\"The Shadow over Innsmouth\\\" story is real masterpiece, Howard!\"</span>\n" +
                "        )\n" +
                "): <span class=\"hljs-string\">\"wrong mailService mailbox content (1)\"</span>;\n" +
                "\n" +
                "<span class=\"hljs-keyword\">assert</span> mailBox.get(<span class=\"hljs-string\">\"Christopher Nolan\"</span>).equals(\n" +
                "        Arrays.asList(\n" +
                "                <span class=\"hljs-string\">\"Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!\"</span>,\n" +
                "                <span class=\"hljs-string\">\"Я так и не понял Интерстеллар.\"</span>\n" +
                "        )\n" +
                "): <span class=\"hljs-string\">\"wrong mailService mailbox content (2)\"</span>;\n" +
                "\n" +
                "<span class=\"hljs-keyword\">assert</span> mailBox.get(randomTo).equals(Collections.&lt;String&gt;emptyList()): <span class=\"hljs-string\">\"wrong mailService mailbox content (3)\"</span>;\n" +
                "\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Создание списка из трех зарплат.</span>\n" +
                "Salary salary1 = <span class=\"hljs-keyword\">new</span> Salary(<span class=\"hljs-string\">\"Facebook\"</span>, <span class=\"hljs-string\">\"Mark Zuckerberg\"</span>, <span class=\"hljs-number\">1</span>);\n" +
                "Salary salary2 = <span class=\"hljs-keyword\">new</span> Salary(<span class=\"hljs-string\">\"FC Barcelona\"</span>, <span class=\"hljs-string\">\"Lionel Messi\"</span>, Integer.MAX_VALUE);\n" +
                "Salary salary3 = <span class=\"hljs-keyword\">new</span> Salary(randomFrom, randomTo, randomSalary);\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Создание почтового сервиса, обрабатывающего зарплаты.</span>\n" +
                "MailService&lt;Integer&gt; salaryService = <span class=\"hljs-keyword\">new</span> MailService&lt;&gt;();\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Обработка списка зарплат почтовым сервисом</span>\n" +
                "Arrays.asList(salary1, salary2, salary3).forEach(salaryService);\n" +
                "\n" +
                "<span class=\"hljs-comment\">// Получение и проверка словаря \"почтового ящика\",</span>\n" +
                "<span class=\"hljs-comment\">//   где по получателю можно получить список зарплат, которые были ему отправлены.</span>\n" +
                "Map&lt;String, List&lt;Integer&gt;&gt; salaries = salaryService.getMailBox();\n" +
                "<span class=\"hljs-keyword\">assert</span> salaries.get(salary1.getTo()).equals(Arrays.asList(<span class=\"hljs-number\">1</span>)): <span class=\"hljs-string\">\"wrong salaries mailbox content (1)\"</span>;\n" +
                "<span class=\"hljs-keyword\">assert</span> salaries.get(salary2.getTo()).equals(Arrays.asList(Integer.MAX_VALUE)): <span class=\"hljs-string\">\"wrong salaries mailbox content (2)\"</span>;\n" +
                "<span class=\"hljs-keyword\">assert</span> salaries.get(randomTo).equals(Arrays.asList(randomSalary)): <span class=\"hljs-string\">\"wrong salaries mailbox content (3)\"</span>;</code></pre>\n" +
                "\n" +
                "<p>В конечном итоге, вам нужно реализовать классы MailService, MailMessage и Salary (и, вероятно, вспомогательные классы и интерфейсы) и отправить их код в форму. Все классы должны быть публичными и статическими (ваши классы подставятся во внешний класс для тестирования).</p>\n" +
                "\n" +
                "<p>В идеологически правильном решении не должно фигурировать ни одного оператора instanceof.</p>\n" +
                "\n" +
                "<p>В классе для тестирования объявлены следующие импорты:</p>\n" +
                "\n" +
                "<pre><code class=\"hljs nginx\"><span class=\"hljs-attribute\">import</span> <span class=\"hljs-regexp\">java.util.*</span>;\n" +
                "<span class=\"hljs-attribute\">import</span> <span class=\"hljs-regexp\">java.util.function.*</span>;</code></pre>\n" +
                "</div>\n" +
                "                    </div>";

        String title2 = "Что нового будет в Java 14";
        String content2 = "<!DOCTYPE DOCTYPE SYSTEM \"SYSTEM\" [\n" +
                "<!ELEMENT DOCTYPE (ELEMENT+)>\n" +
                "<!ATTLIST ELEMENT ATTLIST ENTITY #IMPLIED>\n" +
                "<!NOTATION DOCTYPE SYSTEM \"ENTITY\">\n" +
                "<!ENTITY NOTATION SYSTEM \"ENTITY\" NDATA DOCTYPE>\n" +
                "]>\n" +
                "<DOCTYPE>\n" +
                "<div class=\"post__text post__text-html post__text_v1\" id=\"post-content-body\" data-io-article-url=\"https://habr.com/ru/company/alconost/news/t/491378/\"><a href=\"https://habrahabr.ru/company/alconost/blog/491378/\"><div style=\"text-align:center;\"><img src=\"https://habrastorage.org/webt/wb/ty/nf/wbtynfrtnkc467x98ykr4fyqfvk.jpeg\"></img></div></a><br></br>\n" +
                "Java 14 должна выйти позже в этом месяце — с рядом изменений.<br></br>\n" +
                "<br></br>\n" +
                "Какие изменения планируется включить в обновление:<br></br>\n" +
                "<br></br>\n" +
                "<ol>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/305\">JEP 305: сопоставление шаблонов для «instanceof» (предварительная версия)</a>. Сопоставление шаблонов позволяет выражать обычную логику «кратко и безопасно». Согласно документации OpenJDK, сейчас существуют только специализированные решения для сопоставления шаблонов, поэтому авторы посчитали, что пришло время существенно расширить использование сопоставления шаблонов в Java.&nbsp;<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/343\">JEP 343: упаковщик (инкубатор)</a>. Этот инструмент позволяет создавать установочные пакеты для автономных Java-приложений.&nbsp;<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/345\">JEP 345: выделение памяти с поддержкой NUMA для G1</a>. Предполагается, что это улучшит производительность G1 на больших машинах.<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/349\">JEP 349: потоки событий JFR</a>. Это позволит непрерывно считывать данные профилировщика JDK Flight Recorder.<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/352\">JEP 352: сопоставленные байтовые буферы в энергонезависимой памяти</a>. В этом выпуске добавлены новые режимы сопоставления файлов, которые позволяют использовать API-интерфейс FileChannel для создания экземпляров MappedByteBuffer, ссылающихся на энергонезависимую память.<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/358\">JEP 358: полезная информация в исключениях NullPointerException</a>. Теперь исключения NullPointerException, генерируемые виртуальной Java-машиной, будут указывать, какая переменная оказалась «null».<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/359\">JEP 359: записи (предварительная версия)</a>. Записи дают синтаксис для объявления классов, действующих как удобные и понятные хранилища неизменяемых данных. Одна из основных претензий к Java в том, что приходится писать слишком много кода, особенно когда речь идет о классах. В документации OpenJDK говорится, что из-за этого разработчики иногда пытаются изловчиться и облегчить себе работу, что приводит к проблемам в будущем.<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/361\">JEP 361: «switch» как выражение</a>. Теперь «switch» можно использовать и как оператор, и как выражение. Это упростит использование Java и заложит фундамент для реализации сопоставления шаблонов в «switch». Ранее эта функция была представлена в виде предварительной версии в JDK 12 и JDK 13.&nbsp;<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/362\">JEP 362: устаревание портов на Solaris и SPARC</a>. Начиная с этого выпуска, данные порты будут считаться нерекомендуемыми к использованию, а в одном из следующих выпусков будут полностью удалены.&nbsp;<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/363\">JEP 363: удаление сборщика мусора (GC), работающего по алгоритму маркировки и очистки (CMS)</a>. Сборщик мусора CMS уже более двух лет считается устаревшим&nbsp;— с тех пор внимание было обращено на улучшение других сборщиков. В частности, были представлены два новых: ZGC и Shenandoah. Команда разработчиков считает, что теперь CMS можно спокойно удалять, и ожидает, что будущие улучшения в других сборщиках мусора еще больше снизят потребность в CMS.&nbsp;<br></br>\n" +
                "</li>\n" +
                "<li>JEP 364 и 365: ZGC в <a href=\"https://openjdk.java.net/jeps/364\">macOs</a> и <a href=\"https://openjdk.java.net/jeps/365\">Windows</a>. Сборщик мусора ZCG был портирован на macOS и Windows.<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/366\">JEP 366: устаревание комбинации алгоритмов сбора мусора ParallelScavenge + SerialOld</a>. По словам команды разработчиков, эта комбинация используется редко, но требует значительных усилий по поддержке. Они считают, что такая комбинация полезна только при развертывании, которое сочетает в себе очень большой сборщик мусора нового поколения и очень маленький старого поколения.&nbsp;<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/367\">JEP 367: удаление API и инструментов Pack200</a>. Эти инструменты считаются устаревшими с версии Java SE 11.<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/368\">JEP 368: текстовые блоки (вторая предварительная версия)</a>. Добавлены текстовые блоки&nbsp;— многострочные строковые литералы, которые можно использовать без escape-последовательностей. Это позволяет обеспечить предсказуемое поведение форматирования строк и контролировать их отображение.&nbsp;<br></br>\n" +
                "</li>\n" +
                "<li><a href=\"https://openjdk.java.net/jeps/370\">JEP 370: API доступа к внешней памяти (инкубатор)</a>. Этот API даст возможность приложениям безопасно и эффективно получать доступ к внешней памяти («foreign memory») вне «кучи» Java.<br></br>\n" +
                "</li>\n" +
                "</ol><br></br>\n" +
                "<b>Новость переведена в <a href=\"https://alconost.com/ru/?utm_source=habrahabr&amp;utm_medium=article&amp;utm_campaign=news&amp;utm_content=java14\">Alconost</a></b>, профессиональной студии по переводу и локализации</div>" +
                "</DOCTYPE>";

        String title3 = "Facebook и Google выпустили Yarn, новый менеджер пакетов для JavaScript";
        String content3 = "<!DOCTYPE DOCTYPE SYSTEM \"SYSTEM\" [\n" +
                "<!ELEMENT DOCTYPE (ELEMENT+)>\n" +
                "<!ATTLIST ELEMENT ATTLIST ENTITY #IMPLIED>\n" +
                "<!NOTATION DOCTYPE SYSTEM \"ENTITY\">\n" +
                "<!ENTITY NOTATION SYSTEM \"ENTITY\" NDATA DOCTYPE>\n" +
                "]>\n" +
                "<DOCTYPE>\n" +
                "<div class=\"post__text post__text-html post__text_v1\" id=\"post-content-body\" data-io-article-url=\"https://habr.com/ru/news/t/312458/\"><img src=\"https://habrastorage.org/getpro/habr/post_images/fec/4c7/daa/fec4c7daa10f56d75bf32e585070cc39.jpg\"></img><br></br>\n" +
                "<br></br>\n" +
                "Вчера вечером Facebook официально анонсировала новый пакетный менеджер для JavaScript под названием Yarn. На одной из стадии разработки к проекту подключились компании Google, Exponent и Tilde.<br></br>\n" +
                "<br></br>\n" +
                "«Самый популярный менеджер пакетов JavaScript — это NPM. Он обеспечивает доступ более чем к 300 тысячам пакетов. Используют его более 5 миллионов разработчиков, а ежемесячно к нему обращаются для загрузки более 5 миллиардов раз. <br></br>\n" +
                "<br></br>\n" +
                "Мы успешно использовали NPM в Facebook в течение многих лет, но так как объем нашего кода и число разработчиков выросло, мы столкнулись с проблемами последовательности, безопасности и производительности. После попытки решить все эти вопросы, мы пришли к намерению создать собственное решение, чтобы обеспечить надежность управления разработкой. Итогом этой работы стал Yarn — быстрая, надежная и безопасная альтернатива клиенту NPM», — говорится в <a href=\"https://code.facebook.com/posts/1840075619545360\">официальном блоге Facebook</a> о новинке.<br></br>\n" +
                "<a name=\"habracut\"></a><br></br>\n" +
                "Разработчики Facebook утверждают, что Yarn все так же позволяет получить доступ к пакетам NPM, но при этом позволяет быстрее и последовательно управлять зависимостями между машинами, или работать в защищенной среде в автономном режиме. Это, по мнению создателей Yarn, позволит разработчикам сосредоточиться на том, что на самом деле важно — на создании новых продуктов и функций. Вот перечень основных отличительных особенностей Yarn:<br></br>\n" +
                "<br></br>\n" +
                "<ul>\n" +
                "<li>автономный режим;</li>\n" +
                "<li>детерминированность;</li>\n" +
                "<li>производительность сети;</li>\n" +
                "<li>наличие нескольких реестров;</li>\n" +
                "<li>сетевая гибкость;</li>\n" +
                "<li>наличие Flat Mode;</li>\n" +
                "<li>больше эмодзи (и с котиками тоже).</li>\n" +
                "</ul><br></br>\n" +
                "У социальной сети было несколько причин для создания собственной альтернативы NPM. Конечно же, главной из них была производительность, а также скорость установки и распараллеливание операций. Еще Yarn позволяет достигнуть единообразия на разных машинах. В случае NPM, в зависимости от подключенных модулей, каталог node_modules мог сильно отличаться от машины к машине. В случае небольших команд, занимающихся разработкой, подобная кастомизация может и быть приемлемой, однако не в случае огромной DevOps-команды Facebook. <br></br>\n" +
                "<br></br>\n" +
                "Разработчики оригинального NPM — коммерческая организация, которая была в курсе создания и скорого выхода в свет конкурента. Однако, бизнес-модель проекта построена не вокруг клиента, а вокруг каталога, который также используется и Yarn. Поэтому новинка от Facebook и Google не представляет для них большой угрозы. <br></br>\n" +
                "<br></br>\n" +
                "Команда Facebook решила вынести свою разработку за пределы внутреннего репозитория компании и <a href=\"https://github.com/yarnpkg/yarn\">выложила Yarn на GitHub</a>, где можно ознакомиться с проектом и принять участие в разработке.</div>" +
                "</DOCTYPE>";

        String title4 = "Замыкание в Java Script для непосвященных";
        String content4 = "<!DOCTYPE DOCTYPE SYSTEM \"SYSTEM\" [\n" +
                "<!ELEMENT DOCTYPE (ELEMENT+)>\n" +
                "<!ATTLIST ELEMENT ATTLIST ENTITY #IMPLIED>\n" +
                "<!NOTATION DOCTYPE SYSTEM \"ENTITY\">\n" +
                "<!ENTITY NOTATION SYSTEM \"ENTITY\" NDATA DOCTYPE>\n" +
                "]>\n" +
                "<DOCTYPE>\n" +
                "<div class=\"post__text post__text-html post__text_v1\" id=\"post-content-body\" data-io-article-url=\"https://habr.com/ru/post/37236/\">У моих программистов была задача: в фото галерее при активном изображении подсвечивать thumbnail показываемого изображения. Задача вроде тривиальная, но вызвала у них некоторый затык при решении. Хочу сказать сразу, что владение JS было у них не на высоте.<br></br>\n" +
                "Вообще мало людей по настоящему знающих этот язык и не путующий его с принципами работы DOM документа.<br></br>\n" +
                "<br></br>\n" +
                "В чем же была проблема…<br></br>\n" +
                "1. В том, что сначала они решали её рекурсией :)<br></br>\n" +
                "2. Вторая попытка решить привела к введению глобальной переменной, что я не считаю хорошим тоном<br></br>\n" +
                "3. Загрязнение общего кода подобными маленькими функциями вместо использования объектного подхода<br></br>\n" +
                "<br></br>\n" +
                "В бытности будучи программистом я написал маленький каркас для демонстрации. Вот он:<br></br>\n" +
                "<br></br>\n" +
                "<blockquote><font color=\"black\">&lt;script type=<font color=\"#A31515\">«text/javascript»</font>&gt;<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></br>\n" +
                "<font color=\"#0000ff\">function</font> MainMenu(){<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#0000ff\">var</font> OldImage;<br></br>\n" +
                "<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#0000ff\">this</font>.getOldImage = <font color=\"#0000ff\">function</font>(){ <font color=\"#0000ff\">return</font> OldImage; }<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#0000ff\">this</font>.setOldImage = <font color=\"#0000ff\">function</font>( img ){ OldImage = img;}<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#0000ff\">this</font>.chgImage = <font color=\"#0000ff\">function</font>( obj, newImage){<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#0000ff\">if</font>(newImage != <font color=\"#0000ff\">null</font>){<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#0000ff\">this</font>.setOldImage(obj.src);<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;obj.src = <font color=\"#A31515\">'/images/'</font>+newImage;<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;} <font color=\"#0000ff\">else</font> {<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;obj.src = <font color=\"#0000ff\">this</font>.getOldImage();<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br></br>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br></br>\n" +
                "<br></br>\n" +
                "}<br></br>\n" +
                "<br></br>\n" +
                "<font color=\"#0000ff\">var</font> Menu = <font color=\"#0000ff\">new</font> MainMenu();<br></br>\n" +
                "<br></br>\n" +
                "&lt;/script&gt; </font><font color=\"gray\">* This source code was highlighted with <a href=\"http://source.virtser.net\"><font color=\"gray\">Source Code Highlighter</font></a>.</font></blockquote><br></br>\n" +
                "<br></br>\n" +
                "Вызывать следует так:<br></br>\n" +
                "<br></br>\n" +
                "<blockquote><font color=\"black\">&lt;img src=<font color=\"#A31515\">\"/images/home.gif\"</font> width=<font color=\"#A31515\">«88»</font> height=<font color=\"#A31515\">«23»</font> alt=<font color=\"#A31515\">\"\"</font> onmouseover=<font color=\"#A31515\">«Menu.chgImage(this, 'home_over.gif')»</font> onmouseout=<font color=\"#A31515\">«Menu.chgImage(this)»</font>&gt;</font><font color=\"gray\">* This source code was highlighted with <a href=\"http://source.virtser.net\"><font color=\"gray\">Source Code Highlighter</font></a>.</font></blockquote><br></br>\n" +
                "<br></br>\n" +
                "Что это нам дало:<br></br>\n" +
                "<br></br>\n" +
                "1. Мы не используем рекурсию. Этим мы бережем ресурсы компьютера. У клиентских машин они же не резиновые вопреки обратным утверждениям<br></br>\n" +
                "2. Мы не ввели глобальную переменную, которая в больших системах неудобна по объективням причинам<br></br>\n" +
                "3. В третьих мы сделали универсальный объект. У него инкапсулированы данные!<br></br>\n" +
                "<br></br>\n" +
                "Новичкам просьба разобраться и понять. Этот вариант сделан специально проще, чем мы реализовывали.<br></br>\n" +
                "<br></br>\n" +
                "Мы создаем в теле функции локальную переменную. Затем захватываем эту переменную методом этой функции (это класс, точнее прототип). В итоге получаем хитрый трюк. При выходе из самой функции данные в ней запомнились.<br></br>\n" +
                "<br></br>\n" +
                "Вообще-то по этому вопросу материала достаточно в сети. Этот трюк далеко не новый. Но о нем нужно напоминать «отцам» и учить новичков.</div>" +
                "</DOCTYPE>";



        Article article1 = new Article(title1, content1);
        Article article2 = new Article(title2, content2);
        Article article3 = new Article(title3, content3);
        Article article4 = new Article(title4, content4);

        articleService.persist(article1);
        articleService.persist(article2);
        articleService.persist(article3);
        articleService.persist(article4);

//        entityManager.getTransaction().commit();
    }
}

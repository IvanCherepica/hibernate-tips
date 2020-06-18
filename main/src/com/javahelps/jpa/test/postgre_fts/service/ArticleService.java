package com.javahelps.jpa.test.postgre_fts.service;

import com.javahelps.jpa.test.postgre_fts.entity.Article;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


public class ArticleService {
    static String content1 = "<div class=\"lesson-question\">\n" +
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
            "<pre><code target=\"search\" class=\"hljs nginx\"><span class=\"hljs-attribute\">import</span> <span class=\"hljs-regexp\">java.util.*</span>;\n" +
            "<span class=\"hljs-attribute\">import</span> <span class=\"hljs-regexp\">java.util.function.*</span>;</code></pre>\n" +
            "</div>\n" +
            "                    </div>";
    private static EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Article.class});

    public void persist(Article article) {
        entityManager.getTransaction().begin();

        entityManager.persist(article);

        String textContent = getDivContent(article.getContent());

        entityManager.createNativeQuery(
                "UPDATE article SET vector = to_tsvector(:textContent) WHERE id =:id"
        )
                .setParameter("textContent", textContent)
                .setParameter("id", article.getId())
                .executeUpdate();

        entityManager.getTransaction().commit();
    }

    public static String getDivContent(String html) {
        Document document = Jsoup.parse(html);
        System.out.println(document.title());
        Elements paragraphs = document.getElementsByTag("div");

        StringBuilder builder = new StringBuilder();

        for (Element paragraph : paragraphs) {
            builder.append(paragraph.text());
        }

        return builder.toString();
    }

    public static void main(String[] args) {


        Document document = Jsoup.parse(content1);
        System.out.println(document.title());
        Elements codes = document.select("code[target]");

        List<String> codeContent = new ArrayList<>();

        for (Element code : codes) {
            codeContent.add(code.toString());
        }

        System.out.println(codeContent);
    }
}

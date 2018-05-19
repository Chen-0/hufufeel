<!DOCTYPE html>
<html>

<#assign title="" />
<#include "*/_layout/head.ftl" />

<body>
<div class="container">
    <section id="content" style="margin-top: 200px;">
        <form action="/file/create" method="post" enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="file" name="file" />
            <input type="submit" value="SUBMIT">
        </form>
    </section>
</div>

<script src="/static/js/index.js"></script>
</body>
</html>

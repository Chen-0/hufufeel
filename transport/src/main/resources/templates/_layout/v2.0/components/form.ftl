<#macro formGroup key name>
    <#if ferror?exists && ferror?size gt 0 && ferror[name]?exists>
    <div class="form-group has-error">
        <label for="${name}">${key}：</label>
        <input type="text" class="form-control" id="${name}" name="${name}" value="${fele["${name}"]!}">
        <span class="help-block">${ferror["${name}"]}</span>
    </div>
    <#else>
    <div class="form-group">
        <label for="${name}">${key}：</label>
        <input type="text" class="form-control" id="${name}" name="${name}" value="${fele["${name}"]!}">
    </div>
    </#if>
</#macro>
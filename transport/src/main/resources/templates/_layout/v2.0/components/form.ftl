<#assign MUST="<span class=\"text-danger\">&nbsp;*&nbsp;</span>">

<#macro formGroup label name>
    <#if ferror?exists && ferror?size gt 0 && ferror[name]?exists>
    <div class="form-group has-error">
        <label for="${name}">${label}：</label>
        <input type="text" class="form-control" id="${name}" name="${name}" value="${fele["${name}"]!}">
        <span class="help-block">${ferror[name]}</span>
    </div>
    <#else>
    <div class="form-group">
        <label for="${name}">${label}：</label>
        <input type="text" class="form-control" id="${name}" name="${name}" value="${fele["${name}"]!}">
    </div>
    </#if>
</#macro>

<#macro radioFormGroup label name items>
<div class="form-group">
    <label for="">${label}：</label>

    <div class="radio">
        <#list items as i>
            <#assign foo=false>
            <#if fele[name]?exists>
                <#if fele[name] == i.ordinal()>
                    <#assign foo=true>
                <#else>
                    <#if i_index == 0>
                        <#assign foo = true>
                    </#if>
                </#if>
            </#if>
            <label style="margin-right: 15px;">
                <input type="radio" class="flat-red" name="${name}" value="${i.ordinal()}" ${foo?string("checked=true", "")}>
            ${i.value}
            </label>
        </#list>
    </div>
</div>
</#macro>

<#macro fileFormGroup label name>
    <input type="hidden" id="${name}Id" name="${name}Id" value="${fele["${name}Id"]!}">
    <#if ferror?exists && ferror[name]?exists>
    <div class="form-group has-error">
        <label for="${name}">${label}*：</label>
        <input type="file" id="${name}" name="${name}">
        <span class="help-block">${ferror[name]}</span>
    </div>
    <#else>
    <div class="form-group">
        <label for="${name}">${label}*：</label>
        <input type="file" id="${name}" name="${name}">
        <#if fele["${name}Id"]?exists>
            <p class="help-block">图片已经上传：${fele["${name}Name"]!} </p>
        <#else>
            <p class="help-block">请上传少于100KB的图片</p>
        </#if>
    </div>
    </#if>
</#macro>
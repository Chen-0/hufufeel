<ul class="pagination pagination-sm no-margin pull-right">
<#if elements.totalPages gt 1 >
    <li><a href="${BASEURL}0">«</a></li>
</#if>

<#assign _start=elements.number-3>
<#if _start lte 0>
    <#assign _start=0>
</#if>

<#assign _end=elements.number-1>

<#if (_start != elements.number) >
    <#list _start.._end as n>
        <li><a href="${BASEURL}${n}">${n+1}</a></li>
    </#list>
</#if>

    <li class="active"><a href="${BASEURL}${elements.number}">${elements.number+1}</a></li>

<#assign _start=elements.number+1>

<#assign _end=elements.number+3>
<#if _end gte elements.totalPages>
    <#assign _end=elements.totalPages-1>
</#if>

<#if (_end != elements.number) && _start <= _end >
    <#list _start.._end as n>
        <li><a href="${BASEURL}${n}">${n+1}</a></li>
    </#list>
</#if>

<#if elements.totalPages gt 1 >
    <li><a href="${BASEURL}${elements.totalPages-1}">»</a></li>
</#if>
</ul>

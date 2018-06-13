<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/jQuery-slimScroll/1.3.8/jquery.slimscroll.min.js"></script>
<script src="https://cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-datepicker/1.8.0/js/bootstrap-datepicker.min.js"></script>
<script src="https://cdn.bootcss.com/iCheck/1.0.2/icheck.min.js"></script>
<script src="https://cdn.bootcss.com/sweetalert/2.1.0/sweetalert.min.js"></script>

<script src="/static/LTE/dist/js/adminlte.min.js"></script>
<script src="/static/LTE/dist/js/bkb.js"></script>
<script>$(function(){$.ajaxSetup({headers:{"X-CSRF-Token":"${_csrf.token!}"},method:"post",dataType:"json"});$("#logoutBtn").click(function(e){e.preventDefault();$("#logoutForm").submit()})});function isEmpty(A){if(A===null||A===undefined||A===""){return true}return false}function accDiv(arg1,arg2){var t1=0,t2=0,r1,r2;try{t1=arg1.toString().split(".")[1].length}catch(e){}try{t2=arg2.toString().split(".")[1].length}catch(e){}with(Math){r1=Number(arg1.toString().replace(".",""));r2=Number(arg2.toString().replace(".",""));return accMul((r1/r2),pow(10,t2-t1))}}function accMul(arg1,arg2){var m=0,s1=arg1.toString(),s2=arg2.toString();try{m+=s1.split(".")[1].length}catch(e){}try{m+=s2.split(".")[1].length}catch(e){}return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)}function accAdd(arg1,arg2){var r1,r2,m;try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}m=Math.pow(10,Math.max(r1,r2));return(arg1*m+arg2*m)/m}function Subtr(arg1,arg2){var r1,r2,m,n;try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}m=Math.pow(10,Math.max(r1,r2));n=(r1>=r2)?r1:r2;return((arg1*m-arg2*m)/m).toFixed(n)};</script>

<#--<script>-->
    <#--$(function () {-->
        <#--$.ajaxSetup({-->
            <#--headers:{-->
                <#--'X-CSRF-Token': '${_csrf.token!}'-->
            <#--},-->
            <#--method: 'post',-->
            <#--dataType: 'json'-->
        <#--});-->

        <#--$('#logoutBtn').click(function (e) {-->
            <#--e.preventDefault();-->

            <#--$('#logoutForm').submit();-->
        <#--})-->
    <#--});-->

    <#--function isEmpty(A) {-->
        <#--if (A === null || A === undefined || A === "") {-->
            <#--return true;-->
        <#--}-->
        <#--return false;-->
    <#--}-->

    <#--function accDiv(arg1,arg2){-->
        <#--var t1=0,t2=0,r1,r2;-->
        <#--try{t1=arg1.toString().split(".")[1].length}catch(e){}-->
        <#--try{t2=arg2.toString().split(".")[1].length}catch(e){}-->
        <#--with(Math){-->
            <#--r1=Number(arg1.toString().replace(".",""));-->
            <#--r2=Number(arg2.toString().replace(".",""));-->
            <#--return accMul((r1/r2),pow(10,t2-t1));-->
        <#--}-->
    <#--}-->
    <#--//乘法-->
    <#--function accMul(arg1,arg2)-->
    <#--{-->
        <#--var m=0,s1=arg1.toString(),s2=arg2.toString();-->
        <#--try{m+=s1.split(".")[1].length}catch(e){}-->
        <#--try{m+=s2.split(".")[1].length}catch(e){}-->
        <#--return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)-->
    <#--}-->
    <#--//加法-->
    <#--function accAdd(arg1,arg2){-->
        <#--var r1,r2,m;-->
        <#--try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}-->
        <#--try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}-->
        <#--m=Math.pow(10,Math.max(r1,r2));-->
        <#--return (arg1*m+arg2*m)/m-->
    <#--}-->
    <#--//减法-->
    <#--function Subtr(arg1,arg2){-->
        <#--var r1,r2,m,n;-->
        <#--try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}-->
        <#--try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}-->
        <#--m=Math.pow(10,Math.max(r1,r2));-->
        <#--n=(r1>=r2)?r1:r2;-->
        <#--return ((arg1*m-arg2*m)/m).toFixed(n);-->
    <#--}-->
<#--</script>-->

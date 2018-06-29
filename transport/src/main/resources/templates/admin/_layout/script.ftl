<script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/moment.js/2.18.1/moment.min.js"></script>
<#--<script src="//cdn.bootcss.com/metisMenu/2.7.0/metisMenu.min.js"></script>-->
<#--<script src="//cdn.bootcss.com/startbootstrap-sb-admin-2/3.3.7+1/js/sb-admin-2.min.js"></script>-->
<script src="//cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
<#--<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.15/js/jquery.dataTables.js"></script>-->
<script src="//cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/js/script.js"></script>
<script src="/static/common/submitAsJson.js"></script>
<script src="https://cdn.bootcss.com/iCheck/1.0.2/icheck.min.js"></script>
<script>$(function(){$.ajaxSetup({headers:{"X-CSRF-Token":"${_csrf.token!}"},method:"post",dataType:"json"});$("#logoutBtn").click(function(e){e.preventDefault();$("#logoutForm").submit()})});function isEmpty(A){if(A===null||A===undefined||A===""){return true}return false}function accDiv(arg1,arg2){var t1=0,t2=0,r1,r2;try{t1=arg1.toString().split(".")[1].length}catch(e){}try{t2=arg2.toString().split(".")[1].length}catch(e){}with(Math){r1=Number(arg1.toString().replace(".",""));r2=Number(arg2.toString().replace(".",""));return accMul((r1/r2),pow(10,t2-t1))}}function accMul(arg1,arg2){var m=0,s1=arg1.toString(),s2=arg2.toString();try{m+=s1.split(".")[1].length}catch(e){}try{m+=s2.split(".")[1].length}catch(e){}return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)}function accAdd(arg1,arg2){var r1,r2,m;try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}m=Math.pow(10,Math.max(r1,r2));return(arg1*m+arg2*m)/m}function Subtr(arg1,arg2){var r1,r2,m,n;try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}m=Math.pow(10,Math.max(r1,r2));n=(r1>=r2)?r1:r2;return((arg1*m-arg2*m)/m).toFixed(n)};</script>

<script>
    $(".xn-form").submit(function(event) {
        event.preventDefault();
        submitAsJSON(this);
    });
    $(document).ready(function () {
        var msgContainer = $('.message-container');

        if (msgContainer.length > 0) {
            msgContainer.fadeOut(3000);
        }

        $('#logoutBtn').click(function (e) {
            e.preventDefault();

            $('#logoutForm').submit();
        })
    });
</script>

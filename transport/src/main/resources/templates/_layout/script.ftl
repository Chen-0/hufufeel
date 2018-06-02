<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/jQuery-slimScroll/1.3.8/jquery.slimscroll.min.js"></script>
<script src="https://cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-datepicker/1.8.0/js/bootstrap-datepicker.min.js"></script>
<script src="https://cdn.bootcss.com/iCheck/1.0.2/icheck.min.js"></script>

<script src="/static/LTE/dist/js/adminlte.min.js"></script>
<script src="/static/LTE/dist/js/bkb.js"></script>
<script>
    $(function () {
        $.ajaxSetup({
            headers:{
                'X-CSRF-Token': '${_csrf.token!}'
            },
            method: 'post',
            dataType: 'json'
        });
    });
</script>

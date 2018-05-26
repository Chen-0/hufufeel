
<!-- jQuery 3 -->
<script src="/static/LTE/bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="/static/LTE/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="/static/LTE/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="/static/LTE/bower_components/fastclick/lib/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="/static/LTE/dist/js/adminlte.min.js"></script>

<script src="/static/LTE/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="/static/LTE/plugins/iCheck/icheck.min.js"></script>
<script src="/static/LTE/dist/js/bkb.js"></script
<script>
    $.ajaxSetup({
        headers:{
            'X-CSRF-Token': '${_csrf.token!}'
        },
        method: 'post',
        dataType: 'json'
    });
</script>

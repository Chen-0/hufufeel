<script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/moment.js/2.18.1/moment.min.js"></script>
<script src="//cdn.bootcss.com/metisMenu/2.7.0/metisMenu.min.js"></script>
<script src="//cdn.bootcss.com/startbootstrap-sb-admin-2/3.3.7+1/js/sb-admin-2.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.15/js/jquery.dataTables.js"></script>
<script src="//cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/js/script.js"></script>
<script>
    $(document).ready(function () {
        $.ajaxSetup({
            headers: {
                'X-CSRF-TOKEN': '${_csrf.token}'
            },
            dataType: 'json',
            type: 'post'
        });
    });

    var msgContainer = $('.message-container');

    if (msgContainer.length > 0) {
        msgContainer.fadeOut(3000);
    }

    $('#logout').click(function () {
        $('#logoutForm').submit();
    });
</script>


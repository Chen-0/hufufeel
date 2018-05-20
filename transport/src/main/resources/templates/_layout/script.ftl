<!-- Jquery Core Js -->
<script src="/static/plugins/jquery/jquery.min.js"></script>

<!-- Bootstrap Core Js -->
<script src="/static/plugins/bootstrap/js/bootstrap.js"></script>

<!-- Select Plugin Js -->
<script src="/static/plugins/bootstrap-select/js/bootstrap-select.js"></script>

<!-- Slimscroll Plugin Js -->
<script src="/static/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>

<!-- Waves Effect Plugin Js -->
<script src="/static/plugins/node-waves/waves.js"></script>

<!-- Custom Js -->
<script src="/static/js/admin.js"></script>

<!-- Demo Js -->
<script src="/static/js/demo.js"></script>
<script>
    $(function () {
        $(".remove").click(function (e) {
            e.preventDefault();

            var url = $(this).attr("href");

            if (confirm("确认删除该标签")) {
                window.location.href = url;
            }
        });
    })
</script>
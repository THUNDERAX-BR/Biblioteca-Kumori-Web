$(document).ready(function () {

    $("#btnNavBuscarLivros").on("click", function () {
        location.href = "/buscarlivros";
    });

    $("#btnNavBuscarAutores").on("click", function () {
        location.href = "/buscarautores";
    });

    $("#btnNavGerenciar").on("click", function () {
        location.href = "/gerenciar";
    });

    $("#btnNavSair").on("click", function () {
        location.href = "/login/logout";
    });

    $("#btnCadastroLivro").on("click", function () {
        location.href = "/livro/cadastro";
    });

    $(document).on("click", ".btnInfoLivro", function () {
        const id = $(this).data("id");
        location.href = "/infolivro?id=" + id;
    });

    $("#btnOpenMenu").on("click", function () {
        $("#sideMenu").css("left", "0");
        $("#menuOverlay").show();
    });

    $("#menuOverlay").on("click", function () {
        $("#sideMenu").css("left", "-220px");
        $(this).hide();
    });

    $(window).on("resize", function () {
        if (window.innerWidth > 768) {
            $("#sideMenu").css("left", "0");
            $("#menuOverlay").hide();
        }
    });

    $("#btnFiltro").on("click", function () {
        $("#filterOverlay").fadeIn();
    });

    $("#btnCloseFiltro").on("click", function () {
        $("#filterOverlay").fadeOut();
    });

    $("#filterOverlay").on("click", function (e) {
        if (e.target === this) {
            $("#filterOverlay").fadeOut();
        }
    });

    $("#btnLimparFiltro").on("click", function () {
        $("#anoDe").val("");
        $("#anoAte").val("");
        $("#categoria").val("");
    });

    $(".btnBuscarGlobal").on("click", function () {

        const busca = $("#inputBusca").val();
        const anoIni = $("#anoDe").val();
        const anoFim = $("#anoAte").val();
        const categoria = $("#categoria").val();

        let url = "/buscarlivros?busca=" + encodeURIComponent(busca);

        if (anoIni) url += "&anoIni=" + anoIni;
        if (anoFim) url += "&anoFim=" + anoFim;
        if (categoria) url += "&categoria=" + encodeURIComponent(categoria);

        location.href = url;
    });

});
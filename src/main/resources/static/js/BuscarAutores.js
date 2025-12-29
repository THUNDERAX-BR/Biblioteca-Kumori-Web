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

    $("#btnCadastroAutor").on("click", function () {
        location.href = "/autor/cadastro";
    });

    $(document).on("click", ".btnInfoAutor", function () {
        const id = $(this).closest("tr").find("td:first").text();
        location.href = "/infoautor?id=" + id;
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

    $("#btnFiltroAutores").on("click", function () {
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
        $("#movimentoAutor").val("");
        $("#inputBuscaAutor").val("");
    });

    $(".btnBuscarGlobal").on("click", function () {
        const busca = $("#inputBuscaAutor").val();
        const movimento = $("#movimentoAutor").val();

        let url = "/buscarautores?busca=" + encodeURIComponent(busca);
        if (movimento) url += "&movimento=" + encodeURIComponent(movimento);

        location.href = url;
    });

});

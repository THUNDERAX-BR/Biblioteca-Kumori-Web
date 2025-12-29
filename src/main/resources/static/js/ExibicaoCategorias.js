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

    $("#btnCadastroCategoria").on("click", function () {
        location.href = "/categoria/cadastro";
    });

    $(".btnEditarCategoria").on("click", function () {
        var id = $(this).data("id");
        location.href = "/categoria/cadastro?id=" + id;
    });

    $(".btnExcluirCategoria").on("click", function () {
        var id = $(this).data("id");
        if (confirm("Deseja realmente excluir a categoria " + id + "?")) {
            $("<form>", { method: "post", action: "/categoria/excluir/" + id }).appendTo("body").submit();
        }
    });

    $("#btnBuscarCategoria").on("click", function () {
        var busca = $("#inputBusca").val();
        location.href = "/categoria/exibicao?busca=" + encodeURIComponent(busca);
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
});

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

    $("#btnCadastroMovimento").on("click", function () {
        location.href = "/movimento/cadastro";
    });

    $(".btnEditarMovimento").on("click", function () {
        var id = $(this).data("id");
        location.href = "/movimento/cadastro?id=" + id;
    });

    $(".btnExcluirMovimento").on("click", function () {
        var id = $(this).data("id");
        if (confirm("Deseja realmente excluir o movimento " + id + "?")) {
            $("<form>", {method: "post", action: "/movimento/excluir/" + id}).appendTo("body").submit();
        }
    });

    $("#btnBuscarMovimento").on("click", function () {
        var busca = $("#inputBusca").val();
        location.href = "/movimento/exibicao?busca=" + encodeURIComponent(busca);
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

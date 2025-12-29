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

    $("#btnCadastroEditora").on("click", function () {
        location.href = "/editora/cadastro";
    });

    $(".btnEditarEditora").on("click", function () {
        var id = $(this).data("id");
        location.href = "/editora/cadastro?id=" + id;
    });

    $(".btnExcluirEditora").on("click", function () {
        var id = $(this).data("id");
        if (confirm("Deseja realmente excluir a editora " + id + "?")) {
            $("<form>", {method: "post", action: "/editora/excluir/" + id}).appendTo("body").submit();
        }
    });

    $("#btnBuscarEditora").on("click", function () {
        var busca = $("#inputBusca").val();
        location.href = "/editora/exibicao?busca=" + encodeURIComponent(busca);
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

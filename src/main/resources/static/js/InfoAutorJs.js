//Esse aqui também aparentemente :(
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

    $("#btnEditarAutor").on("click", function () {
        const id = $(this).data("id");
        location.href = `/autor/cadastro?id=${id}`;
    });

    $("#btnExcluirAutor").on("click", function () {
        const id = $(this).data("id");
        if (confirm("Deseja realmente excluir este autor?")) {
            const form = $("#formExcluirAutor");
            form.attr("action", `/autor/excluir/${id}`);
            form.submit();
        }
    });

    $(".btnInfoLivro").on("click", function () {
        const id = $(this).data("id");
        location.href = `/infolivro?id=${id}`;
    });

});

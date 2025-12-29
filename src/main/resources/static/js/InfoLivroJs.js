//Por algum motivo, o programa decidiu que não quer que esse arquivo especificamente tenha o nome InfoLivro.js, senão ele usa um outro arquivo completamente aleatorio, eu não sei o porque, não deveria existir nenhum outro arquivo com esse nome, agora esse aqui é o UNICO js que não segue o padrão
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

    $("#btnEditarLivro").on("click", function () {
        const id = $(this).data("id");
        location.href = `/livro/cadastro?id=${id}`;
    });

    $("#btnNovoExemplar").on("click", function () {
        const idLivro = $(this).data("id");
        location.href = `/exemplar/cadastro?idLivro=${idLivro}`;
    });

    $("#btnExcluirLivro").on("click", function () {
        const id = $(this).data("id");

        if (confirm("Deseja realmente excluir este livro?")) {
            const form = $("#formExcluirLivro");
            form.attr("action", `/livro/excluir/${id}`);
            form.submit();
        }
    });

    $(".btnEditarExemplar").on("click", function () {
        const id = $(this).data("id");
        location.href = `/exemplar/cadastro?idExemplar=${id}`;
    });

    $(".btnExcluirExemplar").on("click", function () {
        const id = $(this).data("id");

        if (confirm("Deseja realmente excluir este exemplar?")) {
            const form = $("#formExcluirExemplar");
            form.attr("action", `/exemplar/excluir/${id}`);
            form.submit();
        }
    });

    $("#btnInfoAutor").on("click", function () {
        const idAutor = $(this).data("id");
        location.href = `/infoautor?id=${idAutor}`;
    });

});
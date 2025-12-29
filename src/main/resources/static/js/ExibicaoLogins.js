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

    $("#btnCadastroLogin").on("click", function () {
        location.href = "/login/cadastro";
    });

    $(".btnExcluirLogin").on("click", function () {
        const id = $(this).data("id");

        if (confirm("Deseja realmente excluir este login?")) {

            const form = $("<form>", {
                method: "POST",
                action: "/login/excluir/" + id
            });

            $(document.body).append(form);
            form.submit();
        }
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

    $(".btnBuscarGlobal").on("click", function () {
        const busca = $("#inputBuscaLogin").val();
        location.href = "/login/exibicao?busca=" + encodeURIComponent(busca);
    });

});
$(document).ready(function () {

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

    $("#formCadastroLogin").on("submit", function (e) {

        const login = $("#loginUsuario").val().trim();
        const senha = $("#senhaUsuario").val().trim();
        const acesso = $("#acessoUsuario").val();

        if (!login || !senha || !acesso) {
            e.preventDefault();
            alert("Preencha todos os campos obrigatórios.");
            return;
        }

        if (senha.length < 8) {
            e.preventDefault();
            alert("A senha deve conter no mínimo 8 caracteres.");
            return;
        }

    });

});
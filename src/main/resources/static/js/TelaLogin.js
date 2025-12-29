$(document).ready(function () {

    $("form").on("submit", function (e) {

        const destino = e.originalEvent.submitter.getAttribute("formaction");

        // Se for continuar sem login, não valida
        if (destino && destino.includes("/sem-login")) {
            return true;
        }

        const login = $("#login").val().trim();
        const senha = $("#senha").val().trim();

        if (!login || !senha) {
            alert("Login e senha devem ser preenchidos.");
            e.preventDefault();
            return false;
        }

        return true;
    });

});